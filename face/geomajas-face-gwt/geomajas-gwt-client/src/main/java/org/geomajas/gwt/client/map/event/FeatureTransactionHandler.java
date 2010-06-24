/*
 * This file is part of Geomajas, a component framework for building
 * rich Internet applications (RIA) with sophisticated capabilities for the
 * display, analysis and management of geographic information.
 * It is a building block that allows developers to add maps
 * and other geographic data capabilities to their web applications.
 *
 * Copyright 2008-2010 Geosparc, http://www.geosparc.com, Belgium
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.geomajas.gwt.client.map.event;

import org.geomajas.global.Api;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import org.geomajas.global.UserImplemented;

/**
 * Interface for event handlers that catch {@link FeatureTransactionEvent}s.
 * 
 * @author Pieter De Graef
 * @since 1.7.0
 */
@Api(allMethods = true)
@UserImplemented
public interface FeatureTransactionHandler extends EventHandler {

	GwtEvent.Type<FeatureTransactionHandler> TYPE = new GwtEvent.Type<FeatureTransactionHandler>();

	/**
	 * Called when a transaction has been successfully persisted on the server.
	 * 
	 * @param event
	 *            The {@link FeatureTransactionEvent}.
	 */
	void onTransactionSuccess(FeatureTransactionEvent event);
}
