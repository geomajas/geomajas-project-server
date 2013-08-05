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
package org.geomajas.smartgwt.example.client.sample.mapwidget;

import java.util.ArrayList;
import java.util.List;

import org.geomajas.geometry.Coordinate;
import org.geomajas.smartgwt.client.Geomajas;
import org.geomajas.smartgwt.client.controller.PanArrowController;
import org.geomajas.smartgwt.client.controller.PanController;
import org.geomajas.smartgwt.client.controller.ZoomInAndOutController;
import org.geomajas.smartgwt.client.gfx.paintable.Image;
import org.geomajas.smartgwt.client.gfx.paintable.mapaddon.MapAddon;
import org.geomajas.smartgwt.client.gfx.paintable.mapaddon.MapAddonGroup;
import org.geomajas.smartgwt.client.gfx.paintable.mapaddon.SingleMapAddon;
import org.geomajas.smartgwt.client.gfx.paintable.mapaddon.ZoomSlider;
import org.geomajas.smartgwt.client.util.ImageUtil;
import org.geomajas.smartgwt.client.util.MapAddonConstants;
import org.geomajas.smartgwt.client.widget.MapWidget;
import org.geomajas.smartgwt.example.base.SamplePanel;
import org.geomajas.smartgwt.example.base.SamplePanelFactory;
import org.geomajas.smartgwt.example.client.sample.i18n.SampleMessages;

import com.google.gwt.core.client.GWT;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.layout.HLayout;

/**
 * <p>
 * Sample that shows a custom pan controller and zoom slider.
 * </p>
 * 
 * @author Emiel Ackermann
 */
public class PanAndZoomSliderSample extends SamplePanel {

	/**
	 * To keep it readable...
	 */
	private static final Integer PAN_DIA = MapAddonConstants.PAN_DIA;
	private static final Integer PAN_MARGIN = MapAddonConstants.PAN_MARGIN;
	private static final Integer ARROW_DIA = MapAddonConstants.PAN_ARROW_DIA;
	private static final Integer BUTTON_DIA = MapAddonConstants.buttonDia;
	private static final Integer MARGIN = MapAddonConstants.ADDON_MARGIN;
	private static final Integer HORIZONTAL_MIDDLE = MARGIN + BUTTON_DIA + MARGIN / 2 + 1;

	private static final Integer SLIDER_WIDTH = 16;
	private static final Integer SLIDER_PART_HEIGHT = 10;

	private static final Integer SLIDER_UNIT_WIDTH = 20;
	private static final Integer SLIDER_UNIT_HEIGHT = 10;

	private static final String PAN_ID = "panCollection";
	private static final String NORTH = "north";
	private static final String EAST = "east";
	private static final String SOUTH = "south";
	private static final String WEST = "west";

	private static final String ZOOM_IN = "zoomIn";
	private static final String ZOOM_OUT = "zoomOut";

	private static final String ICON = "Icon";

	private static final String EXAMPLE_IMAGE_FOLDER = "geomajas/example/image/mapaddon/";
	private static final SampleMessages MESSAGES = GWT
			.create(SampleMessages.class);

	public static final String TITLE = "PanAndZoomSlider";

	public static final SamplePanelFactory FACTORY = new SamplePanelFactory() {

		public SamplePanel createPanel() {
			return new PanAndZoomSliderSample();
		}
	};

	@Override
	public Canvas getViewPanel() {
		HLayout mapLayout = new HLayout();
		mapLayout.setShowEdges(true);
		mapLayout.setWidth100();
		mapLayout.setHeight100();

		// Map with ID mapNoNav is defined in the XML configuration.
		// (mapNoNav.xml)
		final MapWidget mapWidget = new MapWidget("mapNoNav", "gwtExample");

		// Set a panning controller on the map:
		mapWidget.setController(new PanController(mapWidget));
		mapLayout.addMember(mapWidget);
		
		mapWidget.getMapModel().runWhenInitialized(new Runnable() {

			public void run() {
				/*
				 * Images of pan controller with internal margins
				 */
				Image panBg = ImageUtil.createSquareImage("panBg",
						Geomajas.getIsomorphicDir() + "geomajas/mapaddon/panbg.png", 0,
						0, PAN_DIA);
				Image northImage = ImageUtil.createSquareImage(NORTH + ICON,
						Geomajas.getIsomorphicDir() + EXAMPLE_IMAGE_FOLDER
								+ "pan_up.png", PAN_MARGIN, 0, ARROW_DIA);
				Image eastImage = ImageUtil.createSquareImage(EAST + ICON,
						Geomajas.getIsomorphicDir() + EXAMPLE_IMAGE_FOLDER
								+ "pan_right.png", PAN_MARGIN * 2, PAN_MARGIN,
						ARROW_DIA);
				Image southImage = ImageUtil
						.createSquareImage(SOUTH + ICON, Geomajas.getIsomorphicDir()
								+ EXAMPLE_IMAGE_FOLDER + "pan_down.png", PAN_MARGIN,
								PAN_MARGIN * 2, ARROW_DIA);
				Image westImage = ImageUtil.createSquareImage(WEST + ICON,
						Geomajas.getIsomorphicDir() + EXAMPLE_IMAGE_FOLDER
								+ "pan_left.png", 0, PAN_MARGIN, ARROW_DIA);
		
				/*
				 * SingleMapAddons or arrows of pan controller
				 */
				SingleMapAddon north = new SingleMapAddon(NORTH, northImage, mapWidget,
						new PanArrowController(mapWidget, new Coordinate(0, 1)));
				SingleMapAddon east = new SingleMapAddon(EAST, eastImage, mapWidget,
						new PanArrowController(mapWidget, new Coordinate(1, 0)));
				SingleMapAddon south = new SingleMapAddon(SOUTH, southImage, mapWidget,
						new PanArrowController(mapWidget, new Coordinate(0, -1)));
				SingleMapAddon west = new SingleMapAddon(WEST, westImage, mapWidget,
						new PanArrowController(mapWidget, new Coordinate(-1, 0)));
		
				/*
				 * Pan group itself
				 */
				List<MapAddon> panArrows = new ArrayList<MapAddon>();
				panArrows.add(north);
				panArrows.add(east);
				panArrows.add(south);
				panArrows.add(west);
		
				MapAddonGroup pan = new MapAddonGroup(PAN_ID, panArrows, panBg,
						mapWidget);
				pan.setHorizontalMargin(MARGIN * 2);
				pan.setVerticalMargin(MARGIN * 2);
				mapWidget.registerMapAddon(pan);
		
				// START ZOOMSLIDER

				// @extract-start ZoomSliderMapAddon, ZoomSlider map add-on
				// Slider handler icon
				Image sliderUnit = ImageUtil.createRectangleImage(
						ZoomSlider.SLIDER_UNIT + "icon", Geomajas.getIsomorphicDir()
								+ EXAMPLE_IMAGE_FOLDER + "sliderUnit.png", 0, 0,
						SLIDER_UNIT_WIDTH, SLIDER_UNIT_HEIGHT);

				// Part of the background on which the zoom slider handler can move.
				// One zoom level is represented by one image.
				Image backgroundPart = ImageUtil.createRectangleImage(ZoomSlider.SLIDER + "Bg",
						Geomajas.getIsomorphicDir() + EXAMPLE_IMAGE_FOLDER + "sliderbg.png", 0, 0,
						SLIDER_WIDTH, SLIDER_PART_HEIGHT);

				// Zoom in; the top of ZoomSlider
				Image in = ImageUtil.createSquareImage(ZOOM_IN + ICON,
						Geomajas.getIsomorphicDir() + "geomajas/mapaddon/zoomPlus.png",
						0, 0, SLIDER_WIDTH);
				Image sliderTop = ImageUtil.createSquareImage(
						ZoomSlider.SLIDER + "Top",
						Geomajas.getIsomorphicDir() + EXAMPLE_IMAGE_FOLDER + "sliderbgtop.png", 0, 0, SLIDER_WIDTH);
				SingleMapAddon zoomIn = new SingleMapAddon(ZOOM_IN, in, sliderTop,
						mapWidget, new ZoomInAndOutController(mapWidget, 1.01));
		
				// Zoom out; the bottom of ZoomSlider
				Image out = ImageUtil.createRectangleImage(ZOOM_OUT + ICON,
						Geomajas.getIsomorphicDir() + "geomajas/mapaddon/zoomMinus.png", 
						0, 0, SLIDER_WIDTH, SLIDER_PART_HEIGHT);
				Image sliderBottom = ImageUtil.createRectangleImage(ZoomSlider.SLIDER + "Bottom",
						Geomajas.getIsomorphicDir() + EXAMPLE_IMAGE_FOLDER + "sliderbgbottom.png", 0, 0,
						SLIDER_WIDTH, SLIDER_PART_HEIGHT);
				SingleMapAddon zoomOut = new SingleMapAddon(ZOOM_OUT, out,
						sliderBottom, mapWidget, new ZoomInAndOutController(mapWidget, 0.99));
		
				// Zoom slider itself
				ZoomSlider slider = new ZoomSlider(ZoomSlider.SLIDER, mapWidget);
				slider.setZoomIn(zoomIn);
				slider.setBackgroundPart(backgroundPart);
				slider.setSliderUnit(sliderUnit);
				slider.setZoomOut(zoomOut);
				slider.setHorizontalMargin(HORIZONTAL_MIDDLE - SLIDER_WIDTH / 2);
				slider.setVerticalMargin(pan.getVerticalMargin() + PAN_DIA + 2 * MARGIN);
				mapWidget.registerMapAddon(slider);
				mapWidget.getMapModel().getMapView().addMapViewChangedHandler(slider);
				// @extract-end
			}
		});

		return mapLayout;
	}

	@Override
	public String getDescription() {
		return MESSAGES.panAndSliderDescription();
	}

	@Override
	public String[] getConfigurationFiles() {
		return new String[] { "classpath:org/geomajas/smartgwt/example/context/mapNoNav.xml" };
	}

	@Override
	public String ensureUserLoggedIn() {
		return "luc";
	}

}
