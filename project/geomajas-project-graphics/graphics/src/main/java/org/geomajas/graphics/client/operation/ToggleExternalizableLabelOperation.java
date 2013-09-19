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
import org.geomajas.graphics.client.object.role.ExternalizableLabeled;

/**
 * Operation that toggles the label of an object.
 * 
 * @author Jan Venstermans
 * 
 */
public class ToggleExternalizableLabelOperation implements GraphicsOperation {

	private GraphicsObject externalizableLabeled;
	
	public ToggleExternalizableLabelOperation(GraphicsObject externalizableLabeled) {
		this.externalizableLabeled = externalizableLabeled;
	}

	@Override
	public void execute() {
		ExternalizableLabeled exLabel = (ExternalizableLabeled) externalizableLabeled
				.getRole(ExternalizableLabeled.TYPE);
		exLabel.setLabelExternal(!exLabel.isLabelExternal());
	}

	@Override
	public void undo() {
		execute();
	}

	@Override
	public GraphicsObject getObject() {
		return (GraphicsObject) externalizableLabeled;
	}

	@Override
	public Type getType() {
		return Type.UPDATE;
	}

}
