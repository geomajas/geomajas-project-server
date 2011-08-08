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

import org.geomajas.annotation.Api;

/**
 * 
 "OverlapBehavior" tells a system how to behave when multiple raster images in a layer overlap each other, for example
 * with satellite-image scenes.
 * 
 * 
 * Schema fragment(s) for this class:...
 * 
 * <pre>
 * &lt;xs:element
 * xmlns:ns="http://www.opengis.net/sld"
 * xmlns:xs="http://www.w3.org/2001/XMLSchema" name="OverlapBehavior">
 *   &lt;xs:complexType>
 *     &lt;xs:choice>
 *       &lt;xs:element ref="ns:LATEST_ON_TOP"/>
 *       &lt;xs:element ref="ns:EARLIEST_ON_TOP"/>
 *       &lt;xs:element ref="ns:AVERAGE"/>
 *       &lt;xs:element ref="ns:RANDOM"/>
 *     &lt;/xs:choice>
 *   &lt;/xs:complexType>
 * &lt;/xs:element>
 * </pre>
 *
 * @author Jan De Moerloose
 * @since 1.10.0
 */
@Api(allMethods = true)
public class OverlapBehaviorInfo implements Serializable {

	private static final long serialVersionUID = 1100;

	private OverlapBehaviorInfoInner overlapBehavior;

	/**
	 * Get the 'actuate' attribute value.
	 * 
	 * @return value
	 */
	public OverlapBehaviorInfoInner getOverlapBehavior() {
		return overlapBehavior;
	}

	/**
	 * Set the 'actuate' attribute value.
	 * 
	 * @param actuate
	 */
	public void setOverlapBehavior(OverlapBehaviorInfoInner overlapBehavior) {
		this.overlapBehavior = overlapBehavior;
	}

	/**
	 * Inner class for overlap enumeration.
	 * 
	 * @author Jan De Moerloose
	 * 
	 */
	public static enum OverlapBehaviorInfoInner implements Serializable {
		LATEST_ON_TOP("LATEST_ON_TOP"), EARLIEST_ON_TOP("EARLIEST_ON_TOP"), AVERAGE("AVERAGE"), RANDOM("RANDOM");

		private static final long serialVersionUID = 1100;

		private final String value;

		private OverlapBehaviorInfoInner(String value) {
			this.value = value;
		}

		public String xmlValue() {
			return value;
		}

		public static OverlapBehaviorInfoInner convert(String value) {
			for (OverlapBehaviorInfoInner inst : values()) {
				if (inst.xmlValue().equals(value)) {
					return inst;
				}
			}
			return null;
		}
	}
}
