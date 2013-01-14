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

package org.geomajas.example.gwt.client.samples.base;

import java.util.Map;
import java.util.Map.Entry;

import org.geomajas.command.CommandResponse;
import org.geomajas.example.gwt.client.samples.i18n.I18nProvider;
import org.geomajas.gwt.client.command.CommandCallback;
import org.geomajas.gwt.client.command.GwtCommand;
import org.geomajas.gwt.client.command.GwtCommandDispatcher;

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

/**
 * <p>
 * Base template for a test sample. All samples should extend this class.
 * </p>
 * 
 * @author Pieter De Graef
 */
public abstract class SamplePanel extends VLayout {

	// -------------------------------------------------------------------------
	// Constructors:
	// -------------------------------------------------------------------------

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
		hLayout.addMember(getViewPanel());

		String description = getDescription();
		if (description != null) {
			VLayout rightLayout = new VLayout();
			rightLayout.setMembersMargin(15);

			IButton sourceButton = new IButton(I18nProvider.getSampleMessages().generalSourceButton());
			sourceButton.setIcon("[ISOMORPHIC]/geomajas/example/images/silk/script_go.png");
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
			descriptionWindow.setTitle(I18nProvider.getSampleMessages().generalDescription());
			descriptionWindow.setHeaderIcon("[ISOMORPHIC]/geomajas/geomajas_desktopicon_small.png", 16, 16);
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
	 * @return the {@link Canvas} which contains the example
	 */
	public abstract Canvas getViewPanel();

	public abstract String getDescription();

	public abstract String getSourceFileName();

	public abstract String[] getConfigurationFiles();

	/**
	 * If a user must be logged in before the sample can be shown, return his name here.
	 * 
	 * @return user which needs to be logged in
	 */
	public abstract String ensureUserLoggedIn();

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

		GetResourcesRequest request = new GetResourcesRequest(resourceFiles);
		GwtCommand command = new GwtCommand("example.gwt.server.samples.GetSourceCommand");
		command.setCommandRequest(request);
		GwtCommandDispatcher.getInstance().execute(command, new CommandCallback() {

			public void execute(CommandResponse response) {
				if (response instanceof GetResourcesResponse) {
					GetResourcesResponse sourceResponse = (GetResourcesResponse) response;
					if (sourceResponse.getResources() == null) {
						return;
					}
					showFiles(sourceResponse.getResources());
				}
			}
		});
	}

	private void showFiles(Map<String, String> fileContents) {
		Window sourceWindow = new Window();
		sourceWindow.setTitle(I18nProvider.getSampleMessages().generalSourceTitle());
		sourceWindow.setHeaderIcon("[ISOMORPHIC]/geomajas/geomajas_desktopicon_small.png", 16, 16);
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

				Label resourceLabel = new Label(I18nProvider.getSampleMessages().generalFile() + ": <b>"
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

				Tab tab = new Tab(tabTitle, "[ISOMORPHIC]/geomajas/example/images/silk/script_go.png");
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
		tabSource.setContents("<pre style='color:#000000;'>" + JavaParser.parseJava(content) + "</pre>");
		tabSource.setContentsType(ContentsType.PAGE);

		sourceLayout.addMember(tabSource);

		String tabTitle = I18nProvider.getSampleMessages().generalJavaSource();
		int pos = tabTitle.lastIndexOf('/');
		tabTitle = tabTitle.substring(pos + 1);

		Tab tab = new Tab(tabTitle, "[ISOMORPHIC]/geomajas/example/images/silk/script_go.png");
		tab.setPane(sourceLayout);
		tabs.addTab(tab);
	}
}
