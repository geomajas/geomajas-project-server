/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2014 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */

package org.geomajas.internal.rendering;

import org.geomajas.configuration.FeatureStyleInfo;
import org.geomajas.rendering.StyleFilter;
import org.opengis.filter.Filter;

/**
 * <p>
 * Default implementation of the StyleFilter interface. Transforms a {@link FeatureStyleInfo} into a
 * {@link StyleFilter}, by parsing the formula into a {@link Filter} object. Only feature that accept this
 * filter should receive the style contained in the style definition.
 * </p>
 *
 * @author Pieter De Graef
 */
public class StyleFilterImpl implements StyleFilter {

	/**
	 * The OpenGis filter object, parsed from the {@link FeatureStyleInfo}'s formula.
	 */
	private Filter filter;

	/**
	 * The style definition object contains styling information, a name, an id, ...
	 */
	private FeatureStyleInfo definition;

	// -------------------------------------------------------------------------
	// Constructors:
	// -------------------------------------------------------------------------

	/**
	 * No-arguments constructor. Creates an invisible style, with the default ID. Try to avoid this.
	 */
	public StyleFilterImpl() {
		this.filter = Filter.INCLUDE;
		definition = new FeatureStyleInfo();
		definition.setFillColor("#FFFFFF");
		definition.setFillOpacity(0);
		definition.setStrokeColor("#FFFFFF");
		definition.setStrokeOpacity(0);
		definition.setIndex(FeatureStyleInfo.DEFAULT_STYLE_INDEX);
		definition.setName("invisible-style");
		definition.setFormula("");
		definition.setStyleId("default??");
	}

	/**
	 * Takes a style definition object and tries to parse the formula to an OpenGis filter object.
	 *
	 * @param definition The style definition.
	 * @param filter The style filter.
	 */
	public StyleFilterImpl(Filter filter, FeatureStyleInfo definition) {
		this.definition = definition;
		this.filter = filter;
	}

	// -------------------------------------------------------------------------
	// Getters:
	// -------------------------------------------------------------------------

	/**
	 * Return the styling definition.
	 */
	public FeatureStyleInfo getStyleDefinition() {
		return definition;
	}

	/**
	 * Return the filter object associated with the style.
	 */
	public Filter getFilter() {
		return filter;
	}	
	
}