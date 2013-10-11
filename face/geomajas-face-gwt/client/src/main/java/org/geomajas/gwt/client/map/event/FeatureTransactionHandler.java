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
import com.google.gwt.event.shared.GwtEvent;
import org.geomajas.annotation.UserImplemented;

/**
 * Interface for event handlers that catch {@link FeatureTransactionEvent}s.
 * 
 * @author Pieter De Graef
 * @since 1.7.0
 */
@Api(allMethods = true)
@UserImplemented
public interface FeatureTransactionHandler extends EventHandler {

	/** Event type. */
	GwtEvent.Type<FeatureTransactionHandler> TYPE = new GwtEvent.Type<FeatureTransactionHandler>();

	/**
	 * Called when a transaction has been successfully persisted on the server.
	 * 
	 * @param event
	 *            The {@link FeatureTransactionEvent}.
	 */
	void onTransactionSuccess(FeatureTransactionEvent event);
}
