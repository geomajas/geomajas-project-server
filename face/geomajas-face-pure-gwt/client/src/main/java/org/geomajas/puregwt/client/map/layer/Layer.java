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

package org.geomajas.puregwt.client.map.layer;

import org.geomajas.configuration.client.ClientLayerInfo;
import org.geomajas.global.Api;
import org.geomajas.puregwt.client.map.MapModel;

/**
 * The basic definition of a layer within the GWT client.
 * 
 * @param <T>
 *            layer info type, {@link ClientLayerInfo}
 * 
 * @author Pieter De Graef
 * @since 1.0.0
 */
@Api(allMethods = true)
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
	 * @return the configuration object.
	 */
	T getLayerInfo();

	/**
	 * Mark this layer as selected or not. Only one layer can be selected at any given time within a map.
	 * 
	 * @param selected
	 *            The boolean value that indicates whether or not to select this layer.
	 */
	void setSelected(boolean selected);

	/**
	 * Is this layer currently marked as selected or not?
	 * 
	 * @return Returns true or false.
	 */
	boolean isSelected();

	/**
	 * Mark this layer as visible or invisible. This may toggle the visibility flag, but does not guarantee that the
	 * layer be visible. This is because other factors might intrude upon the layer visibility, such as the minimum and
	 * maximum scales between which a layer can be visible.
	 * 
	 * @param markedAsVisible
	 *            Should the layer be marked as visible or invisible?
	 */
	void setMarkedAsVisible(boolean markedAsVisible);

	/**
	 * Has the layer been marked as visible?
	 * 
	 * @return True or false.
	 */
	boolean isMarkedAsVisible();

	/**
	 * Is the layer currently showing on the map or not? In other words, can we actually look at the layer on the map? A
	 * layer may be marked as visible but other factors (such as scale) may intrude upon the actual visibility of a
	 * layer.<br/>
	 * This value will return the final result of all these factors and clearly state whether or not the layer can be
	 * seen.
	 * 
	 * @return true or false.
	 */
	boolean isShowing();

	/**
	 * Return the maps model. This model stores a list of layers among which this one.
	 * 
	 * @return Returns the map model.
	 */
	MapModel getMapModel();
}