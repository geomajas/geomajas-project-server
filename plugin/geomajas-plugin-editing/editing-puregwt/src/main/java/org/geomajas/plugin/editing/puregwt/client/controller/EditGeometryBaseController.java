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

package org.geomajas.plugin.editing.puregwt.client.controller;

import org.geomajas.plugin.editing.client.controller.AbstractGeometryIndexController;
import org.geomajas.plugin.editing.client.controller.GeometryIndexDragController;
import org.geomajas.plugin.editing.client.controller.GeometryIndexInsertController;
import org.geomajas.plugin.editing.client.service.GeometryEditService;
import org.geomajas.plugin.editing.client.service.GeometryEditState;
import org.geomajas.plugin.editing.client.snap.SnapService;
import org.geomajas.puregwt.client.controller.AbstractMapController;
import org.geomajas.puregwt.client.controller.NavigationController;
import org.geomajas.puregwt.client.map.MapPresenter;

import com.google.gwt.event.dom.client.DoubleClickEvent;
import com.google.gwt.event.dom.client.HumanInputEvent;
import com.google.gwt.event.dom.client.MouseMoveEvent;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseWheelEvent;

/**
 * ...
 * 
 * @author Pieter De Graef
 */
public class EditGeometryBaseController extends AbstractMapController {

	private AbstractMapController idleController;

	private AbstractGeometryIndexController dragController;

	private AbstractGeometryIndexController insertController;

	private GeometryEditService service;

	private boolean isClickToStop;

	// ------------------------------------------------------------------------
	// Constructors:
	// ------------------------------------------------------------------------

	public EditGeometryBaseController(GeometryEditService service, SnapService snappingService) {
		super(false);
		this.service = service;
		idleController = new NavigationController();
		dragController = new GeometryIndexDragController(service, snappingService, this);
		insertController = new GeometryIndexInsertController(service, snappingService, this);
	}

	// ------------------------------------------------------------------------
	// GraphicsController implementation:
	// ------------------------------------------------------------------------

	public void onActivate(MapPresenter mapPresenter) {
		super.onActivate(mapPresenter);
		idleController.onActivate(mapPresenter);
		insertController.setMaxBounds(mapPresenter.getConfiguration().getMaxBounds());
		dragController.setMaxBounds(mapPresenter.getConfiguration().getMaxBounds());
	}

	public void onDown(HumanInputEvent<?> event) {
		if (service.getEditingState() == GeometryEditState.IDLE) {
			// No shift key down, because we don't want to pan when deselecting vertices.
			// TODO remove the shift check somehow... (now replaced with a event.stopPropagation in selection handler)
			idleController.onDown(event);
		} else if (service.getEditingState() == GeometryEditState.DRAGGING) {
			dragController.onDown(event);
		} else if (service.getEditingState() == GeometryEditState.INSERTING) {
			insertController.onDown(event);
		}
	}

	public void onDrag(HumanInputEvent<?> event) {
		if (service.getEditingState() == GeometryEditState.IDLE) {
			idleController.onDrag(event);
		} else if (service.getEditingState() == GeometryEditState.DRAGGING) {
			dragController.onDrag(event);
		} else if (service.getEditingState() == GeometryEditState.INSERTING) {
			insertController.onDrag(event);
		}
	}

	public void onMouseMove(MouseMoveEvent event) {
		super.onMouseMove(event);
		if (service.getEditingState() == GeometryEditState.IDLE) {
			idleController.onMouseMove(event);
		} else if (service.getEditingState() == GeometryEditState.DRAGGING) {
			dragController.onMouseMove(event);
		} else if (service.getEditingState() == GeometryEditState.INSERTING) {
			insertController.onMouseMove(event);
		}
	}

	public void onMouseOut(MouseOutEvent event) {
		if (service.getEditingState() == GeometryEditState.IDLE) {
			idleController.onMouseOut(event);
		} else if (service.getEditingState() == GeometryEditState.DRAGGING) {
			dragController.onMouseOut(event);
		} else if (service.getEditingState() == GeometryEditState.INSERTING) {
			insertController.onMouseOut(event);
		}
	}

	public void onMouseOver(MouseOverEvent event) {
		if (service.getEditingState() == GeometryEditState.IDLE) {
			idleController.onMouseOver(event);
		} else if (service.getEditingState() == GeometryEditState.DRAGGING) {
			dragController.onMouseOver(event);
		} else if (service.getEditingState() == GeometryEditState.INSERTING) {
			insertController.onMouseOver(event);
		}
	}

	public void onUp(HumanInputEvent<?> event) {
		if (service.getEditingState() == GeometryEditState.IDLE) {
			if (isClickToStop() && service.isStarted()) {
				service.stop();
			}
			idleController.onUp(event);
		} else if (service.getEditingState() == GeometryEditState.DRAGGING) {
			dragController.onUp(event);
		} else if (service.getEditingState() == GeometryEditState.INSERTING) {
			insertController.onUp(event);
		}
	}

	public void onDoubleClick(DoubleClickEvent event) {
		if (service.getEditingState() == GeometryEditState.IDLE) {
			idleController.onDoubleClick(event);
		} else if (service.getEditingState() == GeometryEditState.DRAGGING) {
			dragController.onDoubleClick(event);
		} else if (service.getEditingState() == GeometryEditState.INSERTING) {
			insertController.onDoubleClick(event);
		}
	}

	public void onMouseWheel(MouseWheelEvent event) {
		idleController.onMouseWheel(event);
	}

	// ------------------------------------------------------------------------
	// Getters and setters:
	// ------------------------------------------------------------------------

	public GeometryEditService getEditService() {
		return service;
	}

	public AbstractMapController getIdleController() {
		return idleController;
	}

	public AbstractGeometryIndexController getDragController() {
		return dragController;
	}

	public AbstractGeometryIndexController getInsertController() {
		return insertController;
	}

	public void setIdleController(AbstractMapController idleController) {
		this.idleController = idleController;
	}

	public void setDragController(AbstractGeometryIndexController dragController) {
		this.dragController = dragController;
	}

	public void setInsertController(AbstractGeometryIndexController insertController) {
		this.insertController = insertController;
	}

	/**
	 * Set boolean that determines if a user can stop editing by clicking outside the geometry that is being edited.
	 * 
	 * @param isClickToStop
	 *            true to stop, false otherwise.
	 */
	public void setClickToStop(boolean isClickToStop) {
		this.isClickToStop = isClickToStop;
	}

	/**
	 * Get boolean that determines if a user can stop editing by clicking outside the geometry that is being edited.
	 * 
	 * @return isClickToStop true to stop, false otherwise.
	 */
	public boolean isClickToStop() {
		return isClickToStop;
	}
}