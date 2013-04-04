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

package org.geomajas.puregwt.client.plugin.wmsclient;

import java.io.IOException;

import org.geomajas.geometry.Coordinate;
import org.geomajas.plugin.wmsclient.client.capabilities.WmsGetCapabilitiesInfo;
import org.geomajas.plugin.wmsclient.client.capabilities.WmsLayerInfo;
import org.geomajas.plugin.wmsclient.client.layer.WmsLayer;
import org.geomajas.plugin.wmsclient.client.layer.WmsLayerConfiguration;
import org.geomajas.plugin.wmsclient.client.layer.WmsTileConfiguration;
import org.geomajas.plugin.wmsclient.client.service.WmsService;
import org.geomajas.plugin.wmsclient.client.service.WmsService.WmsRequest;
import org.geomajas.plugin.wmsclient.client.service.WmsService.WmsUrlTransformer;
import org.geomajas.plugin.wmsclient.client.service.WmsService.WmsVersion;
import org.geomajas.puregwt.client.ContentPanel;
import org.geomajas.puregwt.client.Showcase;
import org.geomajas.puregwt.client.event.MapInitializationEvent;
import org.geomajas.puregwt.client.event.MapInitializationHandler;
import org.geomajas.puregwt.client.map.MapPresenter;
import org.geomajas.puregwt.client.widget.MapLayoutPanel;
import org.geomajas.puregwt.widget.client.map.LegendDropDownWidget;

import com.google.gwt.core.client.Callback;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.text.shared.Renderer;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiFactory;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.ValueListBox;
import com.google.gwt.user.client.ui.Widget;

/**
 * ...
 * 
 * @author Pieter De Graef
 */
public class GetCapabilitiesPanel130 extends ContentPanel {

	private static final String DEFAULT_CAPA_URL = "http://apps.geomajas.org/geoserver/wms";

	/**
	 * UI binder interface for this panel.
	 * 
	 * @author Pieter De Graef
	 */
	interface MyUiBinder extends UiBinder<Widget, GetCapabilitiesPanel130> {
	}

	private static final MyUiBinder UI_BINDER = GWT.create(MyUiBinder.class);

	@UiField
	protected TextBox urlBox;

	@UiField
	protected ValueListBox<WmsLayerInfo> layerBox;

	@UiField
	protected SimplePanel mapPanel;

	private WmsService wmsService;

	public GetCapabilitiesPanel130(MapPresenter mapPresenter) {
		super(mapPresenter);
	}

	@Override
	public String getTitle() {
		return "WMS 1.3.0 - GetCapabilities";
	}

	@Override
	public String getDescription() {
		return "This showcase demonstrates WMS 1.3.0 GetCapabilities parsing. Once a capabilities file has been"
				+ " parsed, the full list of layers is added to a list box. From there they can be added to the map.";
	}

	@Override
	public Widget getContentWidget() {
		Widget widget = UI_BINDER.createAndBindUi(this);
		urlBox.setText(DEFAULT_CAPA_URL);

		// Define the whole layout:
		MapLayoutPanel mapLayoutPanel = new MapLayoutPanel(mapPresenter);

		// Initialize the map, and return the layout:
		mapPresenter.initialize("puregwt-app", "mapEmpty");
		mapPresenter.getEventBus().addMapInitializationHandler(new MapInitializationHandler() {

			public void onMapInitialized(MapInitializationEvent event) {
				WmsLayerConfiguration wmsConfig = new WmsLayerConfiguration();
				wmsConfig.setFormat("image/jpeg");
				wmsConfig.setLayers("bluemarble");
				wmsConfig.setVersion(WmsVersion.V1_3_0);
				wmsConfig.setBaseUrl(DEFAULT_CAPA_URL);

				Coordinate tileOrigin = new Coordinate(mapPresenter.getViewPort().getMaximumBounds().getX(),
						mapPresenter.getViewPort().getMaximumBounds().getY());
				WmsTileConfiguration tileConfig = new WmsTileConfiguration(256, 256, tileOrigin);

				WmsLayer wmsLayer = Showcase.GEOMAJASINJECTOR.getWmsLayerFactory().createWmsLayer("bluemarble",
						wmsConfig, tileConfig);
				mapPresenter.getLayersModel().addLayer(wmsLayer);
			}
		});
		mapPresenter.getWidgetPane().add(new LegendDropDownWidget());
		mapPanel.setWidget(mapLayoutPanel);

		// Get the WmsService singleton:
		wmsService = Showcase.GEOMAJASINJECTOR.getWmsService();

		// Apply a URL transformer on the WMS Service to make sure it uses a proxy servlet for GetCapabilities and
		// GetFeatureInfo requests:
		wmsService.setWmsUrlTransformer(new WmsUrlTransformer() {

			public String transform(WmsRequest request, String url) {
				switch (request) {
					case GETCAPABILITIES:  /* Not for GETFEATUREINFO ! */
						return "proxy?url=" + url;
					default:
				}
				return url;
			}
		});

		// Parse GetCapabilities on startup:
		onSearchButtonClicked(null);

		return widget;
	}

	/**
	 * Executed when the search button is clicked. Get the WMS GetCapabilities file, and parse it...
	 * 
	 * @param event
	 *            Not used.
	 */
	@UiHandler("searchButton")
	protected void onSearchButtonClicked(ClickEvent event) {
		String url = urlBox.getText();
		wmsService.getCapabilities(url, WmsVersion.V1_3_0, new Callback<WmsGetCapabilitiesInfo, String>() {

			public void onFailure(String reason) {
				Window.alert(reason);
			}

			public void onSuccess(WmsGetCapabilitiesInfo result) {
				// Add the list of layers to the drop down box:
				layerBox.setAcceptableValues(result.getLayers());
			}
		});
	}

	@UiHandler("addButton")
	protected void onAddButtonClicked(ClickEvent event) {
		WmsLayerInfo layerInfo = layerBox.getValue();

		WmsLayerConfiguration wmsConfig = new WmsLayerConfiguration();
		wmsConfig.setBaseUrl(urlBox.getValue());
		wmsConfig.setLayers(layerInfo.getName());
		wmsConfig.setTransparent(true);
		wmsConfig.setVersion(WmsVersion.V1_3_0);

		Coordinate origin = new Coordinate(layerInfo.getBoundingBox().getX(), layerInfo.getBoundingBox().getY());
		WmsTileConfiguration tileConfig = new WmsTileConfiguration(256, 256, origin);

		// Create a WMS layer and add it to the map:
		WmsLayer layer = Showcase.GEOMAJASINJECTOR.getWmsLayerFactory().createWmsLayer(layerInfo.getTitle(), wmsConfig,
				tileConfig);
		mapPresenter.getLayersModel().addLayer(layer);

		// Make sure we enable animation for our newly add layer - it's just nicer:
		mapPresenter.getMapRenderer().setNrAnimatedLayers(mapPresenter.getLayersModel().getLayerCount());
	}

	@UiFactory
	protected ValueListBox<WmsLayerInfo> createLayerBox() {
		return new LayerBox();
	}

	/**
	 * List box extension for WMS layer info objects.
	 * 
	 * @author Pieter De Graef
	 */
	private class LayerBox extends ValueListBox<WmsLayerInfo> {

		public LayerBox() {
			super(new Renderer<WmsLayerInfo>() {

				public String render(WmsLayerInfo layer) {
					if (layer != null) {
						return layer.getTitle();
					}
					return "";
				}

				public void render(WmsLayerInfo layer, Appendable appendable) throws IOException {
					appendable.append(render(layer));
				}
			});
		}
	}
}