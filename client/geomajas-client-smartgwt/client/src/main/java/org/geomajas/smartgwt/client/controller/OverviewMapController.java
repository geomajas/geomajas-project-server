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
import org.geomajas.smartgwt.client.widget.OverviewMap;

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
			mapWidget.setCursorString(mapWidget.getDefaultCursorString());
		}
	}

	// ----------------------------------------------------------

	private OverviewMap getOverviewMap() {
		return (OverviewMap) mapWidget;
	}
}