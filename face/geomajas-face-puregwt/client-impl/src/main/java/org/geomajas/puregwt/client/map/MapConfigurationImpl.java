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

package org.geomajas.puregwt.client.map;

import java.util.HashMap;
import java.util.Map;

import org.geomajas.configuration.client.ClientMapInfo;
import org.geomajas.puregwt.client.map.layer.Layer;

/**
 * Default map configuration implementation.
 * 
 * @author Pieter De Graef
 */
public class MapConfigurationImpl implements MapConfiguration {

	public static final Boolean ANIMATION_ENABLED_DEFAULT = true;

	public static final Long ANIMATION_TIME_DEFAULT = 400L;

	private final Map<Layer, Boolean> layerAnimation;

	private final Map<String, Object> hintValues;

	private ClientMapInfo mapInfo;

	// ------------------------------------------------------------------------
	// Constructor:
	// ------------------------------------------------------------------------

	protected MapConfigurationImpl() {
		hintValues = new HashMap<String, Object>();
		layerAnimation = new HashMap<Layer, Boolean>();

		// Now apply the default values:
		setMapHintValue(MapConfiguration.ANIMATION_ENABLED, ANIMATION_ENABLED_DEFAULT);
		setMapHintValue(MapConfiguration.ANIMATION_TIME, ANIMATION_TIME_DEFAULT);
	}

	// ------------------------------------------------------------------------
	// Working with map hints:
	// ------------------------------------------------------------------------

	/** {@inheritDoc} */
	public void setMapHintValue(String hint, Object value) {
		if (value == null) {
			throw new IllegalArgumentException("Null value passed.");
		}
		if (hintValues.containsKey(hint)) {
			hintValues.remove(hint);
		}
		hintValues.put(hint, value);
	}

	/** {@inheritDoc} */
	public Object getMapHintValue(String hint) {
		return hintValues.get(hint);
	}

	// ------------------------------------------------------------------------
	// Getters and setters:
	// ------------------------------------------------------------------------

	/** {@inheritDoc} */
	public ClientMapInfo getServerConfiguration() {
		return mapInfo;
	}

	/**
	 * Protected method used by the MapPresenterImpl to set the server configuration (when it arrives from the server).
	 * 
	 * @param mapInfo
	 *            The server configuration object.
	 */
	protected void setServerConfiguration(ClientMapInfo mapInfo) {
		this.mapInfo = mapInfo;
	}

	/** {@inheritDoc} */
	public boolean isAnimated(Layer layer) {
		if (!layerAnimation.containsKey(layer)) {
			return false;
		}
		return layerAnimation.get(layer);
	}

	/** {@inheritDoc} */
	public void setAnimated(Layer layer, boolean animated) {
		if (layerAnimation.containsKey(layer)) {
			layerAnimation.remove(layer);
		}
		layerAnimation.put(layer, animated);
	}
}