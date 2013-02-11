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
package org.geomajas.widget.layer.editor.client;

import org.geomajas.gwt.client.util.Notify;
import org.geomajas.widget.layer.client.LayerMessages;
import org.geomajas.widget.layer.configuration.client.ClientBranchNodeInfo;

import com.google.gwt.core.client.GWT;
import com.smartgwt.client.types.DragDataAction;
import com.smartgwt.client.types.ListGridFieldType;
import com.smartgwt.client.types.SelectionStyle;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.util.ValueCallback;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.menu.Menu;
import com.smartgwt.client.widgets.menu.MenuItem;
import com.smartgwt.client.widgets.menu.MenuItemIfFunction;
import com.smartgwt.client.widgets.menu.events.ClickHandler;
import com.smartgwt.client.widgets.menu.events.MenuItemClickEvent;
import com.smartgwt.client.widgets.tree.TreeGrid;
import com.smartgwt.client.widgets.tree.TreeGridField;
import com.smartgwt.client.widgets.tree.TreeNode;

/**
 * Used by LayerSelectPanel, this is one Layertree.
 * 
 * @author Kristof Heirwegh
 */
public class LayerTreeGrid extends TreeGrid {

	private static final LayerMessages MESSAGES = GWT.create(LayerMessages.class);

	private final Menu menu = new Menu();

	private final MenuItem menuMapAdd = new MenuItem(MESSAGES.layerTreegridCreateMap());

	private final MenuItem menuMapDelete = new MenuItem(MESSAGES.layerTreegridRemoveMap());

	public LayerTreeGrid(String title, boolean editable) {
		super();
		setWidth100();
		setHeight100();
		setShowRoot(false);
		if (editable) {
			setShowRollOverCanvas(true);
			setCanSort(false); // this messes things up
		} else {
			setShowRollOverCanvas(false);
		}
		setCanReorderRecords(true);
		setDragDataAction(DragDataAction.MOVE);
		setCanAcceptDroppedRecords(true);
		setCanDragRecordsOut(true);
		setSelectionType(SelectionStyle.MULTIPLE);
		setShowAllRecords(true);

		TreeGridField nameFld = new TreeGridField(LayerTreeNode.FLD_NAME);
		nameFld.setWidth("*");
		nameFld.setTitle(title);

		TreeGridField publicFld = new TreeGridField(LayerTreeNode.FLD_PUBLIC, MESSAGES.layerTreegridColumnPublic());
		publicFld.setType(ListGridFieldType.BOOLEAN);
		publicFld.setWidth(90);
		publicFld.setPrompt(MESSAGES.layerTreegridColumnPublicTooltip());

		setFields(nameFld, publicFld);

		// -- Context menu --------------------------------------------------------

		menuMapAdd.addClickHandler(new ClickHandler() {

			public void onClick(MenuItemClickEvent event) {
				mapAdd();
			}
		});
		menuMapDelete.addClickHandler(new ClickHandler() {

			public void onClick(MenuItemClickEvent event) {
				mapDelete();
			}
		});
		menuMapDelete.setEnableIfCondition(new MenuItemIfFunction() {

			public boolean execute(Canvas target, Menu menu, MenuItem item) {
				if (item.equals(menuMapDelete)) {
					LayerTreeNode node = (LayerTreeNode) getSelectedRecord();
					if (node == null) {
						return false;
					}
					return !getTree().isLeaf(node);
				} else {
					return true;
				}
			}
		});

		menu.addItem(menuMapAdd);
		menu.addItem(menuMapDelete);
		setContextMenu(menu);
	}

	private void mapDelete() {
		menu.hideContextMenu();
		LayerTreeNode node = (LayerTreeNode) getSelectedRecord();
		if (node != null && !getTree().isLeaf(node)) {
			TreeNode[] children = getTree().getChildren(node);
			if (children != null && children.length > 0) {
				getTree().addList(children, getTree().getRoot());
			}
			getTree().remove(node);
		} else {
			Notify.error(MESSAGES.layerTreegridRemoveMapErrorNoSelection());
		}
	}

	private void mapAdd() {
		menu.hideContextMenu();
		SC.askforValue(MESSAGES.layerTreegridCreateMapAskValue(), new ValueCallback() {

			public void execute(String value) {
				if ((value != null) && (!"".equals(value.trim()))) {
					ClientBranchNodeInfo newNode = new ClientBranchNodeInfo();
					newNode.setLabel(value);
					LayerTreeNode mapNode = new LayerTreeNode(newNode);
					mapNode.setAttribute(LayerTreeNode.FLD_PUBLIC, true);
					getTree().add(mapNode, getClosestFolder((LayerTreeNode) getSelectedRecord()));
				}
			}
		});
	}

	private LayerTreeNode getClosestFolder(LayerTreeNode node) {
		LayerTreeNode parent = node;
		while (parent != null) {
			if (getTree().isLeaf(parent)) {
				parent = (LayerTreeNode) getTree().getParent(parent);
			} else {
				return parent;
			}
		}
		return (LayerTreeNode) getTree().getRoot();
	}
}
