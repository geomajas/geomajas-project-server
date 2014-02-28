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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.geomajas.configuration.client.ClientLayerInfo;
import org.geomajas.configuration.client.ClientWidgetInfo;
import org.geomajas.global.GeomajasException;
import org.geomajas.plugin.deskmanager.configuration.UserApplicationInfo;
import org.geomajas.plugin.deskmanager.domain.Blueprint;
import org.geomajas.plugin.deskmanager.domain.ClientLayer;
import org.geomajas.plugin.deskmanager.domain.Geodesk;
import org.geomajas.plugin.deskmanager.domain.LayerModel;
import org.geomajas.plugin.deskmanager.domain.dto.BlueprintDto;
import org.geomajas.plugin.deskmanager.domain.dto.LayerDto;
import org.geomajas.plugin.deskmanager.domain.dto.GeodeskDto;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementation for {@link DtoConverterService}.
 * 
 * @author Oliver May
 * 
 */
@Component
@Transactional(readOnly = true)
public class DtoConverterServiceImpl implements DtoConverterService {

	private final Logger log = LoggerFactory.getLogger(DtoConverterServiceImpl.class);

	@Autowired
	private List<UserApplicationInfo> userApplications;

	@Autowired
	private ApplicationContext applicationContext;
	
	@Autowired
	private org.geomajas.service.DtoConverterService converterService;

	// ----------------------------------------------------------
	private static ResourceBundle messages;

	static {
		initMessages();
	}

	private static void initMessages() {
		try {
			messages = ResourceBundle.getBundle("org/geomajas/plugin/deskmanager/i18n/ServiceMessages");

		} catch (MissingResourceException e) {
		}

	}

	public Blueprint fromDto(BlueprintDto dto) throws GeomajasException {
		if (dto == null) {
			return null;
		}
		Blueprint bp = new Blueprint();

		bp.setActive(dto.isActive());
		bp.setCreationBy(dto.getCreationBy());
		bp.setCreationDate(dto.getCreationDate());
		bp.setUserApplicationKey(dto.getUserApplicationKey());
		bp.setDeleted(dto.isDeleted());
		bp.setId(dto.getId());
		bp.setLastEditBy(dto.getLastEditBy());
		bp.setLastEditDate(dto.getLastEditDate());
		bp.setLimitToCreatorTerritory(dto.isLimitToCreatorTerritory());
		bp.setLimitToUserTerritory(dto.isLimitToUserTerritory());
		bp.setGeodesksActive(dto.isGeodesksActive());
		bp.setName(dto.getName());
		bp.setPublic(dto.isPublic());
		bp.setApplicationClientWidgetInfos(dto.getApplicationClientWidgetInfos());
		bp.setMainMapClientWidgetInfos(dto.getMainMapClientWidgetInfos());
		bp.setOverviewMapClientWidgetInfos(dto.getOverviewMapClientWidgetInfos());
		List<Territory> territories = bp.getTerritories();
		if (dto.getTerritories() != null) {
			for (TerritoryDto gDto : dto.getTerritories()) {
				territories.add(fromDto(gDto, false, false));
			}
		}
		List<ClientLayer> mainLayers = bp.getMainMapLayers();
		if (dto.getMainMapLayers() != null) {
			for (LayerDto layer : dto.getMainMapLayers()) {
				mainLayers.add(fromDto(layer));
			}
		}
		List<ClientLayer> overviewLayers = bp.getOverviewMapLayers();
		if (dto.getOverviewMapLayers() != null) {
			for (LayerDto layer : dto.getOverviewMapLayers()) {
				overviewLayers.add(fromDto(layer));
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
		bpDto.setUserApplicationKey(blueprint.getUserApplicationKey());
		bpDto.setDeleted(blueprint.isDeleted());
		bpDto.setId(blueprint.getId());
		bpDto.setLastEditBy(blueprint.getLastEditBy());
		bpDto.setLastEditDate(blueprint.getLastEditDate());
		bpDto.setLimitToLoketTerritory(blueprint.isLimitToCreatorTerritory());
		bpDto.setLimitToUserTerritory(blueprint.isLimitToUserTerritory());
		bpDto.setGeodesksActive(blueprint.isGeodesksActive());
		bpDto.setName(blueprint.getName());
		bpDto.setPublic(blueprint.isPublic());
		bpDto.setApplicationClientWidgetInfos(new HashMap<String, ClientWidgetInfo>(blueprint
				.getApplicationClientWidgetInfos()));
		bpDto.setMainMapClientWidgetInfos(new HashMap<String, ClientWidgetInfo>(blueprint
				.getMainMapClientWidgetInfos()));
		bpDto.setOverviewMapClientWidgetInfos(new HashMap<String, ClientWidgetInfo>(blueprint
				.getOverviewMapClientWidgetInfos()));
		if (blueprint.getMainMapLayers() != null) {
			for (ClientLayer layer : blueprint.getMainMapLayers()) {
				bpDto.getMainMapLayers().add(toDto(layer));
			}
		}
		if (blueprint.getOverviewMapLayers() != null) {
			for (ClientLayer layer : blueprint.getOverviewMapLayers()) {
				bpDto.getOverviewMapLayers().add(toDto(layer));
			}
		}
		if (includeReferences) {
			List<TerritoryDto> territories = bpDto.getTerritories();
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
		lm.setDynamicLayerConfiguration(dto.getLayerConfiguration());
		lm.getWidgetInfo().putAll(dto.getWidgetInfo());
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
		lmDto.setMinScale(layerModel.getMinScale());
		lmDto.setMaxScale(layerModel.getMaxScale());
		lmDto.setReadOnly(layerModel.isReadOnly());
		lmDto.setLayerType(layerModel.getLayerType());
		lmDto.getWidgetInfo().putAll(layerModel.getWidgetInfo());
		String owner;
		if (layerModel.getOwner() == null) {
			owner = getMessage("systemUsr");
		} else {
			owner = layerModel.getOwner().getName();
		}
		if (owner == null) {
			owner = "System";
		}
		lmDto.setOwner(owner);
		if (includeReferences) {
			lmDto.setLayerConfiguration(layerModel.getDynamicLayerConfiguration());
		}
		return lmDto;
	}

	private static String getMessage(String key) {
		return messages.getString(key);
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
		l.setGeodeskId(dto.getGeodeskId());
		l.setLimitToCreatorTerritory(dto.isLimitToCreatorTerritory());
		l.setLimitToUserTerritory(dto.isLimitToUserTerritory());
		l.setName(dto.getName());
		l.setPublic(dto.isPublic());
		l.setOwner(fromDto(dto.getOwner(), false, false));
		List<Territory> territories = l.getTerritories();
		l.setApplicationClientWidgetInfos(dto.getApplicationClientWidgetInfos());
		l.setMainMapClientWidgetInfos(dto.getMainMapClientWidgetInfos());
		l.setOverviewMapClientWidgetInfos(dto.getOverviewMapClientWidgetInfos());
		if (dto.getTerritories() != null && dto.getTerritories().size() > 0) {
			for (TerritoryDto gDto : dto.getTerritories()) {
				territories.add(fromDto(gDto, false, false));
			}
		}
		List<ClientLayer> mainLayers = l.getMainMapLayers();
		if (dto.getMainMapLayers() != null) {
			for (LayerDto layer : dto.getMainMapLayers()) {
				mainLayers.add(fromDto(layer));
			}
		}
		List<ClientLayer> overviewLayers = l.getOverviewMapLayers();
		if (dto.getOverviewMapLayers() != null) {
			for (LayerDto layer : dto.getOverviewMapLayers()) {
				overviewLayers.add(fromDto(layer));
			}
		}
		return l;
	}

	public GeodeskDto toDto(Geodesk geodesk, boolean includeReferences) throws GeomajasException {
		if (geodesk == null) {
			return null;
		}
		GeodeskDto lDto = new GeodeskDto();
		lDto.setActive(geodesk.isActive());
		lDto.setBlueprint(toDto(geodesk.getBlueprint(), includeReferences));
		lDto.setCreationBy(geodesk.getCreationBy());
		lDto.setCreationDate(geodesk.getCreationDate());
		lDto.setId(geodesk.getId());
		lDto.setLastEditBy(geodesk.getLastEditBy());
		lDto.setLastEditDate(geodesk.getLastEditDate());
		lDto.setGeodeskId(geodesk.getGeodeskId());
		lDto.setLimitToLoketTerritory(geodesk.isLimitToCreatorTerritory());
		lDto.setLimitToUserTerritory(geodesk.isLimitToUserTerritory());
		lDto.setName(geodesk.getName());
		lDto.setPublic(geodesk.isPublic());

		lDto.setApplicationClientWidgetInfos(new HashMap<String, ClientWidgetInfo>(geodesk
				.getApplicationClientWidgetInfos()));
		lDto.setMainMapClientWidgetInfos(new HashMap<String, ClientWidgetInfo>(geodesk.getMainMapClientWidgetInfos()));
		lDto.setOverviewMapClientWidgetInfos(new HashMap<String, ClientWidgetInfo>(geodesk
				.getOverviewMapClientWidgetInfos()));
		if (geodesk.getMainMapLayers() != null) {
			for (ClientLayer layer : geodesk.getMainMapLayers()) {
				lDto.getMainMapLayers().add(toDto(layer));
			}
		}
		if (geodesk.getOverviewMapLayers() != null) {
			for (ClientLayer layer : geodesk.getOverviewMapLayers()) {
				lDto.getOverviewMapLayers().add(toDto(layer));
			}
		}

		if (includeReferences) {
			List<TerritoryDto> territories = lDto.getTerritories();
			lDto.setOwner(toDto(geodesk.getOwner(), false, false));
			if (geodesk.getTerritories() != null && geodesk.getTerritories().size() > 0) {
				for (Territory grp : geodesk.getTerritories()) {
					territories.add(toDto(grp, false, false));
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
		gDto.setCrs(territory.getCrs());
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
	
	public TerritoryDto toDto(Territory territory, boolean includeBlueprints,
							  boolean includeGeodesks, boolean includeGeometry)
			throws GeomajasException {
		if (territory == null) {
			return null;
		}
		TerritoryDto gDto = toDto(territory, includeBlueprints, includeGeodesks);
		if (includeGeometry) {
			gDto.setGeometry(converterService.toDto(territory.getGeometry()));
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
		p.setSurname(dto.getSurname());
		p.setTerritory(fromDto(dto.getTerritory(), false, false));
		p.setId(dto.getId());
		p.setRole(dto.getRole());
		p.setFirstName(dto.getName());
		return p;
	}

	public ProfileDto toDto(Profile profile) throws GeomajasException {
		if (profile == null) {
			return null;
		}
		ProfileDto pDto = new ProfileDto();
		pDto.setSurname(profile.getSurname());
		pDto.setTerritory(toDto(profile.getTerritory(), false, false));
		pDto.setId(profile.getId());
		pDto.setRole(profile.getRole());
		pDto.setName(profile.getFirstName());
		return pDto;
	}

	public LayerDto toDto(ClientLayer layer) throws GeomajasException {
		if (layer == null) {
			return null;
		}
		LayerDto dto = new LayerDto();
		dto.setClientLayerIdReference(layer.getLayerModel().getClientLayerId());
		dto.getWidgetInfo().putAll(layer.getWidgetInfo());
		try {
			dto.setReferencedLayerInfo((ClientLayerInfo) applicationContext.getBean(layer.getLayerModel()
					.getClientLayerId()));
		} catch (NoSuchBeanDefinitionException e) {
			log.warn("DeskmanagerClientLayerInfo not found for layer: " + layer.getLayerModel().getClientLayerId()
					+ ", not adding clientLayerinfo. You might need to remove these layers");
		}
		dto.setClientLayerInfo(layer.getClientLayerInfo());
		dto.setLayerModel(toDto(layer.getLayerModel(), false));
		return dto;
	}

	public ClientLayer fromDto(LayerDto dto) throws GeomajasException {
		if (dto == null) {
			return null;
		}
		ClientLayer layer = new ClientLayer();
		layer.setClientLayerInfo(dto.getClientLayerInfo());
		layer.setLayerModel(fromDto(dto.getLayerModel()));
		layer.getWidgetInfo().putAll(dto.getWidgetInfo());
		return layer;
	}

	@Override
	public UserDto toDto(User user, boolean includeProfiles) throws GeomajasException {
		UserDto dto = new UserDto();
		dto.setId(user.getId());
		dto.setEmail(user.getEmail());
		dto.setName(user.getName());
		dto.setSurname(user.getSurname());
		dto.setActive(user.isActive());
		if (includeProfiles) {
			for (GroupMember member : user.getGroups())  {
				dto.getProfiles().add(toProfileDto(member));
			}
		}
		return dto;
	}

	@Override
	public User fromDto(UserDto dto, boolean includeProfiles) throws GeomajasException {
		User user = new User();
		user.setId(dto.getId());
		user.setEmail(dto.getEmail());
		user.setName(dto.getName());
		user.setSurname(dto.getSurname());
		user.setActive(dto.isActive());
		if (includeProfiles) {
			for (ProfileDto profileDto : dto.getProfiles()) {
				user.getGroups().add(fromProfileDto(profileDto, user));
			}
		}
		return user;
	}

	@Override
	public ProfileDto toProfileDto(GroupMember groupMember) throws GeomajasException {
		ProfileDto profileDto = new ProfileDto();
		profileDto.setRole(groupMember.getRole());
		profileDto.setTerritory(toDto(groupMember.getGroup(), false, false, false));
		return profileDto;
	}

	@Override
	public Profile toProfile(GroupMember groupMember) throws GeomajasException {
		Profile profile = new Profile();
		profile.setRole(groupMember.getRole());
		profile.setTerritory(groupMember.getGroup());
		return profile;
	}

	@Override
	public GroupMember fromProfileDto(ProfileDto profileDto, User user) throws GeomajasException {
		Territory group = fromDto(profileDto.getTerritory(), false, false);
		return new GroupMember(user, group, profileDto.getRole());
	}

	@Override
	public List<Long> getIds(List<UserDto> users) {
		List<Long> userIds = new ArrayList<Long>();
		for (UserDto userDto : users) {
			userIds.add(userDto.getId());
		}
		return userIds;
	}
}
