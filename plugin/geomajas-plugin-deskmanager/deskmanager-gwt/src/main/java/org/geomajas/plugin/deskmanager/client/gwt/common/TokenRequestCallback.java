package org.geomajas.plugin.deskmanager.client.gwt.common;

import org.geomajas.plugin.deskmanager.domain.security.dto.ProfileDto;

/**
 * Callback class for the token request handler.
 *
 * @author Oliver May
 *
 */
public interface TokenRequestCallback {

	/**
	 * Callback when a role is selected.
	 *
	 * @param token
	 *            the selected token.
	 */
	void onTokenChanged(String token, ProfileDto profile);
}
