/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2013 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */
package org.geomajas.widget.searchandfilter.search.dto;

import java.io.Serializable;
import java.util.Date;

/**
 * @author Kristof Heirwegh
 */
public class SearchFavourite implements Serializable, Cloneable {

	private static final long serialVersionUID = 100L;
	private Criterion criterion;
	private Long id;
	private String name;
	private String creator;
	private boolean shared;
	private Date lastChange;
	private String lastChangeBy;

	// ----------------------------------------------------------

	public Criterion getCriterion() {
		return criterion;
	}

	public void setCriterion(Criterion criterion) {
		this.criterion = criterion;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	/**
	 * If shared is true, everybody can see this favourite.
	 *
	 * @return true when everybody can see this favourite
	 */
	public boolean isShared() {
		return shared;
	}

	public void setShared(boolean shared) {
		this.shared = shared;
	}

	public Date getLastChange() {
		return lastChange;
	}

	public void setLastChange(Date lastChange) {
		this.lastChange = lastChange;
	}

	public String getLastChangeBy() {
		return lastChangeBy;
	}

	public void setLastChangeBy(String lastChangeBy) {
		this.lastChangeBy = lastChangeBy;
	}

	/** {@inheritDoc} */
	public Object clone() { // NOSONAR super.clone() not supported by GWT
		SearchFavourite newFav = new SearchFavourite();
		newFav.setCreator(creator);
		newFav.setCriterion(criterion);
		newFav.setId(id);
		newFav.setLastChange(lastChange);
		newFav.setLastChangeBy(lastChangeBy);
		newFav.setName(name);
		newFav.setShared(shared);
		return newFav;
	}
}
