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
package org.geomajas.plugin.runtimeconfig.dto.bean;

import java.io.Serializable;

/**
 * Data transfer object for a Spring typed string.
 * 
 * @author Jan De Moerloose
 * 
 */
public class TypedStringInfo implements BeanMetadataElementInfo, Serializable {

	private static final long serialVersionUID = 100L;

	private String value;

	public TypedStringInfo() {
	}

	public TypedStringInfo(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String toString() {
		return "[TypeString," + getValue() + "]";
	}

}
