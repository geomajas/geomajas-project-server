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
package org.geomajas.plugin.deskmanager.service.security.impl;

import org.geomajas.global.GeomajasException;
import org.geomajas.plugin.deskmanager.client.gwt.common.GdmLayout;
import org.geomajas.plugin.deskmanager.domain.security.GroupMember;
import org.geomajas.plugin.deskmanager.domain.security.Territory;
import org.geomajas.plugin.deskmanager.domain.security.User;
import org.geomajas.plugin.deskmanager.domain.security.dto.ProfileDto;
import org.geomajas.plugin.deskmanager.domain.security.dto.Role;
import org.geomajas.plugin.deskmanager.security.DeskmanagerSecurityContext;
import org.geomajas.plugin.deskmanager.service.common.DtoConverterService;
import org.geomajas.security.GeomajasSecurityException;
import org.geomajas.security.SecurityContext;
import org.geomajas.plugin.deskmanager.service.security.UserService;
import org.hibernate.Hibernate;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Default implementation of {@link org.geomajas.plugin.deskmanager.service.security.ProfileService}.
 *
 * @author Jan Venstermans
 *
 */
@Repository
@Transactional(rollbackFor = { Exception.class })
public class ProfileServiceImpl implements org.geomajas.plugin.deskmanager.service.security.ProfileService {

	@Autowired
	private SessionFactory factory;

	@Autowired
	private DtoConverterService converterService;

	@Autowired
	private UserService userService;

	@Autowired
	private SecurityContext securityContext;

	@Override
	public void updateUserProfileList(long userId, List<ProfileDto> addedProfiles,
									  List<ProfileDto> removedProfiles) throws GeomajasException {
		if (addedProfiles == null || removedProfiles == null) {
			throw new GeomajasException(new IllegalArgumentException(
					"some argument of method updateUserProfileList should are null"));
		}
		User user = (User) factory.getCurrentSession().get(User.class, userId);
		if (user == null) {
			throw new GeomajasException(new IllegalArgumentException("Can't find user for userId: " + userId));
		}
		List<GroupMember> groups = user.getGroups();
		Hibernate.initialize(groups);
		Map<Role, Set<Long>> roleListMap = new HashMap<Role, Set<Long>>();
		for (GroupMember member : groups) {
			// all roles but the Administrator
			if (!Role.ADMINISTRATOR.equals(member.getRole())) {
				if (!roleListMap.containsKey(member.getRole())) {
					roleListMap.put(member.getRole(), new HashSet<Long>());
				}
				roleListMap.get(member.getRole()).add(member.getGroup().getId());
			}
		}
		for (ProfileDto addProfile : addedProfiles) {
			// check if it is not yet in the current profiles
			Role addRole = addProfile.getRole();
			if (!Role.ADMINISTRATOR.equals(addRole)) {
				if (!roleListMap.containsKey(addRole) ||
						!roleListMap.get(addRole).contains(addProfile.getTerritory().getId())) {
					GroupMember addMember = converterService.fromProfileDto(addProfile, user);
					groups.add(addMember);
				}
			}
		}
		for (ProfileDto removeProfile : removedProfiles) {
			// check if it is already in the current profiles
			Role removeRole = removeProfile.getRole();
			if (!Role.ADMINISTRATOR.equals(removeRole)) {
				Long territoryId = removeProfile.getTerritory().getId();
				if (roleListMap.containsKey(removeRole) && roleListMap.get(removeRole).contains(territoryId)) {
					GroupMember removeMember = null;
					for (GroupMember member : groups) {
						if (member.getRole().equals(removeRole) && member.getGroup().getId() == territoryId) {
							removeMember = member;
						}
					}
					if (removeMember != null) {
						groups.remove(removeMember);
					}
				}
			}
		}
		factory.getCurrentSession().saveOrUpdate(user);
	}

	@Override
	public void updateGroupAssignment(long territoryId, Map<Long, List<Role>> addedAssignments,
									  Map<Long, List<Role>> removedAssignments) throws GeomajasException {
		if (addedAssignments == null || removedAssignments == null) {
			throw new GeomajasException(new IllegalArgumentException(
					"some argument of method updateGroupAssignment should are null"));
		}
		Territory territory = (Territory) factory.getCurrentSession().get(Territory.class, territoryId);
		if (territory == null) {
			throw new GeomajasException(new IllegalArgumentException("Can't find group for id: " + territoryId));
		}
		List<User> users = userService.findAll(true);
		Map<Long, User> usersMap = new HashMap<Long, User>();
		for (User user : users) {
			usersMap.put(user.getId(), user);
		}
		//remove first
		for (long userId : removedAssignments.keySet()) {
			User user = usersMap.get(userId);
			if (user != null) {
				for (Role role : removedAssignments.get(userId)) {
					if (!Role.ADMINISTRATOR.equals(role)) {
						GroupMember exisitingGroupMember = getGroupMemberOfUser(user, role, territory);
						if (exisitingGroupMember != null) {
							user.getGroups().remove(exisitingGroupMember);
						}
					}
				}
			}
		}
		//add
		for (long userId : addedAssignments.keySet()) {
			User user = usersMap.get(userId);
			if (user != null) {
				boolean changed = false;
				for (Role role : addedAssignments.get(userId)) {
					if (!Role.ADMINISTRATOR.equals(role)) {
						if (getGroupMemberOfUser(user, role, territory) == null) {
							user.getGroups().add(new GroupMember(user, territory, role));
							changed = true;
						}
					}
				}
				if (changed) {
					factory.getCurrentSession().saveOrUpdate(user);
				}
			}
		}
	}

	@Override
	public void updateAdmins(List<Long> addedAdminUserIds, List<Long> removedAdminsUserIds) throws GeomajasException {
		if (addedAdminUserIds == null || removedAdminsUserIds == null) {
			throw new GeomajasException(new IllegalArgumentException(
					"some argument of method addedAdminUserIds should are null"));
		}
		List<User> users = userService.findAll(true);
		Map<Long, User> usersMap = new HashMap<Long, User>();
		for (User user : users) {
			usersMap.put(user.getId(), user);
		}
		//remove first
		for (long userId : removedAdminsUserIds) {
			User user = usersMap.get(userId);
			if (user != null) {
				GroupMember existingAdminMember = getAdminProfileOfUser(user);
				if (existingAdminMember != null) {
					user.getGroups().remove(existingAdminMember);
					factory.getCurrentSession().saveOrUpdate(user);
				}
			}
		}
		// get the territory of current user: should be an admin user
		Role roleLoggedIn = ((DeskmanagerSecurityContext) securityContext).getRole();
		if (roleLoggedIn != null && roleLoggedIn.equals(Role.ADMINISTRATOR)) {
			Territory defaultAdminTerritory = ((DeskmanagerSecurityContext) securityContext).getTerritory();
			//add
			for (long userId : addedAdminUserIds) {
				User user = usersMap.get(userId);
				if (user != null) {
					GroupMember exisitingAdminMember = getAdminProfileOfUser(user);
					if (exisitingAdminMember == null) {
						user.getGroups().add(new GroupMember(user, defaultAdminTerritory, Role.ADMINISTRATOR));
						factory.getCurrentSession().saveOrUpdate(user);
					}
				}
			}
		} else {
			throw new GeomajasSecurityException(GdmLayout.EXCEPTIONCODE_UNAUTHORIZED);
		}
	}

	@Override
	public List<GroupMember> getProfilesOfGroup(Territory territory) {
		List<GroupMember> members = new ArrayList<GroupMember>();
		List<User> users = userService.findAll(true);
		for (User user : users) {
			for (GroupMember member : user.getGroups()) {
				if (member.getGroup().getId() == territory.getId()) {
					members.add(member);
				}
			}
		}
		return members;
	}

	@Override
	public List<User> getAdminUsers() {
		List<User> admins = new ArrayList<User>();
		for (User user : userService.findAll(true)) {
			if (getAdminProfileOfUser(user) != null) {
				admins.add(user);
			}
		}
		return admins;
	}

	//----------------------------------------
	// private methods
	//----------------------------------------

	private GroupMember getGroupMemberOfUser(User user, Role role, Territory territory) {
		for (GroupMember member : user.getGroups()) {
			if (member.getRole().equals(role) && member.getGroup().getId() == territory.getId()) {
				return member;
			}
		}
		return null;
	}

	/**
	 * Returns the {@link GroupMember} of the user that contains the {@link Role#ADMINISTRATOR} role.
	 * If no {@link Role#ADMINISTRATOR} member, null is returned.
	 *
	 * @param user
	 * @return
	 */
	private GroupMember getAdminProfileOfUser(User user) {
		for (GroupMember member : user.getGroups()) {
			if (member.getRole().equals(Role.ADMINISTRATOR)) {
				return member;
			}
		}
		return null;
	}

}
