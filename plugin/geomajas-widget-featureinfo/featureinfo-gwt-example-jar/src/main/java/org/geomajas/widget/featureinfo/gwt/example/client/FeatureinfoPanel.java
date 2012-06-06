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

package org.geomajas.widget.featureinfo.gwt.example.client;

import com.google.gwt.core.client.GWT;
import com.smartgwt.client.types.VisibilityMode;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.Img;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.SectionStack;
import com.smartgwt.client.widgets.layout.SectionStackSection;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.toolbar.ToolStrip;

import org.geomajas.gwt.client.util.WidgetLayout;
import org.geomajas.gwt.client.widget.LayerTree;
import org.geomajas.gwt.client.widget.Legend;
import org.geomajas.gwt.client.widget.LocaleSelect;
import org.geomajas.gwt.client.widget.MapWidget;
import org.geomajas.gwt.client.widget.OverviewMap;
import org.geomajas.gwt.client.widget.Toolbar;
import org.geomajas.gwt.example.base.SamplePanel;
import org.geomajas.gwt.example.base.SamplePanelFactory;
import org.geomajas.widget.featureinfo.client.widget.factory.WidgetFactory;
import org.geomajas.widget.featureinfo.gwt.example.client.
		customfeatureinfowidgets.CustomCountriesFeatureInfoCanvasBuilder;
import org.geomajas.widget.featureinfo.gwt.example.client.i18n.ApplicationMessages;

/**
 * Sample to demonstrate use of the featureinfo  plug-in.
 *
 * @author Wout Swartenbroekx
 */
public class FeatureinfoPanel extends SamplePanel {

	public static final ApplicationMessages MESSAGES = GWT.create(ApplicationMessages.class);
	
	public static final String TITLE = "FeatureInfo plug-in";
	
	private OverviewMap overviewMap;

	private Legend legend;
	
	private static final String CUSTOM_COUNTRIES_FEATURE_DETAIL_INFO_BUILDER_KEY =
			"SampleCustomCountriesFeatureDetail";
	
	public static final SamplePanelFactory FACTORY = new SamplePanelFactory() {
		public SamplePanel createPanel() {
			return new FeatureinfoPanel();
		}
	};
	
	/** {@inheritDoc} */
	public Canvas getViewPanel() {
		VLayout mainLayout = new VLayout();
		mainLayout.setWidth100();
		mainLayout.setHeight100();

		// ---------------------------------------------------------------------
		// Top bar:
		// ---------------------------------------------------------------------
		ToolStrip topBar = new ToolStrip();
		topBar.setHeight(33);
		topBar.setWidth100();
		topBar.addSpacer(6);

		Img icon = new Img("[ISOMORPHIC]/geomajas/geomajas_desktopicon_small.png");
		icon.setSize(24);
		topBar.addMember(icon);
		topBar.addSpacer(6);

		Label title = new Label(MESSAGES.applicationTitle("Feature info"));
		title.setStyleName("appTitle");
		title.setWidth(300);
		topBar.addMember(title);
		topBar.addFill();
		topBar.addMember(new LocaleSelect("Nederlands"));

		mainLayout.addMember(topBar);

		HLayout layout = new HLayout();
		layout.setWidth100();
		layout.setHeight100();
		layout.setMembersMargin(5);
		layout.setMargin(5);

		// ---------------------------------------------------------------------
		// Create the left-side (map and tabs):
		// ---------------------------------------------------------------------
		final MapWidget map = new MapWidget("mapMain", "app");
		final Toolbar toolbar = new Toolbar(map);
		toolbar.setButtonSize(WidgetLayout.toolbarLargeButtonSize);

		VLayout mapLayout = new VLayout();
		mapLayout.setShowResizeBar(true);
		mapLayout.setResizeBarTarget("tabs");
		mapLayout.addMember(toolbar);
		mapLayout.addMember(map);
		mapLayout.setHeight("65%");

		VLayout leftLayout = new VLayout();
		leftLayout.setShowEdges(true);
		leftLayout.addMember(mapLayout);

		layout.addMember(leftLayout);

		// ---------------------------------------------------------------------
		// Create the right-side (overview map, layer-tree, legend):
		// ---------------------------------------------------------------------
		final SectionStack sectionStack = new SectionStack();
		sectionStack.setShowEdges(true);
		sectionStack.setVisibilityMode(VisibilityMode.MULTIPLE);
		sectionStack.setCanReorderSections(true);
		sectionStack.setCanResizeSections(false);
		sectionStack.setSize("250px", "100%");

		// Overview map layout:
		SectionStackSection section1 = new SectionStackSection("Overview map");
		section1.setExpanded(true);
		overviewMap = new OverviewMap("mapOverview", "app", map, false, true);
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
		section3.addItem(legend);
		sectionStack.addSection(section3);

		// Putting the right side layouts together:
		layout.addMember(sectionStack);

		registerWidgetBuilders();
		legend.setHeight(200);
		overviewMap.setHeight(200);

		mainLayout.addMember(layout);

		return mainLayout;
	}
	
	private void registerWidgetBuilders() {
		WidgetFactory.put(CUSTOM_COUNTRIES_FEATURE_DETAIL_INFO_BUILDER_KEY,
				new CustomCountriesFeatureInfoCanvasBuilder());
	}
	
	/** {@inheritDoc} */
	public String getDescription() {
		return MESSAGES.applicationDescription();
	}

	/** {@inheritDoc} */
	public String[] getConfigurationFiles() {
		return new String[]{"classpath:WEB-INF/applicationContext.xml",
		"classpath:WEB-INF/mapMain.xml",
		"classpath:WEB-INF/mapOverview.xml",
		"classpath:WEB-INF/clientLayerOsm.xml",
		"classpath:WEB-INF/clientLayerCountries.xml",
		"classpath:WEB-INF/clientLayerCountriesWms.xml",
		"classpath:WEB-INF/layerOsm.xml",
		"classpath:WEB-INF/layerCountries.xml",
		"classpath:WEB-INF/layerCountriesWms.xml",
		"classpath:WEB-INF/clientLayerPopulatedPlaces110m.xml",
		"classpath:WEB-INF/layerPopulatedPlaces110m.xml"};
	}

	/** {@inheritDoc} */
	public String ensureUserLoggedIn() {
		return "luc";
	}
}
