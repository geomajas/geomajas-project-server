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
package org.geomajas.internal.service;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.imageio.ImageIO;

import org.geomajas.configuration.AbstractAttributeInfo;
import org.geomajas.configuration.CircleInfo;
import org.geomajas.configuration.FeatureInfo;
import org.geomajas.configuration.FeatureStyleInfo;
import org.geomajas.configuration.FontStyleInfo;
import org.geomajas.configuration.ImageInfo;
import org.geomajas.configuration.LabelStyleInfo;
import org.geomajas.configuration.NamedStyleInfo;
import org.geomajas.configuration.PrimitiveAttributeInfo;
import org.geomajas.configuration.PrimitiveType;
import org.geomajas.configuration.RectInfo;
import org.geomajas.configuration.SymbolInfo;
import org.geomajas.global.ExceptionCode;
import org.geomajas.global.GeomajasConstant;
import org.geomajas.global.GeomajasException;
import org.geomajas.layer.LayerException;
import org.geomajas.layer.LayerType;
import org.geomajas.service.FilterService;
import org.geomajas.service.ResourceService;
import org.geomajas.service.StyleConverterService;
import org.geomajas.sld.CssParameterInfo;
import org.geomajas.sld.ExternalGraphicInfo;
import org.geomajas.sld.FeatureTypeStyleInfo;
import org.geomajas.sld.FillInfo;
import org.geomajas.sld.FontInfo;
import org.geomajas.sld.GraphicInfo;
import org.geomajas.sld.GraphicInfo.ChoiceInfo;
import org.geomajas.sld.LineSymbolizerInfo;
import org.geomajas.sld.MarkInfo;
import org.geomajas.sld.NamedLayerInfo;
import org.geomajas.sld.ParameterValueTypeInfo;
import org.geomajas.sld.PointSymbolizerInfo;
import org.geomajas.sld.PolygonSymbolizerInfo;
import org.geomajas.sld.RuleInfo;
import org.geomajas.sld.StrokeInfo;
import org.geomajas.sld.StyledLayerDescriptorInfo;
import org.geomajas.sld.SymbolizerTypeInfo;
import org.geomajas.sld.TextSymbolizerInfo;
import org.geomajas.sld.UserStyleInfo;
import org.geomajas.sld.expression.ExpressionInfo;
import org.geomajas.sld.expression.LiteralTypeInfo;
import org.geomajas.sld.filter.AndInfo;
import org.geomajas.sld.filter.BboxTypeInfo;
import org.geomajas.sld.filter.BeyondInfo;
import org.geomajas.sld.filter.BinaryComparisonOpTypeInfo;
import org.geomajas.sld.filter.BinaryLogicOpTypeInfo;
import org.geomajas.sld.filter.BinarySpatialOpTypeInfo;
import org.geomajas.sld.filter.ComparisonOpsTypeInfo;
import org.geomajas.sld.filter.ContainsInfo;
import org.geomajas.sld.filter.CrossesInfo;
import org.geomajas.sld.filter.DWithinInfo;
import org.geomajas.sld.filter.DisjointInfo;
import org.geomajas.sld.filter.DistanceBufferTypeInfo;
import org.geomajas.sld.filter.EqualsInfo;
import org.geomajas.sld.filter.FeatureIdTypeInfo;
import org.geomajas.sld.filter.FilterTypeInfo;
import org.geomajas.sld.filter.IntersectsInfo;
import org.geomajas.sld.filter.LogicOpsTypeInfo;
import org.geomajas.sld.filter.OrInfo;
import org.geomajas.sld.filter.OverlapsInfo;
import org.geomajas.sld.filter.PropertyIsBetweenTypeInfo;
import org.geomajas.sld.filter.PropertyIsEqualToInfo;
import org.geomajas.sld.filter.PropertyIsGreaterThanInfo;
import org.geomajas.sld.filter.PropertyIsGreaterThanOrEqualToInfo;
import org.geomajas.sld.filter.PropertyIsLessThanInfo;
import org.geomajas.sld.filter.PropertyIsLessThanOrEqualToInfo;
import org.geomajas.sld.filter.PropertyIsLikeTypeInfo;
import org.geomajas.sld.filter.PropertyIsNotEqualToInfo;
import org.geomajas.sld.filter.PropertyIsNullTypeInfo;
import org.geomajas.sld.filter.SpatialOpsTypeInfo;
import org.geomajas.sld.filter.TouchesInfo;
import org.geomajas.sld.filter.UnaryLogicOpTypeInfo;
import org.geomajas.sld.filter.WithinInfo;
import org.geomajas.sld.geometry.AbstractGeometryCollectionInfo;
import org.geomajas.sld.geometry.AbstractGeometryInfo;
import org.geomajas.sld.geometry.BoxTypeInfo;
import org.geomajas.sld.geometry.CoordTypeInfo;
import org.geomajas.sld.geometry.CoordinatesTypeInfo;
import org.geomajas.sld.geometry.GeometryMemberInfo;
import org.geomajas.sld.geometry.InnerBoundaryIsInfo;
import org.geomajas.sld.geometry.LineStringTypeInfo;
import org.geomajas.sld.geometry.LinearRingTypeInfo;
import org.geomajas.sld.geometry.MultiGeometryInfo;
import org.geomajas.sld.geometry.MultiLineStringInfo;
import org.geomajas.sld.geometry.MultiPointInfo;
import org.geomajas.sld.geometry.MultiPolygonInfo;
import org.geomajas.sld.geometry.OuterBoundaryIsInfo;
import org.geomajas.sld.geometry.PointTypeInfo;
import org.geomajas.sld.geometry.PolygonTypeInfo;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.styling.FeatureTypeConstraint;
import org.geotools.styling.FeatureTypeStyle;
import org.geotools.styling.Fill;
import org.geotools.styling.Mark;
import org.geotools.styling.PointSymbolizer;
import org.geotools.styling.ResourceLocator;
import org.geotools.styling.Rule;
import org.geotools.styling.SLDParser;
import org.geotools.styling.SLDTransformer;
import org.geotools.styling.Stroke;
import org.geotools.styling.Style;
import org.geotools.styling.StyleBuilder;
import org.geotools.styling.StyleFactory;
import org.geotools.styling.StyledLayerDescriptor;
import org.geotools.styling.Symbolizer;
import org.geotools.styling.TextSymbolizer;
import org.geotools.styling.UserLayer;
import org.jibx.runtime.BindingDirectory;
import org.jibx.runtime.IBindingFactory;
import org.jibx.runtime.IMarshallingContext;
import org.jibx.runtime.IUnmarshallingContext;
import org.opengis.filter.Filter;
import org.opengis.filter.expression.Expression;
import org.opengis.style.GraphicalSymbol;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.LinearRing;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.Polygon;
import com.vividsolutions.jts.io.WKTWriter;

/**
 * Default implementation of {@link StyleConverterService}. Supports named layers and user styles only.
 * 
 * @author Jan De Moerloose
 */
@Component
public class StyleConverterServiceImpl implements StyleConverterService {

	private static final String SLD_VERSION = "1.0.0";

	private static final String DUMMY_NAMED_LAYER = "Dummy";

	private static final String CSS_FILL = "fill";
	private static final String CSS_FILL_OPACITY = "fill-opacity";
	private static final String CSS_STROKE = "stroke";
	private static final String CSS_STROKE_OPACITY = "stroke-opacity";
	private static final String CSS_STROKE_WIDTH = "stroke-width";
	private static final String CSS_STROKE_DASH_ARRAY = "stroke-dasharray";
	private static final String CSS_FONT_SIZE = "font-size";
	private static final String CSS_FONT_STYLE = "font-style";
	private static final String CSS_FONT_WEIGHT = "font-weight";
	private static final String CSS_FONT_FAMILY = "font-family";

	private static final String MARK_SQUARE = "square";
	private static final String MARK_CIRCLE = "circle";
	
	private static final String ASSOCIATION_DELIMITER_SOURCE = "/";
	private static final String ASSOCIATION_DELIMITER_TARGET = ".";
	
	private static final String MISSING_RESOURCE = "missing resource {}";

	private StyleFactory styleFactory = CommonFactoryFinder.getStyleFactory(null);

	private StyleBuilder styleBuilder;

	private final Logger log = LoggerFactory.getLogger(StyleConverterServiceImpl.class);

	@Autowired
	private ApplicationContext applicationContext;

	@Autowired
	private FilterService filterService;

	@Autowired
	private ResourceService resourceService;

	@Override
	public NamedStyleInfo convert(UserStyleInfo userStyle, FeatureInfo featureInfo) throws LayerException {
		NamedStyleInfo namedStyleInfo = new NamedStyleInfo();
		LabelStyleInfo labelStyleInfo = new LabelStyleInfo();
		List<FeatureStyleInfo> featureStyleInfos = new ArrayList<FeatureStyleInfo>();
		for (FeatureTypeStyleInfo featureTypeStyleInfo : userStyle.getFeatureTypeStyleList()) {
			int styleIndex = 0;
			for (RuleInfo ruleInfo : featureTypeStyleInfo.getRuleList()) {
				FeatureStyleInfo featureStyleInfo = new FeatureStyleInfo();
				if (null != ruleInfo.getChoice()  && ruleInfo.getChoice().ifFilter()) {
					featureStyleInfo.setFormula(convertFormula(ruleInfo.getChoice().getFilter(), featureInfo));
				}
				for (SymbolizerTypeInfo symbolizerTypeInfo : ruleInfo.getSymbolizerList()) {
					if (symbolizerTypeInfo instanceof PointSymbolizerInfo) {
						PointSymbolizerInfo pointInfo = (PointSymbolizerInfo) symbolizerTypeInfo;
						convertSymbol(featureStyleInfo, pointInfo);
					} else if (symbolizerTypeInfo instanceof LineSymbolizerInfo) {
						LineSymbolizerInfo lineInfo = (LineSymbolizerInfo) symbolizerTypeInfo;
						convertStroke(featureStyleInfo, lineInfo.getStroke());
					} else if (symbolizerTypeInfo instanceof PolygonSymbolizerInfo) {
						PolygonSymbolizerInfo polygonInfo = (PolygonSymbolizerInfo) symbolizerTypeInfo;
						convertFill(featureStyleInfo, polygonInfo.getFill());
						convertStroke(featureStyleInfo, polygonInfo.getStroke());
					} else if (symbolizerTypeInfo instanceof TextSymbolizerInfo) {
						TextSymbolizerInfo textInfo = (TextSymbolizerInfo) symbolizerTypeInfo;
						labelStyleInfo.setFontStyle(convertFont(textInfo.getFont()));
						for (ExpressionInfo expr : textInfo.getLabel().getExpressionList()) {
							if (expr instanceof LiteralTypeInfo ) {
								String literalValue = expr.getValue();
								labelStyleInfo.setLabelValueExpression("'" + literalValue + "'");
							} else { // Assume expr is of class PropertyNameInfo
								labelStyleInfo.setLabelAttributeName(expr.getValue());
							}
						}
						convertFontFill(labelStyleInfo.getFontStyle(), textInfo.getFill());
						FeatureStyleInfo background = new FeatureStyleInfo();
						if (textInfo.getHalo() != null) {
							convertFill(background, textInfo.getHalo().getFill());
						}
						labelStyleInfo.setBackgroundStyle(background);
					} else {
						throw new IllegalStateException("Unknown symbolizer type " + symbolizerTypeInfo);
					}
				}
				if (featureStyleInfo.getStrokeColor() == null && featureStyleInfo.getFillColor() != null) {
					// avoid default stroke by setting invisible
					featureStyleInfo.setStrokeColor("black");
					featureStyleInfo.setStrokeOpacity(0);
					featureStyleInfo.setStrokeWidth(0);
				}
				featureStyleInfo.setIndex(styleIndex++);
				featureStyleInfo.setName(ruleInfo.getTitle() != null ? ruleInfo.getTitle() : ruleInfo.getName());
				featureStyleInfos.add(featureStyleInfo);
			}
		}
		namedStyleInfo.setName(userStyle.getTitle() != null ? userStyle.getTitle() : userStyle.getName());
		namedStyleInfo.setFeatureStyles(featureStyleInfos);
		namedStyleInfo.setLabelStyle(labelStyleInfo);
		return namedStyleInfo;
	}

	@Override
	public Style convert(UserStyleInfo userStyleInfo) throws LayerException {
		IBindingFactory bindingFactory;
		try {
			// create a dummy SLD root
			StyledLayerDescriptorInfo sld = new StyledLayerDescriptorInfo();
			sld.setVersion(SLD_VERSION);
			StyledLayerDescriptorInfo.ChoiceInfo choice = new StyledLayerDescriptorInfo.ChoiceInfo();
			NamedLayerInfo namedLayerInfo = new NamedLayerInfo();
			namedLayerInfo.setName(DUMMY_NAMED_LAYER);
			NamedLayerInfo.ChoiceInfo userChoice = new NamedLayerInfo.ChoiceInfo();
			userChoice.setUserStyle(userStyleInfo);
			namedLayerInfo.getChoiceList().add(userChoice);
			choice.setNamedLayer(namedLayerInfo);
			sld.getChoiceList().add(choice);

			// force through Geotools parser
			bindingFactory = BindingDirectory.getFactory(StyledLayerDescriptorInfo.class);
			IMarshallingContext marshallingContext = bindingFactory.createMarshallingContext();
			StringWriter sw = new StringWriter();
			marshallingContext.setOutput(sw);
			marshallingContext.marshalDocument(sld);

			SLDParser parser = new SLDParser(styleFactory, filterService.getFilterFactory());
			parser.setOnLineResourceLocator(new ResourceServiceBasedLocator());
			parser.setInput(new StringReader(sw.toString()));

			Style[] styles = parser.readXML();
			if (styles.length != 0) {
				return styles[0];
			} else {
				throw new LayerException(ExceptionCode.INVALID_USER_STYLE, userStyleInfo.getName());
			}
		} catch (Exception e) {
			throw new LayerException(e, ExceptionCode.INVALID_USER_STYLE, userStyleInfo.getName());
		}
	}

	@Override
	public Rule convert(RuleInfo ruleInfo) throws LayerException {
		UserStyleInfo styleInfo = new UserStyleInfo();
		FeatureTypeStyleInfo fts = new FeatureTypeStyleInfo();
		fts.getRuleList().add(ruleInfo);
		styleInfo.getFeatureTypeStyleList().add(fts);
		Style style = convert(styleInfo);
		return style.featureTypeStyles().get(0).rules().get(0);
	}

	@Override
	public UserStyleInfo convert(NamedStyleInfo namedStyleInfo, String geometryName) throws LayerException {
		Style style = styleBuilder.createStyle();

		// list of rules
		List<Rule> rules = new ArrayList<Rule>();
		for (FeatureStyleInfo featureStyle : namedStyleInfo.getFeatureStyles()) {
			// create the filter
			Filter styleFilter;
			if (featureStyle.getFormula() != null && featureStyle.getFormula().length() > 0) {
				try {
					styleFilter = filterService.parseFilter(featureStyle.getFormula());
				} catch (GeomajasException e) {
					throw new LayerException(e, ExceptionCode.FILTER_PARSE_PROBLEM, featureStyle.getFormula());
				}
			} else {
				styleFilter = Filter.INCLUDE;
			}
			Rule rule = createRule(styleFilter, featureStyle);
			// add the label symbolizer to the rule
			TextSymbolizer textSymbolizer = createTextSymbolizer(namedStyleInfo.getLabelStyle(),
					featureStyle.getLayerType());
			rule.symbolizers().add(textSymbolizer);
			rules.add(rule);
		}
		// create the style
		FeatureTypeStyle fts = styleBuilder.createFeatureTypeStyle(null, rules.toArray(new Rule[rules.size()]));
		style.featureTypeStyles().add(fts);
		// parse and to dto

		try {
			StyledLayerDescriptor styledLayerDescriptor = styleFactory.createStyledLayerDescriptor();
			UserLayer layer = styleFactory.createUserLayer();
			layer.setLayerFeatureConstraints(new FeatureTypeConstraint[] { null });
			styledLayerDescriptor.addStyledLayer(layer);
			layer.addUserStyle(style);

			SLDTransformer styleTransform = new SLDTransformer();
			String xml = styleTransform.transform(styledLayerDescriptor);
			IBindingFactory bindingFactory = BindingDirectory.getFactory(StyledLayerDescriptorInfo.class);
			IUnmarshallingContext unmarshallingContext = bindingFactory.createUnmarshallingContext();
			StyledLayerDescriptorInfo sld = (StyledLayerDescriptorInfo) unmarshallingContext
					.unmarshalDocument(new StringReader(xml));
			return sld.getChoiceList().get(0).getUserLayer().getUserStyleList().get(0);
		} catch (Exception e) {
			throw new LayerException(e, ExceptionCode.INVALID_USER_STYLE, namedStyleInfo.getName());
		}
	}

	private void convertSymbol(FeatureStyleInfo featureStyleInfo, PointSymbolizerInfo pointInfo)
			throws LayerException {
		GraphicInfo graphic = pointInfo.getGraphic();
		SymbolInfo symbol = new SymbolInfo();

		if (graphic.getChoiceList().size() > 0) {
			ChoiceInfo choice = graphic.getChoiceList().get(0);
			if (choice.ifExternalGraphic()) {
				ExternalGraphicInfo externalGraphic = choice.getExternalGraphic();
				String href = externalGraphic.getOnlineResource().getHref().getHref();
				ImageInfo image = new ImageInfo();
				image.setHref(href);
				// SLD has no selection concept + no default: what to do ?
				image.setSelectionHref(href);
				try {
					BufferedImage img = ImageIO.read(resourceService.find(href).getInputStream());
					image.setWidth(img.getWidth());
					image.setHeight(img.getHeight());
				} catch (Exception e) {
					// cannot determine image
					log.warn("Unable to determine size of image " + href, e);
				}
				if (graphic.getSize() != null) {
					double scale = parseFloat(getParameterValue(graphic.getSize())) / image.getHeight();
					image.setHeight((int) (scale * image.getHeight()));
					image.setWidth((int) (scale * image.getWidth()));
				}
				symbol.setImage(image);
			} else if (choice.ifMark()) {
				MarkInfo mark = choice.getMark();
				String name = mark.getWellKnownName().getWellKnownName();
				if (MARK_SQUARE.equalsIgnoreCase(name)) {
					RectInfo rect = new RectInfo();
					rect.setH(parseFloat(getParameterValue(graphic.getSize())));
					rect.setW(parseFloat(getParameterValue(graphic.getSize())));
					symbol.setRect(rect);
				} else {
					// should treat everything else as circle ?!
					CircleInfo circle = new CircleInfo();
					circle.setR(0.5F * parseFloat(getParameterValue(graphic.getSize())));
					symbol.setCircle(circle);
				}
				convertFill(featureStyleInfo, mark.getFill());
				convertStroke(featureStyleInfo, mark.getStroke());
			}
		}
		featureStyleInfo.setSymbol(symbol);
	}

	private float parseFloat(String str) throws LayerException {
		try {
			return Float.parseFloat(str);
		} catch (NumberFormatException nfe) {
			throw new LayerException(nfe, ExceptionCode.SLD_PARSE_NUMBER, str);
		}
	}

	private double parseDouble(String str) throws LayerException {
		try {
			return Double.parseDouble(str);
		} catch (NumberFormatException nfe) {
			throw new LayerException(nfe, ExceptionCode.SLD_PARSE_NUMBER, str);
		}
	}

	private float parseFloat(Map<String, String> cssMap, String attribute) throws LayerException {
		String str = cssMap.get(attribute);
		try {
			return Float.parseFloat(str);
		} catch (NumberFormatException nfe) {
			throw new LayerException(nfe, ExceptionCode.SLD_PARSE_NUMBER_ATTRIBUTE, str, attribute);
		}
	}

	private void convertFontFill(FontStyleInfo fontStyle, FillInfo fill) throws LayerException {
		if (fill != null) {
			Map<String, String> cssMap = getLiteralMap(fill.getCssParameterList());
			fontStyle.setColor(cssMap.get(CSS_FILL));
			if (cssMap.containsKey(CSS_FILL_OPACITY)) {
				fontStyle.setOpacity(parseFloat(cssMap, CSS_FILL_OPACITY));
			}
		}
	}

	private FontStyleInfo convertFont(FontInfo font) throws LayerException {
		FontStyleInfo fontStyle = new FontStyleInfo();
		if (font == null) {
			fontStyle.applyDefaults();
		} else {
			Map<String, String> cssMap = getLiteralMap(font.getCssParameterList());
			fontStyle.setFamily(cssMap.get(CSS_FONT_FAMILY));
			if (cssMap.containsKey(CSS_FONT_SIZE)) {
				fontStyle.setSize((int) parseFloat(cssMap, CSS_FONT_SIZE));
			}
			fontStyle.setStyle(cssMap.get(CSS_FONT_STYLE));
			fontStyle.setWeight(cssMap.get(CSS_FONT_WEIGHT));
		}
		return fontStyle;
	}

	private void convertFill(FeatureStyleInfo featureStyleInfo, FillInfo fill) throws LayerException {
		if (fill != null) {
			Map<String, String> cssMap = getLiteralMap(fill.getCssParameterList());
			if (cssMap.containsKey(CSS_FILL)) {
				featureStyleInfo.setFillColor(cssMap.get(CSS_FILL));
			}
			if (cssMap.containsKey(CSS_FILL_OPACITY)) {
				featureStyleInfo.setFillOpacity(parseFloat(cssMap, CSS_FILL_OPACITY));
			}
			if (fill.getGraphicFill() != null) {
				GraphicInfo graphic = fill.getGraphicFill().getGraphic();
				for (GraphicInfo.ChoiceInfo choice : graphic.getChoiceList()) {
					if (choice.ifExternalGraphic()) {
						log.debug("Can not display an external graphic fill style for non-rasterized layers.");
						// can't handle this
					} else if (choice.ifMark()) {
						MarkInfo mark = choice.getMark();
						if (mark.getFill() != null) {
							convertFill(featureStyleInfo, mark.getFill());
						}
						if (mark.getStroke() != null) {
							convertStroke(featureStyleInfo, mark.getStroke());
						}
					}
				}
			}
		}
	}

	private void convertStroke(FeatureStyleInfo featureStyleInfo, StrokeInfo stroke) throws LayerException {
		if (stroke != null) {
			Map<String, String> cssMap = getLiteralMap(stroke.getCssParameterList());
			// not supported are "stroke-linejoin", "stroke-linecap", and "stroke-dashoffset"
			featureStyleInfo.setStrokeColor(cssMap.get(CSS_STROKE));
			if (cssMap.containsKey(CSS_STROKE_OPACITY)) {
				featureStyleInfo.setStrokeOpacity(parseFloat(cssMap, CSS_STROKE_OPACITY));
			}
			if (cssMap.containsKey(CSS_STROKE_WIDTH)) {
				featureStyleInfo.setStrokeWidth((int) parseFloat(cssMap, CSS_STROKE_WIDTH));
			}
			if (cssMap.containsKey(CSS_STROKE_DASH_ARRAY)) {
				featureStyleInfo.setDashArray(cssMap.get(CSS_STROKE_DASH_ARRAY));
			}
		}
	}

	private String convertFormula(FilterTypeInfo filter, FeatureInfo featureInfo) throws LayerException {
		if (filter.ifComparisonOps()) {
			return toComparison(filter.getComparisonOps(), featureInfo);
		} else if (filter.ifFeatureIdList()) {
			return toFeatureIds(filter.getFeatureIdList());
		} else if (filter.ifLogicOps()) {
			return toLogic(filter.getLogicOps(), featureInfo);
		} else if (filter.ifSpatialOps()) {
			return toSpatial(filter.getSpatialOps());
		}
		return null;
	}

	private String toLogic(LogicOpsTypeInfo logicOps, FeatureInfo featureInfo) throws LayerException {
		if (logicOps instanceof UnaryLogicOpTypeInfo) {
			UnaryLogicOpTypeInfo unary = (UnaryLogicOpTypeInfo) logicOps;
			if (unary.ifComparisonOps()) {
				return "NOT " + toComparison(unary.getComparisonOps(), featureInfo);
			} else if (unary.ifLogicOps()) {
				return "NOT " + toLogic(unary.getLogicOps(), featureInfo);
			} else if (unary.ifSpatialOps()) {
				return "NOT " + toSpatial(unary.getSpatialOps());
			}

		} else if (logicOps instanceof BinaryLogicOpTypeInfo) {
			BinaryLogicOpTypeInfo binary = (BinaryLogicOpTypeInfo) logicOps;
			String[] expressions = new String[2];
			for (int i = 0; i < 2; i++) {
				if (binary.getChoiceList().get(i).ifComparisonOps()) {
					expressions[i] = toComparison(binary.getChoiceList().get(i).getComparisonOps(), featureInfo);
				} else if (binary.getChoiceList().get(i).ifLogicOps()) {
					expressions[i] = toLogic(binary.getChoiceList().get(i).getLogicOps(), featureInfo);
				} else if (binary.getChoiceList().get(i).ifSpatialOps()) {
					expressions[i] = toSpatial(binary.getChoiceList().get(i).getSpatialOps());
				}
			}
			if (binary instanceof OrInfo) {
				return "(" + expressions[0] + ") OR (" + expressions[1] + ")";
			} else if (binary instanceof AndInfo) {
				return "(" + expressions[0] + ") AND (" + expressions[1] + ")";
			}
		}
		return null;
	}

	private String toSpatial(SpatialOpsTypeInfo spatialOps) throws LayerException {
		if (spatialOps instanceof BboxTypeInfo) {
			BboxTypeInfo bbox = (BboxTypeInfo) spatialOps;
			String propertyName = bbox.getPropertyName().getValue();
			Envelope envelope = toEnvelope(bbox.getBox());
			return "BBOX (" + propertyName + "," + envelope.getMinX()  + "," + envelope.getMinY()
					+ envelope.getMaxX()  + "," + envelope.getMaxY() + ")";
		} else if (spatialOps instanceof BinarySpatialOpTypeInfo) {
			BinarySpatialOpTypeInfo binary = (BinarySpatialOpTypeInfo) spatialOps;
			String propertyName = binary.getPropertyName().getValue();
			WKTWriter writer = new WKTWriter();
			GeometryFactory factory = new GeometryFactory();
			Geometry geometry = null;
			if (binary.ifGeometry()) {
				AbstractGeometryInfo geom = binary.getGeometry();
				geometry = toGeometry(factory, geom);
			} else if (binary.ifBox()) {
				BoxTypeInfo boxTypeInfo = binary.getBox();
				Envelope envelope = toEnvelope(boxTypeInfo);
				geometry = factory.toGeometry(envelope);
			}
			String wkt = writer.write(geometry);
			if (binary instanceof ContainsInfo) {
				return "CONTAINS(" + propertyName + "," + wkt + ")";
			} else if (binary instanceof CrossesInfo) {
				return "CROSSES(" + propertyName + "," + wkt + ")";
			} else if (binary instanceof DisjointInfo) {
				return "DISJOINT(" + propertyName + "," + wkt + ")";
			} else if (binary instanceof EqualsInfo) {
				return "EQUALS(" + propertyName + "," + wkt + ")";
			} else if (binary instanceof IntersectsInfo) {
				return "INTERSECTS(" + propertyName + "," + wkt + ")";
			} else if (binary instanceof OverlapsInfo) {
				return "OVERLAPS(" + propertyName + "," + wkt + ")";
			} else if (binary instanceof TouchesInfo) {
				return "TOUCHES(" + propertyName + "," + wkt + ")";
			} else if (binary instanceof WithinInfo) {
				return "WITHIN(" + propertyName + "," + wkt + ")";
			} else {
				throw new IllegalArgumentException("Unhandled type of BinarySpatialOpTypeInfo " + binary);
			}
		} else if (spatialOps instanceof DistanceBufferTypeInfo) {
			DistanceBufferTypeInfo distanceBuffer = (DistanceBufferTypeInfo) spatialOps;
			String propertyName = distanceBuffer.getPropertyName().getValue();
			AbstractGeometryInfo geom = distanceBuffer.getGeometry();
			GeometryFactory factory = new GeometryFactory();
			Geometry geometry = toGeometry(factory, geom);
			WKTWriter writer = new WKTWriter();
			String wkt = writer.write(geometry);
			String units = distanceBuffer.getDistance().getUnits();
			String distance = distanceBuffer.getDistance().getValue();
			if (distanceBuffer instanceof DWithinInfo) {
				return "DWITHIN(" + propertyName + "," + wkt + "," + distance + "," + units + ")";
			} else if (distanceBuffer instanceof BeyondInfo) {
				return "BEYOND(" + propertyName + "," + wkt + "," + distance + "," + units + ")";
			} else {
				throw new IllegalArgumentException("Unhandled type of DistanceBufferTypeInfo " + distanceBuffer);
			}
		} else {
			throw new IllegalArgumentException("Unhandled type of SpatialOpsTypeInfo " + spatialOps);
		}
	}
	
	private Envelope toEnvelope(BoxTypeInfo box) throws LayerException {
		if (box.ifCoordinates()) {
			Coordinate[] coords = getCoordinates(box.getCoordinates());
			if (coords.length == 2) {
				return new Envelope(coords[0].x, coords[1].x, coords[0].y, coords[1].y);
			} else {
				throw new IllegalArgumentException("Number of coordinates != 2 in box : " + box);
			}
		} else {
			if (box.getCoordList().size() == 2) {
				CoordTypeInfo coordMin = box.getCoordList().get(0);
				CoordTypeInfo coordMax = box.getCoordList().get(1);
				return new Envelope(coordMin.getX().doubleValue(), coordMax.getX().doubleValue(), coordMin.getY()
						.doubleValue(), coordMax.getY().doubleValue());
			} else {
				throw new IllegalArgumentException("Number of coordinates != 2 in box : " + box);
			}
		}
	}

	private Geometry toGeometry(GeometryFactory factory, AbstractGeometryInfo geom) throws LayerException {
		Geometry geometry = null;
		if (geom instanceof AbstractGeometryCollectionInfo) {
			AbstractGeometryCollectionInfo geomCollection = (AbstractGeometryCollectionInfo) geom;
			List<GeometryMemberInfo> members = geomCollection.getGeometryMemberList();
			if (geom instanceof MultiPointInfo) {
				Point[] points = new Point[members.size()];
				for (int i = 0; i < members.size(); i++) {
					points[i] = (Point) toSimpleGeometry(factory, members.get(i).getGeometry());
				}
				geometry = factory.createMultiPoint(points);
			} else if (geom instanceof MultiLineStringInfo) {
				LineString[] lines = new LineString[members.size()];
				for (int i = 0; i < members.size(); i++) {
					lines[i] = (LineString) toSimpleGeometry(factory, members.get(i).getGeometry());
				}
				geometry = factory.createMultiLineString(lines);
			} else if (geom instanceof MultiPolygonInfo) {
				Polygon[] polygons = new Polygon[members.size()];
				for (int i = 0; i < members.size(); i++) {
					polygons[i] = (Polygon) toSimpleGeometry(factory, members.get(i).getGeometry());
				}
				geometry = factory.createMultiPolygon(polygons);
			} else if (geom instanceof MultiGeometryInfo) {
				Geometry[] geometries = new Geometry[members.size()];
				for (int i = 0; i < members.size(); i++) {
					geometries[i] = toGeometry(factory, members.get(i).getGeometry());
				}
				geometry = factory.createGeometryCollection(geometries);
			}
		} else {
			geometry = toSimpleGeometry(factory, geom);
		}
		return geometry;
	}

	private Geometry toSimpleGeometry(GeometryFactory factory, AbstractGeometryInfo geom) throws LayerException {
		Geometry geometry = null;
		if (geom instanceof PointTypeInfo) {
			PointTypeInfo point = (PointTypeInfo) geom;
			if (point.ifCoord()) {
				geometry = factory.createPoint(getCoordinates(Collections.singletonList(point.getCoord()))[0]);
			} else if (point.ifCoordinates()) {
				geometry = factory.createPoint(getCoordinates(point.getCoordinates())[0]);
			}
		} else if (geom instanceof LineStringTypeInfo) {
			LineStringTypeInfo linestring = (LineStringTypeInfo) geom;
			if (linestring.ifCoordList()) {
				geometry = factory.createLineString(getCoordinates(linestring.getCoordList()));
			} else if (linestring.ifCoordinates()) {
				geometry = factory.createLineString(getCoordinates(linestring.getCoordinates()));
			}
		} else if (geom instanceof PolygonTypeInfo) {
			PolygonTypeInfo polygon = (PolygonTypeInfo) geom;
			OuterBoundaryIsInfo outer = polygon.getOuterBoundaryIs();
			LinearRing shell = toLinearRing(factory, outer.getLinearRing());
			if (polygon.getInnerBoundaryIList() == null) {
				geometry = factory.createPolygon(shell, null);
			} else {
				LinearRing[] holes = new LinearRing[polygon.getInnerBoundaryIList().size()];
				int i = 0;
				for (InnerBoundaryIsInfo inner : polygon.getInnerBoundaryIList()) {
					holes[i++] = toLinearRing(factory, inner.getLinearRing());
				}
				geometry = factory.createPolygon(shell, holes);
			}
		}
		return geometry;
	}

	private LinearRing toLinearRing(GeometryFactory factory, LinearRingTypeInfo linearRing) throws LayerException {
		LinearRing ring = null;
		if (linearRing.ifCoordList()) {
			ring = factory.createLinearRing(getCoordinates(linearRing.getCoordList()));
		} else if (linearRing.ifCoordinates()) {
			ring = factory.createLinearRing(getCoordinates(linearRing.getCoordinates()));
		}
		return ring;
	}

	private Coordinate[] getCoordinates(List<CoordTypeInfo> coords) {
		Coordinate[] result = new Coordinate[coords.size()];
		int i = 0;
		for (CoordTypeInfo coordinate : coords) {
			result[i++] = new Coordinate(coordinate.getX().doubleValue(), coordinate.getY().doubleValue());
		}
		return result;
	}

	private Coordinate[] getCoordinates(CoordinatesTypeInfo coords) throws LayerException {
		String cs = coords.getCs() == null ? "," : coords.getCs();
		String ts = coords.getTs() == null ? "," : coords.getTs();
		String ds = coords.getDecimal() == null ? "." : coords.getDecimal();
		String[] coordinates = coords.getString().trim().replace(ds, ".").replace(ts, ",")
				.replace(cs, ",").split("[\\s,]+");
		Coordinate[] result = new Coordinate[coordinates.length / 2];
		for (int i = 0; i < coordinates.length; i += 2) {
			double x = parseDouble(coordinates[i].replace(ds, "."));
			double y = parseDouble(coordinates[i + 1].replace(ds, "."));
			result[i / 2] = new Coordinate(x, y);
		}
		return result;
	}

	private String toFeatureIds(List<FeatureIdTypeInfo> featureIds) {
		StringBuilder stringBuilder = new StringBuilder("IN (");
		for (Iterator<FeatureIdTypeInfo> it = featureIds.iterator(); it.hasNext();) {
			stringBuilder.append("'").append(it.next().getFid()).append("'");
			stringBuilder.append(it.hasNext() ? "," : ")");
		}
		return stringBuilder.toString();
	}

	private String toPropertyName(String propertyName) {
		return propertyName.replaceAll(ASSOCIATION_DELIMITER_SOURCE, ASSOCIATION_DELIMITER_TARGET);
	}
	
	private String toComparison(ComparisonOpsTypeInfo coOps, FeatureInfo featureInfo) {
		if (coOps instanceof BinaryComparisonOpTypeInfo) {
			BinaryComparisonOpTypeInfo binary = (BinaryComparisonOpTypeInfo) coOps;
			String propertyName = toPropertyName(binary.getExpressionList().get(0).getValue());
			String propertyValue = binary.getExpressionList().get(1).getValue();
			PrimitiveType type = PrimitiveType.STRING;
			for (AbstractAttributeInfo attributeInfo : featureInfo.getAttributes()) {
				if (attributeInfo.getName().equals(propertyName)) {
					if (attributeInfo instanceof PrimitiveAttributeInfo) {
						type = ((PrimitiveAttributeInfo) attributeInfo).getType();
					}
				}
			}
			switch (type) {
				case BOOLEAN:
					propertyValue = propertyValue.toUpperCase();
					break;
				// string types must be quoted
				case DATE:
				case IMGURL:
				case STRING:
				case URL:
				case CURRENCY:
					propertyValue = "'" + propertyValue + "'";
					break;
				// numerical types unquoted
				case DOUBLE:
				case FLOAT:
				case INTEGER:
				case LONG:
				case SHORT:
				default:
					break;
			}
			if (binary instanceof PropertyIsEqualToInfo) {
				return propertyName + " = " + propertyValue;
			} else if (binary instanceof PropertyIsGreaterThanInfo) {
				return propertyName + " > " + propertyValue;
			} else if (binary instanceof PropertyIsGreaterThanOrEqualToInfo) {
				return propertyName + " >= " + propertyValue;
			} else if (binary instanceof PropertyIsLessThanInfo) {
				return propertyName + " < " + propertyValue;
			} else if (binary instanceof PropertyIsLessThanOrEqualToInfo) {
				return propertyName + " <= " + propertyValue;
			} else if (binary instanceof PropertyIsNotEqualToInfo) {
				return propertyName + " != " + propertyValue;
			} else {
				throw new IllegalStateException("Unknown binary comparison operator " + binary);
			}
		} else {
			if (coOps instanceof PropertyIsBetweenTypeInfo) {
				PropertyIsBetweenTypeInfo isBetween = (PropertyIsBetweenTypeInfo) coOps;
				String lower = isBetween.getLowerBoundary().getExpression().getValue();
				String upper = isBetween.getUpperBoundary().getExpression().getValue();
				String propertyName = toPropertyName(isBetween.getExpression().getValue());
				return propertyName + " BETWEEN " + lower + " AND " + upper;
			} else if (coOps instanceof PropertyIsLikeTypeInfo) {
				PropertyIsLikeTypeInfo isLike = (PropertyIsLikeTypeInfo) coOps;
				String propertyName = toPropertyName(isLike.getPropertyName().getValue());
				return propertyName + " LIKE '" + isLike.getLiteral().getValue() + "'";
			} else if (coOps instanceof PropertyIsNullTypeInfo) {
				PropertyIsNullTypeInfo isNull = (PropertyIsNullTypeInfo) coOps;
				String what;
				if (isNull.ifLiteral()) {
					what = isNull.getLiteral().getValue();
				} else {
					what = isNull.getPropertyName().getValue();
				}
				return what + " IS NULL ";
			} else {
				throw new IllegalStateException("Unknown comparison operator " + coOps);
			}
		}
	}

	private Map<String, String> getLiteralMap(List<CssParameterInfo> css) {
		HashMap<String, String> result = new HashMap<String, String>();
		if (css != null) {
			for (CssParameterInfo cssParameter : css) {
				// check expressions first, if present ignore value
				if (cssParameter.getExpressionList() != null) {
					if (cssParameter.getExpressionList().size() > 0) {
						ExpressionInfo expression = cssParameter.getExpressionList().get(0);
						if (expression instanceof LiteralTypeInfo) {
							result.put(cssParameter.getName(), expression.getValue());
						}
					}
				} else if (cssParameter.getValue() != null) {
					// ignore spaces and tabs for CSS
					String value = cssParameter.getValue().trim();
					if (!value.isEmpty()) {
						result.put(cssParameter.getName(), value);
					}
				}
			}
		}
		return result;
	}

	private String getParameterValue(ParameterValueTypeInfo parameter) {
		if (parameter.getValue() != null) {
			return parameter.getValue();
		} else if (parameter.getExpressionList().size() > 0) {
			ExpressionInfo expression = parameter.getExpressionList().get(0);
			if (expression instanceof LiteralTypeInfo) {
				return expression.getValue();
			}
		}
		return null;
	}

	private Rule createRule(Filter filter, FeatureStyleInfo featureStyle) throws LayerException {
		Rule rule = styleBuilder.createRule(createGeometrySymbolizer(featureStyle));
		if (filter.equals(Filter.INCLUDE)) {
			rule.setElseFilter(true);
		} else {
			rule.setFilter(filter);
		}
		rule.setName(featureStyle.getName());
		rule.setTitle(featureStyle.getName());
		return rule;
	}

	private Symbolizer createGeometrySymbolizer(FeatureStyleInfo featureStyle) throws LayerException {
		Symbolizer symbolizer;
		switch (featureStyle.getLayerType()) {
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
				Expression size = null;
				if (symbol instanceof Mark) {
					SymbolInfo info = featureStyle.getSymbol();
					if (info.getRect() != null) {
						size = styleBuilder.literalExpression((int) info.getRect().getW());
					} else if (info.getCircle() != null) {
						size = styleBuilder.literalExpression(2 * (int) info.getCircle().getR());
					} // else {} already handled by createSymbol()
				} else {
					size = styleBuilder.literalExpression(featureStyle.getSymbol().getImage().getHeight());
				}
				ps.getGraphic().setSize(size);
				ps.getGraphic().graphicalSymbols().clear();
				ps.getGraphic().graphicalSymbols().add(createSymbol(featureStyle));
				symbolizer = ps;
				break;
			default:
				throw new IllegalArgumentException("Unsupported geometry type " + featureStyle.getLayerType());
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

	private GraphicalSymbol createSymbol(FeatureStyleInfo featureStyle) throws LayerException {
		SymbolInfo info = featureStyle.getSymbol();
		if (info.getImage() != null) {

			return styleBuilder.createExternalGraphic(getUrl(info.getImage().getHref()), getFormat(info.getImage()
					.getHref()));
		} else {
			Mark mark;
			if (info.getRect() != null) {
				// TODO: do rectangles by adding custom factory ?
				mark = styleBuilder.createMark(MARK_SQUARE);
			} else if (info.getCircle() != null) {
				mark = styleBuilder.createMark(MARK_CIRCLE);
			} else {
				throw new IllegalArgumentException(
						"Feature style should have either an image, a circle or a rectangle defined. Style name: " +
								featureStyle.getName() + ", index: " + featureStyle.getIndex());
			}
			mark.setFill(createFill(featureStyle));
			mark.setStroke(createStroke(featureStyle));
			return mark;
		}
	}

	private Stroke createStroke(FeatureStyleInfo featureStyle) throws LayerException {
		Stroke stroke = styleBuilder.createStroke(styleBuilder.literalExpression(featureStyle.getStrokeColor()),
				styleBuilder.literalExpression(featureStyle.getStrokeWidth()),
				styleBuilder.literalExpression(featureStyle.getStrokeOpacity()));
		if (featureStyle.getDashArray() != null) {
			String[] strings = featureStyle.getDashArray().split(",");
			float[] nrs = new float[strings.length];
			for (int i = 0; i < strings.length; i++) {
				try {
					nrs[i] = parseFloat(strings[i]);
				} catch (NumberFormatException e) {
					log.warn("Dash array cannot be parsed " + featureStyle.getDashArray(), e);
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

	private URL getUrl(String resourceLocation) throws LayerException {
		if (resourceLocation.startsWith(GeomajasConstant.CLASSPATH_URL_PREFIX)) {
			resourceLocation = resourceLocation.substring(GeomajasConstant.CLASSPATH_URL_PREFIX.length());
		}
		Resource resource = applicationContext.getResource(resourceLocation);
		try {
			if (resource.exists()) {
				return resource.getURL();
			} else {
				String gwtResource = GeomajasConstant.CLASSPATH_URL_PREFIX + resourceLocation;
				Resource[] matching = applicationContext.getResources(gwtResource);
				if (matching.length > 0) {
					return matching[0].getURL();
				} else {
					log.warn(MISSING_RESOURCE, gwtResource);
					throw new LayerException(ExceptionCode.RESOURCE_NOT_FOUND, gwtResource);
				}
			}
		} catch (IOException e) {
			log.warn(MISSING_RESOURCE, resourceLocation);
			throw new LayerException(e, ExceptionCode.RESOURCE_NOT_FOUND, resourceLocation);
		}
	}

	private String getFormat(String href) {
		return "image/" + StringUtils.getFilenameExtension(href);
	}

	/**
	 * Finish service initialization.
	 */
	@PostConstruct
	protected void postConstruct() {
		styleBuilder = new StyleBuilder(filterService.getFilterFactory());
	}
	
	/**
	 * A custom {@link ResourceLocator} that uses the {@link ResourceService} for URL location.
	 * 
	 * @author Jan De Moerloose
	 * 
	 */
	class ResourceServiceBasedLocator implements ResourceLocator {

		public URL locateResource(String uri) {
			URL url = null;
			try {
				Resource resource = resourceService.find(uri);
				url = resource.getURL();
			} catch (Exception e) { // NOSONAR
				log.warn(MISSING_RESOURCE, uri);
			}
			return url;
		}

	}


}
