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

import org.geomajas.gwt.client.event.LayerAddedEvent;
import org.geomajas.gwt.client.event.LayerRemovedEvent;
import org.geomajas.gwt.client.event.MapCompositionHandler;
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
 * ContentPanel that demonstrates changing layer order.
 * 
 * @author Jan De Moerloose
 */
public class LayerAddRemovePanel implements SamplePanel {

	/**
	 * UI binder for this widget.
	 * 
	 * @author Pieter De Graef
	 */
	interface MyUiBinder extends UiBinder<Widget, LayerAddRemovePanel> {
	}

	private static final MyUiBinder UI_BINDER = GWT.create(MyUiBinder.class);

	private MapPresenter mapPresenter;

	@UiField
	protected VerticalPanel layerAddedPanel;

	@UiField
	protected VerticalPanel layerRemovedPanel;

	@UiField
	protected ResizeLayoutPanel mapPanel;

	public Widget asWidget() {
		// Define the left layout:
		Widget layout = UI_BINDER.createAndBindUi(this);

		// Create the MapPresenter and add an InitializationHandler:
		mapPresenter = ExampleJar.getInjector().getMapPresenter();
		mapPresenter.setSize(480, 480);
		mapPresenter.getEventBus().addMapCompositionHandler(new MyMapCompositionHandler());

		DecoratorPanel mapDecorator = new DecoratorPanel();
		mapDecorator.add(mapPresenter.asWidget());
		mapPanel.add(mapDecorator);

		// Initialize the map, and return the layout:
		mapPresenter.initialize("puregwt-app", "mapLegend");
		return layout;
	}

	// ------------------------------------------------------------------------
	// Private classes:
	// ------------------------------------------------------------------------

	/**
	 * When layers are added or removed, display them in the correct panel.
	 * 
	 * @author Pieter De Graef
	 */
	private class MyMapCompositionHandler implements MapCompositionHandler {

		public void onLayerAdded(LayerAddedEvent event) {
			Layer layer = event.getLayer();
			for (int i = 0; i < layerRemovedPanel.getWidgetCount(); i++) {
				LayerRemovedWidget widget = (LayerRemovedWidget) layerRemovedPanel.getWidget(i);
				if (layer.equals(widget.getLayer())) {
					layerRemovedPanel.remove(i);
				}
			}
			layerAddedPanel.add(new LayerAddedWidget(layer));
		}

		public void onLayerRemoved(LayerRemovedEvent event) {
			Layer layer = event.getLayer();
			for (int i = 0; i < layerAddedPanel.getWidgetCount(); i++) {
				LayerAddedWidget widget = (LayerAddedWidget) layerAddedPanel.getWidget(i);
				if (layer == widget.getLayer()) {
					layerAddedPanel.remove(i);

				}
			}
			layerRemovedPanel.add(new LayerRemovedWidget(layer));
		}
	}

	/**
	 * Layer representation on the GUI.
	 * 
	 * @author Pieter De Graef
	 */
	private final class LayerAddedWidget extends HorizontalPanel {

		private final Layer layer;

		private LayerAddedWidget(final Layer layer) {
			this.layer = layer;
			setWidth("100%");
			Button removeBtn = new Button("Remove");
			removeBtn.addClickHandler(new ClickHandler() {

				public void onClick(ClickEvent event) {
					mapPresenter.getLayersModel().removeLayer(layer.getId());
				}
			});
			add(removeBtn);
			add(new Label(layer.getTitle()));
			setStyleName(ExampleBase.getShowcaseResource().css().sampleRow());
		}

		public Layer getLayer() {
			return layer;
		}
	}

	/**
	 * Layer representation on the GUI.
	 * 
	 * @author Pieter De Graef
	 */
	private final class LayerRemovedWidget extends HorizontalPanel {

		private final Layer layer;

		private LayerRemovedWidget(final Layer layer) {
			this.layer = layer;
			setWidth("100%");
			Button removeBtn = new Button("Add");
			removeBtn.addClickHandler(new ClickHandler() {

				public void onClick(ClickEvent event) {
					mapPresenter.getLayersModel().addLayer(layer);
				}
			});
			add(removeBtn);
			add(new Label(layer.getTitle()));
			setStyleName(ExampleBase.getShowcaseResource().css().sampleRow());
		}

		public Layer getLayer() {
			return layer;
		}
	}
}