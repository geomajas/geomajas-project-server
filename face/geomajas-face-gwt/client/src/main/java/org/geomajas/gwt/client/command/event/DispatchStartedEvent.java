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

import com.google.gwt.event.shared.GwtEvent;

/**
 * This event is thrown when the command dispatcher starts dispatching.
 *
 * @author Jan De Moerloose
 * @since 1.6.0
 */
@Api(allMethods = true)
public class DispatchStartedEvent extends GwtEvent<DispatchStartedHandler> {

	/** Handler type. */
	private static Type<DispatchStartedHandler> type;

	// -------------------------------------------------------------------------
	// Event implementation:
	// -------------------------------------------------------------------------

	/**
	 * Get the type associated with this event.
	 *
	 * @return the handler type
	 */
	public static Type<DispatchStartedHandler> getType() {
		if (type == null) {
			type = new Type<DispatchStartedHandler>();
		}
		return type;
	}

	protected void dispatch(DispatchStartedHandler handler) {
		handler.onDispatchStarted(this);
	}

	/**
	 * Get the type associated with this event.
	 *
	 * @return the handler type
	 */
	public final Type<DispatchStartedHandler> getAssociatedType() {
		return type;
	}

}
