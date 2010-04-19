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

package org.geomajas.gwt.client.map.event;

import org.geomajas.global.Api;
import org.geomajas.gwt.client.map.MapView.ZoomOption;
import org.geomajas.gwt.client.spatial.Bbox;

import com.google.gwt.event.shared.GwtEvent;

/**
 * Custom defined event that is triggered every time the view on a map changes. This can be due to zooming in or
 * panning. This event contains the new bounding box and scale.
 * 
 * @author Pieter De Graef
 */
@Api
public class MapViewChangedEvent extends GwtEvent<MapViewChangedHandler> {

	/**
	 * Handler type.
	 */
	private static Type<MapViewChangedHandler> TYPE;

	/**
	 * The new bounding box that has been applied on the map.
	 */
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
	 * 
	 * @param bounds
	 * @param scale
	 * @param panning
	 */

	// -------------------------------------------------------------------------
	// Constructor:
	// -------------------------------------------------------------------------
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
		if (TYPE == null) {
			TYPE = new Type<MapViewChangedHandler>();
		}
		return TYPE;
	}

	protected void dispatch(MapViewChangedHandler handler) {
		handler.onMapViewChanged(this);
	}

	public final Type<MapViewChangedHandler> getAssociatedType() {
		return TYPE;
	}

	// -------------------------------------------------------------------------
	// Getters:
	// -------------------------------------------------------------------------

	public Bbox getBounds() {
		return bounds;
	}

	public double getScale() {
		return scale;
	}

	public boolean isSameScaleLevel() {
		return sameScaleLevel;
	}

	public boolean isPanDragging() {
		return panDragging;
	}

	public boolean isMapResized() {
		return mapResized;
	}
	
	public ZoomOption getZoomOption() {
		return zoomOption;
	}
	
}
