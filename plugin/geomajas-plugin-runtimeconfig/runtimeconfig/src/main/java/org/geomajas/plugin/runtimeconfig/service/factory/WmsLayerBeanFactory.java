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

import org.geomajas.layer.wms.WmsLayer;
import org.springframework.stereotype.Component;


/**
 * Default implementation of {@link org.geomajas.plugin.runtimeconfig.service.BeanFactory} for {@link WmsLayer} objects.
 * 
 * @author Jan De Moerloose
 * 
 */
@Component
public class WmsLayerBeanFactory extends BaseRasterLayerBeanFactory {

	public static final String BASE_WMS_URL = "baseWmsUrl";

	public static final String WMS_USERNAME = "userName";

	public static final String WMS_PASSWORD = "password";

	public static final String PARAMETERS = "parameters";

	protected WmsLayerBeanFactory() {
		super(WmsLayer.class);
	}
	
	

}
