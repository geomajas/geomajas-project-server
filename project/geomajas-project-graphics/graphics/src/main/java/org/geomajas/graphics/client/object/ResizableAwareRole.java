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

import org.geomajas.graphics.client.object.role.RoleType;
import org.vaadin.gwtgraphics.client.VectorObject;

/**
 * Generic cloneable role for {@link Resizable} objects.
 * 
 * @author Jan De Moerloose
 * 
 * @param <T>
 */
public interface ResizableAwareRole<T> {

	/**
	 * Called when object is updated.
	 */
	void onUpdate();

	/**
	 * Set the resizable for which to apply this role.
	 * 
	 * @param resizable
	 */
	void setResizable(Resizable resizable);

	/**
	 * Get the role.
	 * 
	 * @return
	 */
	T asRole();

	/**
	 * Get the type of this role.
	 * 
	 * @return
	 */
	RoleType<T> getType();

	/**
	 * Get the {@link VectorObject} that contains the graphical representation of this role.
	 * 
	 * @return
	 */
	VectorObject asObject();

	/**
	 * Clone this role.
	 * 
	 * @param resizable
	 * @return
	 */
	ResizableAwareRole<T> cloneRole(Resizable resizable);

}
