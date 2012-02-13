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

import org.geomajas.sld.client.model.RuleModel.TypeOfRule;
import org.geomajas.sld.editor.client.GeometryType;

/**
 * @author An Buyle
 *
 */
public class RuleData {

	private TypeOfRule typeOfRule;
	private GeometryType geometryTypeSymbol;
	private Object ruleBody; // full Rule record (general rule info + Filter + Symbolizer list) for this "rule" 
						// if typeOfRule is COMPLETE_RULE

	public TypeOfRule getTypeOfRule() {
		return typeOfRule;
	}

	public void setTypeOfRule(TypeOfRule typeOfRule) {
		this.typeOfRule = typeOfRule;
	}

	public GeometryType getGeometryTypeSymbol() {
		return geometryTypeSymbol;
	}

	public void setGeometryTypeSymbol(GeometryType geometryTypeSymbol) {
		this.geometryTypeSymbol = geometryTypeSymbol;
	}

	public Object getRuleBody() {
		return ruleBody;
	}

	public void setRuleBody(Object ruleBody) {
		this.ruleBody = ruleBody;
	}

}
