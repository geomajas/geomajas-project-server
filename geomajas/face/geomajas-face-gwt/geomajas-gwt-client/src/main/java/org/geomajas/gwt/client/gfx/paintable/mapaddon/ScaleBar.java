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

package org.geomajas.gwt.client.gfx.paintable.mapaddon;

import org.geomajas.configuration.client.UnitType;
import org.geomajas.geometry.Coordinate;
import org.geomajas.gwt.client.gfx.PainterVisitor;
import org.geomajas.gwt.client.gfx.paintable.Rectangle;
import org.geomajas.gwt.client.gfx.paintable.Text;
import org.geomajas.gwt.client.gfx.style.FontStyle;
import org.geomajas.gwt.client.gfx.style.ShapeStyle;
import org.geomajas.gwt.client.spatial.Bbox;
import org.geomajas.gwt.client.widget.MapWidget;

import com.google.gwt.i18n.client.NumberFormat;

/**
 * <p>
 * Map add-on that displays a scalebar.
 * </p>
 * 
 * @author Kristof Heirwegh
 * @author Pieter De Graef
 */
public class ScaleBar extends MapAddon {

	private static final ShapeStyle STYLE_BACKGROUND = new ShapeStyle("white", 0.60f, "white", 0.30f, 1);

	private static final ShapeStyle STYLE_MARKER = new ShapeStyle("black", 1f, null, 0f, 0);

	private static final FontStyle STYLE_FONT = new FontStyle("#000000", 10, "Arial, Verdana", "normal", "normal");

	private static final double METERSINMILE = 1609.344d;

	private static final double METERSINYARD = 0.9144d;

	private static final int HORIZONTALPADDING = 3; // px

	private static final int VERTICALPADDING = 3; // px

	private static final int MARKERHEIGHT = 13; // px

	// max width this scalebar should be
	private static final int MAXSIZEINPIXELS = 125;

	private Rectangle backGround;

	private Rectangle leftMarker;

	private Rectangle rightMarker;

	private Text distance;

	private Rectangle bottomLine;

	private Rectangle dummy;

	private int[] lengths = new int[] { 1, 2, 5, 10, 25, 50, 100, 250, 500, 750, 1000, 2000, 5000, 10000, 25000, 50000,
			75000, 100000, 250000, 500000, 750000, 1000000, 2000000, 5000000, 10000000 };

	// position in lengths array up to where to test for yards (larger values is for miles)
	private int yardStartingPoint = 11;

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

	private MapWidget map;

	public ScaleBar(String id, MapWidget map) {
		super(id, MAXSIZEINPIXELS, MARKERHEIGHT + VERTICALPADDING * 2);
		this.map = map;
	}

	public void accept(PainterVisitor visitor, Bbox bounds, boolean recursive) {
		map.getGraphics().drawGroup(map.getMapModel().getScreenGroup(), this);

		// Draw a dummy at 0,0 so that Internet Explorer knows where coordinate 0,0 is. If this is not drawn, the text
		// will disappear, because the parent group will have coordinate 0,0 at the upper left corner of the union of
		// all the rectangles that are drawn here.
		map.getGraphics().drawRectangle(this, dummy.getId(), dummy.getBounds(), (ShapeStyle) dummy.getStyle());

		map.getGraphics().drawRectangle(this, backGround.getId(), backGround.getBounds(),
				(ShapeStyle) backGround.getStyle());
		map.getGraphics().drawRectangle(this, bottomLine.getId(), bottomLine.getBounds(),
				(ShapeStyle) bottomLine.getStyle());
		map.getGraphics().drawRectangle(this, leftMarker.getId(), leftMarker.getBounds(),
				(ShapeStyle) leftMarker.getStyle());
		map.getGraphics().drawRectangle(this, rightMarker.getId(), rightMarker.getBounds(),
				(ShapeStyle) rightMarker.getStyle());
		map.getGraphics().drawText(this, distance.getId(), distance.getContent(), distance.getPosition(),
				(FontStyle) distance.getStyle());
	}

	public void onDraw() {
	}

	public void onRemove() {
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

		backGround = new Rectangle((null == getId() ? "" : getId() + "-bg"));
		backGround.setBounds(new Bbox(0, 0, 5, MARKERHEIGHT + 2 * VERTICALPADDING));
		backGround.setStyle(STYLE_BACKGROUND);
		leftMarker = new Rectangle((null == getId() ? "" : getId() + "-lm"));
		leftMarker.setStyle(STYLE_MARKER);
		leftMarker.setBounds(new Bbox(0, 0, 1, MARKERHEIGHT));

		rightMarker = new Rectangle((null == getId() ? "" : getId() + "-rm"));
		rightMarker.setStyle(STYLE_MARKER);
		rightMarker.setBounds(new Bbox(0, 0, 1, MARKERHEIGHT));
		bottomLine = new Rectangle((null == getId() ? "" : getId() + "-bm"));
		bottomLine.setStyle(STYLE_MARKER);
		bottomLine.setBounds(new Bbox(0, 0, 0, 1));
		distance = new Text((null == getId() ? "" : getId() + "-text"));
		distance.setStyle(STYLE_FONT);

		dummy = new Rectangle(getId() + "-dummy");
		dummy.setStyle(new ShapeStyle("#FFFFFF", 0, "#FFFFFF", 0, 0));
		dummy.setBounds(new Bbox(0, 0, 1, 1));
	}

	// ----------------------------------------------------------

	public double getUnitLength() {
		return unitLength;
	}

	public UnitType getUnitType() {
		return unitType;
	}

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

	public void setMapSize(int mapWidth, int mapHeight) {
		super.setMapSize(mapWidth, mapHeight);
		if (null == unitType) {
			throw new IllegalStateException("Please initialize scalebar before using");
		}
		backGround.getBounds().setX(getUpperLeftCorner().getX());
		backGround.getBounds().setY(getUpperLeftCorner().getY());
		bottomLine.getBounds().setX(getUpperLeftCorner().getX() + HORIZONTALPADDING);
		bottomLine.getBounds().setY(getUpperLeftCorner().getY() + VERTICALPADDING + MARKERHEIGHT);
		leftMarker.getBounds().setX(getUpperLeftCorner().getX() + HORIZONTALPADDING);
		leftMarker.getBounds().setY(getUpperLeftCorner().getY() + VERTICALPADDING);
		rightMarker.getBounds().setY(getUpperLeftCorner().getY() + VERTICALPADDING);
		distance.setPosition(new Coordinate(getUpperLeftCorner().getX() + HORIZONTALPADDING + 6, getUpperLeftCorner()
				.getY()
				+ VERTICALPADDING));
		updatePaintables();
	}

	// ----------------------------------------------------------

	private void updatePaintables() {
		distance.setContent(formatUnits(widthInUnits));
		backGround.getBounds().setWidth(widthInPixels + 2 * HORIZONTALPADDING);
		rightMarker.getBounds().setX(leftMarker.getBounds().getX() + widthInPixels - 1);
		bottomLine.getBounds().setWidth(widthInPixels);
	}

	/**
	 * Find the rounded value (from the lengths array) which fits the closest into the maxSizeInPixels for the given
	 * scale.
	 * 
	 * @param scale
	 * @return closest fit in units (will be miles or yards for English, m for metric, unit for crs)
	 */
	public void calculateBestFit(double scale) {
		int len = 0;
		long px = 0;
		if (UnitType.ENGLISH.equals(unitType)) {
			// try miles
			for (int i = lengths.length - 1; i > -1; i--) {
				len = this.lengths[i];
				px = Math.round((len * scale / unitLength) * METERSINMILE);
				if (px < MAXSIZEINPIXELS) {
					break;
				}
			}
			// try yards
			if (px > MAXSIZEINPIXELS) {
				for (int i = yardStartingPoint; i > -1; i--) {
					len = this.lengths[i];
					px = Math.round((len * scale / unitLength) * METERSINYARD);
					if (px < MAXSIZEINPIXELS) {
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
				if (px < MAXSIZEINPIXELS) {
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
