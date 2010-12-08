/*
 * This file is part of Geomajas, a component framework for building
 * rich Internet applications (RIA) with sophisticated capabilities for the
 * display, analysis and management of geographic information.
 * It is a building block that allows developers to add maps
 * and other geographic data capabilities to their web applications.
 *
 * Copyright 2008-2010 Geosparc, http://www.geosparc.com, Belgium
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
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
 * Sample that shows two Google maps, one with normal, one satellite, one with terrain.
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
		googleMap = new MapWidget("googleMap", "gwt-samples");
		googleMap.setController(new PanController(googleMap));
		mapLayout1.addMember(googleMap);

		// Create map with Google layer (satellite), and add a PanController to it:
		VLayout mapLayout2 = new VLayout();
		mapLayout2.setShowEdges(true);
		googleSatMap = new MapWidget("googleSatMap", "gwt-samples");
		googleSatMap.setController(new PanController(googleSatMap));
		mapLayout2.addMember(googleSatMap);

		// Create map with Google layer (terrain), and add a PanController to it:
		VLayout mapLayout3 = new VLayout();
		mapLayout3.setShowEdges(true);
		googleTerrainMap = new MapWidget("googleTerrainMap", "gwt-samples");
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
	protected void onDraw() {
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
