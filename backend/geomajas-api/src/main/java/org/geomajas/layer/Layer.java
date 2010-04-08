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
package org.geomajas.layer;

import org.geomajas.configuration.LayerInfo;
import org.geomajas.global.Api;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

/**
 * A layer which is is normally used as part of a map.
 * 
 * @param <T>
 *            type of layer configuration info
 * 
 * @author Joachim Van der Auwera
 * @author Jan De Moerloose
 */
@Api(allMethods = true)
public interface Layer<T extends LayerInfo> {

	/**
	 * Get information about the layer.
	 * 
	 * @return layer info
	 */
	T getLayerInfo();

	/**
	 * Get coordinate reference system for this layer.
	 * 
	 * @return Coordinate reference system for this layer.
	 */
	CoordinateReferenceSystem getCrs();
	
	/**
	 * Get the unique id of this layer.
	 * 
	 * @return Unique id of the layer (auto-set to name of bean in Spring context)
	 */
	String getId();


}
