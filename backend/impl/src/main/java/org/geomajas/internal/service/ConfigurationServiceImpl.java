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

package org.geomajas.internal.service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.geomajas.configuration.LayerExtraInfo;
import org.geomajas.configuration.LayerInfo;
import org.geomajas.configuration.client.ClientApplicationInfo;
import org.geomajas.configuration.client.ClientMapInfo;
import org.geomajas.global.GeomajasException;
import org.geomajas.layer.Layer;
import org.geomajas.layer.LayerException;
import org.geomajas.layer.RasterLayer;
import org.geomajas.layer.VectorLayer;
import org.geomajas.service.ConfigurationService;
import org.geomajas.service.GeoService;
import org.geomajas.service.LayerInvalidationService;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Container class which contains runtime information about the parameters and other information for Geomajas. Values
 * are injected using Spring.
 *
 * @author Joachim Van der Auwera
 */
@Component
public class ConfigurationServiceImpl implements ConfigurationService {

	private final Logger log = LoggerFactory.getLogger(ConfigurationServiceImpl.class);

	@Autowired(required = false)
	protected Map<String, Layer<?>> layerMap = new LinkedHashMap<String, Layer<?>>();

	@Autowired(required = false)
	protected Map<String, VectorLayer> vectorLayerMap = new LinkedHashMap<String, VectorLayer>();

	@Autowired(required = false)
	protected Map<String, RasterLayer> rasterLayerMap = new LinkedHashMap<String, RasterLayer>();

	@Autowired(required = false)
	protected Map<String, ClientApplicationInfo> applicationMap = new LinkedHashMap<String, ClientApplicationInfo>();

	@Autowired(required = false)
	private List<LayerInvalidationService> layerInvalidationServices;

	@Autowired
	private GeoService geoService;

	/** {@inheritDoc} */
	public VectorLayer getVectorLayer(String id) {
		return id == null ? null : vectorLayerMap.get(id);
	}

	/** {@inheritDoc} */
	public RasterLayer getRasterLayer(String id) {
		return id == null ? null : rasterLayerMap.get(id);
	}

	/** {@inheritDoc} */
	public Layer<?> getLayer(String id) {
		return id == null ? null : layerMap.get(id);
	}

	/** {@inheritDoc} */
	public ClientMapInfo getMap(String mapId, String applicationId) {
		if (null == mapId || null == applicationId) {
			return null;
		}
		ClientApplicationInfo application = applicationMap.get(applicationId);
		if (application != null) {
			for (ClientMapInfo map : application.getMaps()) {
				if (mapId.equals(map.getId())) {
					return map;
				}
			}
		}
		return null;
	}

	/** {@inheritDoc} */
	public CoordinateReferenceSystem getCrs(String crs) throws LayerException {
		return geoService.getCrs2(crs);
	}

	/** {@inheritDoc} */
	public void invalidateLayer(String layerId) throws GeomajasException {
		Layer layer = null;
		if (null != layerId) {
			layer = getLayer(layerId);
		}
		invalidateLayer(layer);
	}

	private void invalidateLayer(Layer layer) {
		if (null != layerInvalidationServices) {
			for (LayerInvalidationService service : layerInvalidationServices) {
				try {
					service.invalidateLayer(layer);
					if (null != layer) {
						service.invalidateLayer(null);
					}
				} catch (GeomajasException ge) {
					log.error("Error during invalidateLayer, not rethrown, " + ge.getMessage(), ge);
				}
			}
		}
	}

	/** {@inheritDoc} */
	public void invalidateAllLayers() throws GeomajasException {
		for (Map.Entry<String, Layer<?>> entry : layerMap.entrySet()) {
			invalidateLayer(entry.getValue());
		}
	}

	/** {@inheritDoc} */
	public <TYPE extends LayerExtraInfo> TYPE getLayerExtraInfo(LayerInfo layerInfo, Class<TYPE> type) {
		return getLayerExtraInfo(layerInfo, type.getName(), type);
	}

	/** {@inheritDoc} */
	public <TYPE extends LayerExtraInfo> TYPE getLayerExtraInfo(LayerInfo layerInfo, String key, Class<TYPE> type) {
		Object obj = layerInfo.getExtraInfo().get(key);
		if (null != obj && type.isAssignableFrom(obj.getClass())) {
			return (TYPE) obj;
		}
		return null;
	}

}
