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
package org.geomajas.plugin.graphicsediting.gwt2.example.client.annotation;

import org.geomajas.geometry.Coordinate;
import org.geomajas.gwt.client.map.RenderSpace;
import org.geomajas.gwt2.client.controller.NavigationController;

import com.google.gwt.event.dom.client.MouseWheelEvent;
import com.google.gwt.user.client.Timer;

/**
 * Extends {@link NavigationController} to add a minimum interval between events.
 * 
 * @author Jan De Moerloose
 * 
 */
public class IntervalNavigationController extends NavigationController {

	private int intervalMillis = 100;

	private Timer intervalTimer;

	@Override
	public void onMouseWheel(MouseWheelEvent event) {
		final boolean isNorth;
		if (event.getDeltaY() == 0) {
			isNorth = (getWheelDelta(event.getNativeEvent()) < 0);
		} else {
			isNorth = event.isNorth();
		}
		final Coordinate location = getLocation(event, RenderSpace.WORLD);
		if (intervalTimer != null) {
			intervalTimer.cancel();
		}
		intervalTimer = new Timer() {

			@Override
			public void run() {
				scrollZoomTo(isNorth, location);
			}

		};
		intervalTimer.schedule(intervalMillis);
	}
	
	public long getIntervalMillis() {
		return intervalMillis;
	}

	public void setIntervalMillis(int intervalMillis) {
		this.intervalMillis = intervalMillis;
	}

	public void stopDragging() {
		this.dragging = false;
		this.zooming = false;
	}

}
