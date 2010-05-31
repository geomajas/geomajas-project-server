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

package org.geomajas.example.gwt.client.samples.layertree;

import org.geomajas.example.gwt.client.samples.base.SamplePanel;
import org.geomajas.gwt.client.controller.PanController;
import org.geomajas.gwt.client.map.layer.Layer;
import org.geomajas.example.gwt.client.samples.base.SamplePanelFactory;
import org.geomajas.example.gwt.client.samples.i18n.I18nProvider;
import org.geomajas.gwt.client.widget.Legend;
import org.geomajas.gwt.client.widget.MapWidget;

import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;

/**
 * <p>
 * Sample that shows how the legend reacts when layers become visible/invisible.
 * </p>
 * 
 * @author Pieter De Graef
 */
public class LegendSample extends SamplePanel {

	public static final String TITLE = "LegendSample";

	public static final SamplePanelFactory FACTORY = new SamplePanelFactory() {

		public SamplePanel createPanel() {
			return new LegendSample();
		}
	};

	public Canvas getViewPanel() {
		VLayout mainLayout = new VLayout();
		mainLayout.setWidth100();
		mainLayout.setHeight100();
		mainLayout.setMembersMargin(10);

		HLayout topLayout = new HLayout();
		topLayout.setMembersMargin(10);
		topLayout.setHeight(190);

		VLayout buttonLayout = new VLayout();
		buttonLayout.setMembersMargin(10);
		buttonLayout.setPadding(10);
		buttonLayout.setShowEdges(true);

		VLayout mapLayout = new VLayout();
		mapLayout.setShowEdges(true);
		final MapWidget map = new MapWidget("legendMap", "gwt-samples");
		map.setController(new PanController(map));
		mapLayout.addMember(map);

		IButton rasterButton = new IButton("Toggle Raster layer");
		rasterButton.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				Layer<?> layer = map.getMapModel().getLayer("wmsLayer");
				layer.setVisible(!layer.isShowing());
			}
		});
		rasterButton.setWidth100();
		buttonLayout.addMember(rasterButton);

		IButton lineButton = new IButton("Toggle Line layer");
		lineButton.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				Layer<?> layer = map.getMapModel().getLayer("rivers50mLayer");
				layer.setVisible(!layer.isShowing());
			}
		});
		lineButton.setWidth100();
		buttonLayout.addMember(lineButton);

		IButton polygonButton = new IButton("Toggle Polygon layer");
		polygonButton.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				Layer<?> layer = map.getMapModel().getLayer("countries110mLayer");
				layer.setVisible(!layer.isShowing());
			}
		});
		polygonButton.setWidth100();
		buttonLayout.addMember(polygonButton);

		IButton pointButton = new IButton("Toggle Point layer");
		pointButton.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				Layer<?> layer = map.getMapModel().getLayer("populatedPlaces110mLayer");
				layer.setVisible(!layer.isShowing());
			}
		});
		pointButton.setWidth100();
		buttonLayout.addMember(pointButton);

		VLayout legendLayout = new VLayout();
		legendLayout.setShowEdges(true);

		final Legend legend = new Legend(map.getMapModel());
		legend.setHeight100();
		legend.setWidth100();
		legendLayout.addMember(legend);

		topLayout.addMember(buttonLayout);
		topLayout.addMember(legendLayout);

		mainLayout.addMember(topLayout);
		mainLayout.addMember(mapLayout);
		return mainLayout;
	}

	public String getDescription() {
		return I18nProvider.getSampleMessages().legendDescription();
	}

	public String getSourceFileName() {
		return "classpath:org/geomajas/example/gwt/client/samples/layertree/LegendSample.txt";
	}

	public String[] getConfigurationFiles() {
		return new String[] { "classpath:org/geomajas/example/gwt/clientcfg/layertree/mapLegend.xml",
				"classpath:org/geomajas/example/gwt/servercfg/vector/layerLakes110m.xml",
				"classpath:org/geomajas/example/gwt/servercfg/vector/layerRivers50m.xml",
				"classpath:org/geomajas/example/gwt/servercfg/vector/layerPopulatedPlaces110m.xml",
				"classpath:org/geomajas/example/gwt/servercfg/raster/layerWmsBluemarble.xml" };
	}

	public String ensureUserLoggedIn() {
		return "luc";
	}
}
