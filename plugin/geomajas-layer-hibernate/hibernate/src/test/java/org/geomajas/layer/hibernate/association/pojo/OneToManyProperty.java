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

package org.geomajas.layer.hibernate.association.pojo;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

/**
 * Attribute object that is used as a one-to-many association in the Hibernate model.
 * 
 * @author Pieter De Graef
 */
@Entity
@Table(name = "oneToManyProperty")
public class OneToManyProperty {

	public static final String PARAM_TEXT_ATTR = "textAttr";

	public static final String PARAM_INT_ATTR = "intAttr";

	public static final String PARAM_FLOAT_ATTR = "floatAttr";

	public static final String PARAM_DOUBLE_ATTR = "doubleAttr";

	public static final String PARAM_BOOLEAN_ATTR = "booleanAttr";

	public static final String PARAM_DATE_ATTR = "dateAttr";

	@Id
	@GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;

	@Column(name = "textAttr")
	private String textAttr;

	@Column(name = "intAttr")
	private Integer intAttr;

	@Column(name = "floatAttr")
	private Float floatAttr;

	@Column(name = "doubleAttr")
	private Double doubleAttr;

	@Column(name = "booleanAttr")
	private Boolean booleanAttr;

	@Column(name = "dateAttr")
	private Date dateAttr;

	@ManyToOne
	private AssociationFeature feature;

	// Constructors:

	public OneToManyProperty() {
	}

	public OneToManyProperty(Long id) {
		this.id = id;
	}

	public OneToManyProperty(String textAttr) {
		this.textAttr = textAttr;
	}

	public OneToManyProperty(Long id, String textAttr) {
		this.id = id;
		this.textAttr = textAttr;
	}

	// Class specific functions:

	public String toString() {
		return ReflectionToStringBuilder.toString(this);
	}

	public static OneToManyProperty getDefaultInstance1(Long id, AssociationFeature feature) {
		OneToManyProperty p = new OneToManyProperty(id);
		p.setTextAttr("oneToMany-1");
		p.setBooleanAttr(true);
		p.setIntAttr(1000);
		p.setFloatAttr(1000.0f);
		p.setDoubleAttr(1000.0);
		DateFormat format = new SimpleDateFormat("dd/MM/yyyy");
		Date date;
		try {
			date = format.parse("01/01/2009");
		} catch (ParseException e) {
			date = new Date();
		}
		p.setDateAttr(date);
		p.setFeature(feature);
		return p;
	}

	public static OneToManyProperty getDefaultInstance2(Long id, AssociationFeature feature) {
		OneToManyProperty p = new OneToManyProperty(id);
		p.setTextAttr("oneToMany-2");
		p.setBooleanAttr(false);
		p.setIntAttr(2000);
		p.setFloatAttr(2000.0f);
		p.setDoubleAttr(2000.0);
		DateFormat format = new SimpleDateFormat("dd/MM/yyyy");
		Date date;
		try {
			date = format.parse("01/01/2008");
		} catch (ParseException e) {
			date = new Date();
		}
		p.setDateAttr(date);
		p.setFeature(feature);
		return p;
	}

	public static OneToManyProperty getDefaultInstance3(Long id, AssociationFeature feature) {
		OneToManyProperty p = new OneToManyProperty(id);
		p.setTextAttr("oneToMany-3");
		p.setBooleanAttr(true);
		p.setIntAttr(3000);
		p.setFloatAttr(3000.0f);
		p.setDoubleAttr(3000.0);
		DateFormat format = new SimpleDateFormat("dd/MM/yyyy");
		Date date;
		try {
			date = format.parse("01/01/2007");
		} catch (ParseException e) {
			date = new Date();
		}
		p.setDateAttr(date);
		p.setFeature(feature);
		return p;
	}

	public static OneToManyProperty getDefaultInstance4(Long id, AssociationFeature feature) {
		OneToManyProperty p = new OneToManyProperty(id);
		p.setTextAttr("oneToMany-4");
		p.setBooleanAttr(false);
		p.setIntAttr(4000);
		p.setFloatAttr(4000.0f);
		p.setDoubleAttr(4000.0);
		DateFormat format = new SimpleDateFormat("dd/MM/yyyy");
		Date date;
		try {
			date = format.parse("01/01/2006");
		} catch (ParseException e) {
			date = new Date();
		}
		p.setDateAttr(date);
		p.setFeature(feature);
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

	public Integer getIntAttr() {
		return intAttr;
	}

	public void setIntAttr(Integer intAttr) {
		this.intAttr = intAttr;
	}

	public Float getFloatAttr() {
		return floatAttr;
	}

	public void setFloatAttr(Float floatAttr) {
		this.floatAttr = floatAttr;
	}

	public Double getDoubleAttr() {
		return doubleAttr;
	}

	public void setDoubleAttr(Double doubleAttr) {
		this.doubleAttr = doubleAttr;
	}

	public Boolean getBooleanAttr() {
		return booleanAttr;
	}

	public void setBooleanAttr(Boolean booleanAttr) {
		this.booleanAttr = booleanAttr;
	}

	public Date getDateAttr() {
		return dateAttr;
	}

	public void setDateAttr(Date dateAttr) {
		this.dateAttr = dateAttr;
	}

	public AssociationFeature getFeature() {
		return feature;
	}

	public void setFeature(AssociationFeature feature) {
		this.feature = feature;
	}
}