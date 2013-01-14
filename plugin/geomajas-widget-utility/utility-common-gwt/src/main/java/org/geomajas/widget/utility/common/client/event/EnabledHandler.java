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

import org.geomajas.annotation.Api;
import org.geomajas.annotation.UserImplemented;

import com.google.gwt.event.shared.EventHandler;
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

import com.google.gwt.event.shared.GwtEvent;

/**
 * Interface for observers of {@link EnabledEvent} and {@link DisabledEvent} events.
 * 
 * @author Jan De Moerloose
 * @since 1.0.0
 * 
 */
@Api(allMethods = true)
@UserImplemented
public interface EnabledHandler extends EventHandler {

	/** Event type. */
	GwtEvent.Type<EnabledHandler> TYPE = new GwtEvent.Type<EnabledHandler>();

	/**
	 * Called when the source is enabled.
	 * 
	 * @param event {@link EnabledEvent}
	 */
	void onEnabled(EnabledEvent event);

	/**
	 * Called when the source is disabled.
	 * 
	 * @param event {@link DisabledEvent}
	 */
	void onDisabled(DisabledEvent event);
}
