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

package org.geomajas.security;

import org.springframework.context.ApplicationContext;

/**
 * Interface which allows authorizations to access the spring application context when deserialized.
 * <p/>
 * Wiring always occurs when the authorization is added in the security context. Note that the wiring is not
 * automatically refreshed when the application context is refreshed. However, as authorizations are short lived and
 * the entire context (which contains the authorizations) will need to change because of the refresh, this should not
 * be a problem.
 *
 * @author Joachim Van der Auwera
 */
public interface AuthorizationNeedsWiring {

	/**
	 * Method which is called to handle the wiring.
	 *
	 * @param applicationContext Spring application context
	 */
	void wire(ApplicationContext applicationContext);
}
