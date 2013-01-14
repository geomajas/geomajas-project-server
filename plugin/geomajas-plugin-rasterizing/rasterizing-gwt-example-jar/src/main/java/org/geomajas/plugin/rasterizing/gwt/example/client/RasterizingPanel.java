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
package org.geomajas.plugin.rasterizing.gwt.example.client;

import org.geomajas.gwt.client.gfx.style.ShapeStyle;
import org.geomajas.gwt.client.map.event.LayerDeselectedEvent;
import org.geomajas.gwt.client.map.event.LayerSelectedEvent;
import org.geomajas.gwt.client.map.event.LayerSelectionHandler;
import org.geomajas.gwt.client.map.event.MapModelChangedEvent;
import org.geomajas.gwt.client.map.event.MapModelChangedHandler;
import org.geomajas.gwt.client.map.layer.Layer;
import org.geomajas.gwt.client.util.WidgetLayout;
import org.geomajas.gwt.client.widget.LayerTree;
import org.geomajas.gwt.client.widget.Legend;
import org.geomajas.gwt.client.widget.MapWidget;
import org.geomajas.gwt.client.widget.OverviewMap;
import org.geomajas.gwt.client.widget.Toolbar;
import org.geomajas.gwt.example.base.SamplePanel;
import org.geomajas.gwt.example.base.SamplePanelFactory;
import org.geomajas.plugin.rasterizing.gwt.example.client.i18n.RasterizingMessages;

import com.google.gwt.core.client.GWT;
import com.smartgwt.client.types.VisibilityMode;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.SectionStack;
import com.smartgwt.client.widgets.layout.SectionStackSection;
import com.smartgwt.client.widgets.layout.VLayout;

/**
 * {@link SamplePanel} for rasterizing example.
 * 
 * @author Jan De Moerloose
 * 
 */
public class RasterizingPanel extends SamplePanel {

	public static final String TITLE = "Rasterizing";

	public static final RasterizingMessages MESSAGES = GWT.create(RasterizingMessages.class);

	public static final SamplePanelFactory FACTORY = new SamplePanelFactory() {

		public SamplePanel createPanel() {
			return new RasterizingPanel();
		}
	};

	/**
	 * Get view panel.
	 * 
	 * @return The viewPanel Canvas
	 */
	public Canvas getViewPanel() {
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
		final MapWidget map = new MapWidget("mapRasterizingMain", "appRasterizing");
		final Toolbar toolbar = new Toolbar(map);
		toolbar.setButtonSize(WidgetLayout.toolbarLargeButtonSize);
		toolbar.setBackgroundColor("#647386");
		toolbar.setBackgroundImage("");
		toolbar.setBorder("0px");

		map.getMapModel().addMapModelChangedHandler(new MapModelChangedHandler() {

			public void onMapModelChanged(MapModelChangedEvent event) {
				// switch all labels on
				for (Layer layer : map.getMapModel().getLayers()) {
					layer.setLabeled(true);
				}
			}
		});

		map.getMapModel().addLayerSelectionHandler(new LayerSelectionHandler() {

			public void onSelectLayer(LayerSelectedEvent event) {
				for (Layer layer : map.getMapModel().getLayers()) {
					if (layer.isSelected()) {
						layer.setVisible(true);
					} else {
						layer.setVisible(false);
					}
				}
			}

			public void onDeselectLayer(LayerDeselectedEvent event) {
			}
		});

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

		// Overview map layout:
		SectionStackSection section1 = new SectionStackSection("Overview map");
		section1.setExpanded(true);
		OverviewMap overviewMap = new OverviewMap("mapRasterizingOverview", "appRasterizing", map, false, true);
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
		Legend legend = new Legend(map.getMapModel());
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
		return mainLayout;
	}

	public String getDescription() {
		return MESSAGES.rasterizingDescription();
	}

	public String[] getConfigurationFiles() {
		return new String[] { "classpath:org/geomajas/plugin/rasterizing/gwt/example/context/appRasterizing.xml",
				"classpath:org/geomajas/plugin/rasterizing/gwt/example/context/mapRasterizingMain.xml",
				"classpath:org/geomajas/plugin/rasterizing/gwt/example/context/mapRasterizingOverview.xml",
				"classpath:org/geomajas/plugin/rasterizing/gwt/example/context/clientLayersRasterizing.xml",
				"classpath:org/geomajas/plugin/rasterizing/gwt/example/context/layerOsm.xml",
				"classpath:org/geomajas/plugin/rasterizing/gwt/example/context/layerPointsRasterizing.xml",
				"classpath:org/geomajas/plugin/rasterizing/gwt/example/context/layerLinesRasterizing.xml",
				"classpath:org/geomajas/plugin/rasterizing/gwt/example/context/layerPolygonsRasterizing.xml",
				"classpath:org/geomajas/plugin/rasterizing/gwt/example/context/rasterizing.xml",
				"classpath:org/geomajas/plugin/rasterizing/gwt/example/context/resources.xml" };
	}

	public String ensureUserLoggedIn() {
		return "luc";
	}
}
