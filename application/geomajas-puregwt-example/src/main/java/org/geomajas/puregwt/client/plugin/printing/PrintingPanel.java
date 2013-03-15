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
package org.geomajas.puregwt.client.plugin.printing;

import org.geomajas.geometry.Coordinate;
import org.geomajas.plugin.printing.client.widget.PrintPanel;
import org.geomajas.plugin.wmsclient.client.layer.WmsLayer;
import org.geomajas.plugin.wmsclient.client.layer.WmsLayerConfiguration;
import org.geomajas.plugin.wmsclient.client.layer.WmsTileConfiguration;
import org.geomajas.plugin.wmsclient.client.service.WmsService.WmsVersion;
import org.geomajas.plugin.wmsclient.printing.client.WmsLayerBuilder;
import org.geomajas.puregwt.client.ContentPanel;
import org.geomajas.puregwt.client.Showcase;
import org.geomajas.puregwt.client.event.MapInitializationEvent;
import org.geomajas.puregwt.client.event.MapInitializationHandler;
import org.geomajas.puregwt.client.map.MapPresenter;
import org.geomajas.puregwt.client.widget.MapLayoutPanel;
import org.geomajas.puregwt.widget.client.gadget.LegendDropDownGadget;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * Panel for printing sample.
 * 
 * @author Jan De Moerloose
 * 
 */
public class PrintingPanel extends ContentPanel {

	/**
	 * UI binder interface for this panel.
	 * 
	 * @author Jan De Moerloose
	 */
	interface MyUiBinder extends UiBinder<Widget, PrintingPanel> {
	}

	private static final MyUiBinder UI_BINDER = GWT.create(MyUiBinder.class);

	@UiField
	protected SimplePanel printPanel;

	@UiField
	protected MapLayoutPanel mapPanel;

	public PrintingPanel(MapPresenter mapPresenter) {
		super(mapPresenter);
	}

	@Override
	public String getTitle() {
		return "Printing";
	}

	@Override
	public String getDescription() {
		return "Demonstrates printing capabilities";
	}

	@Override
	public Widget getContentWidget() {
		Widget widget = UI_BINDER.createAndBindUi(this);
		// Initialize the map, and return the layout:
		mapPresenter.initialize("puregwt-app", "mapPrinting");
		mapPresenter.getEventBus().addMapInitializationHandler(new MapInitializationHandler() {

			public void onMapInitialized(MapInitializationEvent event) {
				WmsLayerConfiguration wmsConfig = new WmsLayerConfiguration();
				wmsConfig.setFormat("image/png");
				wmsConfig.setLayers("osm");
				wmsConfig.setVersion(WmsVersion.V1_1_1);
				wmsConfig.setBaseUrl("http://apps.geomajas.org/geoserver/wms");

				Coordinate tileOrigin = new Coordinate(mapPresenter.getViewPort().getMaximumBounds().getX(),
						mapPresenter.getViewPort().getMaximumBounds().getY());
				WmsTileConfiguration tileConfig = new WmsTileConfiguration(256, 256, tileOrigin);

				WmsLayer wmsLayer = Showcase.GEOMAJASINJECTOR.getWmsLayerFactory().createWmsLayer("osm",
						wmsConfig, tileConfig);
				mapPresenter.getLayersModel().addLayer(wmsLayer);
			}
		});
		mapPresenter.addMapGadget(new LegendDropDownGadget());
		mapPanel.setPresenter(mapPresenter);
		PrintPanel panel = new PrintPanel(mapPresenter, "puregwt-app");
		panel.getMapBuilder().registerLayerBuilder(new WmsLayerBuilder());
		printPanel.setWidget(panel);
		return widget;
	}

}
