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
import org.geomajas.command.dto.SearchLayersByPointRequest;
import org.geomajas.command.dto.SearchLayersByPointResponse;
import org.geomajas.geometry.Coordinate;
import org.geomajas.gwt.client.command.CommandCallback;
import org.geomajas.gwt.client.command.GwtCommand;
import org.geomajas.gwt.client.command.GwtCommandDispatcher;
import org.geomajas.gwt.client.controller.FeatureInfoController;
import org.geomajas.gwt.client.map.MapModel;
import org.geomajas.gwt.client.map.layer.Layer;
import org.geomajas.gwt.client.map.layer.VectorLayer;
import org.geomajas.gwt.client.spatial.Bbox;
import org.geomajas.gwt.client.spatial.Mathlib;
import org.geomajas.gwt.client.spatial.WorldViewTransformer;
import org.geomajas.gwt.client.spatial.geometry.Point;
import org.geomajas.gwt.client.util.GeometryConverter;
import org.geomajas.gwt.client.widget.MapWidget;
import org.geomajas.widget.featureinfo.client.FeatureInfoMessages;
import org.geomajas.widget.featureinfo.client.widget.MultiLayerFeatureInfoWindow;
import org.geomajas.widget.featureinfo.client.widget.Notify;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseMoveEvent;
import com.google.gwt.event.dom.client.MouseUpEvent;
import com.smartgwt.client.widgets.Window;

/**
 * Shows information of the features (per visible vector layer) near the position where the user clicked on the map.
 * First a list of all the appropriate features is shown in a floating window. Clicking on a feature of the list shows
 * its attributes in a feature attribute window under the list window. As a starting point for this class the
 * org.geomajas.gwt.client.controller.FeatureInfoController was used.
 * 
 * @author An Buyle
 * @author Oliver May
 * @author Kristof Heirwegh
 */
public class MultiLayerFeatureInfoController extends FeatureInfoController {

	private boolean dragging;
	private boolean clickstart;
	private boolean includeRasterLayers = true;
	private FeatureInfoMessages messages = GWT.create(FeatureInfoMessages.class);

	/**
	 * Number of pixels that describes the tolerance allowed when trying to
	 * select features.
	 */
	private int pixelTolerance;

	public MultiLayerFeatureInfoController(MapWidget mapWidget, int pixelTolerance) {
		super(mapWidget, pixelTolerance);
		this.pixelTolerance = pixelTolerance;
	}

	public int getPixelTolerance() {
		return pixelTolerance;
	}

	public void setPixelTolerance(int pixelTolerance) {
		this.pixelTolerance = pixelTolerance;
	}

	@Override
	public void onMouseDown(MouseDownEvent event) {
		if (event.getNativeButton() != NativeEvent.BUTTON_RIGHT) {
			clickstart = true;
		}
	}

	@Override
	public void onMouseMove(MouseMoveEvent event) {
		if (clickstart) {
			dragging = true;
		}
	}

	/**
	 * On mouse up, execute the search by location, and display a
	 * {@link org.geomajas.widget.featureinfo.client.widget.MultiLayerFeaturesList}
	 * if features are found.
	 */
	public void onMouseUp(MouseUpEvent event) {
		if (clickstart && !dragging) {
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
			for (Layer<?> layer : mapWidget.getMapModel().getLayers()) {
				if (layer.isShowing() && layer instanceof VectorLayer) {
					request.setFilter(layer.getServerLayerId(), ((VectorLayer) layer).getFilter());
				}
			}

			final SearchLayersByPointRequest rasterLayerRequest = new SearchLayersByPointRequest();
			rasterLayerRequest.setLocation(point.getCoordinate());
			rasterLayerRequest.setCrs(mapWidget.getMapModel().getCrs());
			rasterLayerRequest.setSearchType(SearchByLocationRequest.SEARCH_ALL_LAYERS);
			rasterLayerRequest.setPixelTolerance(pixelTolerance);
			rasterLayerRequest.setLayerIds(getServerLayerIds(mapWidget.getMapModel()));
			rasterLayerRequest.setBbox(toBbox(mapWidget.getMapModel().getMapView().getBounds()));
			rasterLayerRequest.setScale(mapWidget.getMapModel().getMapView().getCurrentScale());
			
			
			GwtCommand commandRequest = new GwtCommand(SearchByLocationRequest.COMMAND);
			commandRequest.setCommandRequest(request);
			//TODO: commands are now chained. Perhaps we should combine this into a single command?
			GwtCommandDispatcher.getInstance().execute(commandRequest, new CommandCallback() {

				public void execute(CommandResponse commandResponse) {
					if (commandResponse instanceof SearchByLocationResponse) {
						final SearchByLocationResponse vectorResponse = (SearchByLocationResponse) commandResponse;
						if (includeRasterLayers) {
							
							GwtCommand commandRequest = new GwtCommand(SearchLayersByPointRequest.COMMAND);
							commandRequest.setCommandRequest(rasterLayerRequest);
							GwtCommandDispatcher.getInstance().execute(commandRequest, new CommandCallback() {
								public void execute(CommandResponse commandRasterResponse) {
									SearchLayersByPointResponse rasterResponse = 
										(SearchLayersByPointResponse) commandRasterResponse;
									Map<String, List<org.geomajas.layer.feature.Feature>> featureMap = 
										vectorResponse.getFeatureMap();
									featureMap.putAll(rasterResponse.getFeatureMap());
									showWindow(featureMap);
								}
							});
							
						} else {
							showWindow(vectorResponse.getFeatureMap());
						}
					}
				}
			});
			
		} else {
			dragging = false;
		}
		clickstart = false;
	}
	
	// -------------------------------------------------------------------------
	// Private methods:
	// -------------------------------------------------------------------------

	private void showWindow(Map<String, List<org.geomajas.layer.feature.Feature>> featureMap) {
		if (featureMap.size() > 0) {
			Window window = new MultiLayerFeatureInfoWindow(mapWidget, featureMap);
			window.setPageTop(mapWidget.getAbsoluteTop() + 10);
			window.setPageLeft(mapWidget.getAbsoluteLeft() + 50);
			window.draw();
		} else {
			Notify.info(messages.multiLayerFeatureInfoNoResult());
		}
	}
	
	/**
	 * @param bounds
	 * @return
	 */
	private org.geomajas.geometry.Bbox toBbox(Bbox bounds) {
		return new org.geomajas.geometry.Bbox(bounds.getX(), bounds.getY(), bounds.getWidth(), bounds.getHeight());
	}

	private String[] getServerLayerIds(MapModel mapModel) {
		List<String> layerIds = new ArrayList<String>();
		for (Layer<?> layer : mapModel.getLayers()) {
			if (layer.isShowing()) {
				layerIds.add(layer.getServerLayerId());
			}
		}
		return layerIds.toArray(new String[layerIds.size()]);
	}

	private double calculateBufferFromPixelTolerance() {
		WorldViewTransformer transformer = mapWidget.getMapModel().getMapView().getWorldViewTransformer();
		Coordinate c1 = transformer.viewToWorld(new Coordinate(0, 0));
		Coordinate c2 = transformer.viewToWorld(new Coordinate(pixelTolerance, 0));
		return Mathlib.distance(c1, c2);
	}

	/**
	 * @param includeRasterLayers whether to include raster layers in the result
	 */
	public void setIncludeRasterLayers(boolean includeRasterLayers) {
		this.includeRasterLayers = includeRasterLayers;
	}

	/**
	 * @return the whether to include raster layer features in the result
	 */
	public boolean isIncludeRasterLayers() {
		return includeRasterLayers;
	}
}
