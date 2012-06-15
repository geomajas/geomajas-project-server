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

package org.geomajas.layer.google.gwt.example.client;

import org.geomajas.gwt.client.controller.PanController;
import org.geomajas.gwt.client.widget.MapWidget;
import org.geomajas.gwt.example.base.SamplePanel;
import org.geomajas.gwt.example.base.SamplePanelFactory;
import org.geomajas.layer.google.gwt.client.GoogleAddon;
import org.geomajas.layer.google.gwt.example.client.i18n.GoogleMessages;

import com.google.gwt.core.client.GWT;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.layout.VLayout;

/**
 * Sample to demonstrate use of the google layer.
 *
 * @author Jan De Moerloose
 */
public class GooglePanel extends SamplePanel {

	public static final String TITLE = "GoogleLayer";

	public static final GoogleMessages MESSAGES = GWT.create(GoogleMessages.class);
	
	private MapWidget googleMap;

	private MapWidget googleSatMap;

	private MapWidget googleTerrainMap;

	public static final SamplePanelFactory FACTORY = new SamplePanelFactory() {

		public SamplePanel createPanel() {
			return new GooglePanel();
		}
	};

	/** {@inheritDoc} */
	public Canvas getViewPanel() {
		VLayout layout = new VLayout();
		layout.setWidth100();
		layout.setHeight100();
		layout.setMembersMargin(10);

		// Create map with Google layer, and add a PanController to it:
		VLayout mapLayout1 = new VLayout();
		mapLayout1.setShowEdges(true);
		googleMap = new MapWidget("mapGoogle", "appGoogle");
		googleMap.setController(new PanController(googleMap));
		mapLayout1.addMember(googleMap);

		// Create map with Google layer (satellite), and add a PanController to it:
		VLayout mapLayout2 = new VLayout();
		mapLayout2.setShowEdges(true);
		googleSatMap = new MapWidget("mapGoogleSat", "appGoogle");
		googleSatMap.setController(new PanController(googleSatMap));
		mapLayout2.addMember(googleSatMap);

		// Create map with Google layer (terrain), and add a PanController to it:
		VLayout mapLayout3 = new VLayout();
		mapLayout3.setShowEdges(true);
		googleTerrainMap = new MapWidget("mapGoogleTerrain", "appGoogle");
		googleTerrainMap.setController(new PanController(googleTerrainMap));
		mapLayout3.addMember(googleTerrainMap);

		// Place all three in the layout:
		layout.addMember(mapLayout1);
		layout.addMember(mapLayout2);
		layout.addMember(mapLayout3);

		return layout;
	}
	
	// @extract-start GoogleSample, GoogleSample
	protected void onDraw() {
		googleMap.registerMapAddon(new GoogleAddon("google", googleMap, GoogleAddon.MapType.NORMAL));
		googleSatMap.registerMapAddon(new GoogleAddon("google", googleSatMap, GoogleAddon.MapType.SATELLITE));
		googleTerrainMap.registerMapAddon(new GoogleAddon("google", googleTerrainMap, GoogleAddon.MapType.PHYSICAL));
		MapSynchronizer synchronizer = new MapSynchronizer(googleMap.getMapModel().getMapView(), googleSatMap
				.getMapModel().getMapView());
		MapSynchronizer synchronizer2 = new MapSynchronizer(googleMap.getMapModel().getMapView(), googleTerrainMap
				.getMapModel().getMapView());
		synchronizer.setEnabled(true);
		synchronizer2.setEnabled(true);
	}
	// @extract-end

	public String getDescription() {
		return MESSAGES.googleDescription();
	}

	public String[] getConfigurationFiles() {
		return new String[] { 
				"classpath:org/geomajas/layer/google/gwt/example/context/mapGoogle.xml",
				"classpath:org/geomajas/layer/google/gwt/example/context/mapGoogleSat.xml",
				"classpath:org/geomajas/layer/google/gwt/example/context/mapGoogleTerrain.xml",
				"classpath:org/geomajas/layer/google/gwt/example/context/layerGoogle.xml",
				"classpath:org/geomajas/layer/google/gwt/example/context/layerGoogleSat.xml",
				"classpath:org/geomajas/layer/google/gwt/example/context/layerGoogleTerrain.xml" };
	}

	@Override
	public String ensureUserLoggedIn() {
		return "luc";
	}
}
