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

package org.geomajas.gwt.client.action.toolbar;

import java.util.ArrayList;
import java.util.List;

import org.geomajas.gwt.client.action.ToolbarAction;
import org.geomajas.gwt.client.i18n.I18nProvider;
import org.geomajas.gwt.client.map.feature.Feature;
import org.geomajas.gwt.client.map.layer.VectorLayer;
import org.geomajas.gwt.client.util.WidgetLayout;
import org.geomajas.gwt.client.widget.MapWidget;

import com.smartgwt.client.widgets.events.ClickEvent;

/**
 * Pan so that the selected items are in the center of the screen.
 * 
 * @author Frank Wynants
 */
public class PanToSelectionAction extends ToolbarAction {

	private MapWidget mapWidget;

	public PanToSelectionAction(MapWidget mapWidget) {
		super(WidgetLayout.iconPanToSelection, I18nProvider.getToolbar().panToSelectionTitle(),
				I18nProvider.getToolbar().panToSelectionTooltip());
		this.mapWidget = mapWidget;
	}

	/**
	 * Pan to the selected features.
	 */
	public void onClick(ClickEvent clickEvent) {
		List<Feature> features = new ArrayList<Feature>();
 		for (VectorLayer layer : mapWidget.getMapModel().getVectorLayers()) {
			features.addAll(layer.getSelectedFeatureValues());
		}
		// only zoom when there where really some items selected
		if (features.size() > 0) {
			mapWidget.getMapModel().panToFeatures(features);
		}
	}
}
