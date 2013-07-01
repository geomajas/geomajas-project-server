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

import org.geomajas.puregwt.client.event.LayerHideEvent;
import org.geomajas.puregwt.client.event.LayerShowEvent;
import org.geomajas.puregwt.client.event.LayerVisibilityHandler;
import org.geomajas.puregwt.client.event.LayerVisibilityMarkedEvent;
import org.geomajas.puregwt.client.event.ViewPortChangedEvent;
import org.geomajas.puregwt.client.event.ViewPortChangedHandler;
import org.geomajas.puregwt.client.event.ViewPortScaledEvent;
import org.geomajas.puregwt.client.event.ViewPortTranslatedEvent;
import org.geomajas.puregwt.client.map.MapPresenter;
import org.geomajas.puregwt.client.map.layer.Layer;
import org.geomajas.puregwt.client.map.layer.LayerStylePresenter;
import org.geomajas.sld.RuleInfo;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

/**
 * A view that displays the title and styles for a single layer.
 * 
 * @author Pieter De Graef
 */
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

	private MapPresenter mapPresenter;

	public LayerLegendPanel(MapPresenter mapPresenter, final Layer layer) {
		this.layer = layer;
		initWidget(UI_BINDER.createAndBindUi(this));
		this.mapPresenter = mapPresenter;

		// Apply the layer onto the GUI:
		title.setText(layer.getTitle());
		visibilityToggle.setValue(layer.isMarkedAsVisible());

		// React to layer visibility events:
		mapPresenter.getEventBus().addLayerVisibilityHandler(new LayerVisibilityHandler() {

			public void onShow(LayerShowEvent event) {
				if (event.getLayer() == LayerLegendPanel.this.layer) {
					visibilityToggle.setEnabled(true);
				}
			}

			public void onHide(LayerHideEvent event) {
				if (event.getLayer() == LayerLegendPanel.this.layer) {
					visibilityToggle.setEnabled(false);
				}
			}

			public void onVisibilityMarked(LayerVisibilityMarkedEvent event) {
				if (event.getLayer() == LayerLegendPanel.this.layer) {
					visibilityToggle.setValue(LayerLegendPanel.this.layer.isMarkedAsVisible());
				}
			}
		});

		mapPresenter.getEventBus().addViewPortChangedHandler(new ViewPortChangedHandler() {

			@Override
			public void onViewPortTranslated(ViewPortTranslatedEvent event) {
			}

			@Override
			public void onViewPortScaled(ViewPortScaledEvent event) {
				applyLegendStyle(layer, event.getViewPort().getScale());
			}

			@Override
			public void onViewPortChanged(ViewPortChangedEvent event) {
				applyLegendStyle(layer, event.getViewPort().getScale());
			}
		});

		visibilityToggle.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				if (visibilityToggle.isEnabled()) {
					LayerLegendPanel.this.layer.setMarkedAsVisible(!LayerLegendPanel.this.layer.isMarkedAsVisible());
					visibilityToggle.setEnabled(true); // Works because JavaScript is single threaded...
				}
			}
		});
	}

	private void applyLegendStyle(Layer layer, double scale) {
		// Apply the legend:
		legendTable.removeAllRows();

		for (LayerStylePresenter stylePresenter : layer.getStylePresenters()) {
			if (isVisible(stylePresenter, scale)) {
				addStyle(stylePresenter);
			}
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

	private void addStyle(final LayerStylePresenter stylePresenter) {
		// Add a new row in the table:
		final int row = legendTable.insertRow(legendTable.getRowCount());
		legendTable.addCell(row);
		legendTable.setWidget(row, 0, stylePresenter);
	}
	
	private boolean isVisible(LayerStylePresenter layerStyle, double scale) {
		if (layerStyle == null || layerStyle.getRule() == null) {
			return true;
		}
		double minScale = Double.MAX_VALUE;
		double maxScale = Double.MIN_VALUE;
		
		RuleInfo rInfo = layerStyle.getRule();
		double unitLength = mapPresenter.getConfiguration().getServerConfiguration().getUnitLength();
		double pixelLength = mapPresenter.getConfiguration().getServerConfiguration().getPixelLength();
		
		if (rInfo.getMinScaleDenominator() != null && rInfo.getMinScaleDenominator().getMinScaleDenominator() != 0) {
			minScale = unitLength / (pixelLength * rInfo.getMinScaleDenominator().getMinScaleDenominator());
		}
		
		if (rInfo.getMaxScaleDenominator() != null && rInfo.getMaxScaleDenominator().getMaxScaleDenominator() != 0) {
			maxScale = unitLength / (pixelLength *  rInfo.getMaxScaleDenominator().getMaxScaleDenominator());
		}
		
		return (maxScale <= scale && scale < minScale);
	}
}