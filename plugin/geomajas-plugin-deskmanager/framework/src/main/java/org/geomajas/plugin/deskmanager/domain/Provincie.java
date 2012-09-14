/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2012 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */
package org.geomajas.plugin.deskmanager.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.geomajas.plugin.deskmanager.reporting.csv.CsvBuilder;
import org.geomajas.plugin.deskmanager.reporting.csv.CsvExport;
import org.hibernate.annotations.Type;

import com.vividsolutions.jts.geom.Geometry;

/**
 * 
 * @author Oliver May
 *
 */
@Entity
@Table(name = "layer_provincieslayermodel")
public class Provincie implements Serializable, CsvExport {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "layer_provincieslayermodel_fid")
	private Long id;

	@Column(name = "\"MAPKEY\"")
	private Integer mapkey;

	@Column(name = "\"NAAM\"")
	private String name;

	@Column(name = "\"POPULATION\"")
	private Long population;

	@Column(name = "\"AREA\"")
	private Long area;

	@Column(name = "\"CAPITAL\"")
	private String capital;

	@Column(name = "\"DESCRIPTIO\"")
	private String description;

	// FIXME
	private String niscode = "??";

	@Type(type = "org.hibernatespatial.GeometryUserType")
	@Column(name = "the_geom")
	private Geometry geom;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getMapkey() {
		return mapkey;
	}

	public void setMapkey(Integer mapkey) {
		this.mapkey = mapkey;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Long getPopulation() {
		return population;
	}

	public void setPopulation(Long population) {
		this.population = population;
	}

	public Long getArea() {
		return area;
	}

	public void setArea(Long area) {
		this.area = area;
	}

	public String getCapital() {
		return capital;
	}

	public void setCapital(String capital) {
		this.capital = capital;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getNiscode() {
		return niscode;
	}

	public void setNiscode(String niscode) {
		this.niscode = niscode;
	}

	public Geometry getGeom() {
		return geom;
	}

	public void setGeom(Geometry geom) {
		this.geom = geom;
	}

	// ------------------------------------------------------------------

	public void toCsv(CsvBuilder cb) {
		cb.addField(niscode);
		cb.addField(name);
		cb.endRecord();
	}

	public void addHeaderRow(CsvBuilder cb) {
		cb.addField("Niscode");
		cb.addField("Naam");
		cb.endRecord();
	}
}
