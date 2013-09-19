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
package org.geomajas.graphics.client.service;

import org.geomajas.graphics.client.event.GraphicsObjectContainerEvent;
import org.geomajas.graphics.client.event.GraphicsObjectContainerEvent.ActionType;
import org.geomajas.graphics.client.object.GraphicsObject;
import org.vaadin.gwtgraphics.client.Group;
import org.vaadin.gwtgraphics.client.VectorObjectContainer;

/**
 * Useful base class for {@link GraphicsController}.
 * 
 * @author Jan De Moerloose
 * 
 */
public abstract class UpdateHandlerGraphicsController extends AbstractGraphicsController
		implements GraphicsObjectContainerEvent.Handler {
	
	/**
	 * Group with all handler objects.
	 */
	private Group handlerGroup;
	
	/**
	 * Our own container.
	 */
	private VectorObjectContainer container;
	
	/**
	 * Is controller active (listening to mouse events) ?
	 */
	private boolean active;

	public UpdateHandlerGraphicsController(GraphicsService graphicsService,
			GraphicsObject object) {
		super(graphicsService, object);
	}

	public abstract void updateHandlers();

	public Group getHandlerGroup() {
		return handlerGroup;
	}

	public void setHandlerGroup(Group handlerGroup) {
		this.handlerGroup = handlerGroup;
	}

	public VectorObjectContainer getContainer() {
		return container;
	}

	public void setContainer(VectorObjectContainer container) {
		this.container = container;
	}
	
	@Override
	public void destroy() {
		getContainer().clear();
		removeContainer(getContainer());
	}
	
	@Override
	public boolean isActive() {
		return active;
	}
	
	@Override
	public void setActive(boolean active) {
		if (active != this.active) {
			this.active = active;
			if (active) {
				if (getHandlerGroup() == null || getHandlerGroup().getVectorObjectCount() < 1) {
					// create and (implicitly) activate the handler group
					init();
				} else {
					// the group may be detached, update and reattach !
					updateHandlers();
					getContainer().add(getHandlerGroup());
				}
			} else {
				// just remove the handler group
				if (getHandlerGroup() != null) {
					getContainer().remove(getHandlerGroup());
				}
			}
		}
	}
	
	@Override
	public void onAction(GraphicsObjectContainerEvent event) {
		if (event.getObject() == getObject()) {
			if (event.getActionType() == ActionType.UPDATE) {
				// must re-initialize as this object has changed (mask)
				getContainer().clear();
				setHandlerGroup(null);
				if (isActive()) {
					init();
				}
			} else {
				// handled by meta controller
			}
		}
	}
	
	protected abstract void init();
	
}
