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
import org.geomajas.example.gwt.client.samples.base.SamplePanelFactory;
import org.geomajas.example.gwt.client.samples.i18n.I18nProvider;
import org.geomajas.gwt.client.controller.PanController;
import org.geomajas.gwt.client.map.layer.VectorLayer;
import org.geomajas.gwt.client.widget.MapWidget;

import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.HTMLFlow;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;

/**
 * <p>
 * Sample allows the user to change the layer order.
 * </p>
 * 
 * @author Pieter De Graef
 */
public class LayerOrderSample extends SamplePanel {

	public static final String TITLE = "LAYERORDER";

	public static final SamplePanelFactory FACTORY = new SamplePanelFactory() {

		public SamplePanel createPanel() {
			return new LayerOrderSample();
		}
	};

	public Canvas getViewPanel() {
		VLayout mainLayout = new VLayout();
		mainLayout.setWidth100();
		mainLayout.setHeight100();

		// Build a map, and set a PanController:
		VLayout mapLayout = new VLayout();
		mapLayout.setShowEdges(true);
		final MapWidget map = new MapWidget("legendMap", "gwt-samples");
		map.setController(new PanController(map));
		mapLayout.addMember(map);

		// Layer order panel:
		VLayout orderLayout = new VLayout(10);
		orderLayout.setHeight(80);
		orderLayout.setShowEdges(true);

		orderLayout.addMember(new HTMLFlow(I18nProvider.getSampleMessages().layerOrderTxt()));

		HLayout buttonLayout = new HLayout(5);
		buttonLayout.setPadding(10);

		IButton upButton = new IButton(I18nProvider.getSampleMessages().layerOrderUpBtn());
		upButton.setWidth(150);
		upButton.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				VectorLayer layer = (VectorLayer) map.getMapModel().getLayer("countries110mLayer");
				map.getMapModel().moveVectorLayerUp(layer);
			}
		});
		buttonLayout.addMember(upButton);
		IButton downutton = new IButton(I18nProvider.getSampleMessages().layerOrderDownBtn());
		downutton.setWidth(150);
		downutton.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				VectorLayer layer = (VectorLayer) map.getMapModel().getLayer("countries110mLayer");
				map.getMapModel().moveVectorLayerDown(layer);
			}
		});
		buttonLayout.addMember(downutton);
		orderLayout.addMember(buttonLayout);
		orderLayout.setShowResizeBar(true);

		// Add both to the main layout:
		mainLayout.addMember(orderLayout);
		mainLayout.addMember(mapLayout);

		return mainLayout;
	}

	public String getDescription() {
		return I18nProvider.getSampleMessages().layerOrderDescription();
	}

	public String getSourceFileName() {
		return "classpath:org/geomajas/example/gwt/client/samples/layertree/LayerOrderSample.txt";
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
