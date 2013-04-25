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

package org.geomajas.puregwt.example.client.sample.layer;

import org.geomajas.puregwt.client.event.MapInitializationEvent;
import org.geomajas.puregwt.client.event.MapInitializationHandler;
import org.geomajas.puregwt.client.map.MapPresenter;
import org.geomajas.puregwt.client.map.layer.Layer;
import org.geomajas.puregwt.example.client.ContentPanel;

import com.allen_sauer.gwt.dnd.client.DragEndEvent;
import com.allen_sauer.gwt.dnd.client.DragHandler;
import com.allen_sauer.gwt.dnd.client.DragStartEvent;
import com.allen_sauer.gwt.dnd.client.PickupDragController;
import com.allen_sauer.gwt.dnd.client.VetoDragException;
import com.allen_sauer.gwt.dnd.client.drop.VerticalPanelDropController;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.DecoratorPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * ContentPanel that demonstrates changing layer order.
 * 
 * @author Jan De Moerloose
 */
public class LayerAddRemovePanel extends ContentPanel {

	public LayerAddRemovePanel(MapPresenter mapPresenter) {
		super(mapPresenter);
	}

	private PickupDragController layerDragController;

	private VerticalPanel layerPanel;

	private Label addedMarker;

	private Label removedMarker;

	public String getTitle() {
		return "Adding/Removing layers";
	}

	public String getDescription() {
		return "Example that demonstrates the ability to add layers to/remove layers from the map. "
				+ "Try dragging the labels from the 'Added layers' section to the 'Removed layers' section and back.";
	}

	public Widget getContentWidget() {
		// Define the left layout:
		VerticalPanel leftLayout = new VerticalPanel();
		leftLayout.setSize("200px", "100%");
		leftLayout.add(new HTML("<h3>Layers:</h3>"));
		AbsolutePanel dndBoundary = new AbsolutePanel();
		dndBoundary.setSize("200px", "100%");
		leftLayout.add(dndBoundary);
		layerPanel = new VerticalPanel();
		addedMarker = new Label("Added layers:");
		addedMarker.setWidth("100%");
		removedMarker = new Label("Removed layers:");
		removedMarker.setWidth("100%");
		layerPanel.add(addedMarker);
		layerPanel.add(removedMarker);
		dndBoundary.add(layerPanel);

		layerDragController = new PickupDragController(dndBoundary, false);
		layerDragController.setBehaviorMultipleSelection(false);
		layerDragController.registerDropController(new VerticalPanelDropController(layerPanel));
		layerDragController.addDragHandler(new LayerDragHandler());

		// Create the MapPresenter and add an InitializationHandler:
		mapPresenter.setSize(640, 480);
		mapPresenter.getEventBus().addMapInitializationHandler(new MyMapInitializationHandler());

		// Define the whole layout:
		HorizontalPanel layout = new HorizontalPanel();
		layout.add(leftLayout);
		DecoratorPanel mapDecorator = new DecoratorPanel();
		mapDecorator.add(mapPresenter.asWidget());
		layout.add(mapDecorator);

		// Initialize the map, and return the layout:
		mapPresenter.initialize("puregwt-app", "mapLegend");
		return layout;
	}

	// ------------------------------------------------------------------------
	// Private classes:
	// ------------------------------------------------------------------------

	/**
	 * Added/Removed state.
	 * 
	 * @author Jan De Moerloose
	 * 
	 */
	enum State {
		ADDED, REMOVED
	};

	/**
	 * DragHandler that triggers the moving of layers in the LayersModel.
	 * 
	 * @author Pieter De Graef
	 */
	private class LayerDragHandler implements DragHandler {

		private Layer dragLayer;

		private State before;

		private State after;

		public void onDragEnd(DragEndEvent event) {
			after = getState((LayerWidget) event.getSource());
			int dropIndex = layerPanel.getWidgetIndex((LayerWidget) event.getSource());
			if (before == after && after == State.ADDED) {
				Layer layer = mapPresenter.getLayersModel().getLayer(dragLayer.getId());
				mapPresenter.getLayersModel().moveLayer(layer, dropIndex - 1);
			} else if (after == State.REMOVED) {
				mapPresenter.getLayersModel().removeLayer(dragLayer.getId());
			} else if (after == State.ADDED) {
				mapPresenter.getLayersModel().addLayer(dragLayer);
				mapPresenter.getLayersModel().moveLayer(dragLayer, dropIndex - 1);
			}
		}

		public void onDragStart(DragStartEvent event) {
			dragLayer = ((LayerWidget) event.getSource()).getLayer();
			before = getState((LayerWidget) event.getSource());
		}

		State getState(Widget w) {
			int dragIndex = layerPanel.getWidgetIndex(w);
			int removedIndex = layerPanel.getWidgetIndex(removedMarker);
			return dragIndex < removedIndex ? State.ADDED : State.REMOVED;
		}

		public void onPreviewDragEnd(DragEndEvent event) throws VetoDragException {
			int dropIndex = layerPanel.getWidgetIndex((LayerWidget) event.getSource());
			if (dropIndex == 0) {
				throw new VetoDragException();
			}
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
				layerPanel.insert(widget, layerPanel.getWidgetCount() - 1);
			}
		}
	}

	/**
	 * Definition of a layer label widget.
	 * 
	 * @author Jan De Moerloose
	 */
	private final class LayerWidget extends Label {

		private final Layer layer;

		private LayerWidget(Layer layer) {
			super(layer.getTitle());
			setWidth("100%");
			setStyleName("layer-block");
			this.layer = layer;
		}

		public Layer getLayer() {
			return layer;
		}
	}
}