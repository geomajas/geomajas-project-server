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

import com.thoughtworks.xstream.annotations.XStreamOmitField;
import org.geomajas.configuration.FeatureStyleInfo;
import org.geomajas.configuration.SymbolInfo;
import org.geomajas.layer.LayerType;
import org.geomajas.plugin.printing.component.LegendComponent;
import org.geomajas.plugin.printing.component.PdfContext;
import org.geomajas.plugin.printing.component.PrintComponentVisitor;
import org.geomajas.plugin.printing.component.dto.LegendIconComponentInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.lowagie.text.Image;
import com.lowagie.text.Rectangle;

/**
 * Legend icon inclusion in printed document.
 *
 * @author Jan De Moerloose
 */
@Component()
@Scope(value = "prototype")
public class LegendIconComponentImpl extends AbstractPrintComponent<LegendIconComponentInfo> {

	private String label;

	private LayerType layerType;

	private FeatureStyleInfo styleInfo;

	@XStreamOmitField
	private final Logger log = LoggerFactory.getLogger(LegendIconComponentImpl.class);

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
	 * @param visitor visitor
	 */
	public void accept(PrintComponentVisitor visitor) {
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
			fillColor = context.getColor(styleInfo.getFillColor(), styleInfo.getFillOpacity(), Color.white);
			strokeColor = context.getColor(styleInfo.getStrokeColor(), styleInfo.getStrokeOpacity(),
					Color.black);
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
				drawPoint(context, iconRect, fillColor, strokeColor);
				break;
			case LINESTRING:
			case MULTILINESTRING:
				drawLine(context, iconRect, strokeColor, dashArray);
				break;
			case POLYGON:
			case MULTIPOLYGON:
				context.fillRectangle(iconRect, fillColor);
				context.strokeRectangle(iconRect, strokeColor, baseWidth, dashArray);
				break;
			case GEOMETRY:
				drawPoint(context, iconRect, fillColor, strokeColor);
				drawLine(context, iconRect, strokeColor, dashArray);
				break;
			default:
				log.warn("Cannot draw unknown layerType " + layerType);
		}
	}

	private void drawLine(PdfContext context, Rectangle iconRect, Color strokeColor, float[] dashArray) {
		float baseWidth = iconRect.getWidth() / 10;
		context.drawRelativePath(new float[] {0f, 0.75f, 0.25f, 1f},
				new float[]{0f, 0.25f, 0.75f, 1f}, iconRect, strokeColor, baseWidth * 2, dashArray);
	}

	private void drawPoint(PdfContext context, Rectangle iconRect, Color fillColor, Color strokeColor) {
		float baseWidth = iconRect.getWidth() / 10;
		SymbolInfo symbol = styleInfo.getSymbol();
		if (symbol.getImage() != null) {
			try {
				Image pointImage = Image.getInstance(symbol.getImage().getHref());
				context.drawImage(pointImage, iconRect, iconRect);
			} catch (Exception ex) { // NOSONAR
				log.error("Not able to create image for POINT Symbol", ex);
			}
		} else if (symbol.getRect() != null) {
			context.fillRectangle(iconRect, fillColor);
			context.strokeRectangle(iconRect, strokeColor, baseWidth / 2);
		} else {
			context.fillEllipse(iconRect, fillColor);
			context.strokeEllipse(iconRect, strokeColor, baseWidth / 2);
		}
	}

	@Override
	public void fromDto(LegendIconComponentInfo iconInfo) {
		super.fromDto(iconInfo);
		setLabel(iconInfo.getLabel());
		setLayerType(iconInfo.getLayerType());
		setStyleInfo(iconInfo.getStyleInfo());
	}

}
