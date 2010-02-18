/*
 * This file is part of Geomajas, a component framework for building
 * rich Internet applications (RIA) with sophisticated capabilities for the
 * display, analysis and management of geographic information.
 * It is a building block that allows developers to add maps
 * and other geographic data capabilities to their web applications.
 *
 * Copyright 2008-2010 Geosparc, http://www.geosparc.com, Belgium
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.geomajas.plugin.springsecurity.configuration;

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
 */
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
