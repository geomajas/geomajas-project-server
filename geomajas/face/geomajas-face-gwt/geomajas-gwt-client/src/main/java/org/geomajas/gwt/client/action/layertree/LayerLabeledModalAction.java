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
import org.geomajas.gwt.client.map.layer.VectorLayer;

/**
 * Action that switches the labels on a layer.
 *
 * @author Frank Wynants
 * @author Pieter De Graef
 */
public class LayerLabeledModalAction extends LayerTreeModalAction {

	public LayerLabeledModalAction() {
		super("[ISOMORPHIC]/geomajas/layertree/labels-show.png",
				"[ISOMORPHIC]/geomajas/layertree/labels-hide.png",
				"[ISOMORPHIC]/geomajas/layertree/labels-disabled.png",
				I18nProvider.getLayerTree().labelHideAction(), I18nProvider.getLayerTree().labelAction());
	}

	public boolean isEnabled(Layer<?> layer) {
		return (layer != null && layer instanceof VectorLayer);
	}

	public boolean isSelected(Layer<?> layer) {
		return layer != null && layer.isLabeled();
	}

	public void onDeselect(Layer<?> layer) {
		layer.setLabeled(false);
	}

	public void onSelect(Layer<?> layer) {
		layer.setLabeled(true);
	}

}