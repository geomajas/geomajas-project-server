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

package org.geomajas.gwt.example.base;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Cookies;
import com.smartgwt.client.types.TabBarControls;
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
import org.geomajas.gwt.client.command.GwtCommandDispatcher;
import org.geomajas.gwt.client.util.Html;
import org.geomajas.gwt.client.util.HtmlBuilder;
import org.geomajas.gwt.client.util.WidgetLayout;
import org.geomajas.gwt.client.widget.ActivityMonitor;
import org.geomajas.gwt.client.widget.LocaleSelect;
import org.geomajas.gwt.example.base.i18n.ExampleBaseMessages;

import java.util.LinkedHashMap;

/**
 * Layout for the example projects.
 * 
 * @author Pieter De Graef
 * @author Joachim Van der Auwera
 */
public class ExampleLayout {

	private static final ExampleBaseMessages MESSAGES = GWT.create(ExampleBaseMessages.class);
	private TabSet mainTabSet;
	private final Label userLabel = new Label();

	private SimpleAuthenticationHandler authenticationHandler;

	public Label getUserLabel() {
		return userLabel;
	}

	/**
	 * Set the authentication handler to use.
	 *
	 * @param authenticationHandler authentication handler
	 */
	public void setAuthenticationHandler(SimpleAuthenticationHandler authenticationHandler) {
		this.authenticationHandler = authenticationHandler;
	}

	public void buildUi() {

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

		Img icon = new Img(WidgetLayout.iconGeomajas);
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
		userLabel.setHeight(20);
		userLabel.setWidth100();
		userLabel.setPadding(3);
		userLabel.setBorder("1px solid #A0A0A0");

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
			if (null == authenticationHandler ||
					(null != userId && userId.equals(GwtCommandDispatcher.getInstance().getUserDetail().getUserId()))) {
				showSample(panel, treeNode.getName(), treeNode.getIcon());
			} else {
				// Switch user, then show sample:
				authenticationHandler.login(userId, userId, new Runnable() {
					public void run() {
						showSample(panel, name, icon);
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
			String imgHtml = Canvas.imgHTML(icon, 16, 16);
			tab.setTitle(HtmlBuilder.tagHtmlContent(Html.Tag.SPAN, imgHtml, "&nbsp;", name));
			tab.setPane(panel);
			tab.setCanClose(true);
			mainTabSet.addTab(tab);
		}
		mainTabSet.selectTab(tab);
	}

	private void showIntroductionTab() {
		showSample(new IntroductionTab(), MESSAGES.introductionTitle(), WidgetLayout.iconGeomajas);
	}

	/**
	 * Authentication handler for login and logout to the correct user.
	 *
	 * @author Joachim Van der Auwera
	 */
	public interface SimpleAuthenticationHandler {

		/**
		 * Login using given credentials.
		 *
		 * @param login login
		 * @param password password
		 * @param callback callback which is called when successful
		 */
		void login(String login, String password, Runnable callback);
	}
}
