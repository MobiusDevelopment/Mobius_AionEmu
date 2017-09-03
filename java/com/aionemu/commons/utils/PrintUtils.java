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
package com.aionemu.commons.utils;

import java.nio.ByteBuffer;

public class PrintUtils
{
	public static void printSection(String sectionName)
	{
		final StringBuilder sb = new StringBuilder();
		sb.append("-[ " + sectionName + " ]");
		while (sb.length() < 79)
		{
			sb.insert(0, "=");
		}
		System.out.println(sb.toString());
	}
	
	public static byte[] hex2bytes(String string)
	{
		final String finalString = string.replaceAll("\\s+", "");
		final byte[] bytes = new byte[finalString.length() / 2];
		for (int i = 0; i < bytes.length; ++i)
		{
			bytes[i] = (byte) Integer.parseInt(finalString.substring(2 * i, (2 * i) + 2), 16);
		}
		return bytes;
	}
	
	public static String bytes2hex(byte[] bytes)
	{
		final StringBuilder result = new StringBuilder();
		for (final byte b : bytes)
		{
			final int value = b & 255;
			result.append(String.format("%02X", value));
		}
		return result.toString();
	}
	
	public static String toHex(ByteBuffer data)
	{
		final int position = data.position();
		final StringBuilder result = new StringBuilder();
		int counter = 0;
		while (data.hasRemaining())
		{
			if ((counter % 16) == 0)
			{
				result.append(String.format("%04X: ", counter));
			}
			final int b = data.get() & 255;
			result.append(String.format("%02X ", b));
			if ((++counter % 16) != 0)
			{
				continue;
			}
			result.append("  ");
			PrintUtils.toText(data, result, 16);
			result.append("\n");
		}
		final int rest = counter % 16;
		if (rest > 0)
		{
			for (int i = 0; i < (17 - rest); ++i)
			{
				result.append("   ");
			}
			PrintUtils.toText(data, result, rest);
		}
		data.position(position);
		return result.toString();
	}
	
	private static void toText(ByteBuffer data, StringBuilder result, int cnt)
	{
		int charPos = data.position() - cnt;
		for (int a = 0; a < cnt; ++a)
		{
			byte c = data.get(charPos++);
			if ((c > 31) && (c < 128))
			{
				result.append((char) c);
				continue;
			}
			result.append('.');
		}
	}
}
