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

package org.geomajas.internal.rendering.painter.feature;

import org.geomajas.rendering.painter.feature.FeaturePainter;

/**
 * <p>
 * Abstract implementation of the <code>FeaturePainter</code> interface. It
 * implements common functionality for all feature painter: the option
 * settings. In order to decently support lazy loading, all feature painter
 * should support these options!
 * </p>
 *
 * @author Pieter De Graef
 */
public abstract class AbstractFeaturePainter implements FeaturePainter {

	/**
	 * This field contains a bitmask of all the rendering options for feature
	 * painter implementations.
	 */
	private int options;

	// -------------------------------------------------------------------------
	// Constructors:
	// -------------------------------------------------------------------------

	/**
	 * Default constructor. The default settings allow geometries, labels and
	 * style definitions to be added to the <code>RenderedFeature</code>
	 * objects, but not attributes.
	 */
	public AbstractFeaturePainter() {
		this(true, true, true, true);
	}

	/**
	 * Constructor that determines what should be painted and what not.
	 *
	 * @param paintAttributes
	 *            Do we allow attributes to be added to the
	 *            <code>RenderedFeature</code> objects or not?
	 * @param paintGeometry
	 *            Do we allow geometries to be added to the
	 *            <code>RenderedFeature</code> objects or not?
	 * @param paintStyle
	 *            Do we allow style definitions to be added to the
	 *            <code>RenderedFeature</code> objects or not?
	 * @param paintLabel
	 *            Do we allow the label string to be added to the
	 *            <code>RenderedFeature</code> objects or not?
	 */
	public AbstractFeaturePainter(boolean paintAttributes, boolean paintGeometry, boolean paintStyle,
			boolean paintLabel) {
		setOption(OPTION_PAINT_ATTRIBUTES, paintAttributes);
		setOption(OPTION_PAINT_GEOMETRY, paintGeometry);
		setOption(OPTION_PAINT_STYLE, paintStyle);
		setOption(OPTION_PAINT_LABEL, paintLabel);
	}

	// -------------------------------------------------------------------------
	// FeaturePainter implementation (part of it):
	// -------------------------------------------------------------------------

	/**
	 * Turn on or off a specific rendering option. All options are meant to
	 * increase performance. Available options are:
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
	public void setOption(int option, boolean value) {
		if (value) {
			options |= option;
		} else {
			options &= ~option;
		}
	}

	/**
	 * Request the value of an option.
	 *
	 * @param option option to test
	 * @return true when option is set
	 */
	public boolean getOption(int option) {
		return (options & option) == option;
	}
}