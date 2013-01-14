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

package org.geomajas.gwt.example.client.sample.layertree;

import com.google.gwt.core.client.GWT;
import org.geomajas.gwt.example.base.SamplePanel;
import org.geomajas.gwt.client.controller.PanController;
import org.geomajas.gwt.client.map.layer.Layer;
import org.geomajas.gwt.example.base.SamplePanelFactory;
import org.geomajas.gwt.client.widget.Legend;
import org.geomajas.gwt.client.widget.MapWidget;

import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;
import org.geomajas.gwt.example.client.sample.i18n.SampleMessages;

/**
 * <p>
 * Sample that shows how the legend reacts when layers become visible/invisible.
 * </p>
 * 
 * @author Pieter De Graef
 */
public class LegendSample extends SamplePanel {

	private static final SampleMessages MESSAGES = GWT.create(SampleMessages.class);

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
		final MapWidget map = new MapWidget("mapLegend", "gwtExample");
		map.setController(new PanController(map));
		mapLayout.addMember(map);

		IButton rasterButton = new IButton("Toggle Raster layer");
		rasterButton.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				Layer<?> layer = map.getMapModel().getLayer("clientLayerWms");
				layer.setVisible(!layer.isShowing());
			}
		});
		rasterButton.setWidth100();
		buttonLayout.addMember(rasterButton);

		IButton lineButton = new IButton("Toggle Line layer");
		lineButton.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				Layer<?> layer = map.getMapModel().getLayer("clientLayerRivers50m");
				layer.setVisible(!layer.isShowing());
			}
		});
		lineButton.setWidth100();
		buttonLayout.addMember(lineButton);

		IButton polygonButton = new IButton("Toggle Polygon layer");
		polygonButton.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				Layer<?> layer = map.getMapModel().getLayer("clientLayerCountries110m");
				layer.setVisible(!layer.isShowing());
			}
		});
		polygonButton.setWidth100();
		buttonLayout.addMember(polygonButton);

		IButton pointButton = new IButton("Toggle Point layer");
		pointButton.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				Layer<?> layer = map.getMapModel().getLayer("clientLayerPopulatedPlaces110m");
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
		return MESSAGES.legendDescription();
	}

	public String[] getConfigurationFiles() {
		return new String[] { "classpath:org/geomajas/gwt/example/context/mapLegend.xml",
				"classpath:org/geomajas/gwt/example/base/layerLakes110m.xml",
				"classpath:org/geomajas/gwt/example/base/layerRivers50m.xml",
				"classpath:org/geomajas/gwt/example/base/layerPopulatedPlaces110m.xml",
				"classpath:org/geomajas/gwt/example/base/layerWmsBluemarble.xml" };
	}

	public String ensureUserLoggedIn() {
		return "luc";
	}
}
