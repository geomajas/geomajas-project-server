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

package org.geomajas.smartgwt.example.base;

import com.google.gwt.core.client.GWT;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.ContentsType;
import com.smartgwt.client.types.HeaderControls;
import com.smartgwt.client.types.Side;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.HTMLPane;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.Window;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.tab.Tab;
import com.smartgwt.client.widgets.tab.TabSet;
import org.geomajas.gwt.client.command.AbstractCommandCallback;
import org.geomajas.gwt.client.command.GwtCommand;
import org.geomajas.gwt.client.command.GwtCommandDispatcher;
import org.geomajas.gwt.client.util.Html;
import org.geomajas.gwt.client.util.HtmlBuilder;
import org.geomajas.smartgwt.client.util.WidgetLayout;
import org.geomajas.smartgwt.example.base.command.dto.GetResourceRequest;
import org.geomajas.smartgwt.example.base.command.dto.GetResourceResponse;
import org.geomajas.smartgwt.example.base.i18n.ExampleBaseMessages;

import java.util.Map;
import java.util.Map.Entry;

/**
 * <p>
 * Base template for a test sample. All samples should extend this class.
 * </p>
 * 
 * @author Pieter De Graef
 */
public abstract class SamplePanel extends VLayout {

	private static final ExampleBaseMessages MESSAGES = GWT.create(ExampleBaseMessages.class);

	/**
	 * Constructor for the base sample panel. All extensions should have a public default constructor that uses this
	 * constructor.
	 */
	protected SamplePanel() {
		setWidth100();
		setHeight100();
		setMembersMargin(10);

		HLayout hLayout = new HLayout();
		hLayout.setMembersMargin(10);
		hLayout.setWidth100();
		Canvas viewPanel = getViewPanel();
		viewPanel.setWidth100();
		viewPanel.setHeight100();
		hLayout.addMember(viewPanel);

		String description = getDescription();
		if (description != null) {
			VLayout rightLayout = new VLayout();
			rightLayout.setMembersMargin(15);

			IButton sourceButton = new IButton(MESSAGES.generalSourceButton());
			sourceButton.setIcon("[ISOMORPHIC]/geomajas/example/image/silk/script_go.png");
			sourceButton.setLayoutAlign(Alignment.RIGHT);
			sourceButton.setShowDisabledIcon(false);
			sourceButton.setWidth(125);
			sourceButton.addClickHandler(new ClickHandler() {

				public void onClick(ClickEvent event) {
					getJavaSource();
				}
			});
			rightLayout.addMember(sourceButton);

			Window descriptionWindow = new Window();
			descriptionWindow.setTitle(MESSAGES.generalDescription());
			descriptionWindow.setHeaderIcon(WidgetLayout.iconGeomajas, 16, 16);
			descriptionWindow.setShowEdges(true);
			descriptionWindow.setKeepInParentRect(true);
			descriptionWindow.setHeaderControls(HeaderControls.HEADER_ICON, HeaderControls.HEADER_LABEL);

			Canvas contents = new Canvas();
			contents.setCanSelectText(true);
			contents.setPadding(5);
			contents.setContents(description);
			contents.setDefaultWidth(200);

			descriptionWindow.setAutoSize(true);
			descriptionWindow.setAutoHeight();
			descriptionWindow.addItem(contents);

			rightLayout.addMember(descriptionWindow);
			hLayout.addMember(rightLayout);
		}

		addMember(hLayout);
	}

	// -------------------------------------------------------------------------
	// Public methods:
	// -------------------------------------------------------------------------

	/**
	 * Implement this! It is the actual sample panel...
	 * 
	 * @return the {@link com.smartgwt.client.widgets.Canvas} which contains the example
	 */
	public abstract Canvas getViewPanel();

	public abstract String getDescription();

	public final String getSourceFileName() {
		String fqn = getClass().getName();
		return "classpath:" + fqn.replace(".", "/") + ".java§panel";
	}

	public abstract String[] getConfigurationFiles();

	/**
	 * If a user must be logged in before the sample can be shown, return his name here.
	 * 
	 * @return user which needs to be logged in
	 */
	public String ensureUserLoggedIn() {
		return null;
	}

	public String getId() {
		return id;
	}

	// -------------------------------------------------------------------------
	// Private methods:
	// -------------------------------------------------------------------------

	private void getJavaSource() {
		String[] resourceFiles = new String[getConfigurationFiles().length + 1];
		resourceFiles[0] = getSourceFileName();
		for (int i = 0; i < getConfigurationFiles().length; i++) {
			resourceFiles[i + 1] = getConfigurationFiles()[i];
		}

		GetResourceRequest request = new GetResourceRequest(resourceFiles);
		GwtCommand command = new GwtCommand(GetResourceRequest.COMMAND);
		command.setCommandRequest(request);
		GwtCommandDispatcher.getInstance().execute(command, new AbstractCommandCallback<GetResourceResponse>() {

			public void execute(GetResourceResponse response) {
				if (response.getResources() == null) {
					return;
				}
				showFiles(response.getResources());
			}
		});
	}

	private void showFiles(Map<String, String> fileContents) {
		Window sourceWindow = new Window();
		sourceWindow.setTitle(MESSAGES.generalSourceTitle());
		sourceWindow.setHeaderIcon(WidgetLayout.iconGeomajas, 16, 16);
		sourceWindow.setKeepInParentRect(true);
		sourceWindow.setWidth(640);
		sourceWindow.setHeight(480);
		sourceWindow.setTop(100);
		sourceWindow.setLeft(100);
		sourceWindow.setMembersMargin(5);
		sourceWindow.setCanDragReposition(true);
		sourceWindow.setCanDragResize(true);

		TabSet tabs = new TabSet();
		tabs.setTabBarPosition(Side.TOP);
		tabs.setWidth100();
		tabs.setHeight100();

		addSourceTab(tabs, fileContents.get(getSourceFileName()));

		for (Entry<String, String> entry : fileContents.entrySet()) {
			if (!entry.getKey().equals(getSourceFileName())) {
				VLayout resourceLayout = new VLayout();
				HTMLPane tabPane = new HTMLPane();
				tabPane.setWidth100();
				tabPane.setHeight100();
				tabPane.setContents("<pre style='color:#000000;'>" + XmlParser.parseXML(entry.getValue()) + "</pre>");
				tabPane.setContentsType(ContentsType.PAGE);

				Label resourceLabel = new Label(MESSAGES.generalFile() + ": <b>"
						+ entry.getKey() + "</b>");
				resourceLabel.setHeight(30);
				resourceLabel.setPadding(5);
				resourceLayout.addMember(resourceLabel);

				VLayout paneLayout = new VLayout();
				paneLayout.setBorder("1px solid #C0C0C0");
				paneLayout.addMember(tabPane);

				resourceLayout.addMember(paneLayout);

				String tabTitle = entry.getKey();
				int pos = tabTitle.lastIndexOf('/');
				tabTitle = tabTitle.substring(pos + 1);

				Tab tab = new Tab(tabTitle, "[ISOMORPHIC]/geomajas/example/image/silk/script_go.png");
				tab.setPane(resourceLayout);
				tabs.addTab(tab);
			}
		}

		sourceWindow.addItem(tabs);
		addChild(sourceWindow);
		sourceWindow.show();
	}

	private void addSourceTab(TabSet tabs, String content) {
		VLayout sourceLayout = new VLayout();
		HTMLPane tabSource = new HTMLPane();
		tabSource.setWidth100();
		tabSource.setHeight100();
		tabSource.setContents(
				HtmlBuilder.tagStyleHtmlContent(Html.Tag.PRE, "color:#000000;", JavaParser.parseJava(content)));
		tabSource.setContentsType(ContentsType.PAGE);

		sourceLayout.addMember(tabSource);

		String tabTitle = MESSAGES.generalJavaSource();
		int pos = tabTitle.lastIndexOf('/');
		tabTitle = tabTitle.substring(pos + 1);

		Tab tab = new Tab(tabTitle, "[ISOMORPHIC]/geomajas/example/image/silk/script_go.png");
		tab.setPane(sourceLayout);
		tabs.addTab(tab);
	}
}
