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
package org.geomajas.plugin.rasterizing.layer;

import java.awt.Rectangle;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.NavigableSet;
import java.util.Set;
import java.util.TreeSet;

import org.geomajas.configuration.AbstractAttributeInfo;
import org.geomajas.configuration.AssociationAttributeInfo;
import org.geomajas.configuration.FeatureInfo;
import org.geomajas.configuration.GeometryAttributeInfo;
import org.geomajas.configuration.PrimitiveAttributeInfo;
import org.geomajas.configuration.SyntheticAttributeInfo;
import org.geomajas.configuration.VectorLayerInfo;
import org.geomajas.configuration.client.ClientLayerInfo;
import org.geomajas.configuration.client.ClientVectorLayerInfo;
import org.geomajas.geometry.Crs;
import org.geomajas.global.ExceptionCode;
import org.geomajas.global.GeomajasException;
import org.geomajas.layer.VectorLayer;
import org.geomajas.layer.VectorLayerService;
import org.geomajas.layer.feature.InternalFeature;
import org.geomajas.plugin.rasterizing.api.LayerFactory;
import org.geomajas.plugin.rasterizing.command.dto.VectorLayerRasterizingInfo;
import org.geomajas.plugin.rasterizing.sld.SymbolizerFilterVisitor;
import org.geomajas.service.ConfigurationService;
import org.geomajas.service.DtoConverterService;
import org.geomajas.service.FilterService;
import org.geomajas.service.GeoService;
import org.geomajas.service.StyleConverterService;
import org.geomajas.sld.RuleInfo;
import org.geomajas.sld.UserStyleInfo;
import org.geomajas.sld.expression.ExpressionInfo;
import org.geomajas.sld.expression.LiteralTypeInfo;
import org.geomajas.sld.expression.PropertyNameInfo;
import org.geomajas.sld.filter.FilterTypeInfo;
import org.geomajas.sld.filter.PropertyIsEqualToInfo;
import org.geotools.data.collection.ListFeatureCollection;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.FeatureIterator;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.map.FeatureLayer;
import org.geotools.map.Layer;
import org.geotools.map.MapContext;
import org.geotools.renderer.lite.MetaBufferEstimator;
import org.geotools.renderer.lite.RendererUtilities;
import org.geotools.styling.FeatureTypeStyle;
import org.geotools.styling.Rule;
import org.geotools.styling.Style;
import org.geotools.styling.StyleAttributeExtractor;
import org.jboss.serial.io.JBossObjectOutputStream;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.filter.Filter;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.vividsolutions.jts.geom.Envelope;

/**
 * This factory creates a GeoTools layer that is capable of writing vector layers.
 * 
 * @author Jan De Moerloose
 */
@Component
public class VectorLayerFactory implements LayerFactory {

	private static final String NORMAL_RULE_ATTRIBUTE_NAME = "geomajas_normal_rule_index";
	private static final String SELECTED_RULE_ATTRIBUTE_NAME = "geomajas_selected_rule_index";

	private final Logger log = LoggerFactory.getLogger(VectorLayerFactory.class);

	@Autowired
	private VectorLayerService vectorLayerService;

	@Autowired
	private GeoService geoService;

	@Autowired
	private FilterService filterService;

	@Autowired
	private StyleConverterService styleConverterService;

	@Autowired
	private ConfigurationService configurationService;

	@Autowired
	private DtoConverterService dtoConverterService;

	/** Tolerance used to compare doubles for equality */
	private static final double TOLERANCE = 1e-6;

	public boolean canCreateLayer(MapContext mapContext, ClientLayerInfo clientLayerInfo) {
		return clientLayerInfo instanceof ClientVectorLayerInfo;
	}

	public Layer createLayer(MapContext mapContext, ClientLayerInfo clientLayerInfo) throws GeomajasException {
		if (!(clientLayerInfo instanceof ClientVectorLayerInfo)) {
			throw new IllegalArgumentException(
					"VectorLayerFactory.createLayer() should only be called using ClientVectorLayerInfo");
		}
		ClientVectorLayerInfo vectorInfo = (ClientVectorLayerInfo) clientLayerInfo;
		VectorLayerRasterizingInfo extraInfo = (VectorLayerRasterizingInfo) vectorInfo
				.getWidgetInfo(VectorLayerRasterizingInfo.WIDGET_KEY);
		ReferencedEnvelope areaOfInterest = mapContext.getAreaOfInterest();
		VectorLayer layer = configurationService.getVectorLayer(vectorInfo.getServerLayerId());
		// need to clone the extra info object before changing it !
		VectorLayerRasterizingInfo rasterizingInfo = cloneInfo(extraInfo);
		
		// get the style dto
		UserStyleInfo userStyleInfo = rasterizingInfo.getStyle().getUserStyle();
		
		// create the style (original filters)
		Style originalStyle = styleConverterService.convert(userStyleInfo);

		// add the selection rule and replace filters by index filters
		List<RuleInfo> allRules = userStyleInfo.getFeatureTypeStyleList().get(0).getRuleList();
		for (int ruleIndex = 0; ruleIndex < allRules.size(); ruleIndex++) {
			setRuleFilter(allRules.get(ruleIndex), NORMAL_RULE_ATTRIBUTE_NAME, Integer.toString(ruleIndex));
		}
		RuleInfo selectionrule = extraInfo.getSelectionRule();
		if (selectionrule != null) {
			allRules.add(selectionrule);
			setRuleFilter(selectionrule, SELECTED_RULE_ATTRIBUTE_NAME, "true");			
		}
		// create the style (index filters)
		Style indexedStyle = styleConverterService.convert(userStyleInfo);
				
		// remove either label or geometries 
		SymbolizerFilterVisitor visitor = new SymbolizerFilterVisitor();
		visitor.setIncludeGeometry(rasterizingInfo.isPaintGeometries());
		visitor.setIncludeText(rasterizingInfo.isPaintLabels());
		visitor.visit(indexedStyle);
		indexedStyle = (Style) visitor.getCopy();
		
		// estimate the buffer
		MetaBufferEstimator estimator = new MetaBufferEstimator();
		estimator.visit(originalStyle);
		int bufferInPixels = estimator.getBuffer();
		
		// expand area to include buffer
		Rectangle tileInpix = mapContext.getViewport().getScreenArea();
		double tileWidth = tileInpix.getWidth();
		if (tileWidth < 1.0) {
			tileWidth = 1.0;
		}
		double tileHeight = tileInpix.getHeight();
		if (tileHeight < 1.0) {
			tileHeight = 1.0;
		}
		ReferencedEnvelope metaArea = new ReferencedEnvelope(areaOfInterest);
		metaArea.expandBy(bufferInPixels / tileWidth * areaOfInterest.getWidth(),
				bufferInPixels / tileHeight * areaOfInterest.getHeight());
		
		// fetch features in meta area
		Crs layerCrs = vectorLayerService.getCrs(layer);
		Envelope layerBounds = geoService.transform(metaArea, (Crs) areaOfInterest.getCoordinateReferenceSystem(),
				layerCrs);
		Filter filter = filterService.createBboxFilter(layerCrs, layerBounds,
				layer.getLayerInfo().getFeatureInfo().getGeometryType().getName());
		if (extraInfo.getFilter() != null) {
			filter = filterService.createAndFilter(filter, filterService.parseFilter(extraInfo.getFilter()));
		}
		List<InternalFeature> features = vectorLayerService.getFeatures(vectorInfo.getServerLayerId(),
				mapContext.getCoordinateReferenceSystem(), filter, extraInfo.getStyle(),
				VectorLayerService.FEATURE_INCLUDE_ALL);

		// find the selected ids
		Set<String> selectedIds = new HashSet<String>();
		if (selectionrule != null) {
			selectedIds.addAll(Arrays.asList(rasterizingInfo.getSelectedFeatureIds()));
		}

		// must convert to geotools features because StreamingRenderer does not work on objects
		// must disable out-of-scale rules for correct rule index assignment
		disableOutOfScale(mapContext, originalStyle);
		FeatureCollection<SimpleFeatureType, SimpleFeature> gtFeatures = createCollection(features, layer, selectedIds,
				mapContext.getCoordinateReferenceSystem(), originalStyle);
				
		// create the layer
		FeatureLayer featureLayer = new FeatureLayer(gtFeatures, indexedStyle);
		featureLayer.setTitle(vectorInfo.getLabel());
		featureLayer.getUserData().put(USERDATA_KEY_SHOWING, extraInfo.isShowing());
		featureLayer.getUserData().put(USERDATA_KEY_LAYER_ID, layer.getId());
		
		// find the applicable rules		
		TreeSet<Integer> ruleIndices = new TreeSet<Integer>();
		findRules(featureLayer, ruleIndices);
		removeOutOfScale(mapContext, indexedStyle, ruleIndices);
		
		// create the final  list
		List<RuleInfo> ruleInfos = extractIndices(allRules, ruleIndices);
		if (selectionrule != null) {
			ruleInfos.add(selectionrule);
		}
		featureLayer.getUserData().put(USERDATA_KEY_STYLE_RULES, ruleInfos);
		return featureLayer;
	}

	private List<RuleInfo> extractIndices(List<RuleInfo> allRules, NavigableSet<Integer> ruleIndices) {
		List<RuleInfo> ruleInfos = new ArrayList<RuleInfo>();
		for (Integer index : ruleIndices) {
			if (index < allRules.size()) {
				ruleInfos.add(allRules.get(index));
			}
		}
		return ruleInfos;
	}

	private void disableOutOfScale(MapContext mapContext, Style indexedStyle) {
		double scaleDenominator = RendererUtilities.calculateOGCScale(mapContext.getAreaOfInterest(), (int) mapContext
				.getViewport().getScreenArea().getWidth(), null);
		for (FeatureTypeStyle fts : indexedStyle.featureTypeStyles()) {
			for (Rule rule : fts.rules()) {
				if (!isWithInScale(rule, scaleDenominator)) {
					rule.setFilter(Filter.EXCLUDE);
				}
			}
		}
	}

	private void removeOutOfScale(MapContext mapContext, Style indexedStyle, NavigableSet<Integer> ruleIndices) {
		double scaleDenominator = RendererUtilities.calculateOGCScale(mapContext.getAreaOfInterest(), (int) mapContext
				.getViewport().getScreenArea().getWidth(), null);
		int ruleIndex = 0;
		for (FeatureTypeStyle fts : indexedStyle.featureTypeStyles()) {
			for (Rule rule : fts.rules()) {
				if (!isWithInScale(rule, scaleDenominator)) {
					ruleIndices.remove(ruleIndex);
				}
				ruleIndex++;
			}
		}
	}

	private void findRules(FeatureLayer featureLayer, NavigableSet<Integer> ruleIndices) {
		FeatureIterator<SimpleFeature> it2;
		try {
			it2 = featureLayer.getSimpleFeatureSource().getFeatures().features();
			while (it2.hasNext()) {
				SimpleFeature feature = it2.next();
				int index = (Integer) feature.getAttribute(NORMAL_RULE_ATTRIBUTE_NAME);
				if (index != -1) {
					ruleIndices.add(index);
				}
			}
		} catch (IOException e) {
			log.error("Unexpected error finding rules in layer", e);
			// cannot happen !
		}
	}

	private void setRuleFilter(RuleInfo ruleInfo, String attributeName, String value) {
		PropertyIsEqualToInfo prop = new PropertyIsEqualToInfo();
		List<ExpressionInfo> nameValue = new ArrayList<ExpressionInfo>();
		nameValue.add(new PropertyNameInfo(attributeName));
		nameValue.add(new LiteralTypeInfo(value));
		prop.setExpressionList(nameValue);
		ruleInfo.setChoice(new RuleInfo.ChoiceInfo());
		FilterTypeInfo filter = new FilterTypeInfo();
		filter.setComparisonOps(prop);
		ruleInfo.getChoice().setFilter(filter);		
	}

	/**
	 * Should be the same as {@link org.geotools.renderer.lite.StreamingRenderer} !
	 * 
	 * @param r rule
	 * @param scaleDenominator scale denominator
	 * @return true if within scale
	 */
	private boolean isWithInScale(Rule r, double scaleDenominator) {
		return ((r.getMinScaleDenominator() - TOLERANCE) <= scaleDenominator)
				&& ((r.getMaxScaleDenominator() + TOLERANCE) > scaleDenominator);
	}

	private FeatureCollection<SimpleFeatureType, SimpleFeature> createCollection(List<InternalFeature> features,
			VectorLayer layer, Set<String> selectedIds, CoordinateReferenceSystem mapCrs, Style style) {
		SimpleFeatureType type = createFeatureType(layer, mapCrs);
		ListFeatureCollection result = new ListFeatureCollection(type);
		SimpleFeatureBuilder builder = new SimpleFeatureBuilder(type);
		StyleAttributeExtractor extractor = new StyleAttributeExtractor();
		style.accept(extractor);
		Set<String> styleAttributeNames = extractor.getAttributeNameSet();
		FeatureInfo featureInfo = layer.getLayerInfo().getFeatureInfo();
		for (InternalFeature internalFeature : features) {
			// 3 more attributes : normal style rule index, selected style rule index, geometry index
			Object[] values = new Object[type.getAttributeCount()];
			int i = 0;
			for (AbstractAttributeInfo attrInfo : featureInfo.getAttributes()) {
				String name = attrInfo.getName();
				if (styleAttributeNames.contains(name)) {
					values[i++] = internalFeature.getAttributes().get(name).getValue();
				} else {
					values[i++] = null;
				}
			}
			// normal style rule index attribute (TODO deprecate the whole idea of coupling single rule to feature)
			int ruleIndex = -1;
			// find the rule, we assume there is only one applicable !
			List<Rule> rules = style.featureTypeStyles().get(0).rules();
			for (int j = 0; j < rules.size(); j++) {
				Rule rule = rules.get(j);
				if (rule.getFilter() == null || rule.getFilter().evaluate(internalFeature)) {
					ruleIndex = j;
					break;
				}
			}
			values[i++] = ruleIndex;
			// selected style rule index attribute
			values[i++] = selectedIds.contains(internalFeature.getId());
			// geometry attribute
			values[i] = internalFeature.getGeometry();
			result.add(builder.buildFeature(internalFeature.getId(), values));
		}
		return result;
	}

	private SimpleFeatureType createFeatureType(VectorLayer layer, CoordinateReferenceSystem mapCrs) {
		SimpleFeatureTypeBuilder builder = new SimpleFeatureTypeBuilder();
		VectorLayerInfo info = layer.getLayerInfo();
		builder.setName(info.getFeatureInfo().getDataSourceName());
		builder.setCRS(mapCrs);
		for (AbstractAttributeInfo attrInfo : info.getFeatureInfo().getAttributes()) {
			if (attrInfo instanceof PrimitiveAttributeInfo) {
				PrimitiveAttributeInfo prim = (PrimitiveAttributeInfo) attrInfo;
				switch (prim.getType()) {
					case BOOLEAN:
						builder.add(prim.getName(), Boolean.class);
						break;
					case CURRENCY:
						builder.add(prim.getName(), BigDecimal.class);
						break;
					case DATE:
						builder.add(prim.getName(), Date.class);
						break;
					case DOUBLE:
						builder.add(prim.getName(), Double.class);
						break;
					case FLOAT:
						builder.add(prim.getName(), Float.class);
						break;
					case INTEGER:
						builder.add(prim.getName(), Integer.class);
						break;
					case LONG:
						builder.add(prim.getName(), Long.class);
						break;
					case SHORT:
						builder.add(prim.getName(), Short.class);
						break;
					case STRING:
					case URL:
					case IMGURL:
						builder.add(prim.getName(), String.class);
						break;
					default:
						throw new IllegalStateException("Unknown primitive attribute type " + prim.getType());
				}
			} else if (attrInfo instanceof AssociationAttributeInfo) {
				AssociationAttributeInfo ass = (AssociationAttributeInfo) attrInfo;
				switch (ass.getType()) {
					case MANY_TO_ONE:
						builder.add(ass.getName(), Object.class);
						break;
					case ONE_TO_MANY:
						builder.add(ass.getName(), Collection.class);
						break;
					default:
						throw new IllegalStateException("Unknown association attribute type " + ass.getType());
				}
			} else if (attrInfo instanceof SyntheticAttributeInfo) {
				SyntheticAttributeInfo synth = (SyntheticAttributeInfo) attrInfo;
				// can't determine type, using object
				builder.add(synth.getName(), Object.class);
			} else {
				throw new IllegalStateException("Unhandled attribute info for attribute " + attrInfo.getName());
			}
		}
		// add the extra rule index attributes
		builder.add(NORMAL_RULE_ATTRIBUTE_NAME, Integer.class);
		builder.add(SELECTED_RULE_ATTRIBUTE_NAME, Boolean.class);
		// add the geometry attribute
		GeometryAttributeInfo geom = info.getFeatureInfo().getGeometryType();
		builder.add(geom.getName(), dtoConverterService.toInternal(info.getLayerType()), mapCrs);
		builder.setDefaultGeometry(geom.getName());
		return builder.buildFeatureType();
	}

	private VectorLayerRasterizingInfo cloneInfo(VectorLayerRasterizingInfo input) throws GeomajasException {
		try {
			JBossObjectOutputStream jbossSerializer = new JBossObjectOutputStream(null);
			Object obj = jbossSerializer.smartClone(input);
			return (VectorLayerRasterizingInfo) obj;
		} catch (IOException e) {
			// should not happen
			throw new GeomajasException(e, ExceptionCode.UNEXPECTED_PROBLEM);
		}
	}

}
