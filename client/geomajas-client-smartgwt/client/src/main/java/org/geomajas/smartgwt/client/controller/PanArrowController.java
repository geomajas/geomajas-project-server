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

package org.geomajas.smartgwt.client.controller;

import org.geomajas.geometry.Coordinate;
import org.geomajas.smartgwt.client.spatial.Bbox;
import org.geomajas.smartgwt.client.widget.MapWidget;

import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseMoveEvent;
import com.google.gwt.event.dom.client.MouseUpEvent;

/**
 * Pans the map in a given direction (distance is half the size of the viewport).
 *
 * @author Kristof Heirwegh
 */
public class PanArrowController extends AbstractGraphicsController {

	private final Coordinate direction;

	/**
	 * @param mapWidget
	 *            the widget on which to pan
	 * @param direction
	 *            direction in which to pan, expected values are 0,1 or -1 for
	 *            both x and y.
	 */
	public PanArrowController(MapWidget mapWidget, Coordinate direction) {
		super(mapWidget);
		if (null == direction) {
			throw new IllegalArgumentException("please provide a direction");
		}
		this.direction = direction;
	}

	@Override
	public void onMouseUp(MouseUpEvent event) {

		if (NativeEvent.BUTTON_LEFT == event.getNativeButton()) {
			Bbox currView = mapWidget.getMapModel().getMapView().getBounds();
			double w = currView.getWidth() / 3;
			double h = currView.getHeight() / 3;

			mapWidget.getMapModel().getMapView().translate(direction.getX() * w, direction.getY() * h);
			event.stopPropagation();
		}
	}

	@Override
	public void onMouseMove(MouseMoveEvent event) {
		event.stopPropagation();
	}

	@Override
	public void onMouseDown(MouseDownEvent event) {
		event.stopPropagation();
	}
}
