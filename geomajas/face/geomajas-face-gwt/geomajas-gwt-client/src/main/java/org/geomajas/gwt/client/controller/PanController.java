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
import com.google.gwt.event.dom.client.MouseEvent;
import com.google.gwt.event.dom.client.MouseMoveEvent;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseUpEvent;
import com.smartgwt.client.types.Cursor;
import org.geomajas.geometry.Coordinate;
import org.geomajas.gwt.client.widget.MapWidget;

/**
 * Handle panning by dragging the map. Also allows zoom to rectangle when using shift or ctrl when you start to drag.
 *
 * @author Joachim Van der Auwera
 */
public class PanController extends AbstractGraphicsController {

	private boolean zooming;

	private boolean dragging;

	private Coordinate begin;

	private ZoomToRectangleController zoomToRectangleController;
	
	// Constructors:

	public PanController(MapWidget mapWidget) {
		super(mapWidget);
		zoomToRectangleController = new ZoomToRectangleController(mapWidget);
	}

	@Override
	public void onMouseDown(MouseDownEvent event) {
		if (event.isControlKeyDown() || event.isShiftKeyDown()) {
			zooming = true;
			zoomToRectangleController.onMouseDown(event);
		} else if (event.getNativeButton() != NativeEvent.BUTTON_RIGHT) {
			dragging = true;
			begin = getScreenPosition(event);
			mapWidget.setCursor(Cursor.MOVE);
		}
	}

	@Override
	public void onMouseUp(MouseUpEvent event) {
		if (zooming) {
			zoomToRectangleController.onMouseUp(event);
			zooming = false;
		} else if (dragging) {
			stopPanning(event);
		}
	}

	@Override
	public void onMouseMove(MouseMoveEvent event) {
		if (zooming) {
			zoomToRectangleController.onMouseMove(event);
		} else if (dragging) {
			updateView(event);
		}
	}

	@Override
	public void onMouseOut(MouseOutEvent event) {
		if (zooming) {
			zoomToRectangleController.onMouseOut(event);
		} else {
			stopPanning(null);
		}
	}

	// Private methods:

	private void stopPanning(MouseUpEvent event) {
		dragging = false;
		mapWidget.setCursor(Cursor.DEFAULT);
		if (null != event) {
			updateView(event);
		}
	}

	private void updateView(MouseEvent<?> event) {
		Coordinate end = getScreenPosition(event);
		Coordinate beginWorld = getTransformer().viewToWorld(begin);
		Coordinate endWorld = getTransformer().viewToWorld(end);
		mapWidget.getMapModel().getMapView()
				.translate(beginWorld.getX() - endWorld.getX(), beginWorld.getY() - endWorld.getY());
		begin = end;
	}
}
