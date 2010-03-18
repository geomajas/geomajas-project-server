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

import org.geomajas.configuration.FeatureStyleInfo;
import org.geomajas.rendering.StyleFilter;
import org.geotools.filter.text.cql2.CQL;
import org.geotools.filter.text.cql2.CQLException;
import org.opengis.filter.Filter;

/**
 * <p>
 * Default implementation of the StyleFilter interface. Transforms a <code>LayerStyleDefinition</code> into a
 * <code>StyleFilter</code>, by parsing the formula into a <code>Filter</code> object. Only feature that accept this
 * filter should recieve the style contained in the style definition.
 * </p>
 *
 * @author Pieter De Graef
 */
public class StyleFilterImpl implements StyleFilter {

	/**
	 * The opengis filter object, parsed from the <code>LayerStyleDefinition</code>'s formula.
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
	 * The default constructor. Creates an invisible style, with the default ID. Try to avoid this.
	 */
	public StyleFilterImpl() {
		this.filter = Filter.INCLUDE;
		FeatureStyleInfo style = new FeatureStyleInfo();
		style.setFillColor("#FFFFFF");
		style.setFillOpacity(0);
		style.setStrokeColor("#FFFFFF");
		style.setStrokeOpacity(0);
		style.setIndex(FeatureStyleInfo.DEFAULT_STYLE_INDEX);
		style.setName("invisible-style");
		style.setFormula("");
		style.setStyleId("default??");
	}

	/**
	 * The recommended constructor. Takes a style definition object and tries to parse the formula to an opengis filter
	 * object.
	 *
	 * @param definition
	 *            The style definition.
	 */
	public StyleFilterImpl(FeatureStyleInfo definition) {
		try {
			String formula = definition.getFormula();
			if (null != formula && formula.length() > 0) {
				this.filter = CQL.toFilter(definition.getFormula());
			} else {
				this.filter = Filter.INCLUDE;
			}
		} catch (CQLException e) {
			this.filter = Filter.INCLUDE;
		}
		this.definition = definition;
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