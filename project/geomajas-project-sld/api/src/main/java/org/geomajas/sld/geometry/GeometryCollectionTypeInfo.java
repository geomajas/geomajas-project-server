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

import org.geomajas.annotation.Api;

/**
 * 
 A geometry collection must include one or more geometries, referenced through geometryMember elements. User-defined
 * geometry collections that accept GML geometry classes as members must instantiate--or derive from--this type.
 * 
 * 
 * Schema fragment(s) for this class:...
 * 
 * <pre>
 * &lt;xs:complexType
 * xmlns:ns="http://www.opengis.net/gml"
 * xmlns:xs="http://www.w3.org/2001/XMLSchema" name="GeometryCollectionType">
 *   &lt;xs:complexContent>
 *     &lt;xs:extension base="xs:anyType">
 *       &lt;xs:sequence>
 *         &lt;xs:element ref="ns:geometryMember" maxOccurs="unbounded"/>
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
public class GeometryCollectionTypeInfo extends AbstractGeometryInfo implements Serializable {

	private static final long serialVersionUID = 1100;

	private List<GeometryMemberInfo> geometryMemberList = new ArrayList<GeometryMemberInfo>();

	/**
	 * Get the list of 'geometryMember' element items.
	 * 
	 * @return list
	 */
	public List<GeometryMemberInfo> getGeometryMemberList() {
		return geometryMemberList;
	}

	/**
	 * Set the list of 'geometryMember' element items.
	 * 
	 * @param list
	 */
	public void setGeometryMemberList(List<GeometryMemberInfo> list) {
		geometryMemberList = list;
	}
}
