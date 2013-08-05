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

package org.geomajas.smartgwt.client.action.layertree;

import org.geomajas.smartgwt.client.i18n.I18nProvider;
import org.geomajas.smartgwt.client.map.layer.Layer;
import org.geomajas.smartgwt.client.util.WidgetLayout;

/**
 * Action that switches visibility of a layer.
 *
 * @author Frank Wynants
 * @author Pieter De Graef
 */
public class LayerVisibleModalAction extends LayerTreeModalAction {

	public LayerVisibleModalAction() {
		super(WidgetLayout.iconLayerShow, WidgetLayout.iconLayerHide, WidgetLayout.iconLayerDisabled,
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