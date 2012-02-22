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

import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.tree.events.FolderClickEvent;
import com.smartgwt.client.widgets.tree.events.FolderClickHandler;
import com.smartgwt.client.widgets.tree.events.LeafClickEvent;
import com.smartgwt.client.widgets.tree.events.LeafClickHandler;

/**
 * Provides a canvas for displaying a tree of SLD style rules. Also supports GUI controls for rule management:
 * adding/removing a rule, changing the order of the rules.
 * 
 * @author An Buyle
 * 
 */
public class RuleSelector extends Canvas implements LeafClickHandler, FolderClickHandler {

	public void onFolderClick(FolderClickEvent event) {
		// TODO Auto-generated method stub

	}

	public void onLeafClick(LeafClickEvent event) {
		// TODO Auto-generated method stub

	}

	// private SldEditorMessages sldEditorMessages = GWT.create(SldEditorMessages.class);
	//
	// private final String ruleTitleUnspecified = sldEditorMessages.ruleTitleUnspecified();
	//
	// private final String ruleNameUnspecified = "";
	//
	// private static final int INDEX_FIRST_RULE = 1;
	//
	// private static final String INDEX_FIRST_RULE_AS_STRING = String.valueOf(INDEX_FIRST_RULE);
	//
	// private static final int POSITION_IN_NODES_OF_FIRST_RULE = 0; // 0 if no folder nodes,
	//
	// // in other words no grouping of rules
	//
	// private VLayout vLayout;
	//
	// private TreeGrid treeGrid;
	//
	// private Tree tree;
	//
	// private HLayout toolStrip;
	//
	// private AddButton addButton;
	//
	// private RemoveButton removeRuleButton;
	//
	// private UpButton upButton;
	//
	// private DownButton downButton;
	//
	// private RuleTreeNode currentLeaf;
	//
	// private SelectRuleHandler selectRuleHandler;
	//
	// private UpdateRuleHeaderHandler updateRuleHeaderHandler;
	//
	// private GetCurrentRuleStateHandler getCurrentRuleStateHandler;
	//
	// private SldHasChangedHandler sldHasChangedHandler;
	//
	// private DynamicForm ruleGeneralForm;
	//
	// // ruleGeneralForm items
	// private TextItem ruleTitleItem;
	//
	// // private TextItem ruleNameItem;
	//
	// private List<RuleInfo> ruleList;
	//
	// private GeometryType defaultGeomType = GeometryType.POINT;
	//
	// private GeometryType currentGeomType = GeometryType.UNSPECIFIED;
	//
	// public RuleSelector() {
	// super();
	//
	// treeGrid = new TreeGrid();
	// treeGrid.setWidth("100%");
	// treeGrid.setHeight100();
	// treeGrid.setMinHeight(70);
	// // treeGrid.setShowEdges(true);
	// // treeGrid.setBorder("2px solid #A7ABB4");
	// treeGrid.setBodyStyleName("normal");
	// treeGrid.setShowHeader(false);
	// treeGrid.setEmptyMessage(sldEditorMessages.emptyRuleList());
	//
	// treeGrid.setPrompt("Selecteer de stijl die u in het detail venster wenst te bekijken of aanpassen."); // TODO:
	// // i18n
	//
	// TreeGridField titleField = new TreeGridField(RuleTreeNode.RULE_TITLE_FIELDNAME, "Titel");
	// titleField.setCanSort(false);
	//
	// TreeGridField nameField = new TreeGridField(RuleTreeNode.RULE_NAME_FIELDNAME, "Naam");
	// nameField.setEmptyCellValue(ruleNameUnspecified);
	// treeGrid.setFields(titleField, nameField);
	// treeGrid.setWrapCells(true);
	// treeGrid.setFixedRecordHeights(false);
	// // treeGrid.setCanDragResize(true);
	//
	// nameField.setCanSort(false);
	// vLayout = new VLayout();
	// vLayout.setSize("100%", "100%");
	// vLayout.setLayoutTopMargin(10);
	// vLayout.setLayoutLeftMargin(5);
	//
	// vLayout.setGroupTitle(sldEditorMessages.ruleOverviewGroupTitle());
	// vLayout.setIsGroup(true);
	//
	// if (null != treeGrid) {
	// treeGrid.addLeafClickHandler(this);
	// treeGrid.addFolderClickHandler(this);
	// }
	//
	// toolStrip = new HLayout(10/* membersMargin */);
	// addButton = new AddButton();
	//
	// addButton.addClickHandler(new ClickHandler() {
	//
	// public void onClick(ClickEvent event) {
	// onAddRuleButtonClicked(event);
	// }
	// });
	//
	// toolStrip.addMember(addButton);
	//
	// removeRuleButton = new RemoveButton();
	//
	// removeRuleButton.addClickHandler(new ClickHandler() {
	//
	// public void onClick(ClickEvent event) {
	// onRemoveButtonClicked(event);
	//
	// } /* onClick */
	// });
	// toolStrip.addMember(removeRuleButton);
	//
	// upButton = new UpButton();
	//
	// upButton.addClickHandler(new ClickHandler() {
	//
	// public void onClick(ClickEvent event) {
	// onUpButtonClicked(event);
	// }
	// });
	//
	// toolStrip.addMember(upButton);
	//
	// downButton = new DownButton();
	//
	// downButton.addClickHandler(new ClickHandler() {
	//
	// public void onClick(ClickEvent event) {
	// onDownButtonClicked(event);
	// }
	// });
	//
	// toolStrip.addMember(downButton);
	//
	// toolStrip.setAlign(Alignment.LEFT);
	// toolStrip.setPadding(1);
	//
	// ruleGeneralForm = new DynamicForm();
	// ruleGeneralForm.setNumCols(4);
	//
	// ruleTitleItem = new TextItem("RuleTitle", sldEditorMessages.ruleTitleFieldTitle());
	// ruleTitleItem.setWidth(200);
	// ruleTitleItem.addChangedHandler(new ChangedHandler() {
	//
	// public void onChanged(ChangedEvent event) {
	// if (null != currentLeaf) {
	// String ruleTitle = null;
	// if (null == event.getValue()) {
	// ruleTitle = null;
	// } else {
	// ruleTitle = event.getValue().toString();
	// }
	// currentLeaf.setTitle(ruleTitle);
	// refreshMinimal();
	// // Update SLD object, assume currentLeaf != null
	// if (null != currentLeaf.getRuleData()) {
	// updateRuleHeaderHandler.updateTitle(ruleTitle);
	// // TODO setSldHasChangedTrue in call-back
	// }
	// }
	//
	// }
	// });
	//
	// // ruleNameItem = new TextItem("RuleName", "Naam");
	// // ruleNameItem.setEmptyDisplayValue(ruleNameUnspecified);
	// // ruleNameItem.setWidth(200);
	// // ruleNameItem.addChangedHandler(new ChangedHandler() {
	// //
	// // public void onChanged(ChangedEvent event) {
	// // if (null != currentLeaf) {
	// //
	// // String ruleName = null;
	// // if (null == event.getValue()) {
	// // ruleName = null;
	// // } else {
	// // ruleName = event.getValue().toString();
	// // }
	// // currentLeaf.setRuleName(ruleName);
	// // refreshMinimal();
	// // // Update SLD object
	// // if (null != currentLeaf.getRuleData()) {
	// // updateRuleHeaderHandler.execute(ruleTitleItem.getValueAsString(), ruleName);
	// // }
	// // }
	// //
	// // }
	// // });
	//
	// ruleGeneralForm.setItems(ruleTitleItem /* , ruleNameItem */);
	//
	// vLayout.addMember(toolStrip);
	// vLayout.addMember(treeGrid);
	// vLayout.addMember(ruleGeneralForm);
	//
	// addChild(vLayout);
	//
	// }
	//
	// public void addGetCurrentRuleStateHandler(GetCurrentRuleStateHandler getCurrentRuleStateHandler) {
	// this.getCurrentRuleStateHandler = getCurrentRuleStateHandler;
	//
	// }
	//
	// public void addUpdateRuleHeaderHandler(UpdateRuleHeaderHandler updateRuleHeaderHandler) {
	// this.updateRuleHeaderHandler = updateRuleHeaderHandler;
	//
	// }
	//
	// public void addSldHasChangedHandler(SldHasChangedHandler sldHasChangedHandler) {
	// this.sldHasChangedHandler = sldHasChangedHandler;
	// }
	//
	// public void refresh() {
	//
	// treeGrid.setData(tree);
	// treeGrid.getData().openAll();
	// refreshMinimal();
	// }
	//
	// public void refreshMinimal() {
	// treeGrid.markForRedraw();
	// vLayout.markForRedraw();
	// RuleSelector.this.markForRedraw();
	// }
	//
	// public void setRules(List<FeatureTypeStyleInfo> styleList) {
	// this.currentGeomType = GeometryType.UNSPECIFIED;
	//
	// if (null != ruleGeneralForm) {
	// ruleGeneralForm.clearValues();
	// ruleGeneralForm.disable();
	// }
	//
	// if (styleList.size() > 1) {
	// SC.warn("Meer dan 1 groep van regels (&lt;FeatureTypeStyle&gt;) in deze SLD."
	// + "  Enkel de eerste wordt getoond.");
	// // Can be supported later via groups of rules in ruleSelector
	// }
	//
	// FeatureTypeStyleInfo featureTypeStyle = styleList.iterator().next(); // retrieve the first <FeatureTypeStyle>
	// // element
	//
	// if (featureTypeStyle.getRuleList().size() < 1) {
	// // If featureTypeStyle.getRuleList() is NULL or empty, create default rule
	// SC.warn("Een SLD zonder of met leeg stijlelement (&lt;FeatureTypeStyle&gt; element) wordt ingeladen."
	// + "  Een standaard stijl voor een laag met geometrie-type '" + defaultGeomType.value()
	// + "' wordt toegevoegd.");
	//
	// // featureTypeStyle.setName("Nieuwe stijl"); // TODO, for the moment most rule groups don't have a name
	//
	// List<RuleInfo> ruleList = new ArrayList<RuleInfo>();
	// RuleInfo defaultRule = SldUtils.createDefaultRule(defaultGeomType); // TODO: open a dialog window to ask the
	// // geomType
	//
	// ruleList.add(defaultRule);
	//
	// featureTypeStyle.setRuleList(ruleList);
	// sldHasChanged();
	//
	// }
	//
	// ruleList = featureTypeStyle.getRuleList();
	//
	// List<RuleTreeNode> children = new ArrayList<RuleTreeNode>();
	// Integer i = INDEX_FIRST_RULE;
	//
	// for (RuleInfo rule : ruleList) {
	//
	// String title;
	//
	// if (null != rule.getTitle() && rule.getTitle().length() > 0) {
	// title = rule.getTitle();
	// } else if (null != rule.getName() && rule.getName().length() > 0) {
	// title = rule.getName();
	// } else {
	// title = ruleTitleUnspecified;
	// }
	// String name = rule.getName();
	//
	//
	// //TODO temp code : children.add(new RuleTreeNode(i.toString(), title, name, false, rule));
	// i++;
	// }
	// RuleTreeNode[] arrayOfRules = new RuleTreeNode[i - INDEX_FIRST_RULE];
	// arrayOfRules = children.toArray(arrayOfRules);
	//
	// // TODO: support for multiple groups (corresp. with multiple FeatureTypeStyle elements)
	//
	// String styleTitle = featureTypeStyle.getTitle();
	// if (null == styleTitle) {
	// styleTitle = "groep 1";
	// }
	// // final TreeNode root = new RuleTreeNode("root", "Root", "Root", true, null/* data */,
	// // new RuleTreeNode[] { new RuleTreeNode("group 1", styleTitle, featureTypeStyle.getName(), true, null,
	// // arrayOfRules) });
	//
	// final RuleTreeNode root = new RuleTreeNode("root", "Root", "Root", true, null/* data */, arrayOfRules);
	//
	// setRuleTree(root);
	//
	// currentLeaf = children.get(0); /* current leaf = also previous leaf for next call */
	//
	// // Update Rule detail form item to show the first rule
	// selectRule(currentLeaf.getRuleData(), currentLeaf.getTitle(), currentLeaf.getRuleName());
	//
	// }
	//
	// public void addSelectRuleHandler(SelectRuleHandler selectRuleHandler) {
	// this.selectRuleHandler = selectRuleHandler;
	// }
	//
	// public void reset() {
	// makeRuleTreeEmpty();
	// if (null != ruleGeneralForm) {
	// ruleGeneralForm.clearValues();
	// ruleGeneralForm.disable();
	// }
	// }
	//
	// public boolean checkIfAllRulesComplete() {
	// boolean areAllRulesComplete = true;
	//
	// Object[] rules = getAllRulesAsArray();
	// for (Object object : rules) {
	// if (object.getClass().equals(IncompleteRuleInfo.class)) {
	// areAllRulesComplete = false;
	// break;
	// }
	// }
	// return areAllRulesComplete;
	// }
	//
	// // --------------------------------------------------------------------------------------
	//
	// private void onRemoveButtonClicked(ClickEvent event) {
	//
	// final RuleTreeNode node = (RuleTreeNode) treeGrid.getSelectedRecord();
	//
	// if (node == null) {
	// return;
	// }
	// if (node.isFolder()) {
	// return; /* do nothing for a folder node */
	// }
	// SC.confirm(sldEditorMessages.confirmDeleteOfStyle(node.getTitle()), new BooleanCallback() {
	//
	// public void execute(Boolean value) {
	// if (value != null && value) {
	//
	// // -- Remove node from the tree
	// TreeNode[] nodes = tree.getAllNodes();
	//
	// int indexNode = nodes.length;
	// String id = node.getRuleId();
	// Integer idToRemoveAsInt = new Integer(id);
	//
	// RuleTreeNode parent = null;
	// while (indexNode-- > 0) {
	// if (id.equals(((RuleTreeNode) nodes[indexNode]).getRuleId())) {
	// break;
	// }
	// }
	// // Note: Commented out code below, it partly supports grouping of rules
	// // if (indexNode <= 0) {
	// // return; // ABORT
	// // }
	// // int indexParent = indexNode;
	// // while (indexParent-- > 0) {
	// // if (((RuleTreeNode) nodes[indexParent]).isFolder()) {
	// // parent = (RuleTreeNode) nodes[indexParent];
	// // break;
	// // }
	// // }
	// parent = (RuleTreeNode) tree.getRoot();
	// if (parent != null) {
	// RuleTreeNode[] children;
	//
	// children = parent.getChildren();
	// RuleTreeNode[] newChildren = new RuleTreeNode[children.length - 1];
	// int j = 0;
	// for (int i = 0; i < children.length; i++) {
	// Integer idChildAsInt = new Integer(children[i].getRuleId());
	//
	// if (!idToRemoveAsInt.equals(idChildAsInt)) {
	// if (idChildAsInt > idToRemoveAsInt) {
	// children[i].setRuleId(String.valueOf(idChildAsInt - 1));
	// }
	// newChildren[j] = children[i];
	// j++;
	// }
	// }
	//
	// parent.setChildren(newChildren);
	// refresh();
	// int indexSelect = indexNode;
	//
	// if (indexSelect >= nodes.length - 1) {
	// indexSelect--;
	// }
	// treeGrid.selectSingleRecord(indexSelect);
	// updateButtons();
	//
	// currentLeaf = (RuleTreeNode) treeGrid.getSelectedRecord();
	// sldHasChanged();
	//
	// removeRule(node.getRuleId());
	// if (null != currentLeaf && !currentLeaf.isFolder()) {
	// selectRule(currentLeaf.getRuleData(), currentLeaf.getTitle(), currentLeaf.getRuleName());
	// } else {
	// setNoRuleSelected();
	// }
	//
	// } /* parent != null */
	//
	// }
	// } /* execute */
	// });
	// }
	//
	// // --------------------------------------------------------------------------------------
	//
	// private void onAddRuleButtonClicked(ClickEvent event) {
	// if (null != currentLeaf) {
	// currentLeaf.setRuleData(getCurrentRuleStateHandler.execute());
	// }
	//
	// RuleTreeNode newLeaf = new RuleTreeNode(getNewIdForRuleInTree(), "nieuwe stijl"/* title */,
	// ruleNameUnspecified/* name */, false/* isFolder */, null/* ruleData */);
	// // -- add newLeaf at the end of the tree
	//
	// // Note: Commented out code below, it partly supports grouping of rules
	// // TreeNode[] nodes = tree.getAllNodes();
	//
	// // int index = nodes.length;
	// //
	// // RuleTreeNode parent = null;
	// // while (index-- > 0) {
	// // if (((RuleTreeNode) nodes[index]).isFolder()) {
	// // parent = (RuleTreeNode) nodes[index];
	// // break;
	// // }
	// // }
	// RuleTreeNode parent = (RuleTreeNode) tree.getRoot();
	// if (parent != null) {
	// RuleTreeNode[] children;
	//
	// children = parent.getChildren();
	// RuleTreeNode[] newChildren = new RuleTreeNode[children.length + 1];
	// for (int i = 0; i < children.length; i++) {
	// newChildren[i] = children[i];
	// }
	// newChildren[children.length] = newLeaf;
	// parent.setChildren(newChildren);
	// refresh();
	// treeGrid.selectSingleRecord(children.length + POSITION_IN_NODES_OF_FIRST_RULE);
	// /* assuming only 1 group */
	// updateButtons();
	// }
	//
	// RuleInfo defaultRule = SldUtils
	// .createDefaultRule(currentGeomType.equals(GeometryType.UNSPECIFIED) ? GeometryType.POINT
	// : currentGeomType);
	//
	// //TODO: Old code: newLeaf.setRuleData(defaultRule);
	//
	// ruleList.add(defaultRule); /* add new rule add the end of the list */
	//
	// currentLeaf = newLeaf;
	//
	// sldHasChanged();
	// // Update Rule detail form item to show the new rule
	// selectRule(currentLeaf.getRuleData(), currentLeaf.getTitle(), currentLeaf.getRuleName());
	//
	// }
	//
	// // --------------------------------------------------------------------------------------
	// private void onUpButtonClicked(ClickEvent event) {
	// final RuleTreeNode node = (RuleTreeNode) treeGrid.getSelectedRecord();
	//
	// if (node == null) {
	// return;
	// }
	// if (node.isFolder()) {
	// return;
	// }
	//
	// // -- Move node from 1 position up in the tree
	// TreeNode[] nodes = tree.getAllNodes();
	//
	// int indexNode = nodes.length;
	// String id = node.getRuleId();
	//
	// // Note: Commented out code below, it partly supports grouping of rules
	// // RuleTreeNode parent = null;
	// while (indexNode-- > 0) {
	// if (id.equals(((RuleTreeNode) nodes[indexNode]).getRuleId())) {
	// break;
	// }
	// }
	// if (indexNode < POSITION_IN_NODES_OF_FIRST_RULE) {
	// return; // ABORT
	// }
	//
	// // int indexParent = indexNode;
	// // while (indexParent-- > 0) {
	// // if (((RuleTreeNode) nodes[indexParent]).isFolder()) {
	// // parent = (RuleTreeNode) nodes[indexParent];
	// // break;
	// // }
	// // }
	// RuleTreeNode parent = (RuleTreeNode) tree.getRoot();
	//
	// if (parent != null) {
	// RuleTreeNode[] children;
	// int indexSelect = indexNode;
	// children = parent.getChildren();
	// for (int i = 0; i < children.length; i++) {
	// if (id.equals(((RuleTreeNode) children[i]).getRuleId())) {
	// if (i > 0) {
	// indexSelect--;
	// RuleTreeNode swap = children[i - 1];
	// children[i - 1] = children[i];
	// children[i] = swap;
	// children[i].setRuleId(id);
	// }
	// }
	// }
	//
	// parent.setChildren(children);
	// refresh();
	//
	// treeGrid.selectSingleRecord(indexSelect);
	//
	// moveRuleUp(id);
	//
	// node.setRuleId(String.valueOf(new Integer(id) - 1));
	// updateButtons();
	// }
	// }
	//
	// // --------------------------------------------------------------------------------------
	// private void onDownButtonClicked(ClickEvent event) {
	// final RuleTreeNode node = (RuleTreeNode) treeGrid.getSelectedRecord();
	//
	// if (node == null) {
	// return;
	// }
	// if (node.isFolder()) {
	// return;
	// }
	//
	// // -- Move node from 1 position up in the tree
	// TreeNode[] nodes = tree.getAllNodes();
	//
	// int indexNode = nodes.length;
	// String id = node.getRuleId();
	//
	// while (indexNode-- > 0) {
	// if (id.equals(((RuleTreeNode) nodes[indexNode]).getRuleId())) {
	// break;
	// }
	// }
	// if (indexNode >= nodes.length - 1) { /* the last node cannot be moved down */
	// return; // ABORT
	// }
	//
	// // Note: Commented out code below, it partly supports grouping of rules
	// // RuleTreeNode parent = null;
	// // int indexParent = indexNode;
	// // while (indexParent-- > 0) {
	// // if (((RuleTreeNode) nodes[indexParent]).isFolder()) {
	// // parent = (RuleTreeNode) nodes[indexParent];
	// // break;
	// // }
	// // }
	// RuleTreeNode parent = (RuleTreeNode) tree.getRoot();
	//
	// if (parent != null) {
	// RuleTreeNode[] children;
	// int indexSelect = indexNode;
	// children = parent.getChildren();
	// for (int i = 0; i < children.length; i++) {
	// if (id.equals(((RuleTreeNode) children[i]).getRuleId())) {
	// if (i < children.length - 1) {
	// indexSelect++; /* new index of selected node */
	// RuleTreeNode swap = children[i + 1];
	// children[i + 1] = children[i];
	// children[i] = swap;
	// children[i].setRuleId(id);
	// break;
	// }
	// }
	// }
	//
	// parent.setChildren(children);
	// refresh();
	//
	// treeGrid.selectSingleRecord(indexSelect);
	//
	// moveRuleDown(id); /* still with the old ruleId */
	//
	// node.setRuleId(String.valueOf(new Integer(id) + 1));
	// updateButtons();
	//
	// }
	// }
	//
	// // --------------------------------------------------------------------------------------
	// private void selectRule(Object ruleData, String ruleTitle, String ruleName) {
	//
	// updateButtons();
	//
	// if (null != ruleGeneralForm) {
	//
	// ruleTitleItem.setValue(ruleTitle);
	// // ruleNameItem.setValue(ruleName);
	// ruleGeneralForm.enable();
	// ruleGeneralForm.show();
	// }
	//
	// if (ruleData.getClass().equals(RuleInfo.class)) {
	// //TODO: Old code: this.currentGeomType = SldUtils.getGeometryType((RuleInfo) ruleData);
	// }
	//
	// if (null != selectRuleHandler) {
	// selectRuleHandler.execute(true, currentLeaf.getRuleData());
	// }
	//
	// }
	//
	// // --------------------------------------------------------------------------------------
	// private void setNoRuleSelected() {
	// currentLeaf = null;
	//
	// updateButtons();
	// if (null != ruleGeneralForm) {
	// ruleGeneralForm.clearValues();
	// ruleGeneralForm.disable();
	// }
	//
	// if (null != selectRuleHandler) {
	// selectRuleHandler.execute(false, null);
	// }
	//
	// }
	//
	// // --------------------------------------------------------------------------------------
	// private void setRuleTree(RuleTreeNode root) {
	//
	// if (null == tree) {
	// tree = new Tree();
	// tree.setModelType(TreeModelType.CHILDREN);
	//
	// tree.setNameProperty(RuleTreeNode.RULE_ID_FIELDNAME); /* must be unique name amongst siblings */
	// tree.setIdField(RuleTreeNode.RULE_ID_FIELDNAME); /* unique ID within the tree */
	// tree.setTitleProperty(RuleTreeNode.RULE_TITLE_FIELDNAME);
	// tree.setChildrenProperty(RuleTreeNode.CHILDREN_FIELDNAME);
	// tree.setIsFolderProperty(RuleTreeNode.IS_FOLDER_FIELDNAME);
	//
	// tree.setShowRoot(false);
	// }
	// tree.setRoot(root);
	//
	// treeGrid.setData(tree);
	// treeGrid.getData().openAll();
	//
	// treeGrid.selectSingleRecord(POSITION_IN_NODES_OF_FIRST_RULE);
	//
	// updateButtons();
	//
	// treeGrid.markForRedraw();
	//
	// vLayout.markForRedraw();
	// RuleSelector.this.markForRedraw();
	//
	// }
	//
	// // --------------------------------------------------------------------------------------
	// private Object[] getAllRulesAsArray() {
	//
	// Tree tree = treeGrid.getData();
	// List<Object> ruleList = new ArrayList<Object>();
	//
	// if (null != tree) {
	// for (TreeNode node : tree.getAllNodes()) {
	// if (!((RuleTreeNode) node).isFolder()) {
	// Object ruleData = ((RuleTreeNode) node).getRuleData(); /*
	// * will not be null for leaf nodes, except
	// * for default rule ??
	// */
	// if (null == ruleData) {
	// SC.warn("TODO");
	// }
	// ruleList.add(ruleData);
	// }
	// }
	// }
	//
	// return ruleList.toArray();
	//
	// }
	//
	// // --------------------------------------------------------------------------------------
	// private void makeRuleTreeEmpty() {
	// setRuleTree(new RuleTreeNode("root", "Root", null/* name */, true/* isFolder */, null, new RuleTreeNode[] {}));
	// }
	//
	// // --------------------------------------------------------------------------------------
	// private String getNewIdForRuleInTree() {
	// String newId;
	// TreeNode[] nodes = tree.getAllNodes();
	// int index = nodes.length;
	//
	// // Assume only 1 group
	// if (nodes.length == 0 || (nodes.length == 1 && ((RuleTreeNode) nodes[0]).isFolder())) { // no rules,
	// // possibly 1 folder(group) node
	//
	// newId = INDEX_FIRST_RULE_AS_STRING;
	// } else {
	// String lastId = ((RuleTreeNode) nodes[index - 1]).getRuleId();
	// newId = String.valueOf(new Integer(lastId) + 1);
	// }
	// return newId;
	// }
	//
	// // --------------------------------------------------------------------------------------
	// private void removeRule(String ruleId) {
	// /* the ruleId == index of the rule in the ruleList */
	//
	// sldHasChanged();
	// ruleList.remove(new Integer(ruleId) - INDEX_FIRST_RULE); /* remove the rule at position ruleId */
	// }
	//
	// // --------------------------------------------------------------------------------------
	// private void moveRuleUp(String ruleId) {
	// /* the ruleId == index of the rule in the ruleList before it has been moved up */
	// if (new Integer(ruleId) <= INDEX_FIRST_RULE) {
	// return;
	// }
	// sldHasChanged();
	// RuleInfo ruleToSwap = ruleList.get(new Integer(ruleId) - INDEX_FIRST_RULE - 1);
	// ruleList.set(new Integer(ruleId) - INDEX_FIRST_RULE - 1, ruleList.get(new Integer(ruleId) - INDEX_FIRST_RULE));
	// ruleList.set(new Integer(ruleId) - INDEX_FIRST_RULE, ruleToSwap);
	// }
	//
	// // --------------------------------------------------------------------------------------
	// private void moveRuleDown(String ruleId) {
	// /* the ruleId == index of the rule in the ruleList before it has been moved down */
	//
	// if (new Integer(ruleId) >= ruleList.size()) {
	// return;
	// }
	// sldHasChanged();
	//
	// RuleInfo ruleToSwap = ruleList.get(new Integer(ruleId) - INDEX_FIRST_RULE + 1);
	// ruleList.set(new Integer(ruleId), ruleList.get(new Integer(ruleId) - INDEX_FIRST_RULE));
	// ruleList.set(new Integer(ruleId) - INDEX_FIRST_RULE, ruleToSwap);
	// }
	//
	// // -------------------------------------------------------------------------
	// // Private class AddButton:
	// // -------------------------------------------------------------------------
	//
	// /** Definition of the Add button. */
	// private class AddButton extends IButton {
	// public static final String ICON = "[ISOMORPHIC]/" + "geomajas/icons/silk/add.png";
	//
	// public AddButton() {
	// setIcon(ICON);
	// setShowDisabledIcon(true);
	// setPrompt("Voeg nieuwe stijl toe"); // TODO i18n
	// // TODO: validate form first
	// setWidth(24);
	// setDisabled(false);
	//
	// // TODO: setTitle(I18nProvider.getAttribute().btnSaveTitle());
	// // TODO: setTooltip(I18nProvider.getAttribute().btnSaveTooltip());
	//
	// }
	// }
	//
	// // -------------------------------------------------------------------------
	// // Private class RemoveButton:
	// // -------------------------------------------------------------------------
	//
	// /** Definition of the Remove button. */
	// private class RemoveButton extends IButton {
	// public static final String ICON = "[ISOMORPHIC]/" + "geomajas/icons/silk/cancel.png";
	//
	// public RemoveButton() {
	// setIcon(ICON);
	// setShowDisabledIcon(true);
	// setPrompt("Verwijder geselecteerde stijl"); // TODO i18n
	// setWidth(24);
	// setDisabled(false);
	//
	// // TODO: setTitle(I18nProvider.getAttribute().btnSaveTitle());
	// // TODO: setTooltip(I18nProvider.getAttribute().btnSaveTooltip());
	//
	// }
	// }
	//
	// // -------------------------------------------------------------------------
	// // Private class UpButton:
	// // -------------------------------------------------------------------------
	//
	// /** Definition of the Up button. */
	// private class UpButton extends IButton {
	//
	// public static final String ICON_ARROW_UP = "[ISOMORPHIC]/" + "geomajas/icons/silk/arrow_up.png";
	//
	// public UpButton() {
	//
	// setIcon(ICON_ARROW_UP);
	// setShowDisabledIcon(true);
	// setPrompt("Plaats geselecteerde stijl 1 positie hoger"); // TODO i18n
	// setWidth(24);
	// setDisabled(false);
	//
	// // TODO: setTitle(I18nProvider.getAttribute().btnSaveTitle());
	// // TODO: setTooltip(I18nProvider.getAttribute().btnSaveTooltip());
	//
	// }
	// }
	//
	// // -------------------------------------------------------------------------
	// // Private class DownButton:
	// // -------------------------------------------------------------------------
	//
	// /** Definition of the Up button. */
	// private class DownButton extends IButton {
	//
	// public static final String ICON_ARROW_DOWN = "[ISOMORPHIC]/" + "geomajas/icons/silk/arrow_down.png";
	//
	// public DownButton() {
	//
	// setIcon(ICON_ARROW_DOWN);
	// setShowDisabledIcon(true);
	// setPrompt("Plaats geselecteerde stijl 1 positie lager"); // TODO i18n
	// setWidth(24);
	// setDisabled(false);
	//
	// // TODO: setTitle(I18nProvider.getAttribute().btnSaveTitle());
	// // TODO: setTooltip(I18nProvider.getAttribute().btnSaveTooltip());
	//
	// }
	// }
	//
	// public void onFolderClick(FolderClickEvent event) {
	//
	// if (null != currentLeaf) {
	// currentLeaf.setRuleData(getCurrentRuleStateHandler.execute());
	// }
	//
	// setNoRuleSelected();
	//
	// }
	//
	// public void onLeafClick(LeafClickEvent event) {
	//
	// if (null != currentLeaf) {
	// currentLeaf.setRuleData(getCurrentRuleStateHandler.execute());
	// }
	//
	// currentLeaf = (RuleTreeNode) event.getLeaf();
	//
	// // Update Rule detail form item to show the first rule
	// selectRule(currentLeaf.getRuleData(), currentLeaf.getTitle(), currentLeaf.getRuleName());
	//
	// }
	//
	// private void updateButtons() {
	// final RuleTreeNode node = (RuleTreeNode) treeGrid.getSelectedRecord();
	//
	// if (node == null) {
	// removeRuleButton.setDisabled(true);
	// upButton.setDisabled(true);
	// downButton.setDisabled(true);
	// } else if (node.isFolder()) { /* a folder node */
	// removeRuleButton.setDisabled(true);
	// upButton.setDisabled(true);
	// downButton.setDisabled(true);
	// } else {
	// removeRuleButton.setDisabled(false);
	// upButton.setDisabled(INDEX_FIRST_RULE_AS_STRING.equals(node.getRuleId())/* first leaf node */);
	// TreeNode[] nodes = tree.getAllNodes();
	//
	// // Test if ID equals that one of the last leaf node
	// boolean disable = node.getRuleId().equals(((RuleTreeNode) nodes[nodes.length - 1]).getRuleId());
	// downButton.setDisabled(disable);
	// }
	//
	// if (null == ruleList) {
	// addButton.disable();
	// } else {
	// addButton.enable();
	// }
	//
	// }
	//
	// private void sldHasChanged() {
	// if (null != sldHasChangedHandler) {
	// sldHasChangedHandler.execute();
	// }
	// }
}
