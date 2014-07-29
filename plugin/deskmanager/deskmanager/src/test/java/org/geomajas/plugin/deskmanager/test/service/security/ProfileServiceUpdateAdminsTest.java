package org.geomajas.plugin.deskmanager.test.service.security;

import org.geomajas.global.GeomajasException;
import org.geomajas.plugin.deskmanager.command.security.dto.RetrieveRolesRequest;
import org.geomajas.plugin.deskmanager.domain.security.GroupMember;
import org.geomajas.plugin.deskmanager.domain.security.Profile;
import org.geomajas.plugin.deskmanager.domain.security.Territory;
import org.geomajas.plugin.deskmanager.domain.security.User;
import org.geomajas.plugin.deskmanager.domain.security.dto.Role;
import org.geomajas.plugin.deskmanager.security.DeskmanagerSecurityContext;
import org.geomajas.plugin.deskmanager.security.DeskmanagerSecurityService;
import org.geomajas.plugin.deskmanager.service.common.DtoConverterService;
import org.geomajas.plugin.deskmanager.service.security.GroupService;
import org.geomajas.plugin.deskmanager.service.security.ProfileService;
import org.geomajas.plugin.deskmanager.service.security.UserService;
import org.geomajas.plugin.deskmanager.test.SecurityContainingTestBase;
import org.geomajas.plugin.deskmanager.test.security.StubProfileService;
import org.geomajas.security.*;
import org.hibernate.SessionFactory;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Tests for {@link ProfileService#updateAdmins(java.util.List, java.util.List)}.
 * These test are separate from other methods of the same class, because login is required.
 * @author Jan Venstermans
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/org/geomajas/spring/geomajasContext.xml",
		"/org/geomajas/plugin/deskmanager/spring/**/*.xml", "/applicationContext.xml" })
@Transactional
public class ProfileServiceUpdateAdminsTest extends SecurityContainingTestBase {

	@Autowired
	UserService userService;

	@Autowired
	GroupService groupService;

	@Autowired
	ProfileService profileService;

	@Autowired
	private org.geomajas.plugin.deskmanager.security.ProfileService profileService2;

	@Autowired
	DtoConverterService dtoConverterService;

	@Autowired
	SessionFactory sessionFactory;

	@Autowired
	private SecurityService securityService;

	@Autowired
	private org.geomajas.security.SecurityManager securityManager;

	@Autowired
	private SecurityContext securityContext;

	private Profile deskManagerProfileFromStubService;
	private Profile adminProfileFromDatabase;

	private User user;

	@PostConstruct
	public void createProfiles() throws GeomajasException {
		StubProfileService pService = (StubProfileService) profileService2;
		deskManagerProfileFromStubService = pService.getProfileByRole(Role.DESK_MANAGER);

		user = userService.findByAddress("niko.haak@gmail.com");
		User adminUserFromDatabase = userService.findByAddress("admin@admin.com");
		// should have one profile ADMINISTRATOR
		GroupMember adminGroupMember = userService.findGroupsOfUser(adminUserFromDatabase.getId()).get(0);
		adminProfileFromDatabase = dtoConverterService.toProfile(adminGroupMember);
	}

	@Test
	public void testLoginLogoutRoleAndTerritory() throws GeomajasException {
		DeskmanagerSecurityContext deskmanagerSecurityContext = (DeskmanagerSecurityContext) securityContext;
		logIn(adminProfileFromDatabase);
		Role roleLoggedIn = deskmanagerSecurityContext.getRole();
		Territory territoryLoggedIn = deskmanagerSecurityContext.getTerritory();
		Assert.assertEquals(adminProfileFromDatabase.getRole(), roleLoggedIn);
		Assert.assertNotNull(territoryLoggedIn);
		Assert.assertEquals(adminProfileFromDatabase.getTerritory(), territoryLoggedIn);

		clearSecurityContext();
		Role roleLoggedOut = deskmanagerSecurityContext.getRole();
		Territory territoryLoggedOut = deskmanagerSecurityContext.getTerritory();
		Assert.assertNull(roleLoggedOut);
		Assert.assertNull(territoryLoggedOut);
	}

	// ----------------------
	//  updateAdmins
	//  Before the start of the test, there is one admin user.
	// ----------------------

	@Test(expected = GeomajasException.class)
	public void updateAdminsNullArgumentsTest() throws GeomajasException {
		profileService.updateAdmins(null, null);
	}

	@Test(expected = GeomajasSecurityException.class)
	public void testUpdateAdminsNotLogged() throws GeomajasException {
		profileService.updateAdmins(Arrays.asList(user.getId()), new ArrayList<Long>());
	}

	@Test(expected = GeomajasSecurityException.class)
	public void testUpdateAdminsLoggedInAsNotAdmin() throws GeomajasException {
		logIn(deskManagerProfileFromStubService);
		profileService.updateAdmins(Arrays.asList(user.getId()), new ArrayList<Long>());
	}

	/* log in with admin user */

	@Test
	public void updateAdminsAddAdminProfileTest() throws GeomajasException {
		logIn(adminProfileFromDatabase);
		int amountAdminsBefore = profileService.getAdminUsers().size();

		// add the admin role of the user
		profileService.updateAdmins(Arrays.asList(user.getId()), new ArrayList<Long>());

		Assert.assertEquals(amountAdminsBefore + 1,  profileService.getAdminUsers().size());
	}

	@Test
	public void updateAdminsRemoveAdminProfileTest() throws GeomajasException {
		logIn(adminProfileFromDatabase);
		// add user first
		profileService.updateAdmins(Arrays.asList(user.getId()), new ArrayList<Long>());
		int amountAdminsBefore = profileService.getAdminUsers().size();

		// remove the admin role of the user
		profileService.updateAdmins(new ArrayList<Long>(), Arrays.asList(user.getId()));

		Assert.assertEquals(amountAdminsBefore - 1,  profileService.getAdminUsers().size());
	}

	@Test
	public void updateAdminsAddExistingAdminProfileIsNotRegisteredTest() throws GeomajasException {
		logIn(adminProfileFromDatabase);
		// add user first
		profileService.updateAdmins(Arrays.asList(user.getId()), new ArrayList<Long>());
		int amountAdminsBefore = profileService.getAdminUsers().size();

		// add the admin role of the user again
		profileService.updateAdmins(Arrays.asList(user.getId()), new ArrayList<Long>());

		Assert.assertEquals(amountAdminsBefore,  profileService.getAdminUsers().size());
	}

	@Test
	public void updateAdminsRemoveExistingAdminProfileIsNotRegisteredTest() throws GeomajasException {
		logIn(adminProfileFromDatabase);
		int amountAdminsBefore = profileService.getAdminUsers().size();

		// remove the admin role of the user (although he hasn't got one)
		profileService.updateAdmins(new ArrayList<Long>(), Arrays.asList(user.getId()));

		Assert.assertEquals(amountAdminsBefore, profileService.getAdminUsers().size());
	}

	/* private methods */
	private void logIn(Profile profile) {
		// register user and create token
		String token = ((DeskmanagerSecurityService) securityService).registerRole(RetrieveRolesRequest.MANAGER_ID,
				profile);
		// log in with token
		securityManager.createSecurityContext(token);
	}
}
