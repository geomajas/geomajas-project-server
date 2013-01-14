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

package org.geomajas.plugin.editing.client.event;

import java.util.List;

import org.geomajas.annotation.FutureApi;
import org.geomajas.geometry.Geometry;
import org.geomajas.plugin.editing.client.service.GeometryIndex;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

/**
 * Base event type for events that signal changes on a geometry that is being edited.
 * 
 * @author Pieter De Graef
 * @since 1.0.0
 * @param <H>
 *            Type of handler for the event.
 */
@FutureApi(allMethods = true)
public abstract class AbstractGeometryEditEvent<H extends EventHandler> extends GwtEvent<H> {

	private final Geometry geometry;

	private final List<GeometryIndex> indices;

	public AbstractGeometryEditEvent(Geometry geometry, List<GeometryIndex> indices) {
		this.geometry = geometry;
		this.indices = indices;
	}

	/**
	 * Get the geometry that is currently being edited.
	 * 
	 * @return The geometry that is currently being edited.
	 */
	public Geometry getGeometry() {
		return geometry;
	}

	/**
	 * Get the list of geometries/vertices/edges that are the subject of this event. These indices apply onto the given
	 * geometry. If the returned list is null, the geometry itself is considered subject.
	 * 
	 * @return The list of geometries/vertices/edges that are the subject of this event.
	 */
	public List<GeometryIndex> getIndices() {
		return indices;
	}
}