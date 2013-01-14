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
package org.geomajas.widget.searchandfilter.command.dto;

import org.geomajas.command.CommandRequest;
import org.geomajas.widget.searchandfilter.search.dto.SearchFavourite;

/**
 * Request for {@link org.geomajas.widget.searchandfilter.command.searchandfilter.SaveSearchFavouriteCommand}.
 * 
 * @author Kristof Heirwegh
 */
public class SaveSearchFavouriteRequest implements CommandRequest {

	private static final long serialVersionUID = 100L;

	public static final String COMMAND = "command.searchandfilter.SaveSearchFavourite";

	private SearchFavourite searchFavourite;

	public SearchFavourite getSearchFavourite() {
		return searchFavourite;
	}

	public void setSearchFavourite(SearchFavourite searchFavourite) {
		this.searchFavourite = searchFavourite;
	}

}
