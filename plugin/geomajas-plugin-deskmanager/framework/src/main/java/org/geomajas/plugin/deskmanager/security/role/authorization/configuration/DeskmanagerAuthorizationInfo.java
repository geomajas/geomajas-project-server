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
package org.geomajas.plugin.deskmanager.security.role.authorization.configuration;

import java.util.ArrayList;
import java.util.List;

/**
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

	public List<String> getToolsInclude() {
		return toolsInclude;
	}

	public void setToolsInclude(List<String> toolsInclude) {
		this.toolsInclude = toolsInclude;
	}

	public List<String> getToolsExclude() {
		return toolsExclude;
	}

	public void setToolsExclude(List<String> toolsExclude) {
		this.toolsExclude = toolsExclude;
	}

	public List<String> getCommandsInclude() {
		return commandsInclude;
	}

	public void setCommandsInclude(List<String> commandsInclude) {
		this.commandsInclude = commandsInclude;
	}

	public List<String> getCommandsExclude() {
		return commandsExclude;
	}

	public void setCommandsExclude(List<String> commandsExclude) {
		this.commandsExclude = commandsExclude;
	}

	public List<String> getVisibleLayersInclude() {
		return visibleLayersInclude;
	}

	public void setVisibleLayersInclude(List<String> visibleLayersInclude) {
		this.visibleLayersInclude = visibleLayersInclude;
	}

	public List<String> getVisibleLayersExclude() {
		return visibleLayersExclude;
	}

	public void setVisibleLayersExclude(List<String> visibleLayersExclude) {
		this.visibleLayersExclude = visibleLayersExclude;
	}

	public List<String> getUpdateAuthorizedLayersInclude() {
		return updateAuthorizedLayersInclude;
	}

	public void setUpdateAuthorizedLayersInclude(List<String> updateAuthorizedLayersInclude) {
		this.updateAuthorizedLayersInclude = updateAuthorizedLayersInclude;
	}

	public List<String> getUpdateAuthorizedLayersExclude() {
		return updateAuthorizedLayersExclude;
	}

	public void setUpdateAuthorizedLayersExclude(List<String> updateAuthorizedLayersExclude) {
		this.updateAuthorizedLayersExclude = updateAuthorizedLayersExclude;
	}

	public List<String> getCreateAuthorizedLayersInclude() {
		return createAuthorizedLayersInclude;
	}

	public void setCreateAuthorizedLayersInclude(List<String> createAuthorizedLayersInclude) {
		this.createAuthorizedLayersInclude = createAuthorizedLayersInclude;
	}

	public List<String> getCreateAuthorizedLayersExclude() {
		return createAuthorizedLayersExclude;
	}

	public void setCreateAuthorizedLayersExclude(List<String> createAuthorizedLayersExclude) {
		this.createAuthorizedLayersExclude = createAuthorizedLayersExclude;
	}

	public List<String> getDeleteAuthorizedLayersInclude() {
		return deleteAuthorizedLayersInclude;
	}

	public void setDeleteAuthorizedLayersInclude(List<String> deleteAuthorizedLayersInclude) {
		this.deleteAuthorizedLayersInclude = deleteAuthorizedLayersInclude;
	}

	public List<String> getDeleteAuthorizedLayersExclude() {
		return deleteAuthorizedLayersExclude;
	}

	public void setDeleteAuthorizedLayersExclude(List<String> deleteAuthorizedLayersExclude) {
		this.deleteAuthorizedLayersExclude = deleteAuthorizedLayersExclude;
	}

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
