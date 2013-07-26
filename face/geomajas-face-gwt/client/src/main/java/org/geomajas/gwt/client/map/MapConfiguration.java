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

package org.geomajas.gwt.client.map;

import org.geomajas.annotation.Api;
import org.geomajas.configuration.client.ClientMapInfo;
import org.geomajas.gwt.client.map.layer.Layer;

/**
 * Map configuration definition. Contains a server configuration object and a series of map hints to apply specific
 * parameters. One should define a {@link MapHint} constant for each configuration parameter.
 * 
 * @author Pieter De Graef
 * @since 1.0.0
 */
@Api(allMethods = true)
public interface MapConfiguration {

	// ------------------------------------------------------------------------
	// List of known map hints:
	// ------------------------------------------------------------------------

	/**
	 * {@link MapHint} that determines whether or not animation is used during navigation. This setting overrides the
	 * individual layer animation settings in the {@link MapConfiguration}. It's value should be of type
	 * <code>Boolean</code>.
	 */
	MapHint<Boolean> ANIMATION_ENABLED = new MapHint<Boolean>("animation_enabled");

	/**
	 * {@link MapHint} used to determine how long the animations should take during navigation (zooming). The value
	 * should be expressed in milliseconds. It's value should be of type <code>Long</code>.
	 * 
	 * @since 1.0.0
	 */
	MapHint<Long> ANIMATION_TIME = new MapHint<Long>("animation_time");

	// ------------------------------------------------------------------------
	// Working with map hints:
	// ------------------------------------------------------------------------

	/**
	 * Apply a new value for a specific map hint.
	 * 
	 * @param hint
	 *            The hint to change the value for.
	 * @param value
	 *            The new actual value. If the value is null, an IllegalArgumentException is thrown.
	 */
	<T> void setMapHintValue(MapHint<T> hint, T value);

	/**
	 * Get the value for a specific map hint. All hints have a default value, so this method will never return
	 * <code>null</code>.
	 * 
	 * @param hint The hint to retrieve the current value for.
	 * @return The map hint value.
	 */
	<T> T getMapHintValue(MapHint<T> hint);


	// ------------------------------------------------------------------------
	// Getters and setters:
	// ------------------------------------------------------------------------

	/**
	 * <p>
	 * Get the server-side configuration object associated with this map. This configuration only becomes available
	 * after the map has been successfully initialized, as this object first needs to be fetched from the server.
	 * </p>
	 * <p>
	 * Never change the values within this object!
	 * </p>
	 * 
	 * @return The server-side configuration object associated with this map.
	 */
	ClientMapInfo getServerConfiguration();

	/**
	 * Turn animation for a certain layer on or off.
	 * 
	 * @param layer
	 *            The layer to enable or disable animation for.
	 * @param animated
	 *            Should animation during navigation be enabled or disabled?
	 */
	void setAnimated(Layer layer, boolean animated);

	/**
	 * Is a certain layer animated during map navigation or not?
	 * 
	 * @param layer
	 *            The layer to ask for.
	 * @return True or false.
	 */
	boolean isAnimated(Layer layer);
}