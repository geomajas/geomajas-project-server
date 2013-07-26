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
package org.geomajas.gwt.client.event;

import org.geomajas.annotation.Api;

import com.google.web.bindery.event.shared.Event;

/**
 * Event that reports resizing of the map.
 * 
 * @author Pieter De Graef
 * @since 1.0.0
 */
@Api(allMethods = true)
public class MapResizedEvent extends Event<MapResizedHandler> {

	private final int mapWidth;

	private final int mapHeight;

	/**
	 * Create an event with the specified map width and height.
	 * 
	 * @param mapWidth the new map width
	 * @param mapHeight the new map height
	 */
	public MapResizedEvent(int mapWidth, int mapHeight) {
		this.mapWidth = mapWidth;
		this.mapHeight = mapHeight;
	}

	@Override
	public Type<MapResizedHandler> getAssociatedType() {
		return MapResizedHandler.TYPE;
	}

	@Override
	protected void dispatch(MapResizedHandler mapInitializationHandler) {
		mapInitializationHandler.onMapResized(this);
	}

	/**
	 * Get the new map width in pixels.
	 * 
	 * @return The new map width in pixels.
	 */
	public int getMapWidth() {
		return mapWidth;
	}

	/**
	 * Get the new map height in pixels.
	 * 
	 * @return The new map height in pixels.
	 */
	public int getMapHeight() {
		return mapHeight;
	}
}