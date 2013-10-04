/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2013 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */

package org.geomajas.gwt2.client.map.layer;

import org.geomajas.annotation.Api;
import org.geomajas.configuration.client.ClientMapInfo;
import org.geomajas.gwt2.client.map.MapEventBus;
import org.geomajas.gwt2.client.map.ViewPort;


/**
 * <p>
 * Definition of the model that describes the layers that make up a map. The methods within this model all revolve
 * around layer manipulation. This means that there are methods here to add and remove layers, to move layers up and
 * down, to retrieve layers by their identifiers or index in the list, etc...
 * </p>
 * 
 * @author Pieter De Graef
 * @author Jan De Moerloose
 * @since 1.0.0
 */
@Api(allMethods = true)
public interface LayersModel {

	/**
	 * Initialization method for the layers model.
	 * 
	 * @param mapInfo
	 *            The configuration object from which this model should build itself.
	 * @param viewPort
	 *            The view port that is associated with the same map this layer model belongs to.
	 * @param eventBus
	 *            Event bus that governs all event related to this layers model.
	 */
	void initialize(ClientMapInfo mapInfo, ViewPort viewPort, MapEventBus eventBus);

	/**
	 * Add a new layer to the layers model. The new layer will be added at the back of the list (where the back of the
	 * list is rendered on top).
	 * 
	 * @param layer The layer to be added to the model.
	 * @return True or false, indicating success or not.
	 */
	boolean addLayer(Layer layer);

	/**
	 * Remove a layer from this layers model. This will also remove the layer from the map.
	 * 
	 * @param id
	 *            The layers unique identifier within this map.
	 * @return True or false, indicating success or not.
	 */
	boolean removeLayer(String id);

	/**
	 * Get a single layer by its identifier.
	 * 
	 * @param id
	 *            The layers unique identifier within this map.
	 * @return Returns the layer, or null if it could not be found.
	 */
	Layer getLayer(String id);

	/**
	 * Return the total number of layers within this map.
	 * 
	 * @return The layer count.
	 */
	int getLayerCount();

	/**
	 * Set a new position for the given layer. This will automatically redraw the map to apply this new order. Note that
	 * at any time, layers rendered through HTML will always lie behind layers rendered through SVG/VML objects.<br/>
	 * Note also that vector layers can be rendered as images, in which case they will count as HTML layers here.
	 * 
	 * @param layer
	 *            The layer to place at a new position.
	 * @param index
	 *            The new layer order position in the layer array:
	 * @return Returns if the re-ordering was successful or not.
	 */
	boolean moveLayer(Layer layer, int index);

	/**
	 * Move a layer up (=front) one place. This will automatically redraw the map to apply this new order. Note that at
	 * any time, layers rendered through HTML will always lie behind layers rendered through SVG/VML objects.<br/>
	 * Note also that vector layers can be rendered as images, in which case they will count as HTML layers here.
	 * 
	 * @param layer
	 *            The layer to move more to the front.
	 * @return Returns if the re-ordering was successful or not.
	 */
	boolean moveLayerUp(Layer layer);

	/**
	 * Move a layer down (=back) one place. This will automatically redraw the map to apply this new order. Note that at
	 * any time, layers rendered through HTML will always lie behind layers rendered through SVG/VML objects.<br/>
	 * Note also that vector layers can be rendered as images, in which case they will count as HTML layers here.
	 * 
	 * @param layer
	 *            The layer to move more to the front.
	 * @return Returns if the re-ordering was successful or not.
	 */
	boolean moveLayerDown(Layer layer);

	/**
	 * Get the position of a certain layer in this map model.
	 * 
	 * @param layer
	 *            The layer to return the position for.
	 * @return Returns the position of the layer in the map. This position determines layer order. If the layer was not
	 *         found, than -1 is returned.
	 */
	int getLayerPosition(Layer layer);

	/**
	 * Return the layer at a certain index. If the index can't be found, null is returned.
	 * 
	 * @param index
	 *            The specified index.
	 * @return Returns the layer, or null if the index can't be found.
	 */
	Layer getLayer(int index);

	/**
	 * Return the currently selected layer within this map model.
	 * 
	 * @return Returns the selected layer, or null if no layer is selected.
	 */
	Layer getSelectedLayer();
}