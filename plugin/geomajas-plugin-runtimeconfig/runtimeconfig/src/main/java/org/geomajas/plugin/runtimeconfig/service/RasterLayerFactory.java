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
package org.geomajas.plugin.runtimeconfig.service;

import org.geomajas.layer.RasterLayer;
import org.springframework.core.io.Resource;

/**
 * Factory to create a raster layer from a resource.
 * 
 * @author Jan De Moerloose
 * 
 * @param <T> the type of raster layer
 */
public interface RasterLayerFactory<T extends RasterLayer> extends LayerFactory<T> {

	T createLayer(Resource resource);
}
