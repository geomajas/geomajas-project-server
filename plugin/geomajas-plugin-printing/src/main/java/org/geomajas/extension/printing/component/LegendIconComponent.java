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

import com.lowagie.text.Image;
import com.lowagie.text.Rectangle;
import org.geomajas.configuration.StyleInfo;
import org.geomajas.configuration.SymbolInfo;
import org.geomajas.extension.printing.PdfContext;
import org.geomajas.layer.LayerType;

import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlRootElement;
import java.awt.Color;

/**
 * ???
 *
 * @author check subversion
 */
@XmlRootElement
public class LegendIconComponent extends BaseComponent {

	private String label;

	private LayerType layerType;

	private StyleInfo styleInfo;

	public LegendIconComponent() {
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public LayerType getLayerType() {
		return layerType;
	}

	public void setLayerType(LayerType layerType) {
		this.layerType = layerType;
	}

	public StyleInfo getStyleInfo() {
		return styleInfo;
	}

	public void setStyleInfo(StyleInfo styleInfo) {
		this.styleInfo = styleInfo;
	}

	/**
	 * Call back visitor.
	 *
	 * @param visitor
	 */
	public void accept(PrintComponentVisitor visitor) {
		visitor.visit(this);
	}
	
	@Override
	public void calculateSize(PdfContext context) {
		Rectangle textSize = context.getTextSize(label, getLegend().getFont());
		float margin = 0.25f * getLegend().getFont().getSize();
		getConstraint().setMarginX(margin);
		getConstraint().setMarginY(margin);
		setBounds(new Rectangle(textSize.getHeight(), textSize.getHeight()));
	}

	protected LegendComponent getLegend() {
		return (LegendComponent) getParent().getParent();
	}

	@Override
	public void render(PdfContext context) {
		float w = getSize().getWidth();
		float h = getSize().getHeight();
		Rectangle iconRect = new Rectangle(0, 0, w, h);
		Color fillColor = Color.white;
		Color strokeColor = Color.black;
		float[] dashArray = null;
		if (styleInfo != null) {
			fillColor = context.getColor(styleInfo.getFillColor(), new Float(styleInfo.getFillOpacity()));
			strokeColor = context.getColor(styleInfo.getStrokeColor(), new Float(styleInfo.getStrokeOpacity()));
			dashArray = context.getDashArray(styleInfo.getDashArray());
		}
		float baseWidth = iconRect.getWidth() / 10;
		// draw symbol
		switch (layerType) {
			case RASTER:
				Image img = context.getImage("/images/world.png");
				context.drawImage(img, iconRect, null);
				break;
			case POINT:
			case MULTIPOINT:
				SymbolInfo symbol = styleInfo.getSymbol();
				if (symbol.getRect() != null) {
					context.fillRectangle(iconRect, fillColor);
					if (symbol.getRect().getSrc() != null) {
						img = context.getImage(symbol.getRect().getSrc());
						context.drawImage(img, iconRect, null);
					}
					context.strokeRectangle(iconRect, strokeColor, baseWidth / 2);
				} else {
					context.fillEllipse(iconRect, fillColor);
					context.strokeEllipse(iconRect, strokeColor, baseWidth / 2);
				}
				break;
			case LINESTRING:
			case MULTILINESTRING:
				context.drawRelativePath(new float[] {0f, 0.75f, 0.25f, 1f},
						new float[] {0f, 0.25f, 0.75f, 1f}, iconRect, strokeColor, baseWidth * 2, dashArray);
				break;
			case POLYGON:
			case MULTIPOLYGON:
				context.fillRectangle(iconRect, fillColor);
				context.strokeRectangle(iconRect, strokeColor, baseWidth, dashArray);
		}
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
