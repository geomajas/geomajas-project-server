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

package org.geomajas.sld.client.model;

import org.geomajas.sld.filter.FilterTypeInfo;

/**
 * Can store a rule filter, also one that hasn't been fully specified.
 * 
 * @author An Buyle
 * 
 */
public class ChoiceFilterInfo {

	private static final int FILTER_CHOICE = 0;

	private static final int INCOMPLETE_FILTER_CHOICE = 1;

	private int choiceSelect = -1;

	private FilterTypeInfo filter;

	private IncompleteFilterInfo incompleteFilter;

	/**
	 * Check if Filter is current selection for choice.
	 * 
	 * @return <code>true</code> if selection, <code>false</code> if not
	 */
	public boolean ifFilter() {
		return choiceSelect == FILTER_CHOICE;
	}

	/**
	 * Get the 'Filter' element value.
	 * 
	 * @return value
	 */
	public FilterTypeInfo getFilter() {
		return filter;
	}

	/**
	 * Set the 'Filter' element value.
	 * 
	 * @param filter new filter
	 */
	public void setFilter(FilterTypeInfo filter) {
		choiceSelect = FILTER_CHOICE;
		this.filter = filter;
	}

	/**
	 * Check if IncompleteFilter is current selection for choice.
	 * 
	 * @return <code>true</code> if selection, <code>false</code> if not
	 */
	public boolean ifIncompleteFilter() {
		return choiceSelect == INCOMPLETE_FILTER_CHOICE;
	}

	/**
	 * Get the 'IncompleteFilter' element value.
	 * 
	 * @return value
	 */
	public IncompleteFilterInfo getIncompleteFilter() {
		return incompleteFilter;
	}

	/**
	 * Set the 'IncompleteFilter' element value.
	 * 
	 * @param incompleteFilter
	 */
	public void setIncompleteFilter(IncompleteFilterInfo incompleteFilter) {
		choiceSelect = INCOMPLETE_FILTER_CHOICE;
		this.incompleteFilter = incompleteFilter;
	}
}
