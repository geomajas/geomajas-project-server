package org.geomajas.gwt.client.map;

import java.util.List;

import org.geomajas.configuration.client.ClientMapInfo;
import org.geomajas.gwt.client.map.layer.Layer;

/**
 * The moving up and down of layers, knows no difference between vector and raster layers (the old MapModel did make
 * this difference).<br/>
 * Also, everywhere <code>mapId</code> is used instead of a real Map object to identify a map.<br/>
 * 
 * @author Pieter De Graef
 */
public interface MapModel {

	List<Layer<?>> getLayers();

	Layer<?> getLayer(String id);

	int getLayerCount();

	boolean moveLayer(Layer<?> layer, int index);

	boolean moveLayerUp(Layer<?> layer);

	boolean moveLayerDown(Layer<?> layer);

	int getLayerPosition(Layer<?> layer);
	
	Layer<?> getLayer(int index);

	Layer<?> getSelectedLayer();
		
	ViewPort getViewPort();
	
	void initialize(ClientMapInfo mapInfo);

}