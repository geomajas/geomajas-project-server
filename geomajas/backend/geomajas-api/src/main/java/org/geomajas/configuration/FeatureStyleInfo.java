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
package org.geomajas.configuration;

import java.io.Serializable;

import javax.validation.constraints.Null;

/**
 * Style configuration information.
 * 
 * @author Joachim Van der Auwera
 */
public class FeatureStyleInfo implements Serializable {

	/**
	 * Default value for style index, should be the last in sort order.
	 */
	public static final int DEFAULT_STYLE_INDEX = 9999;

	private static final long serialVersionUID = 151L;

	private int index = DEFAULT_STYLE_INDEX;

	private String name;

	private String formula;

	private String fillColor;

	private float fillOpacity = -1;

	private String strokeColor;

	private float strokeOpacity = -1;

	private int strokeWidth = -1;

	private String dashArray;

	private SymbolInfo symbol;

	@Null
	private String styleId;

	/**
	 * Gets the ordering index of the style. Styles are applied in the incremental order determined by their index
	 * values.
	 * 
	 * @return index the ordering index
	 */
	public int getIndex() {
		return index;
	}

	/**
	 * Sets the ordering index of the style. (auto-set by Spring)
	 * 
	 * @param index ordering index
	 */
	public void setIndex(int index) {
		this.index = index;
	}

	/**
	 * Get feature style name.
	 *
	 * @return name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Set feature style name.
	 *
	 * @param name name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Get formula to test whether this style can be applied.
	 *
	 * @return formula
	 */
	public String getFormula() {
		return formula;
	}

	/**
	 * Set formula to test whether this style can be applied.
	 *
	 * @param formula formula
	 */
	public void setFormula(String formula) {
		this.formula = formula;
	}

	/**
	 * Get fill colour.
	 *
	 * @return fill colour (in "#rrggbb" notation)
	 */
	public String getFillColor() {
		return fillColor;
	}

	/**
	 * Set fill colour.
	 *
	 * @param fillColor fill colour (in "#rrggbb" notation)
	 */
	public void setFillColor(String fillColor) {
		this.fillColor = fillColor;
	}

	/**
	 * Get fill opacity. The value should be in the [0,1] range, where 0 is fully transparent, and 1 is opaque.
	 *
	 * @return opacity for background fill
	 */
	public float getFillOpacity() {
		return fillOpacity;
	}

	/**
	 * Set the fill opacity. The value should be in the [0,1] range, where 0 is fully transparent, and 1 is opaque.
	 *
	 * @param fillOpacity opacity for background fill
	 */
	public void setFillOpacity(float fillOpacity) {
		this.fillOpacity = fillOpacity;
	}

	/**
	 * Get stroke colour.
	 *
	 * @return stroke colour (in "#rrggbb" notation)
	 */
	public String getStrokeColor() {
		return strokeColor;
	}

	/**
	 * Set stroke colour.
	 *
	 * @param strokeColor stroke colour (in "#rrggbb" notation)
	 */
	public void setStrokeColor(String strokeColor) {
		this.strokeColor = strokeColor;
	}

	/**
	 * Get stroke opacity. The value should be in the [0,1] range, where 0 is fully transparent, and 1 is opaque.
	 *
	 * @return opacity for stroke colour
	 */
	public float getStrokeOpacity() {
		return strokeOpacity;
	}

	/**
	 * Set the stroke opacity. The value should be in the [0,1] range, where 0 is fully transparent, and 1 is opaque.
	 *
	 * @param strokeOpacity opacity for the stroke
	 */
	public void setStrokeOpacity(float strokeOpacity) {
		this.strokeOpacity = strokeOpacity;
	}

	/**
	 * Get stroke width.
	 *
	 * @return stroke width
	 */
	public int getStrokeWidth() {
		return strokeWidth;
	}

	/**
	 * Set stroke width.
	 *
	 * @param strokeWidth stroke width
	 */
	public void setStrokeWidth(int strokeWidth) {
		this.strokeWidth = strokeWidth;
	}

	/**
	 * Get dash array to apply. When null, the line is solid.
	 *
	 * @return dash array, comma separated list of dash and gap lengths
	 */
	public String getDashArray() {
		return dashArray;
	}

	/**
	 * Set the dash array to apply. When null, the line is solid.
	 *
	 * @param dashArray dash array, comma separated list of dash and gap lengths
	 */
	public void setDashArray(String dashArray) {
		this.dashArray = dashArray;
	}

	/**
	 * Get symbol to indicate feature.
	 *
	 * @return symbol
	 */
	public SymbolInfo getSymbol() {
		return symbol;
	}

	/**
	 * Set symbol to indicate feature.
	 *
	 * @param symbol symbol
	 */
	public void setSymbol(SymbolInfo symbol) {
		this.symbol = symbol;
	}

	/**
	 * Return a unique style identifier for this client side style definition. This value is set automatically on the
	 * server during initialization so don't set it in the configuration.
	 *
	 * @return style id
	 */
	public String getStyleId() {
		return styleId;
	}

	/**
	 * Set a unique style identifier for this client side style definition. This value is set automatically on the
	 * server during initialization so don't set it in the configuration.
	 *
	 * @param styleId style id
	 */
	public void setStyleId(String styleId) {
		this.styleId = styleId;
	}
}
