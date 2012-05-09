/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2012 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */

package org.geomajas.application.gwt.showcase.client.security;

import com.google.gwt.core.client.GWT;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;
import org.geomajas.application.gwt.showcase.client.i18n.ShowcaseMessages;
import org.geomajas.gwt.example.base.SamplePanel;
import org.geomajas.gwt.example.base.SamplePanelFactory;
import org.geomajas.plugin.staticsecurity.client.TokenReleaseButton;
import org.geomajas.plugin.staticsecurity.client.TokenRequestWindow;

/**
 * <p>
 * Sample that shows a map with an OpenStreetMap layer.
 * </p>
 * 
 * @author Pieter De Graef
 */
public class LoginSample extends SamplePanel {

	public static final String LOGIN_TITLE = "Login";
	private static final ShowcaseMessages MESSAGES = GWT.create(ShowcaseMessages.class);

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
				TokenRequestWindow tokenRequestWindow = new TokenRequestWindow();
				tokenRequestWindow.setKeepInParentRect(true);
				layout.addMember(tokenRequestWindow);
			}
		});

		// Create a log out button, and attach an event handler:
		TokenReleaseButton tokenReleaseButton = new TokenReleaseButton();

		buttonLayout.addMember(loginButton);
		buttonLayout.addMember(tokenReleaseButton);
		layout.addMember(buttonLayout);
		return layout;
	}

	public String getDescription() {
		return MESSAGES.loginDescription();
	}

	public String[] getConfigurationFiles() {
		return new String[] { "WEB-INF/security.xml" };
	}

	public String ensureUserLoggedIn() {
		return "luc";
	}
}
