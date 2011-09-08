/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2011 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */

package org.geomajas.plugin.staticsecurity.configuration;

import org.geomajas.annotation.Api;
import org.geomajas.security.BaseAuthorization;

import java.util.List;

/**
 * Allow configuring authorizations at tool, command and layer level.
 * <p/>
 * You can specify a list a includes and excludes. Anything which is not included in not authorized. Anything which is
 * included is only authorized if it is not excluded.
 * <p/>
 * Regex expressions can be used for the strings.
 *
 * @author Joachim Van der Auwera
 * @since 1.6.0
 */
@Api(allMethods = true)
public class LayerAuthorizationInfo implements AuthorizationInfo {

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

	public BaseAuthorization getAuthorization() {
		return new LayerAuthorization(this);
	}
}
