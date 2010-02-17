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

package org.geomajas.gwt.client.samples.mapwidget;

import org.geomajas.geometry.Coordinate;
import org.geomajas.gwt.client.controller.PanController;
import org.geomajas.gwt.client.map.MapView.ZoomOption;
import org.geomajas.gwt.client.samples.base.SamplePanel;
import org.geomajas.gwt.client.samples.base.SamplePanelFactory;
import org.geomajas.gwt.client.samples.i18n.I18nProvider;
import org.geomajas.gwt.client.spatial.Bbox;
import org.geomajas.gwt.client.widget.MapWidget;

import com.smartgwt.client.widgets.Button;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.events.DrawEvent;
import com.smartgwt.client.widgets.events.DrawHandler;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;

/**
 * <p>
 * Sample that shows a map with an OpenStreetMap layer and a list of navigation buttons, testing the MapView.
 * </p>
 * 
 * @author Pieter De Graef
 */
public class NavigationSample extends SamplePanel {

	public static final String TITLE = "MapViewNavigation";

	public static final SamplePanelFactory FACTORY = new SamplePanelFactory() {

		public SamplePanel createPanel() {
			return new NavigationSample();
		}
	};

	public Canvas getViewPanel() {
		VLayout layout = new VLayout();
		layout.setWidth100();
		layout.setHeight100();
		layout.setMembersMargin(10);
		
		HLayout hLayout = new HLayout();
		hLayout.setMembersMargin(15);
		hLayout.setHeight(200);
		hLayout.setPadding(5);
		hLayout.setShowEdges(true);
		VLayout firstColumn = new VLayout();
		firstColumn.setMembersMargin(5);
		VLayout secondColumn = new VLayout();
		secondColumn.setMembersMargin(5);

		// Map with ID osmMap is defined in the XML configuration. (mapOsm.xml)
		final MapWidget map = new MapWidget("osmMap", "gwt-samples");
		map.addDrawHandler(new DrawHandler() {

			public void onDraw(DrawEvent event) {
				map.initialize();
			}
		});

		// Set a panning controller on the map:
		map.setController(new PanController(map));

		// Create a button that centers the map to (0,0):
		Button centerBTN = new Button("center to (0,0)");
		centerBTN.setWidth(120);
		centerBTN.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				map.getMapModel().getMapView().setCenterPosition(new Coordinate(0, 0));
			}
		});

		// Create a button that translate the map:
		Button translateBTN = new Button("translate by (10, -5)");
		translateBTN.setWidth(120);
		translateBTN.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				map.getMapModel().getMapView().translate(10, -5);
			}
		});

		// Create a button that applies a bounding box to zoom to:
		Button bboxBTN = new Button("Apply bbox");
		bboxBTN.setWidth(120);
		translateBTN.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				map.getMapModel().getMapView().applyBounds(new Bbox(10, 10, 40, 40), ZoomOption.EXACT);
			}
		});

		// Create a button that zooms out:
		Button zoomOutBTN = new Button("Zoom out");
		zoomOutBTN.setWidth(120);
		zoomOutBTN.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				map.getMapModel().getMapView().scale(2, ZoomOption.LEVEL_CHANGE);
			}
		});

		// Create a button that zooms in:
		Button zoomInBTN = new Button("Zoom in");
		zoomInBTN.setWidth(120);
		zoomInBTN.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				map.getMapModel().getMapView().scale(2, ZoomOption.LEVEL_CHANGE);
			}
		});

		firstColumn.addMember(centerBTN);
		firstColumn.addMember(translateBTN);
		firstColumn.addMember(bboxBTN);
		
		secondColumn.addMember(zoomOutBTN);
		secondColumn.addMember(zoomInBTN);
		
		hLayout.addMember(firstColumn);
		hLayout.addMember(secondColumn);
		layout.addMember(hLayout);
		layout.addMember(map);

		return layout;
	}

	public String getDescription() {
		return I18nProvider.getSampleMessages().navigationDescription();
	}

	public String[] getConfigurationFiles() {
		return new String[] { "/org/geomajas/gwt/samples/mapwidget/layerOsm.xml",
				"/org/geomajas/gwt/samples/mapwidget/mapOsm.xml" };
	}

	public String ensureUserLoggedIn() {
		return "luc";
	}
}
