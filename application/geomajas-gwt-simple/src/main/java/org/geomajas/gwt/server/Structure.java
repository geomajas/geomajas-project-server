/*
 * This file is part of Geomajas, a component framework for building
 * rich Internet applications (RIA) with sophisticated capabilities for the
 * display, analysis and management of geographic information.
 * It is a building block that allows developers to add maps
 * and other geographic data capabilities to their web applications.
 *
 * Copyright 2008-2010 Geosparc, http://www.geosparc.com, Belgium
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.geomajas.gwt.server;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Type;

import com.vividsolutions.jts.geom.Geometry;

/**
 * Structures object for hibernate layer model.
 * 
 * @author Jan De Moerloose
 *
 */
@Entity
@Table(name = "structures")
public class Structure {

	@Id
	@GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY)
	@Column(name = "gid")
	private Long gid;
	
	private Long id;

	private String type;
	
	private Float area;
	
	private Double perimeter;

	private String image;
	
	private Double cost;
	
	private Boolean forsale;

	@Type(type = "org.hibernatespatial.GeometryUserType")
	@Column(name = "the_geom")
	private Geometry geometry;

	
	public Long getGid() {
		return gid;
	}

	
	public void setGid(Long gid) {
		this.gid = gid;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	public String getType() {
		return type;
	}

	
	public void setType(String type) {
		this.type = type;
	}

	
	public Float getArea() {
		return area;
	}

	
	public void setArea(Float area) {
		this.area = area;
	}

	
	public Double getPerimeter() {
		return perimeter;
	}

	
	public void setPerimeter(Double perimeter) {
		this.perimeter = perimeter;
	}

	
	public String getImage() {
		return image;
	}

	
	public void setImage(String image) {
		this.image = image;
	}

	
	public Double getCost() {
		return cost;
	}

	
	public void setCost(Double cost) {
		this.cost = cost;
	}

	
	public Boolean getForsale() {
		return forsale;
	}

	
	public void setForsale(Boolean forsale) {
		this.forsale = forsale;
	}

	
	public Geometry getGeometry() {
		return geometry;
	}

	
	public void setGeometry(Geometry geometry) {
		this.geometry = geometry;
	}

}
