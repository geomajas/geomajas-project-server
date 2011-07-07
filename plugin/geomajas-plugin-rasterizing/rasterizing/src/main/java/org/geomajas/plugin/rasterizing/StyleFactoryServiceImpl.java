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
import java.util.ArrayList;
import java.util.List;

import org.geomajas.configuration.FeatureInfo;
import org.geomajas.configuration.FeatureStyleInfo;
import org.geomajas.configuration.FontStyleInfo;
import org.geomajas.configuration.LabelStyleInfo;
import org.geomajas.configuration.SymbolInfo;
import org.geomajas.global.ExceptionCode;
import org.geomajas.global.GeomajasException;
import org.geomajas.layer.LayerType;
import org.geomajas.layer.VectorLayer;
import org.geomajas.plugin.rasterizing.api.StyleFactoryService;
import org.geomajas.plugin.rasterizing.command.dto.VectorLayerRasterizingInfo;
import org.geomajas.plugin.rasterizing.sld.RasterizingStyleVisitor;
import org.geomajas.service.FilterService;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.styling.ExternalGraphic;
import org.geotools.styling.FeatureTypeStyle;
import org.geotools.styling.Fill;
import org.geotools.styling.Mark;
import org.geotools.styling.PointSymbolizer;
import org.geotools.styling.Rule;
import org.geotools.styling.SLDParser;
import org.geotools.styling.Stroke;
import org.geotools.styling.Style;
import org.geotools.styling.StyleBuilder;
import org.geotools.styling.StyleFactory;
import org.geotools.styling.Symbolizer;
import org.geotools.styling.TextSymbolizer;
import org.opengis.filter.Filter;
import org.opengis.filter.expression.Expression;
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

	private StyleFactory styleFactory = CommonFactoryFinder.getStyleFactory(null);

	public Style createStyle(VectorLayer layer, VectorLayerRasterizingInfo vectorLayerRasterizingInfo)
			throws GeomajasException {
		// check for sld style
		if (vectorLayerRasterizingInfo.getStyle().getSldLocation() != null) {
			try {
				return createSldStyle(layer, vectorLayerRasterizingInfo);
			} catch (Exception e) {
				throw new GeomajasException(e, ExceptionCode.INVALID_SLD, vectorLayerRasterizingInfo.getStyle()
						.getSldLocation(), layer.getId());
			}
		} else {
			return createNamedStyle(layer, vectorLayerRasterizingInfo);
		}
	}

	private Style createSldStyle(VectorLayer layer, VectorLayerRasterizingInfo vectorLayerRasterizingInfo)
			throws Exception {
		Resource sld = applicationContext.getResource(vectorLayerRasterizingInfo.getStyle().getSldLocation());
		SLDParser parser = new SLDParser(styleFactory);
		// external graphics will be resolved with respect to the SLD URL !
		parser.setInput(sld.getURL());
		Style[] styles = parser.readXML();
		for (Style style : styles) {
			if (style.getName().equals(vectorLayerRasterizingInfo.getStyle().getSldStyleName())) {
				return style;
			}
		}
		// visit to draw/omit labels/geometries
		RasterizingStyleVisitor visitor = new RasterizingStyleVisitor(vectorLayerRasterizingInfo);
		visitor.visit(styles[0]);
		return (Style) visitor.getCopy();
	}

	private Style createNamedStyle(VectorLayer layer, VectorLayerRasterizingInfo vectorLayerRasterizingInfo)
			throws GeomajasException {
		Style style = styleBuilder.createStyle();
		String typeName = layer.getLayerInfo().getFeatureInfo().getDataSourceName();
		FeatureInfo featureInfo = layer.getLayerInfo().getFeatureInfo();
		LayerType layerType = layer.getLayerInfo().getLayerType();

		if (vectorLayerRasterizingInfo.isPaintGeometries()) {
			// apply the normal styles
			List<Rule> rules = new ArrayList<Rule>();
			for (FeatureStyleInfo featureStyle : vectorLayerRasterizingInfo.getStyle().getFeatureStyles()) {
				// create the filter
				Filter styleFilter = null;
				if (featureStyle.getFormula() != null && featureStyle.getFormula().length() > 0) {
					styleFilter = filterService.parseFilter(featureStyle.getFormula());
				} else {
					styleFilter = Filter.INCLUDE;
				}
				// create the rules
				rules.addAll(createRules(layerType, styleFilter, featureInfo, featureStyle));
			}
			// create the style
			FeatureTypeStyle normalStyle = styleBuilder.createFeatureTypeStyle(typeName, rules.toArray(new Rule[0]));
			style.featureTypeStyles().add(normalStyle);
			// apply the selection style
			rules.clear();
			if (vectorLayerRasterizingInfo.getSelectedFeatureIds() != null) {
				// create the filter
				Filter fidFilter = filterService.createFidFilter(vectorLayerRasterizingInfo.getSelectedFeatureIds());
				// create the rules
				rules.addAll(createRules(layerType, fidFilter, featureInfo,
						vectorLayerRasterizingInfo.getSelectionStyle()));
			}
			// create the style
			FeatureTypeStyle selectionStyle = styleBuilder.createFeatureTypeStyle(typeName, rules.toArray(new Rule[0]));
			style.featureTypeStyles().add(selectionStyle);
		}
		// apply the label style
		if (vectorLayerRasterizingInfo.isPaintLabels()) {
			// create the rule
			TextSymbolizer textSymbolizer = createTextSymbolizer(vectorLayerRasterizingInfo.getStyle().getLabelStyle(),
					layerType);
			Rule labelRule = styleBuilder.createRule(textSymbolizer);
			// create the style
			FeatureTypeStyle labelStyle = styleBuilder.createFeatureTypeStyle(typeName, labelRule);
			style.featureTypeStyles().add(labelStyle);
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

	private List<Rule> createRules(LayerType layerType, Filter filter, FeatureInfo featureInfo,
			FeatureStyleInfo featureStyle) {
		String geomName = featureInfo.getGeometryType().getName();
		List<Rule> rules = new ArrayList<Rule>();
		// for mixed geometries we add a filter to distinguish between geometry types
		if (layerType == LayerType.GEOMETRY) {
			// add the configured filter to a filter that selects point features only
			Rule pointRule = styleBuilder.createRule(createGeometrySymbolizer(LayerType.POINT, featureStyle));
			Filter pointFilter = filterService.createGeometryTypeFilter(geomName, "Point");
			Filter multiPointFilter = filterService.createGeometryTypeFilter(geomName, "MultiPoint");
			Filter pointsFilter = filterService.createLogicFilter(pointFilter, "or", multiPointFilter);
			pointRule.setFilter(filterService.createLogicFilter(pointsFilter, "and", filter));
			pointRule.setTitle(featureStyle.getName() + "(Point)");

			// add the configured filter to a filter that selects line features only
			Rule lineRule = styleBuilder.createRule(createGeometrySymbolizer(LayerType.LINESTRING, featureStyle));
			Filter lineFilter = filterService.createGeometryTypeFilter(geomName, "LineString");
			Filter multiLineFilter = filterService.createGeometryTypeFilter(geomName, "MultiLineString");
			Filter linesFilter = filterService.createLogicFilter(lineFilter, "or", multiLineFilter);
			lineRule.setFilter(filterService.createLogicFilter(linesFilter, "and", filter));
			lineRule.setTitle(featureStyle.getName() + "(Line)");

			// add the configured filter to a filter that selects polygon features only
			Rule polygonRule = styleBuilder.createRule(createGeometrySymbolizer(LayerType.POLYGON, featureStyle));
			Filter polygonFilter = filterService.createGeometryTypeFilter(geomName, "Polygon");
			Filter multiPolygonFilter = filterService.createGeometryTypeFilter(geomName, "MultiPolygon");
			Filter polygonsFilter = filterService.createLogicFilter(polygonFilter, "or", multiPolygonFilter);
			polygonRule.setFilter(filterService.createLogicFilter(polygonsFilter, "and", filter));
			polygonRule.setTitle(featureStyle.getName() + "(Polygon)");
			rules.add(pointRule);
			rules.add(lineRule);
			rules.add(polygonRule);
		} else {
			Rule rule = styleBuilder.createRule(createGeometrySymbolizer(layerType, featureStyle));
			if (filter.equals(Filter.INCLUDE)) {
				rule.setElseFilter(true);
			} else {
				rule.setFilter(filter);
			}
			rule.setTitle(featureStyle.getName());
			rules.add(rule);
		}
		return rules;
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
				} else {
					Expression size = styleBuilder.literalExpression(featureStyle.getSymbol().getImage().getHeight());
					ps.getGraphic().setSize(size);
				}
				ps.getGraphic().graphicalSymbols().clear();
				ps.getGraphic().graphicalSymbols().add(createSymbol(featureStyle));
				symbolizer = ps;
				break;
		}
		return symbolizer;
	}

	private TextSymbolizer createTextSymbolizer(LabelStyleInfo labelStyle, LayerType layerType) {
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
		// label placement : point at bottom-center of label (same as vectorized)
		switch (layerType) {
			case MULTIPOINT:
			case POINT:
				symbolizer.setLabelPlacement(styleBuilder.createPointPlacement(0.5, 0, 0));
				break;
			default:
				break;
		}
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
				mark.setSize(styleBuilder.literalExpression(2 * (int) info.getCircle().getR()));
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
