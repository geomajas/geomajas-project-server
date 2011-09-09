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

import org.geomajas.global.FutureApi;


/**
 * Javascript exportable facade of the Map presentation.
 * See the specific implementation for details how to initialize.
 * 
 * The implementation should make sure the newly created Map is registered in a 
 * {@link org.geomajas.jsapi.MapRegistry}! This way created maps are guaranteed available trough Javascript.
 * 
 * @author Oliver May
 */

@FutureApi(allMethods = true)
public interface Map {

	/**
	 * Initialize the map. This method will try to fetch the associated map configuration from the server and apply it
	 * on return.
	 * 
	 * 
	 * @param applicationId the application id.
	 * @param mapId the map id.
	 */
	void initialize(String application, String mapId);

	/**
	 * Returns the layers model for this presenter. This model is the central layer handler for the map, with methods
	 * for getting layers, moving them up and down, adding or removing layers, ..
	 * 
	 * @return The layers model.
	 */
	LayersModel getLayersModel();
	
	/**
	 * Returns the {@link ViewPort} associated with this map. The view port regulates zooming and panning around the
	 * map, but also presents transformation methods for transforming vector objects between the different render
	 * spaces.
	 * 
	 * @return Returns the view port.
	 */
//	ViewPort getViewPort();

	/**
	 * Couples this map to an existing HTML element (div or span).
	 * @param id id of the element
	 */
	void setHtmlElementId(String id);

	/**
	 * Apply a new width and height on the map. Both parameters are expressed in pixels.
	 * 
	 * @param width
	 *            The new pixel width for the map.
	 * @param height
	 *            The new pixel height for the map.
	 */
	void setSize(int width, int height);

}
