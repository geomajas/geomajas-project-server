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
package org.geomajas.service;

import org.geomajas.annotation.Api;
import org.geomajas.configuration.NamedStyleInfo;

/**
 * This service handles retrieval and registration of services. Default styles are fetched from the configuration,
 * (client) updated styles must be registered to this service.
 * 
 * @author Oliver May
 * @since 1.13.0
 */
@Api
public interface StyleService {
	
	/**
	 * Registers a style for a layer, returns the styleName by which the style can be retrieved.
	 *  
	 * @param layerId the id of the layer
	 * @param style the style
	 * @return a new styleName.
	 */
	String registerStyle(String layerId, NamedStyleInfo style);
	
	
	/**
	 * Retrieves the style for a given layer and styleName, null if no such style exists.
	 * 
	 * @param layerId the id of the layer
	 * @param styleName the style name
	 * @return the named style
	 */
	NamedStyleInfo retrieveStyle(String layerId, String styleName);
	
}
