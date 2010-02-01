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
package org.geomajas.layermodel.bean;

import java.util.Date;

/**
 * Java bean for one to many attribute.
 * 
 * @author Jan De Moerloose
 * 
 */
public class OneToManyAttributeBean {

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

}
