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

	/**
	 * List of regular expressions of tools to include.
	 *
	 * @return list of regular expressions
	 */
	public List<String> getToolsInclude() {
		return toolsInclude;
	}

	/**
	 * List of regular expressions of tools to include.
	 *
	 * @param toolsInclude list of regular expressions
	 */
	public void setToolsInclude(List<String> toolsInclude) {
		this.toolsInclude = toolsInclude;
	}

	/**
	 * List of regular expressions of tools to exclude.
	 *
	 * @return list of regular expressions
	 */
	public List<String> getToolsExclude() {
		return toolsExclude;
	}

	/**
	 * List of regular expressions of tools to exclude.
	 *
	 * @param toolsExclude list of regular expressions
	 */
	public void setToolsExclude(List<String> toolsExclude) {
		this.toolsExclude = toolsExclude;
	}

	/**
	 * List of regular expressions of commands to include.
	 *
	 * @return list of regular expressions
	 */
	public List<String> getCommandsInclude() {
		return commandsInclude;
	}

	/**
	 * List of regular expressions of commands to include.
	 *
	 * @param commandsInclude list of regular expressions
	 */
	public void setCommandsInclude(List<String> commandsInclude) {
		this.commandsInclude = commandsInclude;
	}

	/**
	 * List of regular expressions of commands to exclude.
	 *
	 * @return list of regular expressions
	 */
	public List<String> getCommandsExclude() {
		return commandsExclude;
	}

	/**
	 * List of regular expressions of commands to exclude.
	 *
	 * @param commandsExclude list of regular expressions
	 */
	public void setCommandsExclude(List<String> commandsExclude) {
		this.commandsExclude = commandsExclude;
	}

	/**
	 * List of regular expressions of visible layers to include.
	 *
	 * @return list of regular expressions
	 */
	public List<String> getVisibleLayersInclude() {
		return visibleLayersInclude;
	}

	/**
	 * List of regular expressions of visible layers to include.
	 *
	 * @param visibleLayersInclude list of regular expressions
	 */
	public void setVisibleLayersInclude(List<String> visibleLayersInclude) {
		this.visibleLayersInclude = visibleLayersInclude;
	}

	/**
	 * List of regular expressions of visible layers to exclude.
	 *
	 * @return list of regular expressions
	 */
	public List<String> getVisibleLayersExclude() {
		return visibleLayersExclude;
	}

	/**
	 * List of regular expressions of visible layers to exclude.
	 *
	 * @param visibleLayersExclude list of regular expressions
	 */
	public void setVisibleLayersExclude(List<String> visibleLayersExclude) {
		this.visibleLayersExclude = visibleLayersExclude;
	}

	/**
	 * List of regular expressions of to include layers which can be updated.
	 *
	 * @return list of regular expressions
	 */
	public List<String> getUpdateAuthorizedLayersInclude() {
		return updateAuthorizedLayersInclude;
	}

	/**
	 * List of regular expressions of to include layers which can be updated.
	 *
	 * @param updateAuthorizedLayersInclude list of regular expressions
	 */
	public void setUpdateAuthorizedLayersInclude(List<String> updateAuthorizedLayersInclude) {
		this.updateAuthorizedLayersInclude = updateAuthorizedLayersInclude;
	}

	/**
	 * List of regular expressions of to exclude layers which can be updated.
	 *
	 * @return list of regular expressions
	 */
	public List<String> getUpdateAuthorizedLayersExclude() {
		return updateAuthorizedLayersExclude;
	}

	/**
	 * List of regular expressions of to exclude layers which can be updated.
	 *
	 * @param updateAuthorizedLayersExclude list of regular expressions
	 */
	public void setUpdateAuthorizedLayersExclude(List<String> updateAuthorizedLayersExclude) {
		this.updateAuthorizedLayersExclude = updateAuthorizedLayersExclude;
	}

	/**
	 * List of regular expressions of to include layers in which features can be created.
	 *
	 * @return list of regular expressions
	 */
	public List<String> getCreateAuthorizedLayersInclude() {
		return createAuthorizedLayersInclude;
	}

	/**
	 * List of regular expressions of to include layers in which features can be created.
	 *
	 * @param createAuthorizedLayersInclude list of regular expressions
	 */
	public void setCreateAuthorizedLayersInclude(List<String> createAuthorizedLayersInclude) {
		this.createAuthorizedLayersInclude = createAuthorizedLayersInclude;
	}

	/**
	 * List of regular expressions of to exclude layers in which features can be created.
	 *
	 * @return list of regular expressions
	 */
	public List<String> getCreateAuthorizedLayersExclude() {
		return createAuthorizedLayersExclude;
	}

	/**
	 * List of regular expressions of to exclude layers in which features can be created.
	 *
	 * @param createAuthorizedLayersExclude list of regular expressions
	 */
	public void setCreateAuthorizedLayersExclude(List<String> createAuthorizedLayersExclude) {
		this.createAuthorizedLayersExclude = createAuthorizedLayersExclude;
	}

	/**
	 * List of regular expressions of to include layers in which features can be deleted.
	 *
	 * @return list of regular expressions
	 */
	public List<String> getDeleteAuthorizedLayersInclude() {
		return deleteAuthorizedLayersInclude;
	}

	/**
	 * List of regular expressions of to include layers in which features can be deleted.
	 *
	 * @param deleteAuthorizedLayersInclude list of regular expressions
	 */
	public void setDeleteAuthorizedLayersInclude(List<String> deleteAuthorizedLayersInclude) {
		this.deleteAuthorizedLayersInclude = deleteAuthorizedLayersInclude;
	}

	/**
	 * List of regular expressions of to exclude layers in which features can be deleted.
	 *
	 * @return list of regular expressions
	 */
	public List<String> getDeleteAuthorizedLayersExclude() {
		return deleteAuthorizedLayersExclude;
	}

	/**
	 * List of regular expressions of to exclude layers in which features can be deleted.
	 *
	 * @param deleteAuthorizedLayersExclude list of regular expressions
	 */
	public void setDeleteAuthorizedLayersExclude(List<String> deleteAuthorizedLayersExclude) {
		this.deleteAuthorizedLayersExclude = deleteAuthorizedLayersExclude;
	}

	/** {@inheritDoc} */
	public BaseAuthorization getAuthorization() {
		return new LayerAuthorization(this);
	}
}
