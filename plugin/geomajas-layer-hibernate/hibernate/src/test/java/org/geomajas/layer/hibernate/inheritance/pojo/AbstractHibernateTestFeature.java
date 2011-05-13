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

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.hibernate.annotations.Type;

import com.vividsolutions.jts.geom.Geometry;

/**
 * Abstract hibernate test feature.
 * 
 * @author Pieter De Graef
 */
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class AbstractHibernateTestFeature {

	public static final String PARAM_TEXT_ATTR = "textAttr";

	public static final String PARAM_MTO_ATTR = "manyToOne";

	public static final String PARAM_OTM_ATTR = "oneToMany";

	public static final String PARAM_GEOMETRY_ATTR = "geometry";

	@Column(name = "textAttr")
	private String textAttr;

	@ManyToOne(cascade = { CascadeType.ALL })
	private ExtendedAttribute manyToOne;

	@OneToMany(cascade = { CascadeType.ALL }, mappedBy = "feature")
	private Set<ExtendedAttribute> oneToMany = new HashSet<ExtendedAttribute>();

	@Type(type = "org.hibernatespatial.GeometryUserType")
	@Column(name = "the_geom")
	private Geometry geometry;

	// Constructors:

	public AbstractHibernateTestFeature() {
	}

	public AbstractHibernateTestFeature(String textAttr) {
		this.textAttr = textAttr;
	}

	// Class specific functions:

	public String toString() {
		return ReflectionToStringBuilder.toString(this);
	}

	// Getters and setters:

	public ExtendedAttribute getManyToOne() {
		return manyToOne;
	}

	public void setManyToOne(ExtendedAttribute manyToOne) {
		this.manyToOne = manyToOne;
	}

	public String getTextAttr() {
		return textAttr;
	}

	public void setTextAttr(String textAttr) {
		this.textAttr = textAttr;
	}

	public Set<ExtendedAttribute> getOneToMany() {
		return oneToMany;
	}

	public void setOneToMany(Set<ExtendedAttribute> oneToMany) {
		this.oneToMany = oneToMany;
	}

	public Geometry getGeometry() {
		return geometry;
	}

	public void setGeometry(Geometry geometry) {
		this.geometry = geometry;
	}
}