/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2014 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */
package org.geomajas.plugin.deskmanager.service.common;

import org.geomajas.global.GeomajasException;
import org.geomajas.plugin.deskmanager.domain.Blueprint;
import org.geomajas.plugin.deskmanager.domain.ClientLayer;
import org.geomajas.plugin.deskmanager.domain.Geodesk;
import org.geomajas.plugin.deskmanager.domain.LayerModel;
import org.geomajas.plugin.deskmanager.domain.dto.BlueprintDto;
import org.geomajas.plugin.deskmanager.domain.dto.GeodeskDto;
import org.geomajas.plugin.deskmanager.domain.dto.LayerDto;
import org.geomajas.plugin.deskmanager.domain.dto.LayerModelDto;
import org.geomajas.plugin.deskmanager.domain.security.GroupMember;
import org.geomajas.plugin.deskmanager.domain.security.Profile;
import org.geomajas.plugin.deskmanager.domain.security.Territory;
import org.geomajas.plugin.deskmanager.domain.security.TerritoryCategory;
import org.geomajas.plugin.deskmanager.domain.security.User;
import org.geomajas.plugin.deskmanager.domain.security.dto.CategoryDto;
import org.geomajas.plugin.deskmanager.domain.security.dto.ProfileDto;
import org.geomajas.plugin.deskmanager.domain.security.dto.TerritoryDto;
import org.geomajas.plugin.deskmanager.domain.security.dto.UserDto;

import java.util.List;

/**
 * Converts between DTOs and server-side objects.
 * 
 * @author Oliver May
 */
public interface DtoConverterService {

	Blueprint fromDto(BlueprintDto dto) throws GeomajasException;

	BlueprintDto toDto(Blueprint blueprint, boolean includeReferences) throws GeomajasException;

	LayerModel fromDto(LayerModelDto dto) throws GeomajasException;

	LayerModelDto toDto(LayerModel lm, boolean includeReferences)  throws GeomajasException;
	
	Geodesk fromDto(GeodeskDto dto) throws GeomajasException;

	GeodeskDto toDto(Geodesk loket, boolean includeReferences) throws GeomajasException;

	ClientLayer fromDto(LayerDto dto) throws GeomajasException;

	LayerDto toDto(ClientLayer layer) throws GeomajasException;

	// ----------------------------------------------------------

	Territory fromDto(TerritoryDto dto, boolean includeBlueprints, boolean includeGeodesks) throws GeomajasException;

	TerritoryDto toDto(Territory territory, boolean includeBlueprints, boolean includeGeodesks)
			throws GeomajasException;
	
	TerritoryDto toDto(Territory territory, boolean includeBlueprints, boolean includeGeodesks, boolean includeGeometry)
			throws GeomajasException;

	TerritoryCategory fromDto(CategoryDto dto) throws GeomajasException;

	CategoryDto toDto(TerritoryCategory category) throws GeomajasException;

	Profile fromDto(ProfileDto dto) throws GeomajasException;

	ProfileDto toDto(Profile profile) throws GeomajasException;

	User fromDto(UserDto dto, boolean includeProfiles) throws GeomajasException;

	UserDto toDto(User user, boolean includeProfiles) throws GeomajasException;

	ProfileDto toProfileDto(GroupMember groupMember) throws GeomajasException;

	Profile toProfile(GroupMember groupMember) throws GeomajasException;

	GroupMember fromProfileDto(ProfileDto profileDto, User user) throws GeomajasException;

	List<Long> getIds(List<UserDto> users);

	// ----------------------------------------------------------

}
