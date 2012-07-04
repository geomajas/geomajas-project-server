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

package org.geomajas.layer.geotools.gwt.example.client;

import org.geomajas.gwt.client.controller.PanController;
import org.geomajas.gwt.client.util.WidgetLayout;
import org.geomajas.gwt.client.widget.MapWidget;
import org.geomajas.gwt.client.widget.Toolbar;
import org.geomajas.gwt.example.base.SamplePanel;
import org.geomajas.gwt.example.base.SamplePanelFactory;
import org.geomajas.layer.geotools.gwt.example.client.i18n.GeoToolsMessages;

import com.google.gwt.core.client.GWT;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.layout.VLayout;

/**
 * Sample to demonstrate use of the Geotools layer.
 *
 * @author Jan De Moerloose
 */
public class GeoToolsPanel extends SamplePanel {

	public static final String TITLE = "GeoToolsLayer";

	public static final GeoToolsMessages MESSAGES = GWT.create(GeoToolsMessages.class);

	public static final String GEOTOOLS_TITLE = "GeoTools";

	public static final SamplePanelFactory FACTORY = new SamplePanelFactory() {

		public SamplePanel createPanel() {
			return new GeoToolsPanel();
		}
	};

	public Canvas getViewPanel() {
		VLayout layout = new VLayout();
		layout.setWidth100();
		layout.setHeight100();

		// Map with ID mapGeoTools is defined in the XML configuration. (mapGeoTools.xml)
		final MapWidget map = new MapWidget("mapGeoTools", "appGeoTools");
		// Select the layer, so we can also create features !
		map.getMapModel().runWhenInitialized(new Runnable() {

			@Override
			public void run() {
				// select to create new features
				map.getMapModel().getLayer("clientLayerCountries").setSelected(true);
			}
		});

		// Set a panning controller on the map:
		map.setController(new PanController(map));
		// Create a tool-bar for this map:
		final Toolbar toolbar = new Toolbar(map);
		toolbar.setButtonSize(WidgetLayout.toolbarLargeButtonSize);

		layout.addMember(toolbar);
		layout.addMember(map);
		return layout;
	}

	@Override
	public String getDescription() {
		return MESSAGES.geoToolsDescription();
	}

	@Override
	public String[] getConfigurationFiles() {
		return new String[] { 
				"classpath:org/geomajas/layer/geotools/gwt/example/context/appGeoTools.xml",
				"classpath:org/geomajas/layer/geotools/gwt/example/context/mapGeoTools.xml",
				"classpath:org/geomajas/layer/geotools/gwt/example/context/layerCountries.xml",
				"classpath:org/geomajas/layer/geotools/gwt/example/context/clientLayerCountries.xml",
				"classpath:org/geomajas/layer/geotools/gwt/example/context/spring-geotools.xml"};
	}

	@Override
	public String ensureUserLoggedIn() {
		return "luc";
	}
}
