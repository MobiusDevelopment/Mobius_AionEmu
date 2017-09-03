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
package com.aionemu.commons.scripting;

import java.io.File;

/**
 * This interface reperesents common functionality list that should be available for any commpiler that is going to be used with scripting engine. For instance, groovy can be used, hoever it produces by far not the best bytecode so by default javac from sun is used.
 * @author SoulKeeper
 */
public interface ScriptCompiler
{
	/**
	 * Sets parent class loader for this compiler.<br>
	 * <br>
	 * <font color="red">Warning, for now only</font>
	 * @param classLoader ScriptClassLoader that will be used as parent
	 */
	void setParentClassLoader(ScriptClassLoader classLoader);
	
	/**
	 * List of jar files that are required for compilation
	 * @param files list of jar files
	 */
	void setLibraires(Iterable<File> files);
	
	/**
	 * Compiles single class that is represented as string
	 * @param className class name
	 * @param sourceCode class sourse code
	 * @return {@link com.aionemu.commons.scripting.CompilationResult}
	 */
	CompilationResult compile(String className, String sourceCode);
	
	/**
	 * Compiles classes that are represented as strings
	 * @param className class names
	 * @param sourceCode class sources
	 * @return {@link com.aionemu.commons.scripting.CompilationResult}
	 * @throws IllegalArgumentException if number of class names != number of sources
	 */
	CompilationResult compile(String[] className, String[] sourceCode) throws IllegalArgumentException;
	
	/**
	 * Compiles list of files
	 * @param compilationUnits list of files
	 * @return {@link com.aionemu.commons.scripting.CompilationResult}
	 */
	CompilationResult compile(Iterable<File> compilationUnits);
	
	/**
	 * Returns array of supported file types. This files will be threated as source files.
	 * @return array of supported file types.
	 */
	String[] getSupportedFileTypes();
}
