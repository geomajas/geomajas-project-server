/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2016 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */
package org.geomajas.plugin.printing.component.service;

import org.geomajas.configuration.RasterLayerInfo;
import org.geomajas.configuration.VectorLayerInfo;
import org.geomajas.configuration.client.ClientMapInfo;
import org.geomajas.service.ConfigurationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Trimmed down configuration service for printing.
 * 
 * @author Jan De Moerloose
 * @author Jan Venstermans
 */
@Component()
public class PrintConfigurationServiceImpl implements PrintConfigurationService {

	@Autowired
	private ConfigurationService configurationService;

	public ClientMapInfo getMapInfo(String mapId, String applicationId) {
		if (null != mapId && null != applicationId) {
			return configurationService.getMap(mapId, applicationId);
		}
		return null;
	}

	public RasterLayerInfo getRasterLayerInfo(String layerId) {
		return layerId == null ? null : configurationService.getRasterLayer(layerId).getLayerInfo();
	}

	public VectorLayerInfo getVectorLayerInfo(String layerId) {
		return layerId == null ? null : configurationService.getVectorLayer(layerId).getLayerInfo();
	}

}
