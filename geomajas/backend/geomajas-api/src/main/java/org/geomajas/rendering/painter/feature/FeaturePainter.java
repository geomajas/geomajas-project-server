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

package org.geomajas.rendering.painter.feature;

import org.geomajas.layer.feature.RenderedFeature;
import org.geomajas.rendering.RenderException;
import org.geomajas.rendering.painter.LayerPaintContext;

import java.util.List;

/**
 * <p>
 * Interface for painter that render features. Implementations of this interface will be used in the
 * <code>VectorLayer</code>'s paint function. Basically they transform the object from the <code>LayerModel</code> into
 * <code>RenderedFeature</code> objects. Different implementations of this interface will transform to different
 * implementations of a <code>RenderedFeature</code>.
 * </p>
 * <p>
 * It also adds the possibility to determine what parts of the features should be painted: attributes, geometries,
 * styles. Leaving out some (or all) of these parts can increase performance.
 * </p>
 *
 * @author Pieter De Graef
 */
public interface FeaturePainter {

	/**
	 * Do we allow attributes to be added to the <code>RenderedFeature</code> objects or not? (speed issue)
	 */
	int OPTION_PAINT_ATTRIBUTES = 1;

	/**
	 * Do we allow geometries to be added to the <code>RenderedFeature</code> objects or not? (speed issue)
	 */
	int OPTION_PAINT_GEOMETRY = 2;

	/**
	 * Do we allow style definitions to be added to the <code>RenderedFeature</code> objects or not? (speed issue)
	 */
	int OPTION_PAINT_STYLE = 4;

	/**
	 * Do we allow the label string to be added to the <code>RenderedFeature</code> objects or not? (speed issue)
	 */
	int OPTION_PAINT_LABEL = 8;

	/**
	 * Paint an individual feature. In other words transform the generic feature object into a
	 * <code>RenderedFeature</code>.
	 *
	 * @param paintContext
	 *            The provided painting context. It helps to determine what style a feature should receive.
	 * @param feature
	 *            A feature object that comes directly from the <code>LayerModel</code>. Some LayerModels cannot handle
	 *            these features to be altered directly (think Hibernate), so it is important that implementations of
	 *            this interface never transform the feature's geometries.
	 * @throws RenderException oops
	 */
	void paint(LayerPaintContext paintContext, Object feature) throws RenderException;

	/**
	 * The full list of <code>RenderedFeature</code> objects.
	 *
	 * @return list of rendered features
	 */
	List<RenderedFeature> getFeatures();

	/**
	 * Turn on or off a specific rendering option. All options are meant to increase performance. Available options are:
	 * <ul>
	 * <li>OPTION_PAINT_ATTRIBUTES</li>
	 * <li>OPTION_PAINT_GEOMETRY</li>
	 * <li>OPTION_PAINT_STYLE</li>
	 * <li>OPTION_PAINT_LABEL</li>
	 * </ul>
	 *
	 * @param option
	 *            One of the 3 above options.
	 * @param value
	 *            True or false. In other words, turn it on or off.
	 */
	void setOption(int option, boolean value);

	/**
	 * Request the value of an option.
	 *
	 * @param option check value for an option
	 * @return true when that option is set
	 */
	boolean getOption(int option);
}
