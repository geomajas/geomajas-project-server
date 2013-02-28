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

package org.geomajas.plugin.printing.gwt.example.client;

import org.geomajas.geometry.Coordinate;
import org.geomajas.geometry.Geometry;
import org.geomajas.geometry.service.WktException;
import org.geomajas.geometry.service.WktService;
import org.geomajas.gwt.client.gfx.paintable.Circle;
import org.geomajas.gwt.client.gfx.paintable.GfxGeometry;
import org.geomajas.gwt.client.gfx.paintable.Image;
import org.geomajas.gwt.client.gfx.paintable.Rectangle;
import org.geomajas.gwt.client.gfx.paintable.Text;
import org.geomajas.gwt.client.gfx.style.FontStyle;
import org.geomajas.gwt.client.gfx.style.PictureStyle;
import org.geomajas.gwt.client.gfx.style.ShapeStyle;
import org.geomajas.gwt.client.map.event.MapModelChangedEvent;
import org.geomajas.gwt.client.map.event.MapModelChangedHandler;
import org.geomajas.gwt.client.spatial.Bbox;
import org.geomajas.gwt.client.util.GeometryConverter;
import org.geomajas.gwt.client.widget.MapWidget;
import org.geomajas.gwt.client.widget.Toolbar;
import org.geomajas.gwt.example.base.SamplePanel;
import org.geomajas.gwt.example.base.SamplePanelFactory;
import org.geomajas.plugin.printing.gwt.example.client.i18n.PrintingMessages;

import com.google.gwt.core.client.GWT;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.Img;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.toolbar.ToolStrip;

/**
 * Sample to demonstrate use of the printing plugin.
 * 
 * @author Jan De Moerloose
 */
public class PrintingPanel extends SamplePanel {

	public static final PrintingMessages MESSAGES = GWT.create(PrintingMessages.class);

	public static final String TITLE = "Printing";

	public static final SamplePanelFactory FACTORY = new SamplePanelFactory() {

		public SamplePanel createPanel() {
			return new PrintingPanel();
		}
	};

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

		Label title = new Label("Geomajas, Simple printing GWT example");
		title.setStyleName("sgwtTitle");
		title.setWidth(400);
		topBar.addMember(title);

		mainLayout.addMember(topBar);

		HLayout layout = new HLayout();
		layout.setWidth100();
		layout.setHeight100();
		layout.setMembersMargin(5);
		layout.setMargin(5);

		// ---------------------------------------------------------------------
		// Create the left-side (map and tabs):
		// ---------------------------------------------------------------------
		final MapWidget map = new MapWidget("mapPrinting", "appPrinting");
		final Toolbar toolbar = new Toolbar(map);
		toolbar.setButtonSize(Toolbar.BUTTON_SIZE_BIG);
		map.getMapModel().runWhenInitialized(new Runnable() {

			public void run() {
				map.getMapModel().getVectorLayer("clientLayerCountriesPrinting").setLabeled(true);
			}
		});

		map.getMapModel().addMapModelChangedHandler(new AddWorldPaintables(map));

		VLayout mapLayout = new VLayout();
		mapLayout.addMember(toolbar);
		mapLayout.addMember(map);
		mapLayout.setHeight("100%");

		layout.addMember(mapLayout);

		// ---------------------------------------------------------------------
		// Finally draw everything:
		// ---------------------------------------------------------------------
		return layout;
	}

	@Override
	public String getDescription() {
		return MESSAGES.printDescription();
	}

	@Override
	public String[] getConfigurationFiles() {
		return new String[] { "classpath:org/geomajas/plugin/printing/gwt/example/context/appPrinting.xml",
				"classpath:org/geomajas/plugin/printing/gwt/example/context/mapPrinting.xml",
				"classpath:org/geomajas/plugin/printing/gwt/example/context/layerWmsPrinting.xml",
				"classpath:org/geomajas/plugin/printing/gwt/example/context/layerCountriesPrinting.xml",
				"classpath:org/geomajas/plugin/printing/gwt/example/context/clientLayerCountriesPrinting.xml",
				"classpath:org/geomajas/plugin/printing/gwt/example/context/clientLayerWmsPrinting.xml" };
	}

	@Override
	public String ensureUserLoggedIn() {
		return "luc";
	}

	/**
	 * Add some world paintables when map is loaded.
	 */
	class AddWorldPaintables implements MapModelChangedHandler {

		private MapWidget map;

		public AddWorldPaintables(MapWidget map) {
			this.map = map;
		}

		/**
		 * add some world paintables, these will be printed too !!!
		 */
		@Override
		public void onMapModelChanged(MapModelChangedEvent event) {
			try {
				Rectangle bg = new Rectangle("background");
				bg.setBounds(new Bbox(0, 0, 4000000, 4000000));
				bg.setStyle(new ShapeStyle("#FFFFFF", 1f, "#FFFFFF", 1f, 2));
				map.registerWorldPaintable(bg);
				Rectangle rect = new Rectangle("my rect");
				rect.setBounds(new Bbox(0, 0, 1000000, 1000000));
				rect.setStyle(createStyle("#FF0000"));
				map.registerWorldPaintable(rect);
				Circle circle = new Circle("my circle");
				circle.setPosition(new Coordinate(1500000, 1500000));
				circle.setRadius(500000f);
				circle.setStyle(createStyle("#00FF00"));
				map.registerWorldPaintable(circle);
				GfxGeometry triangle = new GfxGeometry("triangle");
				Geometry geom;
				geom = WktService
						.toGeometry("POLYGON ((2000000 2000000, 3000000 2000000,3000000 3000000,2000000 2000000))");
				triangle.setGeometry(GeometryConverter.toGwt(geom));
				triangle.setStyle(createStyle("#FFFF00"));
				map.registerWorldPaintable(triangle);
				Image image = new Image("osgeo");
				image.setHref("http://live.osgeo.org/_images/logo-geomajas.png");
				image.setBounds(new Bbox(0, 2000000, 1000000, 1000000));
				image.setStyle(new PictureStyle(0.7));
				map.registerWorldPaintable(image);
				Text text = new Text("my text", "Hello world", new Coordinate(2000000, 2000000), new FontStyle(
						"#00FFF0", 50, "Verdana", "bold", "italic"));
				map.registerWorldPaintable(text);
			} catch (WktException e) {
			}
		}

		private ShapeStyle createStyle(String color) {
			return new ShapeStyle(color, 0.5f, color, 0.8f, 2);
		}

	}

}
