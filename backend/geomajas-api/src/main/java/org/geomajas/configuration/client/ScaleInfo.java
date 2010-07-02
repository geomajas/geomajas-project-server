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
package org.geomajas.configuration.client;

import java.io.Serializable;

import org.geomajas.global.ResolutionFormat;

/**
 * Represents a single scale. This class is needed to support 1:x notation because annotation-based formatters only
 * support fields (no lists) and custom bean editors only support classes.
 * 
 * @author Jan De Moerloose
 * @since 1.7.0
 * 
 */
public class ScaleInfo implements Serializable {

	private static final long serialVersionUID = 170L;

	@ResolutionFormat
	private double value;

	/**
	 * A maximum scale which is suitable for all purposes but avoids infinity.
	 */
	public static final ScaleInfo MAX_VALUE = new ScaleInfo(10E50);

	/**
	 * The minimum scale which is suitable for all purposes but avoids 0 in calculations.
	 */
	public static final ScaleInfo MIN_VALUE = new ScaleInfo(10E-50);

	/**
	 * Default constructor for GWT serialization.
	 */
	public ScaleInfo() {
	}

	/**
	 * Constructs a scale info object with the specified scale value.
	 * 
	 * @param value
	 *            the scale value
	 */
	public ScaleInfo(double value) {
		this.value = value;
	}

	/**
	 * Returns the scale value.
	 * 
	 * @return the scale value
	 */
	public double getValue() {
		return value;
	}

	/**
	 * Sets the scale value.
	 * 
	 * @param value
	 *            the scale value
	 */
	public void setValue(double value) {
		this.value = value;
	}

}
