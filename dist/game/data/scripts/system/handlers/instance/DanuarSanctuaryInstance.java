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
package system.handlers.instance;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.aionemu.commons.utils.Rnd;
import com.aionemu.gameserver.instance.handlers.GeneralInstanceHandler;
import com.aionemu.gameserver.instance.handlers.InstanceID;
import com.aionemu.gameserver.model.EmotionType;
import com.aionemu.gameserver.model.Race;
import com.aionemu.gameserver.model.drop.DropItem;
import com.aionemu.gameserver.model.gameobjects.Creature;
import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.StaticDoor;
import com.aionemu.gameserver.model.gameobjects.VisibleObject;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.items.storage.Storage;
import com.aionemu.gameserver.model.templates.spawns.SpawnTemplate;
import com.aionemu.gameserver.network.aion.serverpackets.SM_DIE;
import com.aionemu.gameserver.network.aion.serverpackets.SM_EMOTION;
import com.aionemu.gameserver.network.aion.serverpackets.SM_PLAY_MOVIE;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import com.aionemu.gameserver.services.drop.DropRegistrationService;
import com.aionemu.gameserver.services.item.ItemService;
import com.aionemu.gameserver.spawnengine.SpawnEngine;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.ThreadPoolManager;
import com.aionemu.gameserver.world.WorldMapInstance;

import javolution.util.FastMap;

/**
 * Author Rinzler (Encom) パーティーを3つのグループに分けて、作戦を遂行せよ！ IDに入場すると、A,B,Cの3つの入口があり、パーティーメンバーはそのいずれかを選択し、それぞれ異なる開始地点に移動する。 開始地点が違うだけでなく、モンスターの構成と戦闘パターンが全く異なるため、パーティーメンバーを慎重に配置しなければならない。 背景ストーリー ルーン族高位層の墓地であり、地下に設計された。当時の権威を象徴する場所であったため、貴重なルーン族の遺産が多 量に埋蔵されてある。その遺産を守るために複雑な迷路が建設された。この遺産には金銀財宝だけでなくイドゲル活用法
 * が書かれている遺跡も含まれおり、ベリトラの軍特殊調査隊、そしてシューゴとシュラクで構成されたチール盗掘団はそ ういった情報をいち早く手に入れようとルーンの安息所に真っ先に足を踏み入れた。特殊調査隊はイドゲル活用法が書か れた遺跡を探すために、チール盗掘団はルーン族の金銀財宝を盗掘するために活発している。 一歩遅れてルーンの安息所 に来た天族のカタラム派遣隊と魔族のカタラム討伐軍はチール盗掘団のシューゴを買収して、この状況を把握することと なる。天族と魔族はイドゲル活用法が書かれているルーン族の遺跡を死守するためにルーンの安息所に入っていく。
 * -開始点のキーボックスを壊すと3種類のキーのいずれかを選択することになる。 -自分が担当することになっているコースに合わせてキーを選択しましょう​​。 -岐路に立って自分のコースの入り口に入ると、ダンジョン攻略が始まる。 神聖の墓地※Aコース 推奨人数 -レザー系1人 -クラスはそれほど重要でないが、潜伏モンスターを意識しながら進めるとより安全である。 マップの説明 -1番の開封装置を開けなければCコースの進行ができない。(Bコースの宝部屋が同時に開かれる) -鍵番(K)を倒すと、赤or青のキーがドロップされる。キーの色に応じて、ドアを開けて次に進もう。
 * -2番の閉まった門はBコースで開けなければならない。 -4番の開封装置を開けると、Bコースの最後のドアが開く。 -3番の宝部屋のドアは、Cコースにある開封装置を作動させなければ開けることができない。 -最後の5番のドアは、B,Cの共通地域にある開封装置を作動させなければ開けることができない。 攻略ポイント -いかに早く1番、4番の開封装置を作動させるかが攻略のポイントとなる。 -4番の開封装置を作動させた後は、5番のドアが開くまで休憩しても構わない。 知恵の墓地※Bコース 推奨人数 -タンカー+回復役+攻撃手の3人 -精鋭モンスターが多いため、大量出現に気を付けなければならない。
 * マップの説明 -3番のドアはCコースで開けなければならない。 -2番の開封装置を作動させないと、Aコースを進めることができない。 -鍵番(K)を倒すと、赤or青のキーがドロップされる。キーの色に応じて、次に進む。 -6番の開封装置を作動させれば、Cコースの最後のドアが開く。 -6番の開封装置が存在する部屋の中央にあるストーンを破壊すると、上昇気流が現れる。 -上昇気流(↑)を利用して部屋の2階にある大砲を作動させ、3階にあるドアを壊すと、次に進むことができる。 -最後の地点にある4番のドアは、Aコースで開封装置を作動させると開く。
 * -その後、BコースとCコースがクロスするチール盗掘団の密集地域に入ることになる。 攻略ポイント -強いモンスターが多数登場するため、大量出現を最小化するのが重要である。 -上昇気流の部屋で1人は大砲を操作し、残りの2人は3階に先に移動すると時間を短縮することができる。 推奨人数 -回復役+攻撃手の2人 -多数の一般モンスターと少数の精鋭モンスターが混ざって出現する。広域技が多いクラスであればよりやりやすい。 マップの説明 -3番の開封装置を作動させなければ、Bコースに進むことができない。 (Aコースの宝部屋が同時に開く) -1番の閉まった門は、Aコースで開けなければならない。
 * -鍵番（K）を倒せば、赤or青のキーがドロップされる。キーの色に応じて、次に進む。 -動く橋の地域にある大砲を操作し、ドアを破壊すると、次に進むことができる。 -動く橋の地域から転落すると、開始地点に戻ってしまう。格別な注意が必要である。 -6番のドアは、Bコースで開けることができる。 -その後、BコースとCコースがクロスするチール盗掘団の密集地域に入ることになる。 攻略ポイント -モンスターをいかに早く倒すかがポイントとなる。 -通路の随所に壊れやすいストーンが設置され、それによって封鎖されているため、無理な走りは禁物。 -動く橋地域ではムービングコントロールが重要となる。
 * 盗賊団密集地域※B＋Cコース -タンカー+回復役2人+攻撃手2人の5人による攻略 -BコースとCコースがクロスする地域で、多数のチール盗掘団が密集している。 マップの説明 -B、Cコースのメンバーが集まる場所。 -多数のチール盗掘団があり、中級ネームドモンスター3匹が配置されている。 -中級ネームド モンスターを攻撃すると、周囲にいるすべてのチール盗掘団がアド(大量出現)になる。要注意！ -鍵番である護衛組長「ヤタキン」を倒すと、衝車のエンジンをかけるキーを入手することができる。 -このキーで、横にある入口突破用の衝車を作動させれば、永遠の棲家につながる通路が開かれる。
 * -その後、5番の開封装置を作動させれば、Aコースにある最後のドアが開く。 攻略ポイント -モンスターの大量出現による全滅のリスクが高い場所。 -ネームドモンスターが大量出現しないように気を付けながら、周囲にいる一般モンスターを処置する必要がある。 ネームドモンスター 3種類の最後のネームドモンスターを攻略する場所。 永遠の棲家に進入すると、パーティーメンバー全員に会うことができる。 最後のネームドモンスターは、医務・斥候・参謀の3個体で、いずれもヒーロー級である。倒すと、ネームド別に異なるアイテムがドロップされる。 攻略ポイント
 * 1.ヒーロー級だが、スリープやリストレイン、フィアーなどで無能力化することができる。 2.参謀ネームドを眠らせた後、[医務>斥候>参謀]の順で倒そう。 入手アイテム 装備アイテム 最後のネームド モンスター3人組を倒せば、一定の確率でヒーロー級とユニーク級のアイテムを入手することができる。 各アイテムは、武器、防具、ヘルムで構成されており、コンクエスト シリーズと外見が同じである。 ・ノーブル ハイ ランク ルーン シリーズ ・ディヴァイン ハイ ランク ルーン シリーズ スネーク武器の製作材料 最後のネームドモンスターのうち、司祭系のモンスターの医務隊長タグヌを倒せば、
 * ミューテーション カタリウム武器の製作に必要な魔力の武器の破片を一定の確率で入手することができる。 その他の追加報酬 ダンジョン内にある重たい宝箱、壊れやすい壺、古代ルーン族の棺を開けると、 古代コインやイディアン、古代魔石などの追加の報酬アイテムが入手できる。 1.重たい宝箱：一定の確率で安息所の古代コイン袋、安息所のイディアン袋、レジェンド級の古代魔石がドロップされる。 2.壊れやすい壺：一定の確率で安息所の古代コイン袋がドロップされる。 3.古代ルーン族の棺：一定の確率で古代遺物と一般スティグマがドロップされる。
 * 4.護衛隊長ヤタキン：チール盗掘団の密集地域にある鍵番を倒せば、ユニーク級の古代魔石がドロップされる。 5.シュラク族モンスター：シュラク族モンスターを倒すと、一定の確率でスティール ローズ号からドロップされるスティール ローズ海賊団セットがドロップされる。
 **/
@InstanceID(301380000)
public class DanuarSanctuaryInstance extends GeneralInstanceHandler
{
	private Race spawnRace;
	private Map<Integer, StaticDoor> doors;
	private final List<Integer> movies = new ArrayList<>();
	private final FastMap<Integer, VisibleObject> objects = new FastMap<>();
	
	@Override
	public void onInstanceCreate(WorldMapInstance instance)
	{
		super.onInstanceCreate(instance);
		doors = instance.getDoors();
		spawnDanuarSanctuaryBoss();
	}
	
	@Override
	public void onEnterInstance(Player player)
	{
		super.onInstanceCreate(instance);
		// The Beritran Special Research Team commanders are nearing The Chamber of Ruin.
		sendMsgByRace(1401855, Race.PC_ALL, 300000);
		// The Beritran Special Research Team commanders have discovered The Chamber of Ruin.
		sendMsgByRace(1401856, Race.PC_ALL, 600000);
		// The Beritran Special Research Team commanders have entered The Chamber of Ruin.
		sendMsgByRace(1401857, Race.PC_ALL, 900000);
		// The Beritran Special Research Team commanders are collecting Danuar relics.
		sendMsgByRace(1401858, Race.PC_ALL, 1200000);
		// The Beritran Special Research Team commanders have departed with their treasures.
		sendMsgByRace(1401859, Race.PC_ALL, 1500000);
		// The Chir Grave Robbers are almost finished digging.
		sendMsgByRace(1401860, Race.PC_ALL, 1800000);
		// The Chir Grave Robbers have left.
		sendMsgByRace(1401861, Race.PC_ALL, 2100000);
		switch (player.getRace())
		{
			case ELYOS:
			{
				sendMovie(player, 910);
				break;
			}
			case ASMODIANS:
			{
				sendMovie(player, 911);
				break;
			}
		}
		if (spawnRace == null)
		{
			spawnRace = player.getRace();
			SpawnDanuarRace();
		}
	}
	
	private void SpawnDanuarRace()
	{
		final int danuarGuard1 = spawnRace == Race.ASMODIANS ? 233126 : 233129;
		final int danuarGuard2 = spawnRace == Race.ASMODIANS ? 233127 : 233130;
		final int danuarGuard3 = spawnRace == Race.ASMODIANS ? 233128 : 233131;
		spawn(danuarGuard1, 911.333f, 904.6127f, 284.5891f, (byte) 110);
		spawn(danuarGuard1, 917.35785f, 901.0081f, 284.5891f, (byte) 50);
		spawn(danuarGuard1, 1025.9675f, 474.7492f, 290.26837f, (byte) 0);
		spawn(danuarGuard1, 1033.9897f, 474.7517f, 290.26837f, (byte) 61);
		spawn(danuarGuard2, 1029.233f, 484.0199f, 290.52118f, (byte) 31);
		spawn(danuarGuard2, 978.1413f, 1337.8359f, 335.875f, (byte) 34);
		spawn(danuarGuard2, 1019.45715f, 1367.1343f, 337.25f, (byte) 52);
		spawn(danuarGuard2, 881.45166f, 892.719f, 284.55508f, (byte) 109);
		spawn(danuarGuard2, 885.13104f, 898.88446f, 284.50986f, (byte) 109);
		spawn(danuarGuard3, 1103.6545f, 439.36285f, 284.61642f, (byte) 66);
		spawn(danuarGuard3, 833.283f, 961.50146f, 304.86777f, (byte) 79);
		spawn(danuarGuard3, 824.21826f, 967.07446f, 304.86777f, (byte) 79);
		spawn(danuarGuard3, 932.1827f, 876.7008f, 305.45746f, (byte) 92);
		spawn(danuarGuard3, 949.92975f, 903.508f, 299.75253f, (byte) 93);
	}
	
	@Override
	public void onDropRegistered(Npc npc)
	{
		final Set<DropItem> dropItems = DropRegistrationService.getInstance().getCurrentDropMap().get(npc.getObjectId());
		final int npcId = npc.getNpcId();
		int index = dropItems.size() + 1;
		switch (npcId)
		{
			case 702658: // Abbey Box.
			{
				dropItems.add(DropRegistrationService.getInstance().regDropItem(1, 0, npcId, 188053579, 1)); // [Event] Abbey Bundle.
				break;
			}
			case 702659: // Noble Abbey Box.
			{
				dropItems.add(DropRegistrationService.getInstance().regDropItem(1, 0, npcId, 188053580, 1)); // [Event] Noble Abbey Bundle.
				break;
			}
			case 235600: // Shulack Mercenary Cannon Chief.
			{
				dropItems.add(DropRegistrationService.getInstance().regDropItem(1, 0, npcId, 186000254, 1)); // Seal Breaking Magic Cannonball.
				break;
			}
			case 235658: // Bodyguard Yatakin.
			{
				dropItems.add(DropRegistrationService.getInstance().regDropItem(1, 0, npcId, 185000174, 1)); // Sentry Post Of Eternity Key.
				break;
			}
			case 235624: // Warmage Suyaroka.
			case 235625: // Chief Medic Tagnu.
			case 235626: // Virulent Ukahim.
			{
				for (Player player : instance.getPlayersInside())
				{
					if (player.isOnline())
					{
						dropItems.add(DropRegistrationService.getInstance().regDropItem(index++, player.getObjectId(), npcId, 188053619, 1)); // Ancient Manastone Bundle.
						dropItems.add(DropRegistrationService.getInstance().regDropItem(index++, player.getObjectId(), npcId, 188053789, 1)); // Major Stigma Support Bundle.
						dropItems.add(DropRegistrationService.getInstance().regDropItem(index++, player.getObjectId(), npcId, 188052572, 1)); // Conquerer's Ancient Manastone Bundle.
						dropItems.add(DropRegistrationService.getInstance().regDropItem(index++, player.getObjectId(), npcId, 188053998, 1)); // Ornate Enchantment Stone Bundle.
						dropItems.add(DropRegistrationService.getInstance().regDropItem(index++, player.getObjectId(), npcId, 166500000, 1)); // Amplification Stone.
					}
					switch (Rnd.get(1, 2))
					{
						case 1:
						{
							dropItems.add(DropRegistrationService.getInstance().regDropItem(1, 0, npcId, 188053623, 1)); // Fire Dragon King's Weapon Bundle [Mythic].
							break;
						}
						case 2:
						{
							dropItems.add(DropRegistrationService.getInstance().regDropItem(1, 0, npcId, 188054244, 1)); // Dreaming Nether Water Dragon King's Weapon Chest [Mythic].
							break;
						}
					}
				}
				break;
			}
			case 233391: // Sanctuary Keybox.
			{
				// Be careful in your selection. The key cannot be changed once it is chosen.
				sendMsgByRace(1401946, Race.PC_ALL, 0);
				for (Player player : instance.getPlayersInside())
				{
					if (player.isOnline())
					{
						dropItems.add(DropRegistrationService.getInstance().regDropItem(index++, player.getObjectId(), npcId, 185000181, 1)); // The Catacombs Key.
						dropItems.add(DropRegistrationService.getInstance().regDropItem(index++, player.getObjectId(), npcId, 185000182, 1)); // The Crypts Key.
						dropItems.add(DropRegistrationService.getInstance().regDropItem(index++, player.getObjectId(), npcId, 185000183, 1)); // The Charnels Key.
					}
				}
				break;
			}
			case 233185: // Danuar Sanctuary Jar.
			case 233190: // Stone Treasure Box I.
			case 233191: // Stone Treasure Box II.
			case 233192: // Stone Treasure Box III.
			{
				switch (Rnd.get(1, 5))
				{
					case 1:
					{
						dropItems.add(DropRegistrationService.getInstance().regDropItem(1, 0, npcId, 169405254, 2)); // Earth Trace.
						break;
					}
					case 2:
					{
						dropItems.add(DropRegistrationService.getInstance().regDropItem(1, 0, npcId, 152012592, 2)); // Earth Scrap.
						break;
					}
					case 3:
					{
						dropItems.add(DropRegistrationService.getInstance().regDropItem(1, 0, npcId, 152012613, 2)); // Burning Vitality.
						break;
					}
					case 4:
					{
						dropItems.add(DropRegistrationService.getInstance().regDropItem(1, 0, npcId, 169405267, 2)); // Flame Vitality.
						break;
					}
					case 5:
					{
						dropItems.add(DropRegistrationService.getInstance().regDropItem(1, 0, npcId, 169405268, 2)); // Lightning Vitality.
						break;
					}
				}
				switch (Rnd.get(1, 12))
				{
					case 1:
					{
						dropItems.add(DropRegistrationService.getInstance().regDropItem(1, 0, npcId, 152012580, 2)); // Fire Mote.
						break;
					}
					case 2:
					{
						dropItems.add(DropRegistrationService.getInstance().regDropItem(1, 0, npcId, 152012581, 2)); // Fire Breath.
						break;
					}
					case 3:
					{
						dropItems.add(DropRegistrationService.getInstance().regDropItem(1, 0, npcId, 152012582, 2)); // Fire Fragment.
						break;
					}
					case 4:
					{
						dropItems.add(DropRegistrationService.getInstance().regDropItem(1, 0, npcId, 152012583, 2)); // Fire Source.
						break;
					}
					case 5:
					{
						dropItems.add(DropRegistrationService.getInstance().regDropItem(1, 0, npcId, 152012584, 2)); // Water Source.
						break;
					}
					case 6:
					{
						dropItems.add(DropRegistrationService.getInstance().regDropItem(1, 0, npcId, 152012585, 2)); // Wind Mote.
						break;
					}
					case 7:
					{
						dropItems.add(DropRegistrationService.getInstance().regDropItem(1, 0, npcId, 152012586, 2)); // Wind Breath.
						break;
					}
					case 8:
					{
						dropItems.add(DropRegistrationService.getInstance().regDropItem(1, 0, npcId, 152012587, 2)); // Wind Eternity.
						break;
					}
					case 9:
					{
						dropItems.add(DropRegistrationService.getInstance().regDropItem(1, 0, npcId, 152012589, 2)); // Wind Source.
						break;
					}
					case 10:
					{
						dropItems.add(DropRegistrationService.getInstance().regDropItem(1, 0, npcId, 152012588, 2)); // Wind Fragment.
						break;
					}
					case 11:
					{
						dropItems.add(DropRegistrationService.getInstance().regDropItem(1, 0, npcId, 152012590, 2)); // Wind Origin.
						break;
					}
					case 12:
					{
						dropItems.add(DropRegistrationService.getInstance().regDropItem(1, 0, npcId, 152012591, 2)); // Water Fragment.
						break;
					}
				}
				break;
			}
		}
	}
	
	@Override
	public void handleUseItemFinish(Player player, Npc npc)
	{
		switch (npc.getNpcId())
		{
			case 701859: // Metallic Mystic KeyStone.
			{
				if (player.getInventory().isFull())
				{
					sendMsgByRace(1390149, Race.PC_ALL, 0);
				}
				despawnNpc(npc);
				ItemService.addItem(player, 188052613, 1); // Sanctuary Treasure Crate.
				break;
			}
			case 701860: // Golden Mystic KeyStone.
			{
				if (player.getInventory().isFull())
				{
					sendMsgByRace(1390149, Race.PC_ALL, 0);
				}
				despawnNpc(npc);
				ItemService.addItem(player, 188052613, 1); // Sanctuary Treasure Crate.
				break;
			}
			case 701863: // Spherical Mystic KeyStone.
			{
				// A door has opened somewhere.
				sendMsgByRace(1401838, Race.PC_ALL, 0);
				break;
			}
			case 701864: // Pyramidal Mystic KeyStone.
			{
				// A heavy door has opened somewhere.
				sendMsgByRace(1401839, Race.PC_ALL, 0);
				break;
			}
		}
	}
	
	@Override
	public void onDie(Npc npc)
	{
		switch (npc.getObjectTemplate().getTemplateId())
		{
			case 233084: // Ancien Danuar Coffin.
			{
				despawnNpc(npc);
				switch (Rnd.get(1, 2))
				{
					case 1:
					{
						spawn(233085, npc.getX(), npc.getY(), npc.getZ(), npc.getHeading()); // Ancient Danuar Remains.
						break;
					}
					case 2:
					{
						break;
					}
				}
				break;
			}
			/**
			 * Attack the rocks to activate the updraft.
			 */
			case 233188: // Sturdy Boulder.
			{
				despawnNpc(npc);
				spawnInfernalBoulder();
				// An ascending air current is rising from the spot where the egg was.
				// You can fly vertically up by spreading your wings and riding the current.
				sendMsgByRace(1400477, Race.PC_ALL, 2000);
				break;
			}
			case 235624: // Warmage Suyaroka.
			case 235625: // Chief Medic Tagnu.
			case 235626: // Virulent Ukahim.
			{
				spawnAbbeyNobleBox();
				sendMsg("[Congratulation]: you finish <Danuar Sanctuary 4.8>");
				spawn(701876, 1057.1633f, 557.6902f, 284.73123f, (byte) 30); // Danuar Sanctuary Exit.
				break;
			}
		}
	}
	
	private void spawnAbbeyNobleBox()
	{
		switch (Rnd.get(1, 2))
		{
			case 1:
			{
				spawn(702658, 1053.4221f, 565.259f, 282.28778f, (byte) 19); // Abbey Box.
				break;
			}
			case 2:
			{
				spawn(702659, 1060.8652f, 565.46436f, 282.2873f, (byte) 41); // Noble Abbey Box.
				break;
			}
		}
	}
	
	private void spawnDanuarSanctuaryBoss()
	{
		switch (Rnd.get(1, 3))
		{
			case 1:
			{
				spawn(235624, 1056.5698f, 693.86584f, 282.0391f, (byte) 30); // Warmage Suyaroka.
				break;
			}
			case 2:
			{
				spawn(235625, 1045.4534f, 682.2679f, 282.0391f, (byte) 60); // Chief Medic Tagnu.
				break;
			}
			case 3:
			{
				spawn(235626, 1056.4889f, 670.9826f, 282.0391f, (byte) 91); // Virulent Ukahim.
				break;
			}
		}
	}
	
	private void spawnInfernalBoulder()
	{
		final SpawnTemplate sturdyInfernalBoulder = SpawnEngine.addNewSingleTimeSpawn(301380000, 233187, 906.1991f, 859.88177f, 278.64731f, (byte) 37);
		sturdyInfernalBoulder.setEntityId(1699);
		objects.put(233187, SpawnEngine.spawnObject(sturdyInfernalBoulder, instanceId));
	}
	
	public void removeItems(Player player)
	{
		final Storage storage = player.getInventory();
		storage.decreaseByItemId(185000181, storage.getItemCountByItemId(185000181)); // The Catacombs Key.
		storage.decreaseByItemId(185000182, storage.getItemCountByItemId(185000182)); // The Crypts Key.
		storage.decreaseByItemId(185000183, storage.getItemCountByItemId(185000183)); // The Charnels Key.
	}
	
	private void sendMsg(String str)
	{
		instance.doOnAllPlayers(player -> PacketSendUtility.sendMessage(player, str));
	}
	
	protected void sendMsgByRace(int msg, Race race, int time)
	{
		ThreadPoolManager.getInstance().schedule(() -> instance.doOnAllPlayers(player ->
		{
			if (player.getRace().equals(race) || race.equals(Race.PC_ALL))
			{
				PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(msg));
			}
		}), time);
	}
	
	private void sendMovie(Player player, int movie)
	{
		if (!movies.contains(movie))
		{
			movies.add(movie);
			PacketSendUtility.sendPacket(player, new SM_PLAY_MOVIE(0, movie));
		}
	}
	
	@Override
	public void onLeaveInstance(Player player)
	{
		removeItems(player);
	}
	
	@Override
	public void onPlayerLogOut(Player player)
	{
		removeItems(player);
	}
	
	private void despawnNpc(Npc npc)
	{
		if (npc != null)
		{
			npc.getController().onDelete();
		}
	}
	
	@Override
	public void onInstanceDestroy()
	{
		doors.clear();
		movies.clear();
	}
	
	@Override
	public boolean onDie(Player player, Creature lastAttacker)
	{
		PacketSendUtility.broadcastPacket(player, new SM_EMOTION(player, EmotionType.DIE, 0, player.equals(lastAttacker) ? 0 : lastAttacker.getObjectId()), true);
		PacketSendUtility.sendPacket(player, new SM_DIE(player.haveSelfRezEffect(), player.haveSelfRezItem(), 0, 8));
		return true;
	}
}