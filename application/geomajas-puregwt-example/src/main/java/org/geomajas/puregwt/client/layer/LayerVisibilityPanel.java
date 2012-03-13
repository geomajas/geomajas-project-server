/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2012 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */

package org.geomajas.puregwt.client.layer;

import org.geomajas.puregwt.client.ContentPanel;
import org.geomajas.puregwt.client.event.LayerHideEvent;
import org.geomajas.puregwt.client.event.LayerShowEvent;
import org.geomajas.puregwt.client.event.LayerVisibilityHandler;
import org.geomajas.puregwt.client.event.LayerVisibilityMarkedEvent;
import org.geomajas.puregwt.client.event.MapInitializationEvent;
import org.geomajas.puregwt.client.event.MapInitializationHandler;
import org.geomajas.puregwt.client.map.MapPresenter;
import org.geomajas.puregwt.client.map.layer.Layer;

import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.DecoratorPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * ContentPanel that demonstrates layer visibility.
 * 
 * @author Pieter De Graef
 */
public class LayerVisibilityPanel extends ContentPanel {

	private MapPresenter mapPresenter;

	private VerticalPanel layerCheckBoxLayout;

	private VerticalPanel layerEventLayout;

	public String getTitle() {
		return "Layer Visibility";
	}

	public String getDescription() {
		return "This example shows the events that accompany the toggling of a layer's visibility.<br/>Note that the" +
				" 'countries' layer has been configured to be visible only on medium scale lavels. When zooming too" +
				" far in or out, the countries layer will never be visible.";
	}

	public Widget getContentWidget() {
		// Define the left layout:
		VerticalPanel leftLayout = new VerticalPanel();
		leftLayout.setSize("220px", "100%");

		leftLayout.add(new HTML("<h3>Layers:</h3>"));
		layerCheckBoxLayout = new VerticalPanel();
		leftLayout.add(layerCheckBoxLayout);

		leftLayout.add(new HTML("<h3>Events:</h3>"));
		layerEventLayout = new VerticalPanel();
		leftLayout.add(layerEventLayout);

		// Create the MapPresenter and add an InitializationHandler:
		mapPresenter = getInjector().getMapPresenter();
		mapPresenter.setSize(640, 480);
		mapPresenter.getEventBus().addMapInitializationHandler(new MyMapInitializationHandler());
		mapPresenter.getEventBus().addLayerVisibilityHandler(new MyLayerVisibilityHandler());

		// Define the whole layout:
		HorizontalPanel layout = new HorizontalPanel();
		layout.add(leftLayout);
		DecoratorPanel mapDecorator = new DecoratorPanel();
		mapDecorator.add(mapPresenter.asWidget());
		layout.add(mapDecorator);

		// Initialize the map, and return the layout:
		mapPresenter.initialize("pure-gwt-app", "mapLayerVisibility");
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
				final Layer<?> layer = mapPresenter.getLayersModel().getLayer(i);
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