/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2011 Geosparc nv, http://www.geosparc.com/, Belgium.
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
public class GeometryEditSuspendEvent extends GwtEvent<GeometryEditSuspensionHandler> {

	private GeometryEditSuspensionReason reason;

	public GeometryEditSuspendEvent(GeometryEditSuspensionReason reason) {
		this.reason = reason;
	}

	public Type<GeometryEditSuspensionHandler> getAssociatedType() {
		return GeometryEditSuspensionHandler.TYPE;
	}

	protected void dispatch(GeometryEditSuspensionHandler handler) {
		handler.onGeometryEditSuspend(this);
	}

	public GeometryEditSuspensionReason getReason() {
		return reason;
	}
}