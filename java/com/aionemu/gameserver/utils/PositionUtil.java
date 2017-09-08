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
package com.aionemu.gameserver.utils;

import com.aionemu.gameserver.model.gameobjects.VisibleObject;

/**
 * @author ATracer
 */
public class PositionUtil
{
	private static final float MAX_ANGLE_DIFF = 90f;
	
	public static boolean isBehindTarget(VisibleObject object1, VisibleObject object2)
	{
		final float angleObject1 = MathUtil.calculateAngleFrom(object1, object2);
		final float angleObject2 = MathUtil.convertHeadingToDegree(object2.getHeading());
		float angleDiff = angleObject1 - angleObject2;
		if (angleDiff <= (-360 + MAX_ANGLE_DIFF))
		{
			angleDiff += 360;
		}
		if (angleDiff >= (360 - MAX_ANGLE_DIFF))
		{
			angleDiff -= 360;
		}
		return Math.abs(angleDiff) <= MAX_ANGLE_DIFF;
	}
	
	public static boolean isInFrontOfTarget(VisibleObject object1, VisibleObject object2)
	{
		final float angleObject2 = MathUtil.calculateAngleFrom(object2, object1);
		final float angleObject1 = MathUtil.convertHeadingToDegree(object2.getHeading());
		float angleDiff = angleObject1 - angleObject2;
		if (angleDiff <= (-360 + MAX_ANGLE_DIFF))
		{
			angleDiff += 360;
		}
		if (angleDiff >= (360 - MAX_ANGLE_DIFF))
		{
			angleDiff -= 360;
		}
		return Math.abs(angleDiff) <= MAX_ANGLE_DIFF;
	}
	
	public static boolean isBehind(VisibleObject object1, VisibleObject object2)
	{
		float angle = MathUtil.convertHeadingToDegree(object1.getHeading()) + 90;
		if (angle >= 360)
		{
			angle -= 360;
		}
		final double radian = Math.toRadians(angle);
		final float x0 = object1.getX();
		final float y0 = object1.getY();
		final float x1 = (float) (Math.cos(radian) * 5) + x0;
		final float y1 = (float) (Math.sin(radian) * 5) + y0;
		final float xA = object2.getX();
		final float yA = object2.getY();
		final float temp = ((x1 - x0) * (yA - y0)) - ((y1 - y0) * (xA - x0));
		return temp > 0;
	}
	
	public static float getAngleToTarget(VisibleObject object1, VisibleObject object2)
	{
		float angleObject1 = MathUtil.convertHeadingToDegree(object1.getHeading()) - 180;
		if (angleObject1 < 0)
		{
			angleObject1 += 360;
		}
		final float angleObject2 = MathUtil.calculateAngleFrom(object1, object2);
		float angleDiff = angleObject1 - angleObject2 - 180;
		if (angleDiff < 0)
		{
			angleDiff += 360;
		}
		return angleDiff;
	}
	
	public static float getDirectionalBound(VisibleObject object1, VisibleObject object2, boolean inverseTarget)
	{
		float angle = 90 - (inverseTarget ? getAngleToTarget(object2, object1) : getAngleToTarget(object1, object2));
		if (angle < 0)
		{
			angle += 360;
		}
		final double radians = Math.toRadians(angle);
		final float x1 = (float) (object1.getX() + (object1.getObjectTemplate().getBoundRadius().getSide() * Math.cos(radians)));
		final float y1 = (float) (object1.getY() + (object1.getObjectTemplate().getBoundRadius().getFront() * Math.sin(radians)));
		final float x2 = (float) (object2.getX() + (object2.getObjectTemplate().getBoundRadius().getSide() * Math.cos(Math.PI + radians)));
		final float y2 = (float) (object2.getY() + (object2.getObjectTemplate().getBoundRadius().getFront() * Math.sin(Math.PI + radians)));
		final float bound1 = (float) MathUtil.getDistance(object1.getX(), object1.getY(), x1, y1);
		final float bound2 = (float) MathUtil.getDistance(object2.getX(), object2.getY(), x2, y2);
		return bound1 - bound2;
	}
	
	public static float getDirectionalBound(VisibleObject object1, VisibleObject object2)
	{
		return getDirectionalBound(object1, object2, false);
	}
	
	public static byte getMoveAwayHeading(VisibleObject fromObject, VisibleObject object)
	{
		final float angle = MathUtil.calculateAngleFrom(fromObject, object);
		final byte heading = MathUtil.convertDegreeToHeading(angle);
		return heading;
	}
}