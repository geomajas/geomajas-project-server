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
package org.geomajas.plugin.deskmanager.domain.security;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.geomajas.annotation.Api;
import org.geomajas.plugin.deskmanager.domain.security.dto.Role;

/**
 * User information.
 *
 * @author Jan De Moerloose
 * @since 1.15.0
 */
@Api(allMethods = true)
@Entity
@Table(name = "gdm_user", uniqueConstraints = { @UniqueConstraint(columnNames = { "email" }) })
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "email", nullable = false, updatable = false)
	private String email;

	@Column(name = "name", nullable = false)
	private String name;

	@Column(name = "surname", nullable = false)
	private String surname;

	@Column(name = "password", nullable = false)
	private String password;

	@Column(name = "active", nullable = false)
	private boolean active;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "user", orphanRemoval = true)
	private List<GroupMember> groups = new ArrayList<GroupMember>();

	/**
	 * Get the id of the user.
	 *
	 * @return
	 */
	public Long getId() {
		return id;
	}

	/**
	 * Set the id of the user.
	 *
	 * @param id
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * Get the email of the user.
	 *
	 * @return
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * Set the email of the user.
	 *
	 * @param email
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * Get the name of the user.
	 *
	 * @return
	 */
	public String getName() {
		return name;
	}

	/**
	 * Set the name of the user.
	 *
	 * @param name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Get the surname of the user.
	 *
	 * @return
	 */
	public String getSurname() {
		return surname;
	}

	/**
	 * Set the surname of the user.
	 *
	 * @param surname
	 */
	public void setSurname(String surname) {
		this.surname = surname;
	}

	/**
	 * Get the encoded password of the user.
	 *
	 * @return
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * Set the encrypted password of the user.
	 *
	 * @param password
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * Get the activity state of the user.
	 *
	 * @return
	 */
	public boolean isActive() {
		return active;
	}

	/**
	 * Set the activity state of the user.
	 *
	 * @param active
	 */
	public void setActive(boolean active) {
		this.active = active;
	}

	/**
	 * Get the groups of the user.
	 *
	 * @return
	 */
	public List<GroupMember> getGroups() {
		return groups;
	}

	/**
	 * Set the groups of the user.
	 *
	 * @param groups
	 */
	public void setGroups(List<GroupMember> groups) {
		this.groups = groups;
	}

	/**
	 * Find a group/role combination for a specific group name.
	 *
	 * @param name
	 * @return
	 */
	public GroupMember findGroupMemberByNameAndRole(String name, Role role) {
		for (GroupMember group : groups) {
			if (group.getGroup().getName().equals(name) && group.getGroup().equals(role)) {
				return group;
			}
		}
		return null;
	}

	/**
	 * Add user to a group, based on group and role.
	 *
	 * @param group
	 * @param role
	 * @return
	 */
	public GroupMember join(Territory group, Role role) {
		GroupMember member = findGroupMemberByNameAndRole(group.getName(), role);
		if (member == null) {
			member = new GroupMember(this, group, role);
			groups.add(member);
		}
		return member;
	}

	/**
	 * Add user to a group, with a specific role.
	 *
	 * @author Jan De Moerloose
	 */
	public void addToGroupInRole(Territory group, Role role) {
		join(group, role);
	}

	/**
	 * Remove user from a group.
	 *
	 * @param group
	 */
	public void removeFromGroupInRole(Territory group, Role role) {
		GroupMember member = findGroupMemberByNameAndRole(group.getName(), role);
		if (member != null) {
			groups.remove(member);
		}
	}

}
