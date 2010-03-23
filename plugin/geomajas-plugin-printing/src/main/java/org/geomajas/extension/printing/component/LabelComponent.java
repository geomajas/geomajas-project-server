/*
 * This file is part of Geomajas, a component framework for building
 * rich Internet applications (RIA) with sophisticated capabilities for the
 * display, analysis and management of geographic information.
 * It is a building block that allows developers to add maps
 * and other geographic data capabilities to their web applications.
 *
 * Copyright 2008-2010 Geosparc, http://www.geosparc.com, Belgium
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.geomajas.extension.printing.component;

import com.lowagie.text.Rectangle;
import org.geomajas.extension.printing.parser.ColorAdapter;
import org.geomajas.extension.printing.parser.FontAdapter;
import org.geomajas.extension.printing.PdfContext;

import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.awt.Color;
import java.awt.Font;

/**
 * Inclusion of label in printed document.
 *
 * @author Jan De Moerloose
 */
@XmlRootElement
public class LabelComponent extends BaseComponent {

	/**
	 * The font for the text
	 */
	private Font font;

	/**
	 * Label text
	 */
	private String text;

	/**
	 * Color of the text
	 */
	private Color fontColor;

	/**
	 * Background color
	 */
	private Color backgroundColor;

	/**
	 * Line color
	 */
	private Color borderColor;

	/**
	 * Only text, no border ?
	 */
	private boolean textOnly;

	/**
	 * Border line width
	 */
	private float lineWidth;

	/**
	 * Margin around text
	 */
	private float margin;

	public LabelComponent() {
		this(Color.white, Color.BLACK, new Font("Dialog", Font.PLAIN, 12), Color.black, "<missing text>",
				false, 1f);
	}

	public LabelComponent(Font font, Color fontColor, String text) {
		this(Color.white, Color.BLACK, font, fontColor, text, true, 1f);
	}

	public LabelComponent(Color backgroundColor, Color borderColor, Font font, Color fontColor, String text,
			boolean textOnly, float lineWidth) {
		this.backgroundColor = backgroundColor;
		this.borderColor = borderColor;
		this.font = font;
		this.fontColor = fontColor;
		this.text = text;
		this.textOnly = textOnly;
		this.lineWidth = lineWidth;
	}

	/**
	 * Call back visitor.
	 *
	 * @param visitor
	 */
	public void accept(PrintComponentVisitor visitor) {
		visitor.visit(this);
	}

	@XmlJavaTypeAdapter(ColorAdapter.class)
	public Color getBackgroundColor() {
		return backgroundColor;
	}

	public void setBackgroundColor(Color backgroundColor) {
		this.backgroundColor = backgroundColor;
	}

	@XmlJavaTypeAdapter(ColorAdapter.class)
	public Color getBorderColor() {
		return borderColor;
	}

	public void setBorderColor(Color borderColor) {
		this.borderColor = borderColor;
	}

	@XmlJavaTypeAdapter(ColorAdapter.class)
	public Color getFontColor() {
		return fontColor;
	}

	public void setFontColor(Color fontColor) {
		this.fontColor = fontColor;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	@XmlJavaTypeAdapter(FontAdapter.class)
	public Font getFont() {
		return font;
	}

	public void setFont(Font font) {
		this.font = font;
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

	public void render(PdfContext context) {
		if (!isTextOnly()) {
			context.fillRectangle(getSize(), getBackgroundColor());
			context.strokeRectangle(getSize(), getBorderColor(), getLineWidth());
		}
		if (getText() != null) {
			context.drawText(getText(), getFont(), getSize(), getFontColor());
		}
	}

	@Override
	public void calculateSize(PdfContext context) {
		Rectangle textSize = context.getTextSize(getText(), getFont());
		margin = 0.25f * getFont().getSize();
		setBounds(new Rectangle(textSize.getWidth() + 2 * margin, textSize.getHeight() + 2 * margin));
	}

	/**
	 * Resets cyclic references like child -> parent relationship.
	 *
	 * @param u
	 * @param parent
	 */
	public void afterUnmarshal(Unmarshaller u, Object parent) {
		setParent((PrintComponent) parent);
	}

}
