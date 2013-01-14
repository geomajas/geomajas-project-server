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

package org.geomajas.gwt.client.command.event;

import com.google.gwt.event.shared.GwtEvent;
import org.geomajas.annotation.Api;
import org.geomajas.gwt.client.command.UserDetail;

/**
 * Event that reports when logging in was successful.
 * 
 * @author Joachim Van der Auwera
 * @since 0.0.0
 */
@Api
public class TokenChangedEvent extends GwtEvent<TokenChangedHandler> {

	private String token;
	private UserDetail userDetail;
	private boolean loginPending;

	/**
	 * Constructor for a token without user details.
	 *
	 * @param token user token
	 */
	public TokenChangedEvent(String token) {
		this(token, null, false);
	}

	/**
	 * Constructor containing both user token and user details.
	 *
	 * @param token user token
	 * @param userDetail user details
	 */
	public TokenChangedEvent(String token, UserDetail userDetail) {
		this(token, userDetail, false);
	}

	/**
	 * Constructor containing both user token and user details.
	 *
	 * @param token user token
	 * @param userDetail user details
	 * @param loginPending true if a login is about to follow
	 */
	public TokenChangedEvent(String token, UserDetail userDetail, boolean loginPending) {
		this.token = token;
		this.userDetail = userDetail;
		if (null == userDetail) {
			this.userDetail = new UserDetail();
		}
		this.loginPending = loginPending;
	}

	/** {@inheritDoc} */
	@SuppressWarnings("unchecked")
	public Type getAssociatedType() {
		return TokenChangedHandler.TYPE;
	}

	/** {@inheritDoc} */
	protected void dispatch(TokenChangedHandler tokenChangedHandler) {
		tokenChangedHandler.onTokenChanged(this);
	}

	/**
	 * Get the new user token.
	 *
	 * @return user token
	 * @since 0.0.0
	 */
	@Api
	public String getToken() {
		return token;
	}

	/**
	 * Get the new user info object.
	 * <p/>
	 * The result is always non-null but the values inside the object may be null.
	 *
	 * @return user info object
	 */
	public UserDetail getUserDetail() {
		return userDetail;
	}

	/**
	 * Indicates whether a new login is pending. Check on this flag to avoid unnecessary actions during the logout
	 * period.
	 * 
	 * @return true if pending
	 * @since 1.1.0
	 */
	@Api
	public boolean isLoginPending() {
		return loginPending;
	}	
	
}
