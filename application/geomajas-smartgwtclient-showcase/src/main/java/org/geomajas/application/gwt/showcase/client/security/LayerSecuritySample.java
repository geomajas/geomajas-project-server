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
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;
import org.geomajas.application.gwt.showcase.client.i18n.ShowcaseMessages;
import org.geomajas.smartgwt.client.controller.PanController;
import org.geomajas.smartgwt.client.widget.MapWidget;
import org.geomajas.smartgwt.example.base.SamplePanel;
import org.geomajas.smartgwt.example.base.SamplePanelFactory;

/**
 * <p>
 * Sample that tests security on layer level.
 * </p>
 * 
 * @author Pieter De Graef
 */
public class LayerSecuritySample extends SamplePanel {

	public static final String LAYER_SECURITY_TITLE = "LayerSecurity";

	private static final ShowcaseMessages MESSAGES = GWT.create(ShowcaseMessages.class);

	public static final SamplePanelFactory FACTORY = new SamplePanelFactory() {

		public SamplePanel createPanel() {
			return new LayerSecuritySample();
		}
	};

	@Override
	public Canvas getViewPanel() {
		final VLayout layout = new VLayout();
		layout.setMembersMargin(10);
		layout.setWidth100();
		layout.setHeight100();

		// Map with ID duisburgMap is defined in the XML configuration. (mapDuisburg.xml)
		final VLayout mapLayout = new VLayout();
		mapLayout.setShowEdges(true);
		mapLayout.setHeight("60%");
		MapWidget map = new MapWidget("mapVectorSecurity", "gwtExample");
		map.setController(new PanController(map));
		mapLayout.addMember(map);

		// Create horizontal layout for login buttons:
		HLayout buttonLayout = new HLayout();
		buttonLayout.setMembersMargin(10);

		buttonLayout.addMember(new UserLoginButton("marino"));
		buttonLayout.addMember(new UserLoginButton("luc"));

		layout.addMember(mapLayout);
		layout.addMember(buttonLayout);
		return layout;
	}

	@Override
	public String getDescription() {
		return MESSAGES.layerSecurityDescription();
	}

	@Override
	public String[] getConfigurationFiles() {
		return new String[] { "WEB-INF/security.xml",
				"WEB-INF/mapVectorSecurity.xml",
				"classpath:org/geomajas/gwt/example/base/layerCountries110m2.xml",
				"classpath:org/geomajas/gwt/example/base/layerWmsBluemarble.xml" };
	}

	@Override
	public String ensureUserLoggedIn() {
		return "luc";
	}
}
