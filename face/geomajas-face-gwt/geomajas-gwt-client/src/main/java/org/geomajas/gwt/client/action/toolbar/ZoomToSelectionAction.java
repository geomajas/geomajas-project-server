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

package org.geomajas.gwt.client.action.toolbar;

import java.util.List;

import org.geomajas.global.GeomajasConstant;
import org.geomajas.gwt.client.action.ToolbarAction;
import org.geomajas.gwt.client.i18n.I18nProvider;
import org.geomajas.gwt.client.map.MapView;
import org.geomajas.gwt.client.map.feature.Feature;
import org.geomajas.gwt.client.map.feature.LazyLoadCallback;
import org.geomajas.gwt.client.map.layer.Layer;
import org.geomajas.gwt.client.map.layer.VectorLayer;
import org.geomajas.gwt.client.map.store.VectorLayerStore;
import org.geomajas.gwt.client.spatial.Bbox;
import org.geomajas.gwt.client.widget.MapWidget;

import com.smartgwt.client.widgets.events.ClickEvent;

/**
 * Zoom to the current selection.
 * 
 * @author Frank Wynants
 */
public class ZoomToSelectionAction extends ToolbarAction {

	private MapWidget mapWidget;

	public ZoomToSelectionAction(MapWidget mapWidget) {
		super("[ISOMORPHIC]/geomajas/osgeo/zoom-selection.png", I18nProvider.getToolbar().zoomToSelection());
		this.mapWidget = mapWidget;
	}

	/**
	 * Zoom to the selected features.
	 */
	public void onClick(ClickEvent clickEvent) {
		//get the selected VectorLayer
		Layer<?> selectedLayer = mapWidget.getMapModel().getSelectedLayer();
		if (selectedLayer instanceof VectorLayer) {
			//iterate all features of the selected layer
			VectorLayerStore featureStore = ((VectorLayer) selectedLayer).getFeatureStore();
			featureStore.getFeatures(GeomajasConstant.FEATURE_INCLUDE_GEOMETRY, new LazyLoadCallback() {
				public void execute(List<Feature> features) {
					Bbox selectionBounds = new Bbox(0, 0, 0, 0);
					for (Feature feature : features) {
						//if the feature is selected union the bounding box
						if (feature.isSelected()) {
							selectionBounds = selectionBounds.union(feature.getGeometry().getBounds());
						}
					}

					// only zoom when their where really some items selected
					if ((selectionBounds.getX() != 0) && (selectionBounds.getY() != 0)) {
						mapWidget.getMapModel().getMapView().applyBounds(selectionBounds,
								MapView.ZoomOption.LEVEL_CHANGE);
					}
				}
			});
		}
	}
}
