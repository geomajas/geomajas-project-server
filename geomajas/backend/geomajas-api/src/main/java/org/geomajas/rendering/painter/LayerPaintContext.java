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
package org.geomajas.rendering.painter;

import org.geomajas.layer.VectorLayer;
import org.geomajas.rendering.style.StyleFilter;
import org.opengis.filter.Filter;
import org.opengis.referencing.operation.MathTransform;

/**
 * This context is needed by a painter to successfully render a feature.
 *
 * @author Jan De Moerloose
 */
public interface LayerPaintContext {

	/**
	 * Get the transformation that transforms features during their rendering stage. Coordinates systems can differ
	 * between layers and their map, and this transformation class will handle just that.
	 *
	 * @return transformation matrix for converting the layer coordinates to map coordinates
	 */
	MathTransform getMathTransform();

	/**
	 * Set the transformation transforms features during their rendering stage. Coordinates systems can differ between
	 * layers and their map, and this transformation class will handle just that.
	 *
	 * @param mathTransform The transformation object.
	 */
	void setMathTransform(MathTransform mathTransform);

	/**
	 * Return the layer to which the feature belongs.
	 *
	 * @return layer reference
	 */
	VectorLayer getLayer();

	/**
	 * The filter that should be applied on the layer, when retrieving features.
	 *
	 * @return filter which needs to be applied when transforming
	 */
	Filter getFilter();

	/**
	 * find the style filter that must be applied to this feature.
	 *
	 * @param feature
	 *            the feature
	 * @return a style filter
	 */
	StyleFilter findStyleFilter(Object feature);
}
