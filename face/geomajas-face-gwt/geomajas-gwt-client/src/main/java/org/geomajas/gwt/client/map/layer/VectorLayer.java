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

package org.geomajas.gwt.client.map.layer;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.geomajas.configuration.client.ClientVectorLayerInfo;
import org.geomajas.global.Api;
import org.geomajas.gwt.client.gfx.PaintableGroup;
import org.geomajas.gwt.client.gfx.PainterVisitor;
import org.geomajas.gwt.client.gfx.paintable.Composite;
import org.geomajas.gwt.client.map.MapModel;
import org.geomajas.gwt.client.map.cache.TileCache;
import org.geomajas.gwt.client.map.cache.tile.TileFunction;
import org.geomajas.gwt.client.map.cache.tile.VectorTile;
import org.geomajas.gwt.client.map.event.FeatureDeselectedEvent;
import org.geomajas.gwt.client.map.event.FeatureSelectedEvent;
import org.geomajas.gwt.client.map.event.FeatureSelectionHandler;
import org.geomajas.gwt.client.map.event.HasFeatureSelectionHandlers;
import org.geomajas.gwt.client.map.feature.Feature;
import org.geomajas.gwt.client.map.store.VectorLayerStore;
import org.geomajas.gwt.client.spatial.Bbox;

import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.event.shared.HandlerRegistration;

/**
 * <p>
 * The client side representation of a vector layer.
 * </p>
 * 
 * @author Pieter De Graef
 */
@Api
public class VectorLayer extends AbstractLayer<ClientVectorLayerInfo> implements HasFeatureSelectionHandlers {

	/** Storage of features in this layer. */
	private TileCache cache;

	private String filter;

	private Composite featureGroup = new Composite("features");

	private Composite selectionGroup = new Composite("selection");

	private Composite labelGroup = new Composite("labels");

	private HandlerManager handlerManager = new HandlerManager(this);

	/** selected features id -> feature map */
	private Map<String, Feature> selectedFeatures = new HashMap<String, Feature>();

	// -------------------------------------------------------------------------
	// Constructors:
	// -------------------------------------------------------------------------

	/**
	 * The only constructor! Set the MapModel and the metadata.
	 * 
	 * @param mapModel
	 *            The model of layers and features behind a map. This layer will be a part of this model.
	 */
	public VectorLayer(MapModel mapModel, ClientVectorLayerInfo layerInfo) {
		super(mapModel, layerInfo);
		Bbox maxExtent = new Bbox(layerInfo.getMaxExtent().getX(), layerInfo.getMaxExtent().getY(), layerInfo
				.getMaxExtent().getWidth(), layerInfo.getMaxExtent().getHeight());
		cache = new TileCache(this, maxExtent);
	}

	public final HandlerRegistration addFeatureSelectionHandler(FeatureSelectionHandler handler) {
		return handlerManager.addHandler(FeatureSelectionHandler.TYPE, handler);
	}

	// -------------------------------------------------------------------------
	// Paintable implementation:
	// -------------------------------------------------------------------------

	public void accept(final PainterVisitor visitor, final Object group, final Bbox bounds, boolean recursive) {
		// Draw layer-specific stuff (see VectorLayerPainter)
		visitor.visit(this, group);

		// When visible, take care of fetching through an queryAndSync:
		if (recursive && isShowing()) {
			TileFunction<VectorTile> onDelete = new TileFunction<VectorTile>() {

				// When deleting a tile, delete selected features from the paint !
				public void execute(final VectorTile tile) {
					for (Feature feature : tile.getPartialFeatures()) {
						if (feature != null && feature.isSelected()) {
							visitor.remove(feature, group);
						}
					}
					visitor.remove(tile, group);
				}
			};
			TileFunction<VectorTile> onUpdate = new TileFunction<VectorTile>() {

				// Updating a tile, is simply re-rendering it:
				public void execute(VectorTile tile) {
					tile.accept(visitor, group, bounds, true);
					// also re-render the selected features !
					for (Feature feature : selectedFeatures.values()) {
						visitor.visit(feature, group);
					}

				}
			};
			cache.queryAndSync(bounds, filter, onDelete, onUpdate);
		}
	}

	// -------------------------------------------------------------------------
	// Public methods for feature Selection
	// -------------------------------------------------------------------------

	/**
	 * Return whether the feature with given id is selected.
	 * 
	 * @param featureId
	 *            feature id to test
	 * @return true when the feature with given ide is selected
	 */
	public boolean isFeatureSelected(String featureId) {
		return selectedFeatures.containsKey(featureId);
	}

	/**
	 * Select a feature: set the feature's selected state and add it to the layer's selection.
	 * 
	 * @param feature
	 *            The feature that is to be selected.
	 * @return true when the feature was deselected
	 */
	public boolean selectFeature(Feature feature) {
		if (!selectedFeatures.containsKey(feature.getId())) {
			// make sure we get the layer's instance (if we can find it) !
			Feature layerFeature = getFeatureStore().getPartialFeature(feature.getId());
			if (null != layerFeature) {
				selectedFeatures.put(layerFeature.getId(), layerFeature);
				handlerManager.fireEvent(new FeatureSelectedEvent(layerFeature));
			} else {
				selectedFeatures.put(feature.getId(), feature);
				handlerManager.fireEvent(new FeatureSelectedEvent(feature));
			}
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Deselect a feature: set the feature's selected state and remove it from the layer's selection.
	 * 
	 * @param feature
	 *            The feature that is to be selected.
	 * @return true when the feature was selected
	 */
	public boolean deselectFeature(Feature feature) {
		if (selectedFeatures.containsKey(feature.getId())) {
			Feature org = selectedFeatures.remove(feature.getId());
			handlerManager.fireEvent(new FeatureDeselectedEvent(org));
			return true;
		} else {
			return false;
		}
	}

	/** Clear the list of selected features. */
	public void clearSelectedFeatures() {
		List<Feature> clone = new LinkedList<Feature>(selectedFeatures.values());
		for (Feature feature : clone) {
			selectedFeatures.remove(feature.getId());
			handlerManager.fireEvent(new FeatureDeselectedEvent(feature));
		}
	}

	/** Returns a set of selected features in this layer by their ID's. */
	public Set<String> getSelectedFeatures() {
		return selectedFeatures.keySet();
	}

	// -------------------------------------------------------------------------
	// Public methods:
	// -------------------------------------------------------------------------

	public String getFilter() {
		return filter;
	}

	public void setFilter(String filter) {
		this.filter = filter;
	}

	public VectorLayerStore getFeatureStore() {
		return cache;
	}

	public PaintableGroup getFeatureGroup() {
		return featureGroup;
	}

	public PaintableGroup getSelectionGroup() {
		return selectionGroup;
	}

	public PaintableGroup getLabelGroup() {
		return labelGroup;
	}
}
