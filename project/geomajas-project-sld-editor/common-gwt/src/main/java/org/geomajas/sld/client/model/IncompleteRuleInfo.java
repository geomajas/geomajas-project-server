/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2012 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */

package org.geomajas.sld.client.model;

import org.geomajas.sld.RuleInfo;

/**
 * Incomplete ruleInfo class. Used when rule filter hasn't been fully specified.
 * 
 * @author An Buyle
 * 
 */
public class IncompleteRuleInfo {

	private RuleInfo ruleInfo;

	private IncompleteFilterInfo incompleteFilterInfo;

	public RuleInfo getRule() {
		return ruleInfo;
	}

	public void setRuleInfo(RuleInfo ruleInfo) {
		this.ruleInfo = ruleInfo;
	}

	public IncompleteFilterInfo getIncompleteFilter() {
		return incompleteFilterInfo;
	}

	public void setIncompleteFilterInfo(IncompleteFilterInfo incompleteFilterInfo) {
		this.incompleteFilterInfo = incompleteFilterInfo;
	}

}
