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
package org.geomajas.plugin.deskmanager.staticsecurity;

import org.geomajas.plugin.deskmanager.domain.security.Profile;
import org.geomajas.plugin.deskmanager.security.ProfileService;
import org.geomajas.plugin.deskmanager.service.common.TerritoryService;
import org.geomajas.service.CacheService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Service that loads and stores static security profiles for the deskmanager plug-in.
 *
 * @author Oliver May
 */
public class StaticSecurityProfileService implements ProfileService, LoginService {


	private List<StaticSecurityUser> staticSecurityUsers;

	@Autowired
	private CacheService cacheService;

	@Autowired
	private TerritoryService territoryService;

	@Override
	public List<Profile> getProfiles(String token) {
		return cacheService.get(StaticSecurityProfileService.class.toString(), token, List.class);
	}

	@Override
	public String login(String username, String password) {
		for (StaticSecurityUser user : staticSecurityUsers) {
			if (user.getUsername().equals(username) && user.getPassword().equals(password)) {
				UUID uuid = UUID.randomUUID();
				cacheService.put(StaticSecurityProfileService.class.toString(), uuid.toString(),
						createProfiles(user));
				return uuid.toString();
			}
		}
		return null;
	}

	@Override
	public void logout(String token) {
		cacheService.remove(StaticSecurityProfileService.class.toString(), token);
	}

	/**
	 * Get a list of static security profiles.
	 *
	 * @return the list of static security profiles.
	 */
	public List<StaticSecurityUser> getStaticSecurityUsers() {
		return staticSecurityUsers;
	}

	/**
	 * Set the list of static security profiles.
	 *
	 * @param staticSecurityUsers
	 */
	public void setStaticSecurityUsers(List<StaticSecurityUser> staticSecurityUsers) {
		this.staticSecurityUsers = staticSecurityUsers;
	}

	private List<Profile> createProfiles(StaticSecurityUser user) {
		List<Profile> profiles = new ArrayList<Profile>(user.getProfiles().size());
		for (StaticSecurityProfile staticSecurityProfile : user.getProfiles()) {
			Profile profile = new Profile();
			profile.setFirstName(user.getName());
			profile.setSurname(user.getSurname());
			profile.setId(user.getName().toString() + staticSecurityProfile.getRole().toString() + staticSecurityProfile
					.getTerritoryCode());
			profile.setTerritory(territoryService.getByCode(staticSecurityProfile.getTerritoryCode()));
			profiles.add(profile);
		}
		return profiles;
	}

}
