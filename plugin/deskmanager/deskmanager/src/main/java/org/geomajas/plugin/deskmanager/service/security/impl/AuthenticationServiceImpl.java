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
package org.geomajas.plugin.deskmanager.service.security.impl;

import org.geomajas.plugin.deskmanager.domain.security.AuthenticationSession;
import org.geomajas.plugin.deskmanager.domain.security.User;
import org.geomajas.plugin.deskmanager.security.AuthenticationService;
import org.geomajas.plugin.deskmanager.service.security.AuthenticationSessionService;
import org.geomajas.plugin.deskmanager.service.security.UserService;
import org.geomajas.security.GeomajasSecurityException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Default implementation of {@link org.geomajas.plugin.deskmanager.service.security.UserService},
 * using user info from database via {@link AuthenticationSessionService}.
 *
 * @author Jan Venstermans
 *
 */
@Repository
@Transactional(rollbackFor = { Exception.class })
public class AuthenticationServiceImpl implements AuthenticationService {

	@Autowired
	private UserService userService;

	@Autowired
	private AuthenticationSessionService authenticationSessionService;

	private static final int DEFAULT_AUTHENTICATION_SESSION_LIFETIME_IN_DAYS = 30;

	@Override
	public String authenticateUsernamePassword(String email, String password) throws GeomajasSecurityException {
		User user = userService.findByAddress(email);
		if (user != null && user.getPassword().equals(UserServiceImpl.encodePassword(email, password))) {
			// user has been authenticated => get old sessions or create new one
			List<AuthenticationSession> priorSessions =
					authenticationSessionService.getActiveSessionsOfUser(user.getId());
			AuthenticationSession currentSession;
			if (priorSessions.size() == 1) {
				currentSession = priorSessions.get(0);
			} else {
				currentSession = authenticationSessionService.
						createSession(user, DEFAULT_AUTHENTICATION_SESSION_LIFETIME_IN_DAYS);
			}
			return currentSession.getAuthenticationSessionToken();
		}
		throw new GeomajasSecurityException();
	}

	@Override
	public String getUsernameFromToken(String authenticationSessionToken) throws GeomajasSecurityException {
		AuthenticationSession authenticationSession = getAuthenticationSessionFromToken(authenticationSessionToken);
		if (authenticationSession != null) {
			return authenticationSession.getUser().getEmail();
		}
		throw new GeomajasSecurityException(new Exception("Authentication session token " + authenticationSessionToken +
			" is invalid or no longer active."));
	}

	@Override
	public void removeAuthenticationSession(String authenticationSessionToken) {
		AuthenticationSession authenticationSession = getAuthenticationSessionFromToken(authenticationSessionToken);
		authenticationSessionService.dropSession(authenticationSession);
	}

	private AuthenticationSession getAuthenticationSessionFromToken(String authenticationSessionToken) {
		return authenticationSessionService.getActiveSessionOfToken(authenticationSessionToken);
	}


}
