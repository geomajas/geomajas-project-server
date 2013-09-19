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
import org.geomajas.graphics.client.object.role.Strokable;

/**
 * Operation that applies stroke to an object.
 * 
 * @author Jan De Moerloose
 * 
 */
public class StrokeOperation implements GraphicsOperation {

	private String beforeStrokeColor;

	private double beforeStrokeOpacity;
	
	private int beforeStrokeWidth;

	private String afterStrokeColor;

	private double afterStrokeOpacity;
	
	private int afterStrokeWidth;

	private GraphicsObject strokeable;

	public StrokeOperation(GraphicsObject strokeable, String beforeStrokeColor,
			double beforeStrokeOpacity, int beforeStrokeWidth, String afterStrokeColor, double afterStrokeOpacity,
			int afterStrokeWidth) {
		this.strokeable = strokeable;
		this.beforeStrokeColor = beforeStrokeColor;
		this.beforeStrokeOpacity = beforeStrokeOpacity;
		this.beforeStrokeWidth = beforeStrokeWidth;
		this.afterStrokeColor = afterStrokeColor;
		this.afterStrokeOpacity = afterStrokeOpacity;
		this.afterStrokeWidth = afterStrokeWidth;
	}

	@Override
	public void execute() {
		asStrokable().setStrokeColor(afterStrokeColor);
		asStrokable().setStrokeOpacity(afterStrokeOpacity);
		asStrokable().setStrokeWidth(afterStrokeWidth);
	}

	private Strokable asStrokable() {
		return strokeable.getRole(Strokable.TYPE);
	}

	@Override
	public void undo() {
		asStrokable().setStrokeColor(beforeStrokeColor);
		asStrokable().setStrokeOpacity(beforeStrokeOpacity);
		asStrokable().setStrokeWidth(beforeStrokeWidth);
	}

	@Override
	public GraphicsObject getObject() {
		return strokeable;
	}

	@Override
	public Type getType() {
		return Type.UPDATE;
	}

}
