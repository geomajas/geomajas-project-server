/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2013 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the Apache
 * License, Version 2.0. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */
package org.geomajas.graphics.client.controller;

import org.geomajas.geometry.service.BboxService;
import org.geomajas.graphics.client.event.GraphicsObjectContainerEvent;
import org.geomajas.graphics.client.object.Draggable;
import org.geomajas.graphics.client.object.GraphicsObject;
import org.geomajas.graphics.client.object.anchor.AnchoredTo;
import org.geomajas.graphics.client.service.GraphicsService;
import org.geomajas.graphics.client.service.UpdateHandlerGraphicsController;
import org.vaadin.gwtgraphics.client.Group;

import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseDownHandler;

/**
 * {@link AbstractGraphicsController} that handles resizing (through anchor points) and dragging.
 * 
 * @author Jan De Moerloose
 * 
 */
public class DragController extends UpdateHandlerGraphicsController implements
		MouseDownHandler {

	/**
	 * Object under control.
	 */
	private Draggable object;

	/**
	 * Handler to drag our object.
	 */
	private GraphicsObjectDragHandler dragHandler;

	private boolean dragOnActivate;
	
	public DragController(GraphicsObject object, GraphicsService service, boolean dragOnActivate) {
		super(service, object);
		this.object = object.getRole(Draggable.TYPE);
		setContainer(createContainer());
		this.dragOnActivate = dragOnActivate;
		// listen to changes to our object
		service.getObjectContainer().addGraphicsObjectContainerHandler(this);
	}

	@Override
	public void onAction(GraphicsObjectContainerEvent event) {
		super.onAction(event);
		if (getObject().hasRole(AnchoredTo.TYPE)) {
			if (event.getObject() == getObject().getRole(AnchoredTo.TYPE).getMasterObject()) {
				getObject().getRole(AnchoredTo.TYPE).updateAnchorLine();
			}
		}
	}

	@Override
	public void onMouseDown(MouseDownEvent event) {
		if (isActive()) {
			if (dragOnActivate) {
				if (BboxService.contains(object.getUserBounds(), getUserCoordinate(event))) {
					dragHandler.onMouseDown(event);
					event.stopPropagation();
				}
			}
		}
	}

	@Override
	protected void init() {
		setHandlerGroup(new Group());
		// create the drag handler and attach it
		dragHandler = new GraphicsObjectDragHandler(getObject(), getService(), this);
		getHandlerGroup().add(dragHandler.getInvisbleMaskGraphicsObject().asObject());
		// update positions
		updateHandlers();
		// add the group
		getContainer().add(getHandlerGroup());
	}

	@Override
	public void updateHandlers() {
		if (dragHandler != null) {
			dragHandler.update();
		}
	}

	@Override
	public void setVisible(boolean visible) {
		// do nothing
		
	}
}
