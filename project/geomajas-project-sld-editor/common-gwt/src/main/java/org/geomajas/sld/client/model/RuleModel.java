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

import org.geomajas.sld.editor.client.GeometryTypes;
import org.geomajas.sld.editor.client.i18n.SldEditorMessages;

import com.google.gwt.core.client.GWT;

/**
 * @author An Buyle
 *
 */
public class RuleModel {

	private SldEditorMessages sldEditorMessages = GWT.create(SldEditorMessages.class);

	private String name;

	private String title;

	private RuleData ruleData;

	public enum TypeOfRule {
			DEFAULT_RULE,
			INCOMPLETE_RULE,
			COMPLETE_RULE
	}

	/**
	 * Get the 'Name' element value.
	 * 
	 * @return value
	 */
	public String getName() {
		return name;
	}

	/**
	 * Set the 'Name' element value.
	 * 
	 * @param name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Get the 'Title' element value.
	 * 
	 * @return value
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * Set the 'Title' element value.
	 * 
	 * @param title
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	public RuleData getRuleData() {
		return ruleData;
	}

	public void setRuleData(RuleData ruleData) {
		this.ruleData = ruleData;
	}

	public RuleModel createDefaultRuleModel(GeometryTypes geometryType) {
		RuleModel defaultRuleModel = new RuleModel();

		defaultRuleModel.setTitle(sldEditorMessages.ruleTitleUnspecified());

		defaultRuleModel.setRuleData(new RuleData());
		defaultRuleModel.getRuleData().setGeometryTypeSymbol(geometryType);
		defaultRuleModel.getRuleData().setTypeOfRule(TypeOfRule.DEFAULT_RULE);

		return defaultRuleModel;
	}
}
