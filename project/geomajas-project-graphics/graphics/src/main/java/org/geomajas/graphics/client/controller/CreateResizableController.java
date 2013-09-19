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

import org.geomajas.geometry.Bbox;
import org.geomajas.geometry.Coordinate;
import org.geomajas.graphics.client.object.GraphicsObject;
import org.geomajas.graphics.client.object.Resizable;
import org.geomajas.graphics.client.object.anchor.Anchored;
import org.geomajas.graphics.client.operation.AddOperation;
import org.geomajas.graphics.client.service.AbstractGraphicsController;
import org.geomajas.graphics.client.service.GraphicsObjectContainer.Space;
import org.geomajas.graphics.client.service.GraphicsService;
import org.geomajas.graphics.client.util.BboxPosition;
import org.geomajas.graphics.client.util.GraphicsUtil;
import org.vaadin.gwtgraphics.client.VectorObjectContainer;

import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseDownHandler;
import com.google.gwt.event.dom.client.MouseMoveEvent;
import com.google.gwt.event.dom.client.MouseMoveHandler;
import com.google.gwt.event.dom.client.MouseUpEvent;
import com.google.gwt.event.dom.client.MouseUpHandler;
import com.google.gwt.user.client.DOM;
import com.google.web.bindery.event.shared.HandlerRegistration;

/**
 * Generic controller that creates a {@link Resizable}.
 * 
 * @author Jan De Moerloose
 * 
 */
public abstract class CreateResizableController extends AbstractGraphicsController implements MouseDownHandler,
		MouseMoveHandler, MouseUpHandler {

	private GraphicsObject dragResizable;

	private Coordinate begin;

	private boolean active;

	/**
	 * Our own container.
	 */
	private VectorObjectContainer container;

	private HandlerRegistration registration;

	/**
	 * Create the object. This object should support the {@link Resizable} role.
	 * 
	 * @return
	 */
	protected abstract GraphicsObject createObject();

	public CreateResizableController(GraphicsService graphicsService) {
		super(graphicsService);
		container = createContainer();
	}

	@Override
	public void setActive(boolean active) {
		this.active = active;
		if (active) {
			container = createContainer();
			registration = getObjectContainer().addMouseDownHandler(this);
		} else {
			if (container != null) {
				removeContainer(container);
			}
			container = null;
			if (registration != null) {
				registration.removeHandler();
				registration = null;
			}
		}
	}

	@Override
	public boolean isActive() {
		return active;
	}

	@Override
	public void destroy() {
	}

	@Override
	public void onMouseDown(MouseDownEvent event) {
		begin = getUserCoordinate(event);
		if (dragResizable == null) {
			dragResizable = createObject();
			dragResizable.getRole(Resizable.TYPE).setUserBounds(new Bbox(begin.getX(), begin.getY(), 0, 0));
			setAnchor(dragResizable);
			dragResizable.asObject().addMouseMoveHandler(this);
			dragResizable.asObject().addMouseUpHandler(this);
			container.add(dragResizable.asObject());
		}
		DOM.setCapture(dragResizable.asObject().getElement());
	}

	private void setAnchor(GraphicsObject object) {
		if (object.hasRole(Anchored.TYPE)) {
			Bbox userBounds = object.getRole(Resizable.TYPE).getUserBounds();
			Coordinate midLow = GraphicsUtil.getPosition(userBounds, BboxPosition.MIDDLE_LOW);
			Coordinate midLowScreen = getObjectContainer().transform(midLow, Space.USER, Space.SCREEN);
			Coordinate anchorPos = getObjectContainer().transform(
					new Coordinate(midLowScreen.getX(), midLowScreen.getY() + 20), Space.SCREEN, Space.USER);
			object.getRole(Anchored.TYPE).setAnchorPosition(anchorPos);
		}

	}

	@Override
	public void onMouseUp(MouseUpEvent event) {
		GraphicsObject result = createObject();
		result.getRole(Resizable.TYPE).setUserBounds(dragResizable.getRole(Resizable.TYPE).getUserBounds());
		setAnchor(result);
		DOM.releaseCapture(dragResizable.asObject().getElement());
		container.remove(dragResizable.asObject());
		dragResizable = null;
		execute(new AddOperation(result));
	}

	@Override
	public void onMouseMove(MouseMoveEvent event) {
		Coordinate end = getUserCoordinate(event);
		dragResizable.getRole(Resizable.TYPE).setUserBounds(
				new Bbox(Math.min(begin.getX(), end.getX()), Math.min(begin.getY(), end.getY()), Math.abs(begin.getX()
						- end.getX()), Math.abs(begin.getY() - end.getY())));
		setAnchor(dragResizable);
	}
	
	@Override
	public void setVisible(boolean visible) {
		// TODO do nothing, this is ceate controller
		
	}
}
