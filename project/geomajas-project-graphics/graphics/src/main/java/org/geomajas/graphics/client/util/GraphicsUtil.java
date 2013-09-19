/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2013 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the Apache
 * License, Version 2.0. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */
package org.geomajas.graphics.client.util;

import org.geomajas.geometry.Bbox;
import org.geomajas.geometry.Coordinate;

/**
 * Utility class for some graphics operations.
 * 
 * @author Jan De Moerloose
 * 
 */
public final class GraphicsUtil {

	private GraphicsUtil() {

	}

	public static Bbox clone(Bbox bounds) {
		return new Bbox(bounds.getX(), bounds.getY(), bounds.getWidth(), bounds.getHeight());
	}

	public static Coordinate getPosition(Bbox bounds, BboxPosition position) {
		switch (position) {
			case CORNER_LL:
				return new Coordinate(bounds.getX(), bounds.getY());
			case CORNER_LR:
				return new Coordinate(bounds.getMaxX(), bounds.getY());
			case CORNER_UL:
				return new Coordinate(bounds.getX(), bounds.getMaxY());
			case CORNER_UR:
				return new Coordinate(bounds.getMaxX(), bounds.getMaxY());
			case MIDDLE_LEFT:
				return new Coordinate(bounds.getX(), 0.5 * (bounds.getY() + bounds.getMaxY()));
			case MIDDLE_LOW:
				return new Coordinate(0.5 * (bounds.getX() + bounds.getMaxX()), bounds.getY());
			case MIDDLE_RIGHT:
				return new Coordinate(bounds.getMaxX(), 0.5 * (bounds.getY() + bounds.getMaxY()));
			case MIDDLE_UP:
			default:
				return new Coordinate(0.5 * (bounds.getX() + bounds.getMaxX()), bounds.getMaxY());
		}
	}

	public static Bbox translatePosition(Bbox bounds, BboxPosition position, double dx, double dy) {
		switch (position) {
			case CORNER_LL:
				return toBbox(bounds.getX() + dx, bounds.getMaxX(), bounds.getY() + dy, bounds.getMaxY());
			case CORNER_LR:
				return toBbox(bounds.getX(), bounds.getMaxX() + dx, bounds.getY() + dy, bounds.getMaxY());
			case CORNER_UL:
				return toBbox(bounds.getX() + dx, bounds.getMaxX(), bounds.getY(), bounds.getMaxY() + dy);
			case CORNER_UR:
				return toBbox(bounds.getX(), bounds.getMaxX() + dx, bounds.getY(), bounds.getMaxY() + dy);
			case MIDDLE_LEFT:
				return toBbox(bounds.getX() + dx, bounds.getMaxX(), bounds.getY(), bounds.getMaxY());
			case MIDDLE_LOW:
				return toBbox(bounds.getX(), bounds.getMaxX(), bounds.getY() + dy, bounds.getMaxY());
			case MIDDLE_RIGHT:
				return toBbox(bounds.getX(), bounds.getMaxX() + dx, bounds.getY(), bounds.getMaxY());
			case MIDDLE_UP:
			default:
				return toBbox(bounds.getX(), bounds.getMaxX(), bounds.getY(), bounds.getMaxY() + dy);
		}
	}

	public static FlipState getFlipState(Bbox bounds, BboxPosition position, double dx, double dy) {
		switch (position) {
			case CORNER_LL:
				return toFlipState(bounds.getX() + dx, bounds.getMaxX(), bounds.getY() + dy, bounds.getMaxY());
			case CORNER_LR:
				return toFlipState(bounds.getX(), bounds.getMaxX() + dx, bounds.getY() + dy, bounds.getMaxY());
			case CORNER_UL:
				return toFlipState(bounds.getX() + dx, bounds.getMaxX(), bounds.getY(), bounds.getMaxY() + dy);
			case CORNER_UR:
				return toFlipState(bounds.getX(), bounds.getMaxX() + dx, bounds.getY(), bounds.getMaxY() + dy);
			case MIDDLE_LEFT:
				return toFlipState(bounds.getX() + dx, bounds.getMaxX(), bounds.getY(), bounds.getMaxY());
			case MIDDLE_LOW:
				return toFlipState(bounds.getX(), bounds.getMaxX(), bounds.getY() + dy, bounds.getMaxY());
			case MIDDLE_RIGHT:
				return toFlipState(bounds.getX(), bounds.getMaxX() + dx, bounds.getY(), bounds.getMaxY());
			case MIDDLE_UP:
			default:
				return toFlipState(bounds.getX(), bounds.getMaxX(), bounds.getY(), bounds.getMaxY() + dy);
		}
	}

	public static Bbox stretchToRatio(Bbox bounds, double ratio, BboxPosition type) {
		double newRatio = bounds.getWidth() / bounds.getHeight();
		boolean stretchHeight = newRatio > ratio;
		double minX = bounds.getX();
		double maxX = bounds.getMaxX();
		double minY = bounds.getY();
		double maxY = bounds.getMaxY();
		double centX = 0.5 * (minX + maxX);
		double centY = 0.5 * (minY + maxY);
		double width = ratio * bounds.getHeight();
		double height = bounds.getWidth() / ratio;
		switch (type) {
			case CORNER_LL:
				return stretchHeight ? toBbox(minX, maxX, maxY - height, maxY) : toBbox(maxX - width, maxX, minY, maxY);
			case CORNER_LR:
				return stretchHeight ? toBbox(minX, maxX, maxY - height, maxY) : toBbox(minX, minX + width, minY, maxY);
			case CORNER_UL:
				return stretchHeight ? toBbox(minX, maxX, minY, minY + height) : toBbox(maxX - width, maxX, minY, maxY);
			case CORNER_UR:
				return stretchHeight ? toBbox(minX, maxX, minY, minY + height) : toBbox(minX, minX + width, minY, maxY);
			case MIDDLE_LEFT:
				return toBbox(minX, maxX, centY - 0.5 * height, centY + 0.5 * height);
			case MIDDLE_LOW:
				return toBbox(centX - 0.5 * width, centX + 0.5 * width, minY, maxY);
			case MIDDLE_RIGHT:
				return toBbox(minX, maxX, centY - 0.5 * height, centY + 0.5 * height);
			case MIDDLE_UP:
			default:
				return toBbox(centX - 0.5 * width, centX + 0.5 * width, minY, maxY);
		}
	}

	private static Bbox toBbox(double x1, double x2, double y1, double y2) {
		return new Bbox(Math.min(x1, x2), Math.min(y1, y2), Math.abs(x1 - x2), Math.abs(y1 - y2));
	}

	private static FlipState toFlipState(double minX, double maxX, double minY, double maxY) {
		FlipState flipstate = FlipState.NONE;
		if (minX > maxX) {
			if (minY > maxY) {
				flipstate = FlipState.FLIP_XY;
			} else {
				flipstate = FlipState.FLIP_X;
			}
		} else if (minY > maxY) {
			flipstate = FlipState.FLIP_Y;
		}
		return flipstate;
	}
}
