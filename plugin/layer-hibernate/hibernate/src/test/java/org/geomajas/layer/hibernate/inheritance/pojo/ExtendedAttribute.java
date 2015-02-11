/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2015 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */

package org.geomajas.layer.hibernate.inheritance.pojo;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * Extended attribute.
 * 
 * @author Pieter De Graef
 */
@Entity
@Table(name = "extendedAttribute")
public class ExtendedAttribute extends AbstractAttribute {

	public static final String PARAM_INT_ATTR = "intAttr";


	@Column(name = "intAttr")
	private Integer intAttr;
	
	
	@ManyToOne(cascade = { CascadeType.ALL })
	private ExtendedHibernateTestFeature feature;

	// Constructors:


	public ExtendedAttribute(Long id) {
		super(id);
	}

	public ExtendedAttribute(String textAttr) {
		super(null, textAttr);
	}

	public ExtendedAttribute(Long id, String textAttr) {
		super(id, textAttr);
	}
	
	

	// Class specific functions:

	public static ExtendedAttribute getDefaultInstance1(Long id) {
		ExtendedAttribute p = new ExtendedAttribute(id);
		p.setTextAttr("extended-attribute-1");
		p.setIntAttr(100);
		return p;
	}

	public static ExtendedAttribute getDefaultInstance2(Long id) {
		ExtendedAttribute p = new ExtendedAttribute(id);
		p.setTextAttr("extended-attribute-2");
		p.setIntAttr(200);
		return p;
	}

	public static ExtendedAttribute getDefaultInstance3(Long id) {
		ExtendedAttribute p = new ExtendedAttribute(id);
		p.setTextAttr("extended-attribute-3");
		p.setIntAttr(300);
		return p;
	}

	public static ExtendedAttribute getDefaultInstance4(Long id) {
		ExtendedAttribute p = new ExtendedAttribute(id);
		p.setTextAttr("extended-attribute-4");
		p.setIntAttr(400);
		return p;
	}

	// Getters and setters:

	public Integer getIntAttr() {
		return intAttr;
	}

	public void setIntAttr(Integer intAttr) {
		this.intAttr = intAttr;
	}

	
	public ExtendedHibernateTestFeature getFeature() {
		return feature;
	}

	
	public void setFeature(ExtendedHibernateTestFeature feature) {
		this.feature = feature;
	}
	
	
}