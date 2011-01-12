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
package org.geomajas.layer.bean;

import java.util.List;
import java.util.Date;

/**
 * Java bean feature containing all attribute types. For easy Spring configuration, the geometry can be specified in wkt
 * (Well-Known-Text) format. Samples: <code><pre>
POINT(6 10)
LINESTRING(3 4,10 50,20 25)
POLYGON((1 1,5 1,5 5,1 5,1 1),(2 2, 3 2, 3 3, 2 3,2 2))
MULTIPOINT((3.5 5.6),(4.8 10.5))
MULTILINESTRING((3 4,10 50,20 25),(-5 -8,-10 -8,-15 -4))
MULTIPOLYGON(((1 1,5 1,5 5,1 5,1 1),(2 2, 3 2, 3 3, 2 3,2 2)),((3 3,6 2,6 4,3 3)))
GEOMETRYCOLLECTION(POINT(4 6),LINESTRING(4 6,7 10))
POINT ZM (1 1 5 60)
POINT M (1 1 80)
POINT EMPTY
MULTIPOLYGON EMPTY
</pre></code>
 * 
 * 
 * 
 * @author Jan De Moerloose
 */
public class FeatureBean {

	private Long id;

	private Boolean booleanAttr;

	private String currencyAttr;

	private Date dateAttr;

	private Double doubleAttr;

	private Float floatAttr;

	private String imageUrlAttr;

	private Integer integerAttr;

	private Long longAttr;

	private Short shortAttr;

	private String stringAttr;

	private String urlAttr;

	private ManyToOneAttributeBean manyToOneAttr;

	private List<OneToManyAttributeBean> oneToManyAttr;

	private String geometry;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Boolean getBooleanAttr() {
		return booleanAttr;
	}

	public void setBooleanAttr(Boolean booleanAttr) {
		this.booleanAttr = booleanAttr;
	}

	public String getCurrencyAttr() {
		return currencyAttr;
	}

	public void setCurrencyAttr(String currencyAttr) {
		this.currencyAttr = currencyAttr;
	}

	public Date getDateAttr() {
		return dateAttr;
	}

	public void setDateAttr(Date dateAttr) {
		this.dateAttr = dateAttr;
	}

	public Double getDoubleAttr() {
		return doubleAttr;
	}

	public void setDoubleAttr(Double doubleAttr) {
		this.doubleAttr = doubleAttr;
	}

	public Float getFloatAttr() {
		return floatAttr;
	}

	public void setFloatAttr(Float floatAttr) {
		this.floatAttr = floatAttr;
	}

	public String getImageUrlAttr() {
		return imageUrlAttr;
	}

	public void setImageUrlAttr(String imageUrlAttr) {
		this.imageUrlAttr = imageUrlAttr;
	}

	public Integer getIntegerAttr() {
		return integerAttr;
	}

	public void setIntegerAttr(Integer integerAttr) {
		this.integerAttr = integerAttr;
	}

	public Long getLongAttr() {
		return longAttr;
	}

	public void setLongAttr(Long longAttr) {
		this.longAttr = longAttr;
	}

	public Short getShortAttr() {
		return shortAttr;
	}

	public void setShortAttr(Short shortAttr) {
		this.shortAttr = shortAttr;
	}

	public String getStringAttr() {
		return stringAttr;
	}

	public void setStringAttr(String stringAttr) {
		this.stringAttr = stringAttr;
	}

	public String getUrlAttr() {
		return urlAttr;
	}

	public void setUrlAttr(String urlAttr) {
		this.urlAttr = urlAttr;
	}

	public ManyToOneAttributeBean getManyToOneAttr() {
		return manyToOneAttr;
	}

	public void setManyToOneAttr(ManyToOneAttributeBean manyToOneAttr) {
		this.manyToOneAttr = manyToOneAttr;
	}

	public List<OneToManyAttributeBean> getOneToManyAttr() {
		return oneToManyAttr;
	}

	public void setOneToManyAttr(List<OneToManyAttributeBean> oneToManyAttr) {
		this.oneToManyAttr = oneToManyAttr;
	}

	public String getGeometry() {
		return geometry;
	}

	public void setGeometry(String geometry) {
		this.geometry = geometry;
	}
}
