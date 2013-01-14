/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2013 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the Apache
 * License, Version 2.0. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */
package org.geomajas.sld.editor.client.view;

import org.geomajas.sld.editor.common.client.presenter.StyledLayerDescriptorLayoutPresenter;

import com.google.gwt.user.client.ui.Widget;
import com.gwtplatform.mvp.client.ViewImpl;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.layout.Layout;
import com.smartgwt.client.widgets.layout.VLayout;

/**
 * Main view with content slots for child presenters.
 * 
 * @author An Buyle
 * @author Jan De Moerloose
 * 
 */
public class StyledLayerDescriptorLayoutView extends ViewImpl implements StyledLayerDescriptorLayoutPresenter.MyView {

	private VLayout generalContentPanel;

	private VLayout rulesContentPanel;

	private VLayout ruleContentPanel;

	private VLayout actionContentPanel;

	private VLayout widget;

	public StyledLayerDescriptorLayoutView() {

		generalContentPanel = new VLayout();
		generalContentPanel.setWidth100();
		// minimal initial height
		generalContentPanel.setHeight(10);

		rulesContentPanel = new VLayout();
		rulesContentPanel.setWidth100();
		// take all the remaining space
		rulesContentPanel.setHeight("*");
		rulesContentPanel.setResizeBarTarget("next");
		rulesContentPanel.setShowResizeBar(true);

		ruleContentPanel = new VLayout();
		ruleContentPanel.setWidth100();
		// minimal initial height
		ruleContentPanel.setHeight(300);

		actionContentPanel = new VLayout();
		actionContentPanel.setWidth100();
		actionContentPanel.setHeight(10);

		widget = new VLayout();
		widget.setWidth100();
		widget.setHeight100();
		widget.setMembersMargin(5);
		widget.setMargin(5);
		widget.addMember(generalContentPanel);
		widget.addMember(rulesContentPanel);
		widget.addMember(ruleContentPanel);
		widget.addMember(actionContentPanel);

	}

	public Widget asWidget() {
		return widget;
	}

	// GWTP will call setInSlot when a child presenter asks to be added under
	// this view.
	@Override
	public void setInSlot(Object slot, Widget content) {
		if (slot == StyledLayerDescriptorLayoutPresenter.TYPE_GENERAL_CONTENT) {
			replaceContent(generalContentPanel, (Canvas) content);
		} else if (slot == StyledLayerDescriptorLayoutPresenter.TYPE_RULES_CONTENT) {
			replaceContent(rulesContentPanel, (Canvas) content);
		} else if (slot == StyledLayerDescriptorLayoutPresenter.TYPE_RULE_CONTENT) {
			replaceContent(ruleContentPanel, (Canvas) content);
		} else if (slot == StyledLayerDescriptorLayoutPresenter.TYPE_ACTION_CONTENT) {
			replaceContent(actionContentPanel, (Canvas) content);
		} else {
			super.setInSlot(slot, content);
		}
	}

	private void replaceContent(Layout layout, Canvas content) {
		if (content != null) {
			content.setWidth100();
			content.setHeight100();
			if (layout.hasMember(content)) {
				content.setVisible(true);
			} else {
				layout.addMember(content);
				//TODO: needed ?  content.setVisible(true);
			}
		}
	}

	public void showLoading(boolean visibile) {
		// TODO Auto-generated method stub

	}

	public boolean isLoaded() {
		return (generalContentPanel.getMembers().length > 0) &&  rulesContentPanel.getMembers().length > 0
				&&	ruleContentPanel.getMembers().length > 0 && (actionContentPanel.getMembers().length > 0 
		);
	}

	public void redraw() {
		widget.markForRedraw();
		
	}
}