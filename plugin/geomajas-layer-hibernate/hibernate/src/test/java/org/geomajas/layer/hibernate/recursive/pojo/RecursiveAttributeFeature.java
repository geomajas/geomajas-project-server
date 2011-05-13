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

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.hibernate.annotations.Type;

import com.vividsolutions.jts.geom.Geometry;

/**
 * A normal Hibernate test feature. Is used an an attribute within another feature type ({@link RecursiveTopFeature}).
 * 
 * @author Pieter De Graef
 */
@Entity
@Table(name = "recursiveAttributeFeature")
public class RecursiveAttributeFeature {

	@Id
	@GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;

	@Column(name = "textAttr")
	private String textAttr;

	@ManyToOne(cascade = { CascadeType.ALL })
	private RecursiveAttribute manyToOne;

	@OneToMany(cascade = { CascadeType.ALL })
	private List<RecursiveAttribute> oneToMany;

	@Type(type = "org.hibernatespatial.GeometryUserType")
	@Column(name = "the_geom")
	private Geometry geometry;

	// Constructors:

	public RecursiveAttributeFeature() {
	}

	public RecursiveAttributeFeature(Long id) {
		this.id = id;
	}

	public RecursiveAttributeFeature(String textAttr) {
		this.textAttr = textAttr;
	}

	public RecursiveAttributeFeature(Long id, String textAttr) {
		this.id = id;
		this.textAttr = textAttr;
	}

	// Class specific functions:

	public String toString() {
		return ReflectionToStringBuilder.toString(this);
	}

	public static RecursiveAttributeFeature getDefaultInstance1(Long id) {
		RecursiveAttributeFeature p = new RecursiveAttributeFeature(id);
		p.setTextAttr("default-name-1");
		p.setManyToOne(RecursiveAttribute.getDefaultInstance1(id + 1));
		List<RecursiveAttribute> oneToMany = new ArrayList<RecursiveAttribute>();
		oneToMany.add(RecursiveAttribute.getDefaultInstance1(id + 1));
		oneToMany.add(RecursiveAttribute.getDefaultInstance3(id + 2));
		p.setOneToMany(oneToMany);
		return p;
	}

	public static RecursiveAttributeFeature getDefaultInstance2(Long id) {
		RecursiveAttributeFeature p = new RecursiveAttributeFeature(id);
		p.setTextAttr("default-name-2");
		p.setManyToOne(RecursiveAttribute.getDefaultInstance2(id));
		List<RecursiveAttribute> oneToMany = new ArrayList<RecursiveAttribute>();
		oneToMany.add(RecursiveAttribute.getDefaultInstance2(id + 1));
		oneToMany.add(RecursiveAttribute.getDefaultInstance4(id + 2));
		p.setOneToMany(oneToMany);
		return p;
	}

	// Getters and setters:

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public RecursiveAttribute getManyToOne() {
		return manyToOne;
	}

	public void setManyToOne(RecursiveAttribute manyToOne) {
		this.manyToOne = manyToOne;
	}

	public String getTextAttr() {
		return textAttr;
	}

	public void setTextAttr(String textAttr) {
		this.textAttr = textAttr;
	}

	public Geometry getGeometry() {
		return geometry;
	}

	public void setGeometry(Geometry geometry) {
		this.geometry = geometry;
	}

	public List<RecursiveAttribute> getOneToMany() {
		return oneToMany;
	}

	public void setOneToMany(List<RecursiveAttribute> oneToMany) {
		this.oneToMany = oneToMany;
	}
}