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

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

/**
 * Interface for event handlers that catch {@link GeometryEditResumeEvent}s and {@link GeometryEditSuspendEvent}s.
 * 
 * @author Pieter De Graef
 */
public interface GeometryEditSuspendResumeHandler extends EventHandler {

	GwtEvent.Type<GeometryEditSuspendResumeHandler> TYPE = new GwtEvent.Type<GeometryEditSuspendResumeHandler>();

	/**
	 * Called when the editing process has been suspended.
	 * 
	 * @param event
	 *            {@link GeometryEditSuspendEvent}
	 */
	void onGeometryEditSuspend(GeometryEditSuspendEvent event);

	/**
	 * Called when the editing process has been resumed again.
	 * 
	 * @param event
	 *            {@link GeometryEditResumeEvent}
	 */
	void onGeometryEditResume(GeometryEditResumeEvent event);
}