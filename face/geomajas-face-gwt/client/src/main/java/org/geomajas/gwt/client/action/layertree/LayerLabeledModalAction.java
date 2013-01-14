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
import org.geomajas.gwt.client.map.layer.VectorLayer;
import org.geomajas.gwt.client.util.WidgetLayout;

/**
 * Action that switches the labels on a layer.
 *
 * @author Frank Wynants
 * @author Pieter De Graef
 */
public class LayerLabeledModalAction extends LayerTreeModalAction {

	public LayerLabeledModalAction() {
		super(WidgetLayout.iconLabelsShow, WidgetLayout.iconLabelsHide, WidgetLayout.iconLabelsDisabled,
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