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
package org.geomajas.widget.searchandfilter.service;

/**
 * @author Kristof Heirwegh
 */
public class SearchFavouritesSettings {

	private boolean allowAnonymous;
	private boolean anonymousCanEdit;

	public boolean isAllowAnonymous() {
		return allowAnonymous;
	}

	public void setAllowAnonymous(boolean allowAnonymous) {
		this.allowAnonymous = allowAnonymous;
	}

	public boolean isAnonymousCanEdit() {
		return anonymousCanEdit;
	}

	public void setAnonymousCanEdit(boolean anonymousCanEdit) {
		this.anonymousCanEdit = anonymousCanEdit;
	}
}
