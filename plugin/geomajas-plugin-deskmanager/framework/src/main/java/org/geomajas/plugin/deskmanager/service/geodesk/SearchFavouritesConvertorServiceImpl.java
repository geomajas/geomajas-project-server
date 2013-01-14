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
import org.springframework.stereotype.Component;

/**
 * 
 * @author Oliver May
 * 
 */
@Component
public class SearchFavouritesConvertorServiceImpl implements SearchFavouritesConverterService {

	public SearchFavourite toDto(org.geomajas.plugin.deskmanager.domain.SearchFavourite internal) {
		if (internal == null) {
			return null;
		}

		SearchFavourite dto = new SearchFavourite();
		dto.setCreator(internal.getCreator());
		// this is the dto version -- shallow copy
		dto.setCriterion(internal.getCriterion());
		dto.setId(internal.getId());
		dto.setLastChange(internal.getLastChange());
		dto.setLastChangeBy(internal.getLastChangeBy());
		dto.setName(internal.getName());
		dto.setShared(internal.isShared());
		return dto;
	}

	public org.geomajas.plugin.deskmanager.domain.SearchFavourite toInternal(SearchFavourite dto) {
		if (dto == null) {
			return null;
		}

		org.geomajas.plugin.deskmanager.domain.SearchFavourite internal = 
			new org.geomajas.plugin.deskmanager.domain.SearchFavourite();
		internal.setCreator(dto.getCreator());
		internal.setCriterion(dto.getCriterion());
		internal.setId(dto.getId());
		internal.setLastChange(dto.getLastChange());
		internal.setLastChangeBy(dto.getLastChangeBy());
		internal.setName(dto.getName());
		internal.setShared(dto.isShared());
		return internal;
	}
}
