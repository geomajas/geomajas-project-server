/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2011 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
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
		super("[ISOMORPHIC]/geomajas/widget/layertree/labels-show.png",
				"[ISOMORPHIC]/geomajas/widget/layertree/labels-hide.png",
				"[ISOMORPHIC]/geomajas/widget/layertree/labels-disabled.png",
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