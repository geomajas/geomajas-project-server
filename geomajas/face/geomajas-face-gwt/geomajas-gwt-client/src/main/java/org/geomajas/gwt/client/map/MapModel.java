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

package org.geomajas.gwt.client.map;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.geomajas.configuration.LayerInfo;
import org.geomajas.configuration.MapInfo;
import org.geomajas.configuration.RasterLayerInfo;
import org.geomajas.configuration.VectorLayerInfo;
import org.geomajas.gwt.client.gfx.Paintable;
import org.geomajas.gwt.client.gfx.PainterVisitor;
import org.geomajas.gwt.client.gfx.WorldPaintable;
import org.geomajas.gwt.client.map.event.FeatureDeselectedEvent;
import org.geomajas.gwt.client.map.event.FeatureSelectedEvent;
import org.geomajas.gwt.client.map.event.FeatureSelectionHandler;
import org.geomajas.gwt.client.map.event.FeatureTransactionEvent;
import org.geomajas.gwt.client.map.event.FeatureTransactionHandler;
import org.geomajas.gwt.client.map.event.HasFeatureSelectionHandlers;
import org.geomajas.gwt.client.map.event.LayerDeselectedEvent;
import org.geomajas.gwt.client.map.event.LayerSelectedEvent;
import org.geomajas.gwt.client.map.event.LayerSelectionHandler;
import org.geomajas.gwt.client.map.event.MapModelEvent;
import org.geomajas.gwt.client.map.event.MapModelHandler;
import org.geomajas.gwt.client.map.event.MapViewChangedEvent;
import org.geomajas.gwt.client.map.event.MapViewChangedHandler;
import org.geomajas.gwt.client.map.feature.Feature;
import org.geomajas.gwt.client.map.feature.FeatureEditor;
import org.geomajas.gwt.client.map.feature.FeatureTransaction;
import org.geomajas.gwt.client.map.layer.Layer;
import org.geomajas.gwt.client.map.layer.RasterLayer;
import org.geomajas.gwt.client.map.layer.VectorLayer;
import org.geomajas.gwt.client.spatial.Bbox;
import org.geomajas.gwt.client.spatial.geometry.GeometryFactory;

import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.event.shared.HandlerRegistration;

/**
 * <p>
 * The model behind a map. This object contains all the layers related to the map. When re-rendering the entire map, it
 * is actually this model that is rendered. Therefore the MapModel implements the <code>Paintable</code> interface.
 * </p>
 * 
 * @author Pieter De Graef
 */
public class MapModel implements Paintable, MapViewChangedHandler, HasFeatureSelectionHandlers {

	/**
	 * The models ID. This is necessary mainly because of the <code>Paintable</code> interface. Still, every painted
	 * object needs a unique identifier.
	 */
	private String id;

	/** The map's coordinate system as an EPSG code. (i.e. lonlat = 'epsg:4326' => srid = 4326) */
	private int srid;

	/**
	 * An ordered list of layers. The drawing order on the map is as follows: the first layer will be placed at the
	 * bottom, the last layer on top.
	 */
	private List<Layer> layers = new ArrayList<Layer>();

	/** Reference to the <code>MapView</code> object of the <code>MapWidget</code>. */
	private MapView mapView;

	private MapInfo description;

	private FeatureEditor featureEditor;

	private List<WorldPaintable> worldSpacePaintables;

	private HandlerManager handlerManager;

	/** Contains a */
	private Map<String, Feature> selectedFeatures = new HashMap<String, Feature>();

	private GeometryFactory geometryFactory;

	// -------------------------------------------------------------------------
	// Constructors:
	// -------------------------------------------------------------------------

	/**
	 * Initialize map model, coordinate system has to be filled in later (from configuration).
	 * 
	 * @param id
	 *            map id
	 */
	public MapModel(String id) {
		this.id = id;
		featureEditor = new FeatureEditor(this);
		worldSpacePaintables = new ArrayList<WorldPaintable>();
		handlerManager = new HandlerManager(this);
		mapView = new MapView();
		mapView.addMapViewChangedHandler(this);
	}

	/**
	 * Adds this handler to the model.
	 * 
	 * @param handler
	 *            the handler
	 * @return {@link com.google.gwt.event.shared.HandlerRegistration} used to remove the handler
	 */
	public final HandlerRegistration addMapModelHandler(final MapModelHandler handler) {
		return handlerManager.addHandler(MapModelEvent.TYPE, handler);
	}

	public final HandlerRegistration addFeatureSelectionHandler(FeatureSelectionHandler handler) {
		return handlerManager.addHandler(FeatureSelectionHandler.TYPE, handler);
	}

	public HandlerRegistration addFeatureTransactionHandler(FeatureTransactionHandler handler) {
		return handlerManager.addHandler(FeatureTransactionEvent.TYPE, handler);
	}

	public HandlerRegistration addLayerSelectionHandler(LayerSelectionHandler handler) {
		return handlerManager.addHandler(LayerSelectionHandler.TYPE, handler);
	}

	// -------------------------------------------------------------------------
	// Implementation of the Paintable interface:
	// -------------------------------------------------------------------------

	/**
	 * Paintable implementation. First let the PainterVisitor paint this object, then if recursive is true, painter the
	 * layers in order.
	 */
	public void accept(PainterVisitor visitor, Bbox bounds, boolean recursive) {
		// Paint the MapModel itself (see MapModelPainter):
		visitor.visit(this);

		// Paint the layers:
		if (recursive) {
			for (Layer layer : layers) {
				if (layer.isShowing()) {
					layer.accept(visitor, bounds, recursive);
					if (layer instanceof VectorLayer) {
						// nrDeferred++;
					}
				} else {
					// JDM: paint the top part of the layer, if not we loose the
					// map order !!!!!
					layer.accept(visitor, bounds, false);
				}
			}
		}

		// Paint the world space paintable objects:
		if (worldSpacePaintables != null) {
			for (WorldPaintable paintable : worldSpacePaintables) {
				paintable.scale(1 / mapView.getCurrentScale());
				paintable.accept(visitor, bounds, recursive);
			}
		}

		// Paint the editing of a feature (if a feature is being edited):
		if (featureEditor.getFeatureTransaction() != null) {
			featureEditor.getFeatureTransaction().accept(visitor, bounds, recursive);
		}
	}

	/** Return this mapmodel's id. */
	public String getId() {
		return id;
	}

	// -------------------------------------------------------------------------
	// Class specific functions:
	// -------------------------------------------------------------------------

	/**
	 * Initialize the MapModel object, using a configuration object acquired from the server. This will automatically
	 * build the list of layers.
	 * 
	 * @param mapInfo
	 *            The configuration object.
	 */
	public void initialize(final MapInfo mapInfo) {
		description = mapInfo;
		srid = Integer.parseInt(mapInfo.getCrs().substring(mapInfo.getCrs().indexOf(":") + 1));
		if (mapInfo.isResolutionsRelative()) {
			List<Double> realResolutions = new ArrayList<Double>();
			for (Double resolution : mapInfo.getResolutions()) {
				realResolutions.add(resolution * mapInfo.getPixelLength());
			}
			mapView.setResolutions(realResolutions);
		} else {
			mapView.setResolutions(mapInfo.getResolutions());
		}
		mapView.setMaximumScale(mapInfo.getMaximumScale());
		Bbox initialBounds = new Bbox(mapInfo.getInitialBounds().getX(), mapInfo.getInitialBounds().getY(), mapInfo
				.getInitialBounds().getWidth(), mapInfo.getInitialBounds().getHeight());
		mapView.applyBounds(initialBounds, MapView.ZoomOption.LEVEL_CLOSEST);
		removeAllLayers();
		Bbox maxBounds = new Bbox(initialBounds);
		for (LayerInfo layerInfo : mapInfo.getLayers()) {
			addLayer(layerInfo);
			maxBounds = maxBounds.union(new Bbox(layerInfo.getMaxExtent()));
		}
		mapView.setMaxBounds(maxBounds);
		handlerManager.fireEvent(new MapModelEvent());
	}

	protected void removeAllLayers() {
		layers = new ArrayList<Layer>();
	}

	protected void addLayer(LayerInfo layerInfo) {
		Layer layer;
		switch (layerInfo.getLayerType()) {
			case RASTER:
				layer = new RasterLayer(this, (RasterLayerInfo) layerInfo);
				break;
			default:
				layer = new VectorLayer(this, (VectorLayerInfo) layerInfo);
				break;
		}
		layers.add(layer);
	}

	/**
	 * Search a layer by it's id.
	 * 
	 * @param layerId
	 *            The layer's ID.
	 * @return Returns either a Layer, or null.
	 */
	public Layer getLayerByLayerId(String layerId) {
		if (layers != null) {
			for (Layer layer : layers) {
				if (layer.getId().equals(layerId)) {
					return layer;
				}
			}
		}
		return null;
	}

	/**
	 * Select a new layer. Only one layer can be selected at a time, so this function first tries to deselect the
	 * currently selected (if there is one).
	 * 
	 * @param layer
	 *            The layer to select. If layer is null, then the currently selected layer will be deselected!
	 */
	public void selectLayer(Layer layer) {
		if (layer == null) {
			deselectLayer(this.getSelectedLayer());
		} else {
			Layer selLayer = this.getSelectedLayer();
			if (selLayer != null && !layer.getId().equals(selLayer.getId())) {
				deselectLayer(selLayer);
			}
			layer.setSelected(true);
			handlerManager.fireEvent(new LayerSelectedEvent(layer));
		}
	}

	/**
	 * Deselect the currently selected layer, includes sending the deselect events.
	 * 
	 * @param layer
	 *            layer to clear
	 */
	private void deselectLayer(Layer layer) {
		if (layer != null) {
			layer.setSelected(false);
			handlerManager.fireEvent(new LayerDeselectedEvent(layer));
		}
	}

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
	 */
	public void selectFeature(Feature feature) {
		if (!selectedFeatures.containsKey(feature.getId())) {
			Feature clone = feature.clone();
			selectedFeatures.put(clone.getId(), clone);
			handlerManager.fireEvent(new FeatureSelectedEvent(clone));
		}
	}

	/**
	 * Deselect a feature: set the feature's selected state and remove it from the layer's selection.
	 * 
	 * @param feature
	 *            The feature that is to be selected.
	 */
	public void deselectFeature(Feature feature) {
		if (selectedFeatures.containsKey(feature.getId())) {
			Feature org = selectedFeatures.remove(feature.getId());
			handlerManager.fireEvent(new FeatureDeselectedEvent(org));
		}
	}

	/** Clear the list of selected features. */
	public void clearSelectedFeatures() {
		for (Feature feature : selectedFeatures.values()) {
			handlerManager.fireEvent(new FeatureDeselectedEvent(feature));
		}
		selectedFeatures.clear();
	}

	/**
	 * Searches for the selected layer, and returns it.
	 * 
	 * @return Returns the selected layer object, or null if none is selected.
	 */
	public Layer getSelectedLayer() {
		if (layers != null) {
			for (Layer layer : layers) {
				if (layer.isSelected()) {
					return layer;
				}
			}
		}
		return null;
	}

	/**
	 * Retrieve a feature, by it's ID.
	 * 
	 * @param featureId
	 *            Must be the entire id: <layer>.<feature>
	 * @return Returns the feature if it is found, null otherwise.
	 */
	public Feature getFeatureById(String featureId) {
		String[] ids = featureId.split("\\."); // It's a regular expression, not literally.
		Layer<?> layer = this.getLayerByLayerId(ids[0]);
		if (layer != null && layer instanceof VectorLayer) {
			boolean ok = false;
			int count = ids.length - 1;
			while (!ok) {
				String fid = ids[count];
				if (!"use".equals(fid)) {
					ok = true;
				} else {
					count--;
				}
			}
			return ((VectorLayer) layer).getFeatureStore().getFeature(featureId);
		}
		return null;
	}

	public void applyFeatureTransaction(FeatureTransaction ft) {
		if (ft != null) {
			VectorLayer layer = ft.getLayer();
			if (layer != null) {
				layer.getFeatureStore().clear();
			}
			mapView.translate(0, 0);
		}
	}

	// Getters and setters:

	public List<Layer> getLayers() {
		return layers;
	}

	public MapView getMapView() {
		return mapView;
	}

	public FeatureEditor getFeatureEditor() {
		return featureEditor;
	}

	public int getSrid() {
		return srid;
	}

	public String getCrs() {
		return "EPSG:" + srid;
	}

	public int getPrecision() {
		if (description != null) {
			return description.getPrecision();
		}
		return -1;
	}

	public List<WorldPaintable> getWorldSpacePaintables() {
		return worldSpacePaintables;
	}

	public MapInfo getDescription() {
		return description;
	}

	/**
	 * Update the visibility of the layers.
	 * 
	 * @param event
	 *            change event
	 */
	public void onMapViewChanged(MapViewChangedEvent event) {
		for (Layer layer : layers) {
			layer.updateShowing();
		}
	}

	public GeometryFactory getGeometryFactory() {
		if (null == geometryFactory) {
			if (0 == srid) {
				throw new IllegalArgumentException("srid needs to be set on MapModel to obtain GeometryFactory");
			}
			geometryFactory = new GeometryFactory(srid, -1); // @todo precision is not yet implemented
		}
		return geometryFactory;
	}
}
