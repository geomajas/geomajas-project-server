/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2011 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */

package org.geomajas.plugin.editing.client.controller;

import org.geomajas.gwt.client.controller.AbstractGraphicsController;
import org.geomajas.gwt.client.controller.PanController;
import org.geomajas.gwt.client.handler.MapDragHandler;
import org.geomajas.gwt.client.handler.MapEventParser;
import org.geomajas.gwt.client.handler.MapHandler;
import org.geomajas.gwt.client.widget.MapWidget;
import org.geomajas.plugin.editing.client.handler.GwtMapEventParser;
import org.geomajas.plugin.editing.client.service.GeometryEditingService;
import org.geomajas.plugin.editing.client.service.GeometryEditingState;

import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseMoveEvent;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseUpEvent;

/**
 * ...
 * 
 * @author Pieter De Graef
 */
public class EditGeometryBaseController extends AbstractGraphicsController implements MapHandler {

	private PanController panController;

	private boolean panningEnabled;

	private MapDragHandler dragHandler;

	private GeometryEditingService service;

	private GwtMapEventParser eventParser;

	// ------------------------------------------------------------------------
	// Constructors:
	// ------------------------------------------------------------------------

	public EditGeometryBaseController(MapWidget mapWidget, GeometryEditingService service) {
		super(mapWidget);
		this.service = service;
		panController = new PanController(mapWidget);
		panningEnabled = true;
		eventParser = new GwtMapEventParser(mapWidget, getOffsetX(), getOffsetY());
		dragHandler = new MoveGeometrySelectionHandler(mapWidget, service, eventParser);
	}

	// ------------------------------------------------------------------------
	// MapHandler implementation:
	// ------------------------------------------------------------------------

	public MapEventParser getEventParser() {
		return eventParser;
	}

	// ------------------------------------------------------------------------
	// GraphicsController implementation:
	// ------------------------------------------------------------------------

	public void onMouseDown(MouseDownEvent event) {
		if (panningEnabled && service.getEditingState() == GeometryEditingState.IDLE && !event.isShiftKeyDown()) {
			// No shift key down, because we don't want to pan when deselecting vertices.
			panController.onMouseDown(event);
		} else if (service.getEditingState() == GeometryEditingState.DRAGGING) {
			dragHandler.onDragStart(event, getScreenPosition(event));
		}
	}

	public void onMouseMove(MouseMoveEvent event) {
		if (panningEnabled && service.getEditingState() == GeometryEditingState.IDLE) {
			panController.onMouseMove(event);
		} else if (service.getEditingState() == GeometryEditingState.DRAGGING) {
			dragHandler.onDragMove(event, getScreenPosition(event));
		}
	}

	public void onMouseOut(MouseOutEvent event) {
		if (panningEnabled) {
			panController.onMouseOut(event);
		}
	}

	public void onMouseOver(MouseOverEvent event) {
		if (panningEnabled) {
			panController.onMouseOver(event);
		}
	}

	public void onMouseUp(MouseUpEvent event) {
		if (panningEnabled) {
			panController.onMouseUp(event);
		}
		if (service.getEditingState() == GeometryEditingState.DRAGGING) {
			dragHandler.onDragEnd(event, getScreenPosition(event));
		}
	}

	public void onActivate() {
	}

	public void onDeactivate() {
	}

	// ------------------------------------------------------------------------
	// Getters and setters:
	// ------------------------------------------------------------------------

	public boolean isPanningEnabled() {
		return panningEnabled;
	}

	public void setPanningEnabled(boolean panningEnabled) {
		this.panningEnabled = panningEnabled;
	}

	@Override
	public void setOffsetX(int offsetX) {
		super.setOffsetX(offsetX);
		eventParser.setOffsetX(offsetX);
	}

	@Override
	public void setOffsetY(int offsetY) {
		super.setOffsetY(offsetY);
		eventParser.setOffsetY(offsetY);
	}

}