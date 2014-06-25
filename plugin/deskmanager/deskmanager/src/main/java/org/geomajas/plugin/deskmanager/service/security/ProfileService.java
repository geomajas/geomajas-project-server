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
package org.geomajas.plugin.deskmanager.service.security;

import org.geomajas.global.GeomajasException;
import org.geomajas.plugin.deskmanager.domain.security.GroupMember;
import org.geomajas.plugin.deskmanager.domain.security.Territory;
import org.geomajas.plugin.deskmanager.domain.security.User;
import org.geomajas.plugin.deskmanager.domain.security.dto.ProfileDto;
import org.geomajas.plugin.deskmanager.domain.security.dto.Role;

import java.util.List;
import java.util.Map;

/**
 * Service for creating or deleting {@link org.geomajas.plugin.deskmanager.domain.security.GroupMember} object, i.e.
 * linking a {@link User}, a {@link Role} and a
 * {@link org.geomajas.plugin.deskmanager.domain.security.Territory} object.
 *  The type {@link org.geomajas.plugin.deskmanager.domain.security.GroupMember} is also known as a Profile.
 *
 * @author Jan Venstermans
 */
public interface ProfileService {

	/**
	 * Update the profiles (specifically groups-role combination) of a user.
	 * All but the Administrator role profiles will be processed;
	 * profiles of admin roles don't have a group.
	 *
	 * @param userId id of the student
	 * @param addedProfiles list of profiles containing the role and group information
	 *                         of profiles to be added to the user
	 * @param removedProfiles list of profiles containing the role and group information
	 *                         of profiles to be deleted from the user
	 * @throws org.geomajas.global.GeomajasException
	 */
	void updateUserProfileList(long userId, List<ProfileDto> addedProfiles,
							   List<ProfileDto> removedProfiles) throws GeomajasException;

	/**
	 * Update the profiles (specifically user-role combination) related to a group.
	 * All but the Administrator role profiles will be processed;
	 * for adding admin roles to a user, use {@link #updateAdmins(java.util.List, java.util.List)}.
	 *
	 * @param territoryId id of the group
	 * @param addedAssignments map where a key is a userId and
	 *                              a value is a list of roles to be added for that user.
	 * @param removedAssignments map where a key is a userId and
	 *                              a value is a list of roles to be deleted for that user.
	 */
	void updateGroupAssignment(long territoryId, Map<Long, List<Role>> addedAssignments,
							   Map<Long, List<Role>> removedAssignments) throws GeomajasException;

	/**
	 *  Update the list of {@link Role#ADMINISTRATOR} users.
	 *
	 * @param addedAdminUserIds users that have to be added to the administrator list
	 * @param removedAdminsUserIds users that need to be removed from the administrator list
	 * @throws GeomajasException
	 */
	void updateAdmins(List<Long> addedAdminUserIds, List<Long> removedAdminsUserIds) throws GeomajasException;

	/**
	 * Returns the profiles ({@link org.geomajas.plugin.deskmanager.domain.security.GroupMember} instances) where the
	 * group is the one provided as the method argument.
	 *
	 * @param territory group for which the {@link org.geomajas.plugin.deskmanager.domain.security.GroupMember}
	 *                     are searched
	 * @return list of {@link org.geomajas.plugin.deskmanager.domain.security.GroupMember} for the group
	 */
	List<GroupMember> getProfilesOfGroup(Territory territory);

	/**
	 * Returns all users that have a {@link org.geomajas.plugin.deskmanager.domain.security.GroupMember} with
	 * the {@link Role#ADMINISTRATOR} role.
	 *
	 * @return the list of administrator users
	 */
	List<User> getAdminUsers();
}
