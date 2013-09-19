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

import org.geomajas.geometry.Bbox;
import org.geomajas.geometry.Coordinate;
import org.geomajas.graphics.client.object.GraphicsObject;
import org.geomajas.graphics.client.operation.GraphicsOperation;
import org.geomajas.graphics.client.service.GraphicsObjectContainer.Space;
import org.geomajas.graphics.client.util.BboxPosition;
import org.geomajas.graphics.client.util.Interruptible;
import org.vaadin.gwtgraphics.client.VectorObjectContainer;

import com.google.gwt.event.dom.client.MouseEvent;

/**
 * Useful base class for {@link GraphicsController}.
 * 
 * @author Jan De Moerloose
 * 
 */
public abstract class AbstractGraphicsController implements GraphicsController, Interruptible {

	private GraphicsServiceImpl graphicsService;

	private GraphicsObject object;

	public AbstractGraphicsController(GraphicsService graphicsService) {
		this(graphicsService, null);
	}

	public AbstractGraphicsController(GraphicsService graphicsService, GraphicsObject object) {
		this.graphicsService = (GraphicsServiceImpl) graphicsService;
		this.object = object;
	}

	protected GraphicsObject getObject() {
		return object;
	}

	protected VectorObjectContainer createContainer() {
		return graphicsService.createContainer();
	}

	protected void removeContainer(VectorObjectContainer container) {
		graphicsService.removeContainer(container);
	}

	protected void execute(GraphicsOperation operation) {
		graphicsService.execute(operation);
	}

	protected GraphicsService getService() {
		return graphicsService;
	}

	protected GraphicsObjectContainer getObjectContainer() {
		return graphicsService.getObjectContainer();
	}

	protected Coordinate getScreenCoordinate(MouseEvent<?> event) {
		return graphicsService.getObjectContainer().getScreenCoordinate(event);
	}

	protected Coordinate getUserCoordinate(MouseEvent<?> event) {
		return transform(graphicsService.getObjectContainer().getScreenCoordinate(event), Space.SCREEN, Space.USER);
	}

	protected Coordinate transform(Coordinate coordinate, Space from, Space to) {
		return graphicsService.getObjectContainer().transform(coordinate, from, to);
	}

	protected Bbox transform(Bbox bbox, Space from, Space to) {
		return graphicsService.getObjectContainer().transform(bbox, from, to);
	}

	protected BboxPosition transform(BboxPosition position, Space from, Space to) {
		return graphicsService.getObjectContainer().transform(position, from, to);
	}

	// Interruptible interface: create hooks
	public void cancel() {
	}

	public void stop() {
	}

	public void save() {
	}

	public void pause() {
	}

	public void resume() {
	}

	public boolean isInterrupted() {
		return false;
	}

	public boolean isInProgress() {
		return false;
	}
	
	public void start() {
	}
}
