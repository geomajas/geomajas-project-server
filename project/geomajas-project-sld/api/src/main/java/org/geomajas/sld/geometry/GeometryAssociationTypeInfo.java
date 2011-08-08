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

import org.geomajas.annotations.Api;
import org.geomajas.sld.xlink.SimpleLinkInfo;

/**
 * 
 An instance of this type (e.g. a geometryMember) can either enclose or point to a primitive geometry element. When
 * serving as a simple link that references a remote geometry instance, the value of the gml:remoteSchema attribute can
 * be used to locate a schema fragment that constrains the target instance.
 * 
 * 
 * Schema fragment(s) for this class:...
 * 
 * <pre>
 * &lt;xs:complexType
 * xmlns:gml="http://www.opengis.net/gml"
 * xmlns:ns="http://www.w3.org/1999/xlink"
 * xmlns:xs="http://www.w3.org/2001/XMLSchema" name="GeometryAssociationType">
 *   &lt;xs:sequence>
 *     &lt;xs:element ref="gml:_Geometry" minOccurs="0"/>
 *   &lt;/xs:sequence>
 *   &lt;xs:attributeGroup ref="ns:simpleLink"/>
 *   &lt;xs:attribute use="optional" ref="gml:remoteSchema">
 *     &lt;!-- Reference to inner class RemoteSchemaInfo -->
 *   &lt;/xs:attribute>
 * &lt;/xs:complexType>
 * </pre>
 *
 * @author Jan De Moerloose
 * @since 1.10.0
 */
@Api(allMethods = true)
public class GeometryAssociationTypeInfo implements Serializable {

	private static final long serialVersionUID = 1100;

	private AbstractGeometryInfo geometry;

	private SimpleLinkInfo simpleLink;

	private RemoteSchemaInfo remoteSchema;

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
	 * Get the 'simpleLink' attributeGroup value.
	 * 
	 * @return value
	 */
	public SimpleLinkInfo getSimpleLink() {
		return simpleLink;
	}

	/**
	 * Set the 'simpleLink' attributeGroup value.
	 * 
	 * @param simpleLink
	 */
	public void setSimpleLink(SimpleLinkInfo simpleLink) {
		this.simpleLink = simpleLink;
	}

	/**
	 * Get the 'remoteSchema' attribute value.
	 * 
	 * @return value
	 */
	public RemoteSchemaInfo getRemoteSchema() {
		return remoteSchema;
	}

	/**
	 * Set the 'remoteSchema' attribute value.
	 * 
	 * @param remoteSchema
	 */
	public void setRemoteSchema(RemoteSchemaInfo remoteSchema) {
		this.remoteSchema = remoteSchema;
	}

	/**
	 * Schema fragment(s) for this class:...
	 * 
	 * <pre>
	 * &lt;xs:attribute
 * xmlns:ns="http://www.opengis.net/gml"
 * xmlns:xs="http://www.w3.org/2001/XMLSchema" use="optional" ref="ns:remoteSchema"/>
	 * 
	 * &lt;xs:attribute
 * xmlns:ns="http://www.opengis.net/gml"
 * xmlns:xs="http://www.w3.org/2001/XMLSchema" type="xs:string" name="remoteSchema"/>
	 * </pre>
	 */
	public static class RemoteSchemaInfo implements Serializable {

		private static final long serialVersionUID = 1100;

		private String remoteSchema;

		/**
		 * Get the 'remoteSchema' attribute value.
		 * 
		 * @return value
		 */
		public String getRemoteSchema() {
			return remoteSchema;
		}

		/**
		 * Set the 'remoteSchema' attribute value.
		 * 
		 * @param remoteSchema
		 */
		public void setRemoteSchema(String remoteSchema) {
			this.remoteSchema = remoteSchema;
		}
	}
}
