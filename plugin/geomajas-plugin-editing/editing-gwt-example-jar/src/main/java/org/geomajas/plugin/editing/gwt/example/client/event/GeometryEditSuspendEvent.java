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

package org.geomajas.plugin.editing.gwt.example.client.event;

import com.google.gwt.event.shared.GwtEvent;

/**
 * Event which is passed when the editing process has been suspended.
 * 
 * @author Pieter De Graef
 */
public class GeometryEditSuspendEvent extends GwtEvent<GeometryEditSuspendResumeHandler> {

	private GeometryEditSuspendReason reason;

	public GeometryEditSuspendEvent(GeometryEditSuspendReason reason) {
		this.reason = reason;
	}

	public Type<GeometryEditSuspendResumeHandler> getAssociatedType() {
		return GeometryEditSuspendResumeHandler.TYPE;
	}

	protected void dispatch(GeometryEditSuspendResumeHandler handler) {
		handler.onGeometryEditSuspend(this);
	}

	public GeometryEditSuspendReason getReason() {
		return reason;
	}
}