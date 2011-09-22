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

import org.geomajas.gwt.client.controller.AbstractController;
import org.geomajas.gwt.client.controller.AbstractGraphicsController;
import org.geomajas.gwt.client.controller.PanController;
import org.geomajas.gwt.client.handler.MapHandler;
import org.geomajas.gwt.client.widget.MapWidget;
import org.geomajas.plugin.editing.client.service.GeometryEditingService;
import org.geomajas.plugin.editing.client.service.GeometryEditingState;

import com.google.gwt.event.dom.client.HumanInputEvent;
import com.google.gwt.event.dom.client.MouseMoveEvent;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOverEvent;

/**
 * ...
 * 
 * @author Pieter De Graef
 */
public class EditGeometryBaseController extends AbstractGraphicsController implements MapHandler {

	// private boolean panningEnabled;

	private AbstractController idleController;

	private AbstractController dragController;

	// private MapDragHandler dragHandler;

	private GeometryEditingService service;

	// private GwtMapEventParser eventParser;

	// ------------------------------------------------------------------------
	// Constructors:
	// ------------------------------------------------------------------------

	public EditGeometryBaseController(MapWidget mapWidget, GeometryEditingService service) {
		super(mapWidget);
		this.service = service;
		idleController = new PanController(mapWidget);
		// panningEnabled = true;
		// eventParser = new GwtMapEventParser(mapWidget, getOffsetX(), getOffsetY());
		// dragHandler = new MoveGeometrySelectionHandler(mapWidget, service, eventParser);
		dragController = new GeometryIndexDragController(service, getEventParser());
	}

	// ------------------------------------------------------------------------
	// GraphicsController implementation:
	// ------------------------------------------------------------------------

	public void onDown(HumanInputEvent<?> event) {
		if (service.getEditingState() == GeometryEditingState.IDLE && !event.isShiftKeyDown()) {
			// No shift key down, because we don't want to pan when deselecting vertices.
			idleController.onDown(event);
		} else if (service.getEditingState() == GeometryEditingState.DRAGGING) {
			dragController.onDown(event);
			// dragHandler.onDragStart(event, getScreenPosition(event));
		}
	}

	public void onDrag(HumanInputEvent<?> event) {
		if (service.getEditingState() == GeometryEditingState.IDLE) {
			idleController.onDrag(event);
		} else if (service.getEditingState() == GeometryEditingState.DRAGGING) {
			dragController.onDrag(event);
		}
	}

	public void onMouseMove(MouseMoveEvent event) {
		super.onMouseMove(event);
		if (service.getEditingState() == GeometryEditingState.IDLE) {
			idleController.onMouseMove(event);
		} else if (service.getEditingState() == GeometryEditingState.DRAGGING) {
			dragController.onMouseMove(event);
		}
	}

	public void onMouseOut(MouseOutEvent event) {
		if (service.getEditingState() == GeometryEditingState.IDLE) {
			idleController.onMouseOut(event);
		} else if (service.getEditingState() == GeometryEditingState.DRAGGING) {
			dragController.onMouseOut(event);
		}
	}

	public void onMouseOver(MouseOverEvent event) {
		if (service.getEditingState() == GeometryEditingState.IDLE) {
			idleController.onMouseOver(event);
		} else if (service.getEditingState() == GeometryEditingState.DRAGGING) {
			dragController.onMouseOver(event);
		}
	}

	public void onUp(HumanInputEvent<?> event) {
		if (service.getEditingState() == GeometryEditingState.IDLE) {
			idleController.onUp(event);
		} else if (service.getEditingState() == GeometryEditingState.DRAGGING) {
			dragController.onUp(event);
		}
	}

	// public void onMouseUp(MouseUpEvent event) {
	// if (panningEnabled) {
	// idleController.onMouseUp(event);
	// }
	// if (service.getEditingState() == GeometryEditingState.DRAGGING) {
	// dragHandler.onDragEnd(event, getScreenPosition(event));
	// }
	// }
}