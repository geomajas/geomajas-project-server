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
package org.geomajas.gwt.example.client.sample.mapwidget;

import java.util.ArrayList;
import java.util.List;

import org.geomajas.configuration.client.ScaleInfo;
import org.geomajas.geometry.Coordinate;
import org.geomajas.gwt.client.Geomajas;
import org.geomajas.gwt.client.controller.AbstractGraphicsController;
import org.geomajas.gwt.client.controller.PanArrowController;
import org.geomajas.gwt.client.controller.PanController;
import org.geomajas.gwt.client.controller.ZoomInAndOutController;
import org.geomajas.gwt.client.controller.ZoomToRectangleOnceController;
import org.geomajas.gwt.client.gfx.paintable.Image;
import org.geomajas.gwt.client.gfx.paintable.mapaddon.MapAddon;
import org.geomajas.gwt.client.gfx.paintable.mapaddon.MapAddonGroup;
import org.geomajas.gwt.client.gfx.paintable.mapaddon.SingleMapAddon;
import org.geomajas.gwt.client.map.MapModel;
import org.geomajas.gwt.client.map.MapView.ZoomOption;
import org.geomajas.gwt.client.util.ImageUtil;
import org.geomajas.gwt.client.util.MapAddonConstants;
import org.geomajas.gwt.client.widget.MapWidget;
import org.geomajas.gwt.example.base.SamplePanel;
import org.geomajas.gwt.example.base.SamplePanelFactory;
import org.geomajas.gwt.example.client.sample.i18n.SampleMessages;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.HumanInputEvent;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.layout.HLayout;
/**
 * <p>
 * Sample that shows custom {@link MapAddonGroup}s and {@link SingleMapAddon}.
 * </p>
 * 
 * @author Emiel Ackermann
 *
 */
public class GroupAndSingleAddonSample extends SamplePanel {

	/**
	 * To keep it readable...
	 */
	private static final Integer PAN_DIA = MapAddonConstants.PAN_DIA;
	private static final Integer PAN_MARGIN = MapAddonConstants.PAN_MARGIN;
	private static final Integer ARROW_DIA = MapAddonConstants.PAN_ARROW_DIA;
	private static final Integer BUTTON_DIA = MapAddonConstants.buttonDia;
	private static final Integer ICON_DIA = MapAddonConstants.ADDON_ICON_DIA;
	private static final Integer MARGIN = MapAddonConstants.ADDON_MARGIN;

	private static final String PAN_ID = "panCollection";
	private static final String NORTH = "north";
	private static final String EAST = "east";
	private static final String SOUTH = "south";
	private static final String WEST = "west";

	private static final String ZOOM_IN = "zoomIn";
	private static final String ZOOM_OUT = "zoomOut";
	private static final String ZOOM_IN_AND_OUT = "zoomInAndOut";
	private static final String ZOOM_TO_MAX = "zoomToMax";
	private static final String ZOOM_TO_RECT = "zoomRect";

	private static final String ICON = "Icon";

	private static final String EXAMPLE_IMAGE_FOLDER = "geomajas/example/image/mapaddon/";
	private static final SampleMessages MESSAGES = GWT
			.create(SampleMessages.class);

	public static final String TITLE = "GroupAndSingleAddon";

	public static final SamplePanelFactory FACTORY = new SamplePanelFactory() {

		public SamplePanel createPanel() {
			return new GroupAndSingleAddonSample();
		}
	};

	@Override
	public Canvas getViewPanel() {
		HLayout mapLayout = new HLayout();
		mapLayout.setShowEdges(true);
		mapLayout.setWidth100();
		mapLayout.setHeight100();

		// Map with ID mapNoNav is defined in the XML configuration.
		final MapWidget mapWidget = new MapWidget("mapNoNav", "gwtExample");
		
		// Set a panning controller on the map:
		mapWidget.setController(new PanController(mapWidget));
		mapLayout.addMember(mapWidget);
		mapWidget.getMapModel().runWhenInitialized(new Runnable() {

			public void run() {
				// Images of pan controller with internal margins
				Image panBg = ImageUtil.createSquareImage("panBg",
						Geomajas.getIsomorphicDir() + "geomajas/mapaddon/panbg.png", 0,
						0, PAN_DIA);
				Image northImage = ImageUtil.createSquareImage(NORTH + ICON,
						Geomajas.getIsomorphicDir() + "geomajas/mapaddon/pan_up.gif",
						PAN_MARGIN, 0, ARROW_DIA);
				Image eastImage = ImageUtil
						.createSquareImage(EAST + ICON, Geomajas.getIsomorphicDir()
								+ "geomajas/mapaddon/pan_right.gif", PAN_MARGIN * 2,
								PAN_MARGIN, ARROW_DIA);
				Image southImage = ImageUtil.createSquareImage(SOUTH + ICON,
						Geomajas.getIsomorphicDir() + "geomajas/mapaddon/pan_down.gif",
						PAN_MARGIN, PAN_MARGIN * 2, ARROW_DIA);
				Image westImage = ImageUtil.createSquareImage(WEST + ICON,
						Geomajas.getIsomorphicDir() + "geomajas/mapaddon/pan_left.gif",
						0, PAN_MARGIN, ARROW_DIA);
		
				// SingleMapAddons or arrows of pan controller
				SingleMapAddon north = new SingleMapAddon(NORTH, northImage, mapWidget,
						new PanArrowController(mapWidget, new Coordinate(0, 1)));
				SingleMapAddon east = new SingleMapAddon(EAST, eastImage, mapWidget,
						new PanArrowController(mapWidget, new Coordinate(1, 0)));
				SingleMapAddon south = new SingleMapAddon(SOUTH, southImage, mapWidget,
						new PanArrowController(mapWidget, new Coordinate(0, -1)));
				SingleMapAddon west = new SingleMapAddon(WEST, westImage, mapWidget,
						new PanArrowController(mapWidget, new Coordinate(-1, 0)));
		
				// Pan group itself
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

				// @extract-start SingleMapAddon, Create a SingleMapAddon
				// Default background for following add ons.
				Image background = ImageUtil.createSquareImage("background",
						Geomajas.getIsomorphicDir() + "geomajas/mapaddon/panbg.png", 0,
						0, BUTTON_DIA);
		
				// Zoom in
				Image in = ImageUtil.createSquareImage(ZOOM_IN + ICON,
						Geomajas.getIsomorphicDir() + "geomajas/osgeo/zoom-in.png",
						MARGIN, MARGIN, ICON_DIA);
				SingleMapAddon zoomIn = new SingleMapAddon(ZOOM_IN, in, background,
						mapWidget, new ZoomInAndOutController(mapWidget, 2));
				// @extract-end
				// no (internal) margins needed; this addon receives its margins from
				// its group
		
				// Zoom out
				Image out = ImageUtil.createSquareImage(ZOOM_OUT + ICON,
						Geomajas.getIsomorphicDir() + "geomajas/osgeo/zoom-out.png",
						MARGIN, MARGIN, ICON_DIA);
				SingleMapAddon zoomOut = new SingleMapAddon(ZOOM_OUT, out, background,
						mapWidget, new ZoomInAndOutController(mapWidget, 0.5));
				// only internal horizontal margin needed
				zoomOut.setHorizontalMargin(BUTTON_DIA + MARGIN);
		
				// Zoom in and out group
				MapAddonGroup zoomInAndOut = new MapAddonGroup(ZOOM_IN_AND_OUT,
						BUTTON_DIA + MARGIN + BUTTON_DIA, BUTTON_DIA, mapWidget);
				zoomInAndOut.addAddon(zoomIn);
				zoomInAndOut.addAddon(zoomOut);
				zoomInAndOut.setHorizontalMargin(MARGIN);
				zoomInAndOut.setVerticalMargin(pan.getVerticalMargin() + PAN_DIA + MARGIN);
				mapWidget.registerMapAddon(zoomInAndOut);
		
				// Zoom out to maximum scale
				Image select = ImageUtil.createSquareImage(ZOOM_TO_MAX + ICON,
						Geomajas.getIsomorphicDir() + EXAMPLE_IMAGE_FOLDER
								+ "zoomMax.png", MARGIN + 1, MARGIN + 1, ICON_DIA);
				SingleMapAddon zoomToMax = new SingleMapAddon(ZOOM_TO_MAX, select,
						background, mapWidget,
						new AbstractGraphicsController(mapWidget) {
		
							@Override
							public void onDown(HumanInputEvent<?> event) {
								event.stopPropagation();
							}
		
							@Override
							public void onUp(HumanInputEvent<?> event) {
								MapModel mapModel = mapWidget.getMapModel();
								List<ScaleInfo> zoomLevels = mapModel.getMapInfo()
										.getScaleConfiguration().getZoomLevels();
								// if zoomslider is used uncomment following to align
								// its handle without IndexOutOfBounds.
								// double scale = 0d;
								// for (ScaleInfo si : zoomLevels) {
								// scale = si.getPixelPerUnit();
								// if
								// (mapWidget.getMapModel().getMapView().isResolutionAvailable(1.0
								// / scale)) {
								// break;
								// }
								// }
								mapModel.getMapView().scale(
										zoomLevels.get(0).getPixelPerUnit(),
										ZoomOption.EXACT);
								event.stopPropagation();
							}
		
							@Override
							public void onDrag(HumanInputEvent<?> event) {
								event.stopPropagation();
							}
		
						});
				zoomToMax.setHorizontalMargin(MARGIN);
				zoomToMax.setVerticalMargin(zoomInAndOut.getVerticalMargin()
						+ BUTTON_DIA + MARGIN);
				mapWidget.registerMapAddon(zoomToMax);
		
				// Zoom to dragged rectangle
				Image rect = ImageUtil.createSquareImage(ZOOM_TO_RECT + ICON,
						Geomajas.getIsomorphicDir()
								+ "geomajas/osgeo/zoom-selection.png", MARGIN + 3,
						MARGIN + 2, ICON_DIA);
				SingleMapAddon zoomToRect = new SingleMapAddon(ZOOM_TO_RECT, rect,
						background, mapWidget, new ZoomToRectangleOnceController(
								mapWidget));
				zoomToRect.setHorizontalMargin(MARGIN + BUTTON_DIA + MARGIN);
				zoomToRect.setVerticalMargin(zoomInAndOut.getVerticalMargin()
						+ BUTTON_DIA + MARGIN);
				mapWidget.registerMapAddon(zoomToRect);
				}
			});
		return mapLayout;
	}

	@Override
	public String getDescription() {
		return MESSAGES.groupAndSingleDescription();
	}

	@Override
	public String[] getConfigurationFiles() {
		return new String[] { "classpath:org/geomajas/gwt/example/context/mapNoNav.xml" };
	}

	@Override
	public String ensureUserLoggedIn() {
		return "luc";
	}

}
