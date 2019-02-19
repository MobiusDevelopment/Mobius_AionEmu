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
package com.aionemu.gameserver.model.broker.filter;

import com.aionemu.gameserver.model.templates.item.ItemTemplate;

/**
 * @author ATracer
 */
public class BrokerMinMaxFilter extends BrokerFilter
{
	
	private final int min;
	private final int max;
	
	/**
	 * @param min
	 * @param max
	 */
	public BrokerMinMaxFilter(int min, int max)
	{
		this.min = min * 100000;
		this.max = max * 100000;
	}
	
	@Override
	public boolean accept(ItemTemplate template)
	{
		return (template.getTemplateId() >= min) && (template.getTemplateId() < max);
	}
	
}
