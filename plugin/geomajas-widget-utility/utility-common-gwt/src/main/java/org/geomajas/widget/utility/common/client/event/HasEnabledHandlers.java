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

package org.geomajas.widget.utility.common.client.event;

import com.google.gwt.event.shared.HandlerRegistration;

/**
 * Implemented by classes that that trigger {@link EnabledEvent} and {@link DisabledEvent} events.
 * 
 * @author Jan De Moerloose
 * 
 */
public interface HasEnabledHandlers {

	/**
	 * Add a new handler for {@link EnabledEvent} and {@link DisabledEvent} events.
	 * 
	 * @param handler The handler to be registered.
	 * @return Returns the handlers registration object.
	 */
	HandlerRegistration addEnabledHandler(EnabledHandler handler);
	
	/**
	 * Return whether this object is enabled.
	 * 
	 * @return true if enabled, false otherwise
	 */
	boolean isEnabled();
}
