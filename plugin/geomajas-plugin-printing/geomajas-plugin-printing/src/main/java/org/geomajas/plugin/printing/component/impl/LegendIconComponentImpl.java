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
package org.geomajas.plugin.printing.component.impl;

import java.awt.Color;

import org.geomajas.configuration.FeatureStyleInfo;
import org.geomajas.configuration.SymbolInfo;
import org.geomajas.layer.LayerType;
import org.geomajas.plugin.printing.component.LegendComponent;
import org.geomajas.plugin.printing.component.LegendIconComponent;
import org.geomajas.plugin.printing.component.PdfContext;
import org.geomajas.plugin.printing.component.PrintComponentVisitor;
import org.geomajas.plugin.printing.component.dto.LegendIconComponentInfo;
import org.geomajas.plugin.printing.component.dto.PrintComponentInfo;
import org.geomajas.plugin.printing.component.service.PrintDtoConverterService;

import com.lowagie.text.Image;
import com.lowagie.text.Rectangle;

/**
 * Legend icon inclusion in printed document.
 *
 * @author Jan De Moerloose
 */
public class LegendIconComponentImpl extends PrintComponentImpl implements LegendIconComponent {

	private String label;

	private LayerType layerType;

	private FeatureStyleInfo styleInfo;

	public LegendIconComponentImpl() {
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

	public FeatureStyleInfo getStyleInfo() {
		return styleInfo;
	}

	public void setStyleInfo(FeatureStyleInfo styleInfo) {
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
				Image img = context.getImage("/images/layer-raster.png");
				context.drawImage(img, iconRect, null);
				break;
			case POINT:
			case MULTIPOINT:
				SymbolInfo symbol = styleInfo.getSymbol();
				if (symbol.getRect() != null) {
					context.fillRectangle(iconRect, fillColor);
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

	@Override
	public void fromDto(PrintComponentInfo info, PrintDtoConverterService service) {
		super.fromDto(info, service);
		LegendIconComponentInfo iconInfo = (LegendIconComponentInfo) info;
		setLabel(iconInfo.getLabel());
		setLayerType(iconInfo.getLayerType());
		setStyleInfo(iconInfo.getStyleInfo());
	}

}
