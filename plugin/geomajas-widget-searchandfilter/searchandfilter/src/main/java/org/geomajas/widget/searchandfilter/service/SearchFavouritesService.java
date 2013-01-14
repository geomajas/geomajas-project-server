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

import java.io.IOException;
import java.util.Collection;

import org.geomajas.widget.searchandfilter.search.dto.SearchFavourite;

/**
 * Crud operations for SearchFavourites.
 * <p>
 * Note that we are passing dto's, so implementations might need to convert to their own inner types, but also have
 * the freedom of using their own inner types (eg. pojo or hibernate or something else all together).
 * <p>
 * Also note that we are doing no security-checks, so don't forget to do that before calling these methods.
 * @author Kristof Heirwegh
 */
public interface SearchFavouritesService {

	SearchFavourite getSearchFavourite(Long id) throws IOException;

	Collection<SearchFavourite> getPrivateSearchFavourites(String user) throws IOException;

	Collection<SearchFavourite> getSharedSearchFavourites() throws IOException;

	void deleteSearchFavourite(SearchFavourite sf) throws IOException;

	void saveOrUpdateSearchFavourite(SearchFavourite sf) throws IOException;

}
