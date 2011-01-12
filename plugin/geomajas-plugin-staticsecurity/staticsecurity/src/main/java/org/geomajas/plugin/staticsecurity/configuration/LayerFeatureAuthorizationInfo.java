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

import org.geomajas.global.Api;

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

	public List<String> getVisibleIncludes() {
		return visibleIncludes;
	}

	public void setVisibleIncludes(List<String> visibleIncludes) {
		this.visibleIncludes = visibleIncludes;
	}

	public List<String> getVisibleExcludes() {
		return visibleExcludes;
	}

	public void setVisibleExcludes(List<String> visibleExcludes) {
		this.visibleExcludes = visibleExcludes;
	}

	public List<String> getUpdateAuthorizedIncludes() {
		return updateAuthorizedIncludes;
	}

	public void setUpdateAuthorizedIncludes(List<String> updateAuthorizedIncludes) {
		this.updateAuthorizedIncludes = updateAuthorizedIncludes;
	}

	public List<String> getUpdateAuthorizedExcludes() {
		return updateAuthorizedExcludes;
	}

	public void setUpdateAuthorizedExcludes(List<String> updateAuthorizedExcludes) {
		this.updateAuthorizedExcludes = updateAuthorizedExcludes;
	}

	public List<String> getDeleteAuthorizedIncludes() {
		return deleteAuthorizedIncludes;
	}

	public void setDeleteAuthorizedIncludes(List<String> deleteAuthorizedIncludes) {
		this.deleteAuthorizedIncludes = deleteAuthorizedIncludes;
	}

	public List<String> getDeleteAuthorizedExcludes() {
		return deleteAuthorizedExcludes;
	}

	public void setDeleteAuthorizedExcludes(List<String> deleteAuthorizedExcludes) {
		this.deleteAuthorizedExcludes = deleteAuthorizedExcludes;
	}

	public boolean isCreateAuthorized() {
		return createAuthorized;
	}

	public void setCreateAuthorized(boolean createAuthorized) {
		this.createAuthorized = createAuthorized;
	}
}
