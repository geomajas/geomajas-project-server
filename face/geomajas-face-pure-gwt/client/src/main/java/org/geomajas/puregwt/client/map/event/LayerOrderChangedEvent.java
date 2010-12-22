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

import com.google.gwt.event.shared.GwtEvent;

/**
 * Event that is fired when the order of the layers within a map has changed.
 * 
 * @author Pieter De Graef
 * @author Jan De Moerloose
 */
public class LayerOrderChangedEvent extends GwtEvent<LayerOrderChangedHandler> {

	private int minIndex;

	private int maxIndex;

	public LayerOrderChangedEvent(int minIndex, int maxIndex) {
		this.minIndex = minIndex;
		this.maxIndex = maxIndex;
	}

	@Override
	public Type<LayerOrderChangedHandler> getAssociatedType() {
		return LayerOrderChangedHandler.TYPE;
	}

	@Override
	protected void dispatch(LayerOrderChangedHandler layerOrderChangedHandler) {
		layerOrderChangedHandler.onLayerOrderChanged(this);
	}

	public int getMinIndex() {
		return minIndex;
	}

	public int getMaxIndex() {
		return maxIndex;
	}
}