/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2013 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
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