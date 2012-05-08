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

package org.geomajas.plugin.editing.client.merge.event;

import org.geomajas.annotation.Api;

import com.google.gwt.event.shared.GwtEvent;

/**
 * Event that reports the merging process for geometries has begun.
 * 
 * @author Pieter De Graef
 * @since 1.0.0
 */
@Api(allMethods = true)
public class GeometryMergeStartEvent extends GwtEvent<GeometryMergeStartHandler> {

	/**
	 * Default constructor.
	 */
	public GeometryMergeStartEvent() {
	}

	@Override
	public Type<GeometryMergeStartHandler> getAssociatedType() {
		return GeometryMergeStartHandler.TYPE;
	}

	@Override
	protected void dispatch(GeometryMergeStartHandler handler) {
		handler.onGeometryMergingStart(this);
	}
}