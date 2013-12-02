/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2013 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */
package org.geomajas.plugin.runtimeconfig.service.factory;

import java.util.Map;

import org.geomajas.configuration.client.ClientLayerInfo;
import org.geomajas.configuration.client.ClientVectorLayerInfo;
import org.geomajas.service.ConfigurationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Default implementation of {@link org.geomajas.plugin.runtimeconfig.service.BeanFactory} for
 * {@link ClientVectorLayerInfo} objects.
 * 
 * @author Jan De Moerloose
 * 
 */
@Component
public class ClientVectorLayerBeanFactory extends BaseClientLayerBeanFactory {

	@Autowired
	private ConfigurationService configurationService;

	protected ClientVectorLayerBeanFactory() {
		super(ClientVectorLayerInfo.class);
	}

	@Override
	protected void processExtraParameters(ClientLayerInfo clientLayerInfo, Map<String, Object> parameters) {
		ClientVectorLayerInfo layerInfo = (ClientVectorLayerInfo) clientLayerInfo;
		
		String serverLayerId = getString(SERVER_LAYER_ID, parameters);
		
		if (configurationService.getVectorLayer(serverLayerId) != null) {
			layerInfo.setNamedStyleInfo(configurationService.getVectorLayer(serverLayerId).getLayerInfo()
					.getNamedStyleInfos().get(0));
		}
	}

	@Override
	protected ClientLayerInfo createBean() {
		return new ClientVectorLayerInfo();
	}
}
