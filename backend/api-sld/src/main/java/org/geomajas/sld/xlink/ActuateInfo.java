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
package org.geomajas.sld.xlink;

import java.io.Serializable;

import org.geomajas.global.Api;

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
 * @since 1.10.0
 */
@Api(allMethods = true)
public class ActuateInfo implements Serializable {

	private static final long serialVersionUID = 1100;

	private ActuateInfoInner actuate;

	/**
	 * Get the 'actuate' attribute value.
	 * 
	 * @return value
	 */
	public ActuateInfoInner getActuate() {
		return actuate;
	}

	/**
	 * Set the 'actuate' attribute value.
	 * 
	 * @param actuate
	 */
	public void setActuate(ActuateInfoInner actuate) {
		this.actuate = actuate;
	}

	/**
	 * Schema fragment(s) for this class:...
	 * 
	 * <pre>
	 * &lt;xs:simpleType
 * xmlns:xs="http://www.w3.org/2001/XMLSchema">
	 *   &lt;xs:restriction base="xs:string">
	 *     &lt;xs:enumeration value="onLoad"/>
	 *     &lt;xs:enumeration value="onRequest"/>
	 *     &lt;xs:enumeration value="other"/>
	 *     &lt;xs:enumeration value="none"/>
	 *   &lt;/xs:restriction>
	 * &lt;/xs:simpleType>
	 * </pre>
	 */
	public static enum ActuateInfoInner implements Serializable {
		ON_LOAD("onLoad"), ON_REQUEST("onRequest"), OTHER("other"), NONE("none");

		private static final long serialVersionUID = 1100;

		private final String value;

		private ActuateInfoInner(String value) {
			this.value = value;
		}

		public String xmlValue() {
			return value;
		}

		public static ActuateInfoInner convert(String value) {
			for (ActuateInfoInner inst : values()) {
				if (inst.xmlValue().equals(value)) {
					return inst;
				}
			}
			return null;
		}
	}
}
