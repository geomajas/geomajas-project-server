/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2011 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */
package org.geomajas.plugin.rasterizing.api;

import org.geomajas.configuration.client.ClientLayerInfo;
import org.geomajas.global.GeomajasException;
import org.geotools.map.Layer;
import org.geotools.map.MapContext;

/**
 * Service that acts as an entry point for creating renderable layers.
 * 
 * @author Jan De Moerloose
 * @since 1.0.0
 * 
 */
public interface LayerFactoryService {

	/**
	 * Creates a layer for the specified metadata.
	 * 
	 * @param mapContext
	 *            the map context
	 * @param clientLayerInfo
	 *            the layer metadata
	 * @return layer ready for rendering
	 * @throws GeomajasException
	 *             oops, something went wrong
	 */
	Layer createLayer(MapContext mapContext, ClientLayerInfo clientLayerInfo) throws GeomajasException;
}
