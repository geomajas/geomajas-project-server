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
import org.vaadin.gwtgraphics.client.VectorObject;

/**
 * Implementation of {@link Renderable} role for {@link Resizable} objects. Renders the actual object.
 * 
 * @author Jan De Moerloose
 * 
 */
public class ResizableRenderer implements Renderable, ResizableAwareRole<Renderable> {

	private Resizable resizable;

	@Override
	public void setResizable(Resizable resizable) {
		this.resizable = resizable;
	}

	@Override
	public Renderable asRole() {
		return this;
	}

	@Override
	public RoleType<Renderable> getType() {
		return Renderable.TYPE;
	}

	@Override
	public VectorObject asObject() {
		return resizable.asObject();
	}

	@Override
	public void onUpdate() {
		
	}

	@Override
	public ResizableAwareRole<Renderable> cloneRole(Resizable resizable) {
		ResizableRenderer clone = new ResizableRenderer();
		clone.setResizable(resizable);
		return clone;
	}

}
