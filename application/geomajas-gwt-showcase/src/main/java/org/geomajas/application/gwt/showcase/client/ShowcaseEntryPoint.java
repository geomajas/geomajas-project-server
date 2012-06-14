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

import org.geomajas.application.gwt.showcase.client.i18n.ShowcaseMessages;
import org.geomajas.application.gwt.showcase.client.layer.OpenCycleMapSample;
import org.geomajas.application.gwt.showcase.client.layer.TmsSample;
import org.geomajas.application.gwt.showcase.client.security.AttributeSecuritySample;
import org.geomajas.application.gwt.showcase.client.security.CommandSecuritySample;
import org.geomajas.application.gwt.showcase.client.security.FilterSecuritySample;
import org.geomajas.application.gwt.showcase.client.security.LayerSecuritySample;
import org.geomajas.application.gwt.showcase.client.security.ShowcaseTokenRequestHandler;
import org.geomajas.application.gwt.showcase.client.security.ToolSecuritySample;
import org.geomajas.gwt.client.command.GwtCommandDispatcher;
import org.geomajas.gwt.client.command.event.TokenChangedEvent;
import org.geomajas.gwt.client.command.event.TokenChangedHandler;
import org.geomajas.gwt.example.base.ExampleLayout;
import org.geomajas.gwt.example.base.SampleTreeNode;
import org.geomajas.gwt.example.base.SampleTreeNodeRegistry;
import org.geomajas.plugin.staticsecurity.client.util.SsecAccess;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.smartgwt.client.widgets.Label;

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

	// CHECKSTYLE VISIBILITY MODIFIER: OFF

	/** Runnable which should be run on successful login. */
	public static Runnable runOnLogin;

	// CHECKSTYLE VISIBILITY MODIFIER: ON

	public void onModuleLoad() {
		// layer samples
		SampleTreeNodeRegistry.addSampleTreeNode(new SampleTreeNode(MESSAGES.openCycleMapTitle(),
				"[ISOMORPHIC]/geomajas/osgeo/layer-raster.png", OpenCycleMapSample.TITLE, "Layers",
				OpenCycleMapSample.FACTORY));
		SampleTreeNodeRegistry.addSampleTreeNode(new SampleTreeNode(MESSAGES.tmsTitle(),
				"[ISOMORPHIC]/geomajas/osgeo/layer-raster.png", TmsSample.TITLE, "Layers",
				TmsSample.FACTORY));

		// Security samples:
		SampleTreeNodeRegistry.addSampleTreeNode(new SampleTreeNode(MESSAGES.treeGroupSecurity(),
				"[ISOMORPHIC]/geomajas/silk/key.png", SECURITY_GROUP, "topLevel"));
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
		
		exampleLayout.setAuthenticationHandler(new ShowcaseAuthenticationHandler());
		
		exampleLayout.buildUi();

		// security demo
		final Label userLabel = exampleLayout.getUserLabel();
		GwtCommandDispatcher dispatcher = GwtCommandDispatcher.getInstance();
		dispatcher.setTokenRequestHandler(new ShowcaseTokenRequestHandler());
		dispatcher.addTokenChangedHandler(new TokenChangedHandler() {
			public void onTokenChanged(TokenChangedEvent event) {
				String userId = null;
				if (null != event.getUserDetail()) {
					userId = event.getUserDetail().getUserId();
				}
				if (null == userId) {
					userLabel.setContents("No user is logged in.");
				} else {
					userLabel.setContents("Logged in with: " + userId);
					if (null != runOnLogin) {
						runOnLogin.run();
						runOnLogin = null;
					}
				}
			}
		});
		SsecAccess.login("luc", "luc", null);
	}

	/**
	 * Authentication handler to assure panels are running with the requested user.
	 *
	 * @author Joachim Van der Auwera
	 */
	private final class ShowcaseAuthenticationHandler implements ExampleLayout.SimpleAuthenticationHandler {

		public void login(String login, String password, final Runnable callback) {
			if (null == login) {
				login = "luc";
				password = login;
			}
			ShowcaseTokenRequestHandler.userId = login;
			ShowcaseTokenRequestHandler.password = password;
			runOnLogin = callback;
			GwtCommandDispatcher.getInstance().login();
		}
	}

}
