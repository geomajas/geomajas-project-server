/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2013 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the Apache
 * License, Version 2.0. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */
package org.geomajas.sld;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import org.geomajas.annotation.Api;

/**
 * 
 A "Fill" specifies the pattern for filling an area geometry. The allowed CssParameters are: "fill" (color) and
 * "fill-opacity".
 * 
 * 
 * Schema fragment(s) for this class:...
 * 
 * <pre>
 * &lt;xs:element
 * xmlns:ns="http://www.opengis.net/sld"
 * xmlns:xs="http://www.w3.org/2001/XMLSchema" name="Fill">
 *   &lt;xs:complexType>
 *     &lt;xs:sequence>
 *       &lt;xs:element ref="ns:GraphicFill" minOccurs="0"/>
 *       &lt;xs:element ref="ns:CssParameter" minOccurs="0" maxOccurs="unbounded"/>
 *     &lt;/xs:sequence>
 *   &lt;/xs:complexType>
 * &lt;/xs:element>
 * </pre>
 * 
 * @author Jan De Moerloose
 * @since 1.0.0
 */

@Api(allMethods = true)
public class FillInfo implements Serializable {

	private static final String FILL_OPACITY = "fill-opacity";

	private static final String FILL = "fill";

	private static final long serialVersionUID = 100;

	private GraphicFillInfo graphicFill;

	private List<CssParameterInfo> cssParameterList = new ArrayList<CssParameterInfo>();

	/**
	 * Get the 'GraphicFill' element value.
	 * 
	 * @return value
	 */
	public GraphicFillInfo getGraphicFill() {
		return graphicFill;
	}

	/**
	 * Set the 'GraphicFill' element value.
	 * 
	 * @param graphicFill
	 */
	public void setGraphicFill(GraphicFillInfo graphicFill) {
		this.graphicFill = graphicFill;
	}

	/**
	 * Get the list of 'CssParameter' element items.
	 * 
	 * @return list
	 */
	public List<CssParameterInfo> getCssParameterList() {
		return cssParameterList;
	}

	/**
	 * Set the list of 'CssParameter' element items.
	 * 
	 * @param list
	 */
	public void setCssParameterList(List<CssParameterInfo> list) {
		cssParameterList = list;
	}

	/**
	 * Set the fill color.
	 * 
	 * @param fillColor the css fill color
	 */
	public void setFillColor(String fillColor) {
		for (CssParameterInfo param : getCssParameterList()) {
			if (param.getName().equals(FILL)) {
				param.setValue(fillColor);
				return;
			}
		}
		getCssParameterList().add(new CssParameterInfo(FILL, fillColor));
	}

	/**
	 * Set the fill opacity.
	 * @param f the fill opacity
	 */
	public void setFillOpacity(float f) {
		for (CssParameterInfo param : getCssParameterList()) {
			if (param.getName().equals(FILL_OPACITY)) {
				param.setValue(Float.toString(f));
				return;
			}
		}
		getCssParameterList().add(new CssParameterInfo(FILL_OPACITY, Float.toString(f)));
	}

	/** {@inheritDoc} */
	@java.lang.Override
	@java.lang.SuppressWarnings("all")
	public java.lang.String toString() {
		return "FillInfo(graphicFill=" + this.getGraphicFill() + ", cssParameterList=" + this.getCssParameterList()
				+ ")";
	}

	/** {@inheritDoc} */
	@java.lang.Override
	@java.lang.SuppressWarnings("all")
	public boolean equals(final java.lang.Object o) {
		if (o == this) {
			return true;
		}
		if (!(o instanceof FillInfo)) {
			return false;
		}
		final FillInfo other = (FillInfo) o;
		if (!other.canEqual((java.lang.Object) this)) {
			return false;
		}
		if (this.getGraphicFill() == null ? other.getGraphicFill() != null : !this.getGraphicFill().equals(
				(java.lang.Object) other.getGraphicFill())) {
			return false;
		}
		if (this.getCssParameterList() == null ? other.getCssParameterList() != null : !this.getCssParameterList()
				.equals((java.lang.Object) other.getCssParameterList())) {
			return false;
		}
		return true;
	}

	/** {@inheritDoc} */
	@java.lang.SuppressWarnings("all")
	public boolean canEqual(final java.lang.Object other) {
		return other instanceof FillInfo;
	}

	/** {@inheritDoc} */
	@java.lang.Override
	@java.lang.SuppressWarnings("all")
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = result * prime + (this.getGraphicFill() == null ? 0 : this.getGraphicFill().hashCode());
		result = result * prime + (this.getCssParameterList() == null ? 0 : this.getCssParameterList().hashCode());
		return result;
	}
}