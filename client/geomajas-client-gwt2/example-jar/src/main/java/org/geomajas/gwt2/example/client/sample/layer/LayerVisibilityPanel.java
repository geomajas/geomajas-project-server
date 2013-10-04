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

package org.geomajas.gwt2.example.client.sample.layer;

import org.geomajas.gwt2.client.event.LayerHideEvent;
import org.geomajas.gwt2.client.event.LayerShowEvent;
import org.geomajas.gwt2.client.event.LayerVisibilityHandler;
import org.geomajas.gwt2.client.event.LayerVisibilityMarkedEvent;
import org.geomajas.gwt2.client.event.MapInitializationEvent;
import org.geomajas.gwt2.client.event.MapInitializationHandler;
import org.geomajas.gwt2.client.map.MapPresenter;
import org.geomajas.gwt2.client.map.layer.Layer;
import org.geomajas.gwt2.example.base.client.ExampleBase;
import org.geomajas.gwt2.example.base.client.sample.SamplePanel;
import org.geomajas.gwt2.example.client.ExampleJar;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.DecoratorPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ResizeLayoutPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * ContentPanel that demonstrates layer visibility.
 * 
 * @author Pieter De Graef
 */
public class LayerVisibilityPanel implements SamplePanel {

	/**
	 * UI binder for this widget.
	 * 
	 * @author Pieter De Graef
	 */
	interface MyUiBinder extends UiBinder<Widget, LayerVisibilityPanel> {
	}

	private static final MyUiBinder UI_BINDER = GWT.create(MyUiBinder.class);

	private MapPresenter mapPresenter;

	@UiField
	protected VerticalPanel layerCheckBoxLayout;

	@UiField
	protected VerticalPanel layerEventLayout;

	@UiField
	protected ResizeLayoutPanel mapPanel;

	public Widget asWidget() {
		Widget layout = UI_BINDER.createAndBindUi(this);

		// Create the MapPresenter and add an InitializationHandler:
		mapPresenter = ExampleJar.getInjector().getMapPresenter();
		mapPresenter.setSize(480, 480);
		mapPresenter.getEventBus().addMapInitializationHandler(new MyMapInitializationHandler());
		mapPresenter.getEventBus().addLayerVisibilityHandler(new MyLayerVisibilityHandler());

		// Define the whole layout:
		DecoratorPanel mapDecorator = new DecoratorPanel();
		mapDecorator.add(mapPresenter.asWidget());
		mapPanel.add(mapDecorator);

		// Initialize the map, and return the layout:
		mapPresenter.initialize("gwt-app", "mapLayerVisibility");
		return layout;
	}

	/**
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
				layerCheckBoxLayout.add(layerCheck);
			}

			// Now zoom in a lot, because the countries layer is not visible at high altitudes:
			mapPresenter.getViewPort().applyBounds(ExampleBase.BBOX_ITALY);
		}
	}

	/**
	 * Handler that catches layer visibility events and prints them out.
	 * 
	 * @author Pieter De Graef
	 */
	private class MyLayerVisibilityHandler implements LayerVisibilityHandler {

		public void onShow(LayerShowEvent event) {
			layerEventLayout.add(new Label("onShow: " + event.getLayer().getTitle()));
		}

		public void onHide(LayerHideEvent event) {
			layerEventLayout.add(new Label("onHide: " + event.getLayer().getTitle()));
		}

		public void onVisibilityMarked(LayerVisibilityMarkedEvent event) {
			layerEventLayout.add(new Label("onVisibilityMarked: " + event.getLayer().getTitle()));
		}
	}
}