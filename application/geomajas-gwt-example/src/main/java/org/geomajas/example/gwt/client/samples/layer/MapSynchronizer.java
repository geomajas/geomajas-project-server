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

	private MapView map1;

	private MapView map2;

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
