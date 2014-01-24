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
package org.geomajas.plugin.runtimeconfig.service;

import org.geomajas.layer.Layer;
import org.springframework.core.io.Resource;

/**
 * Factory to create a layer from a resource.
 * 
 * @author Jan De Moerloose
 * 
 * @param <T> the type of layer
 */
public interface LayerFactory<T extends Layer<?>> {

	/**
	 * Create a layer from this resource.
	 * 
	 * @param resource the resource (could be shape file or image...)
	 * @return the layer
	 */
	T createLayer(Resource resource);
}
