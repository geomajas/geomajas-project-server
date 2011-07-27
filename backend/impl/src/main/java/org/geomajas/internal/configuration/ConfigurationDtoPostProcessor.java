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
package org.geomajas.internal.configuration;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.geomajas.configuration.AttributeInfo;
import org.geomajas.configuration.FeatureStyleInfo;
import org.geomajas.configuration.LayerInfo;
import org.geomajas.configuration.NamedStyleInfo;
import org.geomajas.configuration.RasterLayerInfo;
import org.geomajas.configuration.VectorLayerInfo;
import org.geomajas.configuration.client.ClientApplicationInfo;
import org.geomajas.configuration.client.ClientLayerInfo;
import org.geomajas.configuration.client.ClientLayerTreeInfo;
import org.geomajas.configuration.client.ClientLayerTreeNodeInfo;
import org.geomajas.configuration.client.ClientMapInfo;
import org.geomajas.configuration.client.ClientVectorLayerInfo;
import org.geomajas.configuration.client.ScaleInfo;
import org.geomajas.geometry.Bbox;
import org.geomajas.geometry.Coordinate;
import org.geomajas.geometry.Crs;
import org.geomajas.geometry.CrsTransform;
import org.geomajas.global.ExceptionCode;
import org.geomajas.global.GeomajasException;
import org.geomajas.layer.Layer;
import org.geomajas.layer.LayerException;
import org.geomajas.layer.RasterLayer;
import org.geomajas.layer.VectorLayer;
import org.geomajas.service.DtoConverterService;
import org.geomajas.service.GeoService;
import org.geomajas.service.StyleConverterService;
import org.geomajas.sld.StyledLayerDescriptorInfo;
import org.geotools.geometry.DirectPosition2D;
import org.geotools.referencing.GeodeticCalculator;
import org.jibx.runtime.BindingDirectory;
import org.jibx.runtime.IBindingFactory;
import org.jibx.runtime.IUnmarshallingContext;
import org.jibx.runtime.JiBXException;
import org.opengis.referencing.operation.TransformException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanInitializationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import com.vividsolutions.jts.geom.Envelope;

/**
 * Post-processes configuration DTOs. Generally responsible for any behaviour that would violate the DTO contract
 * (especially for GWT) if it would be added to the configuration objects themselves, such as hooking up client
 * configurations to their server layers.
 * 
 * @author Jan De Moerloose
 */
@Component
public class ConfigurationDtoPostProcessor {

	private static final double METER_PER_INCH = 0.0254;

	private final Logger log = LoggerFactory.getLogger(ConfigurationDtoPostProcessor.class);

	@Autowired
	private DtoConverterService converterService;

	@Autowired
	private GeoService geoService;

	@Autowired
	private StyleConverterService styleConverterService;

	@Autowired(required = false)
	protected Map<String, ClientApplicationInfo> applicationMap = new LinkedHashMap<String, ClientApplicationInfo>();

	@Autowired(required = false)
	protected Map<String, NamedStyleInfo> namedStyleMap = new LinkedHashMap<String, NamedStyleInfo>();

	@Autowired(required = false)
	protected Map<String, Layer<?>> layerMap = new LinkedHashMap<String, Layer<?>>();

	@Autowired(required = false)
	protected Map<String, RasterLayer> rasterLayerMap = new LinkedHashMap<String, RasterLayer>();

	@Autowired(required = false)
	protected Map<String, VectorLayer> vectorLayerMap = new LinkedHashMap<String, VectorLayer>();

	@Autowired(required = true)
	private ApplicationContext applicationContext;

	public ConfigurationDtoPostProcessor() {

	}

	@PostConstruct
	protected void processConfiguration() throws BeansException {
		try {
			for (RasterLayer layer : rasterLayerMap.values()) {
				postProcess(layer);
			}
			for (VectorLayer layer : vectorLayerMap.values()) {
				postProcess(layer);
			}
			for (ClientApplicationInfo application : applicationMap.values()) {
				postProcess(application);
			}
			for (NamedStyleInfo style : namedStyleMap.values()) {
				postProcess(style);
			}
		} catch (LayerException e) {
			throw new BeanInitializationException("Invalid configuration", e);
		}
	}

	private void postProcess(RasterLayer layer) throws LayerException {
		RasterLayerInfo info = layer.getLayerInfo();
		for (ScaleInfo scale : info.getZoomLevels()) {
			// for raster layers we don't accept 1 : x notation !
			if (scale.getDenominator() != 0) {
				throw new LayerException(ExceptionCode.CONVERSION_PROBLEM, "Raster layer " + layer.getId()
						+ " has zoom level " + scale.getNumerator() + " : " + scale.getDenominator()
						+ " in disallowed 1 : x notation");
			}
			// add the resolution for deprecated api support
			info.getResolutions().add(1. / scale.getPixelPerUnit());
		}
	}

	private void postProcess(VectorLayer layer) throws LayerException {
		VectorLayerInfo info = layer.getLayerInfo();
		if (info != null) {
			// check for invalid attribute names
			for (AttributeInfo attributeInfo : info.getFeatureInfo().getAttributes()) {
				if (attributeInfo.getName().contains(".") || attributeInfo.getName().contains("/")) {
					throw new LayerException(ExceptionCode.INVALID_ATTRIBUTE_NAME, attributeInfo.getName(),
							layer.getId());
				}
			}
			// apply defaults to all styles
			for (NamedStyleInfo namedStyle : info.getNamedStyleInfos()) {
				// check sld location
				if (namedStyle.getSldLocation() != null) {
					Resource resource = applicationContext.getResource(namedStyle.getSldLocation());
					IBindingFactory bindingFactory;
					try {
						bindingFactory = BindingDirectory.getFactory(StyledLayerDescriptorInfo.class);
						IUnmarshallingContext unmarshallingContext = bindingFactory.createUnmarshallingContext();
						StyledLayerDescriptorInfo sld = (StyledLayerDescriptorInfo) unmarshallingContext
								.unmarshalDocument(new InputStreamReader(resource.getInputStream()));
						namedStyle.setStyledLayerInfo(sld);
					} catch (JiBXException e) {
						throw new LayerException(e, ExceptionCode.INVALID_SLD, namedStyle.getSldLocation(),
								layer.getId());
					} catch (IOException e) {
						throw new LayerException(e, ExceptionCode.INVALID_SLD, namedStyle.getSldLocation(),
								layer.getId());
					}
					String layerName = (namedStyle.getSldLayerName() != null ? namedStyle.getSldLayerName() : layer
							.getId());
					String styleName = (namedStyle.getSldStyleName() != null ? namedStyle.getSldStyleName() : layer
							.getId());
					NamedStyleInfo sldStyle = styleConverterService.convert(namedStyle.getStyledLayerInfo(),
							info.getFeatureInfo(), layerName, styleName);
					namedStyle.setFeatureStyles(sldStyle.getFeatureStyles());
					namedStyle.setLabelStyle(sldStyle.getLabelStyle());
				}
			}
			// apply defaults to all styles
			for (NamedStyleInfo namedStyle : info.getNamedStyleInfos()) {
				for (FeatureStyleInfo featureStyle : namedStyle.getFeatureStyles()) {
					featureStyle.applyDefaults();
				}
				if (namedStyle.getLabelStyle().getLabelAttributeName() == null) {
					AttributeInfo attributeInfo = info.getFeatureInfo().getAttributes().get(0);
					namedStyle.getLabelStyle().setLabelAttributeName(attributeInfo.getName());
				}
				namedStyle.getLabelStyle().getBackgroundStyle().applyDefaults();
				namedStyle.getLabelStyle().getFontStyle().applyDefaults();
			}
		}
	}

	private ClientApplicationInfo postProcess(ClientApplicationInfo client) throws LayerException, BeansException {
		// initialize maps
		for (ClientMapInfo map : client.getMaps()) {
			map.setUnitLength(getUnitLength(map.getCrs(), map.getInitialBounds()));
			// result should be m = (m/inch) / (number/inch)
			map.setPixelLength(METER_PER_INCH / client.getScreenDpi());
			log.debug("Map " + map.getId() + " has unit length : " + map.getUnitLength() + "m, pixel length "
					+ map.getPixelLength() + "m");
			// calculate scales
			double pixPerUnit = map.getUnitLength() / map.getPixelLength();
			// if resolutions have been defined the old way, calculate the scale configuration
			if (map.getResolutions().size() > 0) {
				for (Double resolution : map.getResolutions()) {
					if (map.isResolutionsRelative()) {
						map.getScaleConfiguration().getZoomLevels().add(new ScaleInfo(1., resolution));
					} else {
						map.getScaleConfiguration().getZoomLevels().add(new ScaleInfo(1. / resolution));
					}
				}
				map.getResolutions().clear();
			}
			// convert the scales so we have both relative and pix/unit
			boolean relativeScales = true;
			for (ScaleInfo scale : map.getScaleConfiguration().getZoomLevels()) {
				if (scale.getDenominator() == 0) {
					relativeScales = false;
				} else if (!relativeScales) {
					throw new LayerException(ExceptionCode.SCALE_CONVERSION_PROBLEM, map.getId());
				}
				completeScale(scale, pixPerUnit);
				// add the resolution for deprecated api support
				if (!map.isResolutionsRelative()) {
					map.getResolutions().add(1. / scale.getPixelPerUnit());
				} else {
					map.getResolutions().add(scale.getDenominator() / scale.getNumerator());
				}
			}
			completeScale(map.getScaleConfiguration().getMaximumScale(), pixPerUnit);
			for (ClientLayerInfo layer : map.getLayers()) {
				String layerId = layer.getServerLayerId();
				Layer<?> serverLayer = layerMap.get(layerId);
				if (serverLayer == null) {
					throw new LayerException(ExceptionCode.LAYER_NOT_FOUND, layerId);
				}
				LayerInfo layerInfo = serverLayer.getLayerInfo();
				layer.setLayerInfo(layerInfo);
				layer.setMaxExtent(getClientMaxExtent(map.getCrs(), layer.getCrs(), layerInfo.getMaxExtent(), layerId));
				completeScale(layer.getMaximumScale(), pixPerUnit);
				completeScale(layer.getMinimumScale(), pixPerUnit);
				log.debug("Layer " + layer.getId() + " has scale range : " + layer.getMinimumScale().getPixelPerUnit()
						+ "," + layer.getMaximumScale().getPixelPerUnit());
				if (layer instanceof ClientVectorLayerInfo) {
					postProcess((ClientVectorLayerInfo) layer);
				}
			}
			checkLayerTree(map);
		}
		return client;
	}

	private ClientVectorLayerInfo postProcess(ClientVectorLayerInfo layer) throws LayerException {
		// copy feature info from server if not explicitly defined
		if (layer.getFeatureInfo() == null) {
			VectorLayerInfo serverInfo = (VectorLayerInfo) layer.getLayerInfo();
			layer.setFeatureInfo(serverInfo.getFeatureInfo());
		}

		return layer;
	}

	private NamedStyleInfo postProcess(NamedStyleInfo client) {
		// index styles
		int i = 0;
		for (FeatureStyleInfo style : client.getFeatureStyles()) {
			style.setIndex(i++);
			style.setStyleId(client.getName() + "-" + style.getIndex());
		}
		return client;
	}

	private double getUnitLength(String mapCrsKey, Bbox mapBounds) throws LayerException {
		try {
			if (null == mapBounds) {
				throw new LayerException(ExceptionCode.MAP_MAX_EXTENT_MISSING);
			}
			Crs crs = geoService.getCrs2(mapCrsKey);
			GeodeticCalculator calculator = new GeodeticCalculator(crs);
			Coordinate center = new Coordinate(0.5 * (mapBounds.getX() + mapBounds.getMaxX()),
					0.5 * (mapBounds.getY() + mapBounds.getMaxY()));
			calculator.setStartingPosition(new DirectPosition2D(crs, center.getX(), center.getY()));
			calculator.setDestinationPosition(new DirectPosition2D(crs, center.getX() + 1, center.getY()));
			return calculator.getOrthodromicDistance();
		} catch (TransformException e) {
			throw new LayerException(e, ExceptionCode.TRANSFORMER_CREATE_LAYER_TO_MAP_FAILED);
		}
	}

	public Bbox getClientMaxExtent(String mapCrsKey, String layerCrsKey, Bbox serverBbox, String layer)
			throws LayerException {
		if (mapCrsKey.equals(layerCrsKey)) {
			return serverBbox;
		}
		try {
			Crs mapCrs = geoService.getCrs2(mapCrsKey);
			Crs layerCrs = geoService.getCrs2(layerCrsKey);
			Envelope serverEnvelope = converterService.toInternal(serverBbox);
			CrsTransform transformer = geoService.getCrsTransform(layerCrs, mapCrs);
			Bbox res = converterService.toDto(geoService.transform(serverEnvelope, transformer));
			if (Double.isNaN(res.getX()) || Double.isNaN(res.getY()) || Double.isNaN(res.getWidth())
					|| Double.isNaN(res.getHeight())) {
				throw new LayerException(ExceptionCode.LAYER_EXTENT_CANNOT_CONVERT, layer, mapCrsKey);
			}
			return res;
		} catch (GeomajasException e) {
			throw new LayerException(e, ExceptionCode.TRANSFORMER_CREATE_LAYER_TO_MAP_FAILED);
		}
	}

	/**
	 * Convert the scale in pixels per unit or relative values, which ever is missing.
	 * 
	 * @param scaleInfo scaleInfo object which needs to be completed
	 * @param mapUnitInPixels the number of pixels in a map unit
	 */
	public void completeScale(ScaleInfo scaleInfo, double mapUnitInPixels) {
		if (0 == mapUnitInPixels) {
			throw new IllegalArgumentException("ScaleInfo.completeScale mapUnitInPixels should never be zero.");
		}
		double denominator = scaleInfo.getDenominator();
		double numerator = scaleInfo.getNumerator();
		if (denominator != 0) {
			scaleInfo.setPixelPerUnit(numerator / denominator * mapUnitInPixels);
		} else {
			double pixelPerUnit = scaleInfo.getPixelPerUnit();
			if (pixelPerUnit > mapUnitInPixels) {
				scaleInfo.setNumerator(pixelPerUnit / mapUnitInPixels);
				scaleInfo.setDenominator(1);
			} else {
				scaleInfo.setNumerator(1);
				scaleInfo.setDenominator(mapUnitInPixels / pixelPerUnit);
			}
		}
	}

	private void checkLayerTree(ClientMapInfo map) throws BeansException {
		// if the map contains a layer tree, verify that the layers are part of the map
		ClientLayerTreeInfo layerTree = map.getLayerTree();
		if (null != layerTree) {
			checkTreeNode(map, layerTree.getTreeNode());
		}
	}

	private void checkTreeNode(ClientMapInfo map, ClientLayerTreeNodeInfo node) throws BeansException {
		for (ClientLayerInfo layer : node.getLayers()) {
			if (!mapContains(map, layer)) {
				throw new BeanInitializationException(
						"A LayerTreeNodeInfo object can only reference layers which are part of the map, layer "
								+ layer.getId() + " is not part of map " + map.getId() + ".");
			}
		}
		for (ClientLayerTreeNodeInfo child : node.getTreeNodes()) {
			checkTreeNode(map, child);
		}
	}

	private boolean mapContains(ClientMapInfo map, ClientLayerInfo layer) {
		String id = layer.getId();
		boolean res = false;
		if (null != id) {
			for (ClientLayerInfo mapLayer : map.getLayers()) {
				res |= id.equals(mapLayer.getId());
			}
		}
		return res;
	}

}
