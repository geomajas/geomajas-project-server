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

package org.geomajas.gwt.client.action.toolbar;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import org.geomajas.gwt.client.action.ToolbarAction;
import org.geomajas.gwt.client.map.MapView;
import org.geomajas.gwt.client.map.event.MapViewChangedEvent;
import org.geomajas.gwt.client.map.event.MapViewChangedHandler;
import org.geomajas.gwt.client.widget.MapWidget;

/**
 * Manages the queue of zoom levels for the next and previous zoom level buttons.
 * 
 * @author Joachim Van der Auwera
 */
public final class ZoomQueue implements MapViewChangedHandler {

	private static Map<String, ZoomQueue> QUEUES = new HashMap<String, ZoomQueue>();

	private static final double DELTA = .0001;

	private static final int MAX_STACK_SIZE = 10;

	private MapView mapView;

	private LinkedList<MapViewChangedEvent> previous = new LinkedList<MapViewChangedEvent>();

	private LinkedList<MapViewChangedEvent> next = new LinkedList<MapViewChangedEvent>();

	private ToolbarAction zoomNext;

	private ToolbarAction zoomPrevious;

	private boolean active = true;

	public static ZoomQueue getZoomQueue(MapWidget mapWidget) {
		ZoomQueue queue = QUEUES.get(mapWidget.getID());
		if (null == queue) {
			queue = new ZoomQueue(mapWidget.getMapModel().getMapView());
			QUEUES.put(mapWidget.getID(), queue);
		}
		return queue;
	}

	private ZoomQueue(MapView mapView) {
		this.mapView = mapView;
		mapView.addMapViewChangedHandler(this);
	}

	public void onMapViewChanged(MapViewChangedEvent event) {
		if (active) {
			MapViewChangedEvent peek = previous.peek();
			if ((null != peek && sameEvent(event, peek)) || event.isPanDragging()) {
				return;
			}
			previous.addFirst(event);
			while (previous.size() > MAX_STACK_SIZE) {
				previous.removeLast();
			}
			next.clear();
			zoomNext.setDisabled(true);
		} else {
			active = true;
		}
	}

	/**
	 * Is there a previous zoom level?
	 * 
	 * @return true when zoomPrevious() can be used
	 */
	public boolean hasPrevious() {
		return previous.size() > 1;
	}

	/**
	 * Is there a next zoom level?
	 * 
	 * @return true when zoomNext() can be used
	 */
	public boolean hasNext() {
		return !next.isEmpty();
	}

	/**
	 * Zoom to the next level.
	 */
	public void zoomNext() {
		if (hasNext()) {
			if (zoomPrevious != null && zoomPrevious.isDisabled()) {
				zoomPrevious.setDisabled(false);
			}
			MapViewChangedEvent data = next.remove();
			previous.addFirst(data);
			active = false;
			mapView.applyBounds(data.getBounds(), MapView.ZoomOption.LEVEL_CLOSEST);
			if (zoomNext != null && !hasNext()) {
				zoomNext.setDisabled(true);
			}
		}
	}

	/**
	 * Zoom to the previous level.
	 */
	public void zoomPrevious() {
		if (hasPrevious()) {
			if (zoomNext != null && zoomNext.isDisabled()) {
				zoomNext.setDisabled(false);
			}
			next.addFirst(previous.remove());
			MapViewChangedEvent data = previous.peek();
			active = false;
			mapView.applyBounds(data.getBounds(), MapView.ZoomOption.LEVEL_CLOSEST);
			if (zoomPrevious != null && !hasPrevious()) {
				zoomPrevious.setDisabled(true);
			}
		}
	}

	public void setZoomNextAction(ToolbarAction zoomNext) {
		this.zoomNext = zoomNext;
	}

	public void setZoomPreviousAction(ToolbarAction zoomPrevious) {
		this.zoomPrevious = zoomPrevious;
	}

	private boolean sameEvent(MapViewChangedEvent ev1, MapViewChangedEvent ev2) {
		return Math.abs(ev1.getScale() - ev2.getScale()) < DELTA
				&& Math.abs(ev1.getBounds().getX() - ev2.getBounds().getX()) < DELTA
				&& Math.abs(ev1.getBounds().getY() - ev2.getBounds().getY()) < DELTA
				&& Math.abs(ev1.getBounds().getWidth() - ev2.getBounds().getWidth()) < DELTA
				&& Math.abs(ev1.getBounds().getHeight() - ev2.getBounds().getHeight()) < DELTA;
	}
}
