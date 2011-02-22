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

package org.geomajas.puregwt.client.map;

import java.util.ArrayList;
import java.util.List;

import org.geomajas.configuration.client.ClientLayerInfo;
import org.geomajas.configuration.client.ClientMapInfo;
import org.geomajas.configuration.client.ScaleConfigurationInfo;
import org.geomajas.configuration.client.ScaleInfo;
import org.geomajas.puregwt.client.map.layer.Layer;
import org.geomajas.puregwt.client.spatial.Bbox;
import org.geomajas.puregwt.client.spatial.BboxImpl;

/**
 * ...
 * 
 * @author Pieter De Graef
 */
public class MapModelImpl implements MapModel {

	private String id;

	private ClientMapInfo mapInfo;

	private ViewPort viewPort;

	/**
	 * An ordered list of layers. The drawing order on the map is as follows: the first layer will be placed at the
	 * bottom, the last layer on top.
	 */
	private List<Layer<?>> layers = new ArrayList<Layer<?>>();

	// ------------------------------------------------------------------------
	// Constructors:
	// ------------------------------------------------------------------------

	public MapModelImpl(String id) {
		this.id = id;
	}

	// ------------------------------------------------------------------------
	// MapModel implementation:
	// ------------------------------------------------------------------------

	/**
	 * Initialization method for the map model. TODO why not do this in the constructor??
	 * 
	 * @param mapInfo
	 *            The configuration object from which this model should build itself.
	 */
	public void initialize(ClientMapInfo mapInfo) {
		this.mapInfo = mapInfo;
		// srid = Integer.parseInt(mapInfo.getCrs().substring(mapInfo.getCrs().indexOf(":") + 1));
		ScaleConfigurationInfo scaleConfigurationInfo = mapInfo.getScaleConfiguration();
		List<Double> realResolutions = new ArrayList<Double>();
		for (ScaleInfo scale : scaleConfigurationInfo.getZoomLevels()) {
			realResolutions.add(1. / scale.getPixelPerUnit());
		}
		Bbox maxBounds = new BboxImpl(mapInfo.getMaxBounds());
		Bbox initialBounds = new BboxImpl(mapInfo.getInitialBounds());
		// if the max bounds was not configured, take the union of initial and layer bounds
		Bbox all = new BboxImpl(org.geomajas.geometry.Bbox.ALL);
		if (maxBounds.equals(all)) {
			for (ClientLayerInfo layerInfo : mapInfo.getLayers()) {
				maxBounds = new BboxImpl(initialBounds);
				maxBounds = maxBounds.union(new BboxImpl(layerInfo.getMaxExtent()));
			}
		}
		viewPort = new ViewPortImpl(realResolutions, scaleConfigurationInfo.getMaximumScale().getPixelPerUnit(),
				maxBounds);
		layers = new ArrayList<Layer<?>>();
		for (ClientLayerInfo layerInfo : mapInfo.getLayers()) {
			addLayer(layerInfo);
		}

		// Zoom to the initial bounds as configured in the MapInfo:
		viewPort.applyBounds(initialBounds, ZoomOption.LEVEL_CLOSEST);
	}

	/**
	 * Get a single layer by its identifier.
	 * 
	 * @param id
	 *            The layers unique identifier within this map.
	 * @return Returns the layer, or null if it could not be found.
	 */
	public Layer<?> getLayer(String id) {
		if (id == null) {
			throw new NullPointerException("Null ID passed to the getLayer method.");
		}
		for (Layer<?> layer : layers) {
			if (id.equals(layer.getId())) {
				return layer;
			}
		}
		return null;
	}

	/**
	 * Return the total number of layers within this map.
	 * 
	 * @return The layer count.
	 */
	public int getLayerCount() {
		return layers.size();
	}

	public boolean moveLayer(Layer<?> layer, int index) {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean moveLayerUp(Layer<?> layer) {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean moveLayerDown(Layer<?> layer) {
		// TODO Auto-generated method stub
		return false;
	}

	/**
	 * Get the position of a certain layer in this map model.
	 * 
	 * @param layer
	 *            The layer to return the position for.
	 * @return Returns the position of the layer in the map. This position determines layer order. If the layer was not
	 *         found, than -1 is returned.
	 */
	public int getLayerPosition(Layer<?> layer) {
		if (layer == null) {
			return -1;
		}
		for (int i = 0; i < layers.size(); i++) {
			if (layer.getId().equals(layers.get(i).getId())) {
				return i;
			}
		}
		return -1;
	}

	/**
	 * Return the layer at a certain index. If the index can't be found, null is returned.
	 * 
	 * @param index
	 *            The specified index.
	 * @return Returns the layer, or null if the index can't be found.
	 */
	public Layer<?> getLayer(int index) {
		return layers.get(index);
	}

	/**
	 * Return the currently selected layer within this map model.
	 * 
	 * @return Returns the selected layer, or null if no layer is selected.
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
	 * Returns the {@link ViewPort} associated with this map.
	 * 
	 * @return Returns the view port.
	 */
	public ViewPort getViewPort() {
		return viewPort;
	}

	// -------------------------------------------------------------------------
	// Private methods:
	// -------------------------------------------------------------------------

	private void addLayer(ClientLayerInfo layerInfo) {
		switch (layerInfo.getLayerType()) {
			case RASTER:
				// RasterLayer rasterLayer = new RasterLayer(this, (ClientRasterLayerInfo) layerInfo);
				// layers.add(rasterLayer);
				break;
			default:
				// VectorLayer vectorLayer = new VectorLayer(this, (ClientVectorLayerInfo) layerInfo);
				// layers.add(vectorLayer);
				// vectorLayer.addFeatureSelectionHandler(selectionPropagator);
				break;
		}
	}
}