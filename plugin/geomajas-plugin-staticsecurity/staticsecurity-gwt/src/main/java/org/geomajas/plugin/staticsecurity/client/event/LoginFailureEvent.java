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
package org.geomajas.plugin.staticsecurity.client.event;

import java.util.List;

import com.google.gwt.event.shared.GwtEvent;
import org.geomajas.annotation.Api;

/**
 * Event that reports when logging in has failed.
 * 
 * @author Pieter De Graef
 * @since 1.7.1
 * @deprecated use {@link org.geomajas.gwt.client.command.event.TokenChangedEvent}
 */
@Api
@Deprecated
public class LoginFailureEvent extends GwtEvent<LoginHandler> {

	private final List<String> messages;

	public LoginFailureEvent(List<String> messages) {
		super();
		this.messages = messages;
	}

	@SuppressWarnings("unchecked")
	public Type getAssociatedType() {
		return LoginHandler.TYPE;
	}

	protected void dispatch(LoginHandler loginHandler) {
		loginHandler.onLoginFailure(this);
	}

	/**
	 * @return Get the error message
	 * @since 1.7.1
	 */
	@Api
	public List<String> getMessages() {
		return messages;
	}
}
