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

package org.geomajas.example.gwt.client;

import java.util.LinkedHashMap;

import org.geomajas.example.gwt.client.samples.IntroductionTab;
import org.geomajas.example.gwt.client.samples.base.SamplePanel;
import org.geomajas.example.gwt.client.samples.base.SamplePanelFactory;
import org.geomajas.example.gwt.client.samples.base.SampleTree;
import org.geomajas.example.gwt.client.samples.base.SampleTreeNode;
import org.geomajas.example.gwt.client.samples.i18n.I18nProvider;
import org.geomajas.example.gwt.client.samples.i18n.Samples;
import org.geomajas.gwt.client.widget.ActivityMonitor;
import org.geomajas.gwt.client.widget.LocaleSelect;
import org.geomajas.plugin.staticsecurity.client.Authentication;
import org.geomajas.plugin.staticsecurity.client.event.LoginFailureEvent;
import org.geomajas.plugin.staticsecurity.client.event.LoginHandler;
import org.geomajas.plugin.staticsecurity.client.event.LoginSuccessEvent;
import org.geomajas.plugin.staticsecurity.client.event.LogoutFailureEvent;
import org.geomajas.plugin.staticsecurity.client.event.LogoutHandler;
import org.geomajas.plugin.staticsecurity.client.event.LogoutSuccessEvent;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.ConstantsWithLookup;
import com.google.gwt.user.client.Cookies;
import com.smartgwt.client.types.TabBarControls;
import com.smartgwt.client.util.BooleanCallback;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.Img;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.form.fields.SelectItem;
import com.smartgwt.client.widgets.form.fields.events.ChangeEvent;
import com.smartgwt.client.widgets.form.fields.events.ChangeHandler;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.LayoutSpacer;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.tab.Tab;
import com.smartgwt.client.widgets.tab.TabSet;
import com.smartgwt.client.widgets.toolbar.ToolStrip;
import com.smartgwt.client.widgets.toolbar.ToolStripButton;
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

	public void onModuleLoad() {
		org.geomajas.gwt.client.i18n.I18nProvider.setLookUp(GWT.<ConstantsWithLookup>create(Samples.class));

		// Setup the overall layout:
		VLayout main = new VLayout();
		main.setWidth100();
		main.setHeight100();
		main.setStyleName("tabSetContainer");

		// Top bar:
		ToolStrip topBar = new ToolStrip();
		topBar.setHeight(33);
		topBar.setWidth100();
		topBar.addSpacer(6);

		Img icon = new Img("[ISOMORPHIC]/geomajas/geomajas_desktopicon_small.png");
		icon.setSize(24);
		topBar.addMember(icon);

		Label title = new Label("Geomajas GWT Showcase");
		title.setStyleName("sgwtTitle");
		title.setWidth(300);
		topBar.addMember(title);
		topBar.addFill();

		ToolStripButton devConsoleButton = new ToolStripButton();
		devConsoleButton.setTitle("Developer Console");
		devConsoleButton.setIcon("[ISOMORPHIC]/geomajas/silk/bug.png");
		devConsoleButton.addClickHandler(new com.smartgwt.client.widgets.events.ClickHandler() {

			public void onClick(com.smartgwt.client.widgets.events.ClickEvent event) {
				SC.showConsole();
			}
		});

		topBar.addButton(devConsoleButton);

		topBar.addSeparator();

		// Skin select:
		SelectItem selectItem = new SelectItem();
		selectItem.setHeight(21);
		selectItem.setWidth(130);
		LinkedHashMap<String, String> valueMap = new LinkedHashMap<String, String>();
		valueMap.put("Graphite", "Graphite");
		valueMap.put("EnterpriseBlue", "Enterprise Blue");
		valueMap.put("Enterprise", "Enterprise Gray");

		selectItem.setValueMap(valueMap);

		String currentSkin = Cookies.getCookie("skin_name");
		if (currentSkin == null) {
			currentSkin = "Graphite";
		}
		selectItem.setDefaultValue(currentSkin);
		selectItem.setShowTitle(false);
		selectItem.addChangeHandler(new ChangeHandler() {

			public void onChange(ChangeEvent event) {
				Cookies.setCookie("skin_name", (String) event.getValue());
				com.google.gwt.user.client.Window.Location.reload();
			}
		});
		topBar.addFormItem(selectItem);
		main.addMember(topBar);

		// Horizontal layout:
		HLayout hLayout = new HLayout();
		hLayout.setWidth100();
		hLayout.setHeight100();
		hLayout.setLayoutMargin(5);

		VLayout leftTreeLayout = new VLayout();
		leftTreeLayout.setHeight100();
		leftTreeLayout.setWidth(200);
		leftTreeLayout.setShowResizeBar(true);
		leftTreeLayout.setMembersMargin(1);

		SampleTree sampleTree = new SampleTree();
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

		// Show an ActivityMonitor:
		ActivityMonitor monitor = new ActivityMonitor();
		leftTreeLayout.addMember(monitor);

		hLayout.addMember(leftTreeLayout);

		mainTabSet = new TabSet();
		mainTabSet.setWidth100();
		mainTabSet.setHeight100();

		LayoutSpacer layoutSpacer = new LayoutSpacer();
		layoutSpacer.setWidth(5);
		LocaleSelect localeSelect = new LocaleSelect("English");
		mainTabSet.setTabBarControls(TabBarControls.TAB_SCROLLER, TabBarControls.TAB_PICKER, layoutSpacer, 
				localeSelect);

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
		String imgHTML = Canvas.imgHTML("[ISOMORPHIC]/geomajas/geomajas_desktopicon_small.png", 16, 16);
		tab.setTitle("<span>" + imgHTML + "&nbsp;" + I18nProvider.getSampleMessages().introductionTitle() + "</span>");
		tab.setPane(sample.getViewPanel());
		mainTabSet.addTab(tab);
	}
}
