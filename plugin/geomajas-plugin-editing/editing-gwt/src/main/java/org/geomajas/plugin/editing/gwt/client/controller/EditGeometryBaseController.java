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

package org.geomajas.plugin.editing.gwt.client.controller;

import org.geomajas.gwt.client.controller.AbstractController;
import org.geomajas.gwt.client.controller.AbstractGraphicsController;
import org.geomajas.gwt.client.controller.PanController;
import org.geomajas.gwt.client.widget.MapWidget;
import org.geomajas.plugin.editing.client.controller.GeometryIndexDragController;
import org.geomajas.plugin.editing.client.controller.GeometryIndexInsertController;
import org.geomajas.plugin.editing.client.service.GeometryEditingService;
import org.geomajas.plugin.editing.client.service.GeometryEditingState;
import org.geomajas.plugin.editing.client.snapping.SnappingService;

import com.google.gwt.event.dom.client.DoubleClickEvent;
import com.google.gwt.event.dom.client.HumanInputEvent;
import com.google.gwt.event.dom.client.MouseMoveEvent;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOverEvent;

/**
 * ...
 * 
 * @author Pieter De Graef
 */
public class EditGeometryBaseController extends AbstractGraphicsController {

	private AbstractController idleController;

	private GeometryIndexDragController dragController;

	private GeometryIndexInsertController insertController;

	private GeometryEditingService service;

	// ------------------------------------------------------------------------
	// Constructors:
	// ------------------------------------------------------------------------

	public EditGeometryBaseController(MapWidget mapWidget, GeometryEditingService service,
			SnappingService snappingService) {
		super(mapWidget);
		this.service = service;
		idleController = new PanController(mapWidget);
		dragController = new GeometryIndexDragController(service, snappingService, this);
		insertController = new GeometryIndexInsertController(service, snappingService, this);
	}

	// ------------------------------------------------------------------------
	// GraphicsController implementation:
	// ------------------------------------------------------------------------

	public void onDown(HumanInputEvent<?> event) {
		if (service.getEditingState() == GeometryEditingState.IDLE) {
			// No shift key down, because we don't want to pan when deselecting vertices.
			// TODO remove the shift check somehow... (now replaced with a event.stopPropagation in selection handler)
			idleController.onDown(event);
		} else if (service.getEditingState() == GeometryEditingState.DRAGGING) {
			dragController.onDown(event);
		} else if (service.getEditingState() == GeometryEditingState.INSERTING) {
			insertController.onDown(event);
		}
	}

	public void onDrag(HumanInputEvent<?> event) {
		if (service.getEditingState() == GeometryEditingState.IDLE) {
			idleController.onDrag(event);
		} else if (service.getEditingState() == GeometryEditingState.DRAGGING) {
			dragController.onDrag(event);
		} else if (service.getEditingState() == GeometryEditingState.INSERTING) {
			insertController.onDrag(event);
		}
	}

	public void onMouseMove(MouseMoveEvent event) {
		super.onMouseMove(event);
		if (service.getEditingState() == GeometryEditingState.IDLE) {
			idleController.onMouseMove(event);
		} else if (service.getEditingState() == GeometryEditingState.DRAGGING) {
			dragController.onMouseMove(event);
		} else if (service.getEditingState() == GeometryEditingState.INSERTING) {
			insertController.onMouseMove(event);
		}
	}

	public void onMouseOut(MouseOutEvent event) {
		if (service.getEditingState() == GeometryEditingState.IDLE) {
			idleController.onMouseOut(event);
		} else if (service.getEditingState() == GeometryEditingState.DRAGGING) {
			dragController.onMouseOut(event);
		} else if (service.getEditingState() == GeometryEditingState.INSERTING) {
			insertController.onMouseOut(event);
		}
	}

	public void onMouseOver(MouseOverEvent event) {
		if (service.getEditingState() == GeometryEditingState.IDLE) {
			idleController.onMouseOver(event);
		} else if (service.getEditingState() == GeometryEditingState.DRAGGING) {
			dragController.onMouseOver(event);
		} else if (service.getEditingState() == GeometryEditingState.INSERTING) {
			insertController.onMouseOver(event);
		}
	}

	public void onUp(HumanInputEvent<?> event) {
		if (service.getEditingState() == GeometryEditingState.IDLE) {
			idleController.onUp(event);
		} else if (service.getEditingState() == GeometryEditingState.DRAGGING) {
			dragController.onUp(event);
		} else if (service.getEditingState() == GeometryEditingState.INSERTING) {
			insertController.onUp(event);
		}
	}

	public void onDoubleClick(DoubleClickEvent event) {
		if (service.getEditingState() == GeometryEditingState.IDLE) {
			idleController.onDoubleClick(event);
		} else if (service.getEditingState() == GeometryEditingState.DRAGGING) {
			dragController.onDoubleClick(event);
		} else if (service.getEditingState() == GeometryEditingState.INSERTING) {
			insertController.onDoubleClick(event);
		}
	}

	// ------------------------------------------------------------------------
	// Getters and setters:
	// ------------------------------------------------------------------------

	public AbstractController getIdleController() {
		return idleController;
	}

	public void setIdleController(AbstractController idleController) {
		this.idleController = idleController;
	}

	public GeometryIndexDragController getDragController() {
		return dragController;
	}

	public void setDragController(GeometryIndexDragController dragController) {
		this.dragController = dragController;
	}

	public GeometryIndexInsertController getInsertController() {
		return insertController;
	}

	public void setInsertController(GeometryIndexInsertController insertController) {
		this.insertController = insertController;
	}
}