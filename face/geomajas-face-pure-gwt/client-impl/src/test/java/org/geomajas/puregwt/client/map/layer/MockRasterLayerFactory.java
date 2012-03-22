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
package org.geomajas.puregwt.client.map.layer;

import org.geomajas.configuration.client.ClientRasterLayerInfo;
import org.geomajas.puregwt.client.map.MapEventBus;
import org.geomajas.puregwt.client.map.ViewPort;
import org.geomajas.puregwt.client.service.EndPointService;

import com.google.inject.Inject;

/**
 * Mock implementation of a {@link RasterLayerFactory}.
 * 
 * @author Jan De Moerloose
 */
public class MockRasterLayerFactory implements RasterLayerFactory {

	@Inject
	EndPointService endPointService;

	public Layer<ClientRasterLayerInfo> create(ClientRasterLayerInfo clientRasterLayerInfo, ViewPort viewPort,
			MapEventBus eventBus) {
		return new RasterLayer(clientRasterLayerInfo, viewPort, eventBus, endPointService);
	}
}