/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2012 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the Apache
 * License, Version 2.0. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */
package org.geomajas.sld.geometry;

import java.io.Serializable;
import org.geomajas.annotation.Api;

/**
 * 
 A Point is defined by a single coordinate tuple.
 * 
 * 
 * Schema fragment(s) for this class:...
 * 
 * <pre>
 * &lt;xs:complexType
 * xmlns:ns="http://www.opengis.net/gml"
 * xmlns:xs="http://www.w3.org/2001/XMLSchema" name="PointType">
 *   &lt;xs:complexContent>
 *     &lt;xs:extension base="xs:anyType">
 *       &lt;xs:sequence>
 *         &lt;xs:choice>
 *           &lt;xs:element ref="ns:coord"/>
 *           &lt;xs:element ref="ns:coordinates"/>
 *         &lt;/xs:choice>
 *       &lt;/xs:sequence>
 *     &lt;/xs:extension>
 *   &lt;/xs:complexContent>
 * &lt;/xs:complexType>
 * </pre>
 * 
 * @author Jan De Moerloose
 * @since 1.0.0
 */

@Api(allMethods = true)
public class PointTypeInfo extends AbstractGeometryInfo implements Serializable {

	private static final long serialVersionUID = 100;

	private int choiceSelect = -1;

	private static final int COORD_CHOICE = 0;

	private static final int COORDINATES_CHOICE = 1;

	private CoordTypeInfo coord;

	private CoordinatesTypeInfo coordinates;

	private void setChoiceSelect(int choice) {
		if (choiceSelect == -1) {
			choiceSelect = choice;
		} else if (choiceSelect != choice) {
			throw new IllegalStateException("Need to call clearChoiceSelect() before changing existing choice");
		}
	}

	/**
	 * Clear the choice selection.
	 */
	public void clearChoiceSelect() {
		choiceSelect = -1;
	}

	/**
	 * Check if Coord is current selection for choice.
	 * 
	 * @return <code>true</code> if selection, <code>false</code> if not
	 */
	public boolean ifCoord() {
		return choiceSelect == COORD_CHOICE;
	}

	/**
	 * Get the 'coord' element value.
	 * 
	 * @return value
	 */
	public CoordTypeInfo getCoord() {
		return coord;
	}

	/**
	 * Set the 'coord' element value.
	 * 
	 * @param coord
	 */
	public void setCoord(CoordTypeInfo coord) {
		setChoiceSelect(COORD_CHOICE);
		this.coord = coord;
	}

	/**
	 * Check if Coordinates is current selection for choice.
	 * 
	 * @return <code>true</code> if selection, <code>false</code> if not
	 */
	public boolean ifCoordinates() {
		return choiceSelect == COORDINATES_CHOICE;
	}

	/**
	 * Get the 'coordinates' element value.
	 * 
	 * @return value
	 */
	public CoordinatesTypeInfo getCoordinates() {
		return coordinates;
	}

	/**
	 * Set the 'coordinates' element value.
	 * 
	 * @param coordinates
	 */
	public void setCoordinates(CoordinatesTypeInfo coordinates) {
		setChoiceSelect(COORDINATES_CHOICE);
		this.coordinates = coordinates;
	}

	/** {@inheritDoc} */
	@java.lang.Override
	@java.lang.SuppressWarnings("all")
	public java.lang.String toString() {
		return "PointTypeInfo(choiceSelect=" + this.choiceSelect + ", coord=" + this.getCoord() + ", coordinates="
				+ this.getCoordinates() + ")";
	}

	/** {@inheritDoc} */
	@java.lang.Override
	@java.lang.SuppressWarnings("all")
	public boolean equals(final java.lang.Object o) {
		if (o == this) {
			return true;
		}
		if (!(o instanceof PointTypeInfo)) {
			return false;
		}
		final PointTypeInfo other = (PointTypeInfo) o;
		if (!other.canEqual((java.lang.Object) this)) {
			return false;
		}
		if (!super.equals(o)) {
			return false;
		}
		if (this.choiceSelect != other.choiceSelect) {
			return false;
		}
		if (this.getCoord() == null ? other.getCoord() != null : !this.getCoord().equals(
				(java.lang.Object) other.getCoord())) {
			return false;
		}
		if (this.getCoordinates() == null ? other.getCoordinates() != null : !this.getCoordinates().equals(
				(java.lang.Object) other.getCoordinates())) {
			return false;
		}
		return true;
	}

	/** {@inheritDoc} */
	@java.lang.SuppressWarnings("all")
	public boolean canEqual(final java.lang.Object other) {
		return other instanceof PointTypeInfo;
	}

	/** {@inheritDoc} */
	@java.lang.Override
	@java.lang.SuppressWarnings("all")
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = result * prime + super.hashCode();
		result = result * prime + this.choiceSelect;
		result = result * prime + (this.getCoord() == null ? 0 : this.getCoord().hashCode());
		result = result * prime + (this.getCoordinates() == null ? 0 : this.getCoordinates().hashCode());
		return result;
	}
}