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
public interface GraphicsObjectContainer extends HasAllMouseHandlers, HasClickHandlers,
		HasDoubleClickHandlers {

	/**
	 * User/screen space as known by GWT graphics objects.
	 * 
	 * @author Jan De Moerloose
	 * 
	 */
	enum Space {
		USER, SCREEN
	}

	VectorObjectContainer createContainer();

	void removeContainer(VectorObjectContainer container);

	List<GraphicsObject> getObjects();

	void add(GraphicsObject object);

	void remove(GraphicsObject object);
	
	void clear();

	void update(GraphicsObject object);
	
	void setSelected(GraphicsObject object, boolean selected);

	void deselectAll();

	HandlerRegistration addGraphicsObjectContainerHandler(GraphicsObjectContainerEvent.Handler handler);
	
	HandlerRegistration addGraphicsOperationEventHandler(GraphicsOperationEvent.Handler handler);

	Coordinate getScreenCoordinate(MouseEvent<?> event);

	Coordinate transform(Coordinate coordinate, Space from, Space to);

	Bbox transform(Bbox bounds, Space from, Space to);

	BboxPosition transform(BboxPosition position, Space from, Space to);

	HasAllMouseAndClickHandlers getBackGround();

	HasAllMouseAndClickHandlers getObjectGroup();
	
	HandlerRegistration addAddAnchoredLinesHandler(AddAnchoredLinesEvent.Handler handler);
	
	void findObjectsAnchoredTo(GraphicsObject object);
}
