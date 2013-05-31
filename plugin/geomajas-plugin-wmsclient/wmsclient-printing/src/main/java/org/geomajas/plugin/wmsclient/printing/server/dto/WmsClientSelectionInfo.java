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

package org.geomajas.plugin.wmsclient.printing.server.dto;

import java.io.Serializable;

import org.geomajas.sld.RuleInfo;

/**
 * DTO for encapsulating selected features (mainly their geometry and id).
 * 
 * @author An Buyle
 */
public class WmsClientSelectionInfo implements Serializable {

	private static final long serialVersionUID = 100L;

	private RuleInfo selectionRule;

	private InfoSelectedFeature[] infoSelectedFeatures;

	/**
	 * Gets the selected features of this layer.
	 * 
	 * @return array of InfoSelectedFeature entries, each entry specifies one selected feature.
	 */
	public InfoSelectedFeature[] getInfoSelectedFeatures() {
		return infoSelectedFeatures;
	}

	/**
	 * Sets the selected features of this layer.
	 * 
	 * @param infoSelectedFeats
	 *            array of InfoSelectedFeature entries, each entry specifies one selected feature.
	 */
	public void setInfoSelectedFeatures(InfoSelectedFeature[] infoSelectedFeats) {
		this.infoSelectedFeatures = infoSelectedFeats;
	}

	public RuleInfo getSelectionRule() {
		return this.selectionRule;
	}

	public void setSelectionRule(RuleInfo selectionRule) {
		this.selectionRule = selectionRule;
	}
}