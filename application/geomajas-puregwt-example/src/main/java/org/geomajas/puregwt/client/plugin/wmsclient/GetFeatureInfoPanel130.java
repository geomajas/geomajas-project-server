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

import java.util.List;

import org.geomajas.geometry.Bbox;
import org.geomajas.geometry.Coordinate;
import org.geomajas.plugin.wmsclient.client.controller.WmsGetFeatureInfoController;
import org.geomajas.plugin.wmsclient.client.layer.FeaturesSupportedWmsLayer;
import org.geomajas.plugin.wmsclient.client.layer.WmsLayerConfiguration;
import org.geomajas.plugin.wmsclient.client.layer.WmsTileConfiguration;
import org.geomajas.plugin.wmsclient.client.service.WmsService.GetFeatureInfoFormat;
import org.geomajas.plugin.wmsclient.client.service.WmsService.WmsVersion;
import org.geomajas.puregwt.client.ContentPanel;
import org.geomajas.puregwt.client.Showcase;
import org.geomajas.puregwt.client.event.MapInitializationEvent;
import org.geomajas.puregwt.client.event.MapInitializationHandler;
import org.geomajas.puregwt.client.map.MapPresenter;
import org.geomajas.puregwt.client.map.feature.Feature;
import org.geomajas.puregwt.widget.client.map.ResizableMapLayout;

import com.google.gwt.core.client.Callback;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * ...
 * 
 * @author Pieter De Graef
 */
public class GetFeatureInfoPanel130 extends ContentPanel {

	/**
	 * UI binder interface for this panel.
	 * 
	 * @author Pieter De Graef
	 */
	interface MyUiBinder extends UiBinder<Widget, GetFeatureInfoPanel130> {
	}

	private static final MyUiBinder UI_BINDER = GWT.create(MyUiBinder.class);

	private FeaturesSupportedWmsLayer wmsLayer;

	private WmsGetFeatureInfoController controller;

	@UiField
	protected RadioButton optionHtml;

	@UiField
	protected RadioButton optionGeom;

	@UiField
	protected HTML htmlDescription;

	@UiField
	protected SimplePanel mapPanel;

	public GetFeatureInfoPanel130(MapPresenter mapPresenter) {
		super(mapPresenter);
	}

	@Override
	public String getTitle() {
		return "WMS 1.3.0 - GetFeatureInfo";
	}

	@Override
	public String getDescription() {
		return "This showcase demonstrates the use of the WMS GetFeatureInfo request.";
	}

	@Override
	public Widget getContentWidget() {
		// Define the whole layout:
		Widget widget = UI_BINDER.createAndBindUi(this);
		mapPanel.add(new ResizableMapLayout(mapPresenter));

		// Initialize the map:
		mapPresenter.initialize("puregwt-app", "mapEmpty");

		// Add a WMS layer when the map has been initialized:
		mapPresenter.getEventBus().addMapInitializationHandler(new MapInitializationHandler() {

			public void onMapInitialized(MapInitializationEvent event) {				
				mapPresenter.getViewPort().applyBounds(new Bbox(-140, 18, 80, 40));

				// Create WMS layer configuration:
				WmsLayerConfiguration wmsConfig = new WmsLayerConfiguration();
				wmsConfig.setLayers("states");
				wmsConfig.setVersion(WmsVersion.v1_3_0);
				wmsConfig.setBaseUrl("http://apps.geomajas.org/geoserver/wms");

				Coordinate tileOrigin = new Coordinate(mapPresenter.getViewPort().getMaximumBounds().getX(),
						mapPresenter.getViewPort().getMaximumBounds().getY());
				WmsTileConfiguration tileConfig = new WmsTileConfiguration(256, 256, tileOrigin);

				// Create the WMS layer and add it to the map:
				wmsLayer = Showcase.GEOMAJASINJECTOR.getWmsLayerFactory().createFeaturesSupportedWmsLayer("states",
						wmsConfig, tileConfig);
				mapPresenter.getLayersModel().addLayer(wmsLayer);

				// Now install a controller for GetFeatureInfo requests:
				controller = new WmsGetFeatureInfoController(wmsLayer);
				controller.setGmlCallback(new GetFeatureInfoGmlCallback());
				controller.setHtmlCallback(new GetFeatureInfoHtmlCallback());
				mapPresenter.addMapListener(controller); // Add as listener as it should not interfere with navigation.
			}
		});
		return widget;
	}

	@UiHandler("optionHtml")
	public void handleGetFeatureInfoAsHtml(final ValueChangeEvent<Boolean> event) {
		controller.setFormat(GetFeatureInfoFormat.HTML);
	}

	@UiHandler("optionGeom")
	public void handleGetFeatureInfoAsGml(final ValueChangeEvent<Boolean> event) {
		controller.setFormat(GetFeatureInfoFormat.GML2);
	}

	private class GetFeatureInfoGmlCallback implements Callback<List<Feature>, String> {

		public void onFailure(String reason) {
			Window.alert("GetFeatureInfo has failed: " + reason);
		}

		public void onSuccess(List<Feature> result) {
			wmsLayer.clearSelectedFeatures();
			for (Feature feature : result) {
				wmsLayer.selectFeature(feature);
			}
		}
	}

	private class GetFeatureInfoHtmlCallback implements Callback<String, String> {

		public void onFailure(String reason) {
			Window.alert("GetFeatureInfo has failed: " + reason);
		}

		public void onSuccess(String result) {
			wmsLayer.clearSelectedFeatures();
			htmlDescription.setHTML(result);
		}
	}
}