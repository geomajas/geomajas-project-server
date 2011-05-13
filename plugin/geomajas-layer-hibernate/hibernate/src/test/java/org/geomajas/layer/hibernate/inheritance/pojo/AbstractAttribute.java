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

package org.geomajas.layer.hibernate.inheritance.pojo;

import javax.persistence.Column;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;

/**
 * Abstract attribute. Needs another object to extend it.
 * 
 * @author Pieter De Graef
 */
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public class AbstractAttribute {

	public static final String PARAM_TEXT_ATTR = "textAttr";

	@Column(name = "textAttr")
	private String textAttr;

	// Constructors:

	public AbstractAttribute() {
	}

	public AbstractAttribute(String textAttr) {
		this.textAttr = textAttr;
	}

	// Getters and setters:

	public String getTextAttr() {
		return textAttr;
	}

	public void setTextAttr(String textAttr) {
		this.textAttr = textAttr;
	}
}