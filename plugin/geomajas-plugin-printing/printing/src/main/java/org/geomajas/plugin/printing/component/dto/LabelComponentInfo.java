/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2011 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */
package org.geomajas.plugin.printing.component.dto;

import java.io.Serializable;

import org.geomajas.configuration.FontStyleInfo;
import org.geomajas.annotation.Api;

/**
 * DTO object for LabelComponent.
 * 
 * @author Jan De Moerloose
 * @see org.geomajas.plugin.printing.component.LabelComponent
 * @since 2.0.0
 */
@Api(allMethods = true)
public class LabelComponentInfo extends PrintComponentInfo implements Serializable {

	private static final long serialVersionUID = 200L;

	private FontStyleInfo font;

	private String text;

	private String fontColor;

	private String backgroundColor;

	private String borderColor;

	private boolean textOnly;

	private float lineWidth;

	private float margin;

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public FontStyleInfo getFont() {
		return font;
	}

	public void setFont(FontStyleInfo font) {
		this.font = font;
	}

	public String getFontColor() {
		return fontColor;
	}

	public void setFontColor(String fontColor) {
		this.fontColor = fontColor;
	}

	public String getBackgroundColor() {
		return backgroundColor;
	}

	public void setBackgroundColor(String backgroundColor) {
		this.backgroundColor = backgroundColor;
	}

	public String getBorderColor() {
		return borderColor;
	}

	public void setBorderColor(String borderColor) {
		this.borderColor = borderColor;
	}

	public boolean isTextOnly() {
		return textOnly;
	}

	public void setTextOnly(boolean textOnly) {
		this.textOnly = textOnly;
	}

	public float getLineWidth() {
		return lineWidth;
	}

	public void setLineWidth(float lineWidth) {
		this.lineWidth = lineWidth;
	}

	public float getMargin() {
		return margin;
	}

	public void setMargin(float margin) {
		this.margin = margin;
	}

}
