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
package org.geomajas.plugin.deskmanager.service.common;

import org.geomajas.configuration.client.ClientApplicationInfo;
import org.geomajas.plugin.deskmanager.domain.Geodesk;

/**
 * This service will create a ClientApplicationInfo object from a loketConfiguration that can be used to load a geomajas
 * application.
 * 
 * @author Kristof Heirwegh
 */
public interface GeodeskConfigurationService {

	/**
	 * 
	 * @param loket
	 * @param includeMaps if the maps should be loaded, is set to false the maps won't be included.
	 * @return
	 */
	ClientApplicationInfo createGeodeskConfiguration(Geodesk loket, boolean includeMaps);

	/**
	 * 
	 * @param loket
	 * @param includeMaps
	 * @return
	 */
	ClientApplicationInfo createClonedGeodeskConfiguration(Geodesk loket, boolean includeMaps);

}
