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
package org.geomajas.plugin.deskmanager.client.gwt.common;

import org.geomajas.annotation.Api;
import org.geomajas.gwt.client.command.TokenRequestHandler;
import org.geomajas.gwt.client.command.event.TokenChangedEvent;
import org.geomajas.gwt.client.command.event.TokenChangedHandler;
import org.geomajas.plugin.deskmanager.client.gwt.common.RolesWindow.AskRoleCallback;
import org.geomajas.plugin.deskmanager.domain.security.dto.ProfileDto;

/**
 * Token request handler for the deskmanager. This handler will request a list of roles from the server, and let the
 * user choose the correct one.
 * 
 * This implementation presumes that the user has already logged in trough another authentication way (reverse proxy,
 * sso,...) so that only a specific role for the application has to be chosen.
 * 
 * @author Oliver May
 * @since 1.0.0
 */
@Api
public class DeskmanagerTokenRequestHandler implements TokenRequestHandler {

	private String geodeskId;

	private ProfileSelectionWindow profileSelectionWindow;

	protected String token;

	protected ProfileDto profile;

	public DeskmanagerTokenRequestHandler(String geodeskId, ProfileSelectionWindow profileSelectionWindow) {
		this.geodeskId = geodeskId;
		this.profileSelectionWindow = profileSelectionWindow;
	}

	public void login(final TokenChangedHandler tokenChangedHandler) {
		profileSelectionWindow.askRole(geodeskId, new AskRoleCallback() {

			public void execute(String token, ProfileDto profile) {
				DeskmanagerTokenRequestHandler.this.token = token;
				DeskmanagerTokenRequestHandler.this.profile = profile;
				tokenChangedHandler.onTokenChanged(new TokenChangedEvent(token));
			}
		});
	}

	/**
	 * Get the token for the current active role.
	 * 
	 * @return the token
	 */
	public String getToken() {
		return token;
	}

	/**
	 * Get the profile for the current active role.
	 * 
	 * @return the profile
	 */
	public ProfileDto getProfile() {
		return profile;
	}

}
