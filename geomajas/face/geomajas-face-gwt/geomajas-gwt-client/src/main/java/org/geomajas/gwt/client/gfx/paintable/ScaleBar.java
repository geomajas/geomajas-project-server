/*
 * This file is part of Geomajas, a component framework for building
 * rich Internet applications (RIA) with sophisticated capabilities for the
 * display, analysis and management of geographic information.
 * It is a building block that allows developers to add maps
 * and other geographic data capabilities to their web applications.
 *
 * Copyright 2008-2010 Geosparc, http://www.geosparc.com, Belgium
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.geomajas.gwt.client.gfx.paintable;

import org.geomajas.configuration.client.UnitType;
import org.geomajas.geometry.Coordinate;
import org.geomajas.gwt.client.gfx.style.FontStyle;
import org.geomajas.gwt.client.gfx.style.ShapeStyle;
import org.geomajas.gwt.client.spatial.Bbox;

import com.google.gwt.i18n.client.NumberFormat;

/**
 * Paints a Scalebar object
 * <p>
 * Scalebar needs to be initialized before you van use it
 * <p>
 * Call <code>adjustScale()</code> at least once so the Scalebar can shown some
 * sensible data.
 *
 * @author Kristof Heirwegh
 */
public class ScaleBar extends Composite {

	private static final ShapeStyle STYLE_BACKGROUND = new ShapeStyle("white", 0.60f, "white", 0.30f, 1);
	private static final ShapeStyle STYLE_MARKER = new ShapeStyle("black", 1f, null, 0f, 0);
	private static final FontStyle STYLE_FONT = new FontStyle("#000000", 10, "Courier New, Arial, Verdana", "normal",
			"normal");

	private static final double METERSINMILE = 1609.344d;
	private static final double METERSINYARD = 0.9144d;

	private int horizontalMargin = 3; // px
	private int verticalMargin = 3; // px
	private int markerHeight = 15; // px
	private Coordinate position = new Coordinate(0, 0);

	private Rectangle backGround;
	private Rectangle leftMarker;
	private Rectangle rightMarker;
	private Text distance;
	private Rectangle bottomLine;

	private int[] lengths = new int[] {1, 2, 5, 10, 25, 50, 100, 250, 500, 750, 1000, 2000, 5000, 10000, 25000, 50000,
			75000, 100000, 250000, 500000, 750000, 1000000, 2000000, 5000000, 10000000};

	// position in lengths array up to where to test for yards (larger values is for miles)
	private int yardStartingPoint = 11;

	// max width this scalebar should be
	private int maxSizeInPixels = 125;

	/**
	 * unitlength in meter
	 */
	private double unitLength;

	/**
	 * how should units be shown to the user
	 */
	private UnitType unitType;

	// -- for internal use, holds the last calculated best value
	private int widthInUnits;
	// -- for internal use, holds the last calculated best value
	private int widthInPixels;
	// -- for internal use, for UnitType.ENGLISH only
	private boolean widthInUnitsIsMiles;

	// --

	public ScaleBar() {
	}

	/**
	 * @param id
	 *            id of the paintable
	 */
	public ScaleBar(String id) {
		super(id);
	}

	/**
	 *
	 * @param unitType
	 *            how are units shown (english/metric/crs)
	 * @param unitLength
	 *            length of a unit in meter
	 * @param position
	 *            coordinate where scalebar should be drawn on the graphics context
	 */
	public void initialize(UnitType unitType, double unitLength, Coordinate position) {
		if (null == unitType) {
			throw new IllegalArgumentException("please provide a unitType");
		}

		this.unitType = unitType;
		this.unitLength = unitLength;

		backGround = new Rectangle((null == getGroupName() ? "" : getGroupName() + ".bg"));
		backGround.setBounds(new Bbox(0, 0, 5, markerHeight + 2 * verticalMargin));
		backGround.setStyle(STYLE_BACKGROUND);
		leftMarker = new Rectangle((null == getGroupName() ? "" : getGroupName() + ".lm"));
		leftMarker.setStyle(STYLE_MARKER);
		leftMarker.setBounds(new Bbox(0, 0, 1, markerHeight));

		rightMarker = new Rectangle((null == getGroupName() ? "" : getGroupName() + ".rm"));
		rightMarker.setStyle(STYLE_MARKER);
		rightMarker.setBounds(new Bbox(0, 0, 1, markerHeight));
		bottomLine = new Rectangle((null == getGroupName() ? "" : getGroupName() + ".bm"));
		bottomLine.setStyle(STYLE_MARKER);
		bottomLine.setBounds(new Bbox(0, 0, 0, 1));
		distance = new Text((null == getGroupName() ? "" : getGroupName() + ".di"));
		distance.setStyle(STYLE_FONT);

		addChild(backGround);
		addChild(bottomLine);
		addChild(leftMarker);
		addChild(rightMarker);
		addChild(distance);

		if (null != position) {
			setPosition(position);
		}
	}

	// ----------------------------------------------------------

	public double getUnitLength() {
		return unitLength;
	}

	public UnitType getUnitType() {
		return unitType;
	}

	public int getHorizontalMargin() {
		return horizontalMargin;
	}

	public void setHorizontalMargin(int horizontalMargin) {
		this.horizontalMargin = horizontalMargin;
	}

	public int getVerticalMargin() {
		return verticalMargin;
	}

	public void setVerticalMargin(int verticalMargin) {
		this.verticalMargin = verticalMargin;
	}

	public Coordinate getPosition() {
		return position;
	}

	public void setPosition(Coordinate position) {
		if (null == position) {
			throw new IllegalArgumentException("Please provide a position");
		}
		if (null == unitType) {
			throw new IllegalStateException("Please initialize scalebar before using");
		}
		this.position = position;
		backGround.getBounds().setX(position.getX());
		backGround.getBounds().setY(position.getY());
		bottomLine.getBounds().setX(position.getX() + horizontalMargin);
		bottomLine.getBounds().setY(position.getY() + verticalMargin + markerHeight);
		leftMarker.getBounds().setX(position.getX() + horizontalMargin);
		leftMarker.getBounds().setY(position.getY() + verticalMargin);
		rightMarker.getBounds().setY(position.getY() + verticalMargin);
		distance.setPosition(new Coordinate(position.getX() + horizontalMargin + 6, position.getY() + verticalMargin));
	}

	// ----------------------------------------------------------

	/**
	 * Adjust the scalebar to reflect the given scale.
	 *
	 * @param scale
	 */
	public void adjustScale(double scale) {
		if (null == unitType) {
			throw new IllegalStateException("Please initialize scalebar before using");
		}

		calculateBestFit(scale);
		updatePaintables();
	}

	private void updatePaintables() {
		distance.setContent(formatUnits(widthInUnits));
		backGround.getBounds().setWidth(widthInPixels + 2 * horizontalMargin);
		rightMarker.getBounds().setX(position.getX() + horizontalMargin + widthInPixels - 1);
		bottomLine.getBounds().setWidth(widthInPixels);
	}

	/**
	 * Find the rounded value (from the lengths array) which fits the closest
	 * into the maxSizeInPixels for the given scale.
	 *
	 * @param scale
	 * @return closest fit in units (will be miles or yards for English, m for
	 *         metric, unit for crs)
	 */
	public void calculateBestFit(double scale) {
		int len = 0;
		long px = 0;
		if (UnitType.ENGLISH.equals(unitType)) {
			// try miles
			for (int i = lengths.length - 1; i > -1; i--) {
				len = this.lengths[i];
				px = Math.round((len * scale / unitLength) * METERSINMILE);
				if (px < maxSizeInPixels) {
					break;
				}
			}
			// try yards
			if (px > maxSizeInPixels) {
				for (int i = yardStartingPoint; i > -1; i--) {
					len = this.lengths[i];
					px = Math.round((len * scale / unitLength) * METERSINYARD);
					if (px < maxSizeInPixels) {
						break;
					}
				}
				widthInUnitsIsMiles = false;
			} else {
				widthInUnitsIsMiles = true;
			}
		} else {
			for (int i = lengths.length - 1; i > -1; i--) {
				len = this.lengths[i];
				px = Math.round(len * scale / unitLength);
				if (px < maxSizeInPixels) {
					break;
				}
			}
		}

		widthInUnits = len;
		widthInPixels = (int) px;
	}

	public boolean getWidthInUnitsIsMiles() {
		return widthInUnitsIsMiles;
	}

	public int getWidthInUnits() {
		return widthInUnits;
	}

	public int getWidthInPixels() {
		return widthInPixels;
	}

	/**
	 * format to humanreadable string converting to unittype
	 *
	 * @param units
	 * @return
	 */
	String formatUnits(int units) {
		switch (unitType) {
			case ENGLISH:
				return NumberFormat.getDecimalFormat().format(units) + (widthInUnitsIsMiles ? " mi" : " yd");

			case METRIC:
				if (units < 10000) {
					return NumberFormat.getDecimalFormat().format(units) + " m";
				} else {
					return NumberFormat.getDecimalFormat().format((double) units / 1000) + " km";
				}

			case CRS:
				return NumberFormat.getDecimalFormat().format(units) + " u";

			default:
				return "??";
		}
	}
}
