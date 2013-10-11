/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2013 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
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

	private static final double METERS_IN_MILE = 1609.344d;

	private static final double METERS_IN_YARD = 0.9144d;

	private static final double FEET_IN_METER = 3.2808399d;

	private static final int HORIZONTAL_PADDING = 3; // px @todo WidgetLayout

	private static final int VERTICAL_PADDING = 3; // px @todo WidgetLayout

	private static final int MARKER_HEIGHT = 13; // px @todo WidgetLayout

	// max width this scale bar should be @todo WidgetLayout
	private static final int MAX_SIZE_IN_PIXELS = 125;

	private static final int[] LENGTHS = new int[] { 1, 2, 5, 10, 25, 50, 100, 250, 500, 750, 1000, 2000, 5000, 10000,
			25000, 50000, 75000, 100000, 250000, 500000, 750000, 1000000, 2000000, 5000000, 10000000 };

	// position in lengths array up to where to test for yards (larger values is for miles)
	private static final int YARD_STARTING_POINT = 11;

	private Rectangle backGround;

	private Rectangle leftMarker;

	private Rectangle rightMarker;

	private Text distance;

	private Rectangle bottomLine;

	private Rectangle dummy;

	/** Unit length in meter. */
	private double unitLength;

	/** How should units be shown to the user. */
	private UnitType unitType;

	// -- for internal use, holds the last calculated best value
	private int widthInUnits;

	// -- for internal use, holds the last calculated best value
	private int widthInPixels;

	// -- for internal use, for UnitType.ENGLISH only
	private boolean widthInUnitsIsMiles;

	private final MapWidget map;

	/**
	 * Construct a scale bar.
	 *
	 * @param id object id
	 * @param map map
	 */
	public ScaleBar(String id, MapWidget map) {
		super(id, MAX_SIZE_IN_PIXELS, MARKER_HEIGHT + VERTICAL_PADDING * 2);
		setRepaintOnMapViewChange(true);
		this.map = map;
	}

	@Override
	public void accept(PainterVisitor visitor, Object group, Bbox bounds, boolean recursive) {
		map.getVectorContext().drawGroup(group, this);
		adjustScale(map.getMapModel().getMapView().getCurrentScale());
		// Draw a dummy at 0,0 so that Internet Explorer knows where coordinate 0,0 is. If this is not drawn, the text
		// will disappear, because the parent group will have coordinate 0,0 at the upper left corner of the union of
		// all the rectangles that are drawn here.
		map.getVectorContext().drawRectangle(this, dummy.getId(), dummy.getBounds(), (ShapeStyle) dummy.getStyle());

		map.getVectorContext().drawRectangle(this, backGround.getId(), backGround.getBounds(),
				backGround.getStyle());
		map.getVectorContext().drawRectangle(this, bottomLine.getId(), bottomLine.getBounds(),
				bottomLine.getStyle());
		map.getVectorContext().drawRectangle(this, leftMarker.getId(), leftMarker.getBounds(),
				leftMarker.getStyle());
		map.getVectorContext().drawRectangle(this, rightMarker.getId(), rightMarker.getBounds(),
				rightMarker.getStyle());
		map.getVectorContext().drawText(this, distance.getId(), distance.getContent(), distance.getPosition(),
				distance.getStyle());
	}

	@Override
	public void onDraw() {
	}

	@Override
	public void onRemove() {
	}

	/**
	 * Initialize scale bar.
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
		backGround.setBounds(new Bbox(0, 0, 5, MARKER_HEIGHT + 2 * VERTICAL_PADDING));
		backGround.setStyle(STYLE_BACKGROUND);
		leftMarker = new Rectangle((null == getId() ? "" : getId() + "-lm"));
		leftMarker.setStyle(STYLE_MARKER);
		leftMarker.setBounds(new Bbox(0, 0, 1, MARKER_HEIGHT));

		rightMarker = new Rectangle((null == getId() ? "" : getId() + "-rm"));
		rightMarker.setStyle(STYLE_MARKER);
		rightMarker.setBounds(new Bbox(0, 0, 1, MARKER_HEIGHT));
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

	/**
	 * Get unit length.
	 *
	 * @return unit length
	 */
	public double getUnitLength() {
		return unitLength;
	}

	/**
	 * Get unit type.
	 *
	 * @return unit type
	 */
	public UnitType getUnitType() {
		return unitType;
	}

	/**
	 * Adjust the scale bar to reflect the given scale.
	 * 
	 * @param scale scale
	 */
	private void adjustScale(double scale) {
		if (null == unitType) {
			throw new IllegalStateException("Please initialize scalebar before using");
		}

		calculateBestFit(scale);
		updatePaintables();
	}

	/**
	 * Set map size.
	 *
	 * @param mapWidth map width
	 * @param mapHeight map height
	 */
	public void setMapSize(int mapWidth, int mapHeight) {
		super.setMapSize(mapWidth, mapHeight);
		if (null == unitType) {
			throw new IllegalStateException("Please initialize scale bar before using");
		}
		backGround.getBounds().setX(getUpperLeftCorner().getX());
		backGround.getBounds().setY(getUpperLeftCorner().getY());
		bottomLine.getBounds().setX(getUpperLeftCorner().getX() + HORIZONTAL_PADDING);
		bottomLine.getBounds().setY(getUpperLeftCorner().getY() + VERTICAL_PADDING + MARKER_HEIGHT);
		leftMarker.getBounds().setX(getUpperLeftCorner().getX() + HORIZONTAL_PADDING);
		leftMarker.getBounds().setY(getUpperLeftCorner().getY() + VERTICAL_PADDING);
		rightMarker.getBounds().setY(getUpperLeftCorner().getY() + VERTICAL_PADDING);
		distance.setPosition(new Coordinate(getUpperLeftCorner().getX() + HORIZONTAL_PADDING + 6,
				getUpperLeftCorner().getY() + VERTICAL_PADDING));
	}

	// ----------------------------------------------------------

	private void updatePaintables() {
		distance.setContent(formatUnits(widthInUnits));
		backGround.getBounds().setWidth(widthInPixels + 2 * HORIZONTAL_PADDING);
		rightMarker.getBounds().setX(leftMarker.getBounds().getX() + widthInPixels - 1);
		bottomLine.getBounds().setWidth(widthInPixels);
	}

	/**
	 * Set the rounded value (from the lengths array) which fits the closest into the maxSizeInPixels for the given
	 * scale.
	 * 
	 * @param scale scale
	 */
	public void calculateBestFit(double scale) {
		int len = 0;
		long px = 0;
		if (UnitType.ENGLISH.equals(unitType)) {
			// try miles
			for (int i = LENGTHS.length - 1; i > -1; i--) {
				len = LENGTHS[i];
				px = Math.round((len * scale / unitLength) * METERS_IN_MILE);
				if (px < MAX_SIZE_IN_PIXELS) {
					break;
				}
			}
			// try yards
			if (px > MAX_SIZE_IN_PIXELS) {
				for (int i = YARD_STARTING_POINT; i > -1; i--) {
					len = LENGTHS[i];
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
			// try miles
			for (int i = LENGTHS.length - 1; i > -1; i--) {
				len = LENGTHS[i];
				px = Math.round((len * scale / unitLength) * METERS_IN_MILE);
				if (px < MAX_SIZE_IN_PIXELS) {
					break;
				}
			}
			// try feet
			if (px > MAX_SIZE_IN_PIXELS) {
				for (int i = YARD_STARTING_POINT; i > -1; i--) {
					len = LENGTHS[i];
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
			for (int i = LENGTHS.length - 1; i > -1; i--) {
				len = LENGTHS[i];
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
	 * Is the unit width in miles? Provided for unit testing only.
	 *
	 * @return true when current unit is miles.
	 */
	protected boolean isUnitWidthInMiles() {
		return widthInUnitsIsMiles;
	}

	/**
	 * Get width in units.
	 *
	 * @return width in units
	 */
	public int getWidthInUnits() {
		return widthInUnits;
	}

	/**
	 * Get width in pixels.
	 *
	 * @return width in pixels
	 */
	public int getWidthInPixels() {
		return widthInPixels;
	}

	/**
	 * Convert distance (in CRS units) to human readable format (including unit indicator).
	 * 
	 * @param units unit
	 * @return human readable string in unit type
	 */
	String formatUnits(int units) {
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
				throw new IllegalStateException("Unknown unit type " + unitType);
		}
	}
}
