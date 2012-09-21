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
package org.geomajas.plugin.deskmanager.service.common;

import org.geomajas.global.GeomajasException;
import org.geomajas.plugin.deskmanager.domain.Blueprint;
import org.geomajas.plugin.deskmanager.domain.Geodesk;
import org.geomajas.plugin.deskmanager.domain.Layer;
import org.geomajas.plugin.deskmanager.domain.LayerModel;
import org.geomajas.plugin.deskmanager.domain.LayerTree;
import org.geomajas.plugin.deskmanager.domain.LayerTreeNode;
import org.geomajas.plugin.deskmanager.domain.LayerView;
import org.geomajas.plugin.deskmanager.domain.MailAddress;
import org.geomajas.plugin.deskmanager.domain.dto.BlueprintDto;
import org.geomajas.plugin.deskmanager.domain.dto.GeodeskDto;
import org.geomajas.plugin.deskmanager.domain.dto.LayerDto;
import org.geomajas.plugin.deskmanager.domain.dto.LayerModelDto;
import org.geomajas.plugin.deskmanager.domain.dto.LayerTreeDto;
import org.geomajas.plugin.deskmanager.domain.dto.LayerTreeNodeDto;
import org.geomajas.plugin.deskmanager.domain.dto.LayerViewDto;
import org.geomajas.plugin.deskmanager.domain.dto.MailAddressDto;
import org.geomajas.plugin.deskmanager.domain.security.TerritoryCategory;
import org.geomajas.plugin.deskmanager.domain.security.Territory;
import org.geomajas.plugin.deskmanager.domain.security.Profile;
import org.geomajas.plugin.deskmanager.domain.security.dto.CategoryDto;
import org.geomajas.plugin.deskmanager.domain.security.dto.TerritoryDto;
import org.geomajas.plugin.deskmanager.domain.security.dto.ProfileDto;

/**
 * Converts between DTOs and server-side objects.
 * 
 * @author Oliver May
 */
public interface DtoConverterService {

	Blueprint fromDto(BlueprintDto dto) throws GeomajasException;

	BlueprintDto toDto(Blueprint blueprint, boolean includeReferences) throws GeomajasException;

	LayerModel fromDto(LayerModelDto dto) throws GeomajasException;

	LayerModelDto toDto(LayerModel layerModel, boolean includeReferences) throws GeomajasException;

	Geodesk fromDto(GeodeskDto dto) throws GeomajasException;

	GeodeskDto toDto(Geodesk loket, boolean includeReferences) throws GeomajasException;

	LayerTree fromDto(LayerTreeDto dto) throws GeomajasException;

	LayerTreeDto toDto(LayerTree layerTree) throws GeomajasException;

	LayerTreeNode fromDto(LayerTreeNodeDto dto) throws GeomajasException;

	LayerTreeNodeDto toDto(LayerTreeNode layerTreeNode) throws GeomajasException;

	LayerView fromDto(LayerViewDto dto) throws GeomajasException;

	LayerViewDto toDto(LayerView layerView) throws GeomajasException;

	Layer fromDto(LayerDto dto) throws GeomajasException;

	LayerDto toDto(Layer layer) throws GeomajasException;

	// ----------------------------------------------------------

	Territory fromDto(TerritoryDto dto, boolean includeBlueprints, boolean includeGeodesks) throws GeomajasException;

	TerritoryDto toDto(Territory territory, boolean includeBlueprints, boolean includeGeodesks)
			throws GeomajasException;

	TerritoryCategory fromDto(CategoryDto dto) throws GeomajasException;

	CategoryDto toDto(TerritoryCategory category) throws GeomajasException;

	Profile fromDto(ProfileDto dto) throws GeomajasException;

	ProfileDto toDto(Profile profile) throws GeomajasException;

	MailAddress fromDto(MailAddressDto dto) throws GeomajasException;

	MailAddressDto toDto(MailAddress mail) throws GeomajasException;

	// ----------------------------------------------------------

}
