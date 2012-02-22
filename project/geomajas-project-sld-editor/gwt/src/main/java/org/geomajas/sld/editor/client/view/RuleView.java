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
package org.geomajas.sld.editor.client.view;

import org.geomajas.sld.RuleInfo;
import org.geomajas.sld.editor.common.client.i18n.SldEditorMessages;
import org.geomajas.sld.editor.common.client.presenter.RulePresenter;
import org.geomajas.sld.editor.common.client.view.ViewUtil;

import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.ViewImpl;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.Overflow;
import com.smartgwt.client.types.Side;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.layout.Layout;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.tab.Tab;
import com.smartgwt.client.widgets.tab.TabSet;

/**
 * Default implementation of {@link RulePresenter.MyView}.
 * 
 * @author An Buyle
 * @author Jan De Moerloose
 *
 */
public class RuleView extends ViewImpl implements RulePresenter.MyView {

	private VLayout ruleDetailContainer;

	private VLayout symbolPane;

	private VLayout filterPane;

	private RuleInfo currentRule;

	private Label noRuleSelectedMessage;

	private final EventBus eventBus;

	private final ViewUtil viewUtil;

	private final SldEditorMessages sldEditorMessages;

	@Inject
	public RuleView(final EventBus eventBus, final ViewUtil viewUtil, final SldEditorMessages sldEditorMessages) {
		this.eventBus = eventBus;
		this.viewUtil = viewUtil;
		this.sldEditorMessages = sldEditorMessages;
		ruleDetailContainer = new VLayout(5);
		ruleDetailContainer.setWidth100();
		ruleDetailContainer.setLayoutTopMargin(10);
		ruleDetailContainer.setLayoutLeftMargin(5);
		ruleDetailContainer.setLayoutBottomMargin(5);
		ruleDetailContainer.setGroupTitle(sldEditorMessages.ruleDetailContainerTitle());
		ruleDetailContainer.setIsGroup(true);

		noRuleSelectedMessage = new Label("<i>Geen stijl geselecteerd!</i>");
		noRuleSelectedMessage.setAlign(Alignment.CENTER);
		ruleDetailContainer.addMember(noRuleSelectedMessage);
		noRuleSelectedMessage.hide();

		TabSet topTabSet = new TabSet();
		topTabSet.setTabBarPosition(Side.TOP);

		symbolPane = new VLayout();
		symbolPane.setMembersMargin(5);
		symbolPane.setMargin(5);
		symbolPane.setWidth100();
		symbolPane.setHeight100();
		Tab tTab1 = new Tab(sldEditorMessages.symbologyTabTitle());
		tTab1.setPane(symbolPane);

		filterPane = new VLayout();
		filterPane.setMembersMargin(5);
		filterPane.setMargin(5);
		filterPane.setHeight100();
		filterPane.setWidth100();
		Tab tTab2 = new Tab(sldEditorMessages.filterTabTitle());
		tTab2.setPane(filterPane);

		topTabSet.addTab(tTab1);
		topTabSet.addTab(tTab2);
		topTabSet.setWidth100();
		topTabSet.setHeight100();
		topTabSet.setOverflow(Overflow.VISIBLE);
		topTabSet.setPaneContainerOverflow(Overflow.VISIBLE);
		ruleDetailContainer.addMember(topTabSet);
	}

	@Override
	public void setInSlot(Object slot, Widget content) {
		if (slot == RulePresenter.TYPE_SYMBOL_CONTENT) {
			setContent(symbolPane, (Canvas) content);
		} else if (slot == RulePresenter.TYPE_FILTER_CONTENT) {
			setContent(filterPane, (Canvas) content);
		} else {
			super.setInSlot(slot, content);
		}
	}

	public void reset() {
		hideContent(filterPane);
		hideContent(symbolPane);
	}

	private void hideContent(Layout layout) {
		for (Canvas canvas : layout.getMembers()) {
			canvas.hide();
		}
	}

	private void setContent(Layout layout, Canvas content) {
		if (content != null) {
			content.setWidth100();
			content.setHeight100();
			if (layout.hasMember(content)) {
				content.setVisible(true);
			} else {
				layout.addMember(content);
			}
		}
	}

	public Widget asWidget() {
		return ruleDetailContainer;
	}

}
