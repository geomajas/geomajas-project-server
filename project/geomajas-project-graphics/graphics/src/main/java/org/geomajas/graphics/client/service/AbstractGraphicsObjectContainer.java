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
import org.geomajas.graphics.client.event.GraphicsObjectContainerEvent.ActionType;
import org.geomajas.graphics.client.event.GraphicsObjectContainerEvent.Handler;
import org.geomajas.graphics.client.event.GraphicsObjectSelectedEvent;
import org.geomajas.graphics.client.event.GraphicsOperationEvent;
import org.geomajas.graphics.client.object.GraphicsObject;
import org.geomajas.graphics.client.object.anchor.AnchoredTo;
import org.geomajas.graphics.client.object.role.HtmlRenderable;
import org.geomajas.graphics.client.shape.CoordinatePath;
import org.vaadin.gwtgraphics.client.Group;
import org.vaadin.gwtgraphics.client.VectorObjectContainer;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.DomEvent;
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
import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Widget;
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

	private HasHandlerVectorObjectContainer rootGroup;

	private HasHandlerWidget backGround;

	private ObjectGroup objectGroup = new ObjectGroup();

	private EventBus eventBus;

	private List<GraphicsObject> objects = new ArrayList<GraphicsObject>();

	private HasWidgets.ForIsWidget widgetContainer;

	protected AbstractGraphicsObjectContainer(EventBus eventBus) {
		this.eventBus = eventBus;
	}

	protected AbstractGraphicsObjectContainer(EventBus eventBus, VectorObjectContainer rootGroup,
			Widget backGroundWidget) {
		this.eventBus = eventBus;
		setRootContainer(rootGroup);
		setBackGround(backGroundWidget);
	}

	public void setBackGround(Widget backGroundWidget) {
		backGround = new HasHandlerWidget(backGroundWidget, true);
	}

	public void setRootContainer(VectorObjectContainer rootGroup) {
		this.rootGroup = new HasHandlerVectorObjectContainer(rootGroup, true);
		this.rootGroup.add(objectGroup);
	}

	public void setWidgetContainer(HasWidgets.ForIsWidget widgetContainer) {
		this.widgetContainer = widgetContainer;
	}

	@Override
	public HasAllMouseAndClickHandlers getObjectGroup() {
		return objectGroup;
	}

	@Override
	public HasHandlerWidget getBackGround() {
		return backGround;
	}

	@Override
	public boolean isObject(MouseEvent<?> event) {
		for (GraphicsObject object : objects) {
			if (object.asObject() == event.getSource()) {
				return true;
			} else if (object.hasRole(HtmlRenderable.TYPE)) {
				if (object.getRole(HtmlRenderable.TYPE).asWidget() == event.getSource()) {
					return true;
				}
			}
		}
		return false;
	}

	@Override
	public GraphicsObject getObject(MouseEvent<?> event) {
		for (GraphicsObject object : objects) {
			if (object.asObject() == event.getSource()) {
				return object;
			} else if (object.hasRole(HtmlRenderable.TYPE)) {
				if (object.getRole(HtmlRenderable.TYPE).asWidget() == event.getSource()) {
					return object;
				}
			}
		}
		return null;
	}

	@Override
	public boolean isBackGround(MouseEvent<?> event) {
		return backGround.asWidget() == event.getSource();
	}

	@Override
	public void fireEvent(GwtEvent<?> event) {
		backGround.fireEvent(event);
	}

	@Override
	public HandlerRegistration addMouseDownHandler(final MouseDownHandler handler) {
		return registerRootAndBackGround(handler, MouseDownEvent.getType());
	}

	@Override
	public HandlerRegistration addMouseUpHandler(MouseUpHandler handler) {
		return registerRootAndBackGround(handler, MouseUpEvent.getType());
	}

	@Override
	public HandlerRegistration addMouseOutHandler(MouseOutHandler handler) {
		return registerRootAndBackGround(handler, MouseOutEvent.getType());
	}

	@Override
	public HandlerRegistration addMouseOverHandler(MouseOverHandler handler) {
		return registerRootAndBackGround(handler, MouseOverEvent.getType());
	}

	@Override
	public HandlerRegistration addMouseMoveHandler(MouseMoveHandler handler) {
		return registerRootAndBackGround(handler, MouseMoveEvent.getType());
	}

	@Override
	public HandlerRegistration addMouseWheelHandler(MouseWheelHandler handler) {
		return registerRootAndBackGround(handler, MouseWheelEvent.getType());
	}

	@Override
	public HandlerRegistration addClickHandler(ClickHandler handler) {
		return registerRootAndBackGround(handler, ClickEvent.getType());
	}

	@Override
	public HandlerRegistration addDoubleClickHandler(DoubleClickHandler handler) {
		return registerRootAndBackGround(handler, DoubleClickEvent.getType());
	}

	private <H extends EventHandler> HandlerRegistration registerRootAndBackGround(H handler, DomEvent.Type<H> type) {
		final HandlerRegistration rootRegistration = rootGroup.addDomHandler(handler, type);
		final HandlerRegistration bgRegistration = backGround.addDomHandler(handler, type);
		return new HandlerRegistration() {

			@Override
			public void removeHandler() {
				rootRegistration.removeHandler();
				bgRegistration.removeHandler();
			}
		};
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
		if (object.hasRole(HtmlRenderable.TYPE)) {
			if (widgetContainer != null) {
				widgetContainer.add(object.getRole(HtmlRenderable.TYPE).asWidget());
			}
		}
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
