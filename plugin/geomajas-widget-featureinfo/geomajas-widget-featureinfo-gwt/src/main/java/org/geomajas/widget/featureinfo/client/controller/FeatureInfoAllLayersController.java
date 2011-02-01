/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2011 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */

package org.geomajas.widget.featureinfo.client.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.geomajas.command.CommandResponse;
import org.geomajas.command.dto.SearchByLocationRequest;
import org.geomajas.command.dto.SearchByLocationResponse;
import org.geomajas.geometry.Coordinate;
import org.geomajas.gwt.client.command.CommandCallback;
import org.geomajas.gwt.client.command.GwtCommand;
import org.geomajas.gwt.client.command.GwtCommandDispatcher;
import org.geomajas.gwt.client.controller.AbstractGraphicsController;
import org.geomajas.gwt.client.i18n.I18nProvider;
import org.geomajas.gwt.client.map.MapModel;
import org.geomajas.gwt.client.map.feature.Feature;
import org.geomajas.gwt.client.map.layer.Layer;
import org.geomajas.gwt.client.map.layer.VectorLayer;
import org.geomajas.gwt.client.spatial.Mathlib;
import org.geomajas.gwt.client.spatial.WorldViewTransformer;
import org.geomajas.gwt.client.spatial.geometry.Point;
import org.geomajas.gwt.client.util.GeometryConverter;
import org.geomajas.gwt.client.widget.MapWidget;
import org.geomajas.widget.featureinfo.client.FeatureInfoMessages;
import org.geomajas.widget.featureinfo.client.widget.FeatClickHandler;
import org.geomajas.widget.featureinfo.client.widget.FeatureAttributeCanvas;
import org.geomajas.widget.featureinfo.client.widget.NearbyFeaturesList;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.MouseUpEvent;
import com.smartgwt.client.types.Overflow;
import com.smartgwt.client.types.VisibilityMode;
import com.smartgwt.client.widgets.Window;
import com.smartgwt.client.widgets.layout.SectionStack;
import com.smartgwt.client.widgets.layout.SectionStackSection;

/**
 * Shows information of the features (per visible vector layer) near the position where the user clicked on the map.
 * First a list of all the appropriate features is shown in a floating window. Clicking on a feature of the list shows
 * its attributes in a feature attribute window under the list window. As a starting point for this class the
 * org.geomajas.gwt.client.controller.FeatureInfoController was used.
 * 
 * @author An Buyle
 */
public class FeatureInfoAllLayersController extends AbstractGraphicsController {

	private FeatureInfoMessages messages = GWT.create(FeatureInfoMessages.class);

	private boolean useSepDetailsWindow;
	
	/** Number of pixels that describes the tolerance allowed when trying to select features. */
	private int pixelTolerance;

	private NearbyFeaturesList nearbyFeaturesList;

	private Window window; /* top window for this widget or window with feature list */

	private FeatureAttributeCanvas featureAttributeCanvas;

	private SectionStackSection detailsSection;

	public FeatureInfoAllLayersController(MapWidget mapWidget, int pixelTolerance, boolean useSepDetailsWindow) {
		super(mapWidget);
		this.pixelTolerance = pixelTolerance;
		this.useSepDetailsWindow = useSepDetailsWindow;
	}

	public int getPixelTolerance() {
		return pixelTolerance;
	}

	public void setPixelTolerance(int pixelTolerance) {
		this.pixelTolerance = pixelTolerance;
	}

	public boolean isUseSepDetailsWindow() {
		return useSepDetailsWindow;
	}

	
	public void setUseSepDetailsWindow(boolean useSepDetailsWindow) {
		this.useSepDetailsWindow = useSepDetailsWindow;
	}

	/**
	 * On mouse up, execute the search by location, and display a
	 * {@link org.geomajas.widget.featureinfo.client.widget.NearbyFeaturesList} if features are found.
	 */
	public void onMouseUp(MouseUpEvent event) {
		Coordinate worldPosition = getWorldPosition(event);
		Point point = mapWidget.getMapModel().getGeometryFactory().createPoint(worldPosition);

		SearchByLocationRequest request = new SearchByLocationRequest();
		request.setLocation(GeometryConverter.toDto(point));
		request.setCrs(mapWidget.getMapModel().getCrs());
		request.setQueryType(SearchByLocationRequest.QUERY_INTERSECTS);
		request.setSearchType(SearchByLocationRequest.SEARCH_ALL_LAYERS);
		request.setBuffer(calculateBufferFromPixelTolerance());
		request.setFeatureIncludes(GwtCommandDispatcher.getInstance().getLazyFeatureIncludesSelect());
		request.setLayerIds(getServerLayerIds(mapWidget.getMapModel()));
		Layer<?> layer = mapWidget.getMapModel().getSelectedLayer();
		if (null != layer && layer instanceof VectorLayer) {
			request.setFilter(((VectorLayer) layer).getFilter());
		}

		GwtCommand commandRequest = new GwtCommand("command.feature.SearchByLocation");
		commandRequest.setCommandRequest(request);
		GwtCommandDispatcher.getInstance().execute(commandRequest, new CommandCallback() {

			public void execute(CommandResponse commandResponse) {
				if (commandResponse instanceof SearchByLocationResponse) {
					SearchByLocationResponse response = (SearchByLocationResponse) commandResponse;
					Map<String, List<org.geomajas.layer.feature.Feature>> featureMap = response.getFeatureMap();

					if (window != null) {
						mapWidget.removeChild(window);
					}
					window = new Window();
					FeatClickHandler featClickHandler = null;

					if (!useSepDetailsWindow) {
						featureAttributeCanvas = new FeatureAttributeCanvas(null/* feature */, false/* noedit */,
								mapWidget.getHeight() / 2);

						detailsSection = new SectionStackSection(messages.nearbyFeaturesDetailsSectionTitle());
						/* handler for when a feature in the feature list is clicked-on */
						featClickHandler = new FeatClickHandler() {

							public void onClick(Feature feat) {
								detailsSection.setTitle(I18nProvider.getAttribute().getAttributeWindowTitle(
										feat.getLabel()));
								featureAttributeCanvas.setFeature(feat, mapWidget.getHeight() / 2);
								detailsSection.setExpanded(true);
							}
						};
					} else {
						featClickHandler = new FeatClickHandler() {

							public void onClick(Feature feat) {
								createSepDetailsWindow(feat);
							}

						};

					}
					nearbyFeaturesList = new NearbyFeaturesList(mapWidget, featureMap, featClickHandler);

					window.setTitle(messages.nearbyFeaturesWindowTitle());
					window.setWidth("300px"); // TODO: configurable/calculate appropriate value at run-time
					if (!useSepDetailsWindow) {
						window.setHeight("420px"); // TODO: configurable/calculate appropriate value at run-time
					} else {
						window.setHeight("320px"); // TODO: configurable/calculate appropriate value at run-time
					}
					window.setMaxHeight(mapWidget.getHeight() - 10);
					// window.setAutoHeight();
					// window.setOverflow(Overflow.VISIBLE);
					window.setPageTop(mapWidget.getAbsoluteTop() + 10);
					window.setPageLeft(mapWidget.getAbsoluteLeft() + 10);
					window.setCanDragReposition(true);
					window.setCanDragResize(true);

					if (!useSepDetailsWindow) {
						SectionStack featureInfoStack = new SectionStack();
						featureInfoStack.setVisibilityMode(VisibilityMode.MULTIPLE);
						featureInfoStack.setWidth100();
						featureInfoStack.setHeight100();
						// featureInfoStack.setAutoHeight();
						featureInfoStack.setOverflow(Overflow.VISIBLE);

						SectionStackSection listSection = new SectionStackSection(messages
								.nearbyFeaturesListSectionTitle());
						listSection.setExpanded(true);

						listSection.addItem(nearbyFeaturesList.getCanvas()); /*
																			 * do NOT use addChild because then the
																			 * layout of window is lost (window title
																			 * and close button)
																			 */

						featureInfoStack.addSection(listSection);

						detailsSection.setExpanded(false); /* initially minimized view */
						// featureInfoStack.setOverflow(Overflow.AUTO);

						detailsSection.addItem(featureAttributeCanvas); // initially the
																		// featureAttributeCanvas is empty
						featureInfoStack.addSection(detailsSection);

						window.addItem(featureInfoStack);
					} else {
						window.addItem(nearbyFeaturesList.getCanvas());
					}
					mapWidget.addChild(window);
					window.markForRedraw();
					mapWidget.redraw();
				}
			}
		});
	}

	// -------------------------------------------------------------------------
	// Private methods:
	// -------------------------------------------------------------------------

	private String[] getServerLayerIds(MapModel mapModel) {
		List<String> layerIds = new ArrayList<String>();
		for (Layer<?> layer : mapModel.getLayers()) {
			if (layer.isShowing()) {
				layerIds.add(layer.getServerLayerId());
			}
		}
		return layerIds.toArray(new String[] {});
	}

	private double calculateBufferFromPixelTolerance() {
		WorldViewTransformer transformer = mapWidget.getMapModel().getMapView().getWorldViewTransformer();
		Coordinate c1 = transformer.viewToWorld(new Coordinate(0, 0));
		Coordinate c2 = transformer.viewToWorld(new Coordinate(pixelTolerance, 0));
		return Mathlib.distance(c1, c2);
	}

	private void createSepDetailsWindow(Feature feat) {
		FeatureAttributeCanvas featureAttributeCanvas = new FeatureAttributeCanvas(feat, false/* noedit */,
				mapWidget.getHeight() / 2);

		Window detailWindow = new Window();
		detailWindow.setTitle(I18nProvider.getAttribute().getAttributeWindowTitle(feat.getLabel()));
		detailWindow.addItem(featureAttributeCanvas);

		detailWindow.setWidth("300px"); // TODO: configurable/calculate appropriate value at run-time
		
		int heightDetailWindow = 300;
		if (feat.getAttributes().size() <= 4) {
			detailWindow.setHeight("300px"); // TODO: configurable/calculate appropriate value at run-time
			heightDetailWindow = 300;
		} else {
			detailWindow.setHeight("350px"); // TODO: configurable/calculate appropriate value at run-time
			heightDetailWindow = 350;
		}

		detailWindow.setMaxHeight(mapWidget.getHeight() - 10);
		/* Check if it's OK to place the detail window under the feature list,
		 * else place it at the left with some overlap 
		 */

		// TODO: organize detail windows (now they will overlap because they all have the same left-top coordinate
		int absoluteTop = window.getAbsoluteTop() + window.getHeight();
		int absoluteLeft = mapWidget.getAbsoluteLeft() + 10;

		try {
			heightDetailWindow = detailWindow.getHeight();
		} catch (Exception e) {
			// Do nothing
		}
		if (absoluteTop + heightDetailWindow > mapWidget.getAbsoluteTop() + mapWidget.getHeight()) {
			absoluteTop = window.getAbsoluteTop();
			absoluteLeft = mapWidget.getAbsoluteLeft() + (window.getWidth() * 2) / 3;
		}

		detailWindow.setPageTop(absoluteTop);
		detailWindow.setPageLeft(absoluteLeft);
		detailWindow.setCanDragReposition(true);
		detailWindow.setCanDragResize(true);

		mapWidget.addChild(detailWindow);
		detailWindow.markForRedraw();
		mapWidget.redraw();
	}

}
