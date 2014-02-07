/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2014 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */

package org.geomajas.layer.hibernate.pojo;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.geomajas.layer.feature.attribute.AssociationValue;
import org.geomajas.layer.feature.attribute.BooleanAttribute;
import org.geomajas.layer.feature.attribute.DateAttribute;
import org.geomajas.layer.feature.attribute.DoubleAttribute;
import org.geomajas.layer.feature.attribute.FloatAttribute;
import org.geomajas.layer.feature.attribute.IntegerAttribute;
import org.geomajas.layer.feature.attribute.LongAttribute;
import org.geomajas.layer.feature.attribute.ManyToOneAttribute;
import org.geomajas.layer.feature.attribute.PrimitiveAttribute;
import org.geomajas.layer.feature.attribute.StringAttribute;

/**
 * Hibernate test object for associations.
 *
 * @author Pieter De Graef
 * @author Jan De Moerloose
 */
@Entity
@Table(name = "manyToOne")
public class HibernateTestManyToOne {

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

	// Constructors:

	public HibernateTestManyToOne() {
	}

	public HibernateTestManyToOne(Long id) {
		this.id = id;
	}

	public HibernateTestManyToOne(String textAttr) {
		this.textAttr = textAttr;
	}

	public HibernateTestManyToOne(Long id, String textAttr) {
		this.id = id;
		this.textAttr = textAttr;
	}

	// Class specific functions:

	public String toString() {
		return ReflectionToStringBuilder.toString(this);
	}

	public static HibernateTestManyToOne getDefaultInstance1(Long id) {
		HibernateTestManyToOne p = new HibernateTestManyToOne(id);
		p.setTextAttr("manyToOne-1");
		p.setBooleanAttr(true);
		p.setIntAttr(100);
		p.setFloatAttr(100.0f);
		p.setDoubleAttr(100.0);
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

	public static HibernateTestManyToOne getDefaultInstance2(Long id) {
		HibernateTestManyToOne p = new HibernateTestManyToOne(id);
		p.setTextAttr("manyToOne-2");
		p.setBooleanAttr(false);
		p.setIntAttr(200);
		p.setFloatAttr(200.0f);
		p.setDoubleAttr(200.0);
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

	public static HibernateTestManyToOne getDefaultInstance3(Long id) {
		HibernateTestManyToOne p = new HibernateTestManyToOne(id);
		p.setTextAttr("manyToOne-3");
		p.setBooleanAttr(true);
		p.setIntAttr(300);
		p.setFloatAttr(300.0f);
		p.setDoubleAttr(300.0);
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

	public static HibernateTestManyToOne getDefaultInstance4(Long id) {
		HibernateTestManyToOne p = new HibernateTestManyToOne(id);
		p.setTextAttr("manyToOne-4");
		p.setBooleanAttr(false);
		p.setIntAttr(400);
		p.setFloatAttr(400.0f);
		p.setDoubleAttr(400.0);
		DateFormat format = new SimpleDateFormat("dd/MM/yyyy");
		Date date;
		try {
			date = format.parse("01/01/2006");
		} catch (ParseException e) {
			date = new Date();
		}
		p.setDateAttr(date);
		return p;
	}

	public static ManyToOneAttribute getDefaultAttributeInstance1(Long id) {
		 Map<String, PrimitiveAttribute<?>> attributes = new HashMap<String, PrimitiveAttribute<?>>();
		attributes.put("textAttr", new StringAttribute("manyToOne-1"));
		attributes.put("booleanAttr", new BooleanAttribute(true));
		attributes.put("intAttr", new IntegerAttribute(100));
		attributes.put("floatAttr", new FloatAttribute(100.0f));
		attributes.put("doubleAttr", new DoubleAttribute(100.0));
		
		DateFormat format = new SimpleDateFormat("dd/MM/yyyy");
		Date date;
		try {
			date = format.parse("01/01/2009");
		} catch (ParseException e) {
			date = new Date();
		}
		attributes.put("dateAttr", new DateAttribute(date));
		return new ManyToOneAttribute(new AssociationValue(new LongAttribute(id), attributes));
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
}
