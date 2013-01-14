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

import org.geomajas.sld.editor.common.client.presenter.MainLayoutPresenter;

import com.google.gwt.user.client.ui.Widget;
import com.gwtplatform.mvp.client.ViewImpl;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.Overflow;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;

/**
 * Main view with content slots for child presenters.
 * 
 * @author Jan De Moerloose
 * 
 */
public class MainLayoutView extends ViewImpl implements MainLayoutPresenter.MyView {

	private VLayout mainContentPanel;

	private VLayout sideContentPanel;

	private HLayout topContentPanel;

	private VLayout widget;

	public MainLayoutView() {

		sideContentPanel = new VLayout();
		sideContentPanel.setWidth("30%");
		sideContentPanel.setHeight100();
		sideContentPanel.setShowResizeBar(true);

		topContentPanel = new HLayout();
		Label topLabel = new Label();
		topLabel.setContents("Loading...");
		topLabel.setAlign(Alignment.CENTER);
		topLabel.setOverflow(Overflow.HIDDEN);
		topContentPanel.addMember(topLabel);
		topContentPanel.setHeight(100);
		topContentPanel.setWidth100();

		mainContentPanel = new VLayout();
		Label mainLabel = new Label();
		mainLabel.setContents("Loading...");
		mainLabel.setAlign(Alignment.CENTER);
		mainLabel.setOverflow(Overflow.HIDDEN);
		mainContentPanel.addMember(mainLabel);
		mainContentPanel.setHeight100();
		mainContentPanel.setWidth("70%");

		HLayout hLayout = new HLayout();
		hLayout.addMember(sideContentPanel);
		hLayout.addMember(mainContentPanel);
		hLayout.setHeight("*");
		hLayout.setWidth100();

		widget = new VLayout();
		widget.setWidth100();
		widget.setHeight100();
		widget.setMargin(5);
		widget.setMembersMargin(5);
		widget.addMember(topContentPanel);
		widget.addMember(hLayout);

	}

	public Widget asWidget() {
		return widget;
	}

	// GWTP will call setInSlot when a child presenter asks to be added under
	// this view.
	@Override
	public void setInSlot(Object slot, Widget content) {
		if (slot == MainLayoutPresenter.TYPE_MAIN_CONTENT) {
			setMainContent(content);
		} else if (slot == MainLayoutPresenter.TYPE_SIDE_CONTENT) {
			setSideContent(content);
		} else if (slot == MainLayoutPresenter.TYPE_TOP_CONTENT) {
			setTopContent(content);
		} else {
			super.setInSlot(slot, content);
		}
	}

	private void setMainContent(Widget content) {
		if (content != null) {
			mainContentPanel.setMembers((Canvas) content);
		}
	}

	private void setSideContent(Widget content) {
		if (content != null) {
			sideContentPanel.setMembers((Canvas) content);
		}
	}

	private void setTopContent(Widget content) {
		if (content != null) {
			topContentPanel.setMembers((Canvas) content);
		}
	}

	public void showLoading(boolean visibile) {
		// TODO Auto-generated method stub

	}

	public boolean isLoaded() {
		return (mainContentPanel.getMembers().length > 0 && 
			sideContentPanel.getMembers().length > 0);
	}
}