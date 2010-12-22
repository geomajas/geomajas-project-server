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

package org.geomajas.puregwt.client.map.event;

import org.geomajas.global.Api;
import org.geomajas.global.UserImplemented;

import com.google.gwt.event.shared.EventHandler;

/**
 * Interface for event handlers that catch changes in the <code>ViewPort</code>.
 * 
 * @author Pieter De Graef
 * @since 1.0.0
 */
@Api(allMethods = true)
@UserImplemented
public interface ViewPortChangedHandler extends EventHandler {

	/**
	 * Catches events where the <code>ViewPort</code> has both scaled and translated.
	 * 
	 * @param event
	 *            The actual {@link org.geomajas.puregwt.client.map.event.ViewPortChangedEvent}.
	 */
	void onViewPortChanged(ViewPortChangedEvent event);

	/**
	 * Catches events where the <code>ViewPort</code> scaled only.
	 * 
	 * @param event
	 *            The actual {@link org.geomajas.puregwt.client.map.event.ViewPortScaledEvent}.
	 */
	void onViewPortScaled(ViewPortScaledEvent event);

	/**
	 * Catches events where the <code>ViewPort</code> has translated only.
	 * 
	 * @param event
	 *            The actual {@link org.geomajas.puregwt.client.map.event.ViewPortTranslatedEvent}.
	 */
	void onViewPortTranslated(ViewPortTranslatedEvent event);

	/**
	 * Catches events where the <code>ViewPort</code> has been dragged (panning).
	 * 
	 * @param event
	 *            The actual {@link org.geomajas.puregwt.client.map.event.ViewPortDraggedEvent}.
	 */
	void onViewPortDragged(ViewPortDraggedEvent event);
}