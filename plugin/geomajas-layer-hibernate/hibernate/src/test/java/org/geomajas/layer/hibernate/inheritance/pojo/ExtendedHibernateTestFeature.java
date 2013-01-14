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

package org.geomajas.layer.hibernate.inheritance.pojo;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

/**
 * Extended Hibernate test feature.
 * 
 * @author Pieter De Graef
 */
@Entity
@Table(name = "extendedFeature")
public class ExtendedHibernateTestFeature extends AbstractHibernateTestFeature {

	public static final String PARAM_INT_ATTR = "intAttr";


	@Column(name = "intAttr")
	private Integer intAttr;

	// Constructors:

	public ExtendedHibernateTestFeature(Long id) {
		super(id);
	}

	public ExtendedHibernateTestFeature(Long id, String textAttr, Integer intAttr) {
		super(id, textAttr);
		this.intAttr = intAttr;
	}

	// Class specific functions:

	public String toString() {
		return ReflectionToStringBuilder.toString(this);
	}

	public static ExtendedHibernateTestFeature getDefaultInstance1(Long id) {
		ExtendedHibernateTestFeature p = new ExtendedHibernateTestFeature(id);
		p.setTextAttr("abstract-name-1");
		p.setIntAttr(new Integer(100));
		p.setManyToOne(ExtendedAttribute.getDefaultInstance1(id + 1));
		Set<ExtendedAttribute> otm = new HashSet<ExtendedAttribute>();
		otm.add(ExtendedAttribute.getDefaultInstance1(id + 2));
		otm.add(ExtendedAttribute.getDefaultInstance2(id + 3));
		p.setOneToMany(otm);
		return p;
	}

	public static ExtendedHibernateTestFeature getDefaultInstance2(Long id) {
		ExtendedHibernateTestFeature p = new ExtendedHibernateTestFeature(id);
		p.setTextAttr("abstract-name-2");
		p.setIntAttr(new Integer(200));
		p.setManyToOne(ExtendedAttribute.getDefaultInstance3(id + 1));
		Set<ExtendedAttribute> otm = new HashSet<ExtendedAttribute>();
		otm.add(ExtendedAttribute.getDefaultInstance3(id + 2));
		otm.add(ExtendedAttribute.getDefaultInstance4(id + 3));
		p.setOneToMany(otm);
		return p;
	}

	// Getters and setters:


	public Integer getIntAttr() {
		return intAttr;
	}

	public void setIntAttr(Integer intAttr) {
		this.intAttr = intAttr;
	}
}