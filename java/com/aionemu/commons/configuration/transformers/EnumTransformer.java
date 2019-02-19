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
package com.aionemu.commons.configuration.transformers;

import java.lang.reflect.Field;

import com.aionemu.commons.configuration.PropertyTransformer;
import com.aionemu.commons.configuration.TransformationException;

/**
 * Transforms enum string representation to enum. String must match case definition of enum, for instance:
 * <p/>
 * < pre> enum{ FILE, URL }
 * </pre>
 * <p/>
 * will be parsed with string "FILE" but not "file".
 * @author SoulKeeper
 */
public class EnumTransformer implements PropertyTransformer<Enum<?>>
{
	/**
	 * Shared instance of this transformer. It's thread-safe so no need of multiple instances
	 */
	public static final EnumTransformer SHARED_INSTANCE = new EnumTransformer();
	
	/**
	 * Trnasforms string to enum
	 * @param value value that will be transformed
	 * @param field value will be assigned to this field
	 * @return Enum object representing the value
	 * @throws TransformationException if somehting went wrong
	 */
	@Override
	@SuppressWarnings("unchecked")
	public Enum<?> transform(String value, Field field) throws TransformationException
	{
		@SuppressWarnings("rawtypes")
		final Class<? extends Enum> clazz = (Class<? extends Enum>) field.getType();
		
		try
		{
			return Enum.valueOf(clazz, value);
		}
		catch (Exception e)
		{
			throw new TransformationException(e);
		}
	}
}
