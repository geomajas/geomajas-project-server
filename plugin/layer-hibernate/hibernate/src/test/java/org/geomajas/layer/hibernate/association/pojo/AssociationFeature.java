/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2016 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */

package org.geomajas.layer.hibernate.association.pojo;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Type;

import com.vividsolutions.jts.geom.Geometry;

/**
 * Hibernate test feature that uses many-to-one and one-to-many association attributes.
 * 
 * @author Pieter De Graef
 */
@Entity
@Table(name = "associationFeature")
public class AssociationFeature {

	@Id
	@GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;

	@ManyToOne(cascade = { CascadeType.ALL })
	private ManyToOneProperty manyToOne;

	@OneToMany(cascade = { CascadeType.ALL }, mappedBy = "feature")
	private Set<OneToManyProperty> oneToMany;

	@Type(type = "org.hibernatespatial.GeometryUserType")
	@Column(name = "the_geom")
	private Geometry geometry;

	// Constructors:

	public AssociationFeature() {
	}

	public AssociationFeature(Long id) {
		this.id = id;
	}

	// Class specific functions:

	public static AssociationFeature getDefaultInstance1(Long id) {
		AssociationFeature p = new AssociationFeature(id);
		if (id == null) {
			p.setManyToOne(ManyToOneProperty.getDefaultInstance1(null));
		} else {
			p.setManyToOne(ManyToOneProperty.getDefaultInstance1(new Long(21)));
			Set<OneToManyProperty> otm = new HashSet<OneToManyProperty>();
			otm.add(OneToManyProperty.getDefaultInstance1(new Long(11), p));
			otm.add(OneToManyProperty.getDefaultInstance3(new Long(12), p));
			p.setOneToMany(otm);
		}
		return p;
	}

	public static AssociationFeature getDefaultInstance2(Long id) {
		AssociationFeature p = new AssociationFeature(id);
		if (id == null) {
			p.setManyToOne(ManyToOneProperty.getDefaultInstance2(null));
		} else {
			p.setManyToOne(ManyToOneProperty.getDefaultInstance2(new Long(22)));
			Set<OneToManyProperty> otm = new HashSet<OneToManyProperty>();
			otm.add(OneToManyProperty.getDefaultInstance2(new Long(13), p));
			otm.add(OneToManyProperty.getDefaultInstance4(new Long(14), p));
			p.setOneToMany(otm);
		}
		return p;
	}

	public static AssociationFeature getDefaultInstance3(Long id) {
		AssociationFeature p = new AssociationFeature(id);
		if (id == null) {
			p.setManyToOne(ManyToOneProperty.getDefaultInstance3(null));
		} else {
			p.setManyToOne(ManyToOneProperty.getDefaultInstance3(new Long(23)));
			Set<OneToManyProperty> otm = new HashSet<OneToManyProperty>();
			otm.add(OneToManyProperty.getDefaultInstance2(new Long(15), p));
			otm.add(OneToManyProperty.getDefaultInstance4(new Long(16), p));
			p.setOneToMany(otm);
		}
		return p;
	}

	public static AssociationFeature getDefaultInstance4(Long id) {
		AssociationFeature p = new AssociationFeature(id);
		if (id == null) {
			p.setManyToOne(ManyToOneProperty.getDefaultInstance4(null));
		} else {
			p.setManyToOne(ManyToOneProperty.getDefaultInstance4(new Long(24)));
			Set<OneToManyProperty> otm = new HashSet<OneToManyProperty>();
			otm.add(OneToManyProperty.getDefaultInstance2(new Long(17), p));
			otm.add(OneToManyProperty.getDefaultInstance4(new Long(18), p));
			p.setOneToMany(otm);
		}
		return p;
	}

	// Getters and setters:

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public ManyToOneProperty getManyToOne() {
		return manyToOne;
	}

	public void setManyToOne(ManyToOneProperty manyToOne) {
		this.manyToOne = manyToOne;
	}

	public Set<OneToManyProperty> getOneToMany() {
		return oneToMany;
	}

	public void setOneToMany(Set<OneToManyProperty> oneToMany) {
		this.oneToMany = oneToMany;
	}

	public Geometry getGeometry() {
		return geometry;
	}

	public void setGeometry(Geometry geometry) {
		this.geometry = geometry;
	}
}