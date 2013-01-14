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

package org.geomajas.widget.featureinfo.client.action.toolbar;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.geomajas.command.dto.SearchByLocationRequest;
import org.geomajas.command.dto.SearchByLocationResponse;
import org.geomajas.geometry.Coordinate;
import org.geomajas.gwt.client.command.AbstractCommandCallback;
import org.geomajas.gwt.client.command.GwtCommand;
import org.geomajas.gwt.client.command.GwtCommandDispatcher;
import org.geomajas.gwt.client.controller.listener.AbstractListener;
import org.geomajas.gwt.client.controller.listener.ListenerEvent;
import org.geomajas.gwt.client.map.feature.Feature;
import org.geomajas.gwt.client.map.layer.Layer;
import org.geomajas.gwt.client.map.layer.RasterLayer;
import org.geomajas.gwt.client.map.layer.VectorLayer;
import org.geomajas.gwt.client.spatial.Bbox;
import org.geomajas.gwt.client.spatial.Mathlib;
import org.geomajas.gwt.client.spatial.WorldViewTransformer;
import org.geomajas.gwt.client.spatial.geometry.Point;
import org.geomajas.gwt.client.util.GeometryConverter;
import org.geomajas.gwt.client.util.Notify;
import org.geomajas.gwt.client.widget.MapWidget;
import org.geomajas.layer.wms.command.dto.SearchByPointRequest;
import org.geomajas.layer.wms.command.dto.SearchByPointResponse;
import org.geomajas.widget.featureinfo.client.FeatureInfoMessages;
import org.geomajas.widget.featureinfo.client.util.FitSetting;
import org.geomajas.widget.featureinfo.client.widget.MultiLayerFeatureInfoWindow;
import org.geomajas.widget.featureinfo.client.widget.factory.FeatureDetailWidgetFactory;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.NativeEvent;
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
 * @author Wout Swartenbroekx
 */
public class MultiLayerFeatureInfoListener extends AbstractListener {

	private static final FeatureInfoMessages MESSAGES = GWT.create(FeatureInfoMessages.class);

	private boolean dragging;
	private boolean clickstart;
	private final MapWidget mapWidget;

	private boolean includeRasterLayers = FitSetting.featureinfoIncludeRasterLayer;

	/**
	 * Number of pixels that describes the tolerance allowed when trying to select features.
	 */
	private int pixelTolerance = FitSetting.featureInfoPixelTolerance;
	
	private final List<String> layersToExclude = new ArrayList<String>();
	
	private Map<String, String> featuresListLabels;

	/**
	 * Constructor.
	 *
	 * @param mapWidget map widget
	 */
	public MultiLayerFeatureInfoListener(MapWidget mapWidget) {
		super();
		this.mapWidget = mapWidget;
	}

	/**
	 * Constructor.
	 *
	 * @param mapWidget map widget
	 * @param pixelTolerance pixel tolerance
	 */
	public MultiLayerFeatureInfoListener(MapWidget mapWidget, int pixelTolerance) {
		this(mapWidget);
		this.pixelTolerance = pixelTolerance;
	}

	public int getPixelTolerance() {
		return pixelTolerance;
	}

	public void setPixelTolerance(int pixelTolerance) {
		this.pixelTolerance = pixelTolerance;
	}

	public void setLayersToExclude(String[] layerIds) {
		this.layersToExclude.clear();
		Collections.addAll(this.layersToExclude, layerIds);
	}

	@Override
	public void onMouseDown(ListenerEvent event) {
		if (event.getNativeButton() != NativeEvent.BUTTON_RIGHT) {
			clickstart = true;
		}
	}

	@Override
	public void onMouseMove(ListenerEvent event) {
		if (clickstart) {
			dragging = true;
		}
	}

	/**
	 * On mouse up, execute the search by location, and display a
	 * {@link org.geomajas.widget.featureinfo.client.widget.MultiLayerFeaturesList} if features are found.
	 */
	public void onMouseUp(ListenerEvent event) {
		if (clickstart && !dragging) {
			Coordinate worldPosition = event.getWorldPosition();
			Point point = mapWidget.getMapModel().getGeometryFactory().createPoint(worldPosition);

			SearchByLocationRequest request = new SearchByLocationRequest();
			request.setLocation(GeometryConverter.toDto(point));
			request.setCrs(mapWidget.getMapModel().getCrs());
			request.setQueryType(SearchByLocationRequest.QUERY_INTERSECTS);
			request.setSearchType(SearchByLocationRequest.SEARCH_ALL_LAYERS);
			request.setBuffer(calculateBufferFromPixelTolerance());
			request.setFeatureIncludes(GwtCommandDispatcher.getInstance().getLazyFeatureIncludesSelect());
			
			for (Layer<?> layer : mapWidget.getMapModel().getLayers()) {
				if (layer.isShowing() && layer instanceof VectorLayer  && !layersToExclude.contains(layer.getId())) {
					request.addLayerWithFilter(layer.getId(), layer.getServerLayerId(),
							((VectorLayer) layer).getFilter());
				}
			}
			final SearchByPointRequest rasterLayerRequest = new SearchByPointRequest();
			rasterLayerRequest.setLocation(point.getCoordinate());
			rasterLayerRequest.setCrs(mapWidget.getMapModel().getCrs());
			rasterLayerRequest.setSearchType(SearchByPointRequest.SEARCH_ALL_LAYERS);
			rasterLayerRequest.setPixelTolerance(pixelTolerance);
			final Map<String, String> rasterLayerIds = new HashMap<String, String>();
			for (Layer<?> layer : mapWidget.getMapModel().getLayers()) {
				if (layer.isShowing() && layer instanceof RasterLayer && !layersToExclude.contains(layer.getId())) {
					rasterLayerIds.put(layer.getId(), layer.getServerLayerId());
				}
			}
			rasterLayerRequest.setLayerMapping(rasterLayerIds);
			rasterLayerRequest.setBbox(toBbox(mapWidget.getMapModel().getMapView().getBounds()));
			rasterLayerRequest.setScale(mapWidget.getMapModel().getMapView().getCurrentScale());

			GwtCommand commandRequest = new GwtCommand(SearchByLocationRequest.COMMAND);
			commandRequest.setCommandRequest(request);
			// TODO: commands are now chained. Perhaps we should combine this
			// into a single command?
			GwtCommandDispatcher.getInstance().execute(commandRequest, 
								new AbstractCommandCallback<SearchByLocationResponse>() {
				public void execute(final SearchByLocationResponse vectorResponse) {
					if (includeRasterLayers) {
						GwtCommand commandRequest = new GwtCommand(SearchByPointRequest.COMMAND);
						commandRequest.setCommandRequest(rasterLayerRequest);
						GwtCommandDispatcher.getInstance().execute(commandRequest,
								new AbstractCommandCallback<SearchByPointResponse>() {
									public void execute(final SearchByPointResponse rasterResponse) {
										//Featuremap maps client layer on feature
										Map<String, List<org.geomajas.layer.feature.Feature>> featureMap = 
											vectorResponse.getFeatureMap();
										featureMap.putAll(rasterResponse.getFeatureMap());
										showFeatureInfo(featureMap);
									}
								});

					} else {
						showFeatureInfo(vectorResponse.getFeatureMap());
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

	private void showFeatureInfo(Map<String, List<org.geomajas.layer.feature.Feature>> featureMap) {
		if (featureMap.size() > 0) {
			if (featureMap.size() == 1 && featureMap.values().iterator().next().size() == 1) {
				Layer<?> layer = (mapWidget.getMapModel().getLayer(featureMap.keySet().iterator().next()));
				if (null != layer) {
					org.geomajas.layer.feature.Feature featDTO = featureMap.values().iterator().next().get(0);
					Feature feature;
					if (layer instanceof VectorLayer) {
						feature = new Feature(featDTO, (VectorLayer) layer);
					} else {
						feature = new Feature(featDTO, null);
					}
					Window window = FeatureDetailWidgetFactory.createFeatureDetailWindow(feature, layer, false);
					window.setPageTop(mapWidget.getAbsoluteTop() + 25);
					window.setPageLeft(mapWidget.getAbsoluteLeft() + 25);
					window.setKeepInParentRect(true);
					window.draw();
				} else {
					Notify.error(MESSAGES.multiLayerFeatureInfoLayerNotFound());
				}
			} else {
				Window window = new MultiLayerFeatureInfoWindow(mapWidget, featureMap, featuresListLabels);
				window.setPageTop(mapWidget.getAbsoluteTop() + 10);
				window.setPageLeft(mapWidget.getAbsoluteLeft() + 50);
				window.draw();
			}
		} else {
			Notify.info(MESSAGES.multiLayerFeatureInfoNoResult());
		}
	}

	/**
	 * Convert to Bbox DTO.
	 *
	 * @param bounds bounds
	 * @return DTO
	 */
	private org.geomajas.geometry.Bbox toBbox(Bbox bounds) {
		return new org.geomajas.geometry.Bbox(bounds.getX(), bounds.getY(), bounds.getWidth(), bounds.getHeight());
	}

	private double calculateBufferFromPixelTolerance() {
		WorldViewTransformer transformer = mapWidget.getMapModel().getMapView().getWorldViewTransformer();
		Coordinate c1 = transformer.viewToWorld(new Coordinate(0, 0));
		Coordinate c2 = transformer.viewToWorld(new Coordinate(pixelTolerance, 0));
		return Mathlib.distance(c1, c2);
	}

	/**
	 * Set whether to include raster layers.
	 *
	 * @param includeRasterLayers
	 *            whether to include raster layers in the result
	 */
	public void setIncludeRasterLayers(boolean includeRasterLayers) {
		this.includeRasterLayers = includeRasterLayers;
	}

	/**
	 * Are raster layers included?
	 *
	 * @return the whether to include raster layer features in the result
	 */
	public boolean isIncludeRasterLayers() {
		return includeRasterLayers;
	}

	/**
	 * Set list labels.
	 *
	 * @param featuresListLabels the featuresListLabels to set
	 */
	public void setFeaturesListLabels(Map<String, String> featuresListLabels) {
		this.featuresListLabels = featuresListLabels;
	}
}