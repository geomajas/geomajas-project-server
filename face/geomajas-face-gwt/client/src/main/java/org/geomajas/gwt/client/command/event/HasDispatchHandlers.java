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
package org.geomajas.gwt.client.command.event;

import org.geomajas.annotation.Api;

import com.google.gwt.event.shared.HandlerRegistration;

/**
 * Classes triggering the {@link DispatchStartedEvent} should implement this interface, thereby allowing handlers to be
 * registered to catch these events.
 *
 * @author Jan De Moerloose
 * @since 1.6.0
 */
@Api
public interface HasDispatchHandlers {

	/**
	 * Add a new handler for {@link DispatchStartedEvent} events.
	 *
	 * @param handler The handler to be registered.
	 * @return Returns the handlers registration object.
	 */
	HandlerRegistration addDispatchStartedHandler(DispatchStartedHandler handler);

	/**
	 * Add a new handler for {@link DispatchStoppedEvent} events.
	 *
	 * @param handler The handler to be registered.
	 * @return Returns the handlers registration object.
	 */
	HandlerRegistration addDispatchStoppedHandler(DispatchStoppedHandler handler);
}
