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
package org.geomajas.rendering;

import org.geomajas.configuration.FeatureStyleInfo;
import org.geomajas.global.Api;
import org.opengis.filter.Filter;

/**
 * <p>
 * Simple interface for defining a style filter. It couples a style-definition to an OpenGIS filter implementation.
 * </p>
 *
 * @author Pieter De Graef
 */
@Api(allMethods = true)
public interface StyleFilter {

	/**
	 * Return the opengis filter implementation coupled to a style.
	 *
	 * @return opengis filter
	 */
	Filter getFilter();

	/**
	 * A style definition object as described in the geomajas configuration.
	 *
	 * @return style info
	 */
	FeatureStyleInfo getStyleDefinition();
}
