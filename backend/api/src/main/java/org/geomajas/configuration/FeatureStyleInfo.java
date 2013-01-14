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
package org.geomajas.configuration;

import javax.validation.constraints.Null;

import org.geomajas.annotation.Api;
import org.geomajas.global.CacheableObject;
import org.geomajas.layer.LayerType;

/**
 * Style configuration information.
 * 
 * @author Joachim Van der Auwera
 * @since 1.6.0
 */
@Api(allMethods = true)
public class FeatureStyleInfo implements IsInfo, CacheableObject {

	private static final int PRIME = 31;

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
	
	private LayerType layerType;
	
	/**
	 * No args constructor for configuration and GWT.
	 */
	public FeatureStyleInfo() {
		// NOSONAR nothing to do
	}
	
	/**
	 * Copy constructor. Creates a deep copy of the specified {@link FeatureStyleInfo} object.
	 * 
	 * @param other the feature style to copy
	 * @since 1.10.0
	 */
	public FeatureStyleInfo(FeatureStyleInfo other) {
		setDashArray(other.getDashArray());
		setFillColor(other.getFillColor());
		setFillOpacity(other.getFillOpacity());
		setFormula(other.getFormula());
		setLayerType(other.getLayerType());
		setName(other.getName());
		setStrokeColor(other.getStrokeColor());
		setStrokeOpacity(other.getStrokeOpacity());
		setStrokeWidth(other.getStrokeWidth());
		setSymbol(new SymbolInfo(other.getSymbol()));
	}

	/**
	 * Applies default values to all properties that have not been set.
	 * 
	 * @since 1.8.0
	 */
	public void applyDefaults() {
		if (fillColor == null) {
			fillColor = "#ffffff"; // white
		}
		if (strokeColor == null) {
			strokeColor = "#000000"; // black
		}
		if (strokeOpacity == -1) {
			strokeOpacity = 1; // fully opaque by default
		}
		if (fillOpacity == -1) {
			fillOpacity = .5f; // 50% transparent by default
		}
		if (strokeWidth == -1) {
			strokeWidth = 1; // white
		}
		if (symbol == null) { // circle with radius 10 by default
			symbol = new SymbolInfo();
		}
		if (symbol.getCircle() == null && symbol.getRect() == null && symbol.getImage() == null) {
			symbol.setCircle(new CircleInfo());
			symbol.getCircle().setR(10);
		}
	}

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
	 * @param index
	 *            ordering index
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
	 * @param formula
	 *            formula
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
	 * @param fillColor
	 *            fill colour (in "#rrggbb" notation)
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
	 * @param fillOpacity
	 *            opacity for background fill
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
	 * @param strokeColor
	 *            stroke colour (in "#rrggbb" notation)
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
	 * @param strokeOpacity
	 *            opacity for the stroke
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
	 * @param strokeWidth
	 *            stroke width
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
	 * @param dashArray
	 *            dash array, comma separated list of dash and gap lengths
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
	 * @param symbol
	 *            symbol
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
	 * @param styleId
	 *            style id
	 */
	public void setStyleId(String styleId) {
		this.styleId = styleId;
	}
	
	/**
	 * Returns the geometry type for which this style is applicable. Used to distinguish between geometry types for a
	 * mixed geometry layer when establishing a one-to-one relationship with SLD rules. Defaults to the layer type of
	 * the layer or {@link LayerType#POLYGON} for mixed geometry layers.
	 * 
	 * @return the geometry type for this style (should be one of {@link LayerType#POINT}, {@link LayerType#MULTIPOINT},
	 *         {@link LayerType#LINESTRING}, {@link LayerType#MULTILINESTRING}, {@link LayerType#POLYGON}, or
	 *         {@link LayerType#MULTIPOLYGON}
	 * @since 1.10.0
	 */
	public LayerType getLayerType() {
		return layerType;
	}
	
	/**
	 * Specifies the geometry type for which this style is applicable. Used to distinguish between geometry types for a
	 * mixed geometry layer when establishing a one-to-one relationship with SLD rules. Defaults to the layer type of
	 * the layer or {@link LayerType#POLYGON} for mixed geometry layers.
	 * 
	 * @param layerType the geometry type for this style (should be one of {@link LayerType#POINT},
	 *        {@link LayerType#MULTIPOINT}, {@link LayerType#LINESTRING}, {@link LayerType#MULTILINESTRING},
	 *        {@link LayerType#POLYGON}, or {@link LayerType#MULTIPOLYGON}
	 * @since 1.10.0
	 */
	public void setLayerType(LayerType layerType) {
		this.layerType = layerType;
	}

	/**
	 * String identifier which is guaranteed to include sufficient information to assure to be different for two
	 * instances which could produce different result. It is typically used as basis for calculation of hash codes (like
	 * MD5, SHA1, SHA2 etc) of (collections of) objects.
	 * 
	 * @return cacheId
	 * @since 1.8.0
	 */
	public String getCacheId() {
		return "FeatureStyleInfo{" + "index=" + index + ", name='" + name + '\'' + ", formula='" + formula + '\''
				+ ", fillColor='" + fillColor + '\'' + ", fillOpacity=" + fillOpacity + ", strokeColor='" + strokeColor
				+ '\'' + ", strokeOpacity=" + strokeOpacity + ", strokeWidth=" + strokeWidth + ", dashArray='"
				+ dashArray + '\'' + ", symbol=" + symbol + ", styleId='" + styleId + '\'' + '}';
	}

	/**
	 * String representation of object.
	 * 
	 * @return string representation of object
	 * @since 1.8.0
	 */
	public String toString() {
		return getCacheId();
	}

	/**
	 * Are the two objects equal?
	 * 
	 * @param o
	 *            object to compare
	 * @return true when objects are equal
	 * @since 1.8.0
	 */
	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (!(o instanceof FeatureStyleInfo)) {
			return false;
		}

		FeatureStyleInfo that = (FeatureStyleInfo) o;

		if (Float.compare(that.fillOpacity, fillOpacity) != 0) {
			return false;
		}
		if (index != that.index) {
			return false;
		}
		if (Float.compare(that.strokeOpacity, strokeOpacity) != 0) {
			return false;
		}
		if (strokeWidth != that.strokeWidth) {
			return false;
		}
		if (dashArray != null ? !dashArray.equals(that.dashArray) : that.dashArray != null) {
			return false;
		}
		if (fillColor != null ? !fillColor.equals(that.fillColor) : that.fillColor != null) {
			return false;
		}
		if (formula != null ? !formula.equals(that.formula) : that.formula != null) {
			return false;
		}
		if (name != null ? !name.equals(that.name) : that.name != null) {
			return false;
		}
		if (strokeColor != null ? !strokeColor.equals(that.strokeColor) : that.strokeColor != null) {
			return false;
		}
		if (styleId != null ? !styleId.equals(that.styleId) : that.styleId != null) {
			return false;
		}
		if (symbol != null ? !symbol.equals(that.symbol) : that.symbol != null) {
			return false;
		}

		return true;
	}

	/**
	 * Calculate object hash code.
	 * 
	 * @return hash code
	 * @since 1.8.0
	 */
	@Override
	public int hashCode() {
		int result = index;
		result = PRIME * result + (name != null ? name.hashCode() : 0);
		result = PRIME * result + (formula != null ? formula.hashCode() : 0);
		result = PRIME * result + (fillColor != null ? fillColor.hashCode() : 0);
		result = PRIME * result + (fillOpacity != +0.0f ? (int) (fillOpacity * 10000) : 0);
		result = PRIME * result + (strokeColor != null ? strokeColor.hashCode() : 0);
		result = PRIME * result + (strokeOpacity != +0.0f ? (int) (strokeOpacity * 10000) : 0);
		result = PRIME * result + strokeWidth;
		result = PRIME * result + (dashArray != null ? dashArray.hashCode() : 0);
		result = PRIME * result + (symbol != null ? symbol.hashCode() : 0);
		result = PRIME * result + (styleId != null ? styleId.hashCode() : 0);
		return result;
	}
}
