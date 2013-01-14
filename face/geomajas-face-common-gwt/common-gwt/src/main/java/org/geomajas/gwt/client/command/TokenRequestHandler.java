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

package org.geomajas.gwt.client.command;

import org.geomajas.annotation.Api;
import org.geomajas.gwt.client.command.event.TokenChangedHandler;

/**
 * Handler which requests an authentication token.
 * <p/>
 * This is called automatically when a command gives a {@link org.geomajas.security.GeomajasSecurityException} with
 * exception code {@link org.geomajas.global.ExceptionCode#CREDENTIALS_MISSING_OR_INVALID}.
 *
 * @author Joachim Van der Auwera
 * @since 0.0.0
 */
@Api(allMethods = true)
public interface TokenRequestHandler {

	/**
	 * Get an authentication token.
	 *
	 * @param tokenChangedHandler callback which needs to be invoked when the token is known
	 */
	void login(TokenChangedHandler tokenChangedHandler);

}
