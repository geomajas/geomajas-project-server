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

package org.geomajas.example.gwt.client.samples.mapwidget;

import org.geomajas.example.gwt.client.samples.base.SamplePanelFactory;
import org.geomajas.gwt.client.controller.PanController;
import org.geomajas.example.gwt.client.samples.base.SamplePanel;
import org.geomajas.example.gwt.client.samples.i18n.I18nProvider;
import org.geomajas.gwt.client.widget.MapWidget;

import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.layout.VLayout;

/**
 * Sample that shows two Google maps, one with normal, one satellite.
 * 
 * @author Joachim Van der Auwera
 */
public class GoogleSample extends SamplePanel {

	public static final String TITLE = "GoogleLayer";

	public static final SamplePanelFactory FACTORY = new SamplePanelFactory() {

		public SamplePanel createPanel() {
			return new GoogleSample();
		}
	};

	public Canvas getViewPanel() {
		VLayout layout = new VLayout();
		layout.setWidth100();
		layout.setHeight100();
		layout.setMembersMargin(10);

		// Create map with Google layer, and add a PanController to it:
		VLayout mapLayout1 = new VLayout();
		mapLayout1.setShowEdges(true);
		final MapWidget googleMap = new MapWidget("googleMap", "gwt-samples");
		googleMap.setController(new PanController(googleMap));
		mapLayout1.addMember(googleMap);

		// Create map with Google layer (satellite), and add a PanController to it:
		VLayout mapLayout2 = new VLayout();
		mapLayout2.setShowEdges(true);
		final MapWidget googleSatMap = new MapWidget("googleSatMap", "gwt-samples");
		googleSatMap.setController(new PanController(googleSatMap));
		mapLayout2.addMember(googleSatMap);

		// Place both in the layout:
		layout.addMember(mapLayout1);
		layout.addMember(mapLayout2);

		return layout;
	}

	public String getDescription() {
		return I18nProvider.getSampleMessages().googleDescription();
	}

	public String getSourceFileName() {
		return "classpath:org/geomajas/gwt/client/samples/mapwidget/GoogleSample.txt";
	}

	public String[] getConfigurationFiles() {
		return new String[] { "classpath:org/geomajas/gwt/samples/mapwidget/mapGoogle.xml",
				"classpath:org/geomajas/gwt/samples/mapwidget/layerGoogle.xml",
				"classpath:org/geomajas/gwt/samples/mapwidget/mapGoogleSat.xml",
				"classpath:org/geomajas/gwt/samples/mapwidget/layerGoogleSat.xml" };
	}

	public String ensureUserLoggedIn() {
		return "luc";
	}
}
