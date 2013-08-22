/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2013 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */

package org.geomajas.plugin.printing.component.dto;

import org.geomajas.configuration.FontStyleInfo;
import org.geomajas.annotation.Api;

/**
 * DTO object for LabelComponent.
 * 
 * @author Jan De Moerloose
 * @author An Buyle
 * @see org.geomajas.plugin.printing.component.LabelComponent
 * @since 2.0.0
 */
@Api(allMethods = true)
public class LabelComponentInfo extends PrintComponentInfo {

	private static final long serialVersionUID = 200L;

	private FontStyleInfo font;

	private String text;

	private String fontColor;

	private String backgroundColor;

	private String borderColor;

	private boolean textOnly;

	private float lineWidth;

	private float margin; // (in points)

	/** Max. width of rendered text (in point units) Default is 0.0, meaning there's no limit imposed by the client. */
	private float maxWidthText;

	/**
	 * Get label text.
	 * 
	 * @return text
	 */
	public String getText() {
		return text;
	}

	/**
	 * Set label text.
	 * 
	 * @param text
	 *            text
	 */
	public void setText(String text) {
		this.text = text;
	}

	/**
	 * Get font style.
	 * 
	 * @return font style
	 */
	public FontStyleInfo getFont() {
		return font;
	}

	/**
	 * Set font style.
	 * 
	 * @param font
	 *            font style
	 */
	public void setFont(FontStyleInfo font) {
		this.font = font;
	}

	/**
	 * Get font color.
	 * 
	 * @return font color
	 */
	public String getFontColor() {
		return fontColor;
	}

	/**
	 * Set font color.
	 * 
	 * @param fontColor
	 *            font color
	 */
	public void setFontColor(String fontColor) {
		this.fontColor = fontColor;
	}

	/**
	 * Get background color.
	 * 
	 * @return background color
	 */
	public String getBackgroundColor() {
		return backgroundColor;
	}

	/**
	 * Set background color.
	 * 
	 * @param backgroundColor
	 *            background color
	 */
	public void setBackgroundColor(String backgroundColor) {
		this.backgroundColor = backgroundColor;
	}

	/**
	 * Get border color.
	 * 
	 * @return border color
	 */
	public String getBorderColor() {
		return borderColor;
	}

	/**
	 * Set border color.
	 * 
	 * @param borderColor
	 *            border color
	 */
	public void setBorderColor(String borderColor) {
		this.borderColor = borderColor;
	}

	/**
	 * Should the label be text only, without border.
	 * 
	 * @return text only?
	 */
	public boolean isTextOnly() {
		return textOnly;
	}

	/**
	 * Set whether the label should be text only, without border.
	 * 
	 * @param textOnly
	 *            true when border should not be displayed
	 */
	public void setTextOnly(boolean textOnly) {
		this.textOnly = textOnly;
	}

	/**
	 * Get line width.
	 * 
	 * @return set line width
	 */
	public float getLineWidth() {
		return lineWidth;
	}

	/**
	 * Set line width.
	 * 
	 * @param lineWidth
	 *            line width
	 */
	public void setLineWidth(float lineWidth) {
		this.lineWidth = lineWidth;
	}

	/**
	 * Get margin around the text.
	 * 
	 * @return margin
	 */
	public float getMargin() {
		return margin;
	}

	/**
	 * Set the margin around the text.
	 * 
	 * @param margin
	 *            margin
	 */
	public void setMargin(float margin) {
		this.margin = margin;
	}

	/**
	 * @return the max. width of the rendered text excl. margins (in point units); If 0, it there's no limit imposed by
	 *         the client
	 * @since 2.4.0
	 */
	public float getMaxWidthText() {
		return maxWidthText;
	}

	/**
	 * Set the max. width of the rendered text (in point units); Default is 0, meaning there's no limit imposed by the
	 * client
	 * 
	 * @param maxWidthText
	 *            max. width of the rendered text excl. margin (in point units)
	 * @since 2.4.0
	 */
	public void setMaxWidthText(float maxWidthText) {
		this.maxWidthText = maxWidthText;
	}
}