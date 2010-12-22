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
 * Event that is fired when a layer is marked as invisible.
 * 
 * @author Frank Wynants
 * @since 1.6.0
 */
@Api
public class LayerHideEvent extends GwtEvent<LayerVisibleHandler> {

	private Layer<?> layer;

	public LayerHideEvent(Layer<?> layer) {
		this.layer = layer;
	}

	public Layer<?> getLayer() {
		return layer;
	}

	@Override
	public Type<LayerVisibleHandler> getAssociatedType() {
		return LayerVisibleHandler.TYPE;
	}

	@Override
	protected void dispatch(LayerVisibleHandler layerVisibleHandler) {
		layerVisibleHandler.onHide(this);
	}
}