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

package org.geomajas.puregwt.widget.client.map;

import org.geomajas.annotation.Api;
import org.geomajas.gwt.client.event.LayerHideEvent;
import org.geomajas.gwt.client.event.LayerShowEvent;
import org.geomajas.gwt.client.event.LayerVisibilityHandler;
import org.geomajas.gwt.client.event.LayerVisibilityMarkedEvent;
import org.geomajas.gwt.client.map.MapEventBus;
import org.geomajas.gwt.client.map.layer.HasLegendWidget;
import org.geomajas.gwt.client.map.layer.Layer;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

/**
 * <p>
 * A widget that displays the legend for a single layer. It provides the possibility to toggle that layer's visibility
 * through a CheckBox.
 * </p>
 * <p>
 * If the layer should become invisible because the map has zoomed in or out beyond the allowed scale range (for that
 * layer), the CheckBox in this widget will automatically become disabled. It is no use to start marking a layer visible
 * when it won't appear anyway.
 * </p>
 * 
 * @author Pieter De Graef
 * @since 1.0.0
 */
@Api
public class LayerLegendPanel extends Composite {

	/**
	 * UI binder definition for the {@link LayerLegendPanel} widget.
	 * 
	 * @author Pieter De Graef
	 */
	interface LayerLegendPanelUiBinder extends UiBinder<Widget, LayerLegendPanel> {
	}

	private static final LayerLegendPanelUiBinder UI_BINDER = GWT.create(LayerLegendPanelUiBinder.class);

	private final Layer layer;

	@UiField
	protected CheckBox visibilityToggle;

	@UiField
	protected Label title;

	@UiField
	protected FlexTable legendTable;

	/**
	 * Create a new legend panel for a single layer. This panel
	 * 
	 * @param eventBus
	 *            Map event bus. Must be the bus from the same map that holds the layer.
	 * @param layer
	 *            The layer to display in this legend widget.
	 */
	public LayerLegendPanel(MapEventBus eventBus, final Layer layer) {
		WidgetMapResources.INSTANCE.css().ensureInjected();

		this.layer = layer;
		initWidget(UI_BINDER.createAndBindUi(this));

		// Apply the layer onto the GUI:
		title.setText(layer.getTitle());
		visibilityToggle.setValue(layer.isMarkedAsVisible());

		// React to layer visibility events:
		eventBus.addLayerVisibilityHandler(new LayerVisibilityHandler() {

			public void onShow(LayerShowEvent event) {
				visibilityToggle.setEnabled(true);
			}

			public void onHide(LayerHideEvent event) {
				// If a layer hides while it is marked as visible, it means it has gone beyond it's allowed scale range.
				// If so, disable the CheckBox. It's no use to try to mark the layer as visible anyway:
				if (layer.isMarkedAsVisible()) {
					visibilityToggle.setEnabled(false);
				}
			}

			public void onVisibilityMarked(LayerVisibilityMarkedEvent event) {
				visibilityToggle.setValue(layer.isMarkedAsVisible());
			}
		}, this.layer);

		visibilityToggle.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				if (visibilityToggle.isEnabled()) {
					LayerLegendPanel.this.layer.setMarkedAsVisible(!layer.isMarkedAsVisible());
					visibilityToggle.setEnabled(true); // Works because JavaScript is single threaded...
				}
			}
		});

		// Add the legend:
		if (layer instanceof HasLegendWidget) {
			HasLegendWidget hlw = (HasLegendWidget) layer;
			IsWidget legendWidget = hlw.buildLegendWidget();
			final int row = legendTable.insertRow(legendTable.getRowCount());
			legendTable.addCell(row);
			legendTable.setWidget(row, 0, legendWidget);
		}
	}

	/**
	 * Return the target layer for this legend panel.
	 * 
	 * @return The layer who's styles are displayed within this panel.
	 */
	public Layer getLayer() {
		return layer;
	}
}