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
import org.geomajas.puregwt.client.map.layer.Layer;

import com.google.gwt.event.shared.GwtEvent;

/**
 * Event that reports the deselection of a layer.
 *
 * @author Pieter De Graef
 * @since 1.6.0
 */
@Api(allMethods = true)
public class LayerDeselectedEvent extends GwtEvent<LayerSelectionHandler> {

	private Layer<?> layer;

	public LayerDeselectedEvent(Layer<?> layer) {
		this.layer = layer;
	}

	public Type<LayerSelectionHandler> getAssociatedType() {
		return LayerSelectionHandler.TYPE;
	}

	protected void dispatch(LayerSelectionHandler layerSelectionHandler) {
		layerSelectionHandler.onDeselectLayer(this);
	}

	public Layer<?> getLayer() {
		return layer;
	}
}
