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

import org.geomajas.geometry.Coordinate;
import org.geomajas.graphics.client.object.BaseGraphicsObject;
import org.geomajas.graphics.client.object.Bordered;
import org.geomajas.graphics.client.object.Draggable;
import org.geomajas.graphics.client.object.GraphicsObject;
import org.geomajas.graphics.client.object.anchor.Anchored;
import org.geomajas.graphics.client.object.role.Labeled;
import org.geomajas.graphics.client.operation.DragOperation;
import org.geomajas.graphics.client.operation.GraphicsOperation;
import org.geomajas.graphics.client.service.GraphicsService;
import org.geomajas.graphics.client.service.UpdateHandlerGraphicsController;
import org.vaadin.gwtgraphics.client.VectorObject;

import com.google.gwt.event.dom.client.MouseMoveEvent;

/**
 * Uses the mask for dragging.
 * 
 * @author Jan De Moerloose
 * 
 */
public class GraphicsObjectDragHandler extends AbstractDragHandler {
	
	private GraphicsObject invisbleMaskGraphicsObject;

	public GraphicsObjectDragHandler(GraphicsObject object, GraphicsService service,
			UpdateHandlerGraphicsController graphicsController) {
		super(object, service, graphicsController);
	}

	public void update() {
		invisbleMaskGraphicsObject.getRole(Draggable.TYPE).setPosition(
				getObject().getRole(Draggable.TYPE).getPosition());
	}

	@Override
	protected VectorObject createInvisibleMask() {
		invisbleMaskGraphicsObject = (GraphicsObject) getObject().cloneObject();
		invisbleMaskGraphicsObject.setOpacity(0);
		
		// remove the updateableAwareRoles, but leave the RenderableRole
		if (invisbleMaskGraphicsObject instanceof BaseGraphicsObject) {
			BaseGraphicsObject baseObject = (BaseGraphicsObject) invisbleMaskGraphicsObject;
			if (baseObject.hasRole(Anchored.TYPE)) {
				baseObject.removeRole(Anchored.TYPE);
			}
			if (baseObject.hasRole(Labeled.TYPE)) {
				baseObject.removeRole(Labeled.TYPE);
			}
			if (baseObject.hasRole(Bordered.TYPE)) {
				baseObject.removeRole(Bordered.TYPE);
			}
		}
		return invisbleMaskGraphicsObject.asObject();
	}

	@Override
	protected GraphicsObject createDraggingMask() {
		GraphicsObject maskObject = (GraphicsObject) getObject().cloneObject();
		maskObject.setOpacity(0.5);
		maskObject.getRole(Draggable.TYPE).setPosition(getBeginPositionUser());
		return maskObject;
	}

	@Override
	protected Coordinate getObjectPosition() {
		return getObject().getRole(Draggable.TYPE).getPosition();
	}

	@Override
	protected GraphicsOperation createGraphicsOperation(Coordinate before,
			Coordinate after) {
		return new DragOperation(getObject(), before, after);
	}

	@Override
	protected void mouseMoveContent(MouseMoveEvent event) {
		getDraggingMask().getRole(Draggable.TYPE).setPosition(
				getNewPosition(event.getClientX(), event.getClientY()));
	}

	public GraphicsObject getInvisbleMaskGraphicsObject() {
		return invisbleMaskGraphicsObject;
	}

}


