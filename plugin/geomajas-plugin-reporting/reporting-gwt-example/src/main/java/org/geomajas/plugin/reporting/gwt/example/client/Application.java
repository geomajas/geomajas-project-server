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

package org.geomajas.plugin.reporting.gwt.example.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.smartgwt.client.types.VisibilityMode;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.SectionStack;
import com.smartgwt.client.widgets.layout.SectionStackSection;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.menu.Menu;
import com.smartgwt.client.widgets.menu.MenuItem;
import com.smartgwt.client.widgets.menu.events.ClickHandler;
import com.smartgwt.client.widgets.menu.events.MenuItemClickEvent;
import com.smartgwt.client.widgets.toolbar.ToolStripMenuButton;
import org.geomajas.gwt.client.util.WidgetLayout;
import org.geomajas.gwt.client.widget.LayerTree;
import org.geomajas.gwt.client.widget.Legend;
import org.geomajas.gwt.client.widget.MapWidget;
import org.geomajas.gwt.client.widget.Toolbar;

/**
 * Entry point and main class for GWT application. This class defines the layout and functionality of this application.
 *
 * @author geomajas-gwt-archetype
 */
public class Application implements EntryPoint {

	private MapWidget map;

	private Legend legend;

	public void onModuleLoad() {
		VLayout mainLayout = new VLayout();
		mainLayout.setWidth100();
		mainLayout.setHeight100();
		mainLayout.setBackgroundColor("#A0A0A0");

		HLayout layout = new HLayout();
		layout.setWidth100();
		layout.setHeight100();
		layout.setMembersMargin(10);
		layout.setMargin(10);

		// ---------------------------------------------------------------------
		// Create the left-side (map and tabs):
		// ---------------------------------------------------------------------
		map = new MapWidget("mapMain", "app");
		final Toolbar toolbar = new Toolbar(map);
		toolbar.setButtonSize(WidgetLayout.toolbarLargeButtonSize);
		toolbar.setBackgroundColor("#647386");
		toolbar.setBackgroundImage("");
		toolbar.setBorder("0px");

		toolbar.addToolbarSeparator();
		toolbar.addMenuButton(getReportingMenuButton());

		Label title = new Label("Geomajas reporting demo");
		title.setStyleName("appTitle");
		title.setWidth(260);
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
		// Create the right-side (layer-tree, legend):
		// ---------------------------------------------------------------------
		final SectionStack sectionStack = new SectionStack();
		sectionStack.setBorder("10px solid #777777");
		sectionStack.setStyleName("round_corner");
		sectionStack.setVisibilityMode(VisibilityMode.MULTIPLE);
		sectionStack.setCanReorderSections(true);
		sectionStack.setCanResizeSections(false);
		sectionStack.setSize("250px", "100%");

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

		// Then initialize:
		initialize();
	}

	private void initialize() {
		legend.setHeight(200);
	}

	private ToolStripMenuButton getReportingMenuButton() {
		Menu menu = new Menu();
		menu.setShowShadow(true);
		menu.setShadowDepth(3);

		// simple report without images, using direct controller link
		MenuItem simpleItem = new MenuItem("Simple report", "[ISOMORPHIC]/images/report.png");
		simpleItem.addClickHandler(new ClickHandler() {
			public void onClick(MenuItemClickEvent menuItemClickEvent) {
				String url = GWT.getHostPageBaseURL();
				url += "d/reporting/f/" + "layerCountries" + "/" + "features" + "." + "pdf" + "?filter=";
				com.google.gwt.user.client.Window.open(url, "_blank", null);
			}
		});

		// reports with image, using caching of report data
		MenuItem pdfItem = new MenuItem("PDF", "[ISOMORPHIC]/images/pdf.png");
		pdfItem.addClickHandler(new ReportingClickHandler(map, "featuresWithMap", "clientLayerCountries", "pdf"));
		MenuItem ooItem = new MenuItem("OpenOffice", "[ISOMORPHIC]/images/LibreOfficeWriter.png");
		ooItem.addClickHandler(new ReportingClickHandler(map, "featuresWithMap", "clientLayerCountries", "odt"));

		menu.setItems(simpleItem, pdfItem, ooItem);
		ToolStripMenuButton menuButton = new ToolStripMenuButton("Report", menu);
		menuButton.setIcon("[ISOMORPHIC]/images/report.png");
		menuButton.setWidth(100);
		return menuButton;
	}

}
