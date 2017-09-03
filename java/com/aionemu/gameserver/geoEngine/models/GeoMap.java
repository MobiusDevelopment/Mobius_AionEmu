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
package com.aionemu.gameserver.geoEngine.models;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.aionemu.gameserver.configs.main.GeoDataConfig;
import com.aionemu.gameserver.geoEngine.bounding.BoundingBox;
import com.aionemu.gameserver.geoEngine.collision.CollisionIntention;
import com.aionemu.gameserver.geoEngine.collision.CollisionResult;
import com.aionemu.gameserver.geoEngine.collision.CollisionResults;
import com.aionemu.gameserver.geoEngine.math.Ray;
import com.aionemu.gameserver.geoEngine.math.Triangle;
import com.aionemu.gameserver.geoEngine.math.Vector3f;
import com.aionemu.gameserver.geoEngine.scene.Node;
import com.aionemu.gameserver.geoEngine.scene.Spatial;
import com.aionemu.gameserver.geoEngine.scene.mesh.DoorGeometry;

import javolution.util.FastMap;

/**
 * @author Mr. Poke
 */
public class GeoMap extends Node
{
	
	private short[] terrainData;
	private final List<BoundingBox> tmpBox = new ArrayList<>();
	private final Map<String, DoorGeometry> doors = new FastMap<>();
	
	/**
	 * @param name
	 */
	public GeoMap(String name, int worldSize)
	{
		setCollisionFlags((short) (CollisionIntention.ALL.getId() << 8));
		for (int x = 0; x < worldSize; x += 256)
		{
			for (int y = 0; y < worldSize; y += 256)
			{
				final Node geoNode = new Node("");
				geoNode.setCollisionFlags((short) (CollisionIntention.ALL.getId() << 8));
				tmpBox.add(new BoundingBox(new Vector3f(x, y, 0), new Vector3f(x + 256, y + 256, 4000)));
				super.attachChild(geoNode);
			}
		}
	}
	
	public String getDoorName(int worldId, String meshFile, float x, float y, float z)
	{
		if (!GeoDataConfig.GEO_DOORS_ENABLE)
		{
			return null;
		}
		final String mesh = meshFile.toUpperCase();
		final Vector3f templatePoint = new Vector3f(x, y, z);
		float distance = Float.MAX_VALUE;
		DoorGeometry foundDoor = null;
		for (Entry<String, DoorGeometry> door : doors.entrySet())
		{
			if (!(door.getKey().startsWith(Integer.toString(worldId)) && door.getKey().endsWith(mesh)))
			{
				continue;
			}
			final DoorGeometry checkDoor = doors.get(door.getKey());
			final float doorDistance = checkDoor.getWorldBound().distanceTo(templatePoint);
			if (distance > doorDistance)
			{
				distance = doorDistance;
				foundDoor = checkDoor;
			}
			if (checkDoor.getWorldBound().intersects(templatePoint))
			{
				foundDoor = checkDoor;
				break;
			}
		}
		if (foundDoor == null)
		{
			// log.warn("Could not find static door: " + worldId + " " + meshFile + " " + templatePoint);
			return null;
		}
		foundDoor.setFoundTemplate(true);
		// log.info("Static door " + worldId + " " + meshFile + " " + templatePoint + " matched " + foundDoor.getName() +
		// "; distance: " + distance);
		return foundDoor.getName();
	}
	
	public void setDoorState(int instanceId, String name, boolean isOpened)
	{
		final DoorGeometry door = doors.get(name);
		if (door != null)
		{
			door.setDoorState(instanceId, isOpened);
		}
	}
	
	/*
	 * (non-Javadoc)
	 * @see aionjHungary.geoEngine.scene.Node#attachChild(aionjHungary.geoEngine.scene.Spatial)
	 */
	@Override
	public int attachChild(Spatial child)
	{
		int i = 0;
		
		if (child instanceof DoorGeometry)
		{
			doors.put(child.getName(), (DoorGeometry) child);
		}
		
		for (Spatial spatial : getChildren())
		{
			if (tmpBox.get(i).intersects(child.getWorldBound()))
			{
				((Node) spatial).attachChild(child);
			}
			i++;
		}
		return 0;
	}
	
	/**
	 * @param terrainData The terrainData to set.
	 */
	public void setTerrainData(short[] terrainData)
	{
		this.terrainData = terrainData;
	}
	
	public float getZ(float x, float y)
	{
		final CollisionResults results = new CollisionResults(CollisionIntention.PHYSICAL.getId(), false, 1);
		final Vector3f pos = new Vector3f(x, y, 4000);
		final Vector3f dir = new Vector3f(x, y, 0);
		final Float limit = pos.distance(dir);
		dir.subtractLocal(pos).normalizeLocal();
		final Ray r = new Ray(pos, dir);
		r.setLimit(limit);
		collideWith(r, results);
		Vector3f terrain = null;
		if (terrainData.length == 1)
		{
			terrain = new Vector3f(x, y, terrainData[0] / 32f);
		}
		else
		{
			terrain = terraionCollision(x, y, r);
		}
		if (terrain != null)
		{
			final CollisionResult result = new CollisionResult(terrain, Math.max(0, Math.max(4000 - terrain.z, terrain.z)));
			results.addCollision(result);
		}
		if (results.size() == 0)
		{
			return 0;
		}
		return results.getClosestCollision().getContactPoint().z;
	}
	
	public float getZ(float x, float y, float z, int instanceId)
	{
		final CollisionResults results = new CollisionResults(CollisionIntention.PHYSICAL.getId(), false, instanceId);
		final Vector3f pos = new Vector3f(x, y, z + 2);
		final Vector3f dir = new Vector3f(x, y, z - 100);
		final Float limit = pos.distance(dir);
		dir.subtractLocal(pos).normalizeLocal();
		final Ray r = new Ray(pos, dir);
		r.setLimit(limit);
		collideWith(r, results);
		Vector3f terrain = null;
		if (terrainData.length == 1)
		{
			if (terrainData[0] != 0)
			{
				terrain = new Vector3f(x, y, terrainData[0] / 32f);
			}
		}
		else
		{
			terrain = terraionCollision(x, y, r);
		}
		if ((terrain != null) && (terrain.z > 0) && (terrain.z < (z + 2)))
		{
			final CollisionResult result = new CollisionResult(terrain, Math.abs((z - terrain.z) + 2));
			results.addCollision(result);
		}
		if (results.size() == 0)
		{
			return z;
		}
		return results.getClosestCollision().getContactPoint().z;
	}
	
	public Vector3f getClosestCollision(float x, float y, float z, float targetX, float targetY, float targetZ, boolean changeDirection, boolean fly, int instanceId, byte intentions)
	{
		float zChecked1 = 0;
		float zChecked2 = 0;
		if (!fly && changeDirection)
		{
			zChecked1 = z;
			z = getZ(x, y, z + 2, instanceId);
		}
		z += 1f;
		targetZ += 1f;
		final Vector3f start = new Vector3f(x, y, z);
		final Vector3f end = new Vector3f(targetX, targetY, targetZ);
		final Vector3f pos = new Vector3f(x, y, z);
		final Vector3f dir = new Vector3f(targetX, targetY, targetZ);
		
		final CollisionResults results = new CollisionResults(intentions, false, instanceId);
		
		final Float limit = pos.distance(dir);
		dir.subtractLocal(pos).normalizeLocal();
		final Ray r = new Ray(pos, dir);
		r.setLimit(limit);
		final Vector3f terrain = calculateTerrainCollision(start.x, start.y, start.z, end.x, end.y, end.z, r);
		if (terrain != null)
		{
			final CollisionResult result = new CollisionResult(terrain, terrain.distance(pos));
			results.addCollision(result);
		}
		
		collideWith(r, results);
		
		float geoZ = 0;
		if (results.size() == 0)
		{
			if (fly)
			{
				return end;
			}
			if ((zChecked1 > 0) && (targetX == x) && (targetY == y) && ((targetZ - 1f) == zChecked1))
			{
				geoZ = z - 1f;
			}
			else
			{
				zChecked2 = targetZ;
				geoZ = getZ(targetX, targetY, targetZ + 2, instanceId);
			}
			if (Math.abs(geoZ - targetZ) < start.distance(end))
			{
				return end.setZ(geoZ);
			}
			return start;
		}
		Vector3f contactPoint = results.getClosestCollision().getContactPoint();
		final float distance = results.getClosestCollision().getDistance();
		if (distance < 1)
		{
			return start;
		}
		// -1m
		contactPoint = contactPoint.subtract(dir);
		if (!fly && changeDirection)
		{
			if ((zChecked1 > 0) && (contactPoint.x == x) && (contactPoint.y == y) && (contactPoint.z == zChecked1))
			{
				contactPoint.z = z - 1f;
			}
			else if ((zChecked2 > 0) && (contactPoint.x == targetX) && (contactPoint.y == targetY) && (contactPoint.z == zChecked2))
			{
				contactPoint.z = geoZ;
			}
			else
			{
				contactPoint.z = getZ(contactPoint.x, contactPoint.y, contactPoint.z + 2, instanceId);
			}
		}
		if (!fly && (Math.abs(start.z - contactPoint.z) > distance))
		{
			return start;
		}
		
		return contactPoint;
	}
	
	public CollisionResults getCollisions(float x, float y, float z, float targetX, float targetY, float targetZ, boolean changeDirection, boolean fly, int instanceId, byte intentions)
	{
		if (!fly && changeDirection)
		{
			z = getZ(x, y, z + 2, instanceId);
		}
		z += 1f;
		targetZ += 1f;
		final Vector3f start = new Vector3f(x, y, z);
		final Vector3f end = new Vector3f(targetX, targetY, targetZ);
		final Vector3f pos = new Vector3f(x, y, z);
		final Vector3f dir = new Vector3f(targetX, targetY, targetZ);
		
		final CollisionResults results = new CollisionResults(intentions, false, instanceId);
		
		final Float limit = pos.distance(dir);
		dir.subtractLocal(pos).normalizeLocal();
		final Ray r = new Ray(pos, dir);
		r.setLimit(limit);
		final Vector3f terrain = calculateTerrainCollision(start.x, start.y, start.z, end.x, end.y, end.z, r);
		if (terrain != null)
		{
			final CollisionResult result = new CollisionResult(terrain, terrain.distance(pos));
			results.addCollision(result);
		}
		
		collideWith(r, results);
		return results;
	}
	
	/**
	 * @param z
	 * @param targetZ
	 */
	private Vector3f calculateTerrainCollision(float x, float y, float z, float targetX, float targetY, float targetZ, Ray ray)
	{
		
		final float x2 = targetX - x;
		final float y2 = targetY - y;
		final int intD = (int) Math.abs(ray.getLimit());
		
		for (float s = 0; s < intD; s += 2)
		{
			final float tempX = x + ((x2 * s) / ray.getLimit());
			final float tempY = y + ((y2 * s) / ray.getLimit());
			final Vector3f result = terraionCollision(tempX, tempY, ray);
			if (result != null)
			{
				return result;
			}
		}
		return null;
	}
	
	private Vector3f terraionCollision(float x, float y, Ray ray)
	{
		y /= 2f;
		x /= 2f;
		final int xInt = (int) x;
		final int yInt = (int) y;
		// p1-----p2
		// || ||
		// || ||
		// p3-----p4
		float p1, p2, p3, p4;
		if (terrainData.length == 1)
		{
			p1 = p2 = p3 = p4 = terrainData[0] / 32f;
		}
		else
		{
			final int size = (int) Math.sqrt(terrainData.length);
			try
			{
				p1 = terrainData[(yInt + (xInt * size))] / 32f;
				p2 = terrainData[((yInt + 1) + (xInt * size))] / 32f;
				p3 = terrainData[((yInt) + ((xInt + 1) * size))] / 32f;
				p4 = terrainData[((yInt + 1) + ((xInt + 1) * size))] / 32f;
			}
			catch (Exception e)
			{
				return null;
			}
		}
		final Vector3f result = new Vector3f();
		if ((p1 >= 0) && (p2 >= 0) && (p3 >= 0))
		{
			final Triangle tringle1 = new Triangle(new Vector3f(xInt * 2, yInt * 2, p1), new Vector3f(xInt * 2, (yInt + 1) * 2, p2), new Vector3f((xInt + 1) * 2, yInt * 2, p3));
			if (ray.intersectWhere(tringle1, result))
			{
				return result;
			}
		}
		if ((p4 >= 0) && (p2 >= 0) && (p3 >= 0))
		{
			final Triangle tringle2 = new Triangle(new Vector3f((xInt + 1) * 2, (yInt + 1) * 2, p4), new Vector3f(xInt * 2, (yInt + 1) * 2, p2), new Vector3f((xInt + 1) * 2, yInt * 2, p3));
			if (ray.intersectWhere(tringle2, result))
			{
				return result;
			}
		}
		return null;
	}
	
	public boolean canSee(float x, float y, float z, float targetX, float targetY, float targetZ, float limit, int instanceId)
	{
		targetZ += 1;
		z += 1;
		// Another fix can see in instances
		// if (getZ(targetX, targetY) > targetZ)
		// return false;
		
		final float x2 = x - targetX;
		final float y2 = y - targetY;
		final float distance = (float) Math.sqrt((x2 * x2) + (y2 * y2));
		if (distance > 80f)
		{
			return false;
		}
		final int intD = (int) Math.abs(distance);
		
		final Vector3f pos = new Vector3f(x, y, z);
		final Vector3f dir = new Vector3f(targetX, targetY, targetZ);
		dir.subtractLocal(pos).normalizeLocal();
		final Ray r = new Ray(pos, dir);
		r.setLimit(limit);
		for (float s = 2; s < intD; s += 2)
		{
			final float tempX = targetX + ((x2 * s) / distance);
			final float tempY = targetY + ((y2 * s) / distance);
			final Vector3f result = terraionCollision(tempX, tempY, r);
			if (result != null)
			{
				return false;
			}
		}
		final CollisionResults results = new CollisionResults((byte) (CollisionIntention.PHYSICAL.getId() | CollisionIntention.DOOR.getId()), false, instanceId);
		final int collisions = collideWith(r, results);
		return ((results.size() == 0) && (collisions == 0));
	}
	
	/*
	 * (non-Javadoc)
	 * @see aionjHungary.geoEngine.scene.Spatial#updateModelBound()
	 */
	@Override
	public void updateModelBound()
	{
		if (getChildren() != null)
		{
			final Iterator<Spatial> i = getChildren().iterator();
			while (i.hasNext())
			{
				final Spatial s = i.next();
				if ((s instanceof Node) && ((Node) s).getChildren().isEmpty())
				{
					i.remove();
				}
			}
		}
		super.updateModelBound();
	}
}
