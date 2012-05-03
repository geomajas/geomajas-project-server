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

package org.geomajas.gwt.client.command.event;

import com.google.gwt.event.shared.GwtEvent;
import org.geomajas.annotation.Api;

/**
 * This event is thrown when the command dispatcher starts dispatching.
 *
 * @author Jan De Moerloose
 * @since 0.0.0
 */
@Api(allMethods = true)
public class DispatchStartedEvent extends GwtEvent<DispatchStartedHandler> {

	// -------------------------------------------------------------------------
	// Event implementation:
	// -------------------------------------------------------------------------

	/**
	 * Get the type associated with this event.
	 *
	 * @return the handler type
	 * @deprecated use {@link DispatchStartedHandler#TYPE}
	 */
	@Deprecated
	public static Type<DispatchStartedHandler> getType() {
		return DispatchStartedHandler.TYPE;
	}

	/** {@inheritDoc} */
	protected void dispatch(DispatchStartedHandler handler) {
		handler.onDispatchStarted(this);
	}

	/**
	 * Get the type associated with this event.
	 *
	 * @return the handler type
	 */
	public final Type<DispatchStartedHandler> getAssociatedType() {
		return DispatchStartedHandler.TYPE;
	}

}
