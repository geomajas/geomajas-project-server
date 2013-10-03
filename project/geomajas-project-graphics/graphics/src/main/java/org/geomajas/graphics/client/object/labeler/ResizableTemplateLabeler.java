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
import org.geomajas.graphics.client.object.role.RoleType;
import org.geomajas.graphics.client.object.role.TemplateLabeled;
import org.geomajas.graphics.client.object.role.Textable;
import org.vaadin.gwtgraphics.client.VectorObject;

/**
 * Implementation of {@link TemplateLabeled} role for {@link Resizable} objects.
 * 
 * @author Jan Venstermans
 * 
 */
public class ResizableTemplateLabeler extends ResizableTextable implements TemplateLabeled {

	private String templateText;
	
	private ResizableTextable resTextable;

	public ResizableTemplateLabeler(ResizableTextable resTextable) {
		this.resTextable = resTextable;
		if (resTextable.getLabel() != null) {
			templateText = resTextable.getLabel();
		}
	}
	
	@Override
	public TemplateLabeled asRole() {
		return this;
	}

	@Override
	public RoleType<Textable> getType() {
		return Textable.TYPE;
	}

	@Override
	public ResizableAwareRole<Textable> cloneRole(Resizable resizable) {
		ResizableTemplateLabeler clone = new ResizableTemplateLabeler(
				(ResizableTextable) resTextable.cloneRole(resizable));
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
		return resTextable.getLabel();
	}

	public void setLabelRenderedText(String renderedText) {
		resTextable.setLabel(renderedText);
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

	@Override
	public void setFontColor(String color) {
		resTextable.setFontColor(color);
	}

	@Override
	public String getFontColor() {
		return resTextable.getFontColor();
	}

	@Override
	public void setFontSize(int size) {
		resTextable.setFontSize(size);
	}

	@Override
	public int getFontSize() {
		return resTextable.getFontSize();
	}

	@Override
	public void setFontFamily(String font) {
		resTextable.setFontFamily(font);
	}

	@Override
	public String getFontFamily() {
		return resTextable.getFontFamily();
	}

	@Override
	public void onUpdate() {
		resTextable.onUpdate();
	}

	@Override
	public void setResizable(Resizable resizable) {
		resTextable.setResizable(resizable);
	}

	@Override
	public VectorObject asObject() {
		return resTextable.asObject();
	}
}
