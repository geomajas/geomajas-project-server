/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2011 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
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
 * @since 1.10.0
 */
@Api(allMethods = true)
public class TextSymbolizerInfo extends SymbolizerTypeInfo implements Serializable {

	private static final long serialVersionUID = 1100;

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
}
