/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2011 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */

package org.geomajas.puregwt.client.map.gadget;

import org.geomajas.configuration.client.ClientMapInfo;
import org.geomajas.configuration.client.UnitType;
import org.geomajas.puregwt.client.map.MapGadget;
import org.geomajas.puregwt.client.map.ViewPort;
import org.geomajas.puregwt.client.map.gfx.VectorContainer;
import org.vaadin.gwtgraphics.client.shape.Path;
import org.vaadin.gwtgraphics.client.shape.Rectangle;
import org.vaadin.gwtgraphics.client.shape.Text;
import org.vaadin.gwtgraphics.client.shape.path.LineTo;
import org.vaadin.gwtgraphics.client.shape.path.MoveTo;

import com.google.gwt.i18n.client.NumberFormat;

/**
 * MapGadget implementation that shows a scale bar on the map.
 * 
 * @author Pieter De Graef
 */
public class ScalebarGadget implements MapGadget {

	private static final double METERS_IN_MILE = 1609.344d;

	private static final double METERS_IN_YARD = 0.9144d;

	private static final double FEET_IN_METER = 3.2808399d;

	private static final int MAX_SIZE_IN_PIXELS = 125;

	private int[] lengths = new int[] { 1, 2, 5, 10, 25, 50, 100, 250, 500, 750, 1000, 2000, 5000, 10000, 25000, 50000,
			75000, 100000, 250000, 500000, 750000, 1000000, 2000000, 5000000, 10000000 };

	// position in lengths array up to where to test for yards (larger values is for miles)
	private static final int YARD_STARTING_POINT = 11;

	private Rectangle backGround;

	private Path distanceMarker;

	private Text distance;

	private UnitType unitType;

	private double unitLength;

	private ViewPort viewPort;

	// -- for internal use, holds the last calculated best value
	private int widthInUnits;

	// -- for internal use, holds the last calculated best value
	private int widthInPixels;

	// -- for internal use, for UnitType.ENGLISH only
	private boolean widthInUnitsIsMiles;

	// ------------------------------------------------------------------------
	// Constructor:
	// ------------------------------------------------------------------------

	public ScalebarGadget(ClientMapInfo mapInfo) {
		this.unitType = mapInfo.getDisplayUnitType();
		this.unitLength = mapInfo.getUnitLength();
	}

	// ------------------------------------------------------------------------
	// MapGadget implementation:
	// ------------------------------------------------------------------------

	public void onDraw(ViewPort viewPort, VectorContainer container) {
		this.viewPort = viewPort;

		backGround = new Rectangle(0, viewPort.getMapHeight() - 22, 1, 22);
		backGround.setStrokeOpacity(0);
		backGround.setFillOpacity(0.65);
		container.add(backGround);

		distanceMarker = new Path(3, viewPort.getMapHeight() - 18);
		distanceMarker.lineRelativelyTo(0, 14);
		distanceMarker.lineRelativelyTo(92, 0);
		distanceMarker.lineRelativelyTo(0, -14);
		distanceMarker.setFillOpacity(0);
		container.add(distanceMarker);

		distance = new Text(8, viewPort.getMapHeight() - 8, "");
		distance.setFontSize(12);
		distance.setStrokeOpacity(0);
		distance.setFillColor("#000000");
		container.add(distance);

		onScale();
	}

	public void onTranslate() {
	}

	public void onScale() {
		calculateBestFit(viewPort.getScale());
		distance.setText(formatUnits(widthInUnits));
		backGround.setWidth(widthInPixels + 6);
		distanceMarker.setStep(2, new LineTo(true, widthInPixels, 0));
	}

	public void onResize() {
		backGround.setY(viewPort.getMapHeight() - 22);
		distanceMarker.setStep(0, new MoveTo(false, 3, viewPort.getMapHeight() - 18));
		distance.setY(viewPort.getMapHeight() - 8);
	}

	public void onDestroy() {
	}

	// ------------------------------------------------------------------------
	// Private methods:
	// ------------------------------------------------------------------------

	/**
	 * Find the rounded value (from the lengths array) which fits the closest into the maxSizeInPixels for the given
	 * scale.
	 * 
	 * @param scale
	 * @return closest fit in units (will be miles or yards for English, m for metric, unit for CRS)
	 */
	private void calculateBestFit(double scale) {
		int len = 0;
		long px = 0;
		if (UnitType.ENGLISH.equals(unitType)) {
			// try miles.
			for (int i = lengths.length - 1; i > -1; i--) {
				len = this.lengths[i];
				px = Math.round((len * scale / unitLength) * METERS_IN_MILE);
				if (px < MAX_SIZE_IN_PIXELS) {
					break;
				}
			}
			// try yards.
			if (px > MAX_SIZE_IN_PIXELS) {
				for (int i = YARD_STARTING_POINT; i > -1; i--) {
					len = this.lengths[i];
					px = Math.round((len * scale / unitLength) * METERS_IN_YARD);
					if (px < MAX_SIZE_IN_PIXELS) {
						break;
					}
				}
				widthInUnitsIsMiles = false;
			} else {
				widthInUnitsIsMiles = true;
			}
		} else if (UnitType.ENGLISH_FOOT.equals(unitType)) {
			 // try miles.
			for (int i = lengths.length - 1; i > -1; i--) {
				len = this.lengths[i];
				px = Math.round((len * scale / unitLength) * METERS_IN_MILE);
				if (px < MAX_SIZE_IN_PIXELS) {
					break;
				}
			}
			// try feet.
			if (px > MAX_SIZE_IN_PIXELS) {
				for (int i = YARD_STARTING_POINT; i > -1; i--) {
					len = this.lengths[i];
					px = Math.round((len * scale / unitLength) / FEET_IN_METER);
					if (px < MAX_SIZE_IN_PIXELS) {
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
				if (px < MAX_SIZE_IN_PIXELS) {
					break;
				}
			}
		}

		widthInUnits = len;
		widthInPixels = (int) px;
	}

	/**
	 * format to human readable string converting to unit type.
	 * 
	 * @param units
	 * @return
	 */
	private String formatUnits(int units) {
		switch (unitType) {
			case ENGLISH:
				return NumberFormat.getDecimalFormat().format(units) + (widthInUnitsIsMiles ? " mi" : " yd");
			case ENGLISH_FOOT:
				return NumberFormat.getDecimalFormat().format(units) + (widthInUnitsIsMiles ? " mi" : " ft");
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