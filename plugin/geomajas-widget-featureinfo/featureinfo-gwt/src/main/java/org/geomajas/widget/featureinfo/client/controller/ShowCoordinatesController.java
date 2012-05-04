/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2012 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */

package org.geomajas.widget.featureinfo.client.controller;

import org.geomajas.geometry.Coordinate;
import org.geomajas.gwt.client.controller.AbstractGraphicsController;
import org.geomajas.gwt.client.widget.MapWidget;
import org.geomajas.widget.featureinfo.client.FeatureInfoMessages;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.MouseMoveEvent;
import com.google.gwt.i18n.client.NumberFormat;
import com.smartgwt.client.types.VerticalAlignment;
import com.smartgwt.client.widgets.Label;

/**
 * 
 * @author Kristof Heirwegh
 * 
 */
public class ShowCoordinatesController extends AbstractGraphicsController {

	private FeatureInfoMessages messages = GWT.create(FeatureInfoMessages.class);
	private final boolean showViewCoordinates; // = false;
	private boolean showWorldCoordinates = true;
	private CoordinatesLabel label;

	public ShowCoordinatesController(MapWidget mapWidget, boolean showWorldCoordinates, boolean showViewCoordinates) {
		super(mapWidget);
		this.showViewCoordinates = showViewCoordinates;
		this.showWorldCoordinates = showWorldCoordinates;
	}

	/** Create the context menu for this controller. */
	public void onActivate() {
		// show panel
		label = new CoordinatesLabel();
		label.animateMove(mapWidget.getWidth() - 150, 10);
	}

	/** Clean everything up. */
	public void onDeactivate() {
		// remove panel
		if (label != null) {
			label.destroy();
		}
	}

	@Override
	public void onMouseMove(MouseMoveEvent event) {
		Coordinate screenPosition = getScreenPosition(event);
		Coordinate worldPosition = getWorldPosition(event);
		label.setCoordinates(worldPosition.getX(), worldPosition.getY(), screenPosition.getX(), screenPosition.getY());
	}

	// -------------------------------------------------------------------------
	// Private classes:
	// -------------------------------------------------------------------------

	/**
	 * The label that shows the coordinates.
	 */
	private class CoordinatesLabel extends Label {

		private final NumberFormat viewformatter = NumberFormat.getFormat("#,##0");
		private final NumberFormat worldformatter = NumberFormat.getFormat("#,##0.000");
		private final boolean showBoth;

		public CoordinatesLabel() {
			super();
			setParentElement(mapWidget);
			setValign(VerticalAlignment.TOP);
			setShowEdges(true);
			setHeight(40);
			setPadding(3);

			setTop(-80);
			setBackgroundColor("#FFFFFF");
			setAnimateTime(500);
			showBoth = (showViewCoordinates && showWorldCoordinates);
			if (showBoth) {
				setWidth(150);
			} else {
				setWidth(120);
			}
			setLeft(mapWidget.getWidth() - (getWidth() + 10));
		}

		public void setCoordinates(double worldX, double worldY, double viewX, double viewY) {
			String world = "";
			String view = "";
			if (showViewCoordinates) {
				view = "<b>" + (showBoth ? messages.showCoordinatesViewX() : "X: ") + "</b>"
						+ viewformatter.format(viewX) + "<br/>";
				view += "<b>" + (showBoth ? messages.showCoordinatesViewY() : "Y: ") + "</b>"
						+ viewformatter.format(viewY) + "<br/>";
			}
			if (showWorldCoordinates) {
				world = "<b>" + (showBoth ? messages.showCoordinatesWorldX() : "X: ") + "</b>"
						+ worldformatter.format(worldX) + "<br />";
				world += "<b>" + (showBoth ? messages.showCoordinatesWorldY() : "Y: ") + "</b>"
						+ worldformatter.format(worldY) + "<br />";
			}

			String all = "<div>";
			if (showViewCoordinates) {
				all += view;
			}
			if (showBoth) {
				all += "</div><div style='margin-top:5px;'>";
			}
			if (showWorldCoordinates) {
				all += world;
			}
			all += "</div>";
			setContents(all);
		}
	}
}
