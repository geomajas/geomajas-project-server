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
package org.geomajas.plugin.rasterizing;

import java.io.IOException;
import java.net.URL;

import org.geomajas.configuration.FeatureStyleInfo;
import org.geomajas.configuration.FontStyleInfo;
import org.geomajas.configuration.LabelStyleInfo;
import org.geomajas.configuration.SymbolInfo;
import org.geomajas.global.GeomajasException;
import org.geomajas.layer.LayerType;
import org.geomajas.layer.VectorLayer;
import org.geomajas.plugin.rasterizing.api.StyleFactoryService;
import org.geomajas.plugin.rasterizing.command.dto.VectorLayerRasterizingInfo;
import org.geomajas.service.FilterService;
import org.geotools.feature.NameImpl;
import org.geotools.styling.ExternalGraphic;
import org.geotools.styling.FeatureTypeStyle;
import org.geotools.styling.Fill;
import org.geotools.styling.Mark;
import org.geotools.styling.PointSymbolizer;
import org.geotools.styling.Stroke;
import org.geotools.styling.Style;
import org.geotools.styling.StyleBuilder;
import org.geotools.styling.Symbolizer;
import org.geotools.styling.TextSymbolizer;
import org.opengis.filter.Filter;
import org.opengis.style.GraphicalSymbol;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

/**
 * Default implementation of {@link StyleFactoryService}. Thread scope until further investigation on StyleBuilder
 * 
 * @author Jan De Moerloose
 * 
 */
@Component
@Scope(value = "thread", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class StyleFactoryServiceImpl implements StyleFactoryService {

	@Autowired
	private ApplicationContext applicationContext;

	@Autowired
	private FilterService filterService;

	private final Logger log = LoggerFactory.getLogger(StyleFactoryServiceImpl.class);

	private StyleBuilder styleBuilder = new StyleBuilder();

	public Style createStyle(VectorLayer layer, VectorLayerRasterizingInfo vectorLayerRasterizingInfo)
			throws GeomajasException {
		Style style = styleBuilder.createStyle();
		TextSymbolizer textSymbolizer = null;
		if (vectorLayerRasterizingInfo.isPaintLabels()) {
			textSymbolizer = createTextSymbolizer(vectorLayerRasterizingInfo.getStyle().getLabelStyle());
		}
		if (vectorLayerRasterizingInfo.isPaintGeometries()) {
			// add the selection style first
			if (vectorLayerRasterizingInfo.getSelectedFeatureIds() != null) {
				// create the style
				Symbolizer symbolizer = createGeometrySymbolizer(layer.getLayerInfo().getLayerType(),
						vectorLayerRasterizingInfo.getSelectionStyle());
				FeatureTypeStyle fts = styleBuilder.createFeatureTypeStyle(symbolizer);
				fts.setName(vectorLayerRasterizingInfo.getSelectionStyle().getName());
				fts.featureTypeNames().add(new NameImpl(layer.getLayerInfo().getFeatureInfo().getDataSourceName()));
				// create the filter
				Filter fidFilter = filterService.createFidFilter(vectorLayerRasterizingInfo.getSelectedFeatureIds());
				fts.rules().get(0).setFilter(fidFilter);
				style.featureTypeStyles().add(fts);
			}
			for (FeatureStyleInfo featureStyle : vectorLayerRasterizingInfo.getStyle().getFeatureStyles()) {
				Symbolizer symbolizer = createGeometrySymbolizer(layer.getLayerInfo().getLayerType(), featureStyle);
				FeatureTypeStyle fts = styleBuilder.createFeatureTypeStyle(symbolizer);
				fts.setName(featureStyle.getName());
				fts.featureTypeNames().add(new NameImpl(layer.getLayerInfo().getFeatureInfo().getDataSourceName()));
				if (featureStyle.getFormula() != null && featureStyle.getFormula().length() > 0) {
					fts.rules().get(0).setFilter(filterService.parseFilter(featureStyle.getFormula()));
				} else {
					fts.rules().get(0).setFilter(Filter.INCLUDE);
				}
				if (textSymbolizer != null) {
					fts.rules().get(0).symbolizers().add(textSymbolizer);
				}
				style.featureTypeStyles().add(fts);
			}
		} else {
			// just labeling if present
			if (textSymbolizer != null) {
				FeatureTypeStyle fts = styleBuilder.createFeatureTypeStyle(textSymbolizer);
				fts.setName(vectorLayerRasterizingInfo.getStyle().getName());
				fts.featureTypeNames().add(new NameImpl(layer.getLayerInfo().getFeatureInfo().getDataSourceName()));
				fts.rules().get(0).setFilter(Filter.INCLUDE);
				style.featureTypeStyles().add(fts);
			}
		}
		return style;
	}

	public Style createStyle(LayerType type, FeatureStyleInfo featureStyleInfo) throws GeomajasException {
		Style style = styleBuilder.createStyle();
		Symbolizer symbolizer = createGeometrySymbolizer(type, featureStyleInfo);
		FeatureTypeStyle fts = styleBuilder.createFeatureTypeStyle(symbolizer);
		style.featureTypeStyles().add(fts);
		return style;
	}

	private Symbolizer createGeometrySymbolizer(LayerType layerType, FeatureStyleInfo featureStyle) {
		Symbolizer symbolizer = null;
		switch (layerType) {
			case MULTIPOLYGON:
			case POLYGON:
				symbolizer = styleBuilder.createPolygonSymbolizer(createStroke(featureStyle), createFill(featureStyle));
				break;
			case MULTILINESTRING:
			case LINESTRING:
				symbolizer = styleBuilder.createLineSymbolizer(createStroke(featureStyle));
				break;
			case POINT:
			case MULTIPOINT:
				PointSymbolizer ps = styleBuilder.createPointSymbolizer();
				GraphicalSymbol symbol = createSymbol(featureStyle);
				if (symbol instanceof Mark) {
					ps.getGraphic().setSize(((Mark) symbol).getSize());
				}
				ps.getGraphic().graphicalSymbols().clear();
				ps.getGraphic().graphicalSymbols().add(createSymbol(featureStyle));
				symbolizer = ps;
				break;
		}
		return symbolizer;
	}

	private TextSymbolizer createTextSymbolizer(LabelStyleInfo labelStyle) {
		Fill fontFill = styleBuilder.createFill(styleBuilder.literalExpression(labelStyle.getFontStyle().getColor()),
				styleBuilder.literalExpression(labelStyle.getFontStyle().getOpacity()));
		TextSymbolizer symbolizer = styleBuilder.createTextSymbolizer();
		symbolizer.setFill(fontFill);
		FontStyleInfo fontInfo = labelStyle.getFontStyle();
		symbolizer.setFont(styleBuilder.createFont(styleBuilder.literalExpression(fontInfo.getFamily()),
				styleBuilder.literalExpression(fontInfo.getStyle()),
				styleBuilder.literalExpression(fontInfo.getWeight()),
				styleBuilder.literalExpression(fontInfo.getSize())));
		symbolizer.setLabel(styleBuilder.attributeExpression(labelStyle.getLabelAttributeName()));
		Fill haloFill = styleBuilder.createFill(
				styleBuilder.literalExpression(labelStyle.getBackgroundStyle().getFillColor()),
				styleBuilder.literalExpression(labelStyle.getBackgroundStyle().getFillOpacity()));
		symbolizer.setHalo(styleBuilder.createHalo(haloFill, 1));
		return symbolizer;
	}

	private GraphicalSymbol createSymbol(FeatureStyleInfo featureStyle) {
		SymbolInfo info = featureStyle.getSymbol();
		if (info.getImage() != null) {
			ExternalGraphic graphic = styleBuilder.createExternalGraphic(getURL(info.getImage().getHref()),
					getFormat(info.getImage().getHref()));
			return graphic;

		} else {
			Mark mark = null;
			if (info.getRect() != null) {
				// TODO: do rectangles by adding custom factory ?
				mark = styleBuilder.createMark("square");
				mark.setSize(styleBuilder.literalExpression((int) info.getRect().getW()));
			} else if (info.getCircle() != null) {
				mark = styleBuilder.createMark("circle");
				mark.setSize(styleBuilder.literalExpression((int) info.getCircle().getR()));
			}
			mark.setFill(createFill(featureStyle));
			mark.setStroke(createStroke(featureStyle));
			return mark;
		}
	}

	private Stroke createStroke(FeatureStyleInfo featureStyle) {
		Stroke stroke = styleBuilder.createStroke(styleBuilder.literalExpression(featureStyle.getStrokeColor()),
				styleBuilder.literalExpression(featureStyle.getStrokeWidth()),
				styleBuilder.literalExpression(featureStyle.getStrokeOpacity()));
		if (featureStyle.getDashArray() != null) {
			String[] strs = featureStyle.getDashArray().split(",");
			float[] nrs = new float[strs.length];
			for (int i = 0; i < strs.length; i++) {
				try {
					nrs[i] = Float.parseFloat(strs[i]);
				} catch (NumberFormatException e) {
					log.warn("unparseable dash array " + featureStyle.getDashArray(), e);
				}
			}
			stroke.setDashArray(nrs);
		}
		return stroke;
	}

	private Fill createFill(FeatureStyleInfo featureStyle) {
		return styleBuilder.createFill(styleBuilder.literalExpression(featureStyle.getFillColor()),
				styleBuilder.literalExpression(featureStyle.getFillOpacity()));
	}

	private URL getURL(String resourceLocation) {
		Resource resource = applicationContext.getResource(resourceLocation);
		if (resource.exists()) {
			try {
				return resource.getURL();
			} catch (IOException e) {
				log.warn("missing resource {}", resourceLocation);
			}
		} else {
			log.warn("missing resource {}", resourceLocation);
		}
		return null;
	}

	private String getFormat(String href) {
		return "image/" + StringUtils.getFilenameExtension(href);
	}

}
