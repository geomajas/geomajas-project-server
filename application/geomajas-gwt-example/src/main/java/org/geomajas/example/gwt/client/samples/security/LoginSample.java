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

package org.geomajas.example.gwt.client.samples.security;

import org.geomajas.example.gwt.client.samples.base.SamplePanel;
import org.geomajas.example.gwt.client.samples.base.SamplePanelFactory;
import org.geomajas.example.gwt.client.samples.i18n.I18nProvider;
import org.geomajas.plugin.staticsecurity.client.Authentication;
import org.geomajas.plugin.staticsecurity.client.LoginWindow;
import org.geomajas.plugin.staticsecurity.client.LogoutButton;
import org.geomajas.plugin.staticsecurity.client.event.LoginFailureEvent;
import org.geomajas.plugin.staticsecurity.client.event.LoginHandler;
import org.geomajas.plugin.staticsecurity.client.event.LoginSuccessEvent;
import org.geomajas.plugin.staticsecurity.client.event.LogoutFailureEvent;
import org.geomajas.plugin.staticsecurity.client.event.LogoutHandler;
import org.geomajas.plugin.staticsecurity.client.event.LogoutSuccessEvent;

import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;

/**
 * <p>
 * Sample that shows a map with an OpenStreetMap layer.
 * </p>
 * 
 * @author Pieter De Graef
 */
public class LoginSample extends SamplePanel {

	public static final String LOGIN_TITLE = "Login";

	public static final SamplePanelFactory FACTORY = new SamplePanelFactory() {

		public SamplePanel createPanel() {
			return new LoginSample();
		}
	};

	public Canvas getViewPanel() {
		final VLayout layout = new VLayout();
		layout.setWidth100();
		layout.setHeight100();
		layout.setMembersMargin(20);

		// Create horizontal layout for the buttons:
		HLayout buttonLayout = new HLayout();
		buttonLayout.setMembersMargin(10);
		buttonLayout.setHeight(25);

		// Create a button that displays the login window on click:
		final IButton loginButton = new IButton("Log in");
		loginButton.setIcon("[ISOMORPHIC]/geomajas/silk/key.png");
		loginButton.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				LoginWindow loginWindow = new LoginWindow();
				loginWindow.setKeepInParentRect(true);
				layout.addMember(loginWindow);
			}
		});

		// Add a login handler that disables the login button on successful login:
		Authentication.getInstance().addLoginHandler(new LoginHandler() {

			public void onLoginFailure(LoginFailureEvent event) {
			}

			public void onLoginSuccess(LoginSuccessEvent event) {
				loginButton.setDisabled(true);
			}
		});

		// Create a log out button, and attach an event handler:
		LogoutButton logoutButton = new LogoutButton(new LogoutHandler() {

			// Show localized messages:
			public void onLogoutFailure(LogoutFailureEvent event) {
				SC.warn(I18nProvider.getSampleMessages().logoutFailure());
			}

			// Logout success - enable the login button again:
			public void onLogoutSuccess(LogoutSuccessEvent event) {
				SC.say(I18nProvider.getSampleMessages().logoutSuccess());
				loginButton.setDisabled(false);
			}
		});

		buttonLayout.addMember(loginButton);
		buttonLayout.addMember(logoutButton);
		layout.addMember(buttonLayout);
		return layout;
	}

	public String getDescription() {
		return I18nProvider.getSampleMessages().loginDescription();
	}

	public String getSourceFileName() {
		return "classpath:org/geomajas/example/gwt/client/samples/security/LoginSample.txt";
	}

	public String[] getConfigurationFiles() {
		return new String[] { "WEB-INF/security.xml" };
	}

	public String ensureUserLoggedIn() {
		return null;
	}
}
