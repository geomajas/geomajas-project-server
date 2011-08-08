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
package org.geomajas.sld.filter;

import java.io.Serializable;

import org.geomajas.annotation.Api;
import org.geomajas.sld.expression.PropertyNameInfo;
import org.geomajas.sld.geometry.AbstractGeometryInfo;

/**
 * Schema fragment(s) for this class:...
 * 
 * <pre>
 * &lt;xs:complexType
 * xmlns:ns="http://www.opengis.net/ogc"
 * xmlns:ns1="http://www.opengis.net/gml"
 * xmlns:xs="http://www.w3.org/2001/XMLSchema" name="DistanceBufferType">
 *   &lt;xs:complexContent>
 *     &lt;xs:extension base="ns:SpatialOpsType">
 *       &lt;xs:sequence>
 *         &lt;xs:element ref="ns:PropertyName"/>
 *         &lt;xs:element ref="ns1:_Geometry"/>
 *         &lt;xs:element type="ns:DistanceType" name="Distance"/>
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
public class DistanceBufferTypeInfo extends SpatialOpsTypeInfo implements Serializable {

	private static final long serialVersionUID = 1100;

	private PropertyNameInfo propertyName;

	private AbstractGeometryInfo geometry;

	private DistanceTypeInfo distance;

	/**
	 * Get the 'PropertyName' element value.
	 * 
	 * @return value
	 */
	public PropertyNameInfo getPropertyName() {
		return propertyName;
	}

	/**
	 * Set the 'PropertyName' element value.
	 * 
	 * @param propertyName
	 */
	public void setPropertyName(PropertyNameInfo propertyName) {
		this.propertyName = propertyName;
	}

	/**
	 * Get the '_Geometry' element value.
	 * 
	 * @return value
	 */
	public AbstractGeometryInfo getGeometry() {
		return geometry;
	}

	/**
	 * Set the '_Geometry' element value.
	 * 
	 * @param _Geometry
	 */
	public void setGeometry(AbstractGeometryInfo geometry) {
		this.geometry = geometry;
	}

	/**
	 * Get the 'Distance' element value.
	 * 
	 * @return value
	 */
	public DistanceTypeInfo getDistance() {
		return distance;
	}

	/**
	 * Set the 'Distance' element value.
	 * 
	 * @param distance
	 */
	public void setDistance(DistanceTypeInfo distance) {
		this.distance = distance;
	}
}
