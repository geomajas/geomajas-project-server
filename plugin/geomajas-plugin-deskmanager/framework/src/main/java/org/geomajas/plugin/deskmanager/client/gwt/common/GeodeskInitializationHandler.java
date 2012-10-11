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
package org.geomajas.plugin.deskmanager.client.gwt.common;

import org.geomajas.annotation.Api;
import org.geomajas.plugin.deskmanager.command.geodesk.dto.InitializeGeodeskResponse;



/**
 * Handler that is called when a geodesk is loaded.
 * 
 * @author Oliver May
 * @since 1.0.0
 */
@Api
public interface GeodeskInitializationHandler {
	
	void initialized(InitializeGeodeskResponse response);

}
