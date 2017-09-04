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
package com.aionemu.gameserver.dataholders;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.aionemu.gameserver.model.templates.housing.LBox;
import com.sun.org.apache.xml.internal.serialize.OutputFormat;
import com.sun.org.apache.xml.internal.serialize.XMLSerializer;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "lboxes")
public class HouseScriptData
{
	private static final Logger log = LoggerFactory.getLogger(HouseScriptData.class);
	private static Marshaller marshaller;
	
	@XmlElement(name = "lbox", required = true)
	protected List<LBox> scriptData;
	
	@XmlTransient
	private final Map<Integer, LBox> defaultTemplates;
	
	public HouseScriptData()
	{
		defaultTemplates = new HashMap<>();
	}
	
	void afterUnmarshal(Unmarshaller u, Object parent)
	{
		for (LBox template : scriptData)
		{
			defaultTemplates.put(template.getId(), template);
		}
		scriptData.clear();
		scriptData = null;
	}
	
	public String createScript(int scriptId, int position, int iconId)
	{
		final LBox template = defaultTemplates.get(scriptId);
		final LBox result = (LBox) template.clone();
		result.setId(position);
		result.setIcon(iconId);
		final HouseScriptData fragment = new HouseScriptData();
		fragment.scriptData = new ArrayList<>();
		fragment.scriptData.add(result);
		final Writer writer = new StringWriter();
		try
		{
			marshaller.marshal(fragment, writer);
		}
		catch (JAXBException e)
		{
		}
		return XmlFormatter.format(writer.toString());
	}
	
	public int size()
	{
		return defaultTemplates.size();
	}
	
	static
	{
		final SchemaFactory sf = SchemaFactory.newInstance("http://www.w3.org/2001/XMLSchema");
		Schema schema = null;
		JAXBContext jc = null;
		try
		{
			schema = sf.newSchema(new File("./data/static_data/housing/scripts.xsd"));
			jc = JAXBContext.newInstance(new Class[]
			{
				HouseScriptData.class
			});
			marshaller = jc.createMarshaller();
			marshaller.setSchema(schema);
			marshaller.setProperty("jaxb.encoding", "UTF-8");
		}
		catch (Exception e)
		{
			log.error("Could not instantiate HouseScriptData : \n" + e);
		}
	}
	
	public static class XmlFormatter
	{
		private static final Logger log = LoggerFactory.getLogger(XmlFormatter.class);
		private static final DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		private static DocumentBuilder db;
		
		public static String format(String unformattedXml)
		{
			try
			{
				final Document document = parseXmlFile(unformattedXml);
				final OutputFormat format = new OutputFormat(document);
				format.setIndenting(true);
				format.setIndent(2);
				format.setEncoding("UTF-8");
				final Writer out = new StringWriter();
				final XMLSerializer serializer = new XMLSerializer(out, format);
				serializer.serialize(document);
				return out.toString();
			}
			catch (IOException e)
			{
			}
			return null;
		}
		
		private static Document parseXmlFile(String in)
		{
			try
			{
				final InputSource is = new InputSource(new StringReader(in));
				return db.parse(is);
			}
			catch (SAXException e)
			{
				throw new RuntimeException(e);
			}
			catch (IOException e)
			{
				throw new RuntimeException(e);
			}
		}
		
		static
		{
			try
			{
				db = dbf.newDocumentBuilder();
			}
			catch (ParserConfigurationException e)
			{
				log.error("Could not instantiate XmlFormatter : \n" + e);
			}
		}
	}
}