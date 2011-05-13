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

package org.geomajas.layer.hibernate.recursive.pojo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

/**
 * Test attribute that is used a many-to-one or one-to-many in the recursive testing model.
 * 
 * @author Pieter De Graef
 */
@Entity
@Table(name = "recursiveAttribute")
public class RecursiveAttribute {

	public static final String PARAM_TEXT_ATTR = "textAttr";

	@Id
	@GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;

	@Column(name = "textAttr")
	private String textAttr;

	// Constructors:

	public RecursiveAttribute() {
	}

	public RecursiveAttribute(Long id) {
		this.id = id;
	}

	public RecursiveAttribute(String textAttr) {
		this.textAttr = textAttr;
	}

	public RecursiveAttribute(Long id, String textAttr) {
		this.id = id;
		this.textAttr = textAttr;
	}

	// Class specific functions:

	public String toString() {
		return ReflectionToStringBuilder.toString(this);
	}

	public static RecursiveAttribute getDefaultInstance1(Long id) {
		RecursiveAttribute p = new RecursiveAttribute(id);
		p.setTextAttr("recursiveAttributeMTO-1");
		return p;
	}

	public static RecursiveAttribute getDefaultInstance2(Long id) {
		RecursiveAttribute p = new RecursiveAttribute(id);
		p.setTextAttr("recursiveAttributeMTO-2");
		return p;
	}

	public static RecursiveAttribute getDefaultInstance3(Long id) {
		RecursiveAttribute p = new RecursiveAttribute(id);
		p.setTextAttr("recursiveAttributeMTO-3");
		return p;
	}

	public static RecursiveAttribute getDefaultInstance4(Long id) {
		RecursiveAttribute p = new RecursiveAttribute(id);
		p.setTextAttr("recursiveAttributeMTO-4");
		return p;
	}

	// Getters and setters:

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTextAttr() {
		return textAttr;
	}

	public void setTextAttr(String textAttr) {
		this.textAttr = textAttr;
	}
}