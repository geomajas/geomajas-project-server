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
package org.geomajas.layer.common.proxy;

import org.geomajas.annotation.Api;
import org.geomajas.layer.RasterLayer;

/**
 * Adds support for server-side proxying of any kind of layer. Currently implemented by WMS and TMS layers.
 * 
 * @author Jan De Moerloose
 * @since 1.16.0
 * 
 */
@Api(allMethods = true)
public interface ProxyLayerSupport extends RasterLayer {

	/**
	 * Get the authentication info for this layer.
	 * 
	 * @return authentication info.
	 */
	ProxyAuthentication getProxyAuthentication();

	/**
	 * Is use of caching allowed ?
	 * 
	 * @return true if cache can be used
	 */
	boolean isUseCache();

}
