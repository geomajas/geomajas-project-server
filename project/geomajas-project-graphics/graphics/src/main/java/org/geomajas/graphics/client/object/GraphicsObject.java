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
package org.geomajas.graphics.client.object;

import org.geomajas.graphics.client.object.role.Renderable;
import org.geomajas.graphics.client.object.role.RoleType;
import org.vaadin.gwtgraphics.client.Transparent;


/**
 * Common interface for all graphics objects. All objects can react to mouse events through their {@link Renderable}
 * representation. A graphics object can support any number of roles.
 * 
 * @author Jan De Moerloose
 * 
 */
public interface GraphicsObject extends Renderable, Cloneable, Transparent {

	boolean hasRole(RoleType<?> type);

	<T> T getRole(RoleType<T> type);
	
}
