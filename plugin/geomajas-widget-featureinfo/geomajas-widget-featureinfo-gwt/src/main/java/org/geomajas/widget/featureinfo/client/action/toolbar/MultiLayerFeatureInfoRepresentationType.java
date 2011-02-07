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
package org.geomajas.widget.featureinfo.client.action.toolbar;


/**
 * The representation of a multilayer featureinfo window.
 * @author Oliver May
 *
 */
public enum MultiLayerFeatureInfoRepresentationType {
	
	TREE("tree"),
	/**
	 * Show the multi layer featureinfo as a tree.
	 */
	TREE_FULL("tree_full"),
	/**
	 * Show the multi layer featureinfo as a tree with the attributes as part of the tree.
	 */
	FLAT("flat");
	/**
	 * SHOW the multi layer featureinfo in a flat format.
	 */
	
	private String value;

	
	private MultiLayerFeatureInfoRepresentationType(String value) {
		this.value = value;
	}
	
	public String getValue() {
		return this.value;
	}
}
