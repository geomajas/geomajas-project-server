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
package org.geomajas.internal.configuration;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.PostConstruct;

import org.geomajas.configuration.AbstractAttributeInfo;
import org.geomajas.configuration.AssociationAttributeInfo;
import org.geomajas.configuration.FeatureInfo;
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
import org.geomajas.layer.LayerType;
import org.geomajas.layer.RasterLayer;
import org.geomajas.layer.VectorLayer;
import org.geomajas.service.DtoConverterService;
import org.geomajas.service.GeoService;
import org.geomajas.service.StyleConverterService;
import org.geomajas.sld.NamedLayerInfo;
import org.geomajas.sld.StyledLayerDescriptorInfo;
import org.geomajas.sld.UserLayerInfo;
import org.geomajas.sld.UserStyleInfo;
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
			// for raster layers we don't accept x:y notation !
			if (scale.getDenominator() != 0) {
				throw new LayerException(ExceptionCode.CONVERSION_PROBLEM, "Raster layer " + layer.getId()
						+ " has zoom level " + scale.getNumerator() + ":" + scale.getDenominator()
						+ " in disallowed x:y notation");
			}
			// add the resolution for deprecated api support
			info.getResolutions().add(1. / scale.getPixelPerUnit());
		}
		if (0 == info.getTileWidth()) {
			throw new LayerException(ExceptionCode.LAYER_CONFIGURATION_PROBLEM, layer.getId(),
					"tileWidth should not be zero.");
		}
		if (0 == info.getTileHeight()) {
			throw new LayerException(ExceptionCode.LAYER_CONFIGURATION_PROBLEM, layer.getId(),
					"tileHeight should not be zero.");
		}
	}

	private void postProcess(VectorLayer layer) throws LayerException {
		VectorLayerInfo info = layer.getLayerInfo();
		if (info != null) {
			// log warning when allowing empty attributes
			if (info.isAllowEmptyGeometries()) {
				log.warn("Empty geometries are allowed for layer {}. This disables all security filtering on areas.",
						layer.getId());
			}

			FeatureInfo featureInfo = info.getFeatureInfo();
			postProcess(layer.getId(), featureInfo, "");

			// convert sld to old styles
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
						String layerName = (namedStyle.getSldLayerName() != null ? namedStyle.getSldLayerName() : layer
								.getId());
						String styleName = (namedStyle.getSldStyleName() != null ? namedStyle.getSldStyleName() : layer
								.getId());
						namedStyle.setUserStyle(extractStyle(sld, layerName, styleName));
					} catch (JiBXException e) {
						throw new LayerException(e, ExceptionCode.INVALID_SLD, namedStyle.getSldLocation(),
								layer.getId());
					} catch (IOException e) {
						throw new LayerException(e, ExceptionCode.INVALID_SLD, namedStyle.getSldLocation(),
								layer.getId());
					}
					NamedStyleInfo sldStyle = styleConverterService.convert(namedStyle.getUserStyle(),
							featureInfo);
					namedStyle.setFeatureStyles(sldStyle.getFeatureStyles());
					namedStyle.setLabelStyle(sldStyle.getLabelStyle());
				}
			}
			// check for at least 1 style
			if (info.getNamedStyleInfos().size() == 0) {
				info.getNamedStyleInfos().add(new NamedStyleInfo());
			}
			// apply defaults to all styles
			for (NamedStyleInfo namedStyle : info.getNamedStyleInfos()) {
				namedStyle.applyDefaults();
			}
			
			// check for mixed geometry styles (old school styles without layer type will be converted to 3 styles)
			for (NamedStyleInfo namedStyle : info.getNamedStyleInfos()) {
				List<FeatureStyleInfo> convertedStyles = new ArrayList<FeatureStyleInfo>();
				for (FeatureStyleInfo style : namedStyle.getFeatureStyles()) {
					if (style.getLayerType() == LayerType.RASTER || style.getLayerType() == LayerType.GEOMETRY) {
						throw new LayerException(ExceptionCode.INVALID_FEATURE_STYLE_LAYER_TYPE, style.getLayerType());
					} else if (style.getLayerType() == null) {
						if (layer.getLayerInfo().getLayerType() != LayerType.GEOMETRY) {
							style.setLayerType(layer.getLayerInfo().getLayerType());
							convertedStyles.add(style);
						} else {
							String geometryName = featureInfo.getGeometryType().getName();
							// we have to convert to 3 styles here !
							convertedStyles.add(createPointStyle(style, geometryName));
							convertedStyles.add(createLineStyle(style, geometryName));
							convertedStyles.add(createPolygonStyle(style, geometryName));
						}
					} else {
						convertedStyles.add(style);
					}
				}
				namedStyle.setFeatureStyles(convertedStyles);
			}
			
			// index styles
			for (NamedStyleInfo namedStyle : info.getNamedStyleInfos()) {
				int i = 0;
				for (FeatureStyleInfo style : namedStyle.getFeatureStyles()) {
					style.setIndex(i++);
					style.setStyleId(namedStyle.getName() + "-" + style.getIndex());
				}
			}
			
			// convert old styles to sld
			for (NamedStyleInfo namedStyle : info.getNamedStyleInfos()) {
				if (namedStyle.getUserStyle() == null) {
					UserStyleInfo userStyle = styleConverterService.convert(namedStyle, featureInfo
							.getGeometryType().getName());
					namedStyle.setUserStyle(userStyle);
				}
			}
		}
	}

	@SuppressWarnings("unchecked")
	private void postProcess(String layerId, FeatureInfo featureInfo, String path) throws LayerException {
		List<AbstractAttributeInfo> attributes = (List<AbstractAttributeInfo>) (List) featureInfo.getAttributes();


		// check for invalid attribute names and post-process association attributes' FeatureInfo
		for (AbstractAttributeInfo attributeInfo : attributes) {
			if (attributeInfo instanceof AssociationAttributeInfo) {
				postProcess(layerId, ((AssociationAttributeInfo) attributeInfo).getFeature(),
						path + "/" + attributeInfo.getName());
			}

			if (attributeInfo.getName().contains(".") || attributeInfo.getName().contains("/")) {
				throw new LayerException(ExceptionCode.INVALID_ATTRIBUTE_NAME, attributeInfo.getName(), layerId);
			}
		}

		// check for duplicate attribute names
		checkDuplicateAttributes(layerId, path, attributes);

		featureInfo.setAttributesMap(toMap(attributes));
	}

	private Map<String, AbstractAttributeInfo> toMap(List<AbstractAttributeInfo> attributes) {
		Map<String, AbstractAttributeInfo> map = new HashMap<String, AbstractAttributeInfo>();
		for (AbstractAttributeInfo attributeInfo : attributes) {
			map.put(attributeInfo.getName(), attributeInfo);
		}
		return map;
	}

	@SuppressWarnings("unchecked")
	private void checkDuplicateAttributes(String layerId, String path, List<AbstractAttributeInfo> attributes)
			throws LayerException {
		Set<String> names = new HashSet<String>();
		for (AbstractAttributeInfo attribute : attributes) {
			String name = attribute.getName();
			if (names.contains(name)) {
				throw new LayerException(ExceptionCode.DUPLICATE_ATTRIBUTE_NAME, name, layerId, path);
			}
			names.add(name);
		}
	}

	private FeatureStyleInfo createPointStyle(FeatureStyleInfo style, String geometryName) {
		FeatureStyleInfo copy = new FeatureStyleInfo(style);
		copy.setLayerType(LayerType.POINT);
		copy.setFormula("(geometryType(" + geometryName + ") = 'Point') OR (geometryType(" + geometryName
				+ ") = 'MultiPoint')");
		return copy;
	}

	private FeatureStyleInfo createLineStyle(FeatureStyleInfo style, String geometryName) {
		FeatureStyleInfo copy = new FeatureStyleInfo(style);
		copy.setLayerType(LayerType.LINESTRING);
		copy.setFormula("(geometryType(" + geometryName + ") = 'LineString') OR (geometryType(" + geometryName
				+ ") = 'MultiLineString')");
		return copy;
	}

	private FeatureStyleInfo createPolygonStyle(FeatureStyleInfo style, String geometryName) {
		FeatureStyleInfo copy = new FeatureStyleInfo(style);
		copy.setLayerType(LayerType.POLYGON);
		copy.setFormula("(geometryType(" + geometryName + ") = 'Polygon') OR (geometryType(" + geometryName
				+ ") = 'MultiPolygon')");
		return copy;
	}

	private NamedStyleInfo postProcess(NamedStyleInfo client) {
		// index styles/rules
		int i = 0;
		for (FeatureStyleInfo style : client.getFeatureStyles()) {
			style.setIndex(i++);
			style.setStyleId(client.getName() + "-" + style.getIndex());
		}
		return client;
	}


	private UserStyleInfo extractStyle(StyledLayerDescriptorInfo sld, String sldLayerName, String sldStyleName)
			throws LayerException {
		NamedLayerInfo namedLayerInfo = null;
		UserLayerInfo userLayerInfo = null;
		// find first named layer or find by name
		for (StyledLayerDescriptorInfo.ChoiceInfo choice : sld.getChoiceList()) {
			// we only support named layers, pick the right name or the first one
			if (choice.ifNamedLayer()) {
				if (null != sldLayerName && sldLayerName.equals(choice.getNamedLayer().getName())) {
					namedLayerInfo = choice.getNamedLayer();
					break;
				}
				if (null == namedLayerInfo) {
					namedLayerInfo = choice.getNamedLayer();
				}
			} else if (choice.ifUserLayer()) {
				if (sldLayerName != null && sldLayerName.equals(choice.getUserLayer().getName())) {
					userLayerInfo = choice.getUserLayer();
					break;
				}
				if (namedLayerInfo == null) {
					userLayerInfo = choice.getUserLayer();
				}
			}
		}
		if (null == namedLayerInfo && null == userLayerInfo) {
			throw new LayerException(ExceptionCode.INVALID_SLD, sld.getName(), sldLayerName);
		}

		UserStyleInfo userStyleInfo = null;
		if (namedLayerInfo != null) {
			for (NamedLayerInfo.ChoiceInfo choice : namedLayerInfo.getChoiceList()) {
				// we only support user styles, pick the right name or the first
				if (choice.ifUserStyle()) {
					if (null != sldStyleName  && sldStyleName.equals(choice.getUserStyle().getName())) {
						userStyleInfo = choice.getUserStyle();
						break;
					}
					if (userStyleInfo == null) {
						userStyleInfo = choice.getUserStyle();
					}
				}
			}
		} else {
			for (UserStyleInfo userStyle : userLayerInfo.getUserStyleList()) {
				if (null != sldStyleName  && sldStyleName.equals(userStyle.getName())) {
					userStyleInfo = userStyle;
					break;
				}
				if (userStyleInfo == null) {
					userStyleInfo = userStyle;
				}
			}
		}
		if (userStyleInfo == null) {
			throw new LayerException(ExceptionCode.INVALID_SLD, sld.getName(), sldLayerName);
		} else {
			return userStyleInfo;
		}

	}

	private ClientApplicationInfo postProcess(ClientApplicationInfo client) throws LayerException, BeansException {
		// initialize maps
		for (ClientMapInfo map : client.getMaps()) {
			map.setUnitLength(getUnitLength(map.getCrs(), map.getInitialBounds()));
			// result should be m = (m/inch) / (number/inch)
			map.setPixelLength(METER_PER_INCH / client.getScreenDpi());
			log.debug("Map {} has unit length : {}m, pixel length {}m",
					new Object[] {map.getId(), map.getUnitLength(), map.getPixelLength()});
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
				completeScale(layer.getZoomToPointScale(), pixPerUnit);
				log.debug("Layer {} has scale range : {}, {}", new Object[] {layer.getId(),
						layer.getMinimumScale().getPixelPerUnit(), layer.getMaximumScale().getPixelPerUnit()});
				log.debug("Layer {} has zoom-to-point scale : {}", layer.getId(),
						layer.getZoomToPointScale().getPixelPerUnit());
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
