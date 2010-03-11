/*
 * This file is part of Geomajas, a component framework for building
 * rich Internet applications (RIA) with sophisticated capabilities for the
 * display, analysis and management of geographic information.
 * It is a building block that allows developers to add maps
 * and other geographic data capabilities to their web applications.
 *
 * Copyright 2008-2010 Geosparc, http://www.geosparc.com, Belgium
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.geomajas.gwt.client;

import org.geomajas.gwt.client.samples.IntroductionTab;
import org.geomajas.gwt.client.samples.base.SamplePanel;
import org.geomajas.gwt.client.samples.base.SamplePanelFactory;
import org.geomajas.gwt.client.samples.base.SampleTree;
import org.geomajas.gwt.client.samples.base.SampleTreeNode;
import org.geomajas.gwt.client.samples.i18n.I18nProvider;
import org.geomajas.gwt.client.samples.i18n.Samples;
import org.geomajas.gwt.client.widget.ActivityMonitor;
import org.geomajas.gwt.client.widget.LocaleSelect;
import org.geomajas.plugin.springsecurity.client.Authentication;
import org.geomajas.plugin.springsecurity.client.event.LoginFailureEvent;
import org.geomajas.plugin.springsecurity.client.event.LoginHandler;
import org.geomajas.plugin.springsecurity.client.event.LoginSuccessEvent;
import org.geomajas.plugin.springsecurity.client.event.LogoutFailureEvent;
import org.geomajas.plugin.springsecurity.client.event.LogoutHandler;
import org.geomajas.plugin.springsecurity.client.event.LogoutSuccessEvent;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.ConstantsWithLookup;
import com.smartgwt.client.types.TabBarControls;
import com.smartgwt.client.util.BooleanCallback;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.LayoutSpacer;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.tab.Tab;
import com.smartgwt.client.widgets.tab.TabSet;
import com.smartgwt.client.widgets.tree.TreeNode;
import com.smartgwt.client.widgets.tree.events.LeafClickEvent;
import com.smartgwt.client.widgets.tree.events.LeafClickHandler;

/**
 * <p>
 * The GWT test case sample application. Here here!
 * </p>
 *
 * @author Pieter De Graef
 */
public class GeomajasSamples implements EntryPoint {

	private TabSet mainTabSet;

	private SampleTree sampleTree;

	public void onModuleLoad() {
		org.geomajas.gwt.client.i18n.I18nProvider.setLookUp(GWT.<ConstantsWithLookup>create(Samples.class));

		// Setup the overall layout:
		VLayout main = new VLayout();
		main.setWidth100();
		main.setHeight100();
		main.setLayoutMargin(5);
		main.setStyleName("tabSetContainer");

		HLayout hLayout = new HLayout();
		hLayout.setWidth100();
		hLayout.setHeight100();

		VLayout leftTreeLayout = new VLayout();
		leftTreeLayout.setHeight100();
		leftTreeLayout.setWidth(200);
		leftTreeLayout.setShowResizeBar(true);
		leftTreeLayout.setMembersMargin(1);

		sampleTree = new SampleTree();
		sampleTree.addLeafClickHandler(new LeafClickHandler() {

			public void onLeafClick(LeafClickEvent event) {
				TreeNode node = event.getLeaf();
				prepareSample(node);
			}
		});

		leftTreeLayout.addMember(sampleTree);

		// Show a user label:
		final Label userLabel = new Label();
		userLabel.setHeight(20);
		userLabel.setWidth100();
		userLabel.setPadding(3);
		userLabel.setBorder("1px solid #A0A0A0");

		Authentication.getInstance().addLoginHandler(new LoginHandler() {

			public void onLoginFailure(LoginFailureEvent event) {
			}

			public void onLoginSuccess(LoginSuccessEvent event) {
				userLabel.setContents("Logged in with: " + Authentication.getInstance().getUserId());
			}
		});
		Authentication.getInstance().addLogoutHandler(new LogoutHandler() {

			public void onLogoutFailure(LogoutFailureEvent event) {
			}

			public void onLogoutSuccess(LogoutSuccessEvent event) {
				userLabel.setContents("No user is logged in.");
			}
		});
		Authentication.getInstance().login("luc", "luc", null);
		leftTreeLayout.addMember(userLabel);

		// Show an AcivityMonitor:
		ActivityMonitor monitor = new ActivityMonitor();
		leftTreeLayout.addMember(monitor);

		hLayout.addMember(leftTreeLayout);

		mainTabSet = new TabSet();
		mainTabSet.setWidth100();
		mainTabSet.setHeight100();

		LayoutSpacer layoutSpacer = new LayoutSpacer();
		layoutSpacer.setWidth(5);
		LocaleSelect localeSelect = new LocaleSelect();
		mainTabSet
				.setTabBarControls(TabBarControls.TAB_SCROLLER, TabBarControls.TAB_PICKER, layoutSpacer, localeSelect);

		hLayout.addMember(mainTabSet);
		main.addMember(hLayout);
		main.draw();
		showIntroductionTab();
	}

	private void prepareSample(TreeNode node) {
		if (node instanceof SampleTreeNode) {
			SampleTreeNode treeNode = (SampleTreeNode) node;
			SamplePanelFactory factory = treeNode.getFactory();
			if (factory == null) {
				return;
			}
			if (mainTabSet.getNumTabs() > 1) {
				mainTabSet.removeTab(1);
			}
			final SamplePanel panel = factory.createPanel();
			final String name = treeNode.getName();
			final String icon = treeNode.getIcon();

			String userId = panel.ensureUserLoggedIn();
			if (userId == null) {
				// Log out, then show sample:
				Authentication.getInstance().logout(new BooleanCallback() {

					public void execute(Boolean value) {
						if (value) {
							showSample(panel, name, icon);
						}
					}
				});
			} else if (userId.equals(Authentication.getInstance().getUserId())) {
				showSample(panel, treeNode.getName(), treeNode.getIcon());
			} else {
				// Switch user, then show sample:
				Authentication.getInstance().login(userId, userId, new BooleanCallback() {

					public void execute(Boolean value) {
						if (value) {
							showSample(panel, name, icon);
						}
					}
				});
			}
		}
	}

	private void showSample(SamplePanel panel, String name, String icon) {
		String tabId = panel.getId() + "_tab";
		Tab tab = mainTabSet.getTab(tabId);
		if (tab == null) {
			tab = new Tab();
			tab.setID(tabId);
			String imgHTML = Canvas.imgHTML(icon, 16, 16);
			tab.setTitle("<span>" + imgHTML + "&nbsp;" + name + "</span>");
			tab.setPane(panel);
			tab.setCanClose(true);
			mainTabSet.addTab(tab);
		}
		mainTabSet.selectTab(tab);
	}

	private void showIntroductionTab() {
		IntroductionTab sample = new IntroductionTab();
		Tab tab = new Tab();
		tab.setID(sample.getId() + "_tab");
		String imgHTML = Canvas.imgHTML("[ISOMORPHIC]/geomajas/example/images/geomajas_favicon.jpg", 16, 16);
		tab.setTitle("<span>" + imgHTML + "&nbsp;" + I18nProvider.getSampleMessages().introductionTitle() + "</span>");
		tab.setPane(sample.getViewPanel());
		mainTabSet.addTab(tab);
	}
}
