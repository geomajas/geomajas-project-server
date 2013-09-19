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

import org.geomajas.graphics.client.object.GraphicsObject;

/**
 * Operation that adds an object.
 * 
 * @author Jan De Moerloose
 * 
 */
public class AddOperation implements GraphicsOperation {

	private GraphicsObject object;
	

	public AddOperation(GraphicsObject object) {
		this.object = object;
	}

	@Override
	public void execute() {
	}

	@Override
	public void undo() {
	}

	@Override
	public GraphicsObject getObject() {
		return object;
	}

	@Override
	public Type getType() {
		return Type.ADD;
	}

}
