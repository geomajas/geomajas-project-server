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

import org.geomajas.geometry.Coordinate;
import org.geomajas.geometry.service.BboxService;
import org.geomajas.graphics.client.object.Resizable;
import org.geomajas.graphics.client.object.ResizableAwareRole;
import org.geomajas.graphics.client.object.role.Labeled;
import org.geomajas.graphics.client.object.role.RoleType;
import org.geomajas.graphics.client.shape.AnchoredText;
import org.vaadin.gwtgraphics.client.VectorObject;

/**
 * Implementation of {@link ExternalizableLabeled} role for {@link Resizable} objects.
 * 
 * @author Jan De Moerloose
 * 
 */
public class ResizableLabeler implements Labeled, ResizableAwareRole<Labeled> {

	private Resizable resizable;

	private AnchoredText text;

	public ResizableLabeler() {
		this(null);
	}

	public ResizableLabeler(String label) {
		String labelText = label != null ? label : "";
		text = new AnchoredText(0, 0, labelText, 0.5, 0.5);
		text.setFillColor("#000000");
		text.setStrokeWidth(0);
	}

	@Override
	public VectorObject asObject() {
		return text;
	}
	
	public AnchoredText getInternalLabel() {
		return text;
	}

	@Override
	public void setResizable(Resizable resizable) {
		this.resizable = resizable;
	}

	@Override
	public void onUpdate() {
		centerText();
	}

	@Override
	public void setLabel(String label) {
		text.setText(label);
		centerText();
	}

	@Override
	public String getLabel() {
		return text.getText();
	}

	private void centerText() {
		Coordinate center = BboxService.getCenterPoint(resizable.getUserBounds());
		text.setUserX(center.getX());
		text.setUserY(center.getY());
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
		ResizableLabeler clone = new ResizableLabeler();
		clone.setResizable(resizable);
		clone.setLabel(getLabel());
		return clone;
	}

	@Override
	public void setFontSize(int size) {
		text.setFontSize(size);
	}

	@Override
	public int getFontSize() {
		return text.getFontSize();
	}

	@Override
	public void setFontFamily(String font) {
		text.setFontFamily(font);
	}

	@Override
	public String getFontFamily() {
		return text.getFontFamily();
	}

	@Override
	public void setFontColor(String color) {
		text.setFillColor(color);
	}

	@Override
	public String getFontColor() {
		return text.getFillColor();
	}
	
	public Resizable getResizabel() {
		return resizable;
	}

	@Override
	public void setLabelVisible(boolean visible) {
		text.setVisible(visible);
	}
}
