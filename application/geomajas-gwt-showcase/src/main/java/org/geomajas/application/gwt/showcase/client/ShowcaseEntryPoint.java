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

package org.geomajas.application.gwt.showcase.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.smartgwt.client.widgets.Label;
import org.geomajas.application.gwt.showcase.client.i18n.ShowcaseMessages;
import org.geomajas.application.gwt.showcase.client.security.AttributeSecuritySample;
import org.geomajas.application.gwt.showcase.client.security.CommandSecuritySample;
import org.geomajas.application.gwt.showcase.client.security.FilterSecuritySample;
import org.geomajas.application.gwt.showcase.client.security.LayerSecuritySample;
import org.geomajas.application.gwt.showcase.client.security.LoginSample;
import org.geomajas.application.gwt.showcase.client.security.ToolSecuritySample;
import org.geomajas.gwt.example.base.ExampleLayout;
import org.geomajas.gwt.example.base.SampleTreeNode;
import org.geomajas.gwt.example.base.SampleTreeNodeRegistry;
import org.geomajas.plugin.staticsecurity.client.Authentication;
import org.geomajas.plugin.staticsecurity.client.event.LoginFailureEvent;
import org.geomajas.plugin.staticsecurity.client.event.LoginHandler;
import org.geomajas.plugin.staticsecurity.client.event.LoginSuccessEvent;
import org.geomajas.plugin.staticsecurity.client.event.LogoutFailureEvent;
import org.geomajas.plugin.staticsecurity.client.event.LogoutHandler;
import org.geomajas.plugin.staticsecurity.client.event.LogoutSuccessEvent;

/**
 * <p>
 * The GWT test case sample application. Here here!
 * </p>
 *
 * @author Pieter De Graef
 */
public class ShowcaseEntryPoint implements EntryPoint {

	private static final ShowcaseMessages MESSAGES = GWT.create(ShowcaseMessages.class);

	private static final String SECURITY_GROUP = "Security";

	public void onModuleLoad() {
		// Security samples:
		SampleTreeNodeRegistry.addSampleTreeNode(new SampleTreeNode(MESSAGES.treeGroupSecurity(),
				"[ISOMORPHIC]/geomajas/silk/key.png", SECURITY_GROUP, "topLevel"));
		SampleTreeNodeRegistry.addSampleTreeNode(new SampleTreeNode(MESSAGES.loginTitle(),
				"[ISOMORPHIC]/geomajas/staticsecurity/key_go.png", LoginSample.LOGIN_TITLE, SECURITY_GROUP,
				LoginSample.FACTORY));
		SampleTreeNodeRegistry.addSampleTreeNode(new SampleTreeNode(MESSAGES.layerSecurityTitle(),
				"[ISOMORPHIC]/geomajas/staticsecurity/key_go.png", LayerSecuritySample.LAYER_SECURITY_TITLE,
				SECURITY_GROUP, LayerSecuritySample.FACTORY));
		SampleTreeNodeRegistry.addSampleTreeNode(new SampleTreeNode(MESSAGES.filterSecurityTitle(),
				"[ISOMORPHIC]/geomajas/staticsecurity/key_go.png", FilterSecuritySample.TITLE, SECURITY_GROUP,
				FilterSecuritySample.FACTORY));
		SampleTreeNodeRegistry.addSampleTreeNode(new SampleTreeNode(MESSAGES.attributeSecurityTitle(),
				"[ISOMORPHIC]/geomajas/staticsecurity/key_go.png", AttributeSecuritySample.TITLE, SECURITY_GROUP,
				AttributeSecuritySample.FACTORY));
		SampleTreeNodeRegistry.addSampleTreeNode(new SampleTreeNode(MESSAGES.commandSecurityTitle(),
				"[ISOMORPHIC]/geomajas/staticsecurity/key_go.png", CommandSecuritySample.TITLE, SECURITY_GROUP,
				CommandSecuritySample.FACTORY));
		SampleTreeNodeRegistry.addSampleTreeNode(new SampleTreeNode(MESSAGES.toolSecurityTitle(),
				"[ISOMORPHIC]/geomajas/staticsecurity/key_go.png", ToolSecuritySample.TITLE, SECURITY_GROUP,
				ToolSecuritySample.FACTORY));

		ExampleLayout exampleLayout = new ExampleLayout();
		exampleLayout.buildUi();

		// security demo
		final Label userLabel = exampleLayout.getUserLabel();
		Authentication.getInstance().addLoginHandler(new LoginHandler() {

			public void onLoginFailure(LoginFailureEvent event) {
			}

			public void onLoginSuccess(LoginSuccessEvent event) {
				userLabel.setContents("Logged in with: " + Authentication.getInstance().getUserId());
			}
		});
		Authentication.getInstance().addLogoutHandler(new LogoutHandler() {

			public void onLogoutFailure(LogoutFailureEvent event) {
			}

			public void onLogoutSuccess(LogoutSuccessEvent event) {
				userLabel.setContents("No user is logged in.");
			}
		});
		Authentication.getInstance().login("luc", "luc", null);
	}

}
