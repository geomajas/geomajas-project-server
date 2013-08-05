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

package org.geomajas.smartgwt.client.action.toolbar;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import org.geomajas.smartgwt.client.action.ToolbarAction;
import org.geomajas.smartgwt.client.map.MapView;
import org.geomajas.smartgwt.client.map.event.MapViewChangedEvent;
import org.geomajas.smartgwt.client.map.event.MapViewChangedHandler;
import org.geomajas.smartgwt.client.widget.MapWidget;

/**
 * Manages the queue of zoom levels for the next and previous zoom level buttons.
 * 
 * @author Joachim Van der Auwera
 */
public final class ZoomQueue implements MapViewChangedHandler {

	private static Map<String, ZoomQueue> queues = new HashMap<String, ZoomQueue>();

	private static final double DELTA = .0001;

	private static final int MAX_STACK_SIZE = 10;

	private MapView mapView;

	private LinkedList<MapViewChangedEvent> previous = new LinkedList<MapViewChangedEvent>();

	private LinkedList<MapViewChangedEvent> next = new LinkedList<MapViewChangedEvent>();

	private ToolbarAction zoomNext;

	private ToolbarAction zoomPrevious;

	private boolean active = true;

	public static ZoomQueue getZoomQueue(MapWidget mapWidget) {
		ZoomQueue queue = queues.get(mapWidget.getID());
		if (null == queue) {
			queue = new ZoomQueue(mapWidget.getMapModel().getMapView());
			queues.put(mapWidget.getID(), queue);
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
