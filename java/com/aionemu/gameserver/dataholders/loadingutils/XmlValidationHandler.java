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
package com.aionemu.gameserver.dataholders.loadingutils;

import javax.xml.bind.ValidationEvent;
import javax.xml.bind.ValidationEventHandler;
import javax.xml.bind.ValidationEventLocator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Rolandas
 */
public class XmlValidationHandler implements ValidationEventHandler
{
	
	private static final Logger log = LoggerFactory.getLogger(XmlValidationHandler.class);
	
	@Override
	public boolean handleEvent(ValidationEvent event)
	{
		if ((event.getSeverity() == ValidationEvent.FATAL_ERROR) || (event.getSeverity() == ValidationEvent.ERROR))
		{
			final ValidationEventLocator locator = event.getLocator();
			final String message = event.getMessage();
			final int line = locator.getLineNumber();
			final int column = locator.getColumnNumber();
			log.error("Error at [line=" + line + ", column=" + column + "]: " + message);
			throw new Error(event.getLinkedException());
		}
		return true;
	}
	
}
