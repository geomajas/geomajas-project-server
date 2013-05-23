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
 A "TextSymbolizer" is used to render text labels according to various graphical parameters.
 * 
 * 
 * Schema fragment(s) for this class:...
 * 
 * <pre>
 * &lt;xs:element
 * xmlns:sld="http://www.opengis.net/sld"
 * xmlns:xs="http://www.w3.org/2001/XMLSchema" substitutionGroup="sld:Symbolizer" name="TextSymbolizer">
 *   &lt;xs:complexType>
 *     &lt;xs:complexContent>
 *       &lt;xs:extension base="sld:SymbolizerType">
 *         &lt;xs:sequence>
 *           &lt;xs:element ref="sld:Geometry" minOccurs="0"/>
 *           &lt;xs:element ref="sld:Label" minOccurs="0"/>
 *           &lt;xs:element ref="sld:Font" minOccurs="0"/>
 *           &lt;xs:element ref="sld:LabelPlacement" minOccurs="0"/>
 *           &lt;xs:element ref="sld:Halo" minOccurs="0"/>
 *           &lt;xs:element ref="sld:Fill" minOccurs="0"/>
 *           &lt;xs:element ref="sld:Graphic" minOccurs="0"/>
 *           &lt;xs:element ref="sld:Priority" minOccurs="0"/>
 *           &lt;xs:element ref="sld:VendorOption" minOccurs="0" maxOccurs="unbounded"/>
 *         &lt;/xs:sequence>
 *       &lt;/xs:extension>
 *     &lt;/xs:complexContent>
 *   &lt;/xs:complexType>
 * &lt;/xs:element>
 * </pre>
 * 
 * @author Jan De Moerloose
 * @since 1.0.0
 */

@Api(allMethods = true)
public class TextSymbolizerInfo extends SymbolizerTypeInfo implements Serializable {

	private static final long serialVersionUID = 100;

	private GeometryInfo geometry;

	private LabelInfo label;

	private FontInfo font;

	private LabelPlacementInfo labelPlacement;

	private HaloInfo halo;

	private FillInfo fill;

	private GraphicInfo graphic;

	private PriorityInfo priority;

	private List<VendorOptionInfo> vendorOptionList = new ArrayList<VendorOptionInfo>();

	/**
	 * Get the 'Geometry' element value.
	 * 
	 * @return value
	 */
	public GeometryInfo getGeometry() {
		return geometry;
	}

	/**
	 * Set the 'Geometry' element value.
	 * 
	 * @param geometry
	 */
	public void setGeometry(GeometryInfo geometry) {
		this.geometry = geometry;
	}

	/**
	 * Get the 'Label' element value.
	 * 
	 * @return value
	 */
	public LabelInfo getLabel() {
		return label;
	}

	/**
	 * Set the 'Label' element value.
	 * 
	 * @param label
	 */
	public void setLabel(LabelInfo label) {
		this.label = label;
	}

	/**
	 * Get the 'Font' element value.
	 * 
	 * @return value
	 */
	public FontInfo getFont() {
		return font;
	}

	/**
	 * Set the 'Font' element value.
	 * 
	 * @param font
	 */
	public void setFont(FontInfo font) {
		this.font = font;
	}

	/**
	 * Get the 'LabelPlacement' element value.
	 * 
	 * @return value
	 */
	public LabelPlacementInfo getLabelPlacement() {
		return labelPlacement;
	}

	/**
	 * Set the 'LabelPlacement' element value.
	 * 
	 * @param labelPlacement
	 */
	public void setLabelPlacement(LabelPlacementInfo labelPlacement) {
		this.labelPlacement = labelPlacement;
	}

	/**
	 * Get the 'Halo' element value.
	 * 
	 * @return value
	 */
	public HaloInfo getHalo() {
		return halo;
	}

	/**
	 * Set the 'Halo' element value.
	 * 
	 * @param halo
	 */
	public void setHalo(HaloInfo halo) {
		this.halo = halo;
	}

	/**
	 * Get the 'Fill' element value.
	 * 
	 * @return value
	 */
	public FillInfo getFill() {
		return fill;
	}

	/**
	 * Set the 'Fill' element value.
	 * 
	 * @param fill
	 */
	public void setFill(FillInfo fill) {
		this.fill = fill;
	}

	/**
	 * Get the 'Graphic' element value.
	 * 
	 * @return value
	 */
	public GraphicInfo getGraphic() {
		return graphic;
	}

	/**
	 * Set the 'Graphic' element value.
	 * 
	 * @param graphic
	 */
	public void setGraphic(GraphicInfo graphic) {
		this.graphic = graphic;
	}

	/**
	 * Get the 'Priority' element value.
	 * 
	 * @return value
	 */
	public PriorityInfo getPriority() {
		return priority;
	}

	/**
	 * Set the 'Priority' element value.
	 * 
	 * @param priority
	 */
	public void setPriority(PriorityInfo priority) {
		this.priority = priority;
	}

	/**
	 * Get the list of 'VendorOption' element items.
	 * 
	 * @return list
	 */
	public List<VendorOptionInfo> getVendorOptionList() {
		return vendorOptionList;
	}

	/**
	 * Set the list of 'VendorOption' element items.
	 * 
	 * @param list
	 */
	public void setVendorOptionList(List<VendorOptionInfo> list) {
		vendorOptionList = list;
	}

	@Override
	@java.lang.SuppressWarnings("all")
	public java.lang.String toString() {
		return "TextSymbolizerInfo(geometry=" + this.getGeometry() + ", label=" + this.getLabel() + ", font="
				+ this.getFont() + ", labelPlacement=" + this.getLabelPlacement() + ", halo=" + this.getHalo()
				+ ", fill=" + this.getFill() + ", graphic=" + this.getGraphic() + ", priority=" + this.getPriority()
				+ ", vendorOptionList=" + this.getVendorOptionList() + ")";
	}

	@Override
	@java.lang.SuppressWarnings("all")
	public boolean equals(final java.lang.Object o) {
		if (o == this) {
			return true;
		}
		if (!(o instanceof TextSymbolizerInfo)) {
			return false;
		}
		final TextSymbolizerInfo other = (TextSymbolizerInfo) o;
		if (!other.canEqual((java.lang.Object) this)) {
			return false;
		}
		if (!super.equals(o)) {
			return false;
		}
		if (this.getGeometry() == null ? other.getGeometry() != null : !this.getGeometry().equals(
				(java.lang.Object) other.getGeometry())) {
			return false;
		}
		if (this.getLabel() == null ? other.getLabel() != null : !this.getLabel().equals(
				(java.lang.Object) other.getLabel())) {
			return false;
		}
		if (this.getFont() == null ? other.getFont() != null : !this.getFont().equals(
				(java.lang.Object) other.getFont())) {
			return false;
		}
		if (this.getLabelPlacement() == null ? other.getLabelPlacement() != null : !this.getLabelPlacement().equals(
				(java.lang.Object) other.getLabelPlacement())) {
			return false;
		}
		if (this.getHalo() == null ? other.getHalo() != null : !this.getHalo().equals(
				(java.lang.Object) other.getHalo())) {
			return false;
		}
		if (this.getFill() == null ? other.getFill() != null : !this.getFill().equals(
				(java.lang.Object) other.getFill())) {
			return false;
		}
		if (this.getGraphic() == null ? other.getGraphic() != null : !this.getGraphic().equals(
				(java.lang.Object) other.getGraphic())) {
			return false;
		}
		if (this.getPriority() == null ? other.getPriority() != null : !this.getPriority().equals(
				(java.lang.Object) other.getPriority())) {
			return false;
		}
		if (this.getVendorOptionList() == null ? other.getVendorOptionList() != null : !this.getVendorOptionList()
				.equals((java.lang.Object) other.getVendorOptionList())) {
			return false;
		}
		return true;
	}

	/**
	 * Is there a chance that the object are equal? Verifies that the other object has a comparable type.
	 *
	 * @param other other object
	 * @return true when other is an instance of this type
	 */
	public boolean canEqual(final java.lang.Object other) {
		return other instanceof TextSymbolizerInfo;
	}

	@Override
	@java.lang.SuppressWarnings("all")
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = result * prime + super.hashCode();
		result = result * prime + (this.getGeometry() == null ? 0 : this.getGeometry().hashCode());
		result = result * prime + (this.getLabel() == null ? 0 : this.getLabel().hashCode());
		result = result * prime + (this.getFont() == null ? 0 : this.getFont().hashCode());
		result = result * prime + (this.getLabelPlacement() == null ? 0 : this.getLabelPlacement().hashCode());
		result = result * prime + (this.getHalo() == null ? 0 : this.getHalo().hashCode());
		result = result * prime + (this.getFill() == null ? 0 : this.getFill().hashCode());
		result = result * prime + (this.getGraphic() == null ? 0 : this.getGraphic().hashCode());
		result = result * prime + (this.getPriority() == null ? 0 : this.getPriority().hashCode());
		result = result * prime + (this.getVendorOptionList() == null ? 0 : this.getVendorOptionList().hashCode());
		return result;
	}
}