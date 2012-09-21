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
package org.geomajas.plugin.deskmanager.client.gwt.manager.common;

import org.geomajas.plugin.deskmanager.client.gwt.common.DeskmanagerIcon;
import org.geomajas.plugin.deskmanager.client.gwt.geodesk.widget.infowindow.NotificationWindow;
import org.geomajas.plugin.deskmanager.client.gwt.manager.common.LayerTreeSelectPanel.LayerTreeNode;
import org.geomajas.plugin.deskmanager.domain.dto.LayerTreeNodeDto;

import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.DragDataAction;
import com.smartgwt.client.types.ListGridFieldType;
import com.smartgwt.client.types.SelectionStyle;
import com.smartgwt.client.util.BooleanCallback;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.util.ValueCallback;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.ImgButton;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.layout.HLayout;
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

	private HLayout rollOverCanvas;

	private LayerGroupConfigurationWindow layerGroupConfWi;

	private final Menu menu = new Menu();

	private final MenuItem menuMapAdd = new MenuItem("Nieuwe map maken");

	private final MenuItem menuMapDelete = new MenuItem("Map verwijderen");

	private TreeGrid sourceTreeGrid;

	private LayerTreeNode rollOverNode;

	private Integer rowNumber;

	private boolean allowFolders;

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

		TreeGridField publicFld = new TreeGridField(LayerTreeNode.FLD_PUBLIC, "Publiek");
		publicFld.setType(ListGridFieldType.BOOLEAN);
		publicFld.setWidth(90);
		publicFld.setPrompt("Aan: laag kan geraadpleegd worden in een publiek loket.");

		setFields(nameFld, publicFld);

		// -- Context menu --------------------------------------------------------

		menuMapAdd.addClickHandler(new ClickHandler() {

			public void onClick(MenuItemClickEvent event) {
				mapAdd();
			}
		});
		menuMapAdd.setEnableIfCondition(new MenuItemIfFunction() {

			public boolean execute(Canvas target, Menu menu, MenuItem item) {
				return allowFolders;
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

	@Override
	protected Canvas getRollOverCanvas(Integer rowNum, Integer colNum) {
		rollOverNode = (LayerTreeNode) this.getRecord(rowNum);
		rowNumber = rowNum;

		if (rollOverCanvas == null) {
			rollOverCanvas = new HLayout(3);
			rollOverCanvas.setSnapTo("TR");
			rollOverCanvas.setWidth(22);
			rollOverCanvas.setHeight(22);

			ImgButton editProps = new ImgButton();
			editProps.setShowDown(false);
			editProps.setShowRollOver(false);
			editProps.setLayoutAlign(Alignment.CENTER);
			editProps.setSrc(DeskmanagerIcon.IMG_SRC_COG);
			editProps.setPrompt("Configureren");
			editProps.setShowDisabledIcon(false);
			editProps.setHeight(16);
			editProps.setWidth(16);
			editProps.addClickHandler(new com.smartgwt.client.widgets.events.ClickHandler() {

				public void onClick(ClickEvent event) {
					if (getTree().isLeaf(rollOverNode)) {
						configureLayer(rollOverNode, rowNumber);
					} else {
						configureLayerGroup(rollOverNode, rowNumber);
					}
				}
			});
			rollOverCanvas.addMember(editProps);
		}
		return rollOverCanvas;
	}

	public void setSourceTreeGrid(TreeGrid treeGrid) {
		this.sourceTreeGrid = treeGrid;
	}

	public boolean isAllowFolders() {
		return allowFolders;
	}

	public void setAllowFolders(boolean allowFolders) {
		this.allowFolders = allowFolders;
	}

	// ----------------------------------------------------------

	private void configureLayer(LayerTreeNode node, int rowNum) {
//		final LayerTreeNode innerNode = node;
//		final int rowNumber = rowNum;
//
//		if (layerConfWi == null) {
//			layerConfWi = new LayerConfigurationWindow();
//		}
//		layerConfWi.show(innerNode.getNode(), new BooleanCallback() {
//
//			public void execute(Boolean saved) {
//				if (saved) {
//					innerNode.setAttribute(LayerTreeNode.FLD_NAME, innerNode.getNode().getName());
//					refreshRow(rowNumber);
//				}
//			}
//		});
	}

	private void configureLayerGroup(LayerTreeNode node, int rowNum) {
		final LayerTreeNode innerNode = node;
		final int rowNumber = rowNum;

		if (layerGroupConfWi == null) {
			layerGroupConfWi = new LayerGroupConfigurationWindow();
		}
		layerGroupConfWi.show(innerNode.getNode(), new BooleanCallback() {

			public void execute(Boolean saved) {
				if (saved) {
					innerNode.setAttribute(LayerTreeNode.FLD_NAME, innerNode.getNode().getNodeName());
					refreshRow(rowNumber);
				}
			}
		});
	}

	private void mapDelete() {
		menu.hideContextMenu();
		LayerTreeNode node = (LayerTreeNode) getSelectedRecord();
		if (node != null && !getTree().isLeaf(node)) {
			TreeNode[] children = getTree().getChildren(node);
			if (children != null && children.length > 0) {
				if (sourceTreeGrid != null) {
					sourceTreeGrid.getTree().addList(children, sourceTreeGrid.getTree().getRoot());
				} else {
					getTree().addList(children, getTree().getRoot());
				}
			}
			getTree().remove(node);
		} else {
			NotificationWindow.showErrorMessage("Gelieve een map te selecteren vooraleer"
					+ " de actie \"Map Verwijderen\" uit te voeren.");
		}
	}

	private void mapAdd() {
		menu.hideContextMenu();
		SC.askforValue("Gelieve de naam voor de nieuwe map in te geven.", new ValueCallback() {

			public void execute(String value) {
				if ((value != null) && (!"".equals(value.trim()))) {
					LayerTreeNode mapNode = new LayerTreeNode(new LayerTreeNodeDto(value.trim(), false));
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
