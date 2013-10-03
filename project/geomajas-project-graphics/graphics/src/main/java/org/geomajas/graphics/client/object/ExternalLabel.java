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

import org.geomajas.graphics.client.object.role.ExternalizableLabeled;
import org.geomajas.graphics.client.object.role.TemplateLabeled;
import org.geomajas.graphics.client.object.role.Textable;


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
	
	private ExternalizableLabeled externalizableLabeled;
	
	public ExternalLabel(ExternalizableLabeled labeled) {
		super(0, 0, "");
		this.externalizableLabeled = labeled;
	}
	
	public ExternalizableLabeled getExternalizableLabeled() {
		return externalizableLabeled;
	}
	
	@Override
	public void setFontSize(int size) {
		externalizableLabeled.getLabeled().getTextable().setFontSize(size);
		super.setFontSize(externalizableLabeled.getLabeled().getTextable().getFontSize());
	}
	
	public void setFontSizeExternalLabelOnly(int size) {
		super.setFontSize(size);
	}

	@Override
	public void setFontFamily(String font) {
		externalizableLabeled.getLabeled().getTextable().setFontFamily(font);
		super.setFontFamily(externalizableLabeled.getLabeled().getTextable().getFontFamily());
	}
	
	public void setFontFamilyExternalLabelOnly(String font) {
		super.setFontFamily(font);
	}

	@Override
	public void setFontColor(String color) {
		externalizableLabeled.getLabeled().getTextable().setFontColor(color);
		super.setFontColor(externalizableLabeled.getLabeled().getTextable().getFontColor());
	}
	
	public void setFontColorExternalLabelOnly(String color) {
		super.setFontColor(color);
	}
	
	@Override
	public void setLabel(String label) {
		externalizableLabeled.getLabeled().getTextable().setLabel(label);
		Textable textable = externalizableLabeled.getLabeled().getTextable();
		super.setLabel(textable instanceof TemplateLabeled ? 
				((TemplateLabeled) textable).getLabelRenderedText() : textable.getLabel());
	}
	
	public void setLabelExternalLabelOnly(String label) {
		super.setLabel(label);
	}
	
}
