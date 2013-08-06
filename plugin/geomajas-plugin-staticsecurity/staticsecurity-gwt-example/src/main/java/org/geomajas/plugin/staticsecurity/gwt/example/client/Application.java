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

package org.geomajas.plugin.staticsecurity.gwt.example.client;

import org.geomajas.gwt.client.command.AbstractCommandCallback;
import org.geomajas.gwt.client.command.GwtCommand;
import org.geomajas.gwt.client.command.GwtCommandDispatcher;
import org.geomajas.gwt.client.command.UserDetail;
import org.geomajas.gwt.client.command.event.TokenChangedEvent;
import org.geomajas.gwt.client.command.event.TokenChangedHandler;
import org.geomajas.gwt.client.service.ClientConfigurationLoader;
import org.geomajas.gwt.client.service.ClientConfigurationService;
import org.geomajas.gwt.client.service.ClientConfigurationSetter;
import org.geomajas.plugin.staticsecurity.client.StaticSecurityTokenRequestHandler;
import org.geomajas.plugin.staticsecurity.client.TokenReleaseButton;
import org.geomajas.plugin.staticsecurity.gwt.example.server.command.dto.AppConfigurationRequest;
import org.geomajas.plugin.staticsecurity.gwt.example.server.command.dto.AppConfigurationResponse;
import org.geomajas.smartgwt.client.gfx.style.ShapeStyle;
import org.geomajas.smartgwt.client.map.event.MapModelChangedEvent;
import org.geomajas.smartgwt.client.map.event.MapModelChangedHandler;
import org.geomajas.smartgwt.client.util.WidgetLayout;
import org.geomajas.smartgwt.client.widget.LayerTree;
import org.geomajas.smartgwt.client.widget.Legend;
import org.geomajas.smartgwt.client.widget.MapWidget;
import org.geomajas.smartgwt.client.widget.OverviewMap;
import org.geomajas.smartgwt.client.widget.Toolbar;

import com.google.gwt.core.client.EntryPoint;
import com.smartgwt.client.types.VisibilityMode;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.SectionStack;
import com.smartgwt.client.widgets.layout.SectionStackSection;
import com.smartgwt.client.widgets.layout.VLayout;

/**
 * Entry point and main class for GWT application. This class defines the layout and functionality of this application.
 *
 * @author geomajas-gwt-archetype
 */
public class Application implements EntryPoint {

	public static final String APPLICATION_LABEL = "Geomajas GWT: staticsecurity example";
	public static final String APPLICATION_TITLE_STYLE = "appTitle";
	public static final String APPLICATION_USER_STYLE = "appUser";
	public static final String APPLICATION_SECURITY_STYLE = "appSecurity";

	private OverviewMap overviewMap;

	private Legend legend;

	public void onModuleLoad() {
		// @extract-start TokenRequestHandler, Set the token request handler
		GwtCommandDispatcher.getInstance().setTokenRequestHandler(
				new StaticSecurityTokenRequestHandler(
				"Possible users are 'luc' and 'marino'. The password is the same as the login."));
		// @extract-end

		// @extract-start ClientConfigurationLoader, Replace the client configuration loader
		ClientConfigurationService.setConfigurationLoader(new ClientConfigurationLoader() {
			public void loadClientApplicationInfo(final String applicationId, final ClientConfigurationSetter setter) {
				GwtCommand commandRequest = new GwtCommand(AppConfigurationRequest.COMMAND);
				commandRequest.setCommandRequest(new AppConfigurationRequest(applicationId));
				GwtCommandDispatcher.getInstance().execute(commandRequest,
						new AbstractCommandCallback<AppConfigurationResponse>() {
							public void execute(AppConfigurationResponse response) {
								ClientSecurityContext.setBlablaButtonAllowed(response.isBlablaButtonAllowed());
								ClientSecurityContext.setBlablaButtonAllowed(response.isBlablaButtonAllowed());
								setter.set(applicationId, response.getApplication());
							}
						});
			}
		});
		// @extract-end

		VLayout mainLayout = new VLayout();
		mainLayout.setWidth100();
		mainLayout.setHeight100();
		mainLayout.setBackgroundColor("#A0A0A0");

		HLayout layout = new HLayout();
		layout.setWidth100();
		layout.setHeight100();
		layout.setMembersMargin(WidgetLayout.marginLarge);
		layout.setMargin(WidgetLayout.marginLarge);

		// ---------------------------------------------------------------------
		// Create the left-side (map and tabs):
		// ---------------------------------------------------------------------
		final MapWidget map = new MapWidget("mapMain", "app");
		final Toolbar toolbar = new Toolbar(map);
		toolbar.setButtonSize(WidgetLayout.toolbarLargeButtonSize);
		toolbar.setBackgroundColor("#647386");
		toolbar.setBackgroundImage("");
		toolbar.setBorder("0px");

		Label title = new Label(APPLICATION_LABEL);
		title.setStyleName(APPLICATION_TITLE_STYLE);
		title.setWidth("*");
		toolbar.addFill();
		toolbar.addMember(title);

		VLayout mapLayout = new VLayout();
		mapLayout.addMember(toolbar);
		mapLayout.addMember(map);
		mapLayout.setHeight("65%");

		VLayout leftLayout = new VLayout();
		leftLayout.setBorder("10px solid #777777");
		leftLayout.setStyleName("round_corner");
		leftLayout.addMember(mapLayout);

		layout.addMember(leftLayout);

		// ---------------------------------------------------------------------
		// Create the right-side (overview map, layer-tree, legend):
		// ---------------------------------------------------------------------
		final SectionStack sectionStack = new SectionStack();
		sectionStack.setBorder("10px solid #777777");
		sectionStack.setStyleName("round_corner");
		sectionStack.setVisibilityMode(VisibilityMode.MULTIPLE);
		sectionStack.setCanReorderSections(true);
		sectionStack.setCanResizeSections(false);
		sectionStack.setSize("250px", "100%");

		// Block with security related stuff
		SectionStackSection sectionSecurity = new SectionStackSection("Security");
		sectionSecurity.setExpanded(true);
		final VLayout sectionSecurityLayout = new VLayout(WidgetLayout.marginSmall);
		sectionSecurityLayout.setStyleName(APPLICATION_SECURITY_STYLE);
		final Label whoAmI = new Label();
		whoAmI.setStyleName(APPLICATION_USER_STYLE);
		whoAmI.setHeight(16);
		whoAmI.setWidth100();
		GwtCommandDispatcher.getInstance().addTokenChangedHandler(new TokenChangedHandler() {
			public void onTokenChanged(TokenChangedEvent event) {
				String user = "";
				UserDetail userDetail = event.getUserDetail();
				if (null != userDetail.getUserName()) {
					user = userDetail.getUserName();
				} else if (null != userDetail.getUserId()) {
					user = userDetail.getUserId();
				}
				whoAmI.setContents("user: " + user);
			}
		});
		sectionSecurityLayout.addMember(whoAmI);
		TokenReleaseButton tokenReleaseButton = new TokenReleaseButton();
		tokenReleaseButton.setWidth100();
		sectionSecurityLayout.addMember(tokenReleaseButton);
		sectionSecurityLayout.setHeight(1);
		sectionSecurityLayout.setWidth100();
		sectionSecurity.addItem(sectionSecurityLayout);
		sectionStack.addSection(sectionSecurity);
		final IButton blablaButton = new IButton("blabla");
		blablaButton.setWidth100();
		blablaButton.hide();
		blablaButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent clickEvent) {
				SC.say("You have the rights to click the blabla button<br/>May the force be with you.");
			}
		});
		sectionSecurityLayout.addMember(blablaButton);
		map.getMapModel().addMapModelChangedHandler(new MapModelChangedHandler() {
			public void onMapModelChanged(MapModelChangedEvent event) {
				if (ClientSecurityContext.isBlablaButtonAllowed()) {
					blablaButton.show();
				} else {
					blablaButton.hide();
				}
			}
		});

		// Overview map layout:
		SectionStackSection section1 = new SectionStackSection("Overview map");
		section1.setExpanded(true);
		overviewMap = new OverviewMap("mapOverview", "app", map, false, true);
		overviewMap.setTargetMaxExtentRectangleStyle(new ShapeStyle("#888888", 0.3f, "#666666", 0.75f, 2));
		overviewMap.setRectangleStyle(new ShapeStyle("#6699FF", 0.3f, "#6699CC", 1f, 2));
		section1.addItem(overviewMap);
		sectionStack.addSection(section1);

		// LayerTree layout:
		SectionStackSection section2 = new SectionStackSection("Layer tree");
		section2.setExpanded(true);
		LayerTree layerTree = new LayerTree(map);
		section2.addItem(layerTree);
		sectionStack.addSection(section2);

		// Legend layout:
		SectionStackSection section3 = new SectionStackSection("Legend");
		section3.setExpanded(true);
		legend = new Legend(map.getMapModel());
		legend.setBackgroundColor("#FFFFFF");
		section3.addItem(legend);
		sectionStack.addSection(section3);

		// Putting the right side layouts together:
		layout.addMember(sectionStack);

		// ---------------------------------------------------------------------
		// Finally draw everything:
		// ---------------------------------------------------------------------
		mainLayout.addMember(layout);
		mainLayout.draw();

		// Initialize:
		initialize();
	}

	private void initialize() {
		legend.setHeight(200);
		overviewMap.setHeight(200);
	}
}
