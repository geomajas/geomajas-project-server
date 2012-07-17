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
package org.geomajas.plugin.runtimeconfig.service.factory;

import java.util.Map;

import org.geomajas.configuration.RasterLayerInfo;

/**
 * Base implementation of {@link org.geomajas.plugin.runtimeconfig.service.BeanFactory;} for all
 * {@link org.geomajas.layer.RasterLayer} beans.
 * 
 * @author Jan De Moerloose
 * 
 */
public class BaseRasterLayerBeanFactory extends BaseBeanFactory {

	public static final String LAYER_INFO = "layerInfo";

	protected BaseRasterLayerBeanFactory(Class<?> clazz) {
		super(clazz);
	}

	protected BaseRasterLayerBeanFactory(String className) {
		super(className);
	}

	@Override
	public Priority getPriority(Map<String, Object> parameters) {
		Priority priority = super.getPriority(parameters);
		priority = priority.and(checkClass(LAYER_INFO, RasterLayerInfo.class, parameters));
		return priority;
	}

}
