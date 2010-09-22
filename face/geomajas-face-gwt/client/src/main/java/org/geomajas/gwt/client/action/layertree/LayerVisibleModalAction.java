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

/**
 * Action that switches visibility of a layer.
 *
 * @author Frank Wynants
 * @author Pieter De Graef
 */
public class LayerVisibleModalAction extends LayerTreeModalAction {

	public LayerVisibleModalAction() {
		super("[ISOMORPHIC]/geomajas/widget/layertree/layer-show.png",
				"[ISOMORPHIC]/geomajas/widget/layertree/layer-hide.png",
				"[ISOMORPHIC]/geomajas/widget/layertree/layer-disabled.png",
				I18nProvider.getLayerTree().inVisibleAction(), I18nProvider.getLayerTree().visibleAction());
	}

	public boolean isEnabled(Layer<?> layer) {
		return (layer != null);
	}

	public boolean isSelected(Layer<?> layer) {
		return layer != null && layer.isShowing();
	}

	public void onDeselect(Layer<?> layer) {
		layer.setVisible(false);
	}

	public void onSelect(Layer<?> layer) {
		layer.setVisible(true);
	}
}