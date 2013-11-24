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
package org.geomajas.plugin.deskmanager.security.role.authorization.configuration;

import java.util.ArrayList;
import java.util.List;

/**
 * Authorization info object for deskmanager. The purpose of this class is to configure authorizations from the
 * deskmanager configuration. They are read by and processed by
 * {@link org.geomajas.plugin.deskmanager.security.role.authorization.DeskmanagerAuthorization}.
 * 
 * <p>
 * Empty lists define indifference. To check if (for example) a command is allowed, it must meat following two
 * conditions:
 * <ul>
 * <li>commandsInclude contains the command (or a wildcard matching the command) <b>AND</b>
 * <li>commandsExclude does not contain the command (or a wildcard matching the command)
 * </ul>
 * </p>
 * So you may configure a limited list of commands that can be executed by only providing commandsInclude and a limited
 * list of commands that may never be executed by provide a .* wildcard in commandsInclude, and put all excluded
 * commands in commandsExclude. <br />
 * Putting a .* wildcard in commandsExclude will always exclude all commands.
 * <p>
 * Example: if you want to create a user that has only access to the command.general.Log command and nothing else, you
 * would define this in commandsInclude, and leave all the rest empty. If you want to create a user that has access to
 * everything except command.general.Log, put .* in commandsInclude and command.general.Log in commandsExclude.
 * </p>
 * 
 * @author Oliver May
 * 
 */
public class DeskmanagerAuthorizationInfo implements Cloneable {

	private List<String> toolsInclude;

	private List<String> toolsExclude;

	private List<String> commandsInclude;

	private List<String> commandsExclude;

	private List<String> visibleLayersInclude;

	private List<String> visibleLayersExclude;

	private List<String> updateAuthorizedLayersInclude;

	private List<String> updateAuthorizedLayersExclude;

	private List<String> createAuthorizedLayersInclude;

	private List<String> createAuthorizedLayersExclude;

	private List<String> deleteAuthorizedLayersInclude;

	private List<String> deleteAuthorizedLayersExclude;

	/**
	 * List of tools that are available.
	 */
	public List<String> getToolsInclude() {
		return toolsInclude;
	}

	/**
	 * List of tools that are available.
	 */
	public void setToolsInclude(List<String> toolsInclude) {
		this.toolsInclude = toolsInclude;
	}

	/**
	 * List of tools that must not be available.
	 */
	public List<String> getToolsExclude() {
		return toolsExclude;
	}

	/**
	 * List of tools that must not be available.
	 */
	public void setToolsExclude(List<String> toolsExclude) {
		this.toolsExclude = toolsExclude;
	}

	/**
	 * List of commands that can be executed.
	 */
	public List<String> getCommandsInclude() {
		return commandsInclude;
	}

	/**
	 * List of commands that can be executed.
	 */
	public void setCommandsInclude(List<String> commandsInclude) {
		this.commandsInclude = commandsInclude;
	}

	/**
	 * List of commands that can not be executed.
	 */
	public List<String> getCommandsExclude() {
		return commandsExclude;
	}

	/**
	 * List of commands that can not be executed.
	 */
	public void setCommandsExclude(List<String> commandsExclude) {
		this.commandsExclude = commandsExclude;
	}

	/**
	 * List of layers that are available, overridden by specific blueprint/geodesk configuration.
	 */
	public List<String> getVisibleLayersInclude() {
		return visibleLayersInclude;
	}

	/**
	 * List of layers that are available, overridden by specific blueprint/geodesk configuration.
	 */
	public void setVisibleLayersInclude(List<String> visibleLayersInclude) {
		this.visibleLayersInclude = visibleLayersInclude;
	}

	/**
	 * List of layers that are never available.
	 */
	public List<String> getVisibleLayersExclude() {
		return visibleLayersExclude;
	}

	/**
	 * List of layers that are never available.
	 */
	public void setVisibleLayersExclude(List<String> visibleLayersExclude) {
		this.visibleLayersExclude = visibleLayersExclude;
	}

	/**
	 * List of layers wherein features are updateable.
	 */
	public List<String> getUpdateAuthorizedLayersInclude() {
		return updateAuthorizedLayersInclude;
	}

	/**
	 * List of layers wherein features are updateable.
	 */
	public void setUpdateAuthorizedLayersInclude(List<String> updateAuthorizedLayersInclude) {
		this.updateAuthorizedLayersInclude = updateAuthorizedLayersInclude;
	}

	/**
	 * List of layers wherein features are never updateable.
	 */
	public List<String> getUpdateAuthorizedLayersExclude() {
		return updateAuthorizedLayersExclude;
	}

	/**
	 * List of layers wherein features are never updateable.
	 */
	public void setUpdateAuthorizedLayersExclude(List<String> updateAuthorizedLayersExclude) {
		this.updateAuthorizedLayersExclude = updateAuthorizedLayersExclude;
	}

	/**
	 * List of layers wherein features are creatable.
	 */
	public List<String> getCreateAuthorizedLayersInclude() {
		return createAuthorizedLayersInclude;
	}

	/**
	 * List of layers wherein features are creatable.
	 */
	public void setCreateAuthorizedLayersInclude(List<String> createAuthorizedLayersInclude) {
		this.createAuthorizedLayersInclude = createAuthorizedLayersInclude;
	}

	/**
	 * List of layers wherein features are never creatable.
	 */
	public List<String> getCreateAuthorizedLayersExclude() {
		return createAuthorizedLayersExclude;
	}

	/**
	 * List of layers wherein features are never creatable.
	 */
	public void setCreateAuthorizedLayersExclude(List<String> createAuthorizedLayersExclude) {
		this.createAuthorizedLayersExclude = createAuthorizedLayersExclude;
	}

	/**
	 * List of layers wherein features are deleteable.
	 */
	public List<String> getDeleteAuthorizedLayersInclude() {
		return deleteAuthorizedLayersInclude;
	}

	/**
	 * List of layers wherein features are deleteable.
	 */
	public void setDeleteAuthorizedLayersInclude(List<String> deleteAuthorizedLayersInclude) {
		this.deleteAuthorizedLayersInclude = deleteAuthorizedLayersInclude;
	}

	/**
	 * List of layers wherein features are never deleteable.
	 */
	public List<String> getDeleteAuthorizedLayersExclude() {
		return deleteAuthorizedLayersExclude;
	}

	/**
	 * List of layers wherein features are never deleteable.
	 */
	public void setDeleteAuthorizedLayersExclude(List<String> deleteAuthorizedLayersExclude) {
		this.deleteAuthorizedLayersExclude = deleteAuthorizedLayersExclude;
	}

	@Override
	public Object clone() {
		DeskmanagerAuthorizationInfo clone = new DeskmanagerAuthorizationInfo();
		if (commandsExclude != null) {
			clone.setCommandsExclude(new ArrayList<String>(commandsExclude));
		}
		if (commandsInclude != null) {
			clone.setCommandsInclude(new ArrayList<String>(commandsInclude));
		}
		if (createAuthorizedLayersExclude != null) {
			clone.setCreateAuthorizedLayersExclude(new ArrayList<String>(createAuthorizedLayersExclude));
		}
		if (createAuthorizedLayersInclude != null) {
			clone.setCreateAuthorizedLayersInclude(new ArrayList<String>(createAuthorizedLayersInclude));
		}
		if (deleteAuthorizedLayersExclude != null) {
			clone.setDeleteAuthorizedLayersExclude(new ArrayList<String>(deleteAuthorizedLayersExclude));
		}
		if (deleteAuthorizedLayersInclude != null) {
			clone.setDeleteAuthorizedLayersInclude(new ArrayList<String>(deleteAuthorizedLayersInclude));
		}
		if (toolsExclude != null) {
			clone.setToolsExclude(new ArrayList<String>(toolsExclude));
		}
		if (toolsInclude != null) {
			clone.setToolsInclude(new ArrayList<String>(toolsInclude));
		}
		if (updateAuthorizedLayersExclude != null) {
			clone.setUpdateAuthorizedLayersExclude(new ArrayList<String>(updateAuthorizedLayersExclude));
		}
		if (updateAuthorizedLayersInclude != null) {
			clone.setUpdateAuthorizedLayersInclude(new ArrayList<String>(updateAuthorizedLayersInclude));
		}
		if (visibleLayersExclude != null) {
			clone.setVisibleLayersExclude(new ArrayList<String>(visibleLayersExclude));
		}
		if (visibleLayersInclude != null) {
			clone.setVisibleLayersInclude(new ArrayList<String>(visibleLayersInclude));
		}

		return clone;
	}

}
