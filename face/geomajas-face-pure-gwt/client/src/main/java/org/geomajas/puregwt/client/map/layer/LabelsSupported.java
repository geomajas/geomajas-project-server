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

import org.geomajas.global.Api;

/**
 * Extension for layers that indicates whether or not labeling is supported. Of course, these labels can only be visible
 * if the layer itself is visible; but one can change the labels-setting nonetheless.
 * 
 * @author Pieter De Graef
 * @since 1.0.0
 */
@Api
public interface LabelsSupported {

	/**
	 * Make the feature labels visible or invisible on the map.
	 * 
	 * @param labeled
	 *            Should the labels be shown or not?
	 */
	void setLabeled(boolean labeled);

	/**
	 * Are the feature labels currently visible or not?
	 * 
	 * @return Returns true or false.
	 */
	boolean isLabeled();
}