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
package com.aionemu.commons.utils.xml;

import java.io.StringReader;
import java.io.StringWriter;
import java.net.URL;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.validation.Schema;

import org.w3c.dom.Document;

public class JAXBUtil
{
	public static String serialize(Object obj)
	{
		try
		{
			final JAXBContext jc = JAXBContext.newInstance(obj.getClass());
			final Marshaller m = jc.createMarshaller();
			m.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
			m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			final StringWriter sw = new StringWriter();
			m.marshal(obj, sw);
			return sw.toString();
		}
		catch (final JAXBException e)
		{
			throw new RuntimeException("Failed to marshall object of class " + obj.getClass().getName(), e);
		}
	}
	
	public static Document serializeToDocument(Object obj)
	{
		final String s = serialize(obj);
		return XmlUtils.getDocument(s);
	}
	
	public static <T> T deserialize(String s, Class<T> clazz)
	{
		return deserialize(s, clazz, (Schema) null);
	}
	
	public static <T> T deserialize(String s, Class<T> clazz, URL schemaURL)
	{
		final Schema schema = XmlUtils.getSchema(schemaURL);
		return deserialize(s, clazz, schema);
	}
	
	public static <T> T deserialize(String s, Class<T> clazz, String schemaString)
	{
		final Schema schema = XmlUtils.getSchema(schemaString);
		return deserialize(s, clazz, schema);
	}
	
	public static <T> T deserialize(Document xml, Class<T> clazz, String schemaString)
	{
		final String xmlAsString = XmlUtils.getString(xml);
		return deserialize(xmlAsString, clazz, schemaString);
	}
	
	@SuppressWarnings("unchecked")
	public static <T> T deserialize(String s, Class<T> clazz, Schema schema)
	{
		try
		{
			final JAXBContext jc = JAXBContext.newInstance(clazz);
			final Unmarshaller u = jc.createUnmarshaller();
			u.setSchema(schema);
			return (T) u.unmarshal(new StringReader(s));
		}
		catch (final Exception e)
		{
			throw new RuntimeException("Failed to unmarshall class " + clazz.getName() + " from xml:\n " + s, e);
		}
	}
	
	public static String generateSchemma(Class<?>... classes)
	{
		try
		{
			final JAXBContext jc = JAXBContext.newInstance(classes);
			final StringSchemaOutputResolver ssor = new StringSchemaOutputResolver();
			jc.generateSchema(ssor);
			return ssor.getSchemma();
		}
		catch (final Exception e)
		{
			throw new RuntimeException("Failed to generate schemma", e);
		}
	}
}
