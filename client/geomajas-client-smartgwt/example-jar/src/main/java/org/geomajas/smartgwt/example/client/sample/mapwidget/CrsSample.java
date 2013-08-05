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

package org.geomajas.smartgwt.example.client.sample.mapwidget;

import com.google.gwt.core.client.GWT;
import org.geomajas.smartgwt.example.base.SamplePanel;
import org.geomajas.smartgwt.client.controller.PanController;
import org.geomajas.smartgwt.example.base.SamplePanelFactory;
import org.geomajas.smartgwt.client.widget.MapWidget;

import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.layout.VLayout;
import org.geomajas.smartgwt.example.client.sample.i18n.SampleMessages;

/**
 * <p>
 * Sample that shows a map one raster and one vector layer, both using the same CRS.
 * </p>
 * 
 * @author Pieter De Graef
 */
public class CrsSample extends SamplePanel {

	private static final SampleMessages MESSAGES = GWT.create(SampleMessages.class);

	public static final String TITLE = "Crs";

	public static final SamplePanelFactory FACTORY = new SamplePanelFactory() {

		public SamplePanel createPanel() {
			return new CrsSample();
		}
	};

	public Canvas getViewPanel() {
		VLayout layout = new VLayout();
		layout.setWidth100();
		layout.setHeight100();

		// Map with ID crsMap is defined in the XML configuration. (mapOsm.xml)
		final MapWidget map = new MapWidget("mapCrs", "gwtExample");

		// Set a panning controller on the map:
		map.setController(new PanController(map));
		layout.addMember(map);

		return layout;
	}

	public String getDescription() {
		return MESSAGES.crsDescription();
	}

	public String[] getConfigurationFiles() {
		return new String[] { "classpath:org/geomajas/smartgwt/example/context/mapCrs.xml",
				"classpath:org/geomajas/smartgwt/example/context/layerBeansLonLat.xml",
				"classpath:org/geomajas/smartgwt/example/context/layerBeansMercator.xml" };
	}

	public String ensureUserLoggedIn() {
		return "luc";
	}
}
