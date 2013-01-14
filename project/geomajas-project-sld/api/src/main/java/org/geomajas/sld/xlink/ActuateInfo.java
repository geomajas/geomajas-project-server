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
package org.geomajas.sld.xlink;

import java.io.Serializable;

import org.geomajas.annotation.Api;

/**
 * The 'actuate' attribute is used to communicate the desired timing of traversal from the starting resource to the
 * ending resource; it's value should be treated as follows: onLoad - traverse to the ending resource immediately on
 * loading the starting resource onRequest - traverse from the starting resource to the ending resource only on a
 * post-loading event triggered for this purpose other - behavior is unconstrained; examine other markup in link for
 * hints none - behavior is unconstrained
 * 
 * 
 * Schema fragment(s) for this class:...
 * 
 * <pre>
 * &lt;xs:attribute
 * xmlns:ns="http://www.w3.org/1999/xlink"
 * xmlns:xs="http://www.w3.org/2001/XMLSchema" name="actuate">
 *   &lt;xs:simpleType>
 *     &lt;!-- Reference to inner class ActuateInfoInner -->
 *   &lt;/xs:simpleType>
 * &lt;/xs:attribute>
 * </pre>
 * 
 * @author Jan De Moerloose
 * @since 1.0.0
 */
@Api(allMethods = true)
public enum ActuateInfo implements Serializable {
	/**
	 * Traverse to the ending resource immediately on loading the starting resource.
	 */
	ON_LOAD("onLoad"),
	/**
	 * Traverse from the starting resource to the ending resource only on a post-loading event triggered for this
	 * purpose.
	 */
	ON_REQUEST("onRequest"),
	/**
	 * Behavior is unconstrained; examine other markup in link for hints.
	 */
	OTHER("other"),
	/**
	 * Behavior is unconstrained.
	 */
	NONE("none");

	private static final long serialVersionUID = 100;

	private final String value;

	private ActuateInfo(String value) {
		this.value = value;
	}

	/**
	 * Get the text value of the actuation attribute.
	 * 
	 * @return the string value
	 */
	public String xmlValue() {
		return value;
	}

	/**
	 * Convert a string to an {@link ActuateInfo} object.
	 * 
	 * @param value the string value
	 * @return the object
	 */
	public static ActuateInfo convert(String value) {
		for (ActuateInfo inst : values()) {
			if (inst.xmlValue().equals(value)) {
				return inst;
			}
		}
		return null;
	}
}
