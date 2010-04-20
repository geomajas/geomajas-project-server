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

import org.geomajas.global.Api;

/**
 * Area authorization info for a specific layer.
 *
 * @author Joachim Van der Auwera
 * @since 1.6.0
 */
@Api(allMethods = true)
public class LayerAreaAuthorizationInfo {

	private String visibleArea;
	private boolean partlyVisibleSufficient;
	private String updateAuthorizedArea;
	private boolean partlyUpdateAuthorizedSufficient;
	private String createAuthorizedArea;
	private boolean partlyCreateAuthorizedSufficient;
	private String deleteAuthorizedArea;
	private boolean partlyDeleteAuthorizedSufficient;

	public String getVisibleArea() {
		return visibleArea;
	}

	public void setVisibleArea(String visibleArea) {
		this.visibleArea = visibleArea;
	}

	public boolean isPartlyVisibleSufficient() {
		return partlyVisibleSufficient;
	}

	public void setPartlyVisibleSufficient(boolean partlyVisibleSufficient) {
		this.partlyVisibleSufficient = partlyVisibleSufficient;
	}

	public String getUpdateAuthorizedArea() {
		return updateAuthorizedArea;
	}

	public void setUpdateAuthorizedArea(String updateAuthorizedArea) {
		this.updateAuthorizedArea = updateAuthorizedArea;
	}

	public boolean isPartlyUpdateAuthorizedSufficient() {
		return partlyUpdateAuthorizedSufficient;
	}

	public void setPartlyUpdateAuthorizedSufficient(boolean partlyUpdateAuthorizedSufficient) {
		this.partlyUpdateAuthorizedSufficient = partlyUpdateAuthorizedSufficient;
	}

	public String getCreateAuthorizedArea() {
		return createAuthorizedArea;
	}

	public void setCreateAuthorizedArea(String createAuthorizedArea) {
		this.createAuthorizedArea = createAuthorizedArea;
	}

	public boolean isPartlyCreateAuthorizedSufficient() {
		return partlyCreateAuthorizedSufficient;
	}

	public void setPartlyCreateAuthorizedSufficient(boolean partlyCreateAuthorizedSufficient) {
		this.partlyCreateAuthorizedSufficient = partlyCreateAuthorizedSufficient;
	}

	public String getDeleteAuthorizedArea() {
		return deleteAuthorizedArea;
	}

	public void setDeleteAuthorizedArea(String deleteAuthorizedArea) {
		this.deleteAuthorizedArea = deleteAuthorizedArea;
	}

	public boolean isPartlyDeleteAuthorizedSufficient() {
		return partlyDeleteAuthorizedSufficient;
	}

	public void setPartlyDeleteAuthorizedSufficient(boolean partlyDeleteAuthorizedSufficient) {
		this.partlyDeleteAuthorizedSufficient = partlyDeleteAuthorizedSufficient;
	}
}
