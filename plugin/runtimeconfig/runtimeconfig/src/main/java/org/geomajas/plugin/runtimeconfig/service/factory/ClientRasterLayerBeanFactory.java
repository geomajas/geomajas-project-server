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
package org.geomajas.plugin.runtimeconfig.service.factory;

import java.util.Map;

import org.geomajas.configuration.client.ClientLayerInfo;
import org.geomajas.configuration.client.ClientRasterLayerInfo;
import org.springframework.stereotype.Component;

/**
 * Default implementation of {@link org.geomajas.plugin.runtimeconfig.service.BeanFactory} for
 * {@link ClientRasterLayerInfo} objects.
 * 
 * @author Jan De Moerloose
 * 
 */
@Component
public class ClientRasterLayerBeanFactory extends BaseClientLayerBeanFactory {

	protected ClientRasterLayerBeanFactory() {
		super(ClientRasterLayerInfo.class);
	}

	@Override
	protected void processExtraParameters(ClientLayerInfo clientLayerInfo, Map<String, Object> parameters) {
	}

	@Override
	protected ClientLayerInfo createBean() {
		return new ClientRasterLayerInfo();
	}

}
