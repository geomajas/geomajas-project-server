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
package org.geomajas.widget.layer.configuration.client;

import javax.validation.constraints.NotNull;

import org.geomajas.annotation.Api;


/**
 * A tree node representing a (collapsable) folder in the tree.
 * 
 * @author Oliver May
 *
 * @since 1.0.0
 */
@Api
public class ClientBranchNodeInfo extends ClientAbstractNodeInfo {

	/**
	 * 
	 */
	private static final long serialVersionUID = 100L;

	@NotNull
	private String label;

	@NotNull
	private boolean expanded;

	/**
	 * Get the tree node label.
	 * 
	 * @return label
	 */
	public String getLabel() {
		return label;
	}

	/**
	 * Set the tree node label.
	 * 
	 * @param label
	 *            label
	 */
	public void setLabel(String label) {
		this.label = label;
	}

	/**
	 * Is the node expanded?
	 * 
	 * @return true when node is expanded
	 */
	public boolean isExpanded() {
		return expanded;
	}

	/**
	 * Set the "expanded" state for the node.
	 * 
	 * @param expanded
	 *            true when node is expanded
	 */
	public void setExpanded(boolean expanded) {
		this.expanded = expanded;
	}

	
}
