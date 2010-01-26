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
package org.geomajas.configuration;

import java.io.Serializable;

/**
 * Style configuration information.
 *
 * @author Joachim Van der Auwera
 */
public class StyleInfo
		implements Serializable {

	private static final long serialVersionUID = 151L;
	private long id;
	private String name;
	private String formula;
	private String fillColor;
	private float fillOpacity = -1;
	private String strokeColor;
	private float strokeOpacity = -1;
	private int strokeWidth = -1;
	private String dashArray;
	private SymbolInfo symbol;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getFormula() {
		return formula;
	}

	public void setFormula(String formula) {
		this.formula = formula;
	}

	public String getFillColor() {
		return fillColor;
	}

	public void setFillColor(String fillColor) {
		this.fillColor = fillColor;
	}

	public float getFillOpacity() {
		return fillOpacity;
	}

	public void setFillOpacity(float fillOpacity) {
		this.fillOpacity = fillOpacity;
	}

	public String getStrokeColor() {
		return strokeColor;
	}

	public void setStrokeColor(String strokeColor) {
		this.strokeColor = strokeColor;
	}

	public float getStrokeOpacity() {
		return strokeOpacity;
	}

	public void setStrokeOpacity(float strokeOpacity) {
		this.strokeOpacity = strokeOpacity;
	}

	public int getStrokeWidth() {
		return strokeWidth;
	}

	public void setStrokeWidth(int strokeWidth) {
		this.strokeWidth = strokeWidth;
	}

	public String getDashArray() {
		return dashArray;
	}

	public void setDashArray(String dashArray) {
		this.dashArray = dashArray;
	}

	public SymbolInfo getSymbol() {
		return symbol;
	}

	public void setSymbol(SymbolInfo symbol) {
		this.symbol = symbol;
	}
}
