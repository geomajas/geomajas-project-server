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

package org.geomajas.gwt.client.command.event;

import com.google.gwt.event.shared.GwtEvent;
import org.geomajas.annotation.Api;

/**
 * This event is thrown when the command dispatcher stops dispatching (no callbacks left).
 *
 * @author Jan De Moerloose
 * @since 0.0.0
 */
@Api(allMethods = true)
public class DispatchStoppedEvent extends GwtEvent<DispatchStoppedHandler> {

	// -------------------------------------------------------------------------
	// Event implementation:
	// -------------------------------------------------------------------------

	/**
	 * Get the type associated with this event.
	 *
	 * @return returns the handler type
	 * @deprecated use {@link DispatchStoppedHandler#TYPE}
	 */
	@Deprecated
	public static Type<DispatchStoppedHandler> getType() {
		return DispatchStoppedHandler.TYPE;
	}

	/** {@inheritDoc} */
	protected void dispatch(DispatchStoppedHandler handler) {
		handler.onDispatchStopped(this);
	}

	/** {@inheritDoc} */
	public final Type<DispatchStoppedHandler> getAssociatedType() {
		return DispatchStoppedHandler.TYPE;
	}

}