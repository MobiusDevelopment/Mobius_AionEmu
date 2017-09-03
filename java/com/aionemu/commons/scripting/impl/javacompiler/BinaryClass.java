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
package com.aionemu.commons.scripting.impl.javacompiler;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Writer;
import java.net.URI;

import javax.tools.JavaFileObject;

import com.sun.tools.javac.file.BaseFileObject;

/**
 * This class is just a hack to make javac compiler work with classes loaded by prevoius classloader. Also it's used as container for loaded class
 * @author SoulKeeper
 */
public class BinaryClass extends BaseFileObject
{
	/**
	 * ClassName
	 */
	private final String name;
	
	/**
	 * Class data will be written here
	 */
	private final ByteArrayOutputStream baos = new ByteArrayOutputStream();
	
	/**
	 * Locaded class will be set here
	 */
	private Class<?> definedClass;
	
	/**
	 * Constructor that accepts class name as parameter
	 * @param name class name
	 */
	protected BinaryClass(String name)
	{
		super(null);
		this.name = name;
	}
	
	/**
	 * Throws {@link UnsupportedOperationException}
	 * @return nothing
	 */
	@Override
	public URI toUri()
	{
		throw new UnsupportedOperationException();
	}
	
	/**
	 * Returns name of this class with ".class" suffix
	 * @return name of this class with ".class" suffix
	 * @deprecated
	 */
	@Deprecated
	@Override
	public String getName()
	{
		return name + ".class";
	}
	
	/**
	 * Creates new ByteArrayInputStream, it just wraps class binary data
	 * @return input stream for class data
	 */
	@Override
	public InputStream openInputStream()
	{
		return new ByteArrayInputStream(baos.toByteArray());
	}
	
	/**
	 * Opens ByteArrayOutputStream for class data
	 * @return output stream
	 */
	@Override
	public OutputStream openOutputStream()
	{
		return baos;
	}
	
	/**
	 * Throws {@link UnsupportedOperationException}
	 * @return nothing
	 */
	@Override
	public CharSequence getCharContent(boolean ignoreEncodingErrors)
	{
		throw new UnsupportedOperationException();
	}
	
	/**
	 * Throws {@link UnsupportedOperationException}
	 * @return nothing
	 */
	@Override
	public Writer openWriter()
	{
		throw new UnsupportedOperationException();
	}
	
	/**
	 * Unsupported operation, always reutrns 0
	 * @return 0
	 */
	@Override
	public long getLastModified()
	{
		return 0;
	}
	
	/**
	 * Unsupported operation, returns false
	 * @return false
	 */
	@Override
	public boolean delete()
	{
		return false;
	}
	
	/**
	 * Returns class name
	 * @param path doesn't matter
	 * @return class name
	 */
	@Override
	protected String inferBinaryName(Iterable<? extends File> path)
	{
		return name;
	}
	
	/**
	 * Returns true if {@link javax.tools.JavaFileObject.Kind#CLASS}
	 * @param simpleName doesn't matter
	 * @param kind kind to compare
	 * @return true if Kind is {@link javax.tools.JavaFileObject.Kind#CLASS}
	 */
	@Override
	public boolean isNameCompatible(String simpleName, JavaFileObject.Kind kind)
	{
		return JavaFileObject.Kind.CLASS.equals(kind);
	}
	
	/**
	 * Returns bytes of class
	 * @return bytes of class
	 */
	public byte[] getBytes()
	{
		return baos.toByteArray();
	}
	
	/**
	 * Returns class that was loaded from binary data of this object
	 * @return loaded class
	 */
	public Class<?> getDefinedClass()
	{
		return definedClass;
	}
	
	/**
	 * Sets class that was loaded by this object
	 * @param definedClass class that was loaded
	 */
	public void setDefinedClass(Class<?> definedClass)
	{
		this.definedClass = definedClass;
	}
	
	/*
	 * (non-Javadoc)
	 * @see javax.tools.JavaFileObject#getKind()
	 */
	@Override
	public JavaFileObject.Kind getKind()
	{
		return JavaFileObject.Kind.CLASS;
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.sun.tools.javac.file.BaseFileObject#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object arg0)
	{
		// TODO Auto-generated method stub
		return false;
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.sun.tools.javac.file.BaseFileObject#getShortName()
	 */
	@Override
	public String getShortName()
	{
		// TODO Auto-generated method stub
		return null;
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.sun.tools.javac.file.BaseFileObject#hashCode()
	 */
	@Override
	public int hashCode()
	{
		// TODO Auto-generated method stub
		return 0;
	}
}
