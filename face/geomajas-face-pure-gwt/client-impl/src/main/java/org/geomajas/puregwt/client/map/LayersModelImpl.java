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
import org.geomajas.configuration.client.ClientRasterLayerInfo;
import org.geomajas.puregwt.client.event.EventBus;
import org.geomajas.puregwt.client.map.event.LayerAddedEvent;
import org.geomajas.puregwt.client.map.event.LayerDeselectedEvent;
import org.geomajas.puregwt.client.map.event.LayerOrderChangedEvent;
import org.geomajas.puregwt.client.map.event.LayerSelectedEvent;
import org.geomajas.puregwt.client.map.event.LayerSelectionHandler;
import org.geomajas.puregwt.client.map.layer.Layer;
import org.geomajas.puregwt.client.map.layer.RasterLayer;

/**
 * ...
 * 
 * @author Pieter De Graef
 */
public class LayersModelImpl implements LayersModel {

	private ClientMapInfo mapInfo;

	private ViewPort viewPort;

	private EventBus eventBus;

	/**
	 * An ordered list of layers. The drawing order on the map is as follows: the first layer will be placed at the
	 * bottom, the last layer on top.
	 */
	private List<Layer<?>> layers = new ArrayList<Layer<?>>();

	// ------------------------------------------------------------------------
	// Constructors:
	// ------------------------------------------------------------------------

	public LayersModelImpl(EventBus eventBus) {
		this.eventBus = eventBus;

		// Add a layer selection handler that allows only one selected layer at a time:
		eventBus.addHandler(LayerSelectionHandler.TYPE, new LayerSelectionHandler() {

			public void onSelectLayer(LayerSelectedEvent event) {
				for (Layer<?> layer : layers) {
					if (layer.isSelected() && !layer.equals(event.getLayer())) {
						layer.setSelected(false);
					}
				}
			}

			public void onDeselectLayer(LayerDeselectedEvent event) {
			}
		});
	}

	// ------------------------------------------------------------------------
	// MapModel implementation:
	// ------------------------------------------------------------------------

	/**
	 * Initialization method for the map model. TODO why not do this in the constructor??
	 * 
	 * @param mapInfo
	 *            The configuration object from which this model should build itself.
	 * @param mapWidth
	 *            The width of the map to apply.
	 * @param mapHeight
	 *            The height of the map to apply.
	 */
	public void initialize(ClientMapInfo mapInfo, ViewPort viewPort) {
		this.mapInfo = mapInfo;
		this.viewPort = viewPort;

		// Create all the layers:
		layers = new ArrayList<Layer<?>>();
		for (ClientLayerInfo layerInfo : mapInfo.getLayers()) {
			addLayer(layerInfo);
		}
	}

	public void addLayer(ClientLayerInfo layerInfo) {
		switch (layerInfo.getLayerType()) {
			case RASTER:
				RasterLayer layer = new RasterLayer((ClientRasterLayerInfo) layerInfo, viewPort, eventBus);
				layers.add(layer);
				eventBus.fireEvent(new LayerAddedEvent(layer));
				break;
			default:
				// VectorLayer vectorLayer = new VectorLayer(this, (ClientVectorLayerInfo) layerInfo);
				// layers.add(vectorLayer);
				// vectorLayer.addFeatureSelectionHandler(selectionPropagator);
				break;
		}
	}
	
	public boolean removeLayer(String id) {
		Layer<?> layer = getLayer(id);
		if (layer != null) {
			return layers.remove(layer);
		}
		return false;
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
		int currentIndex = getLayerPosition(layer);
		if (currentIndex < 0 || currentIndex == index) {
			return false;
		}
		ClientLayerInfo layerInfo = mapInfo.getLayers().get(currentIndex);

		// First remove the layer from the list:
		if (!layers.remove(layer)) {
			return false;
		}
		if (!mapInfo.getLayers().remove(layerInfo)) {
			return false;
		}

		// Check the new index:
		if (index < 0) {
			index = 0;
		} else if (index > layers.size()) {
			index = layers.size();
		}

		// Change the order:
		try {
			layers.add(index, layer);
			mapInfo.getLayers().add(index, layerInfo);
		} catch (Exception e) {
			return false;
		}

		// Send out the correct event:
		eventBus.fireEvent(new LayerOrderChangedEvent(currentIndex, index));
		return true;
	}

	public boolean moveLayerUp(Layer<?> layer) {
		int position = getLayerPosition(layer);
		if (position < 0) {
			return false;
		}
		return moveLayer(layer, position + 1);
	}

	public boolean moveLayerDown(Layer<?> layer) {
		int position = getLayerPosition(layer);
		if (position < 0) {
			return false;
		}
		return moveLayer(layer, position - 1);
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
}