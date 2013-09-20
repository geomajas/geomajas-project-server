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
 * This will be supported by both the object that has the role,
 * as an external label that is associated with that object.s
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
		this.object = object;
		Labeled label = getTemplateLabeled();
		labelBox.setText(label.getLabel());
		fillColorBox.setText(label.getFontColor());
		fontSize.setText(label.getFontSize()  + "");
		fontFamily.setText(label.getFontFamily());
		labelBox.setText(label.getLabel());
	}
	
	public void onOk() {
		TemplateLabeled label = getTemplateLabeled();
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
	
	private TemplateLabeled getTemplateLabeled() {
		Labeled label;
		if (object instanceof ExternalLabel) {
			label  = ((ExternalLabel) object).getExternalizableLabeled();
		} else {
			label = object.getRole(Labeled.TYPE);
		}
		if (label instanceof TemplateLabeled) {
			return (TemplateLabeled) label;
		}
		return null;
	}
}
