/*
 * This file is part of the Aion-Emu project.
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.aionemu.gameserver.services;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aionemu.commons.database.DatabaseFactory;
import com.aionemu.gameserver.dataholders.MotionData;
import com.aionemu.gameserver.model.Gender;
import com.aionemu.gameserver.model.Race;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.templates.item.WeaponType;
import com.aionemu.gameserver.skillengine.model.Motion;
import com.aionemu.gameserver.skillengine.model.MotionTime;
import com.aionemu.gameserver.skillengine.model.SkillTemplate;
import com.aionemu.gameserver.skillengine.model.Times;
import com.aionemu.gameserver.skillengine.model.WeaponTypeWrapper;
import com.aionemu.gameserver.utils.PacketSendUtility;

import javolution.util.FastMap;

/**
 * @author kecimis
 */
public class MotionLoggingService
{
	
	private static Logger log = LoggerFactory.getLogger(MotionLoggingService.class);
	
	private final FastMap<String, MotionLog> motionsMap = new FastMap<String, MotionLog>().shared();
	
	private boolean advancedLog = false;
	
	private boolean started = false;
	
	public static MotionLoggingService getInstance()
	{
		return SingletonHolder.instance;
	}
	
	public void start()
	{
		if (started)
		{
			return;
		}
		started = true;
		// load data from sql
		loadFromSql();
	}
	
	public void logTime(Player player, SkillTemplate sk, int clientTime, double distance)
	{
		int currentAttackSpeed = 0;
		if (!started)
		{
			return;
		}
		if (sk == null)
		{
			return;
		}
		if (player.getEquipment().getMainHandWeaponType() == null)
		{
			return;
		}
		final Motion motion = sk.getMotion();
		if (motion == null)
		{
			return;
		}
		
		currentAttackSpeed = player.getGameStats().getAttackSpeed().getCurrent();
		
		final int skillId = sk.getSkillId();
		final WeaponType mainHandWeapon = player.getEquipment().getMainHandWeaponType();
		final WeaponType offHandWeapon = player.getEquipment().getOffHandWeaponType();
		final String motionName = motion.getName();
		// clientTime is send from client
		int baseTime = clientTime;// adjusted time
		
		if (motion.getInstantSkill())
		{
			PacketSendUtility.sendMessage(player, "Skill: " + skillId + " is instant");
			return;
		}
		else if (clientTime == 0)
		{
			PacketSendUtility.sendMessage(player, "ClientTime is 0 for skill: " + skillId);
			return;
		}
		if (motion.getName() == null)
		{
			return;
		}
		
		long ammoTime = 0;
		if (sk.getAmmoSpeed() != 0)
		{
			ammoTime = Math.round((distance / sk.getAmmoSpeed()) * 1000);// checked with client
		}
		
		// adjusting with ammospeed
		baseTime -= ammoTime;
		
		// adjust clientTime if play speed is not 100
		if (motion.getSpeed() != 100)
		{
			baseTime /= motion.getSpeed();
			baseTime *= 100;
		}
		
		// logging
		if (advancedLog)
		{
			PacketSendUtility.sendMessage(player, "skillId: " + sk.getSkillId() + " motionName: " + motionName);
			PacketSendUtility.sendMessage(player, "attackSpeed: " + currentAttackSpeed + " mainHand: " + mainHandWeapon.toString() + " isDual: " + (offHandWeapon != null));
			PacketSendUtility.sendMessage(player, "clientTime: " + clientTime + " baseTime: " + baseTime + " playSpeed: " + motion.getSpeed());
			PacketSendUtility.sendMessage(player, "ammoTime: " + ammoTime + " ammoSpeed: " + sk.getAmmoSpeed() + " distance: " + distance);
			PacketSendUtility.sendMessage(player, "-------------------");
		}
		else
		{
			PacketSendUtility.sendMessage(player, "motionName: " + motionName + " clientTime: " + clientTime + " baseTime: " + baseTime);
		}
		
		final Race race = player.getRace();
		final Gender gender = player.getGender();
		
		// create WeaponTypeWrapper
		final WeaponTypeWrapper weapon = new WeaponTypeWrapper(mainHandWeapon, offHandWeapon);
		// check if its present
		if (isPresent(motionName, weapon, skillId, currentAttackSpeed, race, gender))
		{
			log.info("motionName: " + motionName + " weapon: " + (offHandWeapon != null ? "dual" : mainHandWeapon.toString()) + " skillId: " + skillId + " currentAttackSpeed: " + currentAttackSpeed + "baseTime: " + baseTime + " storedTime: " + getTime(motionName, weapon, skillId, currentAttackSpeed, race, gender));
			PacketSendUtility.sendMessage(player, "Its already stored. storedTime: " + getTime(motionName, weapon, skillId, currentAttackSpeed, race, gender));
			return;
		}
		
		// addtime
		if (addTime(motionName, weapon, skillId, currentAttackSpeed, race, gender, baseTime))
		{
			PacketSendUtility.sendMessage(player, "BaseTime: " + baseTime + " for motion: " + motionName + " was added.");
		}
		else
		{
			PacketSendUtility.sendMessage(player, "Couldnt add baseTime: " + baseTime + " for motion: " + motionName + "!");
		}
		
	}
	
	public void createAnalyzeFiles()
	{
	}
	
	public void createFinalFile()
	{
		final MotionData motionData = new MotionData();
		final List<MotionTime> motionTimes = motionData.getMotionTimes();
		
		// create results
		final TreeMap<String, List<WeaponTime>> results = new TreeMap<>();
		for (Entry<String, MotionLog> entry : motionsMap.entrySet())
		{
			final WeaponTime weaponTimeAm = new WeaponTime(Race.ASMODIANS, Gender.MALE);
			final WeaponTime weaponTimeAf = new WeaponTime(Race.ASMODIANS, Gender.FEMALE);
			final WeaponTime weaponTimeEm = new WeaponTime(Race.ELYOS, Gender.MALE);
			final WeaponTime weaponTimeEf = new WeaponTime(Race.ELYOS, Gender.FEMALE);
			if (entry.getValue() == null)
			{
				continue;
			}
			// loop through weaponType
			for (Entry<WeaponTypeWrapper, List<SkillTime>> entry2 : entry.getValue().getMotionLog().entrySet())
			{
				final WeaponTypeWrapper weapon = entry2.getKey();
				
				if (entry2.getValue() == null)
				{
					continue;
				}
				for (SkillTime st : entry2.getValue())
				{
					switch (st.getRace())
					{
						case ASMODIANS:
						{
							if (st.getGender() == Gender.MALE)
							{
								weaponTimeAm.add(weapon, recalculate("base", weapon, st.getAttackSpeed(), st.getClientTime()));
							}
							else
							{
								weaponTimeAf.add(weapon, recalculate("base", weapon, st.getAttackSpeed(), st.getClientTime()));
							}
							break;
						}
						case ELYOS:
						{
							if (st.getGender() == Gender.MALE)
							{
								weaponTimeEm.add(weapon, recalculate("base", weapon, st.getAttackSpeed(), st.getClientTime()));
							}
							else
							{
								weaponTimeEf.add(weapon, recalculate("base", weapon, st.getAttackSpeed(), st.getClientTime()));
							}
							break;
						}
					}
				}
				final List<WeaponTime> weaponTimes = new ArrayList<>(4);
				weaponTimes.add(weaponTimeAm);
				weaponTimes.add(weaponTimeAf);
				weaponTimes.add(weaponTimeEm);
				weaponTimes.add(weaponTimeEf);
				// fill results
				results.put(entry.getKey(), weaponTimes);
			}
		}
		
		for (Entry<String, List<WeaponTime>> entry : results.entrySet())
		{
			final Set<WeaponTypeWrapper> listofWeapons = new TreeSet<>();
			listofWeapons.add(new WeaponTypeWrapper(WeaponType.BOOK_2H, null));
			listofWeapons.add(new WeaponTypeWrapper(WeaponType.BOW, null));
			listofWeapons.add(new WeaponTypeWrapper(WeaponType.DAGGER_1H, null));
			listofWeapons.add(new WeaponTypeWrapper(WeaponType.MACE_1H, null));
			listofWeapons.add(new WeaponTypeWrapper(WeaponType.ORB_2H, null));
			listofWeapons.add(new WeaponTypeWrapper(WeaponType.POLEARM_2H, null));
			listofWeapons.add(new WeaponTypeWrapper(WeaponType.STAFF_2H, null));
			listofWeapons.add(new WeaponTypeWrapper(WeaponType.SWORD_1H, null));
			listofWeapons.add(new WeaponTypeWrapper(WeaponType.SWORD_2H, null));
			listofWeapons.add(new WeaponTypeWrapper(WeaponType.SWORD_1H, WeaponType.SWORD_1H));
			// 4.3
			listofWeapons.add(new WeaponTypeWrapper(WeaponType.GUN_1H, null));
			listofWeapons.add(new WeaponTypeWrapper(WeaponType.CANNON_2H, null));
			listofWeapons.add(new WeaponTypeWrapper(WeaponType.HARP_2H, null));
			// 4.5
			listofWeapons.add(new WeaponTypeWrapper(WeaponType.KEYBLADE_2H, null));
			
			// create MotionTime
			final MotionTime motion = new MotionTime();
			motion.setName(entry.getKey());
			
			for (WeaponTime wt : entry.getValue())
			{
				// process values
				final TreeMap<WeaponTypeWrapper, Integer> map = wt.process();
				
				final StringBuilder sb = new StringBuilder();
				boolean first = true;
				// create time
				for (WeaponTypeWrapper weapon : listofWeapons)
				{
					if (first)
					{
						sb.append((map.containsKey(weapon) ? map.get(weapon) : "0"));
						first = false;
					}
					else
					{
						sb.append("," + (map.containsKey(weapon) ? map.get(weapon) : "0"));
					}
				}
				
				final Times times = new Times();
				times.setTimes(sb.toString());
				switch (wt.getRace())
				{
					case ASMODIANS:
					{
						if (wt.getGender() == Gender.MALE)
						{
							motion.setAm(times);
						}
						else
						{
							motion.setAf(times);
						}
						break;
					}
					case ELYOS:
					{
						if (wt.getGender() == Gender.MALE)
						{
							motion.setEm(times);
						}
						else
						{
							motion.setEf(times);
						}
						break;
					}
				}
			}
			
			motionTimes.add(motion);
		}
		
		// marshall the final xml file
		marshallFile(motionData, "data/static_data/skills/new_motion_times.xml");
	}
	
	public static void marshallFile(Object templates, String file)
	{
		try
		{
			final JAXBContext jaxbContext = JAXBContext.newInstance(templates.getClass());
			final Marshaller marshaller = jaxbContext.createMarshaller();
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, new Boolean(true));
			marshaller.marshal(templates, new FileOutputStream(file));
		}
		catch (JAXBException e)
		{
			e.printStackTrace();
		}
		catch (FileNotFoundException e)
		{
			e.printStackTrace();
		}
	}
	
	/**
	 * method used to recalculate time to base, cap or given attackspeed
	 * @param method
	 * @param weapon
	 * @param attackSpeed
	 * @param time
	 * @return
	 */
	private int recalculate(String method, WeaponTypeWrapper weapon, int attackSpeed, int time)
	{
		int finalTime = 0;
		final TreeMap<WeaponType, float[]> list = new TreeMap<>();
		
		final float mace1h[] =
		{
			750f,
			1500f
		};
		list.put(WeaponType.MACE_1H, mace1h);
		
		final float sword1h[] =
		{
			700f,
			1400f
		};
		list.put(WeaponType.SWORD_1H, sword1h);
		
		final float gun1h[] =
		{
			900f,
			1800f
		};
		list.put(WeaponType.GUN_1H, gun1h);
		
		final float staff2h[] =
		{
			1000f,
			2000f
		};
		list.put(WeaponType.STAFF_2H, staff2h);
		
		final float dagger1h[] =
		{
			600f,
			1200f
		};
		list.put(WeaponType.DAGGER_1H, dagger1h);
		
		final float book_orb[] =
		{
			1100f,
			2200f
		};
		list.put(WeaponType.BOOK_2H, book_orb);
		list.put(WeaponType.ORB_2H, book_orb);
		
		final float polearm_cannon[] =
		{
			1400f,
			2800f
		};
		list.put(WeaponType.POLEARM_2H, polearm_cannon);
		list.put(WeaponType.CANNON_2H, polearm_cannon);
		
		final float sword_bow_keyblade_harp[] =
		{
			1200f,
			2400f
		};
		list.put(WeaponType.BOW, sword_bow_keyblade_harp);
		list.put(WeaponType.SWORD_2H, sword_bow_keyblade_harp);
		list.put(WeaponType.HARP_2H, sword_bow_keyblade_harp);
		list.put(WeaponType.KEYBLADE_2H, sword_bow_keyblade_harp);
		
		float speed = 0;
		if (method.equalsIgnoreCase("base"))
		{
			speed = list.get(weapon.getMainHand())[1];
			if (weapon.getOffHand() != null)
			{
				speed += (list.get(weapon.getOffHand())[1] * 0.25);
			}
		}
		else if (method.equalsIgnoreCase("cap"))
		{
			speed = list.get(weapon.getMainHand())[0];
			if (weapon.getOffHand() != null)
			{
				speed += (list.get(weapon.getOffHand())[0] * 0.25);
			}
		}
		else
		{
			try
			{
				speed = Float.parseFloat(method);
			}
			catch (Exception e)
			{
				// log
			}
		}
		
		finalTime = Math.round(((float) time / (float) attackSpeed) * speed);
		
		return finalTime;
	}
	
	// save to sql
	public void saveToSql()
	{
		Connection con = null;
		
		final String INSERT_QUERY = "INSERT INTO skill_motions (motion_name, weapon_type, off_weapon_type, skill_id, attack_speed, race, gender, time) VALUES (?,?,?,?,?,?,?,?) ON DUPLICATE KEY UPDATE motion_name = ?";
		
		try
		{
			con = DatabaseFactory.getConnection();
			final PreparedStatement stmt = con.prepareStatement(INSERT_QUERY);
			for (Entry<String, MotionLog> entry : motionsMap.entrySet())
			{
				final String motionName = entry.getKey();
				// set motion_name
				stmt.setString(1, motionName);
				stmt.setString(9, motionName);
				if (entry.getValue() == null)
				{
					continue;
				}
				// loop through weaponType
				for (Entry<WeaponTypeWrapper, List<SkillTime>> entry2 : entry.getValue().getMotionLog().entrySet())
				{
					final String weaponType = (entry2.getKey().getMainHand() != null ? entry2.getKey().getMainHand().toString() : "null");
					final String offWeaponType = (entry2.getKey().getOffHand() != null ? entry2.getKey().getOffHand().toString() : "null");
					// set weapon_type
					stmt.setString(2, weaponType);
					stmt.setString(3, offWeaponType);
					
					if (entry2.getValue() == null)
					{
						continue;
					}
					// sort by skillId
					Collections.sort(entry2.getValue());
					for (SkillTime st : entry2.getValue())
					{
						stmt.setInt(4, st.getSkillId());
						stmt.setInt(5, st.getAttackSpeed());
						stmt.setString(6, st.getRace().toString());
						stmt.setString(7, st.getGender().toString());
						stmt.setInt(8, st.getClientTime());
						stmt.execute();
					}
				}
			}
			stmt.close();
		}
		catch (SQLException e)
		{
			log.error("MotionLoggingService", e);
		}
		finally
		{
			DatabaseFactory.close(con);
		}
	}
	
	// load from sql
	public void loadFromSql()
	{
		Connection con = null;
		
		final String SELECT_QUERY = "SELECT * FROM skill_motions";
		try
		{
			con = DatabaseFactory.getConnection();
			final PreparedStatement stmt = con.prepareStatement(SELECT_QUERY);
			
			final ResultSet resultSet = stmt.executeQuery();
			
			while (resultSet.next())
			{
				final String motionName = resultSet.getString("motion_name");
				final WeaponType mainHandWeapon = WeaponType.valueOf(resultSet.getString("weapon_type"));
				final WeaponType offHandWeapon = (resultSet.getString("off_weapon_type").contains("null") ? null : WeaponType.valueOf(resultSet.getString("off_weapon_type")));
				final int skillId = resultSet.getInt("skill_id");
				final int attackSpeed = resultSet.getInt("attack_speed");
				final int time = resultSet.getInt("time");
				final String sRace = resultSet.getString("race");
				final String sGender = resultSet.getString("gender");
				final WeaponTypeWrapper weapon = new WeaponTypeWrapper(mainHandWeapon, offHandWeapon);
				
				Race race = null;
				Gender gender = null;
				try
				{
					race = Race.valueOf(sRace);
					gender = Gender.valueOf(sGender);
				}
				catch (Exception e)
				{
					log.info("cant load gender or race for motion_name: " + motionName);
				}
				finally
				{
					addTime(motionName, weapon, skillId, attackSpeed, race, gender, time);
				}
			}
			resultSet.close();
			stmt.close();
		}
		catch (SQLException e)
		{
			log.error("MotionLoggingService", e);
		}
		finally
		{
			DatabaseFactory.close(con);
		}
	}
	
	public void reloadFromSql()
	{
		clearMotions();
		loadFromSql();
	}
	
	private void clearMotions()
	{
		for (MotionLog mLog : motionsMap.values())
		{
			mLog.getMotionLog().clear();
		}
		motionsMap.clear();
	}
	
	private boolean isPresent(String motionName, WeaponTypeWrapper weapon, int skillId, int currentAttackSpeed, Race race, Gender gender)
	{
		if (motionsMap.containsKey(motionName))
		{
			return motionsMap.get(motionName).isPresent(weapon, skillId, currentAttackSpeed, race, gender);
		}
		
		return false;
	}
	
	private int getTime(String motionName, WeaponTypeWrapper weapon, int skillId, int currentAttackSpeed, Race race, Gender gender)
	{
		if (motionsMap.containsKey(motionName))
		{
			return motionsMap.get(motionName).getTime(weapon, skillId, currentAttackSpeed, race, gender);
		}
		
		return 0;
	}
	
	public boolean addTime(String motionName, WeaponTypeWrapper weapon, int skillId, int currentAttackSpeed, Race race, Gender gender, int clientTime)
	{
		if (!motionsMap.containsKey(motionName))
		{
			final MotionLog motionLog = new MotionLog();
			final boolean result = motionLog.addSkillTime(weapon, new SkillTime(skillId, currentAttackSpeed, race, gender, clientTime));
			motionsMap.put(motionName, motionLog);
			return result;
		}
		else
		{
			return motionsMap.get(motionName).addSkillTime(weapon, new SkillTime(skillId, currentAttackSpeed, race, gender, clientTime));
		}
	}
	
	public void setAdvancedLog(boolean bol)
	{
		advancedLog = bol;
	}
	
	public boolean getAdvancedLog()
	{
		return advancedLog;
	}
	
	private MotionLoggingService()
	{
		log.info("MotionLoggingService started.");
	}
	
	@SuppressWarnings("synthetic-access")
	private static class SingletonHolder
	{
		protected static final MotionLoggingService instance = new MotionLoggingService();
	}
	
	private class MotionLog
	{
		private final FastMap<WeaponTypeWrapper, List<SkillTime>> motionsForWeapons = new FastMap<>();
		
		public FastMap<WeaponTypeWrapper, List<SkillTime>> getMotionLog()
		{
			return motionsForWeapons;
		}
		
		public boolean addSkillTime(WeaponTypeWrapper weapon, SkillTime skillTime)
		{
			if (motionsForWeapons.containsKey(weapon))
			{
				if (!motionsForWeapons.containsValue(skillTime))
				{
					motionsForWeapons.get(weapon).add(skillTime);
					return true;
				}
			}
			else
			{
				final List<SkillTime> list = new ArrayList<>();
				list.add(skillTime);
				motionsForWeapons.put(weapon, list);
				return true;
			}
			
			return false;
		}
		
		public int getTime(WeaponTypeWrapper weapon, int skillId, int currentAttackSpeed, Race race, Gender gender)
		{
			if (motionsForWeapons.containsKey(weapon))
			{
				for (SkillTime st : motionsForWeapons.get(weapon))
				{
					if ((st.getSkillId() == skillId) && (st.getAttackSpeed() == currentAttackSpeed) && (st.getRace() == race) && (st.getGender() == gender))
					{
						return st.getClientTime();
					}
				}
			}
			
			return 0;
		}
		
		public boolean isPresent(WeaponTypeWrapper weapon, int skillId, int currentAttackSpeed, Race race, Gender gender)
		{
			if (motionsForWeapons.containsKey(weapon))
			{
				for (SkillTime st : motionsForWeapons.get(weapon))
				{
					if ((st.getSkillId() == skillId) && (st.getAttackSpeed() == currentAttackSpeed) && (st.getRace() == race) && (st.getGender() == gender))
					{
						return true;
					}
				}
			}
			
			return false;
		}
	}
	
	private class SkillTime implements Comparable<SkillTime>
	{
		private final int skillId;
		private final int attackSpeed;
		private final int clientTime;
		private final Race race;
		private final Gender gender;
		
		public SkillTime(int skillId, int attackSpeed, Race race, Gender gender, int clientTime)
		{
			this.skillId = skillId;
			this.attackSpeed = attackSpeed;
			this.clientTime = clientTime;
			this.race = race;
			this.gender = gender;
		}
		
		@Override
		public int compareTo(SkillTime o)
		{
			if (skillId < o.getSkillId())
			{
				return -1;
			}
			else if (skillId > o.getSkillId())
			{
				return 1;
			}
			else
			{
				return 0;
			}
		}
		
		/*
		 * (non-Javadoc)
		 * @see java.lang.Object#hashCode()
		 */
		@Override
		public int hashCode()
		{
			final int prime = 31;
			int result = 1;
			result = (prime * result) + getOuterType().hashCode();
			result = (prime * result) + attackSpeed;
			result = (prime * result) + clientTime;
			result = (prime * result) + ((gender == null) ? 0 : gender.hashCode());
			result = (prime * result) + ((race == null) ? 0 : race.hashCode());
			result = (prime * result) + skillId;
			return result;
		}
		
		/*
		 * (non-Javadoc)
		 * @see java.lang.Object#equals(java.lang.Object)
		 */
		@Override
		public boolean equals(Object obj)
		{
			if (this == obj)
			{
				return true;
			}
			if (obj == null)
			{
				return false;
			}
			if (getClass() != obj.getClass())
			{
				return false;
			}
			final SkillTime other = (SkillTime) obj;
			if (!getOuterType().equals(other.getOuterType()))
			{
				return false;
			}
			if (attackSpeed != other.attackSpeed)
			{
				return false;
			}
			if (clientTime != other.clientTime)
			{
				return false;
			}
			if (gender != other.gender)
			{
				return false;
			}
			if (race != other.race)
			{
				return false;
			}
			if (skillId != other.skillId)
			{
				return false;
			}
			return true;
		}
		
		public int getSkillId()
		{
			return skillId;
		}
		
		public int getAttackSpeed()
		{
			return attackSpeed;
		}
		
		public int getClientTime()
		{
			return clientTime;
		}
		
		public Race getRace()
		{
			return race;
		}
		
		public Gender getGender()
		{
			return gender;
		}
		
		private MotionLoggingService getOuterType()
		{
			return MotionLoggingService.this;
		}
	}
	
	private class WeaponTime
	{
		private final TreeMap<WeaponTypeWrapper, List<Integer>> values = new TreeMap<>();
		private Race race;
		private Gender gender;
		
		public WeaponTime(Race race, Gender gender)
		{
			this.race = race;
			this.gender = gender;
		}
		
		/**
		 * @return the race
		 */
		public Race getRace()
		{
			return race;
		}
		
		/**
		 * @param race the race to set
		 */
		public void setRace(Race race)
		{
			this.race = race;
		}
		
		/**
		 * @return the gender
		 */
		public Gender getGender()
		{
			return gender;
		}
		
		/**
		 * @param gender the gender to set
		 */
		public void setGender(Gender gender)
		{
			this.gender = gender;
		}
		
		public void add(WeaponTypeWrapper weapon, int value)
		{
			if (values.containsKey(weapon))
			{
				values.get(weapon).add(value);
			}
			else
			{
				final List<Integer> list = new ArrayList<>();
				list.add(value);
				values.put(weapon, list);
			}
		}
		
		public TreeMap<WeaponTypeWrapper, Integer> process()
		{
			final TreeMap<WeaponTypeWrapper, Integer> weaponMap = new TreeMap<>();
			
			for (Entry<WeaponTypeWrapper, List<Integer>> entry2 : values.entrySet())
			{
				// logic to calculate one value per weaponType
				// count the element with the most occurencies
				int finalValue = 0;
				int maxFrequency = 0;
				int value = 0;
				int total = 0;
				for (Integer i : entry2.getValue())
				{
					total += i;
					if (calculateFrequency(entry2.getValue(), i) > maxFrequency)
					{
						maxFrequency = calculateFrequency(entry2.getValue(), i);
						value = i;
					}
				}
				log.info("maxFrequency: " + maxFrequency + " value: " + value + " size: " + entry2.getValue().size());
				// if frequency of given value is higher than 70% take it, otherwise do Arithmetic mean
				if (Math.round(entry2.getValue().size() * 0.7f) <= maxFrequency)
				{
					finalValue = value;
				}
				else
				{
					finalValue = total / entry2.getValue().size();
				}
				
				log.info("weaponTime.process() finalValue: " + finalValue);
				weaponMap.put(entry2.getKey(), finalValue);
			}
			
			return weaponMap;
		}
	}
	
	private int calculateFrequency(List<Integer> list, int value)
	{
		int frequency = 0;
		
		// 10% tolerance
		final int min = Math.round(value * 0.90f);
		final int max = Math.round(value * 1.1f);
		for (Integer i : list)
		{
			if (i == null)
			{
				continue;
			}
			if ((min <= value) && (max >= value))
			{
				frequency++;
			}
		}
		
		return frequency;
	}
}
