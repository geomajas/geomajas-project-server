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

package org.geomajas.example.gwt.client.samples.layer;

import org.geomajas.example.gwt.client.samples.base.SamplePanel;
import org.geomajas.example.gwt.client.samples.base.SamplePanelFactory;
import org.geomajas.example.gwt.client.samples.i18n.I18nProvider;
import org.geomajas.gwt.client.controller.PanController;
import org.geomajas.gwt.client.widget.MapWidget;

import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.layout.VLayout;
import org.geomajas.layer.google.gwt.client.GoogleAddon;

/**
 * Sample that shows three Google maps, one with normal, one satellite, one with terrain.
 * 
 * @author Joachim Van der Auwera
 * @author Oliver May
 */
public class GoogleSample extends SamplePanel {

	public static final String TITLE = "GoogleLayer";

	public static final SamplePanelFactory FACTORY = new SamplePanelFactory() {

		public SamplePanel createPanel() {
			return new GoogleSample();
		}
	};

	private MapWidget googleMap;

	private MapWidget googleSatMap;

	private MapWidget googleTerrainMap;

	public Canvas getViewPanel() {
		VLayout layout = new VLayout();
		layout.setWidth100();
		layout.setHeight100();
		layout.setMembersMargin(10);

		// Create map with Google layer, and add a PanController to it:
		VLayout mapLayout1 = new VLayout();
		mapLayout1.setShowEdges(true);
		googleMap = new MapWidget("mapGoogle", "gwt-samples");
		googleMap.setController(new PanController(googleMap));
		mapLayout1.addMember(googleMap);

		// Create map with Google layer (satellite), and add a PanController to it:
		VLayout mapLayout2 = new VLayout();
		mapLayout2.setShowEdges(true);
		googleSatMap = new MapWidget("mapGoogleSat", "gwt-samples");
		googleSatMap.setController(new PanController(googleSatMap));
		mapLayout2.addMember(googleSatMap);

		// Create map with Google layer (terrain), and add a PanController to it:
		VLayout mapLayout3 = new VLayout();
		mapLayout3.setShowEdges(true);
		googleTerrainMap = new MapWidget("mapGoogleTerrain", "gwt-samples");
		googleTerrainMap.setController(new PanController(googleTerrainMap));
		mapLayout3.addMember(googleTerrainMap);

		// Place all three in the layout:
		layout.addMember(mapLayout1);
		layout.addMember(mapLayout2);
		layout.addMember(mapLayout3);

		return layout;
	}

	public String getDescription() {
		return I18nProvider.getSampleMessages().googleDescription();
	}

	public String getSourceFileName() {
		return "classpath:org/geomajas/example/gwt/client/samples/layer/GoogleSample.txt";
	}

	public String[] getConfigurationFiles() {
		return new String[] { "WEB-INF/mapGoogle.xml", "WEB-INF/mapGoogleSat.xml",
				"WEB-INF/mapGoogleTerrain.xml", "WEB-INF/layerGoogle.xml", "WEB-INF/layerGoogleSat.xml",
				"WEB-INF/layerGoogleTerrain.xml" };
	}

	public String ensureUserLoggedIn() {
		return "luc";
	}

	// @extract-start GoogleSample, GoogleSample
	protected void onDraw2() {
		googleMap.registerMapAddon(new GoogleAddon("google", googleMap, GoogleAddon.MapType.NORMAL, false));
		googleSatMap.registerMapAddon(new GoogleAddon("google", googleSatMap, GoogleAddon.MapType.SATELLITE, false));
		googleTerrainMap.registerMapAddon(new GoogleAddon("google", googleTerrainMap, GoogleAddon.MapType.PHYSICAL,
				false));
		MapSynchronizer synchronizer = new MapSynchronizer(googleMap.getMapModel().getMapView(), googleSatMap
				.getMapModel().getMapView());
		MapSynchronizer synchronizer2 = new MapSynchronizer(googleMap.getMapModel().getMapView(), googleTerrainMap
				.getMapModel().getMapView());
		synchronizer.setEnabled(true);
		synchronizer2.setEnabled(true);
	}
	// @extract-end

}
