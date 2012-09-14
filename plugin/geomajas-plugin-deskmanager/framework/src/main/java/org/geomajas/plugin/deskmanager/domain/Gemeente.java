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
 * TODO.
 * 
 * @author Jan De Moerloose
 *
 */
@Entity
@Table(name = "layer_gemeentenlayermodel")
public class Gemeente implements Serializable, CsvExport {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "layer_gemeentenlayermodel_fid")
	private Long id;

	@Column(name = "\"SHAPE_AREA\"")
	private Double area;

	@Column(name = "\"SHAPE_LEN\"")
	private double perimeter;

	@Column(name = "\"Naam\"")
	private String name;

	@Column(name = "\"Niscode\"")
	private String niscode;

	@Type(type = "org.hibernatespatial.GeometryUserType")
	@Column(name = "the_geom")
	private Geometry geom;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Double getArea() {
		return area;
	}

	public void setArea(Double area) {
		this.area = area;
	}

	public double getPerimeter() {
		return perimeter;
	}

	public void setPerimeter(double perimeter) {
		this.perimeter = perimeter;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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
