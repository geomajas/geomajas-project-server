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

import java.io.Serializable;
import java.util.List;

import org.geomajas.annotation.Api;

/**
 * Saved authorizations, usable to persist and restore a security context. Acts as a serialized security context.
 * <p/>
 * The serialized security context does not contain user info, only authentications (hence the name).
 * Restoring a SaveAuthorization objects erases user information from the security context.
 *
 * @author Joachim Van der Auwera
 * @since 1.9.0
 */
@Api(allMethods = true)
public interface SavedAuthorization extends Serializable {

	/**
	 * Get the {@link SavedAuthentication}s contained in this object.
	 * <p/>
	 * This SavedAuthentication class is a placeholder for the {@link Authorization}s and the providing
	 * {@link SecurityService}. It does not contains actual authentication or user information.
	 *
	 * @return list of authentications
	 */
	List<SavedAuthentication> getAuthentications();
}
