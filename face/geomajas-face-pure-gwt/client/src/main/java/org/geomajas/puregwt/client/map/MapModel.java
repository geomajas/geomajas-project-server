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

import org.geomajas.configuration.client.ClientMapInfo;
import org.geomajas.global.Api;
import org.geomajas.puregwt.client.event.EventBus;
import org.geomajas.puregwt.client.map.layer.Layer;

/**
 * The moving up and down of layers, knows no difference between vector and raster layers (the old MapModel did make
 * this difference).<br/>
 * Also, everywhere <code>mapId</code> is used instead of a real Map object to identify a map.<br/>
 * <p>
 * TODO check the javadoc for the moving of layers<br/>
 * TODO do we need srid, crs, geometryfactory here again?
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
	void initialize(ClientMapInfo mapInfo, int mapWidth, int mapHeight);

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
	 * @param index
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
	 * @return Returns the position of the layer in the map. This position determines layer order. If the layer was not
	 *         found, than -1 is returned.
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

	/**
	 * Returns a map-specific event bus that fires all map/layer/feature related events.
	 * 
	 * @return The map specific event bus.
	 */
	EventBus getEventBus();

	/**
	 * Return the EPSG code of the reference coordinate system used in this map.
	 * 
	 * @return The EPSG code. Example: 'EPSG:4326'.
	 */
	String getEpsg();
}