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
package com.aionemu.commons.objects.filter;

/**
 * This filter is used to combine a few ObjectFilters into one. Its acceptObject method returns true only if all filters, that were passed through constructor return true
 * @param <T>
 * @author Luno
 */
public class AndObjectFilter<T> implements ObjectFilter<T>
{
	/**
	 * All filters that are used when running acceptObject() method
	 */
	private final ObjectFilter<? super T>[] filters;
	
	/**
	 * Constructs new <tt>AndObjectFilter</tt> object, that uses given filters.
	 * @param filters
	 */
	@SafeVarargs
	public AndObjectFilter(ObjectFilter<? super T>... filters)
	{
		this.filters = filters;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean acceptObject(T object)
	{
		for (ObjectFilter<? super T> filter : filters)
		{
			if ((filter != null) && !filter.acceptObject(object))
			{
				return false;
			}
		}
		return true;
	}
}
