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
package org.geomajas.graphics.client.object.role;

import org.geomajas.geometry.Coordinate;
import org.geomajas.graphics.client.object.ExternalLabel;
import org.geomajas.graphics.client.object.GraphicsObject;

/**
 * Implemented by labeled graphics objects.
 * 
 * @author Jan De Moerloose
 * @author Jan Venstermans
 * 
 */
public interface ExternalizableLabeled {
	
	RoleType<ExternalizableLabeled> TYPE = new RoleType<ExternalizableLabeled>("ExternalizableLabeled");
	
	Labeled getLabeled();
		
	void setLabelExternal(boolean labelExternal);
	
	boolean isLabelExternal();
	
	void setPositionExternalLabel(Coordinate coordinate);
	
	Coordinate getPositionExternalLabel();
	
	void setExternalLabel(ExternalLabel exLabel);
	
	ExternalLabel getExternalLabel();
	
	GraphicsObject getMasterObject();
}
