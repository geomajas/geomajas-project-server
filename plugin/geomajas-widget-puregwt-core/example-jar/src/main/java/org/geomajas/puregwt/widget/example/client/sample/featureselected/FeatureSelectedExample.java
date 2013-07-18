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
package org.geomajas.puregwt.widget.example.client.sample.featureselected;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.DecoratorPanel;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.ResizeLayoutPanel;
import com.google.gwt.user.client.ui.Widget;
import org.geomajas.geometry.Coordinate;
import org.geomajas.puregwt.client.event.MapInitializationEvent;
import org.geomajas.puregwt.client.event.MapInitializationHandler;
import org.geomajas.puregwt.client.map.MapPresenter;
import org.geomajas.puregwt.client.map.layer.Layer;
import org.geomajas.puregwt.example.base.client.ExampleBase;
import org.geomajas.puregwt.example.base.client.sample.SamplePanel;

/**
 * Class description.
 *
 * @author Dosi Bingov
 */
public class FeatureSelectedExample implements SamplePanel {

	protected DockLayoutPanel rootElement;

	private MapPresenter mapPresenter;

	@UiField
	protected ResizeLayoutPanel mapPanel;

	@Override
	public Widget asWidget() {
		return  rootElement;
	}

	/**
	 * UI binder interface.
	 *
	 * @author Dosi Bingov
	 */
	interface FeatureSelectedExampleUiBinder extends
			UiBinder<DockLayoutPanel, FeatureSelectedExample> {

	}

	private static final FeatureSelectedExampleUiBinder UIBINDER = GWT.create(FeatureSelectedExampleUiBinder.class);

	public FeatureSelectedExample() {
		rootElement = UIBINDER.createAndBindUi(this);

		mapPresenter = ExampleBase.getInjector().getMapPresenter();

		// Create the MapPresenter and add an InitializationHandler:
		mapPresenter = ExampleBase.getInjector().getMapPresenter();
		mapPresenter.setSize(480, 480);
		mapPresenter.getEventBus().addMapInitializationHandler(new MyMapInitializationHandler());
		//mapPresenter.getEventBus().addLayerVisibilityHandler(new MyLayerVisibilityHandler());

		// Define the whole layout:
		DecoratorPanel mapDecorator = new DecoratorPanel();
		mapDecorator.add(mapPresenter.asWidget());
		mapPanel.add(mapDecorator);

		// Initialize the map, and return the layout:
		mapPresenter.initialize("puregwt-widget-app", "mapLayerVisibility");
	}

	/**
	 * TODO: dont really need this
	 * Map initialization handler that adds a CheckBox to the layout for every layer. With these CheckBoxes, the user
	 * can toggle the layer's visibility.
	 *
	 * @author Pieter De Graef
	 */
	private class MyMapInitializationHandler implements MapInitializationHandler {

		public void onMapInitialized(MapInitializationEvent event) {
			// When the map initializes: add a CheckBox for every layer, so the use can toggle visibility:
			for (int i = 0; i < mapPresenter.getLayersModel().getLayerCount(); i++) {
				final Layer layer = mapPresenter.getLayersModel().getLayer(i);
				CheckBox layerCheck = new CheckBox(layer.getTitle());
				layerCheck.setValue(layer.isMarkedAsVisible());

				// When CheckBox value changes, change the layer's visibility as well:
				layerCheck.addValueChangeHandler(new ValueChangeHandler<Boolean>() {

					public void onValueChange(ValueChangeEvent<Boolean> event) {
						if (event.getValue() != null) {
							layer.setMarkedAsVisible(event.getValue());
						}
					}
				});

				// layerCheckBoxLayout.add(layerCheck);
			}

			// Now zoom in a lot, because the countries layer is not visible at high altitudes:
			double scale = mapPresenter.getViewPort().getScale();
			mapPresenter.getViewPort().applyPosition(new Coordinate(0, 7000000));
			mapPresenter.getViewPort().applyScale(scale * 64);
		}
	}
}