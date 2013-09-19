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

import org.geomajas.geometry.Bbox;
import org.geomajas.graphics.client.object.GraphicsObject;
import org.geomajas.graphics.client.object.Resizable;
import org.geomajas.graphics.client.util.FlipState;

/**
 * Operation that resizes an object.
 * 
 * @author Jan De Moerloose
 * 
 */
public class ResizeOperation implements GraphicsOperation {

	private Bbox beforeBox;

	private Bbox afterBox;

	private FlipState flipState;

	private GraphicsObject resizable;

	public ResizeOperation(GraphicsObject resizable, Bbox beforeBox, Bbox afterBox,
			FlipState flipState) {
		this.beforeBox = beforeBox;
		this.afterBox = afterBox;
		this.flipState = flipState;
		this.resizable = resizable;
	}

	@Override
	public void execute() {
		asResizable().setUserBounds(afterBox);
		asResizable().flip(flipState);
	}

	@Override
	public void undo() {
		asResizable().setUserBounds(beforeBox);
		asResizable().flip(flipState);
	}

	private Resizable asResizable() {
		return resizable.getRole(Resizable.TYPE);
	}

	@Override
	public GraphicsObject getObject() {
		return resizable;
	}

	@Override
	public Type getType() {
		return Type.UPDATE;
	}

}
