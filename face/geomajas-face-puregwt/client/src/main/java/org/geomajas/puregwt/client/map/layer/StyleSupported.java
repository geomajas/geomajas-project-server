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
package org.geomajas.puregwt.client.map.layer;

import org.geomajas.annotation.Api;
import org.geomajas.configuration.NamedStyleInfo;


/**
 * Extension for the layer interface which signifies that this particular layer has support for styles. Trough this 
 * interface styles can be updated, the layer will therefore fire the necessary events to notify it's listeners.
 *  
 * @author Oliver May
 * @since 1.0.0
 */
@Api(allMethods = true)
public interface StyleSupported {
	
	/**
	 * Update the style for a layer. The layer will notify it's listeners.
	 * 
	 * @param styleInfo the styleinfo
	 */
	void updateStyle(NamedStyleInfo styleInfo);
	
}
