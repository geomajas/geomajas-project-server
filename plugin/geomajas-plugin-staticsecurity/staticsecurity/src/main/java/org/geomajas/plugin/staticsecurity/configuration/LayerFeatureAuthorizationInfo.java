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

import java.util.List;

/**
 * Feature authorization info for a specific layer.
 * <p/>
 * You can specify a list a includes and excludes. Anything which is not included in not authorized. Anything which is
 * included is only authorized if it is not excluded.
 * <p/>
 * Regex expressions can be used for the strings. It compares with feature ids after conversion to String.
 *
 * @author Joachim Van der Auwera
 * @since 1.6.0
 */
@Api(allMethods = true)
public class LayerFeatureAuthorizationInfo {
	private List<String> visibleIncludes;
	private List<String> visibleExcludes;
	private List<String> updateAuthorizedIncludes;
	private List<String> updateAuthorizedExcludes;
	private List<String> deleteAuthorizedIncludes;
	private List<String> deleteAuthorizedExcludes;
	private boolean createAuthorized;

	/**
	 * List of regular expressions of feature ids to include as visible.
	 *
	 * @return list of regular expressions
	 */
	public List<String> getVisibleIncludes() {
		return visibleIncludes;
	}

	/**
	 * List of regular expressions of feature ids to include as visible.
	 *
	 * @param visibleIncludes list of regular expressions
	 */
	public void setVisibleIncludes(List<String> visibleIncludes) {
		this.visibleIncludes = visibleIncludes;
	}

	/**
	 * List of regular expressions of feature ids to exclude as visible.
	 *
	 * @return list of regular expressions
	 */
	public List<String> getVisibleExcludes() {
		return visibleExcludes;
	}

	/**
	 * List of regular expressions of feature ids to exclude as visible.
	 *
	 * @param visibleExcludes list of regular expressions
	 */
	public void setVisibleExcludes(List<String> visibleExcludes) {
		this.visibleExcludes = visibleExcludes;
	}

	/**
	 * List of regular expressions of feature ids to include as updatable.
	 *
	 * @return list of regular expressions
	 */
	public List<String> getUpdateAuthorizedIncludes() {
		return updateAuthorizedIncludes;
	}

	/**
	 * List of regular expressions of feature ids to include as updatable.
	 *
	 * @param updateAuthorizedIncludes list of regular expressions
	 */
	public void setUpdateAuthorizedIncludes(List<String> updateAuthorizedIncludes) {
		this.updateAuthorizedIncludes = updateAuthorizedIncludes;
	}

	/**
	 * List of regular expressions of feature ids to exclude as updatable.
	 *
	 * @return list of regular expressions
	 */
	public List<String> getUpdateAuthorizedExcludes() {
		return updateAuthorizedExcludes;
	}

	/**
	 * List of regular expressions of feature ids to exclude as updatable.
	 *
	 * @param updateAuthorizedExcludes list of regular expressions
	 */
	public void setUpdateAuthorizedExcludes(List<String> updateAuthorizedExcludes) {
		this.updateAuthorizedExcludes = updateAuthorizedExcludes;
	}

	/**
	 * List of regular expressions of feature ids to include as deletable.
	 *
	 * @return list of regular expressions
	 */
	public List<String> getDeleteAuthorizedIncludes() {
		return deleteAuthorizedIncludes;
	}

	/**
	 * List of regular expressions of feature ids to include as deletable.
	 *
	 * @param deleteAuthorizedIncludes list of regular expressions
	 */
	public void setDeleteAuthorizedIncludes(List<String> deleteAuthorizedIncludes) {
		this.deleteAuthorizedIncludes = deleteAuthorizedIncludes;
	}

	/**
	 * List of regular expressions of feature ids to exclude as deletable.
	 *
	 * @return list of regular expressions
	 */
	public List<String> getDeleteAuthorizedExcludes() {
		return deleteAuthorizedExcludes;
	}

	/**
	 * List of regular expressions of feature ids to exclude as deletable.
	 *
	 * @param deleteAuthorizedExcludes list of regular expressions
	 */
	public void setDeleteAuthorizedExcludes(List<String> deleteAuthorizedExcludes) {
		this.deleteAuthorizedExcludes = deleteAuthorizedExcludes;
	}

	/**
	 * Can new features be created?
	 *
	 * @return true when features can be created
	 */
	public boolean isCreateAuthorized() {
		return createAuthorized;
	}

	/**
	 * Can new features be created?
	 *
	 * @param createAuthorized can new features be created?
	 */
	public void setCreateAuthorized(boolean createAuthorized) {
		this.createAuthorized = createAuthorized;
	}
}
