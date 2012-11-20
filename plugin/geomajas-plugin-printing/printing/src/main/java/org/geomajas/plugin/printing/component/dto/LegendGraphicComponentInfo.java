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
package org.geomajas.plugin.printing.component.dto;

import org.geomajas.annotation.Api;
import org.geomajas.sld.RuleInfo;

/**
 * DTO object for {@link org.geomajas.plugin.printing.component.impl.LegendGraphicComponentImpl}.
 * 
 * @author Jan De Moerloose
 * @since 2.0.0
 * 
 */
@Api(allMethods = true)
public class LegendGraphicComponentInfo extends PrintComponentInfo {

	private static final long serialVersionUID = 200L;

	private String label;

	private String layerId;

	private RuleInfo ruleInfo;

	
	public String getLabel() {
		return label;
	}

	
	public void setLabel(String label) {
		this.label = label;
	}

	
	public String getLayerId() {
		return layerId;
	}

	
	public void setLayerId(String layerId) {
		this.layerId = layerId;
	}

	
	public RuleInfo getRuleInfo() {
		return ruleInfo;
	}

	
	public void setRuleInfo(RuleInfo ruleInfo) {
		this.ruleInfo = ruleInfo;
	}


}
