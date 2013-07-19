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

package org.geomajas.puregwt.widget.client.mouseover.listener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.geomajas.command.dto.SearchByLocationRequest;
import org.geomajas.command.dto.SearchByLocationResponse;
import org.geomajas.geometry.Coordinate;
import org.geomajas.geometry.Geometry;
import org.geomajas.geometry.service.MathService;
import org.geomajas.gwt.client.command.AbstractCommandCallback;
import org.geomajas.gwt.client.command.GwtCommand;
import org.geomajas.gwt.client.command.GwtCommandDispatcher;
import org.geomajas.gwt.client.controller.AbstractController;
import org.geomajas.gwt.client.map.RenderSpace;
import org.geomajas.layer.feature.Attribute;
import org.geomajas.layer.feature.Feature;
import org.geomajas.puregwt.client.controller.MapController;
import org.geomajas.puregwt.client.map.MapPresenter;
import org.geomajas.puregwt.client.map.layer.FeaturesSupported;
import org.geomajas.puregwt.client.map.layer.ServerLayer;
import org.geomajas.puregwt.widget.client.mouseover.component.ToolTipBox;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseMoveEvent;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.user.client.Timer;

/**
 * Mouse over listener. When this listener is added to the map the users sees a dialog box with feature info when the
 * mouse is over a feature. By default just layer title and feature label are shown in the dialog box.
 * 
 * @author Dosi Bingov
 * 
 */
public class MouseOverListener extends AbstractController implements MapController {

	private Coordinate currentPos;

	private Coordinate lastPosition;

	protected MapPresenter mapPresenter;

	private ToolTipBox tooltip;

	private static final int MIN_PIXEL_DISTANCE = 10;

	private int pixelBuffer;

	private static final int TIMER_DELAY = 500; // 0.5s

	private Timer timer;

	private int xPos;

	private int yPos;

	private boolean showAllAtributes;

	// -------------------------------------------------------------------------
	// Constructors:
	// -------------------------------------------------------------------------

	public MouseOverListener() {
		this(32);
	}

	/**
	 * 
	 * @param showAllAtributes
	 *            if true all attributes of a feature will be shown in the dialog pop up
	 */
	public MouseOverListener(boolean showAllAtributes) {
		this(32);
		this.showAllAtributes = showAllAtributes;
	}

	/**
	 *
	 * @param pixelBuffer minimum distance between features to be included in the call out box.
	 */
	public MouseOverListener(int pixelBuffer) {
		super(false);
		this.pixelBuffer = pixelBuffer;
	}

	// -------------------------------------------------------------------------
	// MapController implementation:
	// -------------------------------------------------------------------------

	public boolean isShowAllAtributes() {
		return showAllAtributes;
	}

	public void setShowAllAtributes(boolean showAllAtributes) {
		this.showAllAtributes = showAllAtributes;
	}

	@Override
	public void onActivate(MapPresenter presenter) {
		mapPresenter = presenter;
		eventParser = mapPresenter.getMapEventParser();
	}

	@Override
	public void onMouseDown(MouseDownEvent event) {
		destroyToolTip();
	}

	@Override
	public void onDeactivate(MapPresenter presenter) {
		destroyToolTip();
	}

	@Override
	public void onMouseOut(MouseOutEvent event) {
		GWT.log("MouseOverListener => mouse out fired");
		destroyToolTip();
	}

	@Override
	public void onMouseMove(MouseMoveEvent event) {
		GWT.log("MouseOverListener => mouse move fired");
		GWT.log("Client X =" + event.getClientX() + " Client Y =" + event.getClientY());
		xPos = event.getClientX();
		yPos = event.getClientY();

		currentPos = getLocation(event, RenderSpace.WORLD);

		if (lastPosition == null) {
			lastPosition = currentPos;
		} else {
			double distance = currentPos.distance(lastPosition);

			if (distance > MIN_PIXEL_DISTANCE) {
				lastPosition = currentPos;

				if (tooltip != null) {
					destroyToolTip();
				}

				// place new tooltip after some time
				if (timer == null) {
					timer = new Timer() {

						public void run() {
							createToolTip();
						}
					};
					timer.schedule(TIMER_DELAY);

				} else {
					timer.cancel();
					timer.schedule(TIMER_DELAY);
				}
			}
		}
	}

	private void createToolTip() {
		if (tooltip == null) {
			tooltip = new ToolTipBox();
		}

		getData();
	}

	public void destroyToolTip() {
		if (tooltip != null) {
			tooltip.hide();
			tooltip.clearContent();
			tooltip = null;
		}

		if (timer != null) {
			timer.cancel();
		}
	}

	private void getData() {
		String crs = mapPresenter.getViewPort().getCrs();

		final Geometry geom = new Geometry();
		geom.setCoordinates(new Coordinate[] { currentPos });
		geom.setGeometryType(Geometry.POINT);

		SearchByLocationRequest request = new SearchByLocationRequest();
		request.setLocation(geom);
		request.setCrs(crs);
		request.setQueryType(SearchByLocationRequest.QUERY_INTERSECTS);
		int layersToSearch = SearchByLocationRequest.SEARCH_ALL_LAYERS;
		request.setSearchType(layersToSearch);
		request.setBuffer(calculateBufferFromPixelTolerance());

		request.setFeatureIncludes(GwtCommandDispatcher.getInstance().getLazyFeatureIncludesSelect());

		int index = mapPresenter.getLayersModel().getLayerCount();
		List<String> serverLayerIds = new ArrayList<String>();

		for (int i = 0; i < index; i++) {
			org.geomajas.puregwt.client.map.layer.Layer layer = mapPresenter.getLayersModel().getLayer(i);

			if (layer.isShowing() && layer instanceof FeaturesSupported && layer instanceof ServerLayer) {
				// filter request.addLayerWithFilter(layer.getId(), layer., ((VectorLayer) layer).getFilter());
				ServerLayer<?> serverLayer = (ServerLayer<?>) layer;
				serverLayerIds.add(serverLayer.getServerLayerId());

			}
		}

		request.setLayerIds(serverLayerIds.toArray(new String[] {}));

		GwtCommand commandRequest = new GwtCommand(SearchByLocationRequest.COMMAND);
		commandRequest.setCommandRequest(request);
		GwtCommandDispatcher.getInstance().execute(commandRequest,
				new AbstractCommandCallback<SearchByLocationResponse>() {

					public void execute(SearchByLocationResponse commandResponse) {
						setTooltipData(commandResponse.getFeatureMap());
					}
				});
	}

	private void setTooltipData(Map<String, List<Feature>> featureMap) {

		int index = mapPresenter.getLayersModel().getLayerCount();

		for (int i = 0; i < index; i++) {
			org.geomajas.puregwt.client.map.layer.Layer layer = mapPresenter.getLayersModel().getLayer(i);

			if (layer instanceof ServerLayer) {
				ServerLayer<?> serverLayer = (ServerLayer<?>) layer;
				String key = serverLayer.getServerLayerId();

				// look just in the layer that we are interested
				if (featureMap.containsKey(key)) {

					List<Feature> features = featureMap.get(key);
					if (features.size() > 0) {

						if (tooltip != null) {
							tooltip.setContentTitle(layer.getTitle());

							List<String> tooltipContent = new ArrayList();

							for (Feature f : features) {
								GWT.log("Feature label =" + f.getLabel());
								GWT.log("Feature id =" + f.getId());

								// add all attributes of the feature
								if (showAllAtributes) {
									for (Entry<String, Attribute> entry : f.getAttributes().entrySet()) {
										GWT.log("feature entry = " + entry.getKey() + "/" + entry.getValue());
										tooltipContent.add(entry.getKey() + ": " + entry.getValue().toString());
									}
								} else {
									tooltipContent.add(f.getLabel());
								}
							}

							// set actual content and show the tooltip
							tooltip.addContentAndShow(tooltipContent, xPos, yPos, true);
						}
					}
				}
			}
		}
	}

	private double calculateBufferFromPixelTolerance() {
		Coordinate c1 = mapPresenter.getViewPort().transform(new Coordinate(0, 0), RenderSpace.SCREEN,
				RenderSpace.WORLD);
		Coordinate c2 = mapPresenter.getViewPort().transform(new Coordinate(pixelBuffer, 0), RenderSpace.SCREEN,
				RenderSpace.WORLD);
		return MathService.distance(c1, c2);
	}
}
