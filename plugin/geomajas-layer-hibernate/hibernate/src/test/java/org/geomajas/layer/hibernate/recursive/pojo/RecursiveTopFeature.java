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

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

/**
 * Hibernate test feature that is used in testing recursive attribute notations in the configuration.
 * 
 * @author Pieter De Graef
 */
@Entity
@Table(name = "recursiveTopFeature")
public class RecursiveTopFeature {

	public static final String PARAM_MTO_ATTR = "manyToOne";

	@Id
	@GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;

	@ManyToOne(cascade = { CascadeType.ALL })
	private RecursiveAttributeFeature manyToOne;

	// Constructors:

	public RecursiveTopFeature() {
	}

	public RecursiveTopFeature(Long id) {
		this.id = id;
	}

	public RecursiveTopFeature(Long id, RecursiveAttributeFeature manyToOne) {
		this.id = id;
		this.manyToOne = manyToOne;
	}

	// Class specific functions:

	public String toString() {
		return ReflectionToStringBuilder.toString(this);
	}

	public static RecursiveTopFeature getDefaultInstance1(Long id) {
		RecursiveTopFeature p = new RecursiveTopFeature(id);
		p.setManyToOne(RecursiveAttributeFeature.getDefaultInstance1(id));
		return p;
	}

	public static RecursiveTopFeature getDefaultInstance2(Long id) {
		RecursiveTopFeature p = new RecursiveTopFeature(id);
		p.setManyToOne(RecursiveAttributeFeature.getDefaultInstance2(id));
		return p;
	}

	// Getters and setters:

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public RecursiveAttributeFeature getManyToOne() {
		return manyToOne;
	}

	public void setManyToOne(RecursiveAttributeFeature manyToOne) {
		this.manyToOne = manyToOne;
	}
}