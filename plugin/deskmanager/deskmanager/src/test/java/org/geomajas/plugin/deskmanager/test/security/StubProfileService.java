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
package org.geomajas.plugin.deskmanager.test.security;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.geomajas.plugin.deskmanager.domain.security.Profile;
import org.geomajas.plugin.deskmanager.domain.security.dto.Role;
import org.geomajas.plugin.deskmanager.security.ProfileService;
import org.geomajas.plugin.deskmanager.security.DeskmanagerSecurityService;
import org.geomajas.plugin.deskmanager.service.common.TerritoryService;
import org.springframework.beans.factory.annotation.Autowired;


/**
 * @author Oliver May
 *
 */
public class StubProfileService implements ProfileService {

	
	@Autowired
	private TerritoryService groupService;
	
	public List<Profile> getProfiles(String token) {
		ArrayList<Profile> profiles = new ArrayList<Profile>();
		

		Profile admin = new Profile();
		admin.setRole(Role.ADMINISTRATOR);
		admin.setId(UUID.randomUUID().toString());
		admin.setFirstName("Peter");
		admin.setSurname("The Administrator");
		admin.setTerritory(groupService.getByCode("ADMIN"));
		profiles.add(admin);

		Profile deskManager = new Profile();
		deskManager.setRole(Role.DESK_MANAGER);
		deskManager.setId(UUID.randomUUID().toString());
		deskManager.setFirstName("Lois");
		deskManager.setSurname("The Deskmanager");
		deskManager.setTerritory(groupService.getByCode("BE"));
		profiles.add(deskManager);
		
		Profile deskConsulter = new Profile();
		deskConsulter.setRole(Role.CONSULTING_USER);
		deskConsulter.setId(UUID.randomUUID().toString());
		deskConsulter.setFirstName("Stewart");
		deskConsulter.setSurname("The Consulter");
		deskConsulter.setTerritory(groupService.getByCode("BE"));
		profiles.add(deskConsulter);
		
		Profile deskEditor = new Profile();
		deskEditor.setRole(Role.EDITING_USER);
		deskEditor.setId(UUID.randomUUID().toString());
		deskEditor.setFirstName("Meg");
		deskEditor.setSurname("The Editor");
		deskEditor.setTerritory(groupService.getByCode("BE"));
		profiles.add(deskEditor);
		
		profiles.add(DeskmanagerSecurityService.createGuestProfile());
		
		return profiles;
	}
	
	/**
	 * Helper method to retrieve a profile for a specific role.
	 * @param role
	 * @return the profile
	 */
	public Profile getProfileByRole(Role role) {
		for (Profile p : getProfiles(null)) {
			if (p.getRole().equals(role)) {
				return p;
			}
		}
		return null;
	}
}