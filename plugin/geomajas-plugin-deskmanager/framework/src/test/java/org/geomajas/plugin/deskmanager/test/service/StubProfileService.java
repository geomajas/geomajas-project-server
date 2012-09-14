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
package org.geomajas.plugin.deskmanager.test.service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.geomajas.plugin.deskmanager.domain.security.Profile;
import org.geomajas.plugin.deskmanager.domain.security.dto.Role;
import org.geomajas.plugin.deskmanager.security.ProfileService;
import org.geomajas.plugin.deskmanager.security.DeskmanagerSecurityService;
import org.geomajas.plugin.deskmanager.service.common.GroupService;
import org.springframework.beans.factory.annotation.Autowired;


/**
 * FIXME: move to example package.
 * @author Oliver May
 *
 */
public class StubProfileService implements ProfileService {

	
	@Autowired
	private GroupService groupService;
	
	public List<Profile> getProfiles() {
		ArrayList<Profile> profiles = new ArrayList<Profile>();
		
		
		Profile admin = new Profile();
		admin.setRole(Role.ADMINISTRATOR);
		admin.setIdmId(UUID.randomUUID().toString());
		admin.setVoornaam("Peter");
		admin.setFamilienaam("The Administrator");
		admin.setTerritory(groupService.getByCode("ADMIN"));
		//profile.setLoketten();
		profiles.add(admin);

		Profile deskManager = new Profile();
		deskManager.setRole(Role.DESK_MANAGER);
		deskManager.setIdmId(UUID.randomUUID().toString());
		deskManager.setVoornaam("Lois");
		deskManager.setFamilienaam("The Deskmanager");
		deskManager.setTerritory(groupService.getByCode("BE"));
		//profile.setLoketten();
		profiles.add(deskManager);
		
		Profile deskConsulter = new Profile();
		deskConsulter.setRole(Role.CONSULTING_USER);
		deskConsulter.setIdmId(UUID.randomUUID().toString());
		deskConsulter.setVoornaam("Stewart");
		deskConsulter.setFamilienaam("The Consulter");
		deskConsulter.setTerritory(groupService.getByCode("BE"));
		//profile.setLoketten();
		profiles.add(deskConsulter);
		
		Profile deskEditor = new Profile();
		deskEditor.setRole(Role.EDITING_USER);
		deskEditor.setIdmId(UUID.randomUUID().toString());
		deskEditor.setVoornaam("Meg");
		deskEditor.setFamilienaam("The Editor");
		deskEditor.setTerritory(groupService.getByCode("BE"));
		//profile.setLoketten();
		profiles.add(deskEditor);
		
		profiles.add(DeskmanagerSecurityService.createGastProfile());
		
		return profiles;
	}

}
