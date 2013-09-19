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
 * Undoable operation on a graphics object.
 * 
 * @author Jan De Moerloose
 * @author Jan Venstermans
 * 
 */
public interface GraphicsOperation {

	void execute();

	void undo();
	
	GraphicsObject getObject();
	
	Type getType();
	
	/**
	 * Different types of Graphics Operation.
	 * 
	 * @author Jan Venstermans
	 * 
	 */
	public enum Type {
		UPDATE,
		ADD,
		REMOVE
	}
}
