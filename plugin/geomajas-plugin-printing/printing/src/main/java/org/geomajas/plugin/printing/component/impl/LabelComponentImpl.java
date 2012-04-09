/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2012 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */
package org.geomajas.plugin.printing.component.impl;

import java.awt.Color;
import java.awt.Font;

import org.geomajas.plugin.printing.component.LabelComponent;
import org.geomajas.plugin.printing.component.PdfContext;
import org.geomajas.plugin.printing.component.PrintComponentVisitor;
import org.geomajas.plugin.printing.component.dto.LabelComponentInfo;
import org.geomajas.plugin.printing.component.service.PrintDtoConverterService;
import org.geomajas.plugin.printing.parser.ColorConverter;
import org.geomajas.plugin.printing.parser.FontConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.lowagie.text.Rectangle;
import com.thoughtworks.xstream.annotations.XStreamConverter;
import com.thoughtworks.xstream.annotations.XStreamOmitField;

/**
 * Inclusion of label in printed document.
 * 
 * @author Jan De Moerloose
 */
@Component()
@Scope(value = "prototype")
public class LabelComponentImpl extends AbstractPrintComponent<LabelComponentInfo> implements LabelComponent {

	/**
	 * The font for the text.
	 */
	@XStreamConverter(FontConverter.class)
	private Font font;

	/**
	 * Label text.
	 */
	private String text;

	/**
	 * Color of the text.
	 */
	@XStreamConverter(ColorConverter.class)
	private Color fontColor;

	/**
	 * Background color.
	 */
	@XStreamConverter(ColorConverter.class)
	private Color backgroundColor;

	/**
	 * Line color.
	 */
	@XStreamConverter(ColorConverter.class)
	private Color borderColor;

	/**
	 * Only text, no border ?
	 */
	private boolean textOnly;

	/**
	 * Border line width.
	 */
	private float lineWidth;

	/**
	 * Margin around text.
	 */
	private float margin;
	
	@Autowired
	@XStreamOmitField
	private PrintDtoConverterService converterService;


	public LabelComponentImpl() {
		this(Color.white, Color.BLACK, new Font("Dialog", Font.PLAIN, 12), Color.black, "<missing text>", false, 1f);
	}

	public LabelComponentImpl(Font font, Color fontColor, String text) {
		this(Color.white, Color.BLACK, font, fontColor, text, true, 1f);
	}

	public LabelComponentImpl(Color backgroundColor, Color borderColor, Font font, Color fontColor, String text,
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
	 * @param visitor visitor
	 */
	public void accept(PrintComponentVisitor visitor) {
		visitor.visit(this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.geomajas.plugin.printing.component.impl.LabelComponent#getBackgroundColor()
	 */
	public Color getBackgroundColor() {
		return backgroundColor;
	}

	public void setBackgroundColor(Color backgroundColor) {
		this.backgroundColor = backgroundColor;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.geomajas.plugin.printing.component.impl.LabelComponent#getBorderColor()
	 */
	public Color getBorderColor() {
		return borderColor;
	}

	public void setBorderColor(Color borderColor) {
		this.borderColor = borderColor;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.geomajas.plugin.printing.component.impl.LabelComponent#getFontColor()
	 */
	public Color getFontColor() {
		return fontColor;
	}

	public void setFontColor(Color fontColor) {
		this.fontColor = fontColor;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.geomajas.plugin.printing.component.impl.LabelComponent#getText()
	 */
	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.geomajas.plugin.printing.component.impl.LabelComponent#getFont()
	 */
	public Font getFont() {
		return font;
	}

	public void setFont(Font font) {
		this.font = font;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.geomajas.plugin.printing.component.impl.LabelComponent#isTextOnly()
	 */
	public boolean isTextOnly() {
		return textOnly;
	}

	public void setTextOnly(boolean textOnly) {
		this.textOnly = textOnly;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.geomajas.plugin.printing.component.impl.LabelComponent#getLineWidth()
	 */
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

	public void fromDto(LabelComponentInfo labelInfo) {
		super.fromDto(labelInfo);
		setBackgroundColor(converterService.toInternal(labelInfo.getBackgroundColor()));
		setBorderColor(converterService.toInternal(labelInfo.getBorderColor()));
		setFont(converterService.toInternal(labelInfo.getFont()));
		setFontColor(converterService.toInternal(labelInfo.getFontColor()));
		setLineWidth(labelInfo.getLineWidth());
		setTextOnly(labelInfo.isTextOnly());
		setText(labelInfo.getText());
	}

}
