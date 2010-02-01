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
package org.geomajas.internal.rendering;

import org.geomajas.configuration.StyleInfo;
import org.geomajas.internal.rendering.style.StyleFilterImpl;
import org.geomajas.layer.VectorLayer;
import org.geomajas.rendering.painter.LayerPaintContext;
import org.geomajas.rendering.style.StyleFilter;
import org.opengis.filter.Filter;
import org.opengis.referencing.operation.MathTransform;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * Default implementation of the {@link org.geomajas.rendering.painter.LayerPaintContext} interface. This context is
 * needed by a painter to successfully render a feature.
 * </p>
 *
 * @author Pieter De Graef
 */
public class DefaultLayerPaintContext implements LayerPaintContext {

	/**
	 * The vector layer that needs painting.
	 */
	private VectorLayer layer;

	/**
	 * The filter that should be applied on the layer, when retrieving features.
	 */
	private Filter filter;

	/**
	 * A list of style filters and their associated styles. This is used to
	 * determine the style for each feature.
	 */
	private List<StyleFilter> styleFilters;

	/**
	 * A transformation object that determines how the feature's geometries
	 * should be transformed when painting.
	 */
	private MathTransform mathTransform;

	// -------------------------------------------------------------------------
	// Constructors:
	// -------------------------------------------------------------------------

	/**
	 * Watch out for ConcurrentModificationExceptions when using this
	 * constructor! Don't alter the styles from this object!
	 *
	 * @param layer
	 */
	public DefaultLayerPaintContext(VectorLayer layer) {
		this(layer, Filter.INCLUDE);
	}

	/**
	 * Create a new paint context for a specific layer, but with an alternate
	 * set of style definitions.
	 *
	 * @param layer
	 * @param filter
	 */
	public DefaultLayerPaintContext(VectorLayer layer, Filter filter) {
		this(layer, filter, layer.getLayerInfo().getStyleDefinitions().toArray(
				new StyleInfo[layer.getLayerInfo().getStyleDefinitions().size()]));
	}

	/**
	 * Create a new paint context for a specific layer, but with an alternate
	 * set of style definitions.
	 *
	 * @param layer
	 * @param styleDefs
	 */
	public DefaultLayerPaintContext(VectorLayer layer, StyleInfo[] styleDefs) {
		this(layer, Filter.INCLUDE, styleDefs);
	}

	/**
	 * Create a new paint context for a specific layer, but with an alternate
	 * set of style definitions.
	 *
	 * @param layer
	 * @param styleDefs
	 */
	public DefaultLayerPaintContext(VectorLayer layer, Filter filter, StyleInfo[] styleDefs) {
		this.layer = layer;
		this.filter = filter;
		initStyleFilters(styleDefs);
	}

	// -------------------------------------------------------------------------
	// PaintContext implementation:
	// -------------------------------------------------------------------------

	/**
	 * Find the style filter that must be applied to this feature.
	 *
	 * @param feature
	 *            the feature
	 * @return a style filter
	 */
	public StyleFilter findStyleFilter(Object feature) {
		for (StyleFilter styleFilter : styleFilters) {
			if (styleFilter.getFilter().evaluate(feature)) {
				return styleFilter;
			}
		}
		return new StyleFilterImpl();
	}

	/**
	 * Return the layer to which the feature belongs.
	 *
	 * @return layer reference
	 */
	public VectorLayer getLayer() {
		return this.layer;
	}

	/**
	 * Set the transformation transforms features during their rendering stage.
	 * Coordinates systems can differ between layers and their map, and this
	 * transformation class will handle just that.
	 *
	 * @param mathTransform
	 *            The transformation object.
	 */
	public void setMathTransform(MathTransform mathTransform) {
		this.mathTransform = mathTransform;
	}

	/**
	 * Get the transformation that transforms features during their rendering
	 * stage. Coordinates systems can differ between layers and their map, and
	 * this transformation class will handle just that.
	 */
	public MathTransform getMathTransform() {
		return mathTransform;
	}

	/**
	 * The filter that should be applied on the layer, when retrieving features.
	 *
	 * @return filter
	 */
	public Filter getFilter() {
		return filter;
	}

	public void setFilter(Filter filter) {
		this.filter = filter;
	}

	// -------------------------------------------------------------------------
	// Private functions:
	// -------------------------------------------------------------------------

	/**
	 * Initiate the style filters from the retrieved style definitions. If none
	 * are passed, a default style filter is put in place.
	 */
	private void initStyleFilters(StyleInfo[] styleDefs) {
		styleFilters = new ArrayList<StyleFilter>();
		if (styleDefs == null || styleDefs.length == 0) {
			styleFilters.add(new StyleFilterImpl()); // use default.
		} else {
			for (StyleInfo styleDef : styleDefs) {
				styleFilters.add(new StyleFilterImpl(styleDef));
			}
		}
	}
}