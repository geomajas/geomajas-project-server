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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.geomajas.geometry.Bbox;
import org.geomajas.geometry.Coordinate;
import org.geomajas.geometry.service.BboxService;
import org.geomajas.graphics.client.event.GraphicsObjectContainerEvent;
import org.geomajas.graphics.client.object.Draggable;
import org.geomajas.graphics.client.object.GRectangle;
import org.geomajas.graphics.client.object.GraphicsObject;
import org.geomajas.graphics.client.object.Resizable;
import org.geomajas.graphics.client.service.AbstractGraphicsController;
import org.geomajas.graphics.client.service.GraphicsController;
import org.geomajas.graphics.client.service.GraphicsControllerFactory;
import org.geomajas.graphics.client.service.GraphicsService;
import org.vaadin.gwtgraphics.client.VectorObject;
import org.vaadin.gwtgraphics.client.VectorObjectContainer;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.DoubleClickEvent;
import com.google.gwt.event.dom.client.DoubleClickHandler;
import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseDownHandler;
import com.google.gwt.event.dom.client.MouseEvent;
import com.google.gwt.event.dom.client.MouseMoveEvent;
import com.google.gwt.event.dom.client.MouseMoveHandler;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.event.dom.client.MouseUpEvent;
import com.google.gwt.event.dom.client.MouseUpHandler;
import com.google.gwt.event.dom.client.MouseWheelEvent;
import com.google.gwt.event.dom.client.MouseWheelHandler;
import com.google.gwt.user.client.DOM;
import com.google.web.bindery.event.shared.HandlerRegistration;

/**
 * Controller of controllers. Activates all controllers when an object is clicked.
 * 
 * @author Jan De Moerloose
 * 
 */
public class MetaController extends AbstractGraphicsController implements MouseDownHandler, MouseUpHandler,
		MouseMoveHandler, MouseOverHandler, MouseOutHandler, MouseWheelHandler, DoubleClickHandler, ClickHandler,
		GraphicsController, GraphicsObjectContainerEvent.Handler {

	protected boolean active;

	private VectorObjectContainer container;

	private Map<GraphicsObject, List<GraphicsController>> controllers;

	private Set<VectorObject> vectorObjects = new HashSet<VectorObject>();

	private List<HandlerRegistration> registrations = new ArrayList<HandlerRegistration>();

	private BackGroundHandler backGroundHandler = new BackGroundHandler();

	public MetaController(GraphicsService service) {
		super(service);
		controllers = new LinkedHashMap<GraphicsObject, List<GraphicsController>>();
		getObjectContainer().addGraphicsObjectContainerHandler(this);
		addAllObjects();
	}

	public void setActive(boolean active) {
		if (active != this.active) {
			this.active = active;
			if (active) {
				// for activation of objects
				for (GraphicsObject object : getObjectContainer().getObjects()) {
					registrations.add(object.asObject().addMouseDownHandler(this));
				}
				// for preventing events to bubble up
				register(getObjectContainer().addMouseDownHandler(this));
				register(getObjectContainer().addMouseUpHandler(this));
				register(getObjectContainer().addMouseMoveHandler(this));
				register(getObjectContainer().addMouseOverHandler(this));
				register(getObjectContainer().addMouseOutHandler(this));
				register(getObjectContainer().addMouseWheelHandler(this));
				register(getObjectContainer().addClickHandler(this));
				register(getObjectContainer().addDoubleClickHandler(this));

				// for de-activation and/or selection of objects
				register(getObjectContainer().getBackGround().addMouseDownHandler(backGroundHandler));
				register(getObjectContainer().getBackGround().addMouseMoveHandler(backGroundHandler));
				register(getObjectContainer().getBackGround().addMouseUpHandler(backGroundHandler));
			} else {
				deactivateAllControllers();
				unregister();
			}
		}
	}

	protected void unregister() {
		for (HandlerRegistration registration : registrations) {
			registration.removeHandler();
		}
		registrations.clear();
	}

	protected void register(HandlerRegistration registration) {
		registrations.add(registration);
	}

	@Override
	public boolean isActive() {
		return active;
	}

	protected Set<VectorObject> getVectorObjects() {
		return vectorObjects;
	}

	@Override
	public void onMouseWheel(MouseWheelEvent event) {
	}

	@Override
	public void onMouseOut(MouseOutEvent event) {
	}

	@Override
	public void onMouseOver(MouseOverEvent event) {
	}

	@Override
	public void onMouseDown(MouseDownEvent event) {
		if (isActive()) {
			if (getVectorObjects().contains(event.getSource())) {
				// deactivate controllers for other object, unless shift key is pressed
				// STUB
				boolean shiftKeyPressed = false;
				if (!shiftKeyPressed) {
					deactivateAllControllers();
				}
				// activate controllers for this object
				for (GraphicsObject object : getObjectContainer().getObjects()) {
					if (object.asObject() == event.getSource()) {
						activateControllersForObject(object, event);
						break;
					}
				}
			}
		}
	}

	@Override
	public void onMouseMove(MouseMoveEvent event) {
	}

	@Override
	public void onClick(ClickEvent event) {
	}

	@Override
	public void onMouseUp(MouseUpEvent event) {
	}

	@Override
	public void onDoubleClick(DoubleClickEvent event) {
		if (isActive()) {
			if (getVectorObjects().contains(event.getSource())) {				
				// activate controllers for this object
				for (GraphicsObject object : getObjectContainer().getObjects()) {
					if (object.asObject() == event.getSource()) {
						activateControllersForObject(object, event);
						break;
					}
				}
			}
		}
	}

	protected void deactivateAllControllers() {
		vectorObjects.clear();
		for (List<GraphicsController> cc : controllers.values()) {
			for (GraphicsController graphicsController : cc) {
				graphicsController.setActive(false);
			}
		}
		for (GraphicsObject object : controllers.keySet()) {
			vectorObjects.add(object.asObject());
		}
		getObjectContainer().deselectAll();
	}

	protected void activateControllersForObject(GraphicsObject object, MouseEvent<?> event) {
		if (controllers.containsKey(object)) {
			for (GraphicsController controller : controllers.get(object)) {
				controller.setActive(true);
				if (event != null) {
					if (controller instanceof MouseDownHandler) {
						if (event.getAssociatedType() == MouseDownEvent.getType()) {
							// forward the event so the controller can handle it (TODO fix this in some other way)
							((MouseDownHandler) controller).onMouseDown((MouseDownEvent) event);
						}
					} else if (controller instanceof DoubleClickHandler) {
						if (event.getAssociatedType() == DoubleClickEvent.getType()) {
							// forward the event so the controller can handle it (TODO fix this in some other way)
							((DoubleClickHandler) controller).onDoubleClick((DoubleClickEvent) event);
						}
					}
				}
			}
			getObjectContainer().setSelected(object, true);
		}
	}

	@Override
	public void onAction(GraphicsObjectContainerEvent event) {
		switch (event.getActionType()) {
			case ADD:
				addObject(event.getObject());
				break;
			case REMOVE:
				removeObject(event.getObject());
				break;
			case UPDATE:
			default:
				break;
		}
	}

	private void addAllObjects() {
		for (GraphicsObject object : getObjectContainer().getObjects()) {
			addObject(object);
		}
	}

	private void addObject(GraphicsObject object) {
		vectorObjects.add(object.asObject());
		for (GraphicsControllerFactory factory : getService().getControllerFactories()) {
			if (factory.supports(object)) {
				if (!controllers.containsKey(object)) {
					controllers.put(object, new ArrayList<GraphicsController>());
				}
				controllers.get(object).add(factory.createController(getService(), object));
			}
		}
		if (active) {
			registrations.add(object.asObject().addMouseDownHandler(this));
			registrations.add(object.asObject().addDoubleClickHandler(this));
		}
	}

	private void removeObject(GraphicsObject object) {
		if (controllers.containsKey(object)) {
			for (GraphicsController controller : controllers.get(object)) {
				controller.setActive(false);
				controller.destroy();
			}
			controllers.remove(object);
			vectorObjects.remove(object);
		}
	}

	@Override
	public void destroy() {
		setActive(false);
	}

	/**
	 * Handlers that deactivates objects or activates them by dragging a rectangle.
	 * 
	 * @author Jan De Moerloose
	 * 
	 */
	public class BackGroundHandler implements MouseDownHandler, MouseMoveHandler, MouseUpHandler {

		private GRectangle dragRectangle;

		private Coordinate begin;

		@Override
		public void onMouseDown(MouseDownEvent event) {
			deactivateAllControllers();
			// start a drag selection !
			begin = getUserCoordinate(event);
			if (dragRectangle == null) {
				dragRectangle = new GRectangle(0, 0, 0, 0, "");
				dragRectangle.setStrokeColor("#696969");
				dragRectangle.setFillOpacity(0);
				dragRectangle.setUserBounds(new Bbox(begin.getX(), begin.getY(), 0, 0));
				dragRectangle.asObject().addMouseMoveHandler(this);
				dragRectangle.asObject().addMouseUpHandler(this);
				container = createContainer();
				container.add(dragRectangle.asObject());
			}
			DOM.setCapture(dragRectangle.asObject().getElement());
		}

		@Override
		public void onMouseMove(MouseMoveEvent event) {
			if (dragRectangle != null) {
				Coordinate end = getUserCoordinate(event);
				dragRectangle.setUserBounds(new Bbox(Math.min(begin.getX(), end.getX()), Math.min(begin.getY(),
						end.getY()), Math.abs(begin.getX() - end.getX()), Math.abs(begin.getY() - end.getY())));
				event.stopPropagation();
			}
		}

		@Override
		public void onMouseUp(MouseUpEvent event) {
			if (dragRectangle != null) {
				for (GraphicsObject object : getObjectContainer().getObjects()) {
					if (object.hasRole(Resizable.TYPE)) {
						Bbox b = object.getRole(Resizable.TYPE).getUserBounds();
						if (BboxService.contains(dragRectangle.getUserBounds(), b)) {
							activateControllersForObject(object, null);
						}
					} else if (object.hasRole(Draggable.TYPE)) {
						Coordinate p = object.getRole(Draggable.TYPE).getPosition();
						if (BboxService.contains(dragRectangle.getUserBounds(), p)) {
							activateControllersForObject(object, null);
						}

					}
				}
				DOM.releaseCapture(dragRectangle.asObject().getElement());
				container.remove(dragRectangle.asObject());
				dragRectangle = null;
				removeContainer(container);
				event.stopPropagation();
			}
		}

	}
	
	public void setControllersOfObjectVisible(GraphicsObject object, boolean visible) {
		if (controllers.containsKey(object)) {
			for (GraphicsController controller : controllers.get(object)) {
				if (controller.isActive()) {
					controller.setVisible(visible);
				}
			}
		}
	}

	@Override
	public void setVisible(boolean visible) {
		// do nothing
	}

}
