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

package mypackage.server;

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
