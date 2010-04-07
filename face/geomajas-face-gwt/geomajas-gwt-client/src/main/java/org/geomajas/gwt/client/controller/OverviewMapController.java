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

import org.geomajas.geometry.Coordinate;
import org.geomajas.gwt.client.widget.OverviewMap;

import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseMoveEvent;
import com.google.gwt.event.dom.client.MouseUpEvent;
import com.smartgwt.client.types.Cursor;

/**
 * Controller used by OverviewMap to handle panning.
 *
 * @author Kristof Heirwegh
 */
public class OverviewMapController extends AbstractGraphicsController {

	private boolean dragging;

	private Coordinate oldPosition;
	private Coordinate previous;

	public OverviewMapController(OverviewMap mapWidget) {
		super(mapWidget);
	}

	@Override
	public void onMouseDown(MouseDownEvent event) {
		if (event.getNativeButton() != NativeEvent.BUTTON_RIGHT) {
			dragging = true;
			oldPosition = getWorldPosition(event);
			previous = getScreenPosition(event);
			mapWidget.setCursor(Cursor.POINTER);
		}
	}

	/**
	 * Only moving rectangle or reticle during drag.
	 */
	@Override
	public void onMouseMove(MouseMoveEvent event) {
		if (dragging) {
			Coordinate current = getScreenPosition(event);
			getOverviewMap().movePov(current.getX() - previous.getX(), current.getY() - previous.getY());
			previous = current;
		}
	}

	@Override
	public void onMouseUp(MouseUpEvent event) {
		if (dragging) {
			Coordinate newPosition = getWorldPosition(event);
			getOverviewMap()
					.panTargetMap(newPosition.getX() - oldPosition.getX(), newPosition.getY() - oldPosition.getY());
			dragging = false;
			mapWidget.setCursor(Cursor.DEFAULT);
		}
	}

	// ----------------------------------------------------------

	private OverviewMap getOverviewMap() {
		return (OverviewMap) mapWidget;
	}
}
