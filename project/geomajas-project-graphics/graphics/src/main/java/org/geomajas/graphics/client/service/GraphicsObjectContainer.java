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

import java.util.List;

import org.geomajas.geometry.Bbox;
import org.geomajas.geometry.Coordinate;
import org.geomajas.graphics.client.event.AddAnchoredLinesEvent;
import org.geomajas.graphics.client.event.GraphicsObjectContainerEvent;
import org.geomajas.graphics.client.event.GraphicsOperationEvent;
import org.geomajas.graphics.client.object.GraphicsObject;
import org.geomajas.graphics.client.util.BboxPosition;
import org.vaadin.gwtgraphics.client.VectorObjectContainer;

import com.google.gwt.event.dom.client.HasAllMouseHandlers;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.dom.client.HasDoubleClickHandlers;
import com.google.gwt.event.dom.client.MouseEvent;
import com.google.web.bindery.event.shared.HandlerRegistration;

/**
 * Container of graphics objects.
 * 
 * @author Jan De Moerloose
 * 
 */
public interface GraphicsObjectContainer extends HasAllMouseHandlers, HasClickHandlers, HasDoubleClickHandlers {

	/**
	 * User/screen space as known by GWT graphics objects.
	 * 
	 * @author Jan De Moerloose
	 * 
	 */
	enum Space {
		USER, SCREEN
	}

	/**
	 * Create a new container for rendering graphics (used by controllers).
	 * 
	 * @return a new container
	 */
	VectorObjectContainer createContainer();

	/**
	 * Remove this container from the root container.
	 * 
	 * @param container
	 */
	void removeContainer(VectorObjectContainer container);

	/**
	 * Get all the objects of this container.
	 * 
	 * @return the list of objects (in creation order)
	 */
	List<GraphicsObject> getObjects();

	/**
	 * Add an object to the container.
	 * 
	 * @param object
	 *            the object
	 */
	void add(GraphicsObject object);

	/**
	 * Remove an object from the container.
	 * 
	 * @param object
	 */
	void remove(GraphicsObject object);

	/**
	 * Clear all objects from the container.
	 */
	void clear();

	/**
	 * Notify the container that an object has been updated (and should possibly be re-rendered).
	 * 
	 * @param object
	 */
	void update(GraphicsObject object);

	/**
	 * Select an object in the container.
	 * 
	 * @param object
	 * @param selected
	 */
	void setSelected(GraphicsObject object, boolean selected);

	/**
	 * Deselect all objects.
	 */
	void deselectAll();

	/**
	 * Add a handler that listens to CRUD operations on {@link GraphicsObject} objects.
	 * 
	 * @param handler
	 * @return
	 */
	HandlerRegistration addGraphicsObjectContainerHandler(GraphicsObjectContainerEvent.Handler handler);

	/**
	 * Add handler that listens to operations on the container.
	 * 
	 * @param handler
	 * @return
	 */
	HandlerRegistration addGraphicsOperationEventHandler(GraphicsOperationEvent.Handler handler);

	/**
	 * Get the screen coordinate of this event.
	 * 
	 * @param event
	 * @return
	 */
	Coordinate getScreenCoordinate(MouseEvent<?> event);

	/**
	 * Transform a coordinate between user/screen space.
	 * 
	 * @param coordinate
	 * @param from
	 * @param to
	 * @return
	 */
	Coordinate transform(Coordinate coordinate, Space from, Space to);

	/**
	 * Transform a bounding box between user/screen space.
	 * 
	 * @param bounds
	 * @param from
	 * @param to
	 * @return
	 */
	Bbox transform(Bbox bounds, Space from, Space to);

	/**
	 * Transform a bounding box position between user/screen space.
	 * 
	 * @param position
	 * @param from
	 * @param to
	 * @return
	 */
	BboxPosition transform(BboxPosition position, Space from, Space to);

	/**
	 * Get the background as an observable for mouse events. All events that are not captured by one of the objects are
	 * sent.
	 * 
	 * @return
	 */
	HasAllMouseAndClickHandlers getBackGround();

	/**
	 * Get the object group as an observable for mouse events. All events that are captured by one of the objects are
	 * sent.
	 * 
	 * @return
	 */
	HasAllMouseAndClickHandlers getObjectGroup();

	HandlerRegistration addAddAnchoredLinesHandler(AddAnchoredLinesEvent.Handler handler);

	void findObjectsAnchoredTo(GraphicsObject object);

	/**
	 * Check if this event has an object as its source.
	 * 
	 * @param event
	 * @return
	 */
	boolean isObject(MouseEvent<?> event);

	/**
	 * Get the source object of this event (or null if the event was not fired by an object).
	 * @param event
	 * @return
	 */
	GraphicsObject getObject(MouseEvent<?> event);

	/**
	 * Check if this event has the background as its source.
	 * @param event
	 * @return
	 */
	boolean isBackGround(MouseEvent<?> event);
}
