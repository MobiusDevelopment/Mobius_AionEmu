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
package com.aionemu.gameserver.model.curingzone;

import com.aionemu.gameserver.controllers.VisibleObjectController;
import com.aionemu.gameserver.model.gameobjects.VisibleObject;
import com.aionemu.gameserver.model.templates.curingzones.CuringTemplate;
import com.aionemu.gameserver.utils.idfactory.IDFactory;
import com.aionemu.gameserver.world.World;
import com.aionemu.gameserver.world.knownlist.NpcKnownList;

/**
 * @author xTz
 */
public class CuringObject extends VisibleObject
{
	private final CuringTemplate template;
	private final float range;
	
	@SuppressWarnings(
	{
		"rawtypes",
		"unchecked"
	})
	public CuringObject(CuringTemplate template, int instanceId)
	{
		super(IDFactory.getInstance().nextId(), new VisibleObjectController()
		{
		}, null, null, World.getInstance().createPosition(template.getMapId(), template.getX(), template.getY(), template.getZ(), (byte) 0, instanceId));
		
		this.template = template;
		range = template.getRange();
		setKnownlist(new NpcKnownList(this));
	}
	
	public CuringTemplate getTemplate()
	{
		return template;
	}
	
	@Override
	public String getName()
	{
		return "";
	}
	
	public float getRange()
	{
		return range;
	}
	
	public void spawn()
	{
		final World w = World.getInstance();
		w.storeObject(this);
		w.spawn(this);
	}
}
