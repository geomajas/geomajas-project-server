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
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

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
import org.geomajas.sld.FeatureTypeStyleInfo;
import org.geomajas.sld.RuleInfo;
import org.geomajas.sld.UserStyleInfo;
import org.geotools.data.collection.CollectionFeatureSource;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.map.FeatureLayer;
import org.geotools.map.Layer;
import org.geotools.map.MapContext;
import org.geotools.renderer.lite.MetaBufferEstimator;
import org.geotools.styling.FeatureTypeStyle;
import org.geotools.styling.Rule;
import org.geotools.styling.Style;
import org.jboss.serial.io.JBossObjectOutputStream;
import org.opengis.filter.Filter;
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
		Style style = styleConverterService.convert(userStyleInfo);
				
		// remove either label or geometries 
		SymbolizerFilterVisitor visitor = new SymbolizerFilterVisitor();
		visitor.setIncludeGeometry(rasterizingInfo.isPaintGeometries());
		visitor.setIncludeText(rasterizingInfo.isPaintLabels());
		visitor.visit(style);
		style = (Style) visitor.getCopy();
		
		// add the selection rule
		RuleInfo selectionrule = extraInfo.getSelectionRule();
		if (selectionrule != null) {
			Rule gtRule = styleConverterService.convert(selectionrule);
			// filter on id
			gtRule.setFilter(filterService.createFidFilter(rasterizingInfo.getSelectedFeatureIds()));
			// copy to style
			style.featureTypeStyles().get(0).rules().add(gtRule);
			// copy to userStyle
			userStyleInfo.getFeatureTypeStyleList().get(0).getRuleList().add(selectionrule);
		}

		// estimate the buffer
		MetaBufferEstimator estimator = new MetaBufferEstimator();
		estimator.visit(style);
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
		List<InternalFeature> features = vectorLayerService.getFeatures(vectorInfo.getServerLayerId(),
				mapContext.getCoordinateReferenceSystem(), filter, extraInfo.getStyle(),
				VectorLayerService.FEATURE_INCLUDE_ALL);

		// must convert to geotools features because StreamingRenderer does not work on objects
		InternalFeatureCollection gtFeatures = new InternalFeatureCollection(layer,
				mapContext.getCoordinateReferenceSystem(), features, metaArea);
				
		// create the layer
		FeatureLayer featureLayer = new FeatureLayer(new CollectionFeatureSource(gtFeatures), style);
		featureLayer.setTitle(vectorInfo.getLabel());
		featureLayer.getUserData().put(USERDATA_KEY_SHOWING, extraInfo.isShowing());
		featureLayer.getUserData().put(USERDATA_KEY_LAYER_ID, layer.getId());
		
		// filter out the rules that are never used
		int fIndex = 0;
		for (FeatureTypeStyle fts : style.featureTypeStyles()) {
			FeatureTypeStyleInfo ftsInfo = userStyleInfo.getFeatureTypeStyleList().get(fIndex); 
			ListIterator<RuleInfo> it1 = ftsInfo.getRuleList().listIterator();
			for (ListIterator<Rule> it = fts.rules().listIterator(); it.hasNext();) {
				Rule rule = it.next();
				it1.next();
				Filter f = (rule.getFilter() != null ? rule.getFilter() : Filter.INCLUDE);
				if (gtFeatures.subCollection(f).isEmpty()) {
					it.remove();
					it1.remove();
				}
			}
			fIndex++;
		}
		
		featureLayer.getUserData().put(USERDATA_KEY_STYLE_RULES,
				userStyleInfo.getFeatureTypeStyleList().get(0).getRuleList());
		return featureLayer;
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
	
	public Map<String, Object> getLayerUserData(MapContext mapContext, ClientLayerInfo clientLayerInfo) {
		Map<String, Object> userData = new HashMap<String, Object>();
		VectorLayerRasterizingInfo extraInfo = (VectorLayerRasterizingInfo) clientLayerInfo
		.getWidgetInfo(VectorLayerRasterizingInfo.WIDGET_KEY);
		userData.put(USERDATA_KEY_SHOWING, extraInfo.isShowing());
		return userData;
	}	

}
