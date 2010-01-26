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

package org.geomajas.gwt.client.gfx.style;

/**
 * <p>
 * Styling definition for a layer.
 * </p>
 *
 * @author Pieter De Graef
 */
public class LayerStyleDefinition {

	/**
	 * A unique identifier for the style definition.
	 */
	private int id;

	/**
	 * A label, to be used in the legend.
	 */
	private String label;

	/**
	 * A filter features must accept.
	 */
	private String formula;

	/**
	 * The actual style object.
	 */
	private ShapeStyle style;

	// Constructors:

	/**
	 * @class Configuration object that defines styles for a layer object.
	 * @author Pieter De Graef
	 *
	 * @constructor
	 * @param id
	 *            Unique identifier.
	 * @param label
	 *            The style's label.
	 * @param style
	 *            A Style object.
	 */
	public LayerStyleDefinition(int id, String label, String formula,
			ShapeStyle style) {
		this.id = id;
		this.label = label;
		this.formula = formula;
		this.style = style;
	}

	// Getters and setters:

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getFormula() {
		return formula;
	}

	public void setFormula(String formula) {
		this.formula = formula;
	}

	public ShapeStyle getStyle() {
		return style;
	}

	public void setStyle(ShapeStyle style) {
		this.style = style;
	}
}