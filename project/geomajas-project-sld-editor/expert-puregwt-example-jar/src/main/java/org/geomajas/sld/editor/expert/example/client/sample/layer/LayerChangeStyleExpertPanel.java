/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2013 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the Apache
 * License, Version 2.0. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */

package org.geomajas.sld.editor.expert.example.client.sample.layer;

import org.geomajas.gwt2.client.GeomajasGinjector;
import org.geomajas.gwt2.client.event.LayerAddedEvent;
import org.geomajas.gwt2.client.event.LayerRemovedEvent;
import org.geomajas.gwt2.client.event.MapCompositionHandler;
import org.geomajas.gwt2.client.map.MapPresenter;
import org.geomajas.gwt2.client.map.layer.Layer;
import org.geomajas.gwt2.client.map.layer.VectorServerLayer;
import org.geomajas.gwt2.example.base.client.ExampleBase;
import org.geomajas.gwt2.example.base.client.sample.SamplePanel;
import org.geomajas.gwt2.widget.client.map.MapLegendPanel;

import com.google.gwt.core.client.Callback;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.AttachEvent;
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
 * ContentPanel that demonstrates changing layer style (using expert editor).
 * 
 * @author Kristof Heirwegh
 */
public class LayerChangeStyleExpertPanel implements SamplePanel {

	/**
	 * UIBinder interface.
	 */
	interface MyUiBinder extends UiBinder<Widget, LayerChangeStyleExpertPanel> {
	}

	private static final MyUiBinder UI_BINDER = GWT.create(MyUiBinder.class);

	private MapPresenter mapPresenter;

	private final ExpertSldEditorHelper editor;
	
	private VectorServerLayer currentLayer;
	
	@UiField
	protected VerticalPanel legendPanel;

	@UiField
	protected VerticalPanel layerPanel;

	@UiField
	protected ResizeLayoutPanel mapPanel;
	
	private final Widget widget;

	public LayerChangeStyleExpertPanel() {
		// Define the left layout:
		widget = UI_BINDER.createAndBindUi(this);

		// Create the MapPresenter and add an InitializationHandler:
		mapPresenter = ((GeomajasGinjector) ExampleBase.getInjector()).getMapPresenter();
		mapPresenter.setSize(480, 480);
		mapPresenter.getEventBus().addMapCompositionHandler(new MyMapCompositionHandler());

		// Add the map to the GUI, using a decorator for nice borders:
		DecoratorPanel mapDecorator = new DecoratorPanel();
		mapDecorator.add(mapPresenter.asWidget());
		mapPanel.add(mapDecorator);

		final MapLegendPanel mlp = new MapLegendPanel(mapPresenter);
		legendPanel.add(mlp);

		// Initialize the map, and return the layout:
		mapPresenter.initialize("puregwt-app", "mapLegend");
		
		editor = new ExpertSldEditorHelper(new Callback<ExpertSldEditorHelper, Void>() {
			public void onFailure(Void reason) {
			}
			public void onSuccess(ExpertSldEditorHelper result) {
				currentLayer.updateStyle(currentLayer.getLayerInfo().getNamedStyleInfo());
				currentLayer.refresh();
			}
		});
		
		widget.addAttachHandler(new AttachEvent.Handler() {
			@Override
			public void onAttachOrDetach(AttachEvent event) {
				if (!event.isAttached()) {
					editor.destroy(); // must remove listeners or next time there will be weird behaviour
				}
			}
		});
	}
	
	public Widget asWidget() {
		return widget;
	}

	// ------------------------------------------------------------------------

	/**
	 * @author Kristof Heirwegh
	 */
	private class MyMapCompositionHandler implements MapCompositionHandler {

		@Override
		public void onLayerAdded(LayerAddedEvent event) {
			Layer layer = event.getLayer();
			if (layer instanceof VectorServerLayer) {
				layerPanel.add(new LayerWidget((VectorServerLayer) layer));
			}
		}

		@Override
		public void onLayerRemoved(LayerRemovedEvent event) {
		}
	}

	/**
	 * @author Kristof Heirwegh
	 */
	private final class LayerWidget extends HorizontalPanel {

		private LayerWidget(final VectorServerLayer layer) {
			setWidth("100%");
			Button changeBtn = new Button("Change Style");
			changeBtn.addClickHandler(new ClickHandler() {
				public void onClick(ClickEvent event) {
					currentLayer = layer;
					editor.setLayer(layer.getLayerInfo());
					editor.showExpertStyleEditor();
				}
			});
			add(changeBtn);
			add(new Label(layer.getTitle()));
			setStyleName(ExampleBase.getShowcaseResource().css().sampleRow());
		}
	}

}
