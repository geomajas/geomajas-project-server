/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2011 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */

package org.geomajas.gwt.example.client.samples.layertree;

import com.google.gwt.core.client.GWT;
import org.geomajas.gwt.example.base.SamplePanel;
import org.geomajas.gwt.example.base.SamplePanelFactory;
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
import org.geomajas.gwt.example.client.samples.i18n.SampleMessages;

/**
 * <p>
 * Sample allows the user to change the layer order.
 * </p>
 * 
 * @author Pieter De Graef
 */
public class LayerOrderSample extends SamplePanel {

	private static final SampleMessages MESSAGES = GWT.create(SampleMessages.class);

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
		final MapWidget map = new MapWidget("mapLegend", "gwt-samples");
		map.setController(new PanController(map));
		mapLayout.addMember(map);

		// Layer order panel:
		VLayout orderLayout = new VLayout(10);
		orderLayout.setHeight(80);
		orderLayout.setShowEdges(true);

		orderLayout.addMember(new HTMLFlow(MESSAGES.layerOrderTxt()));

		HLayout buttonLayout = new HLayout(5);
		buttonLayout.setPadding(10);

		IButton upButton = new IButton(MESSAGES.layerOrderUpBtn());
		upButton.setWidth(150);
		upButton.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				VectorLayer layer = (VectorLayer) map.getMapModel().getLayer("clientLayerCountries110m");
				map.getMapModel().moveVectorLayerUp(layer);
			}
		});
		buttonLayout.addMember(upButton);
		IButton downutton = new IButton(MESSAGES.layerOrderDownBtn());
		downutton.setWidth(150);
		downutton.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				VectorLayer layer = (VectorLayer) map.getMapModel().getLayer("clientLayerCountries110m");
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
		return MESSAGES.layerOrderDescription();
	}

	public String getSourceFileName() {
		return "classpath:org/geomajas/example/gwt/client/samples/layertree/LayerOrderSample.txt";
	}

	public String[] getConfigurationFiles() {
		return new String[] { "WEB-INF/mapLegend.xml", "WEB-INF/layerLakes110m.xml",
				"WEB-INF/layerRivers50m.xml", "WEB-INF/layerPopulatedPlaces110m.xml", 
				"WEB-INF/layerWmsBluemarble.xml" };
	}

	public String ensureUserLoggedIn() {
		return "luc";
	}
}
