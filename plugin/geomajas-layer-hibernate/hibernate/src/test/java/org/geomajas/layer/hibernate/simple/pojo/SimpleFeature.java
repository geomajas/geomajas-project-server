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

package org.geomajas.layer.hibernate.simple.pojo;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.hibernate.annotations.Type;

import com.vividsolutions.jts.geom.Geometry;

/**
 * Hibernate test feature.
 * 
 * @author Pieter De Graef
 * @author Jan De Moerloose
 */
@Entity
@Table(name = "pojo")
public class SimpleFeature {

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

	@Type(type = "org.hibernatespatial.GeometryUserType")
	@Column(name = "the_geom")
	private Geometry geometry;

	// Constructors:

	public SimpleFeature() {
	}

	public SimpleFeature(Long id) {
		this.id = id;
	}

	public SimpleFeature(String textAttr) {
		this.textAttr = textAttr;
	}

	public SimpleFeature(Long id, String textAttr) {
		this.id = id;
		this.textAttr = textAttr;
	}

	// Class specific functions:

	public String toString() {
		return ReflectionToStringBuilder.toString(this);
	}

	public static SimpleFeature getDefaultInstance1(Long id) {
		SimpleFeature p = new SimpleFeature(id);
		p.setTextAttr("default-name-1");
		p.setBooleanAttr(true);
		p.setIntAttr(10);
		p.setFloatAttr(10.0f);
		p.setDoubleAttr(10.0);
		DateFormat format = new SimpleDateFormat("dd/MM/yyyy");
		Date date;
		try {
			date = format.parse("01/01/2009");
		} catch (ParseException e) {
			date = new Date();
		}
		p.setDateAttr(date);
		return p;
	}

	public static SimpleFeature getDefaultInstance2(Long id) {
		SimpleFeature p = new SimpleFeature(id);
		p.setTextAttr("default-name-2");
		p.setBooleanAttr(false);
		p.setIntAttr(20);
		p.setFloatAttr(20.0f);
		p.setDoubleAttr(20.0);
		DateFormat format = new SimpleDateFormat("dd/MM/yyyy");
		Date date;
		try {
			date = format.parse("01/01/2008");
		} catch (ParseException e) {
			date = new Date();
		}
		p.setDateAttr(date);
		return p;
	}

	public static SimpleFeature getDefaultInstance3(Long id) {
		SimpleFeature p = new SimpleFeature(id);
		p.setTextAttr("default-name-3");
		p.setBooleanAttr(true);
		p.setIntAttr(30);
		p.setFloatAttr(30.0f);
		p.setDoubleAttr(30.0);
		DateFormat format = new SimpleDateFormat("dd/MM/yyyy");
		Date date;
		try {
			date = format.parse("01/01/2007");
		} catch (ParseException e) {
			date = new Date();
		}
		p.setDateAttr(date);
		return p;
	}

	public static SimpleFeature getDefaultInstance4(Long id) {
		SimpleFeature p = new SimpleFeature(id);
		p.setTextAttr("default-name-4");
		p.setBooleanAttr(false);
		p.setIntAttr(40);
		p.setFloatAttr(40.0f);
		p.setDoubleAttr(40.0);
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

	public Geometry getGeometry() {
		return geometry;
	}

	public void setGeometry(Geometry geometry) {
		this.geometry = geometry;
	}
}