/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2012 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */

package org.geomajas.gwt.client.map;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.geomajas.annotation.Api;
import org.geomajas.configuration.client.BoundsLimitOption;
import org.geomajas.configuration.client.ClientLayerInfo;
import org.geomajas.configuration.client.ClientMapInfo;
import org.geomajas.configuration.client.ClientRasterLayerInfo;
import org.geomajas.configuration.client.ClientVectorLayerInfo;
import org.geomajas.configuration.client.ScaleConfigurationInfo;
import org.geomajas.configuration.client.ScaleInfo;
import org.geomajas.global.GeomajasConstant;
import org.geomajas.gwt.client.command.GwtCommandDispatcher;
import org.geomajas.gwt.client.command.event.TokenChangedEvent;
import org.geomajas.gwt.client.command.event.TokenChangedHandler;
import org.geomajas.gwt.client.gfx.Paintable;
import org.geomajas.gwt.client.gfx.PainterVisitor;
import org.geomajas.gwt.client.map.MapView.ZoomOption;
import org.geomajas.gwt.client.map.event.FeatureDeselectedEvent;
import org.geomajas.gwt.client.map.event.FeatureSelectedEvent;
import org.geomajas.gwt.client.map.event.FeatureSelectionHandler;
import org.geomajas.gwt.client.map.event.FeatureTransactionEvent;
import org.geomajas.gwt.client.map.event.FeatureTransactionHandler;
import org.geomajas.gwt.client.map.event.HasFeatureSelectionHandlers;
import org.geomajas.gwt.client.map.event.LayerDeselectedEvent;
import org.geomajas.gwt.client.map.event.LayerSelectedEvent;
import org.geomajas.gwt.client.map.event.LayerSelectionHandler;
import org.geomajas.gwt.client.map.event.MapModelChangedEvent;
import org.geomajas.gwt.client.map.event.MapModelChangedHandler;
import org.geomajas.gwt.client.map.event.MapModelClearEvent;
import org.geomajas.gwt.client.map.event.MapModelClearHandler;
import org.geomajas.gwt.client.map.event.MapModelEvent;
import org.geomajas.gwt.client.map.event.MapModelHandler;
import org.geomajas.gwt.client.map.event.MapViewChangedEvent;
import org.geomajas.gwt.client.map.event.MapViewChangedHandler;
import org.geomajas.gwt.client.map.feature.Feature;
import org.geomajas.gwt.client.map.feature.FeatureEditor;
import org.geomajas.gwt.client.map.feature.FeatureTransaction;
import org.geomajas.gwt.client.map.feature.LazyLoadCallback;
import org.geomajas.gwt.client.map.layer.Layer;
import org.geomajas.gwt.client.map.layer.RasterLayer;
import org.geomajas.gwt.client.map.layer.VectorLayer;
import org.geomajas.gwt.client.service.ClientConfigurationService;
import org.geomajas.gwt.client.service.WidgetConfigurationCallback;
import org.geomajas.gwt.client.spatial.Bbox;
import org.geomajas.gwt.client.spatial.geometry.GeometryFactory;
import org.geomajas.gwt.client.util.Log;

import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.event.shared.HandlerRegistration;

/**
 * <p>
 * The model behind a map. This object contains all the layers related to the map. When re-rendering the entire map, it
 * is actually this model that is rendered. Therefore the MapModel implements the <code>Paintable</code> interface.
 * </p>
 * 
 * @author Pieter De Graef
 * @author Joachim Van der Auwera
 * @since 1.6.0
 */
@Api
public class MapModel implements Paintable, MapViewChangedHandler, HasFeatureSelectionHandlers {

	/**
	 * The models ID. This is necessary mainly because of the <code>Paintable</code> interface. Still, every painted
	 * object needs a unique identifier.
	 */
	private String id;

	private String applicationId;

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
	private boolean initCalled;
	private boolean mapModelEventFired; // assures MapModelEvent is only fired once

	private LayerSelectionPropagator selectionPropagator = new LayerSelectionPropagator();
	
	private List<Runnable> whenInitializedRunnables = new ArrayList<Runnable>();
	
	// -------------------------------------------------------------------------
	// Constructors:
	// -------------------------------------------------------------------------

	/**
	 * Initialize map model, coordinate system has to be filled in later (from configuration).
	 *
	 * @param mapId map id
	 * @since 1.6.0
	 * @deprecated use {@link #MapModel(String, String)}, this assume "app" as applicationId
	 */
	@Api
	@Deprecated
	public MapModel(String mapId) {
		this(mapId, "app");
		Log.logWarn("Using deprecated MapModel constructor, assuming application id is 'app'");
	}
	/**
	 * Initialize map model, coordinate system has to be filled in later (from configuration).
	 * 
	 * @param mapId map id
	 * @param applicationId application id
	 * @since 1.10.0
	 */
	@Api
	public MapModel(String mapId, String applicationId) {
		this.id = mapId;
		this.applicationId = applicationId;
		featureEditor = new FeatureEditor(this);
		handlerManager = new HandlerManager(this);
		mapView = new MapView();
		mapView.addMapViewChangedHandler(this);

		// refresh the map when the token changes
		GwtCommandDispatcher.getInstance().addTokenChangedHandler(new TokenChangedHandler() {
			public void onTokenChanged(TokenChangedEvent event) {
				if (event.isLoginPending()) {
					// avoid double refresh on re-login
					clear();
					ClientConfigurationService.clear(); // refresh because configuration changed, clear cache
				} else {
					refresh(); // clearing is done in the refresh
				}
			}
		});
	}

	// constructor for testing
	public MapModel(ClientMapInfo info) {
		this.id = info.getId();
		this.applicationId = "bla";

		featureEditor = new FeatureEditor(this);
		handlerManager = new HandlerManager(this);
		mapView = new MapView();
		mapView.addMapViewChangedHandler(this);

		refresh(info);
	}

	/**
	 * Run some code once when the map is initialized.
	 *
	 * @param runnable code to run
	 * @since 1.10.0
	 */
	@Api
	public void runWhenInitialized(Runnable runnable) {
		if (isInitialized()) {
			runnable.run();
		} else {
			whenInitializedRunnables.add(runnable);
		}
	}

	// -------------------------------------------------------------------------
	// MapModel event handling:
	// -------------------------------------------------------------------------

	/**
	 * Adds this handler to the model.
	 * 
	 * @param handler
	 *            the handler
	 * @return {@link com.google.gwt.event.shared.HandlerRegistration} used to remove the handler
	 * @since 1.6.0
	 */
	@Api
	public final HandlerRegistration addMapModelHandler(final MapModelHandler handler) {
		return handlerManager.addHandler(MapModelEvent.TYPE, handler);
	}

	/**
	 * Remove map model handler.
	 *
	 * @param handler handler to be removed
	 * @since 1.6.0
	 */
	@Api
	public void removeMapModelHandler(final MapModelHandler handler) {
		handlerManager.removeHandler(MapModelEvent.TYPE, handler);
	}

	/**
	 * Add a handler which listens to all changes in the map model.
	 *
	 * @param handler handler
	 * @return {@link com.google.gwt.event.shared.HandlerRegistration} used to remove the handler
	 * @since 1.10.0
	 */
	@Api
	public final HandlerRegistration addMapModelChangedHandler(final MapModelChangedHandler handler) {
		return handlerManager.addHandler(MapModelChangedHandler.TYPE, handler);
	}

	/**
	 * Remove map model changed handler.
	 *
	 * @param handler handler to be removed
	 * @since 1.10.0
	 */
	@Api
	public void removeMapModelChangedHandler(final MapModelChangedHandler handler) {
		handlerManager.removeHandler(MapModelChangedHandler.TYPE, handler);
	}

	/**
	 * Add a handler which listens to clearing the map model.
	 *
	 * @param handler handler
	 * @return {@link com.google.gwt.event.shared.HandlerRegistration} used to remove the handler
	 * @since 1.10.0
	 */
	@Api
	public final HandlerRegistration addMapModelClearHandler(final MapModelClearHandler handler) {
		return handlerManager.addHandler(MapModelClearHandler.TYPE, handler);
	}

	/**
	 * Remove map model clear handler.
	 *
	 * @param handler handler to be removed
	 * @since 1.10.0
	 */
	@Api
	public void removeMapModelClearHandler(final MapModelClearHandler handler) {
		handlerManager.removeHandler(MapModelClearHandler.TYPE, handler);
	}

	/**
	 * Add feature selection handler.
	 * 
	 * @param handler
	 *            The handler to be registered.
	 * @return handler registration
	 * @since 1.6.0
	 */
	@Api
	public final HandlerRegistration addFeatureSelectionHandler(final FeatureSelectionHandler handler) {
		return handlerManager.addHandler(FeatureSelectionHandler.TYPE, handler);
	}

	/**
	 * Add layer selection handler.
	 *
	 * @param handler
	 *            the handler to be registered
	 * @return handler registration
	 * @since 1.6.0
	 */
	@Api
	public HandlerRegistration addLayerSelectionHandler(final LayerSelectionHandler handler) {
		return handlerManager.addHandler(LayerSelectionHandler.TYPE, handler);
	}

	/**
	 * Add a new handler for {@link FeatureTransactionEvent}s.
	 * 
	 * @param handler
	 *            the handler to be registered
	 * @return handler registration
	 * @since 1.7.0
	 */
	@Api
	public HandlerRegistration addFeatureTransactionHandler(final FeatureTransactionHandler handler) {
		return handlerManager.addHandler(FeatureTransactionHandler.TYPE, handler);
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
	// Implementation of the MapViewChangedHandler interface:
	// -------------------------------------------------------------------------

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

	// -------------------------------------------------------------------------
	// Public methods:
	// -------------------------------------------------------------------------

	/**
	 * Refresh the map model. This will re-read the configuration and update the map model, toolbar etc.
	 * <p/>
	 * This should be called if you want the map to redraw itself. it is automatically called when the token changes.
	 *
	 * @since 1.10.0
	 */
	@Api
	public void refresh() {
		if (initCalled) { // to prevent refresh before the map is drawn
			clear();
			ClientConfigurationService.clear(); // refresh because configuration changed, clear cache
			refreshFromConfiguration();
		}
	}

	/**
	 * Initialize the map model. This will read the configuration.
	 * <p/>
	 * Make sure the handler are registered before initializing the map model or you may miss events.
	 * <p/>
	 * Only works the first time, use {@link #refresh()} later on.
	 *
	 * @since 1.10.0
	 */
	@Api
	public void init() {
		if (!initCalled) {
			initCalled = true;
			refreshFromConfiguration();
		}
	}

	/**
	 * Clear the map model. Removes all layers and tools.
	 *
	 * @since 1.10.0
	 */
	@Api
	public void clear() {
		initialized = false;
		handlerManager.fireEvent(new MapModelClearEvent(this));
		layers.clear();
	}

	private void refreshFromConfiguration() {
		ClientConfigurationService.getApplicationWidgetInfo(applicationId, id, new
				WidgetConfigurationCallback<ClientMapInfo>() {

					public void execute(ClientMapInfo mapInfo) {
						if (null == mapInfo) {
							Log.logError("Cannot find map with id " + id);
						} else {
							refresh(mapInfo);
						}
					}
				});
	}

	/**
	 * Refresh the MapModel object, using a configuration object acquired from the server. This will automatically
	 * build the list of layers.
	 *
	 * @param mapInfo The configuration object.
	 */
	private void refresh(final ClientMapInfo mapInfo) {
		boolean firstRefresh = !initialized;
		actualRefresh(mapInfo);
		if (firstRefresh) {
			// only change the initial bounds the first time around
			Bbox initialBounds = new Bbox(mapInfo.getInitialBounds());
			mapView.applyBounds(initialBounds, MapView.ZoomOption.LEVEL_CLOSEST);
			initialized = true;
		}
		fireRefreshEvents();
		
		while (whenInitializedRunnables.size() > 0) {
			Runnable runnable = whenInitializedRunnables.remove(0);
			runnable.run();
		}
	}

	private void fireRefreshEvents() {
		// fire first for backwards compatibility (make sure old event listeners have been called)
		handlerManager.fireEvent(new MapModelChangedEvent(this));
		if (!mapModelEventFired) {
			handlerManager.fireEvent(new MapModelEvent());
		}
		mapModelEventFired = true;
	}

	/**
	 * Refresh the MapModel object, using a configuration object acquired from the server. This will automatically
	 * build the list of layers.
	 *
	 * @param mapInfo
	 *            The configuration object.
	 */
	private void actualRefresh(final ClientMapInfo mapInfo) {
		if (null == mapInfo) {
			Log.logError("Cannot find map with id " + id);
			return;
		}
		this.mapInfo = mapInfo;
		srid = 0;
		try {
			int pos = mapInfo.getCrs().indexOf(":");
			if (pos >= 0) {
				srid = Integer.parseInt(mapInfo.getCrs().substring(pos + 1));
			}
		} catch (NumberFormatException nfe) {
			// warning logged below
		}
		if (0 == srid) {
			Log.logWarn("Cannot parse CRS for map " + id + ", CRS=" + mapInfo.getCrs());
		}

		ScaleConfigurationInfo scaleConfigurationInfo = mapInfo.getScaleConfiguration();
		List<Double> realResolutions = new ArrayList<Double>();
		for (ScaleInfo scale : scaleConfigurationInfo.getZoomLevels()) {
			realResolutions.add(1. / scale.getPixelPerUnit());
		}
		mapView.setResolutions(realResolutions);
		mapView.setMaximumScale(scaleConfigurationInfo.getMaximumScale().getPixelPerUnit());

		// replace layers by new layers
		removeAllLayers();
		for (ClientLayerInfo layerInfo : mapInfo.getLayers()) {
			addLayer(layerInfo);
		}

		Bbox maxBounds = new Bbox(mapInfo.getMaxBounds());
		Bbox initialBounds = new Bbox(mapInfo.getInitialBounds());
		// if the max bounds was not configured, take the union of initial and layer bounds
		if (maxBounds.isAll()) {
			for (ClientLayerInfo layerInfo : mapInfo.getLayers()) {
				maxBounds = (Bbox) initialBounds.clone();
				maxBounds = maxBounds.union(new Bbox(layerInfo.getMaxExtent()));
			}
		}
		mapView.setMaxBounds(maxBounds);
		
		if (null == mapInfo.getViewBoundsLimitOption()) {
			mapView.setViewBoundsLimitOption(BoundsLimitOption.COMPLETELY_WITHIN_MAX_BOUNDS);
		} else {
			mapView.setViewBoundsLimitOption(mapInfo.getViewBoundsLimitOption());
		}
	}

	/**
	 * Is this map model initialized yet ?
	 * 
	 * @return true if initialized
	 * @since 1.6.0
	 */
	@Api
	public boolean isInitialized() {
		return initialized;
	}

	/**
	 * Search a layer by it's id.
	 * 
	 * @param layerId
	 *            The layer's client ID.
	 * @return Returns either a Layer, or null.
	 * @since 1.6.0
	 */
	@Api
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
	 * @since 1.6.0
	 */
	@Api
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
	 * Return a list containing all vector layers within this model.
	 *
	 * @return vector layers
	 */
	public List<VectorLayer> getVectorLayers() {
		ArrayList<VectorLayer> list = new ArrayList<VectorLayer>();
		for (Layer<?> layer : layers) {
			if (layer instanceof VectorLayer) {
				list.add((VectorLayer) layer);
			}
		}
		return list;
	}

	/** Clear the list of selected features in all vector layers. */
	public void clearSelectedFeatures() {
		for (VectorLayer layer : getVectorLayers()) {
			layer.clearSelectedFeatures();
		}
	}

	/**
	 * Return the total number of selected features in all vector layers.
	 *
	 * @return number of selected features
	 */
	public int getNrSelectedFeatures() {
		int count = 0;
		for (VectorLayer layer : getVectorLayers()) {
			count += layer.getSelectedFeatures().size();
		}
		return count;
	}
	
	/**
	 * Return the selected feature if there is 1 selected feature.
	 * 
	 * @return the selected feature or null if none or multiple features are selected
	 */
	public String getSelectedFeature() {
		if (getNrSelectedFeatures() == 1) {
			for (VectorLayer layer : getVectorLayers()) {
				if (layer.getSelectedFeatures().size() > 0) {
					return layer.getSelectedFeatures().iterator().next();
				}
			}
		}
		return null;
	}
	
	/**
	 * Zoom to the bounds of the specified features.
	 * 
	 * @param features list of features, will be lazy-loaded if necessary
	 * @since 1.11.0
	 */
	@Api
	public void zoomToFeatures(List<Feature> features) {
		// calculate the point scale as the minimum point scale for all layers (only relevant for zooming to multiple
		// points at the exact same location)
		double zoomToPointScale = getMapInfo().getScaleConfiguration().getMaximumScale().getPixelPerUnit();
		for (Feature feature : features) {
			double scale = feature.getLayer().getLayerInfo().getZoomToPointScale().getPixelPerUnit();
			zoomToPointScale = Math.min(zoomToPointScale,  scale);
		}
		ZoomToFeaturesLazyLoadCallback callback = new ZoomToFeaturesLazyLoadCallback(features.size(), 
				zoomToPointScale);
		for (Feature feature : features) {
			// no need to fetch if we already have the geometry !
			if (feature.isGeometryLoaded()) {
				callback.execute(Arrays.asList(feature));
			} else {
				feature.getLayer().getFeatureStore()
						.getFeature(feature.getId(), GeomajasConstant.FEATURE_INCLUDE_GEOMETRY, callback);			
			}
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

	/**
	 * Apply a certain feature transaction onto the client side map model. This method is usually called after that same
	 * feature transaction has been successfully applied on the server.
	 * 
	 * @param ft
	 *            The feature transaction to apply. It can create, update or delete features.
	 */
	public void applyFeatureTransaction(FeatureTransaction ft) {
		if (ft != null) {
			VectorLayer layer = ft.getLayer();
			if (layer != null) {
				// clear all the tiles
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
			handlerManager.fireEvent(new FeatureTransactionEvent(ft));
		}
	}

	/**
	 * Set a new position for the given layer. This will automatically redraw the map to apply this new order. Note that
	 * at any time, all raster layers will always lie behind all vector layers. This means that position 0 for a vector
	 * layer is the first(=back) vector layer to be drawn AFTER all raster layers have already been drawn.
	 * 
	 * @param layer
	 *            The vector layer to place at a new position.
	 * @param position
	 *            The new layer order position in the layer array:
	 *            <ul>
	 *            <li>Back = 0 (but still in front of all raster layers)</li>
	 *            <li>Front = (vector layer count - 1)</li>
	 *            </ul>
	 * @return Returns if the re-ordering was successful or not.
	 * @since 1.8.0
	 */
	public boolean moveVectorLayer(VectorLayer layer, int position) {
		if (layer == null) {
			return false;
		}

		// Find attached ClientLayerInfo object:
		ClientLayerInfo layerInfo = null;
		String layerId = layer.getId();
		for (ClientLayerInfo info : mapInfo.getLayers()) {
			if (info.getId().equals(layerId)) {
				layerInfo = info;
				break;
			}
		}
		if (layerInfo == null) {
			return false;
		}

		// First remove the layer from the list:
		if (!layers.remove(layer)) {
			return false;
		}
		if (!mapInfo.getLayers().remove(layerInfo)) {
			return false;
		}

		int rasterCount = rasterLayerCount();
		position += rasterCount;
		if (position < rasterCount) {
			position = rasterCount;
		} else if (position > layers.size()) {
			position = layers.size();
		}
		try {
			layers.add(position, layer);
			mapInfo.getLayers().add(position, layerInfo);
		} catch (Exception e) {
			return false;
		}
		handlerManager.fireEvent(new MapModelChangedEvent(this));
		return true;
	}

	/**
	 * Set a new position for the given layer. This will automatically redraw the map to apply this new order. Note that
	 * at any time, all raster layers will always lie behind all vector layers. This means that position 0 for a vector
	 * layer is the first(=back) vector layer to be drawn AFTER all raster layers have already been drawn.
	 * 
	 * @param layer
	 *            The raster layer to place at a new position.
	 * @param position
	 *            The new layer order position in the layer array:
	 *            <ul>
	 *            <li>Back = 0</li>
	 *            <li>Front = (raster layer count - 1); Larger numbers won't make a difference. Rasters stay behind
	 *            vectors...</li>
	 *            </ul>
	 * @return Returns if the re-ordering was successful or not.
	 * @since 1.8.0
	 */
	public boolean moveRasterLayer(RasterLayer layer, int position) {
		if (layer == null) {
			return false;
		}

		// Find attached ClientLayerInfo object:
		ClientLayerInfo layerInfo = null;
		String layerId = layer.getId();
		for (ClientLayerInfo info : mapInfo.getLayers()) {
			if (info.getId().equals(layerId)) {
				layerInfo = info;
				break;
			}
		}
		if (layerInfo == null) {
			return false;
		}

		int rasterCount = rasterLayerCount();

		// First remove the layer from the list:
		if (!layers.remove(layer)) {
			return false;
		}
		if (!mapInfo.getLayers().remove(layerInfo)) {
			return false;
		}

		if (position < 0) {
			position = 0;
		} else if (position > rasterCount - 1) {
			position = rasterCount - 1;
		}
		try {
			layers.add(position, layer);
			mapInfo.getLayers().add(position, layerInfo);
		} catch (Exception e) {
			return false;
		}
		handlerManager.fireEvent(new MapModelChangedEvent(this));
		return true;
	}

	/**
	 * Move a vector layer up (=front) one place. Note that at any time, all raster layers will always lie behind all
	 * vector layers. This means that position 0 for a vector layer is the first(=back) vector layer to be drawn AFTER
	 * all raster layers have already been drawn.
	 * 
	 * @param layer
	 *            The vector layer to move more to the front.
	 * @return Returns if the re-ordering was successful or not.
	 * @since 1.8.0
	 */
	public boolean moveVectorLayerUp(VectorLayer layer) {
		int position = getLayerPosition(layer);
		return position >= 0 && moveVectorLayer(layer, position + 1);
	}

	/**
	 * Move a vector layer down (=back) one place. Note that at any time, all raster layers will always lie behind all
	 * vector layers. This means that position 0 for a vector layer is the first(=back) vector layer to be drawn AFTER
	 * all raster layers have already been drawn.
	 * 
	 * @param layer
	 *            The vector layer to move more to the back.
	 * @return Returns if the re-ordering was successful or not.
	 * @since 1.8.0
	 */
	public boolean moveVectorLayerDown(VectorLayer layer) {
		int position = getLayerPosition(layer);
		return position >= 0 && moveVectorLayer(layer, position - 1);
	}

	/**
	 * Move a raster layer up (=front) one place. Note that at any time, all raster layers will always lie behind all
	 * vector layers. This means that position 0 for a vector layer is the first(=back) vector layer to be drawn AFTER
	 * all raster layers have already been drawn.
	 * 
	 * @param layer
	 *            The raster layer to move more to the front.
	 * @return Returns if the re-ordering was successful or not.
	 * @since 1.8.0
	 */
	public boolean moveRasterLayerUp(RasterLayer layer) {
		int position = getLayerPosition(layer);
		return position >= 0 && moveRasterLayer(layer, position + 1);
	}

	/**
	 * Move a raster layer down (=back) one place. Note that at any time, all raster layers will always lie behind all
	 * vector layers. This means that position 0 for a vector layer is the first(=back) vector layer to be drawn AFTER
	 * all raster layers have already been drawn.
	 * 
	 * @param layer
	 *            The raster layer to move more to the back.
	 * @return Returns if the re-ordering was successful or not.
	 * @since 1.8.0
	 */
	public boolean moveRasterLayerDown(RasterLayer layer) {
		int position = getLayerPosition(layer);
		return position >= 0 && moveRasterLayer(layer, position - 1);
	}

	/**
	 * Get the position of a certain layer in this map model. Note that for both raster layers and vector layer, the
	 * count starts at 0! On the map, all raster layers always lie behind all vector layers.
	 * 
	 * @param layer
	 *            The layer to return the position for.
	 * @return Returns the position of the layer in the map. This position determines layer order.
	 * @since 1.8.0
	 */
	public int getLayerPosition(Layer<?> layer) {
		if (layer == null) {
			return -1;
		}
		String layerId = layer.getId();
		if (layer instanceof RasterLayer) {
			for (int index = 0; index < mapInfo.getLayers().size(); index++) {
				if (mapInfo.getLayers().get(index).getId().equals(layerId)) {
					return index;
				}
			}
		} else if (layer instanceof VectorLayer) {
			int rasterCount = 0;
			for (int index = 0; index < mapInfo.getLayers().size(); index++) {
				if (layers.get(index) instanceof RasterLayer) {
					rasterCount++;
				}
				if (mapInfo.getLayers().get(index).getId().equals(layerId)) {
					return index - rasterCount;
				}
			}
		}
		return 0;
	}

	// -------------------------------------------------------------------------
	// Getters:
	// -------------------------------------------------------------------------

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
	 * Return a factory for geometries that is suited perfectly for geometries within this model. The SRID and precision
	 * will for the factory will be correct.
	 *
	 * @return geometry factory
	 */
	public GeometryFactory getGeometryFactory() {
		if (null == geometryFactory) {
			if (0 == srid) {
				throw new IllegalArgumentException("srid needs to be set on MapModel to obtain GeometryFactory");
			}
			geometryFactory = new GeometryFactory(srid, -1); // @todo precision is not yet implemented
		}
		return geometryFactory;
	}

	// -------------------------------------------------------------------------
	// Private methods:
	// -------------------------------------------------------------------------

	private void removeAllLayers() {
		layers = new ArrayList<Layer<?>>();
	}

	private void addLayer(ClientLayerInfo layerInfo) {
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

	/**
	 * Count the total number of raster layers in this model.
	 *
	 * @return number of raster layers
	 */
	private int rasterLayerCount() {
		int rasterLayerCount = 0;
		for (int index = 0; index < mapInfo.getLayers().size(); index++) {
			if (layers.get(index) instanceof RasterLayer) {
				rasterLayerCount++;
			}
		}
		return rasterLayerCount;
	}

	// -------------------------------------------------------------------------
	// Private classes:
	// -------------------------------------------------------------------------

	/**
	 * Propagates layer selection events to interested listeners.
	 * 
	 * @author Jan De Moerloose
	 * 
	 */
	private class LayerSelectionPropagator implements FeatureSelectionHandler {

		public void onFeatureDeselected(FeatureDeselectedEvent event) {
			handlerManager.fireEvent(event);
		}

		public void onFeatureSelected(FeatureSelectedEvent event) {
			handlerManager.fireEvent(event);
		}
	}
	
	/**
	 * Stateful callback that zooms to bounds when all features have been retrieved.
	 * 
	 * @author Kristof Heirwegh
	 * @author Jan De Moerloose
	 */
	private class ZoomToFeaturesLazyLoadCallback implements LazyLoadCallback {

		private int featureCount;
		private Bbox bounds;
		private double pointScale;

		public ZoomToFeaturesLazyLoadCallback(int featureCount, double pointScale) {
			this.featureCount = featureCount;
			this.pointScale  = pointScale;
		}

		public void execute(List<Feature> response) {
			if (response != null && response.size() > 0) {
				if (bounds == null) {
					bounds = (Bbox) response.get(0).getGeometry().getBounds().clone();
				} else {
					bounds = bounds.union(response.get(0).getGeometry().getBounds());
				}
			}
			featureCount--;
			if (featureCount == 0) {
				if (bounds != null) {
					if (bounds.getWidth() > 0 || bounds.getHeight() > 0) {
						getMapView().applyBounds(bounds, ZoomOption.LEVEL_FIT);
					} else {
						getMapView().setCenterPosition(bounds.getCenterPoint());
						getMapView().setCurrentScale(pointScale, ZoomOption.LEVEL_CLOSEST);
					}
				}
			}
		}
	}

}
