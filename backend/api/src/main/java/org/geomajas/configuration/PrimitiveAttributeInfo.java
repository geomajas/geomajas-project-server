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
package org.geomajas.configuration;

import org.geomajas.annotation.Api;

/**
 * Primitive attribute configuration information.
 *
 * @author Jan De Moerloose
 * @since 1.6.0
 */
@Api(allMethods = true)
public class PrimitiveAttributeInfo extends AbstractEditableAttributeInfo implements IsInfo {

	private static final long serialVersionUID = 151L;
	private PrimitiveType type;

	/** No-arguments constructor. */
	public PrimitiveAttributeInfo() {
		this(null, null, null);
	}

	/**
	 * Creates an attribute information for the specified primitive type.
	 *
	 * @param name attribute name
	 * @param label attribute label
	 * @param type the primitive type
	 */
	public PrimitiveAttributeInfo(String name, String label, PrimitiveType type) {
		super();
		setName(name);
		setLabel(label);
		this.type = type;
	}

	/**
	 * Get type for the value of the attribute.
	 *
	 * @return type for the value
	 */
	public PrimitiveType getType() {
		return type;
	}

	/**
	 * Set type for the value of the attribute.
	 *
	 * @param type type
	 */
	public void setType(PrimitiveType type) {
		this.type = type;
	}
}
