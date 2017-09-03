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
package com.aionemu.gameserver.utils.xml;

import java.io.ByteArrayOutputStream;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

public final class CompressUtil
{
	
	public static String Decompress(byte[] bytes) throws Exception
	{
		final Inflater decompressor = new Inflater();
		decompressor.setInput(bytes);
		
		final ByteArrayOutputStream bos = new ByteArrayOutputStream(bytes.length);
		
		final byte[] buffer = new byte[1024];
		try
		{
			while (true)
			{
				final int count = decompressor.inflate(buffer);
				if (count > 0)
				{
					bos.write(buffer, 0, count);
				}
				else
				{
					if ((count == 0) && (decompressor.finished()))
					{
						break;
					}
					throw new RuntimeException("Bad zip data, size: " + bytes.length);
				}
			}
		}
		finally
		{
			decompressor.end();
		}
		
		bos.close();
		return bos.toString("UTF-16LE");
	}
	
	public static byte[] Compress(String text) throws Exception
	{
		final Deflater compressor = new Deflater();
		final byte[] bytes = text.getBytes("UTF-16LE");
		compressor.setInput(bytes);
		
		final ByteArrayOutputStream bos = new ByteArrayOutputStream();
		compressor.finish();
		
		final byte[] buffer = new byte[1024];
		try
		{
			while (!compressor.finished())
			{
				final int count = compressor.deflate(buffer);
				bos.write(buffer, 0, count);
			}
		}
		finally
		{
			compressor.finish();
		}
		
		bos.close();
		return bos.toByteArray();
	}
}
