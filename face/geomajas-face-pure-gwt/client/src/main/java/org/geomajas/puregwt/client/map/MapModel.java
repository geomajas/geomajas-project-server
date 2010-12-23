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

package org.geomajas.puregwt.client.map;

import java.util.List;

import org.geomajas.configuration.client.ClientMapInfo;
import org.geomajas.global.Api;
import org.geomajas.puregwt.client.map.layer.Layer;

/**
 * The moving up and down of layers, knows no difference between vector and raster layers (the old MapModel did make
 * this difference).<br/>
 * Also, everywhere <code>mapId</code> is used instead of a real Map object to identify a map.<br/>
 * <p>
 * TODO check the javadoc for the moving of layers
 * </p>
 * 
 * @author Pieter De Graef
 * @author Jan De Moerloose
 * @since 1.0.0
 */
@Api
public interface MapModel {

	/**
	 * Initialization method for the map model.
	 * 
	 * @param mapInfo
	 *            The configuration object from which this model should build itself.
	 */
	void initialize(ClientMapInfo mapInfo);

	/**
	 * Returns the ordered list of layers within this map. The first layer lies at the bottom, the last one on top.
	 * 
	 * @return The full list of layers.
	 */
	List<Layer<?>> getLayers();

	/**
	 * Get a single layer by its identifier.
	 * 
	 * @param id
	 *            The layers unique identifier within this map.
	 * @return Returns the layer, or null if it could not be found.
	 */
	Layer<?> getLayer(String id);

	/**
	 * Return the total number of layers within this map.
	 * 
	 * @return The layer count.
	 */
	int getLayerCount();

	/**
	 * Set a new position for the given layer. This will automatically redraw the map to apply this new order. Note that
	 * at any time, layer rendered as raster will always lie behind layers rendered by SVG/VML objects.<br/>
	 * Note also that vector layers can be rendered as images, in which case they will count as rasterized layers here.
	 * 
	 * @param layer
	 *            The layer to place at a new position.
	 * @param position
	 *            The new layer order position in the layer array:
	 * @return Returns if the re-ordering was successful or not.
	 */
	boolean moveLayer(Layer<?> layer, int index);

	/**
	 * Move a layer up (=front) one place. This will automatically redraw the map to apply this new order. Note that at
	 * any time, layer rendered as raster will always lie behind layers rendered by SVG/VML objects.<br/>
	 * Note also that vector layers can be rendered as images, in which case they will count as rasterized layers here.
	 * 
	 * @param layer
	 *            The layer to move more to the front.
	 * @return Returns if the re-ordering was successful or not.
	 */
	boolean moveLayerUp(Layer<?> layer);

	/**
	 * Move a layer down (=back) one place. This will automatically redraw the map to apply this new order. Note that at
	 * any time, layer rendered as raster will always lie behind layers rendered by SVG/VML objects.<br/>
	 * Note also that vector layers can be rendered as images, in which case they will count as rasterized layers here.
	 * 
	 * @param layer
	 *            The layer to move more to the front.
	 * @return Returns if the re-ordering was successful or not.
	 */
	boolean moveLayerDown(Layer<?> layer);

	/**
	 * Get the position of a certain layer in this map model.
	 * 
	 * @param layer
	 *            The layer to return the position for.
	 * @return Returns the position of the layer in the map. This position determines layer order.
	 */
	int getLayerPosition(Layer<?> layer);

	/**
	 * Return the layer at a certain index. If the index can't be found, null is returned.
	 * 
	 * @param index
	 *            The specified index.
	 * @return Returns the layer, or null if the index can't be found.
	 */
	Layer<?> getLayer(int index);

	/**
	 * Return the currently selected layer within this map model.
	 * 
	 * @return Returns the selected layer, or null if no layer is selected.
	 */
	Layer<?> getSelectedLayer();

	/**
	 * Returns the {@link ViewPort} associated with this map.
	 * 
	 * @return Returns the view port.
	 */
	ViewPort getViewPort();
}