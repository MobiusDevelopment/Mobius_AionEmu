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

import org.apache.commons.lang.ArrayUtils;

import com.aionemu.gameserver.dataholders.DataManager;
import com.aionemu.gameserver.model.templates.item.ItemTemplate;
import com.aionemu.gameserver.model.templates.item.actions.CraftLearnAction;
import com.aionemu.gameserver.model.templates.item.actions.ItemActions;
import com.aionemu.gameserver.model.templates.recipe.RecipeTemplate;

/**
 * @author xTz
 */
public class BrokerRecipeFilter extends BrokerFilter
{
	
	private final int craftSkillId;
	private final int[] masks;
	
	public BrokerRecipeFilter(int craftSkillId, int... masks)
	{
		this.craftSkillId = craftSkillId;
		this.masks = masks;
	}
	
	@Override
	public boolean accept(ItemTemplate template)
	{
		final ItemActions actions = template.getActions();
		if (actions != null)
		{
			final CraftLearnAction craftAction = actions.getCraftLearnAction();
			if (craftAction != null)
			{
				final int id = craftAction.getRecipeId();
				final RecipeTemplate recipeTemplate = DataManager.RECIPE_DATA.getRecipeTemplateById(id);
				if ((recipeTemplate != null) && (recipeTemplate.getSkillid() == craftSkillId))
				{
					return ArrayUtils.contains(masks, template.getTemplateId() / 100000);
				}
			}
		}
		return false;
	}
}
