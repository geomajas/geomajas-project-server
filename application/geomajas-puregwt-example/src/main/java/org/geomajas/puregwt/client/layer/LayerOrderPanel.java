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

package org.geomajas.puregwt.client.layer;

import org.geomajas.puregwt.client.ContentPanel;
import org.geomajas.puregwt.client.event.MapInitializationEvent;
import org.geomajas.puregwt.client.event.MapInitializationHandler;
import org.geomajas.puregwt.client.map.MapPresenter;
import org.geomajas.puregwt.client.map.layer.Layer;

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
 * @author Pieter De Graef
 */
public class LayerOrderPanel extends ContentPanel {

	public LayerOrderPanel(MapPresenter mapPresenter) {
		super(mapPresenter);
	}

	private PickupDragController layerDragController;

	private VerticalPanel layerPanel;

	public String getTitle() {
		return "Changing layer order";
	}

	public String getDescription() {
		return "Example that demonstrates the ability to change layer order. Try dragging the labels up and down.";
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
	 * DragHandler that triggers the moving of layers in the LayersModel.
	 * 
	 * @author Pieter De Graef
	 */
	private class LayerDragHandler implements DragHandler {

		private Layer dragLayer;

		public void onDragEnd(DragEndEvent event) {
			int dropIndex = layerPanel.getWidgetIndex(event.getContext().selectedWidgets.get(0));
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