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
package org.geomajas.plugin.deskmanager.client.gwt.manager.common;

import org.geomajas.gwt.client.util.WidgetLayout;
import org.geomajas.plugin.deskmanager.client.gwt.manager.i18n.ManagerMessages;

import com.google.gwt.core.client.GWT;
import com.smartgwt.client.types.Overflow;
import com.smartgwt.client.types.TitleOrientation;
import com.smartgwt.client.util.BooleanCallback;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.Window;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.CheckboxItem;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;

/**
 * @author Kristof Heirwegh
 */
public class LayerGroupConfigurationWindow extends Window {

	private static final ManagerMessages MESSAGES = GWT.create(ManagerMessages.class);
	
	private static final int FORMITEM_WIDTH = 300;
	public static final String FLD_NAME = "Name";

	// private LayerTreeNodeDto layerGroup;

	private BooleanCallback callback;

	private DynamicForm form;

	private TextItem name;

	private CheckboxItem expanded;

	/**
	 * @param layerGroup
	 * @param callback
	 *            returns true if saved, false if canceled.
	 */
	public LayerGroupConfigurationWindow() {
		setAutoSize(true);
		setCanDragReposition(true);
		setCanDragResize(false);
		setKeepInParentRect(true);
		setOverflow(Overflow.HIDDEN);
		setAutoCenter(true);
		setTitle(MESSAGES.layerGroupConfigWindowTitle());
		setShowCloseButton(false);
		setShowMinimizeButton(false);
		setShowMaximizeButton(false);
		setIsModal(true);
		setShowModalMask(true);

		form = new DynamicForm();
		form.setIsGroup(true);
		form.setGroupTitle(MESSAGES.layerGroupConfigGroupTitleProperties());
		form.setPadding(5);
		form.setAutoWidth();
		form.setAutoFocus(true); /* Set focus on first field */
		form.setNumCols(2);
		form.setTitleOrientation(TitleOrientation.LEFT);

		name = new TextItem(FLD_NAME);
		name.setTitle(MESSAGES.layerGroupConfigName());
		name.setRequired(true);
		name.setWidth(FORMITEM_WIDTH);
		name.setWrapTitle(false);
		name.setTooltip(MESSAGES.layerGroupConfigNameTooltip());

		expanded = new CheckboxItem();
		expanded.setTitle(MESSAGES.layerGroupConfigOpenedAtStart());
		expanded.setWrapTitle(false);
		expanded.setTooltip(MESSAGES.layerGroupConfigOpenedAtStartTooltip());

		form.setFields(name, expanded);

		// ----------------------------------------------------------

		HLayout buttons = new HLayout(10);
		IButton save = new IButton(MESSAGES.saveButtonText());
		save.setIcon(WidgetLayout.iconSave);
		save.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				// saved();
			}
		});
		IButton cancel = new IButton(MESSAGES.cancelButtonText());
		cancel.setIcon(WidgetLayout.iconCancel);
		cancel.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				cancelled();
			}
		});
		buttons.addMember(save);
		buttons.addMember(cancel);

		// ----------------------------------------------------------

		VLayout vl = new VLayout(10);
		vl.setMargin(10);
		vl.addMember(form);
		vl.addMember(buttons);
		addItem(vl);
	}

	// public void show(LayerTreeNodeDto layerGroup, BooleanCallback callback) {
	// if (layerGroup == null || layerGroup.isLeaf()) {
	// throw new IllegalArgumentException("Please provide a non-leaf LayerTreeNode");
	// }
	//
	// this.callback = callback;
	// this.layerGroup = layerGroup;
	//
	// form.clearValues();
	// name.setValue(layerGroup.getNodeName());
	// expanded.setValue(layerGroup.isExpanded());
	//
	// show();
	// }

	private void cancelled() {
		hide();
		if (callback != null) {
			callback.execute(false);
		}
	}

	// private void saved() {
	// if (form.validate()) {
	// layerGroup.setName(name.getValueAsString());
	// layerGroup.setExpanded(expanded.getValueAsBoolean());
	//
	// hide();
	// if (callback != null) {
	// callback.execute(true);
	// }
	// }
	// }
}
