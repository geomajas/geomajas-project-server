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

import java.util.ArrayList;
import java.util.List;

import org.geomajas.graphics.client.event.AddAnchoredLinesEvent;
import org.geomajas.graphics.client.event.GraphicsObjectContainerEvent;
import org.geomajas.graphics.client.event.GraphicsObjectSelectedEvent;
import org.geomajas.graphics.client.event.GraphicsObjectContainerEvent.ActionType;
import org.geomajas.graphics.client.event.GraphicsObjectContainerEvent.Handler;
import org.geomajas.graphics.client.event.GraphicsOperationEvent;
import org.geomajas.graphics.client.object.GraphicsObject;
import org.geomajas.graphics.client.object.anchor.AnchoredTo;
import org.geomajas.graphics.client.shape.CoordinatePath;
import org.vaadin.gwtgraphics.client.Group;
import org.vaadin.gwtgraphics.client.Shape;
import org.vaadin.gwtgraphics.client.VectorObject;
import org.vaadin.gwtgraphics.client.VectorObjectContainer;
import org.vaadin.gwtgraphics.client.shape.Rectangle;

import com.google.gwt.dom.client.Style.Cursor;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.DomEvent.Type;
import com.google.gwt.event.dom.client.DoubleClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.dom.client.HasDoubleClickHandlers;
import com.google.gwt.event.dom.client.HasMouseDownHandlers;
import com.google.gwt.event.dom.client.HasMouseMoveHandlers;
import com.google.gwt.event.dom.client.HasMouseOutHandlers;
import com.google.gwt.event.dom.client.HasMouseOverHandlers;
import com.google.gwt.event.dom.client.HasMouseUpHandlers;
import com.google.gwt.event.dom.client.HasMouseWheelHandlers;
import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseDownHandler;
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
import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.event.shared.HasHandlers;
import com.google.web.bindery.event.shared.EventBus;

/**
 * 
 * Implementation of {@link GraphicsObjectContainer} that is backed by a root {@link VectorObjectContainer}. The
 * implementation provides catch-all mouse event handlers by adding a large background rectangle as the first child of
 * the container. All objects are added to a {@link Group}.
 * 
 * @author Jan De Moerloose
 * 
 */
public abstract class AbstractGraphicsObjectContainer implements GraphicsObjectContainer {

	private VectorObjectContainer rootGroup;

	private BackGround backGround = new BackGround();

	private ObjectGroup objectGroup = new ObjectGroup();

	private EventBus eventBus;

	private List<GraphicsObject> objects = new ArrayList<GraphicsObject>();

	protected AbstractGraphicsObjectContainer(EventBus eventBus) {
		this.eventBus = eventBus;
	}

	protected AbstractGraphicsObjectContainer(EventBus eventBus, VectorObjectContainer rootGroup) {
		this.eventBus = eventBus;
		setRootContainer(rootGroup);
	}

	public void setRootContainer(VectorObjectContainer rootGroup) {
		this.rootGroup = rootGroup;
		rootGroup.add(backGround);
		rootGroup.add(objectGroup);
	}

	public VectorObjectContainer getRootContainer() {
		return rootGroup;
	}

	@Override
	public HasAllMouseAndClickHandlers getObjectGroup() {
		return objectGroup;
	}

	@Override
	public HasAllMouseAndClickHandlers getBackGround() {
		return backGround;
	}

	@Override
	public void fireEvent(GwtEvent<?> event) {
		backGround.fireEvent(event);
	}

	@Override
	public HandlerRegistration addMouseDownHandler(final MouseDownHandler handler) {
		return registerHandler((HasHandlers) rootGroup, handler, MouseDownEvent.getType());
	}

	@Override
	public HandlerRegistration addMouseUpHandler(MouseUpHandler handler) {
		return registerHandler((HasHandlers) rootGroup, handler, MouseUpEvent.getType());
	}

	@Override
	public HandlerRegistration addMouseOutHandler(MouseOutHandler handler) {
		return registerHandler((HasHandlers) rootGroup, handler, MouseOutEvent.getType());
	}

	@Override
	public HandlerRegistration addMouseOverHandler(MouseOverHandler handler) {
		return registerHandler((HasHandlers) rootGroup, handler, MouseOverEvent.getType());
	}

	@Override
	public HandlerRegistration addMouseMoveHandler(MouseMoveHandler handler) {
		return registerHandler((HasHandlers) rootGroup, handler, MouseMoveEvent.getType());
	}

	@Override
	public HandlerRegistration addMouseWheelHandler(MouseWheelHandler handler) {
		return registerHandler((HasHandlers) rootGroup, handler, MouseWheelEvent.getType());
	}

	@Override
	public HandlerRegistration addClickHandler(ClickHandler handler) {
		return ((HasClickHandlers) rootGroup).addClickHandler(handler);
	}

	@Override
	public HandlerRegistration addDoubleClickHandler(DoubleClickHandler handler) {
		return ((HasDoubleClickHandlers) rootGroup).addDoubleClickHandler(handler);
	}

	@Override
	public VectorObjectContainer createContainer() {
		Group group = new Group();
		objectGroup.add(group);
		return group;
	}

	@Override
	public void removeContainer(VectorObjectContainer container) {
		objectGroup.remove((Group) container);
	}

	@Override
	public List<GraphicsObject> getObjects() {
		return objects;
	}

	@Override
	public void add(GraphicsObject object) {
		objectGroup.add(object.asObject());
//		object.getRole(Anchored.TYPE);
		objects.add(object);
		eventBus.fireEvent(new GraphicsObjectContainerEvent(object, ActionType.ADD));
	}

	@Override
	public void remove(GraphicsObject object) {
		objectGroup.remove(object.asObject());
		objects.remove(object);
		eventBus.fireEvent(new GraphicsObjectContainerEvent(object, ActionType.REMOVE));
	}

	@Override
	public void clear() {
		objectGroup.clear();
		objects.clear();
		eventBus.fireEvent(new GraphicsObjectContainerEvent(ActionType.CLEAR));
	}

	@Override
	public void update(GraphicsObject object) {
		eventBus.fireEvent(new GraphicsObjectContainerEvent(object, ActionType.UPDATE));
	}

	@Override
	public void setSelected(GraphicsObject object, boolean selected) {
		eventBus.fireEvent(new GraphicsObjectSelectedEvent(object, selected));
	}

	@Override
	public void deselectAll() {
		eventBus.fireEvent(new GraphicsObjectSelectedEvent(null, false));
	}

	@Override
	public com.google.web.bindery.event.shared.HandlerRegistration addGraphicsObjectContainerHandler(Handler handler) {
		return eventBus.addHandler(GraphicsObjectContainerEvent.getType(), handler);
	}
	
	@Override
	public com.google.web.bindery.event.shared.HandlerRegistration addGraphicsOperationEventHandler(
			org.geomajas.graphics.client.event.GraphicsOperationEvent.Handler handler) {
		return eventBus.addHandler(GraphicsOperationEvent.getType(), handler);
	}	

	private <H extends EventHandler> HandlerRegistration registerHandler(HasHandlers o, H handler, Type<H> type) {
		if (type == MouseDownEvent.getType()) {
			return ((HasMouseDownHandlers) o).addMouseDownHandler((MouseDownHandler) handler);
		}
		if (type == MouseUpEvent.getType()) {
			return ((HasMouseUpHandlers) o).addMouseUpHandler((MouseUpHandler) handler);
		}
		if (type == MouseMoveEvent.getType()) {
			return ((HasMouseMoveHandlers) o).addMouseMoveHandler((MouseMoveHandler) handler);
		}
		if (type == MouseOverEvent.getType()) {
			return ((HasMouseOverHandlers) o).addMouseOverHandler((MouseOverHandler) handler);
		}
		if (type == MouseOutEvent.getType()) {
			return ((HasMouseOutHandlers) o).addMouseOutHandler((MouseOutHandler) handler);
		}
		if (type == MouseWheelEvent.getType()) {
			return ((HasMouseWheelHandlers) o).addMouseWheelHandler((MouseWheelHandler) handler);
		} else {
			throw new IllegalArgumentException("Unsupported type " + type.getName());
		}
	}

	/**
	 * Invisible background to capture all events. This is necessary to capture events outside the {@link ObjectGroup},
	 * e.g. clicking on background to deactivate all objects.
	 * 
	 */
	class BackGround extends Shape implements HasAllMouseAndClickHandlers {

		public BackGround() {
			setFillOpacity(0);
			setStrokeOpacity(0);
			getElement().getStyle().setCursor(Cursor.DEFAULT);
			drawTransformed();
		}

		@Override
		protected Class<? extends VectorObject> getType() {
			return Rectangle.class;
		}

		@Override
		protected void drawTransformed() {
			getImpl().setX(getElement(), Integer.MIN_VALUE / 200, isAttached());
			getImpl().setY(getElement(), Integer.MIN_VALUE / 200, isAttached());
			getImpl().setWidth(getElement(), Integer.MAX_VALUE / 100);
			getImpl().setHeight(getElement(), Integer.MAX_VALUE / 100);
		}

	}

	/**
	 * The {@link Group} that contains all {@link GraphicsObject}s of this container.
	 * 
	 * @author Jan De Moerloose
	 * 
	 */
	class ObjectGroup extends Group implements HasAllMouseAndClickHandlers {

	}
	
	@Override
	public com.google.web.bindery.event.shared.HandlerRegistration addAddAnchoredLinesHandler(
			org.geomajas.graphics.client.event.AddAnchoredLinesEvent.Handler handler) {
		return eventBus.addHandler(AddAnchoredLinesEvent.getType(), handler);
	}


	@Override
	public void findObjectsAnchoredTo(GraphicsObject object) {
		List<CoordinatePath> coordinatePaths = new ArrayList<CoordinatePath>();
		for (GraphicsObject go : objects) {
			if (go.hasRole(AnchoredTo.TYPE) && go.getRole(AnchoredTo.TYPE).getMasterObject() == object) {
				coordinatePaths.add(go.getRole(AnchoredTo.TYPE).getAnchorLineClone());
			}
		}
		eventBus.fireEvent(new AddAnchoredLinesEvent(object, coordinatePaths));
	}	

}
