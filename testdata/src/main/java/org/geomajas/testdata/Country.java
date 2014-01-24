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
package org.geomajas.testdata;

/**
 * Country bean for bean layer. (see org/geomajas/testdata/layerCountries.xml)
 * 
 * @author Jan De Moerloose
 */
public class Country {

	private Long id;

	private String geometry;

	private String name;

	private String country;

	private String region;

	private Integer opec;

	private Integer unesco;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getGeometry() {
		return geometry;
	}

	public void setGeometry(String geometry) {
		this.geometry = geometry;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getRegion() {
		return region;
	}

	public void setRegion(String region) {
		this.region = region;
	}

	public Integer getOpec() {
		return opec;
	}

	public void setOpec(Integer opec) {
		this.opec = opec;
	}

	public Integer getUnesco() {
		return unesco;
	}

	public void setUnesco(Integer unesco) {
		this.unesco = unesco;
	}

}
