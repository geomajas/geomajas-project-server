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

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.geomajas.annotation.Api;
import org.geomajas.plugin.deskmanager.domain.security.dto.Role;

/**
 * Contains unique combination of a {@link User}, {@link Role} and {@link Territory}.
 *
 * @author Jan De Moerloose
 * @since 1.15.0
 */
@Api(allMethods = true)
@Entity
@Table(name = "gdm_user_group", uniqueConstraints = {
		@UniqueConstraint(columnNames = { "role", "user_id", "group_id" }) })
public class GroupMember {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	@JoinColumn(name = "user_id")
	private User user;

	@ManyToOne
	@JoinColumn(name = "group_id")
	private Territory group;

	@Column(name = "role", nullable = false)
	private String role;

	/**
	 * Default constructor.
	 */
	protected GroupMember() {
	}

	/**
	 * Constructor with all requires fields.
	 *
	 * @param group
	 */
	public GroupMember(User user, Territory group, Role role) {
		setUser(user);
		setGroup(group);
		setRole(role);
	}

	/**
	 * Get the user of this GroupMember.
	 *
	 * @return
	 */
	public User getUser() {
		return user;
	}

	/**
	 * Set the user of this GroupMember.
	 *
	 * @param user
	 */
	public void setUser(User user) {
		this.user = user;
	}

	/**
	 *
	 * Get the group of this GroupMember.
	 *
	 * @return
	 */
	public Territory getGroup() {
		return group;
	}

	/**
	 * Set the group of this GroupMember.
	 *
	 * @param group
	 */
	public void setGroup(Territory group) {
		this.group = group;
	}

	/**
	 * Get the role of this GroupMember.
	 *
	 * @return
	 */
	public Role getRole() {
		return Role.fromKey(role);
	}

	/**
	 * Set the role of this GroupMember.
	 *
	 * @param role
	 */
	public void setRole(Role role) {
		this.role = role.getKey();
	}

	@Override
	/**
	 * Equality is based on the restriction unique user/group/role combination.
	 */
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}

		GroupMember that = (GroupMember) o;

		if (that.role == null || !role.equals(that.role)) {
			return false;
		}
		if (that.group == null || group.getId() != that.group.getId()) {
			return false;
		}
		if (that.user == null || user.getId() != that.user.getId()) {
			return false;
		}

		return true;
	}

	@Override
	/**
	 * Hashing is based on the restriction unique user/group/role combination.
	 */
	public int hashCode() {
		int result = user.getId().hashCode();
		result = 31 * result + ((Long) group.getId()).hashCode();
		result = 31 * result + role.hashCode();
		return result;
	}

	/**
	 * Checks if content is same as provided arguments.
	 * @param user
	 * @param group
	 * @param role
	 * @return
	 */
	public boolean contains(User user, Territory group, Role role) {
		if (!this.role.equals(role)) {
		   return false;
		}
		if (this.group.getId() != group.getId()) {
			return false;
		}
		if (this.user.getId() != user.getId()) {
			return false;
		}
		return true;
	}
}
