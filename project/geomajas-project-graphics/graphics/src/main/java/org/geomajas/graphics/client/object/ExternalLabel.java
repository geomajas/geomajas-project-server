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

import org.geomajas.geometry.Coordinate;
import org.geomajas.graphics.client.object.role.ExternalizableLabeled;


/**
 * The {@link ExternalLabel} is a special GText object. It is linked to a {@link Resizable object} via the
 * {@link ResizableLabeler} class. Specificity: all common font and label functionalities are first dispaced to the
 * {@link ResizableLabeler} class; there they are inserted in both the (internal) {@link AnchoredText} and the
 * external {@link ExternalLabel} object.
 * 
 * @author Jan Venstermans
 * 
 */
public class ExternalLabel extends GText {
	
	private ExternalizableLabeled labeled;
	
	public ExternalLabel(ExternalizableLabeled labeled) {
		super(0, 0, "");
		this.labeled = labeled;
		addResizableBorderer();
	}
	
	@Override
	public void setFontSize(int size) {
		labeled.setFontSize(size);
	}
	
	public void setFontSizeExternalLabelOnly(int size) {
		super.setFontSize(size);
	}

	@Override
	public void setFontFamily(String font) {
		labeled.setFontFamily(font);
	}
	
	public void setFontFamilyExternalLabelOnly(String font) {
		super.setFontFamily(font);
	}

	@Override
	public void setFontColor(String color) {
		labeled.setFontColor(color);
	}
	
	public void setFontColorExternalLabelOnly(String color) {
		super.setFontColor(color);
	}
	
	@Override
	public void setLabel(String label) {
		labeled.setLabel(label);
	}
	
	public void setLabelExternalLabelOnly(String label) {
		super.setLabel(label);
	}
	
	@Override
	public void setPosition(Coordinate coord) {
		super.setPosition(coord);
	}
	
	public ExternalizableLabeled getExternalizableLabeled() {
		return labeled;
	}
}
