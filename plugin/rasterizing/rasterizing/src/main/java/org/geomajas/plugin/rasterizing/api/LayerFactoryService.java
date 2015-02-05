/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2015 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */
package org.geomajas.plugin.rasterizing.api;

import java.util.Map;

import org.geomajas.configuration.client.ClientLayerInfo;
import org.geomajas.global.GeomajasException;
import org.geotools.map.Layer;
import org.geotools.map.MapContext;

/**
 * Service that acts as an entry point for creating renderable layers.
 * 
 * @author Jan De Moerloose
 * @author Oliver May
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

	/**
	 * Retrieves the userdata for the specified metadata. Especially {@link LayerFactory.USERDATA_KEY_SHOWING} is set.
	 * 
	 * @param mapContext
	 *            the map context
	 * @param clientLayerInfo
	 *            the layer metadata
	 * @return the user data
	 * @throws GeomajasException
	 *             oops, something went wrong
	 * @since 1.1.0
	 */
	Map<String, Object> getLayerUserData(MapContext mapContext, ClientLayerInfo clientLayerInfo)
			throws GeomajasException;
}
