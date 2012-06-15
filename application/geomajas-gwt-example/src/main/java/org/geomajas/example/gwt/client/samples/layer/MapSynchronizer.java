/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2012 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */

package org.geomajas.example.gwt.client.samples.layer;

import org.geomajas.gwt.client.map.MapView;
import org.geomajas.gwt.client.map.event.MapViewChangedEvent;
import org.geomajas.gwt.client.map.event.MapViewChangedHandler;

/**
 * Synchronizes the navigation of two maps.
 * 
 * @author Jan De Moerloose
 */
public class MapSynchronizer implements MapViewChangedHandler {

	private final MapView map1;

	private final MapView map2;

	private boolean enabled;

	/**
	 * Constructs a synchronizer for the specified maps.
	 * 
	 * @param map1
	 *            first map
	 * @param map2
	 *            second map
	 */
	public MapSynchronizer(MapView map1, MapView map2) {
		this.map1 = map1;
		this.map2 = map2;
		map1.addMapViewChangedHandler(this);
		map2.addMapViewChangedHandler(this);
	}

	public void onMapViewChanged(MapViewChangedEvent event) {
		if (isEnabled()) {
			setEnabled(false);
			if (map1 == event.getSource()) {
				if (event.isSameScaleLevel()) {
					map2.setCenterPosition(map1.getBounds().getCenterPoint());
				} else {
					map2.applyBounds(event.getBounds(), event.getZoomOption());
				}
			} else if (map2 == event.getSource()) {
				if (event.isSameScaleLevel()) {
					map1.setCenterPosition(map2.getBounds().getCenterPoint());
				} else {
					map1.applyBounds(event.getBounds(), event.getZoomOption());
				}
			}
			setEnabled(true);
		}
	}

	/**
	 * Is the synchronization enabled ?
	 * @return true if enabled
	 */
	public boolean isEnabled() {
		return enabled;
	}

	/**
	 * Enable/disable synchronization.
	 * @param enabled true if enabled
	 */
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

}
