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

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.geomajas.command.CommandResponse;
import org.geomajas.command.dto.SearchByLocationRequest;
import org.geomajas.command.dto.SearchByLocationResponse;
import org.geomajas.geometry.Coordinate;
import org.geomajas.gwt.client.command.CommandCallback;
import org.geomajas.gwt.client.command.GwtCommand;
import org.geomajas.gwt.client.command.GwtCommandDispatcher;
import org.geomajas.gwt.client.controller.AbstractGraphicsController;
import org.geomajas.gwt.client.map.MapModel;
import org.geomajas.gwt.client.map.layer.Layer;
import org.geomajas.gwt.client.map.layer.VectorLayer;
import org.geomajas.gwt.client.spatial.Mathlib;
import org.geomajas.gwt.client.spatial.WorldViewTransformer;
import org.geomajas.gwt.client.spatial.geometry.Point;
import org.geomajas.gwt.client.util.GeometryConverter;
import org.geomajas.gwt.client.widget.MapWidget;
import org.geomajas.widget.featureinfo.client.FeatureInfoMessages;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.impl.StringBuilderImpl;
import com.google.gwt.event.dom.client.MouseMoveEvent;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.user.client.Timer;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.Img;

/**
 * 
 * @author Kristof Heirwegh
 * 
 */
public class TooltipOnMouseoverController extends AbstractGraphicsController {

	private FeatureInfoMessages messages = GWT.create(FeatureInfoMessages.class);
	private Canvas tooltip;
	private int pixelTolerance;

	private Coordinate lastPosition; // screen
	private Coordinate currentPosition; // screen
	private Coordinate worldPosition; // world !!
	private int minPixelMove = 5;
	private int showDelay = 250;
	private int layersToSearch = SearchByLocationRequest.SEARCH_ALL_LAYERS;
	private int maxLabelCount = 15;
	private Timer timer;

	private static final String CSS = "<style>.tblcLayerLabel {font-size: 0.9em; font-weight: bold;} "
			+ ".tblcFeatureLabelList {margin: 0; padding-left: 20px;} "
			+ ".tblcFeatureLabel {margin: 0; font-size: 0.9em; font-style: italic;} "
			+ ".tblcMore {padding-top: 5px; font-size: 0.9em; font-style: italic;}</style>";

	public TooltipOnMouseoverController(MapWidget mapWidget, int pixelTolerance) {
		super(mapWidget);
		this.pixelTolerance = pixelTolerance;
	}

	/**
	 * Initialization.
	 */
	public void onActivate() {
		onDeactivate();
	}

	/** Clean everything up. */
	public void onDeactivate() {
		destroyTooltip();
		if (timer != null) {
			timer.cancel();
		}
	}

	@Override
	public void onMouseMove(MouseMoveEvent event) {
		// do not use getRelative(mapwidget.getElement()) -- it reinitializes the map...
		currentPosition = new Coordinate(event.getClientX(), event.getClientY());
		
		if (lastPosition == null) {
			lastPosition = currentPosition;
		} else {
			double distance = currentPosition.distance(lastPosition);

			if (distance > minPixelMove) {
				lastPosition = currentPosition;
				worldPosition = getWorldPosition(event);
				if (isShowing()) {
					destroyTooltip();
				}
				// place new tooltip after some time
				if (timer == null) {
					timer = new Timer() {
						public void run() {
							showTooltip();
						}
					};
					timer.schedule(showDelay);

				} else {
					timer.cancel();
					timer.schedule(showDelay);
				}
			}
		}
	}

	@Override
	public void onMouseOut(MouseOutEvent event) {
		// when label is repositioned in corner it can be put under mouse, which (falsely) generates a mouseOutEvent
		if (!overlapsTooltip(event.getClientX(), event.getClientY())) {
			onDeactivate();
		}
	}

	// ----------------------------------------------------------

	private boolean isShowing() {
		return (tooltip != null);
	}

	private void showTooltip() {
		if (!isShowing()) {
			createTooltip((int) currentPosition.getX() + 12, (int) currentPosition.getY() + 12, null);
			getData();
		}
	}

	private void writeLayerStart(StringBuilderImpl sb, String label) {
		sb.append("<span class='tblcLayerLabel'>");
		sb.append(label);
		sb.append("</span><ul class='tblcFeatureLabelList'>");
	}

	private void writeFeature(StringBuilderImpl sb, String label) {
		sb.append("<li class='tblcFeatureLabel'>");
		sb.append(label);
		sb.append("</li>");
	}

	private void writeLayerEnd(StringBuilderImpl sb) {
		sb.append("</ul>");
	}

	private void writeNone(StringBuilderImpl sb) {
		sb.append("<span class='tblcMore'>(");
		sb.append(messages.tooltipOnMouseoverNoResult());
		sb.append(")</span>");
	}

	private void writeTooMany(StringBuilderImpl sb, int tooMany) {
		sb.append("<span class='tblcMore'>");
		sb.append("" + tooMany);
		sb.append("</span>");
	}

	private void setTooltipData(Coordinate coordUsedForRetrieval,
			Map<String, List<org.geomajas.layer.feature.Feature>> featureMap) {
		if (coordUsedForRetrieval.equals(worldPosition) && tooltip != null) {
			StringBuilderImpl sb = new StringBuilderImpl.ImplStringAppend();
			sb.append(CSS);
			int widest = 10;
			int count = 0;
			
			for (VectorLayer layer : mapWidget.getMapModel().getVectorLayers()) {
				if (featureMap.containsKey(layer.getId())) {
					List<org.geomajas.layer.feature.Feature> features = featureMap.get(layer.getId());
					if (features.size() > 0) {
						if (count < maxLabelCount) {
							writeLayerStart(sb, layer.getLabel());
							if (widest < layer.getLabel().length()) {
								widest = layer.getLabel().length();
							}
							for (org.geomajas.layer.feature.Feature feature : features) {
								if (count < maxLabelCount) {
									writeFeature(sb, feature.getLabel());
									if (widest < feature.getLabel().length()) {
										widest = feature.getLabel().length();
									}
								}
								count++;
							}
							writeLayerEnd(sb);
						} else {
							count += features.size();
						}
					}
				}
			} /* for */
				
			if (count > maxLabelCount) {
				writeTooMany(sb, count - maxLabelCount);
			} else if (count == 0) {
				writeNone(sb);
			}
			
			int left = tooltip.getLeft();
			int top = tooltip.getTop();
			destroyTooltip();
			Canvas content = new Canvas();
			content.setContents(sb.toString());
			content.setWidth(widest * 6 + 10);
			content.setAutoHeight();
			content.setMargin(5);
			createTooltip(left, top, content);
		} // else - mouse moved between request and data retrieval
	}

	private void getData() {
		Point point = mapWidget.getMapModel().getGeometryFactory().createPoint(worldPosition);
		final Coordinate coordUsedForRetrieval = worldPosition;

		SearchByLocationRequest request = new SearchByLocationRequest();
		request.setLocation(GeometryConverter.toDto(point));
		request.setCrs(mapWidget.getMapModel().getCrs());
		request.setQueryType(SearchByLocationRequest.QUERY_INTERSECTS);
		request.setSearchType(layersToSearch);
		request.setBuffer(calculateBufferFromPixelTolerance());
		request.setFeatureIncludes(GwtCommandDispatcher.getInstance().getLazyFeatureIncludesSelect());
		
		for (Layer<?> layer : mapWidget.getMapModel().getLayers()) {
			if (layer.isShowing() && layer instanceof VectorLayer) {
				request.addLayerWithFilter(layer.getId(), layer.getServerLayerId(),
						((VectorLayer) layer).getFilter());
			}
		}

		GwtCommand commandRequest = new GwtCommand(SearchByLocationRequest.COMMAND);
		commandRequest.setCommandRequest(request);
		GwtCommandDispatcher.getInstance().execute(commandRequest, new CommandCallback() {
			public void execute(CommandResponse commandResponse) {
				if (commandResponse instanceof SearchByLocationResponse) {
					SearchByLocationResponse response = (SearchByLocationResponse) commandResponse;
					setTooltipData(coordUsedForRetrieval, response.getFeatureMap());
				}
			}
		});
	}

	private void createTooltip(int x, int y, Canvas content) {
		if (mapWidget != null) {
			tooltip = new Canvas();
			tooltip.setBackgroundColor("white");
			tooltip.setShowShadow(true);
			tooltip.setOpacity(85);
			tooltip.setBorder("thin solid #AAAAAA");
			if (content != null) {
				tooltip.addChild(content);
			} else {
				tooltip.addChild(getLoadingImg());
			}
			tooltip.setAutoWidth();
			tooltip.setAutoHeight();
			tooltip.hide();
			tooltip.draw(); // need this to get correct size of tooltip
			placeTooltip(x, y);
			tooltip.show();
		}
	}
	
	private void placeTooltip(int x, int y) {
		if (tooltip != null) {
			int realx = x;
			int realy = y;
			
			int tooltipWidth = tooltip.getRight() - tooltip.getLeft();
			int tooltipHeight = tooltip.getBottom() - tooltip.getTop();
			
			int overlapX = (x + (tooltipWidth)) - mapWidget.getRight();
			int overlapY = (y + (tooltipHeight)) - mapWidget.getBottom();
			if (overlapX > 0) {
				realx -= overlapX;
			}
			if (overlapY > 0) {
				realy -= overlapY;
			}
			tooltip.moveTo(realx, realy);
		}
	}

	private void destroyTooltip() {
		if (tooltip != null) {
			tooltip.destroy();
			tooltip = null;
		}
	}

	private boolean overlapsTooltip(int x, int y) {
		if (tooltip == null) {
			 return false;
		} else {
			return (!(x < tooltip.getLeft() || x > tooltip.getRight() || y < tooltip.getTop() || y > tooltip
					.getBottom()));
		}
	}
	
	private String[] getServerLayerIds(MapModel mapModel) {
		Set<String> layerIds = new HashSet<String>();
		for (VectorLayer layer : mapModel.getVectorLayers()) {
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
	
	private Canvas getLoadingImg() {
		Canvas c = new Canvas();
		c.setMargin(4);
		c.setWidth(26);
		c.setHeight(26);
		c.addChild(new Img("[ISOMORPHIC]/geomajas/loading_small.gif", 16, 16));
		return c;
	}
}
