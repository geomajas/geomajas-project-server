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
package org.geomajas.plugin.deskmanager.client.gwt.manager;

import org.geomajas.annotation.Api;
import org.geomajas.plugin.deskmanager.domain.security.dto.ProfileDto;


/**
 * Handler that is called when the manager applicaiton is initialized.
 * 
 * @author Oliver May
 *
 */
@Api
public interface ManagerInitializationHandler {

	/**
	 * Fired when the manager application is initialized.
	 * 
	 * @param profile the currently active user profile.
	 */
	void initialized(ProfileDto profile);
	
}
