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
import org.geomajas.graphics.client.object.role.TemplateLabeled;

/**
 * Implementation of {@link TemplateLabeled} role for {@link Resizable} objects.
 * 
 * @author Jan Venstermans
 * 
 */
public class ResizableTemplateLabeler extends ResizableExternalizableLabeler implements TemplateLabeled {

	private String templateText;

	public ResizableTemplateLabeler() {
		super(null, false);
	}
	
	public ResizableTemplateLabeler(String label, boolean externalText) {
		super(null, externalText);
		if (label != null) {
			templateText = label;
		}
	}
	
	@Override
	public TemplateLabeled asRole() {
		return this;
	}

	@Override
	public RoleType<Labeled> getType() {
		return TemplateLabeled.TYPE;
	}

	@Override
	public ResizableAwareRole<Labeled> cloneRole(Resizable resizable) {
		ResizableTemplateLabeler clone = new ResizableTemplateLabeler();
		clone.setResizable(resizable);
		clone.setLabel(getLabel());
		// you don't want the text of the label be cloned
		clone.setLabelRenderedText("");
		return clone;
	}

	@Override
	public String getLabelTemplateText() {
		return templateText;
	}

	@Override
	public void setLabelTemplateText(String templateText) {
		this.templateText = templateText;
		renderTemplateText();
	}

	public String getLabelRenderedText() {
		return super.getLabel();
	}

	public void setLabelRenderedText(String renderedText) {
		super.setLabel(renderedText);
	}
	
	@Override
	public String getLabel() {
		return getLabelTemplateText();
	}
	
	@Override
	public void setLabel(String label) {
		setLabelTemplateText(label);
	}
	
	// this makes the rendered text from the template text
	public void renderTemplateText() {
		// stub
		setLabelRenderedText("template: " + templateText);
	}
}
