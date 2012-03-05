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

package org.geomajas.puregwt.widget.client.map;

import org.geomajas.puregwt.client.map.MapPresenter;
import org.geomajas.puregwt.client.map.layer.Layer;

import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * ...
 * 
 * @author Pieter De Graef
 */
public class MapLegendPopupPanel extends PopupPanel {

	private final VerticalPanel contentPanel = new VerticalPanel();

	private final MapPresenter mapPresenter;

	public MapLegendPopupPanel(MapPresenter mapPresenter) {
		this.mapPresenter = mapPresenter;
		setAnimationEnabled(true);
		setAutoHideEnabled(true);
		setGlassEnabled(false);

		setWidget(contentPanel);
	}

	// ------------------------------------------------------------------------
	// Public methods:
	// ------------------------------------------------------------------------

	public boolean addLayer(Layer<?> layer) {
		int index = getLayerIndex(layer);
		if (index < 0) {
			contentPanel.add(new LayerLegendPanel(mapPresenter.getEventBus(), layer));
			return true;
		}
		return false;
	}

	public boolean removeLayer(Layer<?> layer) {
		int index = getLayerIndex(layer);
		if (index >= 0) {
			contentPanel.remove(index);
			return true;
		}
		return false;
	}

	// ------------------------------------------------------------------------
	// Private methods:
	// ------------------------------------------------------------------------

	private int getLayerIndex(Layer<?> layer) {
		for (int i = 0; i < contentPanel.getWidgetCount(); i++) {
			LayerLegendPanel layerPanel = (LayerLegendPanel) contentPanel.getWidget(i);
			if (layerPanel.getLayer() == layer) {
				return i;
			}
		}
		return -1;
	}
}