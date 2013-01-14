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

package org.geomajas.gwt.client.action.layertree;

import org.geomajas.gwt.client.i18n.I18nProvider;
import org.geomajas.gwt.client.map.layer.Layer;
import org.geomajas.gwt.client.util.WidgetLayout;
import org.geomajas.gwt.client.widget.MapWidget;

/**
 * Refreshes the currently selected layer on the map.
 * 
 * @author Pieter De Graef
 */
public class LayerRefreshAction extends LayerTreeAction {

	private MapWidget map;

	public LayerRefreshAction(MapWidget map) {
		super(WidgetLayout.iconRefresh, I18nProvider.getLayerTree().layerRefresh(), WidgetLayout.iconRefreshDisabled);
		this.map = map;
	}

	public void onClick(Layer<?> layer) {
		map.refreshLayer(layer);
	}

	public boolean isEnabled(Layer<?> layer) {
		return (layer != null);
	}
}
