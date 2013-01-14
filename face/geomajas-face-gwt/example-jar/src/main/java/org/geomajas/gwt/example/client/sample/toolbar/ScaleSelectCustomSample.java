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

package org.geomajas.gwt.example.client.sample.toolbar;

import com.google.gwt.core.client.GWT;
import org.geomajas.gwt.example.base.SamplePanelFactory;
import org.geomajas.gwt.client.controller.PanController;
import org.geomajas.gwt.example.base.SamplePanel;
import org.geomajas.gwt.client.widget.MapWidget;
import org.geomajas.gwt.client.widget.ScaleSelect;
import org.geomajas.gwt.client.widget.Toolbar;

import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.layout.VLayout;
import org.geomajas.gwt.example.client.sample.i18n.SampleMessages;

/**
 * <p>
 * Sample that shows a ScaleSelect using custom zoom levels defined in GWT.
 * </p>
 * 
 * @author Frank Wynants
 */
public class ScaleSelectCustomSample extends SamplePanel {

	private static final SampleMessages MESSAGES = GWT.create(SampleMessages.class);

	public static final String TITLE = "ScaleSelectCustom";

	public static final SamplePanelFactory FACTORY = new SamplePanelFactory() {

		public SamplePanel createPanel() {
			return new ScaleSelectCustomSample();
		}
	};

	/**
	 * @return The viewPanel Canvas
	 */
	public Canvas getViewPanel() {
		VLayout layout = new VLayout();
		layout.setWidth100();
		layout.setHeight100();

		final MapWidget map = new MapWidget("mapOsmNoResolutions", "gwtExample");

		// Set a panning controller on the map:
		map.setController(new PanController(map));

		final Toolbar toolbar = new Toolbar(map);
		// add a button in GWT code
		layout.addMember(toolbar);
		layout.addMember(map);

		// wait for the map to be loaded cause we need a correct map.getPixelPerUnit
		map.getMapModel().runWhenInitialized(new Runnable() {

			public void run() {
				ScaleSelect scaleSelect = new ScaleSelect(map.getMapModel().getMapView(), map.getPixelPerUnit());
				Double[] customScales = new Double[] { 1.0 / 100000000.0, 1.0 / 50000000.0, 1.0 / 2500000.0 };
				scaleSelect.setScales(customScales);
				toolbar.addChild(scaleSelect);
			}
		});

		return layout;
	}

	public String getDescription() {
		return MESSAGES.scaleSelectCustomDescription();
	}

	public String[] getConfigurationFiles() {
		return new String[] {
				"classpath:org/geomajas/gwt/example/context/mapOsmNoResolutions.xml",
				"classpath:org/geomajas/gwt/example/base/layerOsm.xml" };
	}

	public String ensureUserLoggedIn() {
		return "luc";
	}
}
