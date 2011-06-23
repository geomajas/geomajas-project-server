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
 * 
 The 'show' attribute is used to communicate the desired presentation of the ending resource on traversal from the
 * starting resource; it's value should be treated as follows: new - load ending resource in a new window, frame, pane,
 * or other presentation context replace - load the resource in the same window, frame, pane, or other presentation
 * context embed - load ending resource in place of the presentation of the starting resource other - behavior is
 * unconstrained; examine other markup in the link for hints none - behavior is unconstrained
 * 
 * 
 * Schema fragment(s) for this class:...
 * 
 * <pre>
 * &lt;xs:attribute
 * xmlns:ns="http://www.w3.org/1999/xlink"
 * xmlns:xs="http://www.w3.org/2001/XMLSchema" name="show">
 *   &lt;xs:simpleType>
 *     &lt;!-- Reference to inner class ShowInfoInner -->
 *   &lt;/xs:simpleType>
 * &lt;/xs:attribute>
 * </pre>
 *
 * @author Jan De Moerloose
 * @since 1.10.0
 */
@Api(allMethods = true)
public class ShowInfo implements Serializable {

	private static final long serialVersionUID = 1100;

	private ShowInfoInner show;

	/**
	 * Get the 'show' attribute value.
	 * 
	 * @return value
	 */
	public ShowInfoInner getShow() {
		return show;
	}

	/**
	 * Set the 'show' attribute value.
	 * 
	 * @param show
	 */
	public void setShow(ShowInfoInner show) {
		this.show = show;
	}

	/**
	 * Schema fragment(s) for this class:...
	 * 
	 * <pre>
	 * &lt;xs:simpleType
 * xmlns:xs="http://www.w3.org/2001/XMLSchema">
	 *   &lt;xs:restriction base="xs:string">
	 *     &lt;xs:enumeration value="new"/>
	 *     &lt;xs:enumeration value="replace"/>
	 *     &lt;xs:enumeration value="embed"/>
	 *     &lt;xs:enumeration value="other"/>
	 *     &lt;xs:enumeration value="none"/>
	 *   &lt;/xs:restriction>
	 * &lt;/xs:simpleType>
	 * </pre>
	 */
	public static enum ShowInfoInner implements Serializable {
		NEW("new"), REPLACE("replace"), EMBED("embed"), OTHER("other"), NONE("none");

		private static final long serialVersionUID = 1100;

		private final String value;

		private ShowInfoInner(String value) {
			this.value = value;
		}

		public String xmlValue() {
			return value;
		}

		public static ShowInfoInner convert(String value) {
			for (ShowInfoInner inst : values()) {
				if (inst.xmlValue().equals(value)) {
					return inst;
				}
			}
			return null;
		}
	}
}
