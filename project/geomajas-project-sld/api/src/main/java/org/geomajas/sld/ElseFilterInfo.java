/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2012 Geosparc nv, http://www.geosparc.com/, Belgium.
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
 * Schema fragment(s) for this class:...
 * 
 * <pre>
 * &lt;xs:element
 * xmlns:ns="http://www.opengis.net/sld"
 * xmlns:xs="http://www.w3.org/2001/XMLSchema" name="ElseFilter">
 *   &lt;xs:complexType/>
 * &lt;/xs:element>
 * </pre>
 * 
 * @author Jan De Moerloose
 * @since 1.10.0
 */

@Api(allMethods = true)
public class ElseFilterInfo implements Serializable {

	private static final long serialVersionUID = 1100;

	protected ElseFilterInfo() {
	}

	@java.lang.Override
	@java.lang.SuppressWarnings("all")
	public java.lang.String toString() {
		return "ElseFilterInfo()";
	}

	@java.lang.Override
	@java.lang.SuppressWarnings("all")
	public boolean equals(final java.lang.Object o) {
		if (o == this) {
			return true;
		}
		if (!(o instanceof ElseFilterInfo)) {
			return false;
		}
		final ElseFilterInfo other = (ElseFilterInfo) o;
		if (!other.canEqual((java.lang.Object) this)) {
			return false;
		}
		return true;
	}

	@java.lang.SuppressWarnings("all")
	public boolean canEqual(final java.lang.Object other) {
		return other instanceof ElseFilterInfo;
	}

	@java.lang.Override
	@java.lang.SuppressWarnings("all")
	public int hashCode() {
		int result = 1;
		return result;
	}
}