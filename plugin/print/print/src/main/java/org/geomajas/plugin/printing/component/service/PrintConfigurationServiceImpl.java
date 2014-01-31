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
package org.geomajas.plugin.printing.component.service;

import java.util.LinkedHashMap;
import java.util.Map;

import org.geomajas.configuration.RasterLayerInfo;
import org.geomajas.configuration.VectorLayerInfo;
import org.geomajas.configuration.client.ClientApplicationInfo;
import org.geomajas.configuration.client.ClientMapInfo;
import org.geomajas.layer.RasterLayer;
import org.geomajas.layer.VectorLayer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Trimmed down configuration service for printing.
 * 
 * @author Jan De Moerloose
 */
@Component()
public class PrintConfigurationServiceImpl implements PrintConfigurationService {

	@Autowired(required = false)
	protected Map<String, ClientApplicationInfo> applicationMap = new LinkedHashMap<String, ClientApplicationInfo>();

	@Autowired(required = false)
	protected Map<String, VectorLayer> vectorLayerMap = new LinkedHashMap<String, VectorLayer>();

	@Autowired(required = false)
	protected Map<String, RasterLayer> rasterLayerMap = new LinkedHashMap<String, RasterLayer>();

	public ClientMapInfo getMapInfo(String mapId, String applicationId) {
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

	public RasterLayerInfo getRasterLayerInfo(String layerId) {
		return layerId == null ? null : rasterLayerMap.get(layerId).getLayerInfo();
	}

	public VectorLayerInfo getVectorLayerInfo(String layerId) {
		return layerId == null ? null : vectorLayerMap.get(layerId).getLayerInfo();
	}

}
