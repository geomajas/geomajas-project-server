/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2014 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */
package org.geomajas.plugin.deskmanager.service.manager;

import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.MultiLineString;
import com.vividsolutions.jts.geom.MultiPoint;
import com.vividsolutions.jts.geom.MultiPolygon;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.Polygon;
import org.geomajas.configuration.FeatureInfo;
import org.geomajas.configuration.GeometryAttributeInfo;
import org.geomajas.configuration.NamedStyleInfo;
import org.geomajas.configuration.Parameter;
import org.geomajas.configuration.PrimitiveAttributeInfo;
import org.geomajas.configuration.PrimitiveType;
import org.geomajas.configuration.RasterLayerInfo;
import org.geomajas.configuration.VectorLayerInfo;
import org.geomajas.configuration.client.ClientApplicationInfo;
import org.geomajas.configuration.client.ClientLayerInfo;
import org.geomajas.configuration.client.ClientRasterLayerInfo;
import org.geomajas.configuration.client.ClientVectorLayerInfo;
import org.geomajas.configuration.client.ScaleInfo;
import org.geomajas.geometry.Bbox;
import org.geomajas.geometry.Crs;
import org.geomajas.layer.Layer;
import org.geomajas.layer.LayerException;
import org.geomajas.layer.LayerType;
import org.geomajas.layer.common.proxy.LayerAuthentication;
import org.geomajas.layer.geotools.GeoToolsLayer;
import org.geomajas.layer.wms.WmsLayer;
import org.geomajas.plugin.deskmanager.DeskmanagerException;
import org.geomajas.plugin.deskmanager.command.manager.dto.DynamicRasterLayerConfiguration;
import org.geomajas.plugin.deskmanager.command.manager.dto.DynamicVectorLayerConfiguration;
import org.geomajas.plugin.deskmanager.command.manager.dto.GetWmsCapabilitiesRequest;
import org.geomajas.plugin.deskmanager.command.manager.dto.RasterCapabilitiesInfo;
import org.geomajas.plugin.deskmanager.command.manager.dto.VectorCapabilitiesInfo;
import org.geomajas.plugin.deskmanager.configuration.client.DeskmanagerClientLayerInfo;
import org.geomajas.plugin.deskmanager.domain.dto.DynamicLayerConfiguration;
import org.geomajas.plugin.runtimeconfig.service.BeanFactory;
import org.geomajas.plugin.runtimeconfig.service.factory.BaseRasterLayerBeanFactory;
import org.geomajas.plugin.runtimeconfig.service.factory.BaseVectorLayerBeanFactory;
import org.geomajas.plugin.runtimeconfig.service.factory.GeoToolsLayerBeanFactory;
import org.geomajas.plugin.runtimeconfig.service.factory.WmsLayerBeanFactory;
import org.geomajas.service.GeoService;
import org.geotools.data.DataStore;
import org.geotools.data.DataStoreFinder;
import org.geotools.data.ows.CRSEnvelope;
import org.geotools.data.ows.SimpleHttpClient;
import org.geotools.data.ows.WMSCapabilities;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.data.wms.WMSUtils;
import org.geotools.data.wms.WebMapServer;
import org.geotools.data.wms.request.GetMapRequest;
import org.geotools.feature.type.GeometryDescriptorImpl;
import org.geotools.geometry.GeneralEnvelope;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.jdbc.JDBCDataStoreFactory;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.AttributeDescriptor;
import org.opengis.feature.type.AttributeType;
import org.opengis.feature.type.GeometryDescriptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

/**
 * @author Kristof Heirwegh
 */
@SuppressWarnings("deprecation")
@Component
public class DiscoveryServiceImpl implements DiscoveryService {

	private final Logger log = LoggerFactory.getLogger(DiscoveryServiceImpl.class);

	private static final long serialVersionUID = 1L;

	@Resource(name = "discoveryDefaultMinimumScale")
	private ScaleInfo minScale;

	@Resource(name = "discoveryDefaultMaximumScale")
	private ScaleInfo maxScale;

	@Resource(name = "discoveryDefaultPointStyle")
	private NamedStyleInfo pointStyle;

	@Resource(name = "discoveryDefaultLineStyle")
	private NamedStyleInfo lineStyle;

	@Resource(name = "discoveryDefaultPolygonStyle")
	private NamedStyleInfo polygonStyle;

	@Autowired
	private GeoService geoService;

	@Autowired
	private CloneService cloneService;

	@Resource(name = "dataSource")
	private DataSource dataSource;

	@Autowired(required = false)
	@Qualifier("dataSourceDbType")
	private String dataSourceDbType = "postgis";

	@Autowired(required = false)
	@Qualifier("dataSourceNamespace")
	private String dataSourceNamespace = "postgis";

	@Resource(name = "dynamicLayersApplication")
	private ClientApplicationInfo defaultGeodesk;

	@Autowired(required = false)
	private Map<String, ClientLayerInfo> clientLayerInfoMap = new LinkedHashMap<String, ClientLayerInfo>();

	@Autowired(required = false)
	private Map<String, Layer<?>> serverLayerMap = new LinkedHashMap<String, Layer<?>>();

	@Override
	public List<VectorCapabilitiesInfo> getVectorCapabilities(Map<String, String> connectionProperties)
			throws DeskmanagerException {
		DataStore store = null;
		try {
			store = DataStoreFinder.getDataStore(connectionProperties);
			if (store != null) {
				List<VectorCapabilitiesInfo> res = new ArrayList<VectorCapabilitiesInfo>();
				for (String typeName : store.getTypeNames()) {
					VectorCapabilitiesInfo vci = new VectorCapabilitiesInfo();
					vci.setTypeName(typeName);
					String[] tt = typeName.split(":", 2);
					if (tt.length == 2) {
						vci.setNamespace(tt[0]);
						vci.setName(tt[1]);
					} else {
						vci.setName(typeName);
					}

					try {
						SimpleFeatureType sft = store.getSchema(typeName);
						vci.setDescription((sft.getDescription() != null ? sft.getDescription().toString() : ""));
						vci.setCrs(sft.getCoordinateReferenceSystem().getIdentifiers().iterator().next().toString());
						vci.setGeometryType((sft.getGeometryDescriptor() != null ? sft.getGeometryDescriptor()
								.getType().getBinding().getSimpleName() : "?"));
					} catch (Exception e) {
						vci.setDescription("[Failed retrieving metadata!]");
						log.debug("Failed retrieving metadata: " + e.getMessage());
					}
					res.add(vci);
				}
				return res;
			}
		} catch (Exception e) {
			throw new DeskmanagerException(DeskmanagerException.NO_CONNECTION_TO_CAPABILITIES_SERVER, e.getMessage());
		}

		throw new DeskmanagerException(DeskmanagerException.NO_CONNECTION_TO_CAPABILITIES_SERVER, "Not found");
	}

	@Override
	public DynamicVectorLayerConfiguration getVectorLayerConfiguration(Map<String, String> connectionProperties,
			String layerName) throws DeskmanagerException {
		Map<String, Object> connectionPropertiesObjects = new HashMap<String, Object>();
		connectionPropertiesObjects.putAll(connectionProperties);
		return getVectorLayerConfigurationInternal(connectionPropertiesObjects, layerName);
	}

	@Override
	public DynamicVectorLayerConfiguration getShapeFileInDatabaseConfiguration(Map<String, String> connectionProperties,
			String layerName) throws IOException, DeskmanagerException {
		Map<String, Object> connectionPropertiesObjects = new HashMap<String, Object>();
		connectionPropertiesObjects.putAll(connectionProperties);
		if (dataSource != null) {
			connectionPropertiesObjects.put(JDBCDataStoreFactory.DATASOURCE.key, dataSource);
		}
		return getVectorLayerConfigurationInternal(connectionPropertiesObjects, layerName);
	}

	private DynamicVectorLayerConfiguration getVectorLayerConfigurationInternal(
			Map<String, Object> connectionProperties, String layerName) throws DeskmanagerException {
		DataStore store = null;
		try {
			store = DataStoreFinder.getDataStore(connectionProperties);
		} catch (Exception e) {
			throw new DeskmanagerException(DeskmanagerException.NO_CONNECTION_TO_CAPABILITIES_SERVER, e.getMessage());
		}
		if (store == null) {
			throw new DeskmanagerException(DeskmanagerException.NO_CONNECTION_TO_CAPABILITIES_SERVER, "Not found");
		}

		try {
			Bbox maxExtent = null;
			SimpleFeatureSource fs = store.getFeatureSource(layerName);
			if (fs != null) {
				ReferencedEnvelope env = fs.getBounds();
				if (env != null) {
					maxExtent = new Bbox(env.getMinX(), env.getMinY(), env.getWidth(), env.getHeight());
				}
			}

			SimpleFeatureType sft = store.getSchema(layerName);
			if (sft != null) {
				String clientLayerName = UUID.randomUUID().toString();
				String serverLayerName = getServerLayerName(clientLayerName);
				DynamicVectorLayerConfiguration vlc = new DynamicVectorLayerConfiguration();

				// -- Featureinfo --
				FeatureInfo fi = new FeatureInfo();
				PrimitiveAttributeInfo identifier = null;
				for (AttributeDescriptor ad : sft.getAttributeDescriptors()) {
					if (!(ad instanceof GeometryDescriptorImpl) // geometry atrributes are special
							&& !ad.getLocalName().contains(".") // ignore attributes with points in name
							&& !ad.getLocalName().contains("/")) { // ignore attributes with slash in name
						PrimitiveAttributeInfo pai = toPrimitiveAttributeInfo(ad);
						fi.getAttributes().add(pai);
						if (ad.getType().isIdentified() && identifier == null) {
							identifier = pai;
						}
					}
				}
				if (identifier == null) {
					identifier = (PrimitiveAttributeInfo) fi.getAttributes().get(0);
				}
				fi.setDataSourceName(layerName);
				fi.setGeometryType(toGeometryAttributeInfo(sft.getGeometryDescriptor()));
				fi.setIdentifier(identifier);

				// -- serverlayerinfo --
				VectorLayerInfo vli = new VectorLayerInfo();
				vli.setFeatureInfo(fi);
				vli.setMaxExtent(maxExtent);
				vli.setCrs(sft.getCoordinateReferenceSystem().getIdentifiers().iterator().next().toString());
				vli.setLayerType(toLayerType(sft));
				vli.getNamedStyleInfos().add(
						getDefaultStyleInfo(vli.getLayerType(), clientLayerName, identifier.getName()));

				// -- parameters --
				List<Parameter> params = toParameters(connectionProperties);

				// -- clientlayer --
				ClientVectorLayerInfo cvli = new ClientVectorLayerInfo();
				cvli.setId(clientLayerName);
				cvli.setServerLayerId(serverLayerName);
				cvli.setFeatureInfo(fi);
				cvli.setUserData(new DeskmanagerClientLayerInfo());
				cvli.setNamedStyleInfo(vli.getNamedStyleInfos().get(0));
				cvli.setMaxExtent(maxExtent);
				cvli.setMaximumScale(maxScale);
				cvli.setMinimumScale(minScale);
				cvli.setCreatable(false);
				cvli.setDeletable(false);
				cvli.setUpdatable(false);
				cvli.setVisible(true);
				cvli.setLabel(getTypeLabel(sft.getTypeName()));

				// -- config --
				vlc.setClientVectorLayerInfo(cvli);
				vlc.setVectorLayerInfo(vli);
				vlc.setParameters(params);

				return vlc;
			}
		} catch (Exception e) {
			throw new DeskmanagerException(DeskmanagerException.NO_CONNECTION_TO_CAPABILITIES_SERVER, e.getMessage());
		}
		throw new DeskmanagerException(DeskmanagerException.LAYER_NOT_FOUND, layerName);
	}

	@Override
	public List<RasterCapabilitiesInfo> getRasterCapabilities(Map<String, String> connectionProperties)
			throws Exception {
		URL url = new URL(connectionProperties.get(GetWmsCapabilitiesRequest.GET_CAPABILITIES_URL));
		SimpleHttpClient httpClient = new SimpleHttpClient();
		if (connectionProperties.containsKey(WmsLayerBeanFactory.WMS_USERNAME)) {
			httpClient.setUser(connectionProperties.get(WmsLayerBeanFactory.WMS_USERNAME));
			httpClient.setPassword(connectionProperties.get(WmsLayerBeanFactory.WMS_PASSWORD));
		}
		WebMapServer wms = new WebMapServer(url, httpClient);
		WMSCapabilities capabilities = wms.getCapabilities();
		List<RasterCapabilitiesInfo> layers = new ArrayList<RasterCapabilitiesInfo>();

		List<RasterCapabilitiesInfo> nonNativeLayers = new ArrayList<RasterCapabilitiesInfo>();

		for (org.geotools.data.ows.Layer owsLayer : WMSUtils.getNamedLayers(capabilities)) {
			if (owsLayer.getSrs().contains(defaultGeodesk.getMaps().get(0).getCrs())) { // Only add default crs
				layers.add(buildRasterInfo(wms, owsLayer, defaultGeodesk.getMaps().get(0).getCrs()));
			} else { // Add all available crs-es
				for (String srs : owsLayer.getSrs()) {
					try {
						nonNativeLayers.add(buildRasterInfo(wms, owsLayer, srs));
					} catch (LayerException e) {
						log.warn("Got unknown crs from wms server, ignoring: {}", srs);
					}
				}
			}
		}
		layers.addAll(nonNativeLayers);
		return layers;
	}

	private RasterCapabilitiesInfo buildRasterInfo(WebMapServer wms, org.geotools.data.ows.Layer owsLayer, String srs)
			throws LayerException {
		RasterCapabilitiesInfo info = new RasterCapabilitiesInfo();
		Crs crs = geoService.getCrs2(srs);
		info.setCrs(geoService.getCodeFromCrs(crs));
		info.setName(owsLayer.getName());
		info.setExtent(toBbox(owsLayer.getEnvelope(crs)));
		info.setDescription(owsLayer.getTitle());
		info.setGetFeatureInfoFormats(wms.getCapabilities().getRequest().getGetFeatureInfo().getFormats());
		// create a sample request
		GetMapRequest request = wms.createGetMapRequest();
		request.setFormat("image/png");
		request.setTransparent(true);
		request.setSRS(info.getCrs());
		request.setBBox(new CRSEnvelope(owsLayer.getEnvelope(crs)));
		request.addLayer(owsLayer);
		info.setPreviewUrl(request.getFinalURL().toExternalForm());
		GetMapRequest baseRequest = wms.createGetMapRequest();
		info.setBaseUrl(baseRequest.getFinalURL().toExternalForm().replaceFirst("\\?.*", ""));
		return info;
	}

	private Bbox toBbox(GeneralEnvelope envelope) {
		return new Bbox(envelope.getMinimum(0), envelope.getMinimum(1), envelope.getSpan(0), envelope.getSpan(1));
	}

	@Override
	public DynamicRasterLayerConfiguration getRasterLayerConfiguration(Map<String, String> connectionProperties,
			RasterCapabilitiesInfo rasterCapabilitiesInfo) throws IOException, DeskmanagerException {
		try {
			DynamicRasterLayerConfiguration rlc = new DynamicRasterLayerConfiguration();
			String clientLayerName = UUID.randomUUID().toString();
			String serverLayerName = getServerLayerName(clientLayerName);
			// server layer info
			RasterLayerInfo rasterLayerInfo = new RasterLayerInfo();
			rasterLayerInfo.setCrs(rasterCapabilitiesInfo.getCrs());
			rasterLayerInfo.setDataSourceName(rasterCapabilitiesInfo.getName());
			rasterLayerInfo.setMaxExtent(rasterCapabilitiesInfo.getExtent());
			rasterLayerInfo.setTileWidth(512);
			rasterLayerInfo.setTileHeight(512);
			rlc.setRasterLayerInfo(rasterLayerInfo);
			// client layer info
			ClientRasterLayerInfo clientLayerInfo = new ClientRasterLayerInfo();
			clientLayerInfo.setId(clientLayerName);
			clientLayerInfo.setServerLayerId(serverLayerName);
			clientLayerInfo.setUserData(new DeskmanagerClientLayerInfo());
			clientLayerInfo.setMaxExtent(rasterCapabilitiesInfo.getExtent());
			clientLayerInfo.setMaximumScale(maxScale);
			clientLayerInfo.setMinimumScale(minScale);
			clientLayerInfo.setVisible(true);
			clientLayerInfo.setLabel(rasterCapabilitiesInfo.getName());
			rlc.setClientRasterLayerInfo(clientLayerInfo);
			// -- parameters --
			List<Parameter> params = toParameters(connectionProperties);
			params.add(new Parameter(WmsLayerBeanFactory.BASE_WMS_URL, rasterCapabilitiesInfo.getBaseUrl()));
			rlc.setParameters(params);
			return rlc;
		} catch (Exception e) {
			throw new DeskmanagerException(DeskmanagerException.ERROR_CONSTRUCTING_RASTER_LAYER, e.getMessage());
		}
	}

	@Override
	public Map<String, Object> createBeanLayerDefinitionParameters(DynamicLayerConfiguration lc) throws Exception {
		if (clientLayerInfoMap.containsKey(lc.getClientLayerInfo().getId())) {
			throw new DeskmanagerException(DeskmanagerException.CLIENT_LAYERID_ALREADY_IN_USE, lc.getClientLayerInfo()
					.getId());
		}
		if (serverLayerMap.containsKey(lc.getClientLayerInfo().getServerLayerId())) {
			throw new DeskmanagerException(DeskmanagerException.SERVER_LAYERID_ALREADY_IN_USE, lc.getClientLayerInfo()
					.getServerLayerId());
		}

		Map<String, Object> params = new HashMap<String, Object>();
		params.put(BeanFactory.BEAN_NAME, lc.getClientLayerInfo().getServerLayerId());

		if (lc instanceof DynamicVectorLayerConfiguration) {
			createVectorLayerParams((DynamicVectorLayerConfiguration) lc, params);
		} else if (lc instanceof DynamicRasterLayerConfiguration) {
			createRasterLayerParams((DynamicRasterLayerConfiguration) lc, params);
		} else {
			return null;
		}

		return params;
	}

	private void createVectorLayerParams(DynamicVectorLayerConfiguration vlc, Map<String, Object> params)
			throws Exception {
		params.put(BeanFactory.CLASS_NAME, GeoToolsLayer.class.getName());
		params.put(BaseVectorLayerBeanFactory.LAYER_INFO, vlc.getVectorLayerInfo());
		params.put(GeoToolsLayerBeanFactory.PARAMETERS, vlc.getParameters());

		if (DynamicLayerConfiguration.SOURCE_TYPE_SHAPE.equals(vlc.getParameterValue(DynamicLayerConfiguration
				.PARAM_SOURCE_TYPE))) {
			params.put("dataSource", dataSource);
		}
	}

	private void createRasterLayerParams(DynamicRasterLayerConfiguration rlc, Map<String, Object> params)
			throws Exception {
		// TODO: this mixes WMS and generic stuff: split this up for WMS/TMS/OSM or make generic

		//Set layer properties for reflection
		params.put(BeanFactory.CLASS_NAME, WmsLayer.class.getName());
		params.put(BaseRasterLayerBeanFactory.LAYER_INFO, rlc.getRasterLayerInfo());
		params.put(WmsLayerBeanFactory.BASE_WMS_URL, rlc.getParameterValue(WmsLayerBeanFactory.BASE_WMS_URL));
		params.put(WmsLayerBeanFactory.ENABLE_FEATURE_INFO, Boolean.parseBoolean(rlc.getParameterValue(
				WmsLayerBeanFactory.ENABLE_FEATURE_INFO)));

		params.put(WmsLayerBeanFactory.FEATURE_INFO_FORMAT, WmsLayer.WmsFeatureInfoFormat.parseFormat((String) rlc
				.getParameterValue(WmsLayerBeanFactory.FEATURE_INFO_FORMAT)));
		//TODO: all these parameters should be configurable too
		params.put("useProxy", true);
		params.put("useCache", true);
		List<Parameter> parameters = new ArrayList<Parameter>();
		parameters.add(new Parameter("TRANSPARENT", "true"));
		params.put("parameters", parameters);
		if (rlc.getParameterValue(WmsLayerBeanFactory.WMS_USERNAME) != null) {
			LayerAuthentication auth = new LayerAuthentication();
			auth.setUser(rlc.getParameterValue(WmsLayerBeanFactory.WMS_USERNAME));
			auth.setPassword(rlc.getParameterValue(WmsLayerBeanFactory.WMS_PASSWORD));
			params.put("layerAuthentication", auth);
		}
	}

	// -------------------------------------------------
	private Map<String, Object> createBeanClientLayerDefinitionParameters(DynamicLayerConfiguration lc)
		throws Exception {
		if (clientLayerInfoMap.containsKey(lc.getClientLayerInfo().getId())) {
			throw new DeskmanagerException(DeskmanagerException.CLIENT_LAYERID_ALREADY_IN_USE, lc.getClientLayerInfo()
					.getId());
		}
		if (serverLayerMap.containsKey(lc.getClientLayerInfo().getServerLayerId())) {
			throw new DeskmanagerException(DeskmanagerException.SERVER_LAYERID_ALREADY_IN_USE, lc.getClientLayerInfo()
					.getServerLayerId());
		}

		Map<String, Object> params = new HashMap<String, Object>();
		params.put(BeanFactory.BEAN_NAME, lc.getClientLayerInfo().getServerLayerId());

		if (lc instanceof DynamicVectorLayerConfiguration) {
			createVectorLayerParams((DynamicVectorLayerConfiguration) lc, params);
		} else if (lc instanceof DynamicRasterLayerConfiguration) {
			createRasterLayerParams((DynamicRasterLayerConfiguration) lc, params);
		} else {
			return null;
		}

		return params;
	}

	// -------------------------------------------------

	private String getTypeLabel(String name) {
		String[] tt = name.split(":", 2);
		if (tt.length == 2) {
			return tt[1];
		} else {
			return name;
		}
	}

	private List<Parameter> toParameters(Map<String, ?> props) {
		List<Parameter> params = new ArrayList<Parameter>();
		for (Entry<String, ?> entry : props.entrySet()) {
			Parameter p = new Parameter();
			p.setName(entry.getKey());
			p.setValue(entry.getValue().toString());
			params.add(p);
		}
		return params;
	}

	private NamedStyleInfo getDefaultStyleInfo(LayerType type, String clientLayerName, String labelAttributeName) {
		NamedStyleInfo nsi = null;
		switch (type) {
			case POINT:
			case MULTIPOINT:
				nsi = pointStyle;
				break;
			case LINESTRING:
			case MULTILINESTRING:
				nsi = lineStyle;
				break;
			default:
				nsi = polygonStyle;
				break;
		}
		nsi = cloneService.clone(nsi);
		if (nsi.getLabelStyle() == null || nsi.getFeatureStyles().size() < 1) {
			log.warn("NamedStyleInfo is invalid (LabelStyle should not be null "
					+ "& there should be at least one FeatureStyle.)");
		} else {
			nsi.getLabelStyle().setLabelAttributeName(labelAttributeName);
			nsi.getFeatureStyles().get(0).setName(clientLayerName + "_FeatureStyle_0");
			nsi.setName(clientLayerName + "_Style");
		}
		return nsi;
	}

	private LayerType toLayerType(SimpleFeatureType sft) {
		if (sft.getGeometryDescriptor() != null) {
			Class<?> classType = sft.getGeometryDescriptor().getType().getBinding();
			if (Point.class.equals(classType)) {
				return LayerType.POINT;
			}
			if (LineString.class.equals(classType)) {
				return LayerType.LINESTRING;
			}
			if (Polygon.class.equals(classType)) {
				return LayerType.POLYGON;
			}
			if (MultiPoint.class.equals(classType)) {
				return LayerType.MULTIPOINT;
			}
			if (MultiLineString.class.equals(classType)) {
				return LayerType.MULTILINESTRING;
			}
			if (MultiPolygon.class.equals(classType)) {
				return LayerType.MULTIPOLYGON;
			} else {
				return LayerType.GEOMETRY;
			}
		} else {
			return null;
		}
	}

	private PrimitiveAttributeInfo toPrimitiveAttributeInfo(AttributeDescriptor ad) {
		PrimitiveAttributeInfo info = new PrimitiveAttributeInfo();
		info.setName(ad.getLocalName());
		info.setEditable(false);
		info.setHidden(false);
		info.setIdentifying(true);
		info.setLabel(info.getName());
		info.setType(toPrimitiveType(ad.getType()));
		return info;
	}

	private GeometryAttributeInfo toGeometryAttributeInfo(GeometryDescriptor gdi) {
		if (gdi != null) {
			GeometryAttributeInfo gai = new GeometryAttributeInfo();
			gai.setEditable(false);
			gai.setName(gdi.getLocalName());
			return gai;
		} else {
			return null;
		}
	}

	private PrimitiveType toPrimitiveType(AttributeType type) {
		Class<?> classType = type.getBinding();
		if (String.class.equals(classType)) {
			return PrimitiveType.STRING;
		} else if (Integer.class.equals(classType)) {
			return PrimitiveType.INTEGER;
		} else if (Long.class.equals(classType)) {
			return PrimitiveType.LONG;
		} else if (Short.class.equals(classType)) {
			return PrimitiveType.SHORT;
		} else if (Boolean.class.equals(classType)) {
			return PrimitiveType.BOOLEAN;
		} else if (Float.class.equals(classType)) {
			return PrimitiveType.FLOAT;
		} else if (Double.class.equals(classType)) {
			return PrimitiveType.DOUBLE;
		} else if (Timestamp.class.equals(classType) || Date.class.equals(classType)
				|| java.util.Date.class.equals(classType) || Calendar.class.equals(classType)) {
			return PrimitiveType.DATE;
		} else {
			log.warn("Unrecognized attribute type: " + classType.getName());
			return PrimitiveType.STRING;
		}
	}

	private String getServerLayerName(String clientLayerName) throws Exception {
		return clientLayerName + "_server";
	}
}
