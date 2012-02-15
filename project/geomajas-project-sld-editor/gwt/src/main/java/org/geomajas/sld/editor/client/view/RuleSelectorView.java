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

package org.geomajas.sld.editor.client.view;

import java.util.ArrayList;
import java.util.List;

import org.geomajas.sld.client.model.RuleData;
import org.geomajas.sld.client.model.RuleGroup;
import org.geomajas.sld.client.model.RuleModel;
import org.geomajas.sld.client.model.event.RuleSelectedEvent;
import org.geomajas.sld.client.model.event.RuleSelectedEvent.RuleSelectedHandler;
import org.geomajas.sld.client.presenter.RuleSelectorPresenter;
import org.geomajas.sld.client.presenter.RuleSelectorPresenter.MyModel;
import org.geomajas.sld.client.view.ViewUtil;
import org.geomajas.sld.editor.client.GeometryType;
import org.geomajas.sld.editor.client.i18n.SldEditorMessages;
import org.geomajas.sld.editor.client.widget.GetCurrentRuleStateHandler;
import org.geomajas.sld.editor.client.widget.RuleTreeNode;
import org.geomajas.sld.editor.client.widget.SelectRuleHandler;
import org.geomajas.sld.editor.client.widget.SldHasChangedHandler;
import org.geomajas.sld.editor.client.widget.UpdateRuleHeaderHandler;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.event.shared.HandlerRegistration;
import com.gwtplatform.mvp.client.ViewImpl;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.TreeModelType;
import com.smartgwt.client.util.BooleanCallback;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.form.fields.events.ChangedEvent;
import com.smartgwt.client.widgets.form.fields.events.ChangedHandler;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.tree.Tree;
import com.smartgwt.client.widgets.tree.TreeGrid;
import com.smartgwt.client.widgets.tree.TreeGridField;
import com.smartgwt.client.widgets.tree.TreeNode;
import com.smartgwt.client.widgets.tree.events.FolderClickEvent;
import com.smartgwt.client.widgets.tree.events.FolderClickHandler;
import com.smartgwt.client.widgets.tree.events.LeafClickEvent;
import com.smartgwt.client.widgets.tree.events.LeafClickHandler;

/**
 * @author An Buyle
 */
public class RuleSelectorView extends ViewImpl implements RuleSelectorPresenter.MyView {

	private SldEditorMessages sldEditorMessages = GWT.create(SldEditorMessages.class);
	
	private static final String NO_RULES_LOADED = "<i>Geen Stijlen ingeladen!</i>";
	private static final int 	INDEX_FIRST_RULE = 1;
	private static final String INDEX_FIRST_RULE_AS_STRING = String.valueOf(INDEX_FIRST_RULE);
	private static final int POSITION_IN_NODES_OF_FIRST_RULE = 0; // 0 if no folder nodes, 
									// in other words no grouping of rules
	// Widget stuff
	private VLayout vLayout;
	

	private final Label errorMessage;
	private final String ruleTitleUnspecified = sldEditorMessages.ruleTitleUnspecified();
	private final String ruleNameUnspecified = "";
	
	private TreeGrid treeGrid;

	private Tree tree;

	private HLayout toolStrip;

	private AddButton addButton;

	private RemoveButton removeRuleButton;

	private UpButton upButton;

	private DownButton downButton;

	private RuleTreeNode currentLeaf;

	private SelectRuleHandler selectRuleHandler;

	private UpdateRuleHeaderHandler updateRuleHeaderHandler;

	private GetCurrentRuleStateHandler getCurrentRuleStateHandler;

	private SldHasChangedHandler sldHasChangedHandler;

	private DynamicForm ruleGeneralForm;

	// ruleGeneralForm items
	private TextItem ruleTitleItem;

	//private TextItem ruleNameItem;

	private List<RuleModel> ruleList;
	
	
	private MyModel originalModel;
	private GeometryType currentGeomType = GeometryType.UNSPECIFIED;

	private EventBus eventBus;

	@Inject
	public RuleSelectorView(final EventBus eventBus, final ViewUtil viewUtil) {
		this.eventBus = eventBus;
		
		//myHandlerManager = new MyHandlerManager();

		errorMessage = new Label(NO_RULES_LOADED);
		errorMessage.setAlign(Alignment.CENTER);
		

		vLayout = new VLayout(5); 

		
		treeGrid = new TreeGrid();
		treeGrid.setWidth("100%");
		treeGrid.setHeight100();
		treeGrid.setMinHeight(70);
		// treeGrid.setShowEdges(true);
		// treeGrid.setBorder("2px solid #A7ABB4");
		treeGrid.setBodyStyleName("normal");
		treeGrid.setShowHeader(false);
		treeGrid.setEmptyMessage(sldEditorMessages.emptyRuleList());
				
		treeGrid.setPrompt("Selecteer de stijl die u in het detail venster wenst te bekijken of aanpassen."); // TODO:
																												// i18n

		TreeGridField titleField = new TreeGridField(RuleTreeNode.RULE_TITLE_FIELDNAME, "Titel");
		titleField.setCanSort(false);

		TreeGridField nameField = new TreeGridField(RuleTreeNode.RULE_NAME_FIELDNAME, "Naam");
		nameField.setEmptyCellValue(ruleNameUnspecified);
		treeGrid.setFields(titleField, nameField);
		treeGrid.setWrapCells(true);
		treeGrid.setFixedRecordHeights(false);
		// treeGrid.setCanDragResize(true);

		nameField.setCanSort(false);
		vLayout = new VLayout();
		vLayout.setSize("100%", "100%");
		vLayout.setLayoutTopMargin(10);
		vLayout.setLayoutLeftMargin(5);

		vLayout.setGroupTitle(sldEditorMessages.ruleOverviewGroupTitle());
		vLayout.setIsGroup(true);

		if (null != treeGrid) {
			treeGrid.addLeafClickHandler(new LeafClickHandler() {
				
				public void onLeafClick(LeafClickEvent event) {
					if (null != currentLeaf) {
						//TODO: update ??  E.g. let rule detail window call a listener 
						// to save its current state when it has to update its view 
						// to another rule
						//TODO: currentLeaf.setRuleData(getCurrentRuleStateHandler.execute());
					}

					currentLeaf = (RuleTreeNode) event.getLeaf();

					// Update Rule detail form item to show the first rule + inform listeners
					selectRule(currentLeaf.getRuleId(), currentLeaf.getTitle(), currentLeaf.getRuleName(),
							currentLeaf.getRuleData());

				}
			});
					
					
			treeGrid.addFolderClickHandler(new FolderClickHandler() {
				
				public void onFolderClick(FolderClickEvent event) {
					if (null != currentLeaf) {
						//TODO: update ??  E.g. let rule detail window call a listener 
						// itself to save its current state when it has to update its view 
						// to another rule

						//TODO: currentLeaf.setRuleData(getCurrentRuleStateHandler.execute());
					}

					setNoRuleSelected();
					
				}
			});
		}

		toolStrip = new HLayout(10/* membersMargin */);
		addButton = new AddButton();

		addButton.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				onAddRuleButtonClicked(event);
			}
		});

		toolStrip.addMember(addButton);

		removeRuleButton = new RemoveButton();

		removeRuleButton.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				onRemoveButtonClicked(event);

			} /* onClick */
		});
		toolStrip.addMember(removeRuleButton);

		upButton = new UpButton();

		upButton.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				onUpButtonClicked(event);
			}
		});

		toolStrip.addMember(upButton);

		downButton = new DownButton();

		downButton.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				onDownButtonClicked(event);
			}
		});

		toolStrip.addMember(downButton);

		toolStrip.setAlign(Alignment.LEFT);
		toolStrip.setPadding(1);

		ruleGeneralForm = new DynamicForm();
		ruleGeneralForm.setNumCols(4);

		ruleTitleItem = new TextItem("RuleTitle", sldEditorMessages.ruleTitleFieldTitle());
		ruleTitleItem.setWidth(200);
		ruleTitleItem.addChangedHandler(new ChangedHandler() {

			public void onChanged(ChangedEvent event) {
				if (null != currentLeaf) {
					String ruleTitle = null;
					if (null == event.getValue()) {
						ruleTitle = null;
					} else {
						ruleTitle = event.getValue().toString();
					}
					currentLeaf.setTitle(ruleTitle);
					refreshMinimal();
					// Update SLD object, assume currentLeaf != null
					//TODO: check if OK to remove test: if (null != currentLeaf.getRuleData()) 
					
					updateRuleHeaderHandler.updateTitle(ruleTitle);
					// TODO setSldHasChangedTrue in call-back
					
				}

			}
		});
		



// Remove Item to be able to update the rule name
//		
//		ruleNameItem = new TextItem("RuleName", "Naam");
//		ruleNameItem.setEmptyDisplayValue(ruleNameUnspecified);
//		ruleNameItem.setWidth(200);
//		ruleNameItem.addChangedHandler(new ChangedHandler() {
//
//			public void onChanged(ChangedEvent event) {
//				if (null != currentLeaf) {
//
//					String ruleName = null;
//					if (null == event.getValue()) {
//						ruleName = null;
//					} else {
//						ruleName = event.getValue().toString();
//					}
//					currentLeaf.setRuleName(ruleName);
//					refreshMinimal();
//					// Update SLD object
//					if (null != currentLeaf.getRuleData()) {
//						updateRuleHeaderHandler.execute(ruleTitleItem.getValueAsString(), ruleName);
//					}
//				}
//
//			}
//		});

		ruleGeneralForm.setItems(ruleTitleItem /*, ruleNameItem*/);

		vLayout.addMember(toolStrip);
		vLayout.addMember(treeGrid);
		vLayout.addMember(ruleGeneralForm);
		vLayout.addMember(errorMessage);
		errorMessage.hide();


	}

	// @Override
	public Widget asWidget() {
		return vLayout;
	}

//  @Override
//  public String getName() {
//    return nameField.getText();
//  }
//
//  @Override
//  public Button getSendButton() {
//    return sendButton;
//  }

//  @Override
//  public void resetAndFocus() {
//    // Focus the cursor on the name field when the app loads
//     nameOfLayerItem.setFocus(true);
////    nameField.selectAll();
//  }

//  @Override
	public void setError(String errorText) {
		errorMessage.setContents(null == errorText ? "" : errorText);
		if (null == errorText || errorText.isEmpty()) {
			errorMessage.hide();
		} else {
			errorMessage.show();
		}
		errorMessage.markForRedraw();
	}

//@Override
	public void copyToView(MyModel model) {
		originalModel = model;
		this.currentGeomType = GeometryType.UNSPECIFIED;

		if (null != ruleGeneralForm) {
			ruleGeneralForm.clearValues();
			ruleGeneralForm.disable();
		}

		List<RuleGroup> styleList = model.getRuleGroupList();
		if (styleList.size() > 1) {
			SC.warn("Meer dan 1 groep van regels (&lt;FeatureTypeStyle&gt;) in deze SLD." 
					+ "  Enkel de eerste wordt getoond.");
			// Can be supported later via groups of rules in ruleSelector
		}

		RuleGroup featureTypeStyle = styleList.iterator().next(); // retrieve the first <FeatureTypeStyle>
																				// element

		if (featureTypeStyle.getRuleModelList().size() < 1) {
			SC.warn("Fout: Geen rules in deze SLD.");
			return; // ABORT
		}

		ruleList = featureTypeStyle.getRuleModelList();

		// Setup the tree where each leaf node corresponds with 1 rule
		
		List<RuleTreeNode> children = new ArrayList<RuleTreeNode>();
		Integer i = INDEX_FIRST_RULE;
		
		for (RuleModel rule : ruleList) {
			
			if (null == rule.getTitle() || rule.getTitle().length() == 0) {
				//TODO: unexpected error
				SC.warn("Fout: rule zonder titel in deze SLD.");
				return; // ABORT
			}

			children.add(new RuleTreeNode(i.toString(), rule.getTitle(), rule.getName(), false, rule.getRuleData()));
			i++;
		}
		RuleTreeNode[] arrayOfRules = new RuleTreeNode[i - INDEX_FIRST_RULE];
		arrayOfRules = children.toArray(arrayOfRules);

		// TODO: support for multiple groups (corresp. with multiple FeatureTypeStyle elements)

		String styleTitle = featureTypeStyle.getTitle();
		if (null == styleTitle) {
			styleTitle = "groep 1"; // Should have been taken care of in Presenter
		}
//		final TreeNode root = new RuleTreeNode("root", "Root", "Root", true, null/* data */,
//				new RuleTreeNode[] { new RuleTreeNode("group 1", styleTitle, featureTypeStyle.getName(), true, null,
//						arrayOfRules) });

		final RuleTreeNode root = new RuleTreeNode("root", "Root", "Root", true, null/* data */,
						arrayOfRules );

		setRuleTree(root);

		currentLeaf = children.get(0); /* current leaf = also previous leaf for next call */

		//Update Rule detail form item to show the first rule 
		// + inform listeners (needed ?) 
		selectRule(currentLeaf.getRuleId(), currentLeaf.getTitle(), currentLeaf.getRuleName(), 
				currentLeaf.getRuleData());
	}

//@Override
	public void copyToModel(MyModel model) {
		// TODO: validate?
	}

		
	public void reset() {
		//clear 
		errorMessage.setContents(NO_RULES_LOADED);
		errorMessage.markForRedraw();

		// TODO: check
		makeRuleTreeEmpty();
		//TODO: ?? treeGrid.hide();
		
	}
	
	public void focus() {
//		topLevelAttributesForm.clearValues();
//		restoreFromOriginalModel();
		// TODO: Set focus on 1st rule ...

	}
		
		
	private void restoreFromOriginalModel() {
		copyToView(originalModel);
		
	}

	private  MyModel getModel() {
		return originalModel;
	}



	public void refresh() {

		treeGrid.setData(tree);
		treeGrid.getData().openAll();
		refreshMinimal();
	}
	
	public void refreshMinimal() {
		treeGrid.markForRedraw();
		vLayout.markForRedraw();
		//RuleSelector.this.markForRedraw();
	}

// TODO: doesn't belong here, for this class the type of the rule data should be of
//	no concern!
	
//	public boolean checkIfAllRulesComplete() {
//		boolean areAllRulesComplete = true;
//
//		Object[] rules = getAllRulesAsArray();
//		for (Object object : rules) {
//			if (object.getClass().equals(IncompleteRuleInfo.class)) {
//				areAllRulesComplete = false;
//				break;
//			}
//		}
//		return areAllRulesComplete;
//	}


	//--------------------------------------------------------------------------------------

	private void onRemoveButtonClicked(ClickEvent event) {
	
		final RuleTreeNode node = (RuleTreeNode) treeGrid.getSelectedRecord();

		if (node == null) {
			return;
		}
		if (node.isFolder()) {
			return; /* do nothing for a folder node */
		}
		SC.confirm(sldEditorMessages.confirmDeleteOfStyle(node.getTitle()),
				new BooleanCallback() {
	
					public void execute(Boolean value) {
						if (value != null && value) {
	
							// -- Remove node from the tree
							TreeNode[] nodes = tree.getAllNodes();
	
							int indexNode = nodes.length;
							String id = node.getRuleId();
							Integer idToRemoveAsInt = new Integer(id);
	
							RuleTreeNode parent = null;
							while (indexNode-- > 0) {
								if (id.equals(((RuleTreeNode) nodes[indexNode]).getRuleId())) {
									break;
								}
							}
				// Note: Commented out code below, it partly supports grouping of rules
	//								if (indexNode <= 0) {
	//									return; // ABORT
	//								}
	//								int indexParent = indexNode;
	//								while (indexParent-- > 0) {
	//									if (((RuleTreeNode) nodes[indexParent]).isFolder()) {
	//										parent = (RuleTreeNode) nodes[indexParent];
	//										break;
	//									}
	//								}
							parent = (RuleTreeNode) tree.getRoot();
							if (parent != null) {
								RuleTreeNode[] children;
	
								children = parent.getChildren();
								RuleTreeNode[] newChildren = new RuleTreeNode[children.length - 1];
								int j = 0;
								for (int i = 0; i < children.length; i++) {
									Integer idChildAsInt = new Integer(children[i].getRuleId());
	
									if (!idToRemoveAsInt.equals(idChildAsInt)) {
										if (idChildAsInt > idToRemoveAsInt) {
											children[i].setRuleId(String.valueOf(idChildAsInt - 1));
										}
										newChildren[j] = children[i];
										j++;
									}
								}
	
								parent.setChildren(newChildren);
								refresh();
								int indexSelect = indexNode;
								
								if (indexSelect >= nodes.length - 1) {
									indexSelect--;
								}
								treeGrid.selectSingleRecord(indexSelect);
								updateButtons();
	
								currentLeaf = (RuleTreeNode) treeGrid.getSelectedRecord();
								sldHasChanged();
	
								removeRule(node.getRuleId());
								if (null != currentLeaf && !currentLeaf.isFolder()) {
									//Update Rule detail form item to show the first rule 
									// + inform listeners  
									selectRule(currentLeaf.getRuleId(), currentLeaf.getTitle(),
											currentLeaf.getRuleName(), currentLeaf.getRuleData());
								} else {
									setNoRuleSelected();
								}
	
							} /* parent != null */
	
						}
					} /* execute */
				});
	}

	//--------------------------------------------------------------------------------------

	private void onAddRuleButtonClicked(ClickEvent event) {
		if (null != currentLeaf) {
			//TODO: update ??  E.g. let rule detail window call a listener 
			// itself to save its current state when it has to update its view 
			// to another rule

			//TODO: currentLeaf.setRuleData(getCurrentRuleStateHandler.execute());
		}

		RuleTreeNode newLeaf = new RuleTreeNode(getNewIdForRuleInTree(), "nieuwe stijl"/* title */,
				ruleNameUnspecified/* name */, false/* isFolder */, null/* ruleData */);
		// -- add newLeaf at the end of the tree

// Note: Commented out code below, it partly supports grouping of rules
//		TreeNode[] nodes = tree.getAllNodes();

//		int index = nodes.length;
//
//		RuleTreeNode parent = null;
//		while (index-- > 0) {
//			if (((RuleTreeNode) nodes[index]).isFolder()) {
//				parent = (RuleTreeNode) nodes[index];
//				break;
//			}
//		}
		RuleTreeNode parent = (RuleTreeNode) tree.getRoot();
		if (parent != null) {
			RuleTreeNode[] children;

			children = parent.getChildren();
			RuleTreeNode[] newChildren = new RuleTreeNode[children.length + 1];
			for (int i = 0; i < children.length; i++) {
				newChildren[i] = children[i];
			}
			newChildren[children.length] = newLeaf;
			parent.setChildren(newChildren);
			refresh();
			treeGrid.selectSingleRecord(children.length + POSITION_IN_NODES_OF_FIRST_RULE); 
						/* assuming only 1 group */
			updateButtons();
		}


		RuleModel defaultRuleModel = RuleModel.CreateDefaultRuleModel(
				currentGeomType.equals(GeometryType.UNSPECIFIED) ? getModel().getGeomType() : currentGeomType);
		
		// TODO: avoid updating RuleData here
		// TODO!!: Inform listener of creation of a new rule
		newLeaf.setRuleData(defaultRuleModel.getRuleData());
		newLeaf.setTitle(defaultRuleModel.getTitle());
		newLeaf.setRuleName(defaultRuleModel.getName());
		
		
		ruleList.add(defaultRuleModel); /* add new rule add the end of the list */

		currentLeaf = newLeaf;

		sldHasChanged();
		//Update Rule detail form item to show the first rule 
		// + inform listeners  
		selectRule(currentLeaf.getRuleId(), currentLeaf.getTitle(), currentLeaf.getRuleName(),
						currentLeaf.getRuleData());

	}

	//--------------------------------------------------------------------------------------
	private void onUpButtonClicked(ClickEvent event) {
		final RuleTreeNode node = (RuleTreeNode) treeGrid.getSelectedRecord();

		if (node == null) {
			return;
		}
		if (node.isFolder()) {
			return;
		}

		// -- Move node from 1 position up in the tree
		TreeNode[] nodes = tree.getAllNodes();

		int indexNode = nodes.length;
		String id = node.getRuleId();

	// Note: Commented out code below, it partly supports grouping of rules				
//		RuleTreeNode parent = null;
		while (indexNode-- > 0) {
			if (id.equals(((RuleTreeNode) nodes[indexNode]).getRuleId())) {
				break;
			}
		}
		if (indexNode < POSITION_IN_NODES_OF_FIRST_RULE) {
			return; // ABORT
		}

//		int indexParent = indexNode;
//		while (indexParent-- > 0) {
//			if (((RuleTreeNode) nodes[indexParent]).isFolder()) {
//				parent = (RuleTreeNode) nodes[indexParent];
//				break;
//			}
//		}
		RuleTreeNode parent = (RuleTreeNode) tree.getRoot();
		
		if (parent != null) {
			RuleTreeNode[] children;
			int indexSelect = indexNode;
			children = parent.getChildren();
			for (int i = 0; i < children.length; i++) {
				if (id.equals(((RuleTreeNode) children[i]).getRuleId())) {
					if (i > 0) {
						indexSelect--;
						RuleTreeNode swap = children[i - 1];
						children[i - 1] = children[i];
						children[i] = swap;
						children[i].setRuleId(id);
					}
				}
			}

			parent.setChildren(children);
			refresh();

			treeGrid.selectSingleRecord(indexSelect);

			moveRuleUp(id);

			node.setRuleId(String.valueOf(new Integer(id) - 1));
			updateButtons();
		}
	}

	//--------------------------------------------------------------------------------------
	private void onDownButtonClicked(ClickEvent event) {
		final RuleTreeNode node = (RuleTreeNode) treeGrid.getSelectedRecord();

		if (node == null) {
			return;
		}
		if (node.isFolder()) {
			return;
		}

		// -- Move node from 1 position up in the tree
		TreeNode[] nodes = tree.getAllNodes();

		int indexNode = nodes.length;
		String id = node.getRuleId();

		
		while (indexNode-- > 0) {
			if (id.equals(((RuleTreeNode) nodes[indexNode]).getRuleId())) {
				break;
			}
		}
		if (indexNode >= nodes.length - 1) { /* the last node cannot be moved down */
			return; // ABORT
		}
		
	// Note: Commented out code below, it partly supports grouping of rules				
//		RuleTreeNode parent = null;
//		int indexParent = indexNode;
//		while (indexParent-- > 0) {
//			if (((RuleTreeNode) nodes[indexParent]).isFolder()) {
//				parent = (RuleTreeNode) nodes[indexParent];
//				break;
//			}
//		}
		RuleTreeNode parent = (RuleTreeNode) tree.getRoot(); 
		
		if (parent != null) {
			RuleTreeNode[] children;
			int indexSelect = indexNode;
			children = parent.getChildren();
			for (int i = 0; i < children.length; i++) {
				if (id.equals(((RuleTreeNode) children[i]).getRuleId())) {
					if (i < children.length - 1) {
						indexSelect++; /* new index of selected node */
						RuleTreeNode swap = children[i + 1];
						children[i + 1] = children[i];
						children[i] = swap;
						children[i].setRuleId(id);
						break;
					}
				}
			}

			parent.setChildren(children);
			refresh();

			treeGrid.selectSingleRecord(indexSelect);

			moveRuleDown(id); /* still with the old ruleId */

			node.setRuleId(String.valueOf(new Integer(id) + 1));
			updateButtons();

		}
	}
	
	
	//--------------------------------------------------------------------------------------
	private void selectRule(String ruleID, String ruleTitle, String ruleName, RuleData ruleData) {

		updateButtons();

		if (null != ruleGeneralForm) {

			ruleTitleItem.setValue(ruleTitle);
			//ruleNameItem.setValue(ruleName);
			ruleGeneralForm.enable();
			ruleGeneralForm.show();
		}

	// TODO: move code below to a more appropriate place
//		if (ruleData.getClass().equals(RuleInfo.class)) {
//			this.currentGeomType = SldUtils.getGeometryType((RuleInfo) ruleData);
//		}

//		if (null != selectRuleHandler) {
//			selectRuleHandler.execute(true, currentLeaf.getRuleData());
//		}
		
//		for (SelectorChangeHandler  listener : myHandlerManager.getListeners() ) {
//			listener.onChange(new Integer(ruleID), (RuleData)ruleData);
//		}
		//Inform observer(s) of change of selected rule
		RuleSelectedEvent.fire(RuleSelectorView.this, ruleData);
		

	}
	//--------------------------------------------------------------------------------------
	private void setNoRuleSelected() {
		currentLeaf = null;

		updateButtons();
		if (null != ruleGeneralForm) {
			ruleGeneralForm.clearValues();
			ruleGeneralForm.disable();
		}
		RuleSelectedEvent.fire(this, null);
	}
	//--------------------------------------------------------------------------------------
	private void setRuleTree(RuleTreeNode root) {

		if (null == tree) {
			tree = new Tree();
			tree.setModelType(TreeModelType.CHILDREN);

			tree.setNameProperty(RuleTreeNode.RULE_ID_FIELDNAME); /* must be unique name amongst siblings */
			tree.setIdField(RuleTreeNode.RULE_ID_FIELDNAME); /* unique ID within the tree */
			tree.setTitleProperty(RuleTreeNode.RULE_TITLE_FIELDNAME);
			tree.setChildrenProperty(RuleTreeNode.CHILDREN_FIELDNAME);
			tree.setIsFolderProperty(RuleTreeNode.IS_FOLDER_FIELDNAME);

			tree.setShowRoot(false);
		}
		tree.setRoot(root);

		treeGrid.setData(tree);
		treeGrid.getData().openAll();
		
		treeGrid.selectSingleRecord(POSITION_IN_NODES_OF_FIRST_RULE );
		
		updateButtons();

		treeGrid.markForRedraw();

		vLayout.markForRedraw();
		//RuleSelector.this.markForRedraw();

	}

	// --------------------------------------------------------------------------------------
	//--------------------------------------------------------------------------------------
//	private Object[] getAllRulesAsArray() {
//
//		Tree tree = treeGrid.getData();
//		List<Object> ruleList = new ArrayList<Object>();
//
//		if (null != tree) {
//			for (TreeNode node : tree.getAllNodes()) {
//				if (!((RuleTreeNode) node).isFolder()) {
//					Object ruleData = ((RuleTreeNode) node).getRuleData(); /*
//																			 * will not be null for leaf nodes, except
//																			 * for default rule ??
//																			 */
//					if (null == ruleData) {
//						SC.warn("TODO");
//					}
//					ruleList.add(ruleData);
//				}
//			}
//		}
//
//		return ruleList.toArray();
//
//	}
	//--------------------------------------------------------------------------------------
	private void makeRuleTreeEmpty() {
		setRuleTree(new RuleTreeNode("root", "Root", null/* name */, true/* isFolder */, null, new RuleTreeNode[] {}));
	}
	//--------------------------------------------------------------------------------------
	private String getNewIdForRuleInTree() {
		String newId;
		TreeNode[] nodes = tree.getAllNodes();
		int index = nodes.length;

		// Assume only 1 group
		if (nodes.length == 0 || (nodes.length == 1 && ((RuleTreeNode) nodes[0]).isFolder())) { // no rules,
												// possibly 1 folder(group) node
																				
			newId = INDEX_FIRST_RULE_AS_STRING;
		} else {
			String lastId = ((RuleTreeNode) nodes[index - 1]).getRuleId();
			newId = String.valueOf(new Integer(lastId) + 1);
		}
		return newId;
	}
	//--------------------------------------------------------------------------------------
	private void removeRule(String ruleId) {
		/* the ruleId == index of the rule in the ruleList */

		sldHasChanged();
		ruleList.remove(new Integer(ruleId) - INDEX_FIRST_RULE); /* remove the rule at position ruleId */
	}
	//--------------------------------------------------------------------------------------
	private void moveRuleUp(String ruleId) {
		/* the ruleId == index of the rule in the ruleList before it has been moved up */
		if (new Integer(ruleId) <= INDEX_FIRST_RULE) {
			return;
		}
		sldHasChanged();
		RuleModel ruleToSwap = ruleList.get(new Integer(ruleId) - INDEX_FIRST_RULE - 1);
		ruleList.set(new Integer(ruleId) - INDEX_FIRST_RULE - 1, ruleList.get(new Integer(ruleId) - INDEX_FIRST_RULE));
		ruleList.set(new Integer(ruleId) - INDEX_FIRST_RULE, ruleToSwap);
	}
	//--------------------------------------------------------------------------------------
	private void moveRuleDown(String ruleId) {
		/* the ruleId == index of the rule in the ruleList before it has been moved down */

		if (new Integer(ruleId) >= ruleList.size()) {
			return;
		}
		sldHasChanged();

		RuleModel ruleToSwap = ruleList.get(new Integer(ruleId) - INDEX_FIRST_RULE + 1);
		ruleList.set(new Integer(ruleId), ruleList.get(new Integer(ruleId) - INDEX_FIRST_RULE));
		ruleList.set(new Integer(ruleId) - INDEX_FIRST_RULE, ruleToSwap);
	}

	// -------------------------------------------------------------------------
	// Private class AddButton:
	// -------------------------------------------------------------------------

	/** Definition of the Add button. */
	private class AddButton extends IButton {
		public static final String ICON = "[ISOMORPHIC]/" + "geomajas/icons/silk/add.png";
		
		public AddButton() {
			setIcon(ICON);
			setShowDisabledIcon(true);
			setPrompt("Voeg nieuwe stijl toe"); // TODO i18n
			// TODO: validate form first
			setWidth(24);
			setDisabled(false);

			// TODO: setTitle(I18nProvider.getAttribute().btnSaveTitle());
			// TODO: setTooltip(I18nProvider.getAttribute().btnSaveTooltip());

		}
	}

	// -------------------------------------------------------------------------
	// Private class RemoveButton:
	// -------------------------------------------------------------------------

	/** Definition of the Remove button. */
	private class RemoveButton extends IButton {
		public static final String ICON = "[ISOMORPHIC]/" + "geomajas/icons/silk/cancel.png";
		
		public RemoveButton() {
			setIcon(ICON);
			setShowDisabledIcon(true);
			setPrompt("Verwijder geselecteerde stijl"); // TODO i18n
			setWidth(24);
			setDisabled(false);

			// TODO: setTitle(I18nProvider.getAttribute().btnSaveTitle());
			// TODO: setTooltip(I18nProvider.getAttribute().btnSaveTooltip());

		}
	}

	// -------------------------------------------------------------------------
	// Private class UpButton:
	// -------------------------------------------------------------------------

	/** Definition of the Up button. */
	private class UpButton extends IButton {

		public static final String ICON_ARROW_UP = "[ISOMORPHIC]/" + "geomajas/icons/silk/arrow_up.png";

		public UpButton() {

			setIcon(ICON_ARROW_UP);
			setShowDisabledIcon(true);
			setPrompt("Plaats geselecteerde stijl 1 positie hoger"); // TODO i18n
			setWidth(24);
			setDisabled(false);

			// TODO: setTitle(I18nProvider.getAttribute().btnSaveTitle());
			// TODO: setTooltip(I18nProvider.getAttribute().btnSaveTooltip());

		}
	}

	// -------------------------------------------------------------------------
	// Private class DownButton:
	// -------------------------------------------------------------------------

	/** Definition of the Up button. */
	private class DownButton extends IButton {

		public static final String ICON_ARROW_DOWN = "[ISOMORPHIC]/" + "geomajas/icons/silk/arrow_down.png";

		public DownButton() {

			setIcon(ICON_ARROW_DOWN);
			setShowDisabledIcon(true);
			setPrompt("Plaats geselecteerde stijl 1 positie lager"); // TODO i18n
			setWidth(24);
			setDisabled(false);

			// TODO: setTitle(I18nProvider.getAttribute().btnSaveTitle());
			// TODO: setTooltip(I18nProvider.getAttribute().btnSaveTooltip());

		}
	}

	private void updateButtons() {
		final RuleTreeNode node = (RuleTreeNode) treeGrid.getSelectedRecord();

		if (node == null) {
			removeRuleButton.setDisabled(true);
			upButton.setDisabled(true);
			downButton.setDisabled(true);
		} else if (node.isFolder()) { /* a folder node */
			removeRuleButton.setDisabled(true);
			upButton.setDisabled(true);
			downButton.setDisabled(true);
		} else {
			removeRuleButton.setDisabled(false);
			upButton.setDisabled(INDEX_FIRST_RULE_AS_STRING.equals(node.getRuleId())/* first leaf node */);
			TreeNode[] nodes = tree.getAllNodes();

			// Test if ID equals that one of the last leaf node
			boolean disable = node.getRuleId().equals(((RuleTreeNode) nodes[nodes.length - 1]).getRuleId());
			downButton.setDisabled(disable);
		}

		if (null == ruleList) {
			addButton.disable();
		} else {
			addButton.enable();
		}

	}

	private void sldHasChanged() {
		if (null != sldHasChangedHandler) {
			sldHasChangedHandler.execute();
		}
	}

	public void fireEvent(GwtEvent<?> event) {
		eventBus.fireEvent(event);
	}

	
	public HandlerRegistration addRuleSelectedHandler(RuleSelectedHandler handler) {
		return eventBus.addHandler(RuleSelectedEvent.getType(), handler);
	}
	


}
