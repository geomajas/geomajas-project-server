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
package org.geomajas.plugin.deskmanager.service.security;

import org.geomajas.plugin.deskmanager.domain.security.AuthenticationSession;
import org.geomajas.plugin.deskmanager.domain.security.User;

import java.util.List;

/**
 * Contains methods for sessions.
 *
 * @author Jan Venstermans
 */
public interface AuthenticationSessionService {

	AuthenticationSession getActiveSessionOfToken(String token);

	List<AuthenticationSession> getActiveSessionsOfUser(Long userId);

	/**
	 * Create a session for the user and save the session in the database.
	 * The expiration date of the created session is constructed from current date, increased with the provided
	 * {@paramref lifetimeInDays} argument.
	 *
	 * @param user
	 * @param lifetimeInDays lifetime of the session in days, must be a positive integer.
	 * @return login session
	 */
	AuthenticationSession createSession(User user, int lifetimeInDays);

	void dropSession(AuthenticationSession session);

	boolean isActive(AuthenticationSession session);
}
