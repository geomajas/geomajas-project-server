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

package org.geomajas.gwt.client.map.layer;

import org.geomajas.configuration.client.ClientLayerInfo;
import org.geomajas.global.Api;
import org.geomajas.gwt.client.map.MapModel;

/**
 * <p>
 * ...
 * </p>
 * 
 * @param <T>
 *            layer info type, {@link ClientLayerInfo}
 * 
 * @author Pieter De Graef
 * @since 1.0.0
 */
@Api
public interface Layer<T extends ClientLayerInfo> {

	/**
	 * Return this layer's client ID.
	 * 
	 * @return id of the client layer
	 */
	String getId();

	/**
	 * Return this layer's server ID.
	 * 
	 * @return id of the server layer
	 */
	String getServerLayerId();

	/**
	 * Return the layer's title. The difference between the ID and the title, is that the ID is used behind the screens,
	 * while the title is the visible name to the user.
	 * 
	 * @return
	 */
	String getTitle();

	/**
	 * Return the info for this layer.
	 * 
	 * @return the info
	 */
	T getLayerInfo();

	void setSelected(boolean selected);
	
	boolean isSelected();

	void setMarkedAsVisible(boolean markedAsVisible);

	boolean isMarkedAsVisible();
	
	boolean isShowing();
	
	MapModel getMapModel();
}