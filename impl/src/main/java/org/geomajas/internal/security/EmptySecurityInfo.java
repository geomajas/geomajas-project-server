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

package org.geomajas.internal.security;

import org.geomajas.security.SecurityInfo;
import org.geomajas.security.SecurityService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;

/**
 * Empty implementation of {@link SecurityInfo} with warning on instantiation.
 * 
 * @author Jan Venstermans
 */
public final class EmptySecurityInfo extends SecurityInfo {

	private final Logger log = LoggerFactory.getLogger(EmptySecurityInfo.class);

	/**
	 * This constructor is only called if bean security.securityInfo has not been overwritten.
	 */
	public EmptySecurityInfo() {
		setLoopAllServices(false);
		setSecurityServices(new ArrayList<SecurityService>());
		log.warn("No Custom SecurityInfo defined. All server requests will be blocked. Please overwrite " +
				"bean security.securityInfo to enable client-server communication.");
	}

}
