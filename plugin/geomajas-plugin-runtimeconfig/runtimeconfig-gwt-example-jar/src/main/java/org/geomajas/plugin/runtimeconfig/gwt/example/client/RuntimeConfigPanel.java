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
package org.geomajas.plugin.runtimeconfig.gwt.example.client;

import org.geomajas.gwt.client.widget.MapWidget;
import org.geomajas.gwt.client.widget.Toolbar;
import org.geomajas.gwt.example.base.SamplePanel;
import org.geomajas.gwt.example.base.SamplePanelFactory;
import org.geomajas.plugin.runtimeconfig.gwt.example.client.i18n.RuntimeConfigMessages;

import com.google.gwt.core.client.GWT;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.layout.VLayout;

/**
 * Sample panel for admin plugin.
 * 
 * @author Jan De Moerloose
 * 
 */
public class RuntimeConfigPanel extends SamplePanel {

	public static final String TITLE = "RuntimeConfiginistration";

	public static final RuntimeConfigMessages MESSAGES = GWT.create(RuntimeConfigMessages.class);

	public static final SamplePanelFactory FACTORY = new SamplePanelFactory() {

		public SamplePanel createPanel() {
			return new RuntimeConfigPanel();
		}
	};

	@Override
	public Canvas getViewPanel() {
		VLayout layout = new VLayout();
		layout.setWidth100();
		layout.setHeight100();

		final MapWidget map = new MapWidget("mapRuntimeConfig", "appRuntimeConfig");
		final Toolbar toolbar = new Toolbar(map);
		layout.addMember(toolbar);
		layout.addMember(map);
		return layout;
	}

	@Override
	public String getDescription() {
		return MESSAGES.adminDescription();
	}

	@Override
	public String[] getConfigurationFiles() {
		return new String[] { "classpath:org/geomajas/plugin/geocoder/gwt/example/context/runtimeConfig.xml",
				"classpath:org/geomajas/plugin/geocoder/gwt/example/context/appRuntimeConfig.xml",
				"classpath:org/geomajas/plugin/geocoder/gwt/example/context/mapRuntimeConfig.xml" };
	}

	@Override
	public String ensureUserLoggedIn() {
		return "luc";
	}

}
