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
package com.aionemu.gameserver.ai2.poll;

/**
 * @author ATracer
 */
public class SimpleAIAnswer implements AIAnswer
{
	private final boolean answer;
	
	/**
	 * @param answer
	 */
	SimpleAIAnswer(boolean answer)
	{
		this.answer = answer;
	}
	
	@Override
	public boolean isPositive()
	{
		return answer;
	}
	
	@Override
	public Object getResult()
	{
		return answer;
	}
	
}
