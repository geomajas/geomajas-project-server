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
package org.geomajas.sld.geometry;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.geomajas.annotations.Api;

/**
 * 
 A Polygon is defined by an outer boundary and zero or more inner boundaries which are in turn defined by LinearRings.
 * 
 * 
 * Schema fragment(s) for this class:...
 * 
 * <pre>
 * &lt;xs:complexType
 * xmlns:ns="http://www.opengis.net/gml"
 * xmlns:xs="http://www.w3.org/2001/XMLSchema" name="PolygonType">
 *   &lt;xs:complexContent>
 *     &lt;xs:extension base="xs:anyType">
 *       &lt;xs:sequence>
 *         &lt;xs:element ref="ns:outerBoundaryIs"/>
 *         &lt;xs:element ref="ns:innerBoundaryIs" minOccurs="0" maxOccurs="unbounded"/>
 *       &lt;/xs:sequence>
 *     &lt;/xs:extension>
 *   &lt;/xs:complexContent>
 * &lt;/xs:complexType>
 * </pre>
 *
 * @author Jan De Moerloose
 * @since 1.10.0
 */
@Api(allMethods = true)
public class PolygonTypeInfo  extends AbstractGeometryInfo implements Serializable {

	private static final long serialVersionUID = 1100;

	private OuterBoundaryIsInfo outerBoundaryIs;

	private List<InnerBoundaryIsInfo> innerBoundaryIList = new ArrayList<InnerBoundaryIsInfo>();

	/**
	 * Get the 'outerBoundaryIs' element value.
	 * 
	 * @return value
	 */
	public OuterBoundaryIsInfo getOuterBoundaryIs() {
		return outerBoundaryIs;
	}

	/**
	 * Set the 'outerBoundaryIs' element value.
	 * 
	 * @param outerBoundaryIs
	 */
	public void setOuterBoundaryIs(OuterBoundaryIsInfo outerBoundaryIs) {
		this.outerBoundaryIs = outerBoundaryIs;
	}

	/**
	 * Get the list of 'innerBoundaryIs' element items.
	 * 
	 * @return list
	 */
	public List<InnerBoundaryIsInfo> getInnerBoundaryIList() {
		return innerBoundaryIList;
	}

	/**
	 * Set the list of 'innerBoundaryIs' element items.
	 * 
	 * @param list
	 */
	public void setInnerBoundaryIList(List<InnerBoundaryIsInfo> list) {
		innerBoundaryIList = list;
	}
}
