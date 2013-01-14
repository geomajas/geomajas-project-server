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

package org.geomajas.puregwt.client.map.gadget;

import org.geomajas.configuration.client.ClientMapInfo;
import org.geomajas.configuration.client.UnitType;
import org.geomajas.puregwt.client.event.ViewPortChangedEvent;
import org.geomajas.puregwt.client.event.ViewPortChangedHandler;
import org.geomajas.puregwt.client.event.ViewPortScaledEvent;
import org.geomajas.puregwt.client.event.ViewPortTranslatedEvent;
import org.geomajas.puregwt.client.map.MapPresenter;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.DivElement;
import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.layout.client.Layout.Alignment;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Widget;

/**
 * MapGadget implementation that shows a scale bar on the map.
 * 
 * @author Pieter De Graef
 */
public class ScalebarGadget extends AbstractMapGadget {

	/**
	 * UI binder definition for the {@link ScalebarGadget} widget.
	 * 
	 * @author Pieter De Graef
	 */
	interface ScalebarGadgetUiBinder extends UiBinder<Widget, ScalebarGadget> {
	}

	private static final ScalebarGadgetUiBinder UI_BINDER = GWT.create(ScalebarGadgetUiBinder.class);

	private static final double METERS_IN_MILE = 1609.344d;

	private static final double METERS_IN_YARD = 0.9144d;

	private static final double FEET_IN_METER = 3.2808399d;

	private static final int MAX_SIZE_IN_PIXELS = 125;

	private int[] lengths = new int[] { 1, 2, 5, 10, 25, 50, 100, 250, 500, 750, 1000, 2000, 5000, 10000, 25000, 50000,
			75000, 100000, 250000, 500000, 750000, 1000000, 2000000, 5000000, 10000000 };

	// position in lengths array up to where to test for yards (larger values is for miles)
	private static final int YARD_STARTING_POINT = 11;

	private UnitType unitType;

	private double unitLength;

	// -- for internal use, holds the last calculated best value
	private int widthInUnits;

	// -- for internal use, holds the last calculated best value
	private int widthInPixels;

	// -- for internal use, for UnitType.ENGLISH only
	private boolean widthInUnitsIsMiles;

	private Widget layout;

	@UiField
	protected DivElement scaleBarElement;

	// ------------------------------------------------------------------------
	// Constructors:
	// ------------------------------------------------------------------------

	public ScalebarGadget(ClientMapInfo mapInfo) {
		setHorizontalAlignment(Alignment.BEGIN);
		setVerticalAlignment(Alignment.END);
		this.unitType = mapInfo.getDisplayUnitType();
		this.unitLength = mapInfo.getUnitLength();
	}

	// ------------------------------------------------------------------------
	// MapGadget implementation:
	// ------------------------------------------------------------------------

	public Widget asWidget() {
		if (layout == null) {
			buildGui();
		}
		return layout;
	}

	public void beforeDraw(MapPresenter mapPresenter) {
		super.beforeDraw(mapPresenter);
		mapPresenter.getEventBus().addViewPortChangedHandler(new ViewPortChangedHandler() {

			public void onViewPortTranslated(ViewPortTranslatedEvent event) {
			}

			public void onViewPortScaled(ViewPortScaledEvent event) {
				redrawScale();
			}

			public void onViewPortChanged(ViewPortChangedEvent event) {
				redrawScale();
			}
		});
	}

	// ------------------------------------------------------------------------
	// Private methods:
	// ------------------------------------------------------------------------

	private void buildGui() {
		layout = UI_BINDER.createAndBindUi(this);
		layout.getElement().getStyle().setPosition(Position.ABSOLUTE);
		layout.getElement().getStyle().setHeight(19, Unit.PX);
		redrawScale();
	}

	private void redrawScale() {
		calculateBestFit(mapPresenter.getViewPort().getScale());
		scaleBarElement.setInnerText(formatUnits(widthInUnits));
		scaleBarElement.getStyle().setWidth(widthInPixels, Unit.PX);
		layout.getElement().getStyle().setWidth(widthInPixels + 10, Unit.PX);
	}

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