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
package org.geomajas.graphics.client.operation;

import org.geomajas.geometry.Coordinate;
import org.geomajas.graphics.client.object.Draggable;
import org.geomajas.graphics.client.object.GraphicsObject;

/**
 * Operation that drags an object.
 * 
 * @author Jan De Moerloose
 * 
 */
public class DragOperation implements GraphicsOperation {

	private Coordinate beforePosition;

	private Coordinate afterPosition;

	private GraphicsObject draggable;

	public DragOperation(GraphicsObject draggable, Coordinate beforePosition,
			Coordinate afterPosition) {
		this.beforePosition = beforePosition;
		this.afterPosition = afterPosition;
		this.draggable = draggable;
	}

	@Override
	public void execute() {
		asDraggable().setPosition(afterPosition);
	}

	@Override
	public void undo() {
		asDraggable().setPosition(beforePosition);
	}

	private Draggable asDraggable() {
		return draggable.getRole(Draggable.TYPE);
	}

	@Override
	public GraphicsObject getObject() {
		return draggable;
	}

	@Override
	public Type getType() {
		return Type.UPDATE;
	}

}
