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

import org.geomajas.geometry.Bbox;
import org.geomajas.graphics.client.object.role.Fillable;
import org.geomajas.graphics.client.object.role.RoleType;
import org.geomajas.graphics.client.object.role.Strokable;
import org.geomajas.graphics.client.shape.StyledRectangle;
import org.vaadin.gwtgraphics.client.VectorObject;
import org.vaadin.gwtgraphics.client.shape.Rectangle;

/**
 * Implementation of {@link Bordered} role for {@link Resizable} objects.
 * 
 * @author Jan De Moerloose
 * 
 */
public class ResizableBorderer implements Bordered, ResizableAwareRole<Bordered> , Fillable , Strokable {

	private Resizable resizable;

	private Rectangle borderRect;

	public ResizableBorderer() {
		borderRect = new StyledRectangle(0, 0, 0, 0);
		borderRect.setFillColor("#CCFF99");
		borderRect.setStrokeColor("#CCCC99");
	}

	public void setFixedSize(boolean fixedSize) {
		borderRect.setFixedSize(fixedSize);
	}

	public boolean isFixedSize() {
		return borderRect.isFixedSize();
	}

	@Override
	public VectorObject asObject() {
		return borderRect;
	}

	@Override
	public void setResizable(Resizable resizable) {
		this.resizable = resizable;
	}

	@Override
	public void onUpdate() {
		Bbox bounds = resizable.getUserBounds();
		if (borderRect.isFixedSize()) {
			Bbox screenBounds = resizable.getBounds();
			// center on userX, userY !!!
			borderRect.setUserX(bounds.getX() + 0.5 * bounds.getWidth());
			borderRect.setUserY(bounds.getY() + 0.5 * bounds.getHeight());
			borderRect.setUserWidth(screenBounds.getWidth());
			borderRect.setUserHeight(screenBounds.getHeight());
		} else {
			borderRect.setUserX(bounds.getX());
			borderRect.setUserY(bounds.getY());
			borderRect.setUserWidth(bounds.getWidth());
			borderRect.setUserHeight(bounds.getHeight());
		}
	}

	@Override
	public Bordered asRole() {
		return this;
	}

	@Override
	public RoleType<Bordered> getType() {
		return Bordered.TYPE;
	}

	@Override
	public ResizableAwareRole<Bordered> cloneRole(Resizable resizable) {
		ResizableBorderer clone = new ResizableBorderer();
		clone.setResizable(resizable);
		clone.setFixedSize(isFixedSize());
		
		// fillable
		clone.setFillColor(getFillColor());
		clone.setFillOpacity(getFillOpacity()); // not important: will be set to 50% as mask
		
		//strokable
		clone.setStrokeColor(getStrokeColor());
		clone.setStrokeOpacity(getStrokeOpacity()); // not important: will be set to 50% as mask
		clone.setStrokeWidth(getStrokeWidth());
		return clone;
	}

	@Override
	public String getStrokeColor() {
		return borderRect.getStrokeColor();
	}

	@Override
	public void setStrokeColor(String strokeColor) {
		borderRect.setStrokeColor(strokeColor);
	}

	@Override
	public int getStrokeWidth() {
		return borderRect.getStrokeWidth();
	}

	@Override
	public void setStrokeWidth(int strokeWidth) {
		borderRect.setStrokeWidth(strokeWidth);
	}

	@Override
	public double getStrokeOpacity() {
		return borderRect.getStrokeOpacity();
	}

	@Override
	public void setStrokeOpacity(double strokeOpacity) {
		borderRect.setStrokeOpacity(strokeOpacity);
	}

	@Override
	public void setFillColor(String fillColor) {
		borderRect.setFillColor(fillColor);
	}

	@Override
	public void setFillOpacity(double fillOpacity) {
		borderRect.setFillOpacity(fillOpacity);
	}

	@Override
	public String getFillColor() {
		return borderRect.getFillColor();
	}

	@Override
	public double getFillOpacity() {
		return borderRect.getFillOpacity();
	}

}
