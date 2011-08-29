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

package org.geomajas.gwt.client.action.toolbar;

import java.util.List;

import org.geomajas.global.GeomajasConstant;
import org.geomajas.gwt.client.action.ToolbarAction;
import org.geomajas.gwt.client.i18n.I18nProvider;
import org.geomajas.gwt.client.map.feature.Feature;
import org.geomajas.gwt.client.map.feature.LazyLoadCallback;
import org.geomajas.gwt.client.map.layer.Layer;
import org.geomajas.gwt.client.map.layer.VectorLayer;
import org.geomajas.gwt.client.map.store.VectorLayerStore;
import org.geomajas.gwt.client.spatial.Bbox;
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
		// get the selected VectorLayer
		Layer<?> selectedLayer = mapWidget.getMapModel().getSelectedLayer();
		if (selectedLayer instanceof VectorLayer) {
			// iterate all features of the selected layer
			VectorLayerStore featureStore = ((VectorLayer) selectedLayer).getFeatureStore();
			featureStore.getFeatures(GeomajasConstant.FEATURE_INCLUDE_GEOMETRY, new LazyLoadCallback() {

				public void execute(List<Feature> response) {
					boolean success = false;
					Bbox selectionBounds = new Bbox(0, 0, 0, 0);
					for (Feature feature : response) {
						// if the feature is selected union the bounding box
						if (feature.isSelected()) {
							selectionBounds = selectionBounds.union(feature.getGeometry().getBounds());
							success = true;
						}
					}

					// only pan when their where really some items selected
					if (success) {
						mapWidget.getMapModel().getMapView().setCenterPosition(selectionBounds.getCenterPoint());
					}
				}
			});
		}
	}
}
