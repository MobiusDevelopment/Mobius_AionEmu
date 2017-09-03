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
package com.aionemu.commons.callbacks;

/**
 * Basic callback class.<br>
 * Each enhanced method will call "beforeCall" and "afterCall" methods
 * @author SoulKeeper
 * @param <T>
 */
@SuppressWarnings("rawtypes")
public interface Callback<T>
{
	/**
	 * Method that is called before actual method is invoked.<br>
	 * <p/>
	 * Callback should return one of the following results:
	 * <ul>
	 * <li>{@link CallbackResult#newContinue()}</li>
	 * <li>{@link CallbackResult#newCallbackBlocker()}</li>
	 * <li>{@link CallbackResult#newFullBlocker(Object)}</li>
	 * </ul>
	 * <p/>
	 * if result is not {@link CallbackResult#newFullBlocker(Object)} then method will be executed normally. In other case {@link CallbackResult#getResult()} will be returned.
	 * @param obj on whom method should be invoked
	 * @param args method args
	 * @return see {@link CallbackResult}
	 */
	CallbackResult beforeCall(T obj, Object[] args);
	
	/**
	 * Method that is called after actual method call.<br>
	 * <p/>
	 * Callback should return one of the following results:
	 * <ul>
	 * <li>{@link CallbackResult#newContinue()}</li>
	 * <li>{@link CallbackResult#newCallbackBlocker()}</li>
	 * <li>{@link CallbackResult#newFullBlocker(Object)}</li>
	 * </ul>
	 * <p/>
	 * if result is not {@link CallbackResult#newFullBlocker(Object)} then mehodResult will return unmodified. In other case {@link CallbackResult#getResult()} will be returned.
	 * @param obj on whom method was called
	 * @param args method args
	 * @param methodResult result of method invocation
	 * @return see {@link CallbackResult}
	 */
	CallbackResult afterCall(T obj, Object[] args, Object methodResult);
	
	/**
	 * Returns base class that will be used as callback identificator.<br>
	 * {@link com.aionemu.commons.callbacks.metadata.ObjectCallback#value()} should contain class that will be invoked
	 * @return base class of callback
	 */
	Class<? extends Callback> getBaseClass();
}
