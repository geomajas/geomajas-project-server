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

import org.geomajas.configuration.client.ClientVectorLayerInfo;
import org.geomajas.puregwt.client.map.MapEventBus;
import org.geomajas.puregwt.client.map.ViewPort;
import org.geomajas.puregwt.client.service.EndPointService;

import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;

public class MockVectorLayerFactory implements VectorLayerFactory {

	@Inject
	EndPointService endPointService;

	public Layer<ClientVectorLayerInfo> create(ClientVectorLayerInfo clientVectorLayerInfo, ViewPort viewPort,MapEventBus eventBus) {
		return new VectorLayer(clientVectorLayerInfo, viewPort, eventBus, endPointService);
	}

}
