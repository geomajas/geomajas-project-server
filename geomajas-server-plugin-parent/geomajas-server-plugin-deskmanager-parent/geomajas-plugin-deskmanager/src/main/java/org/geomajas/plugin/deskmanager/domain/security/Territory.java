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
package org.geomajas.plugin.deskmanager.domain.security;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.geomajas.annotation.Api;
import org.geomajas.plugin.deskmanager.domain.Blueprint;
import org.geomajas.plugin.deskmanager.domain.Geodesk;
import org.hibernate.annotations.Type;

import com.vividsolutions.jts.geom.Geometry;

/**
 * The territory a user operates in. This defines an administrative unit (municipality, country) a user has rights to
 * view and/or edit data in.
 * 
 * @author Oliver May
 * @author Kristof Heirwegh
 * 
 * @since 1.0.0
 */
@Api (allMethods = true)
@Entity
@Table(name = "gdm_territory", uniqueConstraints = { @UniqueConstraint(columnNames = { "code" }) })
public class Territory implements Serializable {

	private static final long serialVersionUID = 100L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	@Column(name = "name", nullable = false)
	private String name;

	@Column(name = "code", nullable = false)
	private String code;

	@Column(name = "crs", nullable = false)
	private String crs;

	@ManyToOne
	@JoinColumn(name = "category_id")
	private TerritoryCategory category;

	@Type(type = "org.hibernatespatial.GeometryUserType")
	@Column(name = "geom")
	private Geometry geometry;

	@ManyToMany(targetEntity = Blueprint.class, cascade = { CascadeType.ALL }, 
			mappedBy = "groups", fetch = FetchType.LAZY)
	private transient List<Blueprint> blueprints = new ArrayList<Blueprint>();

	@ManyToMany(targetEntity = Geodesk.class, cascade = { CascadeType.ALL }, 
			mappedBy = "groups", fetch = FetchType.LAZY)
	private transient List<Geodesk> geodesks = new ArrayList<Geodesk>();

	// ----------------------------------------------------------

	/**
	 * Get the name of the territory.
	 * 
	 * @return the name.
	 */
	public String getName() {
		return name;
	}

	/**
	 * Set the name of the territory.
	 * 
	 * @param name
	 *            the name.
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Get the code of the territory. For example the ISO country code.
	 * 
	 * @return the code.
	 */
	public String getCode() {
		return code;
	}

	/**
	 * Set the code of the territory. For example the ISO country code.
	 * 
	 * @param code
	 *            the code.
	 */
	public void setCode(String code) {
		this.code = code;
	}

	/**
	 * Get the CRS of the territory's geometry, in string format like 'EPSG:900913'.
	 * 
	 * @return the crs.
	 */
	public String getCrs() {
		return crs;
	}

	/**
	 * Set the CRS of the territory's geometry, in string format like 'EPSG:900913'.
	 * 
	 * @param crs
	 */
	public void setCrs(String crs) {
		this.crs = crs;
	}

	/**
	 * Get the optional category on the territory.
	 * 
	 * @return the category.
	 */
	public TerritoryCategory getCategory() {
		return category;
	}

	/**
	 * Set the optional category on the territory.
	 * 
	 * @param category
	 *            the category.
	 */
	public void setCategory(TerritoryCategory category) {
		this.category = category;
	}

	/**
	 * Get the geometry this territory covers.
	 * 
	 * @return the geomerty.
	 */
	public Geometry getGeometry() {
		return geometry;
	}

	/**
	 * Get the geometry this territory covers.
	 * 
	 * @return the geomerty.
	 */
	public void setGeometry(Geometry geometry) {
		this.geometry = geometry;
	}

	/**
	 * Get a list of all the blueprints managed by this territory.
	 * 
	 * @return the list of blueprints.
	 */
	public List<Blueprint> getBlueprints() {
		return blueprints;
	}

	/**
	 * Set a list of all the blueprints managed by this territory.
	 * 
	 * @param blueprints
	 *            the list of blueprints.
	 */
	public void setBlueprints(List<Blueprint> blueprints) {
		this.blueprints = blueprints;
	}

	/**
	 * Get a list of geodesks managed by this territory.
	 * 
	 * @return the list of geodesks.
	 */
	public List<Geodesk> getGeodesks() {
		return geodesks;
	}

	/**
	 * Set a list of geodesks managed by this territory.
	 * 
	 * @param geodesks
	 *            the list of geodesks.
	 */
	public void setGeodesks(List<Geodesk> geodesks) {
		this.geodesks = geodesks;
	}

	/**
	 * Get the identifier of this territory.
	 * 
	 * @return the identifier.
	 */
	public long getId() {
		return id;
	}

	/**
	 * Set the identifier of this territory. Usually auto generated by hibernate.
	 * 
	 * @param id
	 *            the identifier.
	 */
	public void setId(long id) {
		this.id = id;
	}

	// ----------------------------------------------------------

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((code == null) ? 0 : code.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		Territory other = (Territory) obj;
		if (code == null) {
			if (other.code != null) {
				return false;
			}
		} else if (!code.equals(other.code)) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "Territory [id=" + id + ", name=" + name + ", code=" + code + ", category=" + category + "]";
	}
}
