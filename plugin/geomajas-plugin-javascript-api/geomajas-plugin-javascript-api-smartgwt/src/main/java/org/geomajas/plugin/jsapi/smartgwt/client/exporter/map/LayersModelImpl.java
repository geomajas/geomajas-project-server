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
package org.geomajas.plugin.jsapi.smartgwt.client.exporter.map;

import java.util.ArrayList;
import java.util.List;

import org.geomajas.global.FutureApi;
import org.geomajas.gwt.client.map.layer.Layer;
import org.geomajas.jsapi.map.LayersModel;
import org.geomajas.plugin.jsapi.smartgwt.client.exporter.map.layer.LayerImpl;
import org.geomajas.plugin.jsapi.smartgwt.client.exporter.map.layer.VectorLayerImpl;
import org.timepedia.exporter.client.Export;
import org.timepedia.exporter.client.ExportPackage;
import org.timepedia.exporter.client.Exportable;


/**
 * Exportable facade for {@link org.geomajas.gwt.client.map.MapModel}.
 * 
 * 
 * @author Oliver May
 */
@Export
@ExportPackage("org.geomajas.jsapi.map")
public class LayersModelImpl implements Exportable, LayersModel {
	
	private org.geomajas.gwt.client.map.MapModel mapModel;
	
	/**
	 * Construct the wrapper based on the given mapModel.
	 * 
	 * @param mapModel the mapModel to wrap.
	 * @since 1.0.0
	 */
	@FutureApi
	public LayersModelImpl(org.geomajas.gwt.client.map.MapModel mapModel) {
		this.mapModel = mapModel;
	}
	
	/**
	 * Is the map model initialized yet ?
	 *  
	 * @return true if initialized
	 */
	public boolean isInitialized() {
		return mapModel.isInitialized();
	}
	
	
	/**
	 * Search a vector layer by it's id.
	 * 
	 * @param layerId
	 *            The layer's client ID.
	 * @return Returns either a Layer, or null.
	 */
	public VectorLayerImpl getVectorLayer(String layerId) {
		org.geomajas.gwt.client.map.layer.VectorLayer layer = mapModel.getVectorLayer(layerId);
		if (null == layer) {
			return null;
		}
		return new VectorLayerImpl(layer);
	}
	
	public LayerImpl getLayer(String layerId) {
		// TODO Auto-generated method stub
		return null;
	}
	
	
	/**
	 * Return all available layers on the map. The layer ID is the client layer id.
	 * 
	 * @return an array of layer id's
	 */
	public String[] getLayerIds() {
		List<Layer<?>> layers = mapModel.getLayers();
		String[] layerIds = new String[layers.size()];
		
		for (int i = 0; i < layers.size() ; i++) {
			layerIds[i] = layers.get(i).getId();
		}
		return layerIds;
	}
	
	/**
	 * Return all available vectorlayers on the map. The layer ID is the client layer id.
	 * 
	 * @return an array of layer id's
	 */
	public String[] getVectorLayerIds() {
		List<org.geomajas.gwt.client.map.layer.VectorLayer> layers = mapModel.getVectorLayers();
		String[] layerIds = new String[layers.size()];
		
		for (int i = 0; i < layers.size() ; i++) {
			layerIds[i] = layers.get(i).getId();
		}
		return layerIds;
	}
	
	/**
	 * Return all selected features of all available layers.
	 * 
	 * @return an array of feature id's
	 */
	public String[] getSelectedFeatures() {
		ArrayList<String> features = new ArrayList<String>();
		for (org.geomajas.gwt.client.map.layer.VectorLayer vl : mapModel.getVectorLayers()) {
			features.addAll(vl.getSelectedFeatures());
		}
		return features.toArray(new String[0]);
	}
	
	/**
	 * Return all selected features of the given vectorlayer.
	 * 
	 * @return an array of feature id's
	 */
	public String[] getSelectedFeaturesForLayer(String layerId) {
		org.geomajas.gwt.client.map.layer.VectorLayer vectorLayer = mapModel.getVectorLayer(layerId);
		if (null != vectorLayer) {
			return (String[]) vectorLayer.getSelectedFeatures().toArray();
		}
		return new String[0];
	}

}
