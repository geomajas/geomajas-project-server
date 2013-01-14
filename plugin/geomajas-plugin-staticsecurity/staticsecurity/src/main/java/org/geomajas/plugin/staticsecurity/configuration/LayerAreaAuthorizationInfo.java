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

	/**
	 * Get the visible area as WKT string.
	 *
	 * @return visible area
	 */
	public String getVisibleArea() {
		return visibleArea;
	}

	/**
	 * Set the visible area as WKT string.
	 *
	 * @param visibleArea visible area as WKT string
	 */
	public void setVisibleArea(String visibleArea) {
		this.visibleArea = visibleArea;
	}

	/**
	 * Get whether partly visible geometries are considered visible.
	 *
	 * @return true when features which are only party within the visible area should be considered visible
	 */
	public boolean isPartlyVisibleSufficient() {
		return partlyVisibleSufficient;
	}

	/**
	 * Set whether party visible geometries are considered visible.
	 *
	 * @param partlyVisibleSufficient is partly visible considered visible?
	 */
	public void setPartlyVisibleSufficient(boolean partlyVisibleSufficient) {
		this.partlyVisibleSufficient = partlyVisibleSufficient;
	}

	/**
	 * Get area where updates are allowed as WKT string.
	 *
	 * @return update authorized area
	 */
	public String getUpdateAuthorizedArea() {
		return updateAuthorizedArea;
	}

	/**
	 * Set area where updates are allowed as WKT string.
	 *
	 * @param updateAuthorizedArea update authorized area as WKT string
	 */
	public void setUpdateAuthorizedArea(String updateAuthorizedArea) {
		this.updateAuthorizedArea = updateAuthorizedArea;
	}

	/**
	 * Get whether partly updatable geometries are considered updatable.
	 *
	 * @return true when features which are only partly within the update authorized area should be considered updatable
	 */
	public boolean isPartlyUpdateAuthorizedSufficient() {
		return partlyUpdateAuthorizedSufficient;
	}

	/**
	 * Set whether partly updatable geometries are considered updatable.
	 *
	 * @param partlyUpdateAuthorizedSufficient is overlap with update area sufficient to be updatable
	 */
	public void setPartlyUpdateAuthorizedSufficient(boolean partlyUpdateAuthorizedSufficient) {
		this.partlyUpdateAuthorizedSufficient = partlyUpdateAuthorizedSufficient;
	}

	/**
	 * Get area in which features can be created as WKT string.
	 *
	 * @return are in which features can be created
	 */
	public String getCreateAuthorizedArea() {
		return createAuthorizedArea;
	}

	/**
	 * Set area in which features can be created as WKT string.
	 *
	 * @param createAuthorizedArea create authorized area
	 */
	public void setCreateAuthorizedArea(String createAuthorizedArea) {
		this.createAuthorizedArea = createAuthorizedArea;
	}

	/**
	 * Get whether a feature which partly which intersect the create geometry can be created.
	 *
	 * @return true when features which are only partly within the create authorized area should be considered creatable
	 */
	public boolean isPartlyCreateAuthorizedSufficient() {
		return partlyCreateAuthorizedSufficient;
	}

	/**
	 * Set whether a feature which partly which intersect the create geometry can be created.
	 *
	 * @param partlyCreateAuthorizedSufficient is overlap sufficient for create authorization
	 */
	public void setPartlyCreateAuthorizedSufficient(boolean partlyCreateAuthorizedSufficient) {
		this.partlyCreateAuthorizedSufficient = partlyCreateAuthorizedSufficient;
	}

	/**
	 * Get area in which features can be deleted as WKT string.
	 *
	 * @return delete authorized area
	 */
	public String getDeleteAuthorizedArea() {
		return deleteAuthorizedArea;
	}

	/**
	 * Set area in which features can be deleted as WKT string.
	 *
	 * @param deleteAuthorizedArea delete authorized area
	 */
	public void setDeleteAuthorizedArea(String deleteAuthorizedArea) {
		this.deleteAuthorizedArea = deleteAuthorizedArea;
	}

	/**
	 * Get whether a feature which partly which intersect the delete geometry can be delete.
	 *
	 * @return true when features which are only partly within the deletable area should be considered deletable
	 */
	public boolean isPartlyDeleteAuthorizedSufficient() {
		return partlyDeleteAuthorizedSufficient;
	}

	/**
	 * Set whether a feature which partly which intersect the delete geometry can be delete.
	 *
	 * @param partlyDeleteAuthorizedSufficient is overlap sufficient for delete authorization
	 */
	public void setPartlyDeleteAuthorizedSufficient(boolean partlyDeleteAuthorizedSufficient) {
		this.partlyDeleteAuthorizedSufficient = partlyDeleteAuthorizedSufficient;
	}
}
