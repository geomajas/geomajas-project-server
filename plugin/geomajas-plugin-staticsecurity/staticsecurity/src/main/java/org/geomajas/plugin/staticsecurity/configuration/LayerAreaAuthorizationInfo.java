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
