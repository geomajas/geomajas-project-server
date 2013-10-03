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
package org.geomajas.graphics.client.object.labeler;

import org.geomajas.graphics.client.object.Resizable;
import org.geomajas.graphics.client.object.ResizableAwareRole;
import org.geomajas.graphics.client.object.role.Labeled;
import org.geomajas.graphics.client.object.role.RoleType;
import org.geomajas.graphics.client.object.role.Textable;
import org.geomajas.graphics.client.shape.AnchoredText;
import org.vaadin.gwtgraphics.client.VectorObject;

/**
 * Implementation of {@link Labeled} role for {@link Resizable} objects.
 * 
 * @author Jan De Moerloose
 * 
 */
public class ResizableLabeler implements Labeled, ResizableAwareRole<Labeled> {

	private ResizableTextable resTextable;

	public ResizableLabeler() {
		this((String) null);
	}

	public ResizableLabeler(String label) {
		this(new ResizableTextable(label));
	}
	
	public ResizableLabeler(ResizableTextable restTextable) {
		this.resTextable = restTextable;
	}

	@Override
	public VectorObject asObject() {
		return resTextable.asObject();
	}
	
	public AnchoredText getInternalLabel() {
		return resTextable.getInternalLabel();
	}

	@Override
	public void setResizable(Resizable resizable) {
		resTextable.setResizable(resizable);
	}

	@Override
	public void onUpdate() {
		centerText();
	}

	private void centerText() {
		resTextable.onUpdate();
	}

	@Override
	public Labeled asRole() {
		return this;
	}

	@Override
	public RoleType<Labeled> getType() {
		return Labeled.TYPE;
	}

	@Override
	public ResizableAwareRole<Labeled> cloneRole(Resizable resizable) {
		ResizableTextable resTextClone = (ResizableTextable) resTextable.cloneRole(resizable);
		ResizableLabeler clone = new ResizableLabeler(resTextClone);
		clone.asObject().setVisible((resTextable.asObject().isVisible()));
		return clone;
	}

	
	public Resizable getResizabel() {
		return resTextable.getResizabel();
	}

	@Override
	public void setLabelVisible(boolean visible) {
		asObject().setVisible(visible);
	}

	@Override
	public Textable getTextable() {
		return resTextable;
	}
	
	@Override
	public void setTextable(ResizableTextable textable) {
		resTextable = (ResizableTextable) textable;
	}
}
