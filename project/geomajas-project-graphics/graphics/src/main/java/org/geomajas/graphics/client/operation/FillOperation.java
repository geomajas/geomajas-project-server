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
import org.geomajas.graphics.client.object.role.Fillable;

/**
 * Operation that applies fill to an object.
 * 
 * @author Jan De Moerloose
 * 
 */
public class FillOperation implements GraphicsOperation {

	private String beforeFillColor;

	private double beforeFillOpacity;

	private String afterFillColor;

	private double afterFillOpacity;

	private GraphicsObject fillable;

	public FillOperation(GraphicsObject fillable, String beforeFillColor,
			double beforeFillOpacity, String afterFillColor, double afterFillOpacity) {
		this.fillable = fillable;
		this.beforeFillColor = beforeFillColor;
		this.beforeFillOpacity = beforeFillOpacity;
		this.afterFillColor = afterFillColor;
		this.afterFillOpacity = afterFillOpacity;
	}

	@Override
	public void execute() {
		asFillable().setFillColor(afterFillColor);
		asFillable().setFillOpacity(afterFillOpacity);
	}

	private Fillable asFillable() {
		return fillable.getRole(Fillable.TYPE);
	}

	@Override
	public void undo() {
		asFillable().setFillColor(beforeFillColor);
		asFillable().setFillOpacity(beforeFillOpacity);
	}

	@Override
	public GraphicsObject getObject() {
		return fillable;
	}

	@Override
	public Type getType() {
		return Type.UPDATE;
	}
}
