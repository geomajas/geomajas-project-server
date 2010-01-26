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

package org.geomajas.gwt.client.controller;

import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseMoveEvent;
import com.google.gwt.event.dom.client.MouseUpEvent;
import org.geomajas.geometry.Coordinate;
import org.geomajas.gwt.client.spatial.Bbox;
import org.geomajas.gwt.client.widget.MapWidget;

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
			double w = currView.getWidth() / 2;
			double h = currView.getHeight() / 2;

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
