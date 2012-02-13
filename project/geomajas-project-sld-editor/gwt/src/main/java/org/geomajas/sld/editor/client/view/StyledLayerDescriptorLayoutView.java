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

import org.geomajas.sld.client.presenter.StyledLayerDescriptorLayoutPresenter;

import com.google.gwt.user.client.ui.Widget;
import com.gwtplatform.mvp.client.ViewImpl;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.layout.VLayout;
/**
 * Main view with content slots for child presenters.
 * 
 * @author Jan De Moerloose
 * 
 */
public class StyledLayerDescriptorLayoutView extends ViewImpl implements StyledLayerDescriptorLayoutPresenter.MyView {

	private VLayout generalContentPanel;

	private VLayout rulesContentPanel;

	private VLayout ruleContentPanel;

	private VLayout widget;

	public StyledLayerDescriptorLayoutView() {

		generalContentPanel = new VLayout();
		generalContentPanel.setWidth100();
		generalContentPanel.setShowResizeBar(true);

		rulesContentPanel = new VLayout();
		rulesContentPanel.setWidth100();
		rulesContentPanel.setShowResizeBar(true);
		rulesContentPanel.setHeight("*");

		ruleContentPanel = new VLayout();
		ruleContentPanel.setWidth100();
		ruleContentPanel.setShowResizeBar(true);
	
		widget = new VLayout();
		widget.setWidth100();
		widget.setHeight100();
		widget.addMember(generalContentPanel);
		widget.addMember(rulesContentPanel);
		widget.addMember(ruleContentPanel);

	}

	public Widget asWidget() {
		return widget;
	}

	// GWTP will call setInSlot when a child presenter asks to be added under
	// this view.
	@Override
	public void setInSlot(Object slot, Widget content) {
		if (slot == StyledLayerDescriptorLayoutPresenter.TYPE_GENERAL_CONTENT) {
			setGeneralContent(content);
		} else if (slot == StyledLayerDescriptorLayoutPresenter.TYPE_RULES_CONTENT) {
			setRulesContent(content);
		} else if (slot == StyledLayerDescriptorLayoutPresenter.TYPE_RULE_CONTENT) {
			setRuleContent(content);
		} else {
			super.setInSlot(slot, content);
		}
	}

	private void setGeneralContent(Widget content) {
		for (Canvas member : generalContentPanel.getMembers()) {
			member.removeFromParent();
		}
		if (content != null) {
			generalContentPanel.addMember(content);
		}
	}

	private void setRulesContent(Widget content) {
		for (Canvas member : rulesContentPanel.getMembers()) {
			member.removeFromParent();
		}
		if (content != null) {
			rulesContentPanel.addMember(content);
		}
	}

	private void setRuleContent(Widget content) {
		for (Canvas member : ruleContentPanel.getMembers()) {
			member.removeFromParent();
		}
		if (content != null) {
			ruleContentPanel.addMember(content);
		}
	}

	public void showLoading(boolean visibile) {
		// TODO Auto-generated method stub

	}

	public boolean isLoaded() {
		return generalContentPanel.getMembers().length > 0;
	}
}