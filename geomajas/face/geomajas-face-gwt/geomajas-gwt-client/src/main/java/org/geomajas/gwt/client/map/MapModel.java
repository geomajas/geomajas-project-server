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
import java.util.List;

import org.geomajas.configuration.client.ClientLayerInfo;
import org.geomajas.configuration.client.ClientMapInfo;
import org.geomajas.configuration.client.ClientRasterLayerInfo;
import org.geomajas.configuration.client.ClientVectorLayerInfo;
import org.geomajas.gwt.client.gfx.Paintable;
import org.geomajas.gwt.client.gfx.PainterVisitor;
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
	private List<Layer<?>> layers = new ArrayList<Layer<?>>();

	/** Reference to the <code>MapView</code> object of the <code>MapWidget</code>. */
	private MapView mapView;

	private ClientMapInfo mapInfo;

	private FeatureEditor featureEditor;

	private HandlerManager handlerManager;

	private GeometryFactory geometryFactory;

	private boolean initialized;

	private LayerSelectionPropagator selectionPropagator = new LayerSelectionPropagator();

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

	public void removeMapModelHandler(MapModelHandler handler) {
		handlerManager.removeHandler(MapModelEvent.TYPE, handler);
	}

	// -------------------------------------------------------------------------
	// Implementation of the Paintable interface:
	// -------------------------------------------------------------------------

	/**
	 * Paintable implementation. First let the PainterVisitor paint this object, then if recursive is true, painter the
	 * layers in order.
	 */
	public void accept(PainterVisitor visitor, Object group, Bbox bounds, boolean recursive) {
		// Paint the MapModel itself (see MapModelPainter):
		visitor.visit(this, group);

		// Paint the layers:
		if (recursive) {
			for (Layer<?> layer : layers) {
				if (layer.isShowing()) {
					layer.accept(visitor, group, bounds, recursive);
					if (layer instanceof VectorLayer) {
						// nrDeferred++;
					}
				} else {
					// JDM: paint the top part of the layer, if not we loose the map order
					layer.accept(visitor, group, bounds, false);
				}
			}
		}

		// Paint the editing of a feature (if a feature is being edited):
		if (featureEditor.getFeatureTransaction() != null) {
			featureEditor.getFeatureTransaction().accept(visitor, group, bounds, recursive);
		}
	}

	/**
	 * Return this map model's id.
	 * 
	 * @return id
	 */
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
	public void initialize(final ClientMapInfo mapInfo) {
		if (!initialized) {
			this.mapInfo = mapInfo;
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
			removeAllLayers();
			Bbox maxBounds = new Bbox(initialBounds);
			for (ClientLayerInfo layerInfo : mapInfo.getLayers()) {
				addLayer(layerInfo);
				maxBounds = maxBounds.union(new Bbox(layerInfo.getMaxExtent()));
			}
			mapView.setMaxBounds(maxBounds);
			mapView.applyBounds(initialBounds, MapView.ZoomOption.LEVEL_CLOSEST);
		}
		handlerManager.fireEvent(new MapModelEvent());
		initialized = true;
	}

	/**
	 * Is this map model initialized yet ?
	 * 
	 * @return true if initialized
	 */
	public boolean isInitialized() {
		return initialized;
	}

	protected void removeAllLayers() {
		layers = new ArrayList<Layer<?>>();
	}

	protected void addLayer(ClientLayerInfo layerInfo) {
		switch (layerInfo.getLayerType()) {
			case RASTER:
				RasterLayer rasterLayer = new RasterLayer(this, (ClientRasterLayerInfo) layerInfo);
				layers.add(rasterLayer);
				break;
			default:
				VectorLayer vectorLayer = new VectorLayer(this, (ClientVectorLayerInfo) layerInfo);
				layers.add(vectorLayer);
				vectorLayer.addFeatureSelectionHandler(selectionPropagator);
				break;
		}
	}

	/**
	 * Search a layer by it's id.
	 * 
	 * @param layerId
	 *            The layer's client ID.
	 * @return Returns either a Layer, or null.
	 */
	public Layer<?> getLayer(String layerId) {
		if (layers != null) {
			for (Layer<?> layer : layers) {
				if (layer.getId().equals(layerId)) {
					return layer;
				}
			}
		}
		return null;
	}

	/**
	 * Get all layers with the specified server layer id.
	 * 
	 * @param serverLayerId
	 *            The layer's server layer ID.
	 * @return Returns list of layers with the specified server layer id.
	 */
	public List<Layer<?>> getLayersByServerId(String serverLayerId) {
		List<Layer<?>> l = new ArrayList<Layer<?>>();
		if (layers != null) {
			for (Layer<?> layer : layers) {
				if (layer.getServerLayerId().equals(serverLayerId)) {
					l.add(layer);
				}
			}
		}
		return l;
	}

	/**
	 * Get all vector layers with the specified server layer id.
	 * 
	 * @param serverLayerId
	 *            The layer's server layer ID.
	 * @return Returns list of layers with the specified server layer id.
	 */
	public List<VectorLayer> getVectorLayersByServerId(String serverLayerId) {
		List<VectorLayer> l = new ArrayList<VectorLayer>();
		if (layers != null) {
			for (VectorLayer layer : getVectorLayers()) {
				if (layer.getServerLayerId().equals(serverLayerId)) {
					l.add(layer);
				}
			}
		}
		return l;
	}

	/**
	 * Search a vector layer by it's id.
	 * 
	 * @param layerId
	 *            The layer's client ID.
	 * @return Returns either a Layer, or null.
	 */
	public VectorLayer getVectorLayer(String layerId) {
		if (layers != null) {
			for (VectorLayer layer : getVectorLayers()) {
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
	public void selectLayer(Layer<?> layer) {
		if (layer == null) {
			deselectLayer(this.getSelectedLayer());
		} else {
			Layer<?> selLayer = this.getSelectedLayer();
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
	private void deselectLayer(Layer<?> layer) {
		if (layer != null) {
			layer.setSelected(false);
			handlerManager.fireEvent(new LayerDeselectedEvent(layer));
		}
	}

	public List<VectorLayer> getVectorLayers() {
		ArrayList<VectorLayer> list = new ArrayList<VectorLayer>();
		for (Layer<?> layer : layers) {
			if (layer instanceof VectorLayer) {
				list.add((VectorLayer) layer);
			}
		}
		return list;
	}

	/** Clear the list of selected features. */
	public void clearSelectedFeatures() {
		for (VectorLayer layer : getVectorLayers()) {
			layer.clearSelectedFeatures();
		}
	}

	/**
	 * Searches for the selected layer, and returns it.
	 * 
	 * @return Returns the selected layer object, or null if none is selected.
	 */
	public Layer<?> getSelectedLayer() {
		if (layers != null) {
			for (Layer<?> layer : layers) {
				if (layer.isSelected()) {
					return layer;
				}
			}
		}
		return null;
	}

	public void applyFeatureTransaction(FeatureTransaction ft) {
		if (ft != null) {
			VectorLayer layer = ft.getLayer();
			if (layer != null) {
				// clear all the tiles TODO: limit this a bit more ?
				layer.getFeatureStore().clear();
			}
			// now update/add the features
			if (ft.getNewFeatures() != null) {
				for (Feature feature : ft.getNewFeatures()) {
					ft.getLayer().getFeatureStore().addFeature(feature);
				}
			}
			// make it fetch the tiles
			mapView.translate(0, 0);
		}
	}

	// Getters and setters:

	public List<Layer<?>> getLayers() {
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
		if (mapInfo != null) {
			return mapInfo.getPrecision();
		}
		return -1;
	}

	public ClientMapInfo getMapInfo() {
		return mapInfo;
	}

	/**
	 * Update the visibility of the layers.
	 * 
	 * @param event
	 *            change event
	 */
	public void onMapViewChanged(MapViewChangedEvent event) {
		for (Layer<?> layer : layers) {
			layer.updateShowing();

			// If the map is resized quickly after a previous resize, tile requests are sent out, but when they come
			// back, the world-to-pan matrix will have altered, and so the tiles are placed at the wrong positions....
			// so we clear the store.
			if (layer instanceof RasterLayer && event.isMapResized()) {
				((RasterLayer) layer).getStore().clear();
			}
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

	/**
	 * Propagates selection events to interested listeners.
	 * 
	 * @author Jan De Moerloose
	 * 
	 */
	class LayerSelectionPropagator implements FeatureSelectionHandler {

		public void onFeatureDeselected(FeatureDeselectedEvent event) {
			handlerManager.fireEvent(event);
		}

		public void onFeatureSelected(FeatureSelectedEvent event) {
			handlerManager.fireEvent(event);
		}

	}
}
