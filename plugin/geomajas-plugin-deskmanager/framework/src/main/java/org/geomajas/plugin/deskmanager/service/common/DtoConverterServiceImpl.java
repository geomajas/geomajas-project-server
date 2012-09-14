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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.geomajas.configuration.client.ClientWidgetInfo;
import org.geomajas.global.GeomajasException;
import org.geomajas.plugin.deskmanager.configuration.UserApplicationInfo;
import org.geomajas.plugin.deskmanager.domain.Blueprint;
import org.geomajas.plugin.deskmanager.domain.Geodesk;
import org.geomajas.plugin.deskmanager.domain.LayerModel;
import org.geomajas.plugin.deskmanager.domain.LayerTree;
import org.geomajas.plugin.deskmanager.domain.LayerTreeNode;
import org.geomajas.plugin.deskmanager.domain.LayerView;
import org.geomajas.plugin.deskmanager.domain.MailAddress;
import org.geomajas.plugin.deskmanager.domain.dto.BlueprintDto;
import org.geomajas.plugin.deskmanager.domain.dto.GeodeskDto;
import org.geomajas.plugin.deskmanager.domain.dto.LayerModelDto;
import org.geomajas.plugin.deskmanager.domain.dto.LayerTreeDto;
import org.geomajas.plugin.deskmanager.domain.dto.LayerTreeNodeDto;
import org.geomajas.plugin.deskmanager.domain.dto.LayerViewDto;
import org.geomajas.plugin.deskmanager.domain.dto.MailAddressDto;
import org.geomajas.plugin.deskmanager.domain.security.Profile;
import org.geomajas.plugin.deskmanager.domain.security.Territory;
import org.geomajas.plugin.deskmanager.domain.security.TerritoryCategory;
import org.geomajas.plugin.deskmanager.domain.security.dto.CategoryDto;
import org.geomajas.plugin.deskmanager.domain.security.dto.ProfileDto;
import org.geomajas.plugin.deskmanager.domain.security.dto.TerritoryDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * TODO.
 * 
 * @author Jan De Moerloose
 * 
 */
@Component
public class DtoConverterServiceImpl implements DtoConverterService {

	@Autowired
	private List<UserApplicationInfo> userApplications;

	// ----------------------------------------------------------

	public Blueprint fromDto(BlueprintDto dto) throws GeomajasException {
		if (dto == null) {
			return null;
		}
		Blueprint bp = new Blueprint();

		bp.setActive(dto.isActive());
		bp.setCreationBy(dto.getCreationBy());
		bp.setCreationDate(dto.getCreationDate());
		bp.setUserApplicationKey(dto.getUserApplicationName());
		bp.setDeleted(dto.isDeleted());
		bp.setId(dto.getId());
		bp.setLastEditBy(dto.getLastEditBy());
		bp.setLastEditDate(dto.getLastEditDate());
		bp.setLayerTree(fromDto(dto.getLayerTree()));
		bp.setLimitToLoketTerritory(dto.isLimitToCreatorTerritory());
		bp.setLimitToUserTerritory(dto.isLimitToUserTerritory());
		bp.setLokettenActive(dto.isLokettenActive());
		bp.setName(dto.getName());
		bp.setPublic(dto.isPublic());
		bp.setApplicationClientWidgetInfos(dto.getApplicationClientWidgetInfos());
		bp.setMainMapClientWidgetInfos(dto.getMainMapClientWidgetInfos());
		bp.setOverviewMapClientWidgetInfos(dto.getOverviewMapClientWidgetInfos());
		List<Territory> territories = bp.getTerritories();
		if (dto.getTerritories() != null && dto.getTerritories().size() > 0) {
			for (TerritoryDto gDto : dto.getTerritories()) {
				territories.add(fromDto(gDto, false, false));
			}
		}
		return bp;
	}

	public BlueprintDto toDto(Blueprint blueprint, boolean includeReferences) throws GeomajasException {
		if (blueprint == null) {
			return null;
		}
		BlueprintDto bpDto = new BlueprintDto();

		bpDto.setActive(blueprint.isActive());
		bpDto.setCreationBy(blueprint.getCreationBy());
		bpDto.setCreationDate(blueprint.getCreationDate());
		bpDto.setUserApplicationName(blueprint.getUserApplicationKey());
		bpDto.setDeleted(blueprint.isDeleted());
		bpDto.setId(blueprint.getId());
		bpDto.setLastEditBy(blueprint.getLastEditBy());
		bpDto.setLastEditDate(blueprint.getLastEditDate());
		bpDto.setLimitToLoketTerritory(blueprint.isLimitToCreatorTerritory());
		bpDto.setLimitToUserTerritory(blueprint.isLimitToUserTerritory());
		bpDto.setLokettenActive(blueprint.isLokettenActive());
		bpDto.setName(blueprint.getName());
		bpDto.setPublic(blueprint.isPublic());
		bpDto.setApplicationClientWidgetInfos(new HashMap<String, ClientWidgetInfo>(blueprint
				.getApplicationClientWidgetInfos()));
		bpDto.setMainMapClientWidgetInfos(new HashMap<String, ClientWidgetInfo>(blueprint.
				getMainMapClientWidgetInfos()));
		bpDto.setOverviewMapClientWidgetInfos(new HashMap<String, ClientWidgetInfo>(blueprint
				.getOverviewMapClientWidgetInfos()));
		if (includeReferences) {
			List<TerritoryDto> territories = bpDto.getTerritories();
			bpDto.setLayerTree(toDto(blueprint.getLayerTree()));
			if (blueprint.getTerritories() != null && blueprint.getTerritories().size() > 0) {
				for (Territory grp : blueprint.getTerritories()) {
					territories.add(toDto(grp, false, false));
				}
			}
			bpDto.setUserApplicationInfo(getUserApplicationInfo(blueprint.getUserApplicationKey()));
		}
		return bpDto;
	}

	private UserApplicationInfo getUserApplicationInfo(String id) {
		for (UserApplicationInfo ua : userApplications) {
			if (id.equals(ua.getKey())) {
				return ua;
			}
		}
		return null;
	}

	public LayerModel fromDto(LayerModelDto dto) throws GeomajasException {
		if (dto == null) {
			return null;
		}
		LayerModel lm = new LayerModel();

		lm.setActive(dto.isActive());
		lm.setClientLayerId(dto.getClientLayerId());
		lm.setId(dto.getId());
		lm.setName(dto.getName());
		lm.setPublic(dto.isPublic());
		lm.setLayerType(dto.getLayerType());
		lm.setReadOnly(dto.isReadOnly());
		lm.setLayerConfiguration(dto.getLayerConfiguration());
		List<MailAddress> mails = lm.getMailAddresses();
		if (dto.getMailAddresses() != null && dto.getMailAddresses().size() > 0) {
			for (MailAddressDto mad : dto.getMailAddresses()) {
				mails.add(fromDto(mad));
			}
		}
		return lm;
	}

	public LayerModelDto toDto(LayerModel layerModel, boolean includeReferences) throws GeomajasException {
		if (layerModel == null) {
			return null;
		}
		LayerModelDto lmDto = new LayerModelDto();

		lmDto.setActive(layerModel.isActive());
		lmDto.setClientLayerId(layerModel.getClientLayerId());
		lmDto.setId(layerModel.getId());
		lmDto.setName(layerModel.getName());
		lmDto.setPublic(layerModel.isPublic());
		lmDto.setDefaultVisible(layerModel.isDefaultVisible());
		lmDto.setShowInLegend(layerModel.isShowInLegend());
		lmDto.setMinScale(layerModel.getMinScale());
		lmDto.setMaxScale(layerModel.getMaxScale());
		lmDto.setReadOnly(layerModel.isReadOnly());
		lmDto.setLayerType(layerModel.getLayerType());
		lmDto.setOwner((layerModel.getOwner() == null ? "Systeem" : layerModel.getOwner().getName()));
		if (includeReferences) {
			lmDto.setLayerConfiguration(layerModel.getLayerConfiguration());
			List<MailAddressDto> mails = lmDto.getMailAddresses();
			if (layerModel.getMailAddresses() != null && layerModel.getMailAddresses().size() > 0) {
				for (MailAddress ma : layerModel.getMailAddresses()) {
					mails.add(toDto(ma));
				}
			}
		}
		return lmDto;
	}

	public LayerTree fromDto(LayerTreeDto dto) throws GeomajasException {
		if (dto == null) {
			return null;
		}
		LayerTree lt = new LayerTree();

		lt.setId(dto.getId());
		lt.setRootNode(fromDto(dto.getRootNode()));
		return lt;
	}

	public LayerTreeDto toDto(LayerTree layerTree) throws GeomajasException {
		if (layerTree == null) {
			return null;
		}
		LayerTreeDto ltDto = new LayerTreeDto();

		ltDto.setId(layerTree.getId());
		ltDto.setRootNode(toDto(layerTree.getRootNode()));
		return ltDto;
	}

	/**
	 * NamedStyleInfo is not cloned but referenced !!
	 */
	public LayerTreeNode fromDto(LayerTreeNodeDto dto) throws GeomajasException {
		return fromDto(dto, null);
	}

	private LayerTreeNode fromDto(LayerTreeNodeDto dto, Map<LayerTreeNodeDto, LayerTreeNode> done)
			throws GeomajasException {
		if (dto == null) {
			return null;
		}
		if (done == null) {
			done = new HashMap<LayerTreeNodeDto, LayerTreeNode>();
		} else {
			if (done.containsKey(dto)) {
				return done.get(dto);
			}
		}
		LayerTreeNode ltn = new LayerTreeNode();
		done.put(dto, ltn);

		ltn.setExpanded(dto.isExpanded());
		ltn.setId(dto.getId());
		ltn.setName(dto.getNodeName());
		ltn.setLeaf(dto.isLeaf());
		ltn.setClientLayerId(dto.getClientLayerId());
		ltn.setParentNode(fromDto(dto.getParentNode(), done));
		ltn.setStyleUuid(dto.getStyleUuid());
		ltn.setPublicLayer(dto.isPublicLayer());
		ltn.setView(fromDto(dto.getView()));
		List<LayerTreeNode> children = ltn.getChildren();
		if (dto.getChildren() != null && dto.getChildren().size() > 0) {
			for (LayerTreeNodeDto ltnDto : dto.getChildren()) {
				children.add(fromDto(ltnDto, done));
			}
		}

		return ltn;
	}

	public LayerTreeNodeDto toDto(LayerTreeNode layerTreeNode) throws GeomajasException {
		return toDto(layerTreeNode, null);
	}

	private LayerTreeNodeDto toDto(LayerTreeNode layerTreeNode, Map<LayerTreeNode, LayerTreeNodeDto> done)
			throws GeomajasException {
		if (layerTreeNode == null) {
			return null;
		}
		if (done == null) {
			done = new HashMap<LayerTreeNode, LayerTreeNodeDto>();
		} else {
			if (done.containsKey(layerTreeNode)) {
				return done.get(layerTreeNode);
			}
		}
		LayerTreeNodeDto ltnDto = new LayerTreeNodeDto();
		done.put(layerTreeNode, ltnDto);

		ltnDto.setExpanded(layerTreeNode.isExpanded());
		ltnDto.setId(layerTreeNode.getId());
		ltnDto.setName(layerTreeNode.getNodeName());
		ltnDto.setLeaf(layerTreeNode.isLeaf());
		ltnDto.setParentNode(toDto(layerTreeNode.getParentNode(), done));
		ltnDto.setStyleUuid(layerTreeNode.getStyleUuid());
		ltnDto.setPublicLayer(layerTreeNode.isPublicLayer());
		ltnDto.setView(toDto(layerTreeNode.getView()));
		ltnDto.setClientLayerId(layerTreeNode.getClientLayerId());
		List<LayerTreeNodeDto> children = ltnDto.getChildren();
		if (layerTreeNode.getChildren() != null && layerTreeNode.getChildren().size() > 0) {
			for (LayerTreeNode ltn : layerTreeNode.getChildren()) {
				children.add(toDto(ltn, done));
			}
		}

		return ltnDto;
	}

	/**
	 * Scales are not cloned but referenced !!
	 */
	public LayerView fromDto(LayerViewDto dto) throws GeomajasException {
		if (dto == null) {
			return null;
		}
		LayerView lv = new LayerView();

		lv.setDefaultVisible(dto.isDefaultVisible());
		lv.setId(dto.getId());
		lv.setLabel(dto.getLabel());
		lv.setMaximumScale(dto.getMaximumScale());
		lv.setMinimumScale(dto.getMinimumScale());
		lv.setShowInLegend(dto.isShowInLegend());
		return lv;
	}

	public LayerViewDto toDto(LayerView layerView) throws GeomajasException {
		if (layerView == null) {
			return null;
		}
		LayerViewDto lvDto = new LayerViewDto();

		lvDto.setDefaultVisible(layerView.isDefaultVisible());
		lvDto.setId(layerView.getId());
		lvDto.setLabel(layerView.getLabel());
		lvDto.setMaximumScale(layerView.getMaximumScale());
		lvDto.setMinimumScale(layerView.getMinimumScale());
		lvDto.setShowInLegend(layerView.isShowInLegend());
		return lvDto;
	}

	public Geodesk fromDto(GeodeskDto dto) throws GeomajasException {
		if (dto == null) {
			return null;
		}
		Geodesk l = new Geodesk();
		l.setActive(dto.isActive());
		l.setBlueprint(fromDto(dto.getBlueprint()));
		l.setCreationBy(dto.getCreationBy());
		l.setCreationDate(dto.getCreationDate());
		l.setId(dto.getId());
		l.setLastEditBy(dto.getLastEditBy());
		l.setLastEditDate(dto.getLastEditDate());
		l.setLayerTree(fromDto(dto.getLayerTree()));
		l.setGeodeskId(dto.getLoketId());
		l.setLimitToCreatorTerritory(dto.isLimitToCreatorTerritory());
		l.setLimitToUserTerritory(dto.isLimitToUserTerritory());
		l.setName(dto.getName());
		l.setPublic(dto.isPublic());
		l.setOwner(fromDto(dto.getOwner(), false, false));
		List<Territory> territories = l.getTerritories();
		List<MailAddress> mails = l.getMailAddresses();
		l.setApplicationClientWidgetInfos(dto.getApplicationClientWidgetInfos());
		l.setMainMapClientWidgetInfos(dto.getMainMapClientWidgetInfos());
		l.setOverviewMapClientWidgetInfos(dto.getOverviewMapClientWidgetInfos());
		if (dto.getTerritories() != null && dto.getTerritories().size() > 0) {
			for (TerritoryDto gDto : dto.getTerritories()) {
				territories.add(fromDto(gDto, false, false));
			}
		}
		if (dto.getMailAddresses() != null && dto.getMailAddresses().size() > 0) {
			for (MailAddressDto mad : dto.getMailAddresses()) {
				mails.add(fromDto(mad));
			}
		}
		return l;
	}

	public GeodeskDto toDto(Geodesk loket, boolean includeReferences) throws GeomajasException {
		if (loket == null) {
			return null;
		}
		GeodeskDto lDto = new GeodeskDto();
		lDto.setActive(loket.isActive());
		lDto.setBlueprint(toDto(loket.getBlueprint(), includeReferences));
		lDto.setCreationBy(loket.getCreationBy());
		lDto.setCreationDate(loket.getCreationDate());
		lDto.setId(loket.getId());
		lDto.setLastEditBy(loket.getLastEditBy());
		lDto.setLastEditDate(loket.getLastEditDate());
		lDto.setLoketId(loket.getGeodeskId());
		lDto.setLimitToLoketTerritory(loket.isLimitToCreatorTerritory());
		lDto.setLimitToUserTerritory(loket.isLimitToUserTerritory());
		lDto.setName(loket.getName());
		lDto.setPublic(loket.isPublic());

		lDto.setApplicationClientWidgetInfos(new HashMap<String, ClientWidgetInfo>(loket
				.getApplicationClientWidgetInfos()));
		lDto.setMainMapClientWidgetInfos(new HashMap<String, ClientWidgetInfo>(loket.getMainMapClientWidgetInfos()));
		lDto.setOverviewMapClientWidgetInfos(new HashMap<String, ClientWidgetInfo>(loket
				.getOverviewMapClientWidgetInfos()));

		if (includeReferences) {
			List<TerritoryDto> territories = lDto.getTerritories();
			List<MailAddressDto> mails = lDto.getMailAddresses();
			lDto.setLayerTree(toDto(loket.getLayerTree()));
			lDto.setOwner(toDto(loket.getOwner(), false, false));
			if (loket.getTerritories() != null && loket.getTerritories().size() > 0) {
				for (Territory grp : loket.getTerritories()) {
					territories.add(toDto(grp, false, false));
				}
			}
			if (loket.getMailAddresses() != null && loket.getMailAddresses().size() > 0) {
				for (MailAddress ma : loket.getMailAddresses()) {
					mails.add(toDto(ma));
				}
			}
		}

		return lDto;
	}

	public Territory fromDto(TerritoryDto dto, boolean includeBlueprints, boolean includegeodesks)
			throws GeomajasException {
		if (dto == null) {
			return null;
		}
		Territory g = new Territory();
		g.setCategory(fromDto(dto.getCategory()));
		g.setCode(dto.getCode());
		g.setId(dto.getId());
		g.setName(dto.getName());

		List<Blueprint> bps = g.getBlueprints();
		if (includeBlueprints && dto.getBlueprints() != null && dto.getBlueprints().size() > 0) {
			for (BlueprintDto bpDto : dto.getBlueprints()) {
				bps.add(fromDto(bpDto));
			}
		}

		List<Geodesk> gds = g.getGeodesks();
		if (includegeodesks && dto.getGeodesks() != null && dto.getGeodesks().size() > 0) {
			for (GeodeskDto gdDto : dto.getGeodesks()) {
				gds.add(fromDto(gdDto));
			}
		}

		return g;
	}

	public TerritoryDto toDto(Territory territory, boolean includeBlueprints, boolean includeGeodesks)
			throws GeomajasException {
		if (territory == null) {
			return null;
		}
		TerritoryDto gDto = new TerritoryDto();
		gDto.setCategory(toDto(territory.getCategory()));
		gDto.setCode(territory.getCode());
		gDto.setId(territory.getId());
		gDto.setName(territory.getName());

		List<BlueprintDto> bpsDto = gDto.getBlueprints();
		if (includeBlueprints && territory.getBlueprints() != null && territory.getBlueprints().size() > 0) {
			for (Blueprint bp : territory.getBlueprints()) {
				bpsDto.add(toDto(bp, false));
			}
		}

		List<GeodeskDto> gdDto = gDto.getGeodesks();
		if (includeGeodesks && territory.getGeodesks() != null && territory.getGeodesks().size() > 0) {
			for (Geodesk gd : territory.getGeodesks()) {
				gdDto.add(toDto(gd, false));
			}
		}
		return gDto;
	}

	public TerritoryCategory fromDto(CategoryDto dto) throws GeomajasException {
		if (dto == null) {
			return null;
		}
		TerritoryCategory c = new TerritoryCategory();
		c.setDescription(dto.getDescription());
		c.setCategoryType(dto.getCategoryType());
		c.setId(dto.getId());
		return c;
	}

	public CategoryDto toDto(TerritoryCategory category) throws GeomajasException {
		if (category == null) {
			return null;
		}
		CategoryDto cDto = new CategoryDto();
		cDto.setDescription(category.getDescription());
		cDto.setCategoryType(category.getCategoryType());
		cDto.setId(category.getId());
		return cDto;
	}

	public Profile fromDto(ProfileDto dto) throws GeomajasException {
		if (dto == null) {
			return null;
		}
		Profile p = new Profile();
		p.setFamilienaam(dto.getFamilienaam());
		p.setTerritory(fromDto(dto.getTerritory(), false, false));
		p.setIdmId(dto.getIdmId());
		p.setRole(dto.getRole());
		p.setVoornaam(dto.getVoornaam());
		return p;
	}

	public ProfileDto toDto(Profile profile) throws GeomajasException {
		if (profile == null) {
			return null;
		}
		ProfileDto pDto = new ProfileDto();
		pDto.setFamilienaam(profile.getSurname());
		pDto.setTerritory(toDto(profile.getTerritory(), false, false));
		pDto.setIdmId(profile.getIdmId());
		pDto.setRole(profile.getRole());
		pDto.setVoornaam(profile.getFirstName());
		return pDto;
	}

	public MailAddress fromDto(MailAddressDto dto) throws GeomajasException {
		if (dto == null) {
			return null;
		}
		MailAddress mail = new MailAddress();
		mail.setEmail(dto.getEmail());
		mail.setId(dto.getId());
		mail.setName(dto.getName());
		return mail;
	}

	public MailAddressDto toDto(MailAddress mail) throws GeomajasException {
		if (mail == null) {
			return null;
		}
		MailAddressDto dto = new MailAddressDto();
		dto.setEmail(mail.getEmail());
		dto.setId(mail.getId());
		dto.setName(mail.getName());
		return dto;
	}

}
