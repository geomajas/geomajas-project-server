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

package org.geomajas.gwt.widget.example.client.sample.map;


import org.geomajas.gwt.client.GeomajasGinjector;
import org.geomajas.gwt.client.event.MapInitializationEvent;
import org.geomajas.gwt.client.event.MapInitializationHandler;
import org.geomajas.gwt.client.map.MapPresenter;
import org.geomajas.gwt.client.map.layer.Layer;
import org.geomajas.gwt.example.base.client.sample.SamplePanel;
import org.geomajas.gwt.widget.client.map.MapLegendPanel;

import com.allen_sauer.gwt.dnd.client.DragEndEvent;
import com.allen_sauer.gwt.dnd.client.DragHandler;
import com.allen_sauer.gwt.dnd.client.DragStartEvent;
import com.allen_sauer.gwt.dnd.client.PickupDragController;
import com.allen_sauer.gwt.dnd.client.VetoDragException;
import com.allen_sauer.gwt.dnd.client.drop.VerticalPanelDropController;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.DecoratorPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ResizeLayoutPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * ContentPanel that demonstrates changing layer order.
 * 
 * @author Pieter De Graef
 */
public class LegendOrderSample implements SamplePanel {

	/**
	 * UI binder for this widget.
	 * 
	 * @author Pieter De Graef
	 */
	interface MyUiBinder extends UiBinder<Widget, LegendOrderSample> {
	}

	private static final GeomajasGinjector GEOMAJASINJECTOR = GWT.create(GeomajasGinjector.class);

	private static final MyUiBinder UI_BINDER = GWT.create(MyUiBinder.class);

	private MapPresenter mapPresenter;

	private PickupDragController layerDragController;

	@UiField
	protected VerticalPanel legendPanel;

	@UiField
	protected VerticalPanel layerPanel;

	@UiField
	protected ResizeLayoutPanel mapPanel;

	@UiField
	protected AbsolutePanel dndBoundary;

	public Widget asWidget() {
		// Define the left layout:
		Widget layout = UI_BINDER.createAndBindUi(this);

		layerDragController = new PickupDragController(dndBoundary, false);
		layerDragController.setBehaviorMultipleSelection(false);
		layerDragController.registerDropController(new VerticalPanelDropController(layerPanel));
		layerDragController.addDragHandler(new LayerDragHandler());

		// Create the MapPresenter and add an InitializationHandler:
		mapPresenter = GEOMAJASINJECTOR.getMapPresenter();
		mapPresenter.setSize(480, 480);
		mapPresenter.getEventBus().addMapInitializationHandler(new MyMapInitializationHandler());

		// Add the MapLegendPanel to the layout:
		legendPanel.add(new MapLegendPanel(mapPresenter));

		// Define the whole layout:
		DecoratorPanel mapDecorator = new DecoratorPanel();
		mapDecorator.add(mapPresenter.asWidget());
		mapPanel.add(mapDecorator);

		// Initialize the map, and return the layout:
		mapPresenter.initialize("gwt-app", "mapLegend");
		return layout;
	}

	// ------------------------------------------------------------------------
	// Private classes:
	// ------------------------------------------------------------------------

	/**
	 * DragHandler that triggers the moving of layers in the LayersModel.
	 * 
	 * @author Pieter De Graef
	 */
	private class LayerDragHandler implements DragHandler {

		private Layer dragLayer;

		public void onDragEnd(DragEndEvent event) {
			int dropIndex = layerPanel.getWidgetIndex(event.getContext().selectedWidgets.get(0)) - 1;
			mapPresenter.getLayersModel().moveLayer(dragLayer, dropIndex);
		}

		public void onDragStart(DragStartEvent event) {
			dragLayer = ((LayerWidget) event.getSource()).getLayer();
		}

		public void onPreviewDragEnd(DragEndEvent event) throws VetoDragException {
		}

		public void onPreviewDragStart(DragStartEvent event) throws VetoDragException {
		}
	}

	/**
	 * When the map initializes: add draggable layer labels to the layer panel.
	 * 
	 * @author Pieter De Graef
	 */
	private class MyMapInitializationHandler implements MapInitializationHandler {

		public void onMapInitialized(MapInitializationEvent event) {
			for (int i = 0; i < mapPresenter.getLayersModel().getLayerCount(); i++) {
				LayerWidget widget = new LayerWidget(mapPresenter.getLayersModel().getLayer(i));
				layerDragController.makeDraggable(widget);
				layerPanel.add(widget);
			}
		}
	}

	/**
	 * Definition of a layer label widget.
	 * 
	 * @author Pieter De Graef
	 */
	private final class LayerWidget extends Label {

		private Layer layer;

		private LayerWidget(Layer layer) {
			super(layer.getTitle());
			setWidth("100%");
			this.layer = layer;
		}

		public Layer getLayer() {
			return layer;
		}
	}
}