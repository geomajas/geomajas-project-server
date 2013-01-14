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

package org.geomajas.application.gwt.showcase.client.security;

import com.google.gwt.core.client.GWT;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import org.geomajas.application.gwt.showcase.client.ShowcaseEntryPoint;
import org.geomajas.application.gwt.showcase.client.i18n.ShowcaseMessages;
import org.geomajas.gwt.client.command.GwtCommandDispatcher;

/**
 * Button for logging in a specific user.
 *
 * @author Joachim Van der Auwera
 */
public class UserLoginButton extends IButton {

	private static final ShowcaseMessages MESSAGES = GWT.create(ShowcaseMessages.class);

	private Runnable runnable;

	/**
	 * Create button to login a specific user.
	 *
	 * @param user user
	 */
	public UserLoginButton(String user) {
		this(user, null);
	}

	public UserLoginButton(String user, Runnable runnable) {
		super(MESSAGES.securityLogInWith(user));
		setWidth("50%");
		addClickHandler(new LoginClickHandler(user));
		this.runnable = runnable;
	}

	/**
	 * Click handler for logging in.
	 */
	private final class LoginClickHandler implements ClickHandler {

		private final String user;

		/**
		 * Constructor.
		 *
		 * @param user user to log in (password is the same as the login)
		 */
		public LoginClickHandler(String user) {
			this.user = user;
		}

		/** {@inheritDoc} */
		public void onClick(ClickEvent clickEvent) {
			ShowcaseTokenRequestHandler.userId = user;
			ShowcaseTokenRequestHandler.password = user;
			ShowcaseEntryPoint.runOnLogin = runnable;
			GwtCommandDispatcher.getInstance().login();
		}
	}
}
