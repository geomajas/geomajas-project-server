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

package org.geomajas.gwt.client.map.event;

import org.geomajas.annotation.Api;
import org.geomajas.gwt.client.map.MapView.ZoomOption;
import org.geomajas.gwt.client.spatial.Bbox;

import com.google.gwt.event.shared.GwtEvent;

/**
 * Custom defined event that is triggered every time the view on a map changes. This can be due to zooming in or
 * panning. This event contains the new bounding box and scale.
 * 
 * @author Pieter De Graef
 * @since 1.6.0
 */
@Api(allMethods = true)
public class MapViewChangedEvent extends GwtEvent<MapViewChangedHandler> {

	private static Type<MapViewChangedHandler> type;

	private Bbox bounds;

	/**
	 * The map's current scale.
	 */
	private double scale;

	/**
	 * Is it a panning event ?
	 */
	private boolean sameScaleLevel;

	private boolean panDragging;

	private boolean mapResized;

	private ZoomOption zoomOption;

	/**
	 * Create new event instance.
	 *
	 * @param bounds bounds
	 * @param scale scale
	 * @param sameScaleLevel same scale level?
	 * @param panDragging dragging to pan?
	 * @param mapResized is map resized?
	 * @param zoomOption zoom option
	 */
	public MapViewChangedEvent(Bbox bounds, double scale, boolean sameScaleLevel, boolean panDragging,
			boolean mapResized, ZoomOption zoomOption) {
		this.bounds = bounds;
		this.scale = scale;
		this.sameScaleLevel = sameScaleLevel;
		this.panDragging = panDragging;
		this.mapResized = mapResized;
		this.zoomOption = zoomOption;
	}

	// -------------------------------------------------------------------------
	// Event implementation:
	// -------------------------------------------------------------------------

	/**
	 * Get the type associated with this event.
	 * 
	 * @return returns the handler type
	 */
	public static Type<MapViewChangedHandler> getType() {
		if (type == null) {
			type = new Type<MapViewChangedHandler>();
		}
		return type;
	}

	/** {@inheritDoc} */
	protected void dispatch(MapViewChangedHandler handler) {
		handler.onMapViewChanged(this);
	}

	/** {@inheritDoc} */
	public final Type<MapViewChangedHandler> getAssociatedType() {
		return type;
	}

	// -------------------------------------------------------------------------
	// Getters:
	// -------------------------------------------------------------------------

	/**
	 * Get the new bounding box that has been applied on the map.
	 *
	 * @return new bounding box
	 */
	public Bbox getBounds() {
		return bounds;
	}

	/**
	 * Get the new map scale.
	 *
	 * @return map scale
	 */
	public double getScale() {
		return scale;
	}

	/**
	 * Is the scale level still the same?
	 *
	 * @return true when scale level has not changed
	 */
	public boolean isSameScaleLevel() {
		return sameScaleLevel;
	}

	/**
	 * Is the user panning or dragging.
	 *
	 * @return true when user is panning or dragging
	 */
	public boolean isPanDragging() {
		return panDragging;
	}

	/**
	 * Has the map been resized?
	 *
	 * @return true when map resized
	 */
	public boolean isMapResized() {
		return mapResized;
	}

	/**
	 * Get zoom option.
	 *
	 * @return zoom option
	 */
	public ZoomOption getZoomOption() {
		return zoomOption;
	}
	
}
