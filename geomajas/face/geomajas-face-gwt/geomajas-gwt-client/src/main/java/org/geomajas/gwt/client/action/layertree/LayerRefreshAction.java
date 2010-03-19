/*
 * This file is part of Geomajas, a component framework for building
 * rich Internet applications (RIA) with sophisticated capabilities for the
 * display, analysis and management of geographic information.
 * It is a building block that allows developers to add maps
 * and other geographic data capabilities to their web applications.
 *
 * Copyright 2008-2010 Geosparc, http://www.geosparc.com, Belgium
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.geomajas.gwt.client.action.layertree;

import org.geomajas.gwt.client.i18n.I18nProvider;
import org.geomajas.gwt.client.map.layer.Layer;
import org.geomajas.gwt.client.widget.MapWidget;
import org.geomajas.gwt.client.widget.MapWidget.RenderGroup;
import org.geomajas.gwt.client.widget.MapWidget.RenderStatus;

/**
 * Refreshes the currently selected layer on the map.
 *
 * @author Pieter De Graef
 */
public class LayerRefreshAction extends LayerTreeAction {

	private MapWidget map;

	public LayerRefreshAction(MapWidget map) {
		super("[ISOMORPHIC]/geomajas/layertree/refresh.png", I18nProvider.getLayerTree().layerRefresh(),
				"[ISOMORPHIC]/geomajas/layertree/refresh-disabled.png");
		this.map = map;
	}

	public void onClick(Layer<?> layer) {
		map.render(layer, RenderGroup.PAN, RenderStatus.DELETE);
		map.render(layer, RenderGroup.PAN, RenderStatus.ALL);
	}

	public boolean isEnabled(Layer<?> layer) {
		return (layer != null);
	}
}
