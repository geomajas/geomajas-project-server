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
package org.geomajas.jsapi.map;

import org.geomajas.jsapi.map.layer.Layer;
import org.geomajas.jsapi.map.layer.VectorLayer;

/**
 * Javascript exportable facade for a map's LayersModel.
 * 
 * @author Oliver May
 * 
 */
public interface LayersModel {

	/**
	 * Get a single VectorLayer by its identifier.
	 * 
	 * @param id
	 *            The layers unique identifier within this map.
	 * @return Returns the layer, or null if it could not be found.
	 */
	VectorLayer getVectorLayer(String layerId);

	/**
	 * Get a single layer by its identifier.
	 * 
	 * @param id
	 *            The layers unique identifier within this map.
	 * @return Returns the layer, or null if it could not be found.
	 */
	Layer getLayer(String layerId);

	/**
	 * Get the list of all VectorLayers.
	 * 
	 * @return a list of vector layers.
	 */
	String[] getVectorLayerIds();

	/**
	 * Get the list of all Layers.
	 * 
	 * @return a list of layers.
	 */
	String[] getLayerIds();
}
