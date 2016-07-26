/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2016 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */

package org.geomajas.plugin.rasterizing.api;

import java.util.Map;

import org.geomajas.annotation.Api;
import org.geomajas.annotation.ExpectAlternatives;
import org.geomajas.annotation.UserImplemented;
import org.geomajas.configuration.client.ClientLayerInfo;
import org.geomajas.global.GeomajasException;
import org.geotools.map.Layer;
import org.geotools.map.MapContext;

/**
 * Factory that creates a renderable layer based on the metadata supplied. Typically, each layer class will have its own
 * factory. Use the {@link LayerFactoryService} component as an entry point for iterating over all configured factories.
 * 
 * @author Jan De Moerloose
 * @since 1.0.0
 */
@Api(allMethods = true)
@UserImplemented
@ExpectAlternatives
// @extract-start LayerFactory, Layer factory interface definition
public interface LayerFactory {

	/**
	 * user data to record the layer id (up to caller to decide what to do with this info).
	 */
	String USERDATA_KEY_LAYER_ID = "geomajas.rasterizing.layer"; // String

	/**
	 * user data to record if layer is showing (up to caller to decide what to do with this info).
	 */
	String USERDATA_KEY_SHOWING = "geomajas.rasterizing.showing"; // boolean

	/**
	 * user data for the layer styles (DTOs, should eventually become unnecessary as all info is in SLD).
	 */
	String USERDATA_KEY_STYLE_RULES = "geomajas.rasterizing.style.rules"; // List<RuleInfo>

	/**
	 * user data for the map (up to caller to decide what to do with this info).
	 */
	String USERDATA_RASTERIZING_INFO = "geomajas.rasterizing.info"; // MapRasterizingInfo

	/**
	 * Returns true if this factory is capable of creating layer instances for the specified metadata.
	 * 
	 * @param mapContext the map context
	 * @param clientLayerInfo the client layer metadata
	 * @return true if we can create layer instances
	 */
	boolean canCreateLayer(MapContext mapContext, ClientLayerInfo clientLayerInfo);

	/**
	 * Creates a layer for the specified metadata.
	 * 
	 * @param mapContext the map context
	 * @param clientLayerInfo the client layer metadata
	 * @return layer ready for rendering
	 * @throws GeomajasException something went wrong
	 */
	Layer createLayer(MapContext mapContext, ClientLayerInfo clientLayerInfo) throws GeomajasException;

	/**
	 * Retrieves the userdata for the specified metadata. Especially {@link LayerFactory.USERDATA_KEY_SHOWING} is set.
	 * 
	 * @param mapContext the map context
	 * @param clientLayerInfo the client layer metadata
	 * @return the user data key values
	 * @since 1.1.0
	 */
	Map<String, Object> getLayerUserData(MapContext mapContext, ClientLayerInfo clientLayerInfo);
}
// @extract-end