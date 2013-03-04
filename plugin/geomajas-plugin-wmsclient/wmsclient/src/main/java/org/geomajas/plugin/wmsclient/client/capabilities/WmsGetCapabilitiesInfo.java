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

package org.geomajas.plugin.wmsclient.client.capabilities;

import java.io.Serializable;
import java.util.List;

import org.geomajas.annotation.Api;

/**
 * Generic WMS GetCapabilities definition.
 * 
 * @author Pieter De Graef
 * @since 1.0.0
 */
@Api(allMethods = true)
public interface WmsGetCapabilitiesInfo extends Serializable {

	/**
	 * Retrieve the list of layers defined in the capabilities file.
	 * 
	 * @return The full list of layers.
	 */
	List<WmsLayerInfo> getLayers();
}