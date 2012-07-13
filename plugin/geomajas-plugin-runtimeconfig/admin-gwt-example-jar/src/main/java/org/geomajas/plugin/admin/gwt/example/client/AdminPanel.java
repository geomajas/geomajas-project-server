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
package org.geomajas.plugin.admin.gwt.example.client;

import org.geomajas.gwt.client.widget.MapWidget;
import org.geomajas.gwt.client.widget.Toolbar;
import org.geomajas.gwt.example.base.SamplePanel;
import org.geomajas.gwt.example.base.SamplePanelFactory;
import org.geomajas.plugin.admin.gwt.example.client.i18n.AdminMessages;

import com.google.gwt.core.client.GWT;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.layout.VLayout;

/**
 * Sample panel for admin plugin.
 * 
 * @author Jan De Moerloose
 * 
 */
public class AdminPanel extends SamplePanel {

	public static final String TITLE = "Admininistration";

	public static final AdminMessages MESSAGES = GWT.create(AdminMessages.class);

	public static final SamplePanelFactory FACTORY = new SamplePanelFactory() {

		public SamplePanel createPanel() {
			return new AdminPanel();
		}
	};

	/** {@inheritDoc} */
	public Canvas getViewPanel() {
		VLayout layout = new VLayout();
		layout.setWidth100();
		layout.setHeight100();

		final MapWidget map = new MapWidget("mapAdmin", "appAdmin");
		final Toolbar toolbar = new Toolbar(map);
		layout.addMember(toolbar);
		layout.addMember(map);
		return layout;
	}

	/** {@inheritDoc} */
	public String getDescription() {
		return MESSAGES.adminDescription();
	}

	/** {@inheritDoc} */
	public String[] getConfigurationFiles() {
		return new String[] { "classpath:org/geomajas/plugin/geocoder/gwt/example/context/admin.xml",
				"classpath:org/geomajas/plugin/geocoder/gwt/example/context/appAdmin.xml",
				"classpath:org/geomajas/plugin/geocoder/gwt/example/context/mapAdmin.xml" };
	}

	/** {@inheritDoc} */
	public String ensureUserLoggedIn() {
		return "luc";
	}

}
