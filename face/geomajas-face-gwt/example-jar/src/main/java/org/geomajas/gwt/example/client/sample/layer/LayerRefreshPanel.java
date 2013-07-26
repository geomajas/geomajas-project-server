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

package org.geomajas.gwt.example.client.sample.layer;

import org.geomajas.gwt.client.event.LayerRefreshedEvent;
import org.geomajas.gwt.client.event.LayerRefreshedHandler;
import org.geomajas.gwt.client.event.MapInitializationEvent;
import org.geomajas.gwt.client.event.MapInitializationHandler;
import org.geomajas.gwt.client.map.MapPresenter;
import org.geomajas.gwt.client.map.layer.Layer;
import org.geomajas.gwt.example.base.client.ExampleBase;
import org.geomajas.gwt.example.base.client.sample.SamplePanel;
import org.geomajas.gwt.example.client.ExampleJar;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DecoratorPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ResizeLayoutPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * ContentPanel that demonstrates layer refreshing.
 * 
 * @author Pieter De Graef
 */
public class LayerRefreshPanel implements SamplePanel {

	/**
	 * UI binder for this widget.
	 * 
	 * @author Pieter De Graef
	 */
	interface MyUiBinder extends UiBinder<Widget, LayerRefreshPanel> {
	}

	private static final MyUiBinder UI_BINDER = GWT.create(MyUiBinder.class);

	private MapPresenter mapPresenter;

	@UiField
	protected VerticalPanel layerBtnLayout;

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
		mapPresenter.getEventBus().addLayerRefreshedHandler(new MyLayerRefreshedHandler());

		// Define the whole layout:
		DecoratorPanel mapDecorator = new DecoratorPanel();
		mapDecorator.add(mapPresenter.asWidget());
		mapPanel.add(mapDecorator);

		// Initialize the map, and return the layout:
		mapPresenter.initialize("puregwt-app", "mapCountries");
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
			// When the map initializes, add a refresh button for every layer:
			for (int i = 0; i < mapPresenter.getLayersModel().getLayerCount(); i++) {
				final Layer layer = mapPresenter.getLayersModel().getLayer(i);
				// mapPresenter.getLayersModelRenderer().setLayerAnimation(layer, true);
				layerBtnLayout.add(new LayerWidget(layer));
			}

			// Now zoom in a lot, because the countries layer is not visible at high altitudes:
			mapPresenter.getViewPort().applyBounds(ExampleBase.BBOX_ITALY);
		}
	}

	/**
	 * Handler that catches layer refreshed events and prints them out.
	 * 
	 * @author Pieter De Graef
	 */
	private class MyLayerRefreshedHandler implements LayerRefreshedHandler {

		public void onLayerRefreshed(LayerRefreshedEvent event) {
			layerEventLayout.add(new Label("Layer refreshed: " + event.getLayer().getTitle()));
		}
	}

	/**
	 * Layer representation on the GUI.
	 * 
	 * @author Pieter De Graef
	 */
	private final class LayerWidget extends HorizontalPanel {

		private LayerWidget(final Layer layer) {
			setWidth("100%");
			Button removeBtn = new Button("Refresh");
			removeBtn.addClickHandler(new ClickHandler() {

				public void onClick(ClickEvent event) {
					layer.refresh();
				}
			});
			add(removeBtn);
			add(new Label(layer.getTitle()));
			setStyleName(ExampleBase.getShowcaseResource().css().sampleRow());
		}
	}
}