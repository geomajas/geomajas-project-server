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
package org.geomajas.plugin.deskmanager.command.manager.dto;

import java.io.Serializable;

/**
 * Information of a single vector layer from a vector layer source such as WFS or GIS Database.
 * 
 * @author Oliver May
 * 
 */
public class VectorCapabilitiesInfo implements Serializable {

	private static final long serialVersionUID = 1L;

	private String typeName;

	private String namespace;

	private String name;

	private String crs;

	private String geometryType;

	private String description;

	/**
	 * Get the name of the layer, without the namespace. This is not the ID by which the layer is known on the server,
	 * use {@link #getTypeName()} instead.
	 * 
	 * @return the name.
	 */
	public String getName() {
		return name;
	}

	/**
	 * Set the name of the layer, without the namespace. This is not the ID by which the layer is known on the server,
	 * use {@link #setTypeName()} instead.
	 * 
	 * @param name
	 *            the name.
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Get the description of the layer.
	 * 
	 * @return the description.
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Set the description of the layer.
	 * 
	 * @param description
	 *            the description.
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * Get the CRS of the layer.
	 * 
	 * @return the crs.
	 */
	public String getCrs() {
		return crs;
	}

	/**
	 * Set the CRS of the layer.
	 * 
	 * @param crs
	 *            the crs.
	 */
	public void setCrs(String crs) {
		this.crs = crs;
	}

	/**
	 * Get the namespace of the layer.
	 * 
	 * @return the namespace.
	 */
	public String getNamespace() {
		return namespace;
	}

	/**
	 * Set the namespace of the layer.
	 * 
	 * @param namespace
	 *            the namespace.
	 */
	public void setNamespace(String namespace) {
		this.namespace = namespace;
	}

	/**
	 * Get the typename of the layer, this is the id, or featuretype by which the layer is know on the server.
	 * 
	 * @return the typename.
	 */
	public String getTypeName() {
		return typeName;
	}

	/**
	 * Set the typename of the layer, this is de id, or featuretype by which the layer is know on the server.
	 * @param typeName
	 */
	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	
	/**
	 * Get the geometry type of this layer.
	 * @return the geometry type
	 */
	public String getGeometryType() {
		return geometryType;
	}

	/**
	 * Set the geometry type of this layer.
	 * @param geometryType
	 */
	public void setGeometryType(String geometryType) {
		this.geometryType = geometryType;
	}
}
