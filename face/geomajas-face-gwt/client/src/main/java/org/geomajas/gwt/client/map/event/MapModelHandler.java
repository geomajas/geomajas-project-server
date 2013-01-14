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
package org.geomajas.gwt.client.map.event;

import org.geomajas.annotation.Api;

import com.google.gwt.event.shared.EventHandler;
import org.geomajas.annotation.UserImplemented;

/**
 * Interface for handling map model events.
 * <p/>
 * This is only called on the first initialization of the map model. For all changes, use
 * {@link MapModelChangedHandler}.
 *
 * @author Pieter De Graef
 * @since 1.6.0
 * @deprecated use {@link MapModelChangedHandler}
 */
@Api(allMethods = true)
@UserImplemented
@Deprecated
public interface MapModelHandler extends EventHandler {

	/**
	 * Called when the map model changes.
	 * 
	 * @param event change event
	 */
	void onMapModelChange(MapModelEvent event);
}
