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

package org.geomajas.example.gwt.client.samples.toolbar;

import org.geomajas.example.gwt.client.samples.base.SamplePanel;
import org.geomajas.example.gwt.client.samples.base.SamplePanelFactory;
import org.geomajas.gwt.client.controller.PanController;
import org.geomajas.gwt.client.map.event.MapModelEvent;
import org.geomajas.gwt.client.map.event.MapModelHandler;
import org.geomajas.example.gwt.client.samples.i18n.I18nProvider;
import org.geomajas.gwt.client.widget.MapWidget;
import org.geomajas.gwt.client.widget.ScaleSelect;
import org.geomajas.gwt.client.widget.Toolbar;

import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.layout.VLayout;

/**
 * <p>
 * Sample that shows a ScaleSelect using the default zoomlevels from the configuration.
 * </p>
 * 
 * @author Frank Wynants
 */
public class ScaleSelectDefaultSample extends SamplePanel {

	public static final String TITLE = "ScaleSelectDefault";

	public static final SamplePanelFactory FACTORY = new SamplePanelFactory() {

		public SamplePanel createPanel() {
			return new ScaleSelectDefaultSample();
		}
	};

	/**
	 * @return The viewPanel Canvas
	 */
	public Canvas getViewPanel() {
		VLayout layout = new VLayout();
		layout.setWidth100();
		layout.setHeight100();

		final MapWidget map = new MapWidget("mapOsm", "gwt-samples");

		// Set a panning controller on the map:
		map.setController(new PanController(map));

		final Toolbar toolbar = new Toolbar(map);
		// add a button in GWT code
		layout.addMember(toolbar);
		layout.addMember(map);

		// wait for the map to be loaded cause we need a correct map.getPixelPerUnit
		map.getMapModel().addMapModelHandler(new MapModelHandler() {

			public void onMapModelChange(MapModelEvent event) {
				ScaleSelect scaleSelect = new ScaleSelect(map.getMapModel().getMapView(), map.getPixelPerUnit());
				toolbar.addMember(scaleSelect);
			}
		});

		return layout;
	}

	public String getDescription() {
		return I18nProvider.getSampleMessages().scaleSelectDefaultDescription();
	}

	public String getSourceFileName() {
		return "classpath:org/geomajas/example/gwt/client/samples/toolbar/ScaleSelectDefaultSample.txt";
	}

	public String[] getConfigurationFiles() {
		return new String[] { "WEB-INF/layerOsm.xml",
				"WEB-INF/mapOsm.xml" };
	}

	public String ensureUserLoggedIn() {
		return "luc";
	}
}
