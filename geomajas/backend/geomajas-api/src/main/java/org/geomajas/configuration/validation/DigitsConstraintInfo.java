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
package org.geomajas.configuration.validation;

/**
 * The value of the constrained number attribute must have a maximum number of integer and/or fractional digits.
 * 
 * @author Jan De Moerloose
 */
public class DigitsConstraintInfo implements ConstraintInfo {

	private int integer;

	private int fractional;

	/**
	 * Return the maximum number of integer digits.
	 * 
	 * @return the maximum allowed number of integer digits
	 */
	public int getInteger() {
		return integer;
	}

	/**
	 * Set the maximum number of integer digits.
	 * 
	 * @param integer
	 *            the maximum allowed number of integer digits
	 */
	public void setInteger(int integer) {
		this.integer = integer;
	}

	/**
	 * Return the maximum number of fractional digits.
	 * 
	 * @return the maximum allowed number of fractional digits
	 */
	public int getFractional() {
		return fractional;
	}

	/**
	 * Set the maximum number of fractional digits.
	 * 
	 * @param fractional
	 *            the maximum allowed number of fractional digits
	 */
	public void setFractional(int fractional) {
		this.fractional = fractional;
	}
}
