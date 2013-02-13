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

package org.geomajas.widget.utility.gwt.client.ribbon.map;

import org.geomajas.geometry.Coordinate;
import org.geomajas.gwt.client.controller.listener.Listener;
import org.geomajas.gwt.client.controller.listener.ListenerEvent;
import org.geomajas.gwt.client.widget.MapWidget;
import org.geomajas.widget.utility.common.client.ribbon.RibbonColumn;

import com.google.gwt.user.client.ui.Widget;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.layout.VLayout;

/**
 * Custom ribbon column implementation that prints the X and Y coordinates of the mouse pointer position on the map.
 * 
 * @author Pieter De Graef
 */
public class DisplayCoordinatesRibbonColumn extends VLayout implements RibbonColumn {

	public static final String IDENTIFIER = "DisplayCoordinatesRibbonColumn";
	
	private Label xLabel;

	private Label yLabel;

	private Listener listener;

	private MapWidget mapWidget;

	private String xText = "X";

	private String yText = "Y";

	public DisplayCoordinatesRibbonColumn(MapWidget mapWidget) {
		super(8);
		this.mapWidget = mapWidget;

		xLabel = new Label(xText + ":");
		xLabel.setSize("80px", "16px");
		addMember(xLabel);

		yLabel = new Label(yText + ":");
		yLabel.setSize("80px", "16px");
		addMember(yLabel);

		listener = new MyMapListener();
		mapWidget.addListener(listener);
	}

	public Widget asWidget() {
		return this;
	}

	public void setShowTitles(boolean showTitles) {
	}

	public boolean isShowTitles() {
		return false;
	}

	public void setTitleAlignment(TitleAlignment titleAlignment) {
	}

	public TitleAlignment getTitleAlignment() {
		return TitleAlignment.BOTTOM;
	}

	public void setButtonBaseStyle(String buttonBaseStyle) {
	}

	/**
	 * Can accept "X and "Y" text values to be printed out.
	 */
	public void configure(String key, String value) {
		if ("x".equalsIgnoreCase(key)) {
			xText = value;
		} else if ("y".equalsIgnoreCase(key)) {
			yText = value;
		}
	}

	// ------------------------------------------------------------------------
	// SmartGWT methods overrides:
	// ------------------------------------------------------------------------

	@Override
	protected void onDestroy() {
		mapWidget.removeListener(listener);
		super.onDestroy();
	}

	// ------------------------------------------------------------------------
	// Private classes:
	// ------------------------------------------------------------------------

	/**
	 * Private map listener that gets the world position of the mouse pointer and prints it out.
	 * 
	 * @author Pieter De Graef
	 */
	private class MyMapListener implements Listener {

		public void onMouseDown(ListenerEvent event) {
		}

		public void onMouseUp(ListenerEvent event) {
		}

		public void onMouseMove(ListenerEvent event) {
			Coordinate worldPosition = event.getWorldPosition();
			double x = ((double) Math.round(worldPosition.getX() * 1000)) / 1000;
			double y = ((double) Math.round(worldPosition.getY() * 1000)) / 1000;
			xLabel.setContents(xText + ": " + x);
			yLabel.setContents(yText + ": " + y);
		}

		public void onMouseOut(ListenerEvent event) {
		}

		public void onMouseOver(ListenerEvent event) {
		}

		public void onMouseWheel(ListenerEvent event) {
		}
	}

	public boolean isEnabled() {
		return !getDisabled();
	}

	public void setEnabled(boolean enabled) {
		setDisabled(!enabled);
	}
}