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

import org.geomajas.graphics.client.object.labeler.ResizableTextable;


/**
 * Implemented by labeled graphics objects.
 * 
 * @author Jan De Moerloose
 * 
 */
public interface Labeled {
	
	RoleType<Labeled> TYPE = new RoleType<Labeled>("Labeled");

	Textable getTextable();
	
	void setTextable(ResizableTextable textable);
	
	void setLabelVisible(boolean visible);
}
