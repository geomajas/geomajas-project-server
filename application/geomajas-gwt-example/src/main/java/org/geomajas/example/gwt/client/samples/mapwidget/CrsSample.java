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

package org.geomajas.example.gwt.client.samples.mapwidget;

import org.geomajas.example.gwt.client.samples.base.SamplePanel;
import org.geomajas.gwt.client.controller.PanController;
import org.geomajas.example.gwt.client.samples.base.SamplePanelFactory;
import org.geomajas.example.gwt.client.samples.i18n.I18nProvider;
import org.geomajas.gwt.client.widget.MapWidget;

import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.layout.VLayout;

/**
 * <p>
 * Sample that shows a map one raster and one vector layer, both using the same CRS.
 * </p>
 * 
 * @author Pieter De Graef
 */
public class CrsSample extends SamplePanel {

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
		final MapWidget map = new MapWidget("mapCrs", "gwt-samples");

		// Set a panning controller on the map:
		map.setController(new PanController(map));
		layout.addMember(map);

		return layout;
	}

	public String getDescription() {
		return I18nProvider.getSampleMessages().crsDescription();
	}

	public String getSourceFileName() {
		return "classpath:org/geomajas/example/gwt/client/samples/mapwidget/CrsSample.txt";
	}

	public String[] getConfigurationFiles() {
		return new String[] { "WEB-INF/mapCrs.xml", "WEB-INF/layerBeansLonLat.xml", "WEB-INF/layerBeansMercator.xml" };
	}

	public String ensureUserLoggedIn() {
		return "luc";
	}
}
