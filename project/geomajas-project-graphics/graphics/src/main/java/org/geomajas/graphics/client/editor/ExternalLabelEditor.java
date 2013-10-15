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
package org.geomajas.graphics.client.editor;

import org.geomajas.graphics.client.object.ExternalLabel;
import org.geomajas.graphics.client.object.GraphicsObject;
import org.geomajas.graphics.client.object.role.Labeled;

/**
 * {@link Editor} for the {@link Labeled} role.
 * 
 * @author Jan De Moerloose
 * 
 */
public class ExternalLabelEditor extends LabelEditor {

	@Override
	public boolean supports(GraphicsObject object) {
		return object instanceof ExternalLabel;
	}
	
	@Override
	public void setObject(GraphicsObject object) {
		// object is the object anchored to
		this.object = (GraphicsObject) ((ExternalLabel) object).getExternalizableLabeled().getMasterObject();
		Labeled label = object.getRole(Labeled.TYPE);
		labelBox.setText(label.getTextable().getLabel());
		fillColorValidator.setLabel(label.getTextable().getFontColor());
		fontSize.setText(label.getTextable().getFontSize()  + "");
		fontFamily.setText(label.getTextable().getFontFamily());
	}
	
	@Override
	public boolean validate() {
		return true;
	}
}
