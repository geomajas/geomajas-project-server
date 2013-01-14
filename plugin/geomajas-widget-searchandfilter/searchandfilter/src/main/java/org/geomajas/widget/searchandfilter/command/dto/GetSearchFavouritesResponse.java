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

import java.util.Collection;

import org.geomajas.command.CommandResponse;
import org.geomajas.widget.searchandfilter.search.dto.SearchFavourite;

/**
 * Response for {@link org.geomajas.widget.searchandfilter.command.searchandfilter.GetSearchFavouritesCommand}.
 * 
 * @author Kristof Heirwegh
 */
public class GetSearchFavouritesResponse extends CommandResponse {

	private static final long serialVersionUID = 100L;

	private Collection<SearchFavourite> privateSearchFavourites;
	private Collection<SearchFavourite> sharedSearchFavourites;

	public Collection<SearchFavourite> getPrivateSearchFavourites() {
		return privateSearchFavourites;
	}

	public void setPrivateSearchFavourites(Collection<SearchFavourite> privateSearchFavourites) {
		this.privateSearchFavourites = privateSearchFavourites;
	}

	public Collection<SearchFavourite> getSharedSearchFavourites() {
		return sharedSearchFavourites;
	}

	public void setSharedSearchFavourites(Collection<SearchFavourite> sharedSearchFavourites) {
		this.sharedSearchFavourites = sharedSearchFavourites;
	}
}
