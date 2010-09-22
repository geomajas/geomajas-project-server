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
import org.geomajas.gwt.client.map.MapView.ZoomOption;
import org.geomajas.gwt.client.spatial.Bbox;
import org.geomajas.gwt.client.widget.MapWidget;

import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.event.dom.client.DoubleClickEvent;
import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseEvent;
import com.google.gwt.event.dom.client.MouseMoveEvent;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseUpEvent;
import com.smartgwt.client.types.Cursor;

/**
 * Handle panning by dragging the map. Also allows zoom to rectangle when using shift or ctrl when you start to drag.
 * Double clicking allows the user to zoom in on the clicked location.
 * 
 * @author Joachim Van der Auwera
 * @author Pieter De Graef
 */
public class PanController extends AbstractGraphicsController {

	private boolean zooming;

	private boolean dragging;

	private boolean moving;

	private boolean showCursorOnMove;

	private Coordinate begin;

	private ZoomToRectangleController zoomToRectangleController;

	private Coordinate lastClickPosition;

	// Constructors:

	public PanController(MapWidget mapWidget) {
		super(mapWidget);
		zoomToRectangleController = new ZoomToRectangleController(mapWidget);
	}

	/**
	 * Should the cursor only be shown when mouse is dragged (defaults to false) ?
	 * 
	 * @return true if to be shown on drag
	 */
	public boolean isShowCursorOnMove() {
		return showCursorOnMove;
	}

	/**
	 * If set to true, the move cursor is only shown when the mouse is dragged.
	 * 
	 * @param showCursorOnMove
	 *            true if cursor should be shown on move (defaults to false)
	 */
	public void setShowCursorOnMove(boolean showCursorOnMove) {
		this.showCursorOnMove = showCursorOnMove;
	}

	@Override
	public void onMouseDown(MouseDownEvent event) {
		if (event.isControlKeyDown() || event.isShiftKeyDown()) {
			zooming = true;
			zoomToRectangleController.onMouseDown(event);
		} else if (event.getNativeButton() != NativeEvent.BUTTON_RIGHT) {
			dragging = true;
			mapWidget.getMapModel().getMapView().setPanDragging(true);
			begin = getScreenPosition(event);
			if (!isShowCursorOnMove()) {
				mapWidget.setCursor(Cursor.MOVE);
			}
		}
		lastClickPosition = getWorldPosition(event);
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
			if (!moving && isShowCursorOnMove()) {
				mapWidget.setCursor(Cursor.MOVE);
			}
			moving = true;
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

	/**
	 * Zoom in to the double-clicked position.
	 */
	public void onDoubleClick(DoubleClickEvent event) {
		// Zoom in on the event location.
		Bbox bounds = mapWidget.getMapModel().getMapView().getBounds();
		double x = lastClickPosition.getX() - (bounds.getWidth() / 4);
		double y = lastClickPosition.getY() - (bounds.getHeight() / 4);
		Bbox newBounds = new Bbox(x, y, bounds.getWidth() / 2, bounds.getHeight() / 2);
		mapWidget.getMapModel().getMapView().applyBounds(newBounds, ZoomOption.LEVEL_CHANGE);
	}

	public boolean isMoving() {
		return moving;
	}

	// Private methods:

	private void stopPanning(MouseUpEvent event) {
		mapWidget.getMapModel().getMapView().setPanDragging(false);
		dragging = false;
		moving = false;
		mapWidget.setCursor(Cursor.DEFAULT);
		if (null != event) {
			updateView(event);
		}
	}

	private void updateView(MouseEvent<?> event) {
		Coordinate end = getScreenPosition(event);
		Coordinate beginWorld = getTransformer().viewToWorld(begin);
		Coordinate endWorld = getTransformer().viewToWorld(end);
		mapWidget.getMapModel().getMapView().translate(beginWorld.getX() - endWorld.getX(),
				beginWorld.getY() - endWorld.getY());
		begin = end;
	}
}
