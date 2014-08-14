package org.geomajas.plugin.deskmanager.test.service.security;

import org.geomajas.global.GeomajasException;
import org.geomajas.plugin.deskmanager.command.security.dto.RetrieveRolesRequest;
import org.geomajas.plugin.deskmanager.domain.security.GroupMember;
import org.geomajas.plugin.deskmanager.domain.security.Profile;
import org.geomajas.plugin.deskmanager.domain.security.Territory;
import org.geomajas.plugin.deskmanager.domain.security.User;
import org.geomajas.plugin.deskmanager.domain.security.dto.ProfileDto;
import org.geomajas.plugin.deskmanager.domain.security.dto.Role;
import org.geomajas.plugin.deskmanager.security.DeskmanagerSecurityService;
import org.geomajas.plugin.deskmanager.service.common.DtoConverterService;
import org.geomajas.plugin.deskmanager.service.security.GroupService;
import org.geomajas.plugin.deskmanager.service.security.ProfileService;
import org.geomajas.plugin.deskmanager.service.security.UserService;
import org.geomajas.plugin.deskmanager.test.security.StubProfileService;
import org.geomajas.plugin.deskmanager.test.service.ExampleDatabaseProvisioningServiceImpl;
import org.geomajas.security.GeomajasSecurityException;
import org.geomajas.security.SecurityService;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Test the functions of {@link ProfileService} as implemented by
 * {@link org.geomajas.plugin.deskmanager.service.security.impl.TokenToProfileServiceImpl}.
 *
 * @author Jan Venstermans
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/org/geomajas/spring/geomajasContext.xml",
		"/org/geomajas/plugin/deskmanager/spring/**/*.xml", "/applicationContext.xml" })
@Transactional
public class TokenToProfileServiceImplTest {

	@Autowired
	UserService userService;

	@Autowired
	GroupService groupService;

	@Autowired
	ProfileService profileService;

	@Autowired
	DtoConverterService dtoConverterService;

	@Autowired
	SessionFactory sessionFactory;

	@Autowired
	private SecurityService securityService;

	@Autowired
	private org.geomajas.security.SecurityManager securityManager;

	private Profile adminProfileFromDatabase;

	private User findUserNiko() throws GeomajasSecurityException {
		return userService.findByAddress(ExampleDatabaseProvisioningServiceImpl.USER_NIKO_EMAIL);
	}
	private User findUserAdmin() throws GeomajasSecurityException {
		return userService.findByAddress(ExampleDatabaseProvisioningServiceImpl.USER_ADMIN_EMAIL);
	}

	private List<GroupMember> getCurrentGroups(Long userId) {
		return userService.findGroupsOfUser(userId);
	}

	@PostConstruct
	public void createProfiles() throws GeomajasException {
		User adminUserFromDatabase = userService.findByAddress("admin@admin.com");
		// should have one profile ADMINISTRATOR
		GroupMember adminGroupMember = userService.findGroupsOfUser(adminUserFromDatabase.getId()).get(0);
		adminProfileFromDatabase = dtoConverterService.toProfile(adminGroupMember);
	}

	// ----------------------
	//  updateUserProfileList
	//   Before the start of the test, User with address
	// 	ExampleDatabaseProvisioningServiceImpl.USER_NIKO_EMAIL has one GroupMember:
	// 			Territory with code 'NL' and Role 'CONSULTING_USER'
	// ----------------------

	@Test(expected = GeomajasException.class)
	public void updateUserProfileListNullArgumentsTest() throws GeomajasException {
		// find the user
		profileService.updateUserProfileList(findUserNiko() .getId(), null, null);
	}

	@Test(expected = GeomajasException.class)
	public void updateUserProfileListIncorrectUserIdTest() throws GeomajasException {
		profileService.updateUserProfileList(-5, new ArrayList<ProfileDto>(), new ArrayList<ProfileDto>());
	}

	@Test
	public void updateUserProfileListAddProfileTest() throws GeomajasException {
		User user = findUserNiko();
		Territory group = groupService.findByCode("BE");
		int groupAmountBefore = getCurrentGroups(user.getId()).size();
		List<ProfileDto> addProfiles = new ArrayList<ProfileDto>();
		ProfileDto addProfile = new ProfileDto();
		addProfile.setName("TestProfile");
		addProfile.setRole(Role.CONSULTING_USER);
		addProfile.setTerritory(dtoConverterService.toDto(group, false, false));
		addProfiles.add(addProfile);

		profileService.updateUserProfileList(findUserNiko() .getId(), addProfiles, new ArrayList<ProfileDto>());

		List<GroupMember> groupMembers = getCurrentGroups(user.getId());
		Assert.assertEquals(groupAmountBefore + 1 ,groupMembers.size());
		GroupMember addedProfile = groupMembers.get(groupMembers.size() - 1);
		Assert.assertEquals("BE", dtoConverterService.toDto(addedProfile.getGroup(), false, false).getCode());
		Assert.assertEquals(Role.CONSULTING_USER, addedProfile.getRole());
	}

	@Test
	public void updateUserProfileListRemoveProfileTest() throws GeomajasException {
		User user = findUserNiko();
		int groupAmountBefore = getCurrentGroups(user.getId()).size();
		if (groupAmountBefore < 1) {
			throw new GeomajasException(new Exception("user " + user.getName() + " does not contain a group."));
		}
		GroupMember existingGroupMember = getCurrentGroups(user.getId()).get(0);
		List<ProfileDto> removeProfiles = new ArrayList<ProfileDto>();
		ProfileDto removeProfile = new ProfileDto();
		removeProfile.setName("TestProfile");
		removeProfile.setRole(existingGroupMember.getRole());
		removeProfile.setTerritory(dtoConverterService.toDto(existingGroupMember.getGroup(), false, false));

		removeProfiles.add(removeProfile);
		profileService.updateUserProfileList(user.getId(), new ArrayList<ProfileDto>(), removeProfiles);

		Assert.assertEquals(groupAmountBefore - 1 , getCurrentGroups(user.getId()).size());
	}

	@Test
	public void updateUserProfileListAddAdminProfileIsNotRegisteredTest() throws GeomajasException {
		User user = findUserNiko();
		Territory group = groupService.findByCode("BE");
		int groupAmountBefore = getCurrentGroups(user.getId()).size();
		List<ProfileDto> addProfiles = new ArrayList<ProfileDto>();
		ProfileDto addProfile = new ProfileDto();
		addProfile.setName("TestProfile");
		addProfile.setRole(Role.ADMINISTRATOR);
		addProfile.setTerritory(dtoConverterService.toDto(group, false, false));

		addProfiles.add(addProfile);
		profileService.updateUserProfileList(findUserNiko().getId(), addProfiles, new ArrayList<ProfileDto>());

		Assert.assertEquals(groupAmountBefore , getCurrentGroups(user.getId()).size());
	}

	@Test
	public void updateUserProfileListRemoveAdminProfileIsNotRegisteredTest() throws GeomajasException {
		User user = findUserNiko();
		Territory group = groupService.findByCode("BE");
		int groupAmountBefore = getCurrentGroups(user.getId()).size();
		List<ProfileDto> removeProfiles = new ArrayList<ProfileDto>();
		ProfileDto removeProfile = new ProfileDto();
		removeProfile.setName("TestProfile");
		removeProfile.setRole(Role.ADMINISTRATOR);
		removeProfile.setTerritory(dtoConverterService.toDto(group, false, false));

		removeProfiles.add(removeProfile);
		profileService.updateUserProfileList(findUserNiko() .getId(), new ArrayList<ProfileDto>(), removeProfiles);

		Assert.assertEquals(groupAmountBefore , getCurrentGroups(user.getId()).size());
	}


	@Test
	public void updateUserProfileListAddExistingProfileIsNotRegisteredTest() throws GeomajasException {
		User user = findUserNiko();
		int groupAmountBefore = getCurrentGroups(user.getId()).size();
		if (groupAmountBefore < 1) {
			throw new GeomajasException(new Exception("user " + user.getName() + " does not contain a group."));
		}
		GroupMember existingGroupMember = getCurrentGroups(user.getId()).get(0);
		List<ProfileDto> addProfiles = new ArrayList<ProfileDto>();
		ProfileDto addProfile = new ProfileDto();
		addProfile.setName("TestProfile");
		addProfile.setRole(existingGroupMember.getRole());
		addProfile.setTerritory(dtoConverterService.toDto(existingGroupMember.getGroup(), false, false));

		addProfiles.add(addProfile);
		profileService.updateUserProfileList(user.getId(), addProfiles, new ArrayList<ProfileDto>());

		Assert.assertEquals(groupAmountBefore, getCurrentGroups(user.getId()).size());
	}

	@Test
	public void updateUserProfileListRemoveNonExistingProfileIsNotRregisteredTest() throws GeomajasException {
		User user = findUserNiko();
		int groupAmountBefore = getCurrentGroups(user.getId()).size();
		if (groupAmountBefore < 1) {
			throw new GeomajasException(new Exception("user " + user.getName() + " does not contain a group."));
		}
		Territory group = groupService.findByCode("BE");
		List<ProfileDto> removeProfiles = new ArrayList<ProfileDto>();
		ProfileDto removeProfile = new ProfileDto();
		removeProfile.setName("TestProfile");
		removeProfile.setRole(Role.CONSULTING_USER);
		removeProfile.setTerritory(dtoConverterService.toDto(group, false, false));
		removeProfiles.add(removeProfile);

		profileService.updateUserProfileList(user.getId(), new ArrayList<ProfileDto>(), removeProfiles);

		Assert.assertEquals(groupAmountBefore , getCurrentGroups(user.getId()).size());
	}

	// ----------------------
	//  updateGroupAssignment
	//   Before the start of the test, Territory with code "NL" is linked to one GroupMember:
	// 				'niko haak' with 'Role.CONSULTING_USER'
	// ----------------------

	@Test(expected = GeomajasException.class)
	public void updateGroupAssignmentNullArgumentsTest() throws GeomajasException {
		// find the group
		Territory groupNl = groupService.findByCode("NL");
		profileService.updateGroupAssignment(groupNl.getId(), null, null);
	}

	@Test(expected = GeomajasException.class)
	public void updateGroupAssignmentIncorrectUserIdTest() throws GeomajasException {
		profileService.updateGroupAssignment(-5, new HashMap<Long, List<Role>>(), new HashMap<Long, List<Role>>());
	}

	@Test
	public void updateGroupAssignmentAddProfileTest() throws GeomajasException {
		User user = findUserNiko();
		Territory groupNL = groupService.findByCode("NL");
		// groupNl contains one GroupMember: 'niko haak' as 'Role.CONSULTING_USER'
		int amountProfilesBefore = profileService.getProfilesOfGroup(groupNL).size();
		Map<Long, List<Role>> addMap = new HashMap<Long, List<Role>>();
		addMap.put(user.getId(), Arrays.asList(Role.GUEST));
		profileService.updateGroupAssignment(groupNL.getId(), addMap, new HashMap<Long, List<Role>>());
		groupNL = groupService.findByCode("NL");
		Assert.assertEquals(amountProfilesBefore + 1, profileService.getProfilesOfGroup(groupNL).size());
	}

	@Test
	public void updateGroupAssignmentRemoveProfileTest() throws GeomajasException {
		Territory groupNL = groupService.findByCode("NL");
		// groupNl contains one GroupMember: 'niko haak' as 'Role.CONSULTING_USER'
		List<GroupMember> members = profileService.getProfilesOfGroup(groupNL);
		int amountProfilesBefore = members.size();
		if (amountProfilesBefore < 1) {
			throw new GeomajasException(new Exception("group " + groupNL.getName() + " does not contain profiles."));
		}
		GroupMember member = members.get(0);
		Map<Long, List<Role>> removeMap = new HashMap<Long, List<Role>>();
		removeMap.put(member.getUser().getId(), Arrays.asList(member.getRole()));
		profileService.updateGroupAssignment(groupNL.getId(), new HashMap<Long, List<Role>>(), removeMap);
		groupNL = groupService.findByCode("NL");
		Assert.assertEquals(amountProfilesBefore - 1, profileService.getProfilesOfGroup(groupNL).size());
	}

	@Test
	public void updateGroupAssignmentAddAdminProfileIsNotRegisteredTest() throws GeomajasException {
		User user = findUserNiko();
		Territory groupNL = groupService.findByCode("NL");
		// groupNl contains one GroupMember: 'niko haak' as 'Role.CONSULTING_USER'
		int amountProfilesBefore = profileService.getProfilesOfGroup(groupNL).size();
		Map<Long, List<Role>> addMap = new HashMap<Long, List<Role>>();
		addMap.put(user.getId(), Arrays.asList(Role.ADMINISTRATOR));
		profileService.updateGroupAssignment(groupNL.getId(), addMap, new HashMap<Long, List<Role>>());
		groupNL = groupService.findByCode("NL");
		Assert.assertEquals(amountProfilesBefore, profileService.getProfilesOfGroup(groupNL).size());
	}

	@Test
	public void updateGroupAssignmentRemoveAdminProfileIsNotRegisteredTest() throws GeomajasException {
		Territory groupNL = groupService.findByCode("NL");
		// groupNl contains one GroupMember: 'niko haak' as 'Role.CONSULTING_USER'
		List<GroupMember> members = profileService.getProfilesOfGroup(groupNL);
		int amountProfilesBefore = members.size();
		if (amountProfilesBefore < 1) {
			throw new GeomajasException(new Exception("group " + groupNL.getName() + " does not contain profiles."));
		}
		GroupMember member = members.get(0);
		Map<Long, List<Role>> removeMap = new HashMap<Long, List<Role>>();
		removeMap.put(member.getUser().getId(), Arrays.asList(Role.ADMINISTRATOR));
		profileService.updateGroupAssignment(groupNL.getId(), new HashMap<Long, List<Role>>(), removeMap);
		groupNL = groupService.findByCode("NL");
		Assert.assertEquals(amountProfilesBefore, profileService.getProfilesOfGroup(groupNL).size());
	}

	@Test
	public void updateGroupAssignmentAddExistingProfileIsNotRegisteredTest() throws GeomajasException {
		User user = findUserNiko();
		Territory groupNL = groupService.findByCode("NL");
		// groupNl contains one GroupMember: 'niko haak' as 'Role.CONSULTING_USER'
		int amountProfilesBefore = profileService.getProfilesOfGroup(groupNL).size();
		Map<Long, List<Role>> addMap = new HashMap<Long, List<Role>>();
		addMap.put(user.getId(), Arrays.asList(Role.CONSULTING_USER));
		profileService.updateGroupAssignment(groupNL.getId(), addMap, new HashMap<Long, List<Role>>());
		groupNL = groupService.findByCode("NL");
		Assert.assertEquals(amountProfilesBefore, profileService.getProfilesOfGroup(groupNL).size());
	}

	@Test
	public void updateGroupAssignmentRemoveNonExistingProfileIsNotRegisteredTest() throws GeomajasException {
		Territory groupNL = groupService.findByCode("NL");
		// groupNl contains one GroupMember: 'niko haak' as 'Role.CONSULTING_USER'
		List<GroupMember> members = profileService.getProfilesOfGroup(groupNL);
		int amountProfilesBefore = members.size();
		if (amountProfilesBefore < 1) {
			throw new GeomajasException(new Exception("group " + groupNL.getName() + " does not contain profiles."));
		}
		GroupMember member = members.get(0);
		Map<Long, List<Role>> removeMap = new HashMap<Long, List<Role>>();
		removeMap.put(member.getUser().getId(), Arrays.asList(Role.GUEST));
		profileService.updateGroupAssignment(groupNL.getId(), new HashMap<Long, List<Role>>(), removeMap);
		groupNL = groupService.findByCode("NL");
		Assert.assertEquals(amountProfilesBefore, profileService.getProfilesOfGroup(groupNL).size());
	}

	// ----------------------
	//  updateAdmins
	//  Before the start of the test, there are no admin users
	// ----------------------

	@Test(expected = GeomajasException.class)
	public void updateAdminsNullArgumentsTest() throws GeomajasException {
		profileService.updateAdmins(null, null);
	}

	@Test
	public void updateAdminsAddAdminProfileTest() throws GeomajasException {
		logIn(adminProfileFromDatabase);
		User user = findUserNiko();
		int amountAdminsBefore = profileService.getAdminUsers().size();
		// add the admin role of the user
		profileService.updateAdmins(Arrays.asList(user.getId()), new ArrayList<Long>());
		Assert.assertEquals(amountAdminsBefore + 1,  profileService.getAdminUsers().size());
	}

	@Test
	public void updateAdminsRemoveAdminProfileTest() throws GeomajasException {
		logIn(adminProfileFromDatabase);
		User user = findUserNiko();
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
		User user = findUserNiko();
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
		User user = findUserNiko();
		int amountAdminsBefore = profileService.getAdminUsers().size();
		// remove the admin role of the user (although he hasn't got one)
		profileService.updateAdmins(new ArrayList<Long>(), Arrays.asList(user.getId()));
		Assert.assertEquals(amountAdminsBefore,  profileService.getAdminUsers().size());
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
