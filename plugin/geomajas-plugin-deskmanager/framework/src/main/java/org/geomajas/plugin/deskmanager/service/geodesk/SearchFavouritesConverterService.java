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
package org.geomajas.plugin.deskmanager.service.geodesk;

import org.geomajas.widget.searchandfilter.search.dto.SearchFavourite;

/**
 * 
 * @author Oliver May
 *
 */
public interface SearchFavouritesConverterService {

	SearchFavourite toDto(org.geomajas.plugin.deskmanager.domain.SearchFavourite internal);

	org.geomajas.plugin.deskmanager.domain.SearchFavourite toInternal(SearchFavourite dto);

}
