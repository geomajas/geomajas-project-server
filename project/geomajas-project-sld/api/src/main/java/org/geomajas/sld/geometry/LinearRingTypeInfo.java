/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2013 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the Apache
 * License, Version 2.0. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */
package org.geomajas.sld.geometry;

import java.io.Serializable;
import java.util.List;

import org.geomajas.annotation.Api;

/**
 * 
 A LinearRing is defined by four or more coordinate tuples, with linear interpolation between them; the first and last
 * coordinates must be coincident.
 * 
 * 
 * Schema fragment(s) for this class:...
 * 
 * <pre>
 * &lt;xs:complexType
 * xmlns:ns="http://www.opengis.net/gml" 
 * 
 * xmlns:xs="http://www.w3.org/2001/XMLSchema" name="LinearRingType">
 *   &lt;xs:complexContent>
 *     &lt;xs:extension base="xs:anyType">
 *       &lt;xs:sequence>
 *         &lt;xs:choice>
 *           &lt;xs:element ref="ns:coord" minOccurs="4" maxOccurs="unbounded"/>
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
public class LinearRingTypeInfo implements Serializable {

	private static final long serialVersionUID = 100;

	private int choiceSelect = -1;

	private static final int COORD_LIST_CHOICE = 0;

	private static final int COORDINATES_CHOICE = 1;

	private List<CoordTypeInfo> coordList;

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
	 * Check if CoordList is current selection for choice.
	 * 
	 * @return <code>true</code> if selection, <code>false</code> if not
	 */
	public boolean ifCoordList() {
		return choiceSelect == COORD_LIST_CHOICE;
	}

	/**
	 * Get the list of 'coord' element items.
	 * 
	 * @return list
	 */
	public List<CoordTypeInfo> getCoordList() {
		return coordList;
	}

	/**
	 * Set the list of 'coord' element items.
	 * 
	 * @param list
	 */
	public void setCoordList(List<CoordTypeInfo> list) {
		setChoiceSelect(COORD_LIST_CHOICE);
		coordList = list;
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
		return "LinearRingTypeInfo(choiceSelect=" + this.choiceSelect + ", coordList=" + this.getCoordList()
				+ ", coordinates=" + this.getCoordinates() + ")";
	}

	/** {@inheritDoc} */
	@java.lang.Override
	@java.lang.SuppressWarnings("all")
	public boolean equals(final java.lang.Object o) {
		if (o == this) {
			return true;
		}
		if (!(o instanceof LinearRingTypeInfo)) {
			return false;
		}
		final LinearRingTypeInfo other = (LinearRingTypeInfo) o;
		if (!other.canEqual((java.lang.Object) this)) {
			return false;
		}
		if (this.choiceSelect != other.choiceSelect) {
			return false;
		}
		if (this.getCoordList() == null ? other.getCoordList() != null : !this.getCoordList().equals(
				(java.lang.Object) other.getCoordList())) {
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
		return other instanceof LinearRingTypeInfo;
	}

	/** {@inheritDoc} */
	@java.lang.Override
	@java.lang.SuppressWarnings("all")
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = result * prime + this.choiceSelect;
		result = result * prime + (this.getCoordList() == null ? 0 : this.getCoordList().hashCode());
		result = result * prime + (this.getCoordinates() == null ? 0 : this.getCoordinates().hashCode());
		return result;
	}
}