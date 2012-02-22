/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2012 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the Apache
 * License, Version 2.0. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */

package org.geomajas.sld.editor.client.widget;

import org.geomajas.sld.client.model.RuleModel;

import com.smartgwt.client.widgets.tree.TreeNode;

/**
 * A node inside the RuleTree.
 * 
 * @author An Buyle
 * 
 */
public class RuleTreeNode extends TreeNode {

	public static final String RULE_NAME_FIELDNAME = "ruleName";

	public static final String RULE_TITLE_FIELDNAME = "ruleTitle";

	public static final String RULE_ID_FIELDNAME = "ruleId";

	public static final String RULE_DATE_FIELDNAME = "ruleData";

	public static final String CHILDREN_FIELDNAME = "children";

	public static final String IS_FOLDER_FIELDNAME = "isFolder";

	private RuleTreeNode[] children;

	private String title;

	private String name;

	/* a rule (=leaf node) */
	public RuleTreeNode(String ruleId, String title, String name, boolean isFolder, RuleModel ruleModel) {
		this(ruleId, title, name, isFolder, ruleModel, null);
	}

	/* a rule group */
	public RuleTreeNode(String ruleId, String title, String name, boolean isFolder, RuleModel ruleModel,
			RuleTreeNode[] children) {
		super(ruleId/* name, must be unique amongst siblings */, children);
		super.setID(ruleId);
		this.children = children;

		setIsFolder(isFolder);
		setRuleId(ruleId);
		setRuleModel(ruleModel);
		setChildren(children);
		setTitle(title);
		setRuleName(name);
	}

	public RuleTreeNode[] getChildren() {
		return this.children;
	}

	public void setChildren(RuleTreeNode[] children) {
		this.children = children;
		super.setChildren(children);
	}

	// TODO remove this method?
	public void setRuleModel(RuleModel ruleModel) {
		setAttribute(RULE_DATE_FIELDNAME, (Object) ruleModel);

	}

	// TODO remove this method ?
	public RuleModel getRuleModel() {
		return (RuleModel) getAttributeAsObject(RULE_DATE_FIELDNAME);

	}

	public String getRuleId() {
		return getAttribute(RULE_ID_FIELDNAME);

	}

	public void setRuleId(String value) {
		setAttribute(RULE_ID_FIELDNAME, value);
	}

	public String getRuleName() {
		return this.name;
	}

	public void setRuleName(String name) {
		this.name = name;
		setAttribute(RULE_NAME_FIELDNAME, name);
	}

	public void setTitle(String title) {
		this.title = title;
		setAttribute(RULE_TITLE_FIELDNAME, title);
	}

	public String getTitle() {
		// return super.getTitle();
		return this.title;
	}

	public void setIsFolder(boolean isFolder) {
		setAttribute(IS_FOLDER_FIELDNAME, isFolder);
	}

	public boolean isFolder() {
		return getAttributeAsBoolean(IS_FOLDER_FIELDNAME);

	}

}
