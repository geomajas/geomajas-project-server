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
import org.geomajas.service.CacheService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.UUID;

/**
 * Service that loads and stores static security profiles for the deskmanager plug-in.
 *
 * @author Oliver May
 */
public class StaticSecurityProfileService implements ProfileService {

	private List<StaticSecurityProfile> staticSecurityProfiles;

	@Autowired
	private CacheService cacheService;

	@Override
	public List<Profile> getProfiles(String token) {
		return cacheService.get(StaticSecurityProfileService.class.toString(), token, List.class);
	}

	public String login(String username, String password) {
		for (StaticSecurityProfile profile : staticSecurityProfiles) {
			if (profile.getUsername().equals(username) && profile.getPassword().equals(password)) {
				UUID uuid = UUID.randomUUID();
				cacheService.put(StaticSecurityProfileService.class.toString(), uuid.toString(), profile.getProfiles());
				return uuid.toString();
			}
		}
		return null;
	}

	public void logout(String token) {
		cacheService.remove(StaticSecurityProfileService.class.toString(), token);
	}

	/**
	 * Get a list of static security profiles.
	 *
	 * @return the list of static security profiles.
	 */
	public List<StaticSecurityProfile> getStaticSecurityProfiles() {
		return staticSecurityProfiles;
	}

	/**
	 * Set the list of static security profiles.
	 *
	 * @param staticSecurityProfiles
	 */
	public void setStaticSecurityProfiles(List<StaticSecurityProfile> staticSecurityProfiles) {
		this.staticSecurityProfiles = staticSecurityProfiles;
	}

}
