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

import org.geomajas.graphics.client.object.GraphicsObject;
import org.geomajas.graphics.client.object.ExternalLabel;
import org.geomajas.graphics.client.object.role.Labeled;
import org.geomajas.graphics.client.object.role.TemplateLabeled;
import org.geomajas.graphics.client.operation.LabelOperation;

/**
 * {@link Editor} for the {@link TemplateLabeled} role.
 * 
 * @author Jan De Moerloose
 * 
 */
public class TemplateLabelEditor extends LabelEditor {
	
	@Override
	public boolean supports(GraphicsObject object) {
		// same editor for both parent and external object
		return (object.hasRole(Labeled.TYPE) && object.getRole(Labeled.TYPE) instanceof TemplateLabeled)
				|| (object instanceof ExternalLabel && 
						((ExternalLabel) object).getExternalizableLabeled() instanceof TemplateLabeled);
	}

	@Override
	public void setObject(GraphicsObject object) {
		if (object instanceof ExternalLabel) {
			TemplateLabeled templLabeled = ((TemplateLabeled) ((ExternalLabel) object).getExternalizableLabeled());
			this.object = (GraphicsObject) templLabeled.getMasterObject();
		} else {
			this.object = object;
		}
		TemplateLabeled label = (TemplateLabeled) this.object.getRole(Labeled.TYPE);
		labelBox.setText(label.getLabel());
		fillColorBox.setText(label.getFontColor());
		fontSize.setText(label.getFontSize()  + "");
		fontFamily.setText(label.getFontFamily());
		labelBox.setText(label.getLabel());
	}
	
	public void onOk() {
		TemplateLabeled label = (TemplateLabeled) this.object.getRole(Labeled.TYPE);
		String beforeLabelTemplateText = label.getLabelTemplateText();
		String beforeColor = label.getFontColor();
		int beforeSize = label.getFontSize();
		String beforeFont = label.getFontFamily();
		service.execute(new LabelOperation(object, null, beforeLabelTemplateText,
				beforeColor, beforeSize, beforeFont, labelBox.getText(), fillColorBox
						.getText(), Integer.parseInt(fontSize.getText()), fontFamily.getText()));
	}
	
	@Override
	public String getLabel() {
		return "Edit template text";
	}
}
