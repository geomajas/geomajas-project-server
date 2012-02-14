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
import org.geomajas.sld.editor.client.GeometryType;

/**
 * @author An Buyle
 *
 */
public class RuleData {
	/**
	 * @author An Buyle
	 *
	 */
	public enum TypeOfRule {
		DEFAULT_RULE,
		INCOMPLETE_RULE,
		COMPLETE_RULE
	}
	
	private GeometryType geometryType;

	private TypeOfRule typeOfRule;

	private IncompleteRuleInfo incompleteRuleBody;
	// Incomplete Rule record (Incomplete Filter + Symbolizer list) for this "rule" 
	// 	if typeOfRule is INCOMPLETE_RULE

	private RuleInfo completeRuleBody;

	// full Rule record (general rule info + Filter + Symbolizer list) for this "rule" 
	// if typeOfRule is COMPLETE_RULE



	public RuleData(GeometryType geometryType) {
		this.setGeometryType(geometryType);
	}

	public TypeOfRule getTypeOfRule() {
		return typeOfRule;
	}

	public void setTypeOfRule(TypeOfRule typeOfRule) {
		this.typeOfRule = typeOfRule;
		if (TypeOfRule.COMPLETE_RULE == typeOfRule) {
			//TODO: OK to override current incompleteRuleBody ?
			incompleteRuleBody = null;
		} else {
			//TODO: OK to override current completeRuleBody ?
			completeRuleBody = null;
		}
	}

	public IncompleteRuleInfo getIncompleteRuleBody() {
		return incompleteRuleBody;
	}

	public void setIncompleteRuleBody(IncompleteRuleInfo incompleteRuleBody) {
		this.typeOfRule = TypeOfRule.INCOMPLETE_RULE;
		this.incompleteRuleBody = incompleteRuleBody;
	}

	public RuleInfo getCompleteRuleBody() {
		return completeRuleBody;
	}

	public void setCompleteRuleBody(RuleInfo completeRuleBody) {
		this.typeOfRule = TypeOfRule.COMPLETE_RULE;
		this.completeRuleBody = completeRuleBody;
	}

	public static RuleData createDefaultRuleData(GeometryType geometryType) {
		RuleData defaultRuleData = new RuleData(geometryType);

		defaultRuleData.setTypeOfRule(TypeOfRule.DEFAULT_RULE);
		return defaultRuleData;

	}

	public void setGeometryType(GeometryType geometryType) {
		this.geometryType = geometryType;
	}

	public GeometryType getGeometryType() {
		return geometryType;
	}

}
