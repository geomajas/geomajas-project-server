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

package org.geomajas.widget.featureinfo.client.action.toolbar;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.geomajas.command.dto.SearchByLocationRequest;
import org.geomajas.command.dto.SearchByLocationResponse;
import org.geomajas.configuration.AttributeInfo;
import org.geomajas.configuration.client.ClientVectorLayerInfo;
import org.geomajas.geometry.Coordinate;
import org.geomajas.gwt.client.command.AbstractCommandCallback;
import org.geomajas.gwt.client.command.GwtCommand;
import org.geomajas.gwt.client.command.GwtCommandDispatcher;
import org.geomajas.gwt.client.controller.listener.AbstractListener;
import org.geomajas.gwt.client.controller.listener.ListenerEvent;
import org.geomajas.gwt.client.map.layer.Layer;
import org.geomajas.gwt.client.map.layer.VectorLayer;
import org.geomajas.gwt.client.spatial.Mathlib;
import org.geomajas.gwt.client.spatial.WorldViewTransformer;
import org.geomajas.gwt.client.spatial.geometry.Point;
import org.geomajas.gwt.client.util.GeometryConverter;
import org.geomajas.gwt.client.widget.MapWidget;
import org.geomajas.layer.feature.Attribute;
import org.geomajas.layer.feature.Feature;
import org.geomajas.widget.featureinfo.client.FeatureInfoMessages;
import org.geomajas.widget.featureinfo.client.util.FitSetting;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.impl.StringBuilderImpl;
import com.google.gwt.user.client.Timer;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.Img;

/**
 * 
 * @author Kristof Heirwegh
 * @author Oliver May
 * @author Wout Swartenbroekx
 */
public class TooltipOnMouseoverListener extends AbstractListener {

	private FeatureInfoMessages messages = GWT.create(FeatureInfoMessages.class);
	private Canvas tooltip;
	private int pixelTolerance = FitSetting.tooltipPixelTolerance;

	private int minPixelMove = FitSetting.tooltipMinimalPixelMove;
	private int showDelay = FitSetting.tooltipShowDelay;
	private int maxLabelCount = FitSetting.tooltipMaxLabelCount;
	private boolean showEmptyResults = FitSetting.tooltipShowEmptyResultMessage;

	private Coordinate lastPosition; // screen
	private Coordinate currentPosition; // screen
	private Coordinate worldPosition; // world !!
	private Timer timer;
	
	private MapWidget mapWidget;
	private List<String> layersToExclude = new ArrayList<String>();
	private boolean useFeatureDetail;

	private static final String CSS = "<style>.tblcLayerLabel {font-size: 0.9em; font-weight: bold;} "
			+ ".tblcFeatureLabelList {margin: 0; padding-left: 20px;} "
			+ ".tblcFeatureLabel {margin: 0; font-size: 0.9em; font-style: italic;} "
			+ ".tblcMore {padding-top: 5px; font-size: 0.9em; font-style: italic;}</style>";

	public TooltipOnMouseoverListener(MapWidget mapWidget) {
		this.mapWidget = mapWidget;
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

	public void onMouseMove(ListenerEvent event) {
		// do not use getRelative(mapwidget.getElement()) -- it reinitializes the map...
		currentPosition = event.getClientPosition();
		if (lastPosition == null) {
			lastPosition = currentPosition;
		} else {
			double distance = currentPosition.distance(lastPosition);

			if (distance > minPixelMove) {
				lastPosition = currentPosition;
				worldPosition = event.getWorldPosition();
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

	public void onMouseOut(ListenerEvent event) {
		// when label is repositioned in corner it can be put under mouse, which (falsely) generates a mouseOutEvent
		if (!overlapsTooltip((int) event.getClientPosition().getX(), (int) event.getClientPosition().getY())) {
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
			Map<String, List<Feature>> featureMap) {
		if (useFeatureDetail) {
			setDetailTooltipData(coordUsedForRetrieval, featureMap);
		} else {
			setDefaultTooltipData(coordUsedForRetrieval, featureMap);
		}
	}
	
	private void setDetailTooltipData(Coordinate coordUsedForRetrieval, Map<String, List<Feature>> featureMap) {
		if (coordUsedForRetrieval.equals(worldPosition) && tooltip != null) {
			StringBuilderImpl sb = new StringBuilderImpl.ImplStringAppend();
			sb.append(CSS);
			int widest = 10;
			int count = 0;

			for (Layer<?> layer : mapWidget.getMapModel().getLayers()) {
				if (featureMap.containsKey(layer.getId()) && useLayer(layer.getId())) {
					List<Feature> features = featureMap.get(layer.getId());
					if (features.size() > 0) {
						for (Feature feature : features) {
							if (count < maxLabelCount) {
								String featureLabel = layer.getLabel() + " " + feature.getId();
								widest = updateTooltipSize(widest, featureLabel);
								writeLayerStart(sb, featureLabel);
								for (Entry<String, Attribute> entry : feature.getAttributes().entrySet()) {
									if (isIdentifying(entry.getKey(), layer)) {
										String label = entry.getKey().toString() + ": " + entry.getValue().toString();
										writeFeature(sb, label);
										widest = updateTooltipSize(widest, label);
									}
								}
								writeLayerEnd(sb);
								count++;
							}
						}
					}
				}
			}

			int left = tooltip.getLeft();
			int top = tooltip.getTop();
			destroyTooltip();

			if (count > maxLabelCount) {
				writeTooMany(sb, count - maxLabelCount);
			} else if (count == 0 && showEmptyResults) {
				writeNone(sb);
			} else if (count == 0) {
				return;
			}

			Canvas content = new Canvas();
			content.setContents(sb.toString());
			int width = (int) (widest * 4.8) + 40;
			if (width < 150) {
				width = 150;
			}
			content.setWidth(width);
			content.setAutoHeight();
			content.setMargin(5);
			createTooltip(left, top, content);
		} // else - mouse moved between request and data retrieval
	}
	
	private boolean isIdentifying(String key, Layer layer) {
		if (layer instanceof VectorLayer) {
			ClientVectorLayerInfo c = (ClientVectorLayerInfo) layer.getLayerInfo();
			for (AttributeInfo a : c.getFeatureInfo().getAttributes()) {
				if (a.getName().equalsIgnoreCase(key)) {
					return a.isIdentifying();
				}
			}
		}
		return false;
	}

	private boolean useLayer(String id) {
		if (layersToExclude.contains(id)) {
			return false;
		}
		return true;
	}

	protected int updateTooltipSize(int widest, String label) {
		int size = getLabelSize(label);								
		if (widest < size) {
			widest = size;
		}
		return widest;
	}
	
	private void setDefaultTooltipData(Coordinate coordUsedForRetrieval, Map<String, List<Feature>> featureMap) {
		if (coordUsedForRetrieval.equals(worldPosition) && tooltip != null) {
			StringBuilderImpl sb = new StringBuilderImpl.ImplStringAppend();
			sb.append(CSS);
			int widest = 10;
			int count = 0;
			
			for (Layer<?> layer : mapWidget.getMapModel().getLayers()) {
				if (featureMap.containsKey(layer.getId()) && useLayer(layer.getId())) {
					List<Feature> features = featureMap.get(layer.getId());
					if (features.size() > 0) {
						if (count < maxLabelCount) {
							writeLayerStart(sb, layer.getLabel());
							if (widest < layer.getLabel().length()) {
								widest = layer.getLabel().length();
							}
							for (Feature feature : features) {
								if (count < maxLabelCount) {
									String label = getLabel(feature, (VectorLayer) layer);
									int size = getLabelSize(label);
									writeFeature(sb, label);
									if (widest < size) {
										widest = size;
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
			}

			int left = tooltip.getLeft();
			int top = tooltip.getTop();
			destroyTooltip();

			if (count > maxLabelCount) {
				writeTooMany(sb, count - maxLabelCount);
			} else if (count == 0 && showEmptyResults) {
				writeNone(sb);
			} else if (count == 0) {
				return;
			}

			Canvas content = new Canvas();
			content.setContents(sb.toString());
			int width = (int) (widest * 4.8) + 40;
			if (width < 150) {
				width = 150;
			}
			content.setWidth(width);
			content.setAutoHeight();
			content.setMargin(5);
			createTooltip(left, top, content);
		} // else - mouse moved between request and data retrieval
	}

	/**
	 * Return the text to add to the tooltip for the given feature.
	 * <p>Default implementation returns <code>feature.getLabel()</code>
	 * 
	 * @param feature feature
	 * @param layer layer
	 * @return label
	 */
	protected String getLabel(Feature feature, VectorLayer layer) {
		return feature.getLabel();
	}
	
	protected int getLabelSize(String label) {
		if (label == null) {
			return 0;
		} else if (label.length() < 20) {
			return label.length(); // don't bother
		} else {
			String[] subLab = label.split("<br ?/>");
			if (subLab.length > 1) {
				int max = 0;
				for (String sub : subLab) {
					if (sub.length() > max) {
						max = sub.length();
					}
				}
				return max;
			} else {
				return label.length();
			}
		}
	}
	
	private void getData() {
		Point point = mapWidget.getMapModel().getGeometryFactory().createPoint(worldPosition);
		final Coordinate coordUsedForRetrieval = worldPosition;

		SearchByLocationRequest request = new SearchByLocationRequest();
		request.setLocation(GeometryConverter.toDto(point));
		request.setCrs(mapWidget.getMapModel().getCrs());
		request.setQueryType(SearchByLocationRequest.QUERY_INTERSECTS);
		int layersToSearch = SearchByLocationRequest.SEARCH_ALL_LAYERS;
		request.setSearchType(layersToSearch);
		request.setBuffer(calculateBufferFromPixelTolerance());
		request.setFeatureIncludes(GwtCommandDispatcher.getInstance().getLazyFeatureIncludesSelect());
		
		for (Layer<?> layer : mapWidget.getMapModel().getLayers()) {
			if (layer.isShowing() && layer instanceof VectorLayer) {
				request.addLayerWithFilter(layer.getId(), layer.getServerLayerId(), ((VectorLayer) layer).getFilter());
		
			}
		}
		
		GwtCommand commandRequest = new GwtCommand(SearchByLocationRequest.COMMAND);
		commandRequest.setCommandRequest(request);
		GwtCommandDispatcher.getInstance().execute(commandRequest, 
					new AbstractCommandCallback<SearchByLocationResponse>() {
			public void execute(SearchByLocationResponse commandResponse) {
				setTooltipData(coordUsedForRetrieval, commandResponse.getFeatureMap());
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
	
	public int getPixelTolerance() {
		return pixelTolerance;
	}

	public void setPixelTolerance(int pixelTolerance) {
		this.pixelTolerance = pixelTolerance;
	}

	/**
	 * Set if empty results should be displayed as "no results found" or be omitted.
	 * @param show true if empty results should be shown.
	 */
	public void setShowEmptyResult(boolean show) {
		this.showEmptyResults = show;
	}

	/**
	 * Set the minimal distance the mouse must move before a new mouse over request is triggered.
	 * @param distance the minimal distance.
	 */
	public void setMinimalMoveDistance(int distance) {
		this.minPixelMove = distance;
	}
	
	/**
	 * Set if tooltip should be filled with feature details instead of label.
	 * @param detailedLayers true to use details of object, false to use default labels
	 */
	public void setTooltipUseFeatureDetail(boolean useFeatureDetail) {
		this.useFeatureDetail = useFeatureDetail;
	}

	/**
	 * Set the list of Layer IDs for layers which should not be used to draw the detail tooltip.
	 * @param layerIds list of Layer IDs
	 */
	public void setLayersToExclude(String[] layerIds) {
		this.layersToExclude.clear();
		for (String layerId : layerIds) {
			this.layersToExclude.add(layerId);
		}
	}

	/**
	 * Set the amount of features for which detailed information should be shown.
	 * @param maxLabelCount the number of features.
	 */
	public void setTooltipMaxLabelCount(int maxLabelCount) {
		this.maxLabelCount = maxLabelCount;
	}
	
	public int getMaxLabelCount() {
		return this.maxLabelCount;
	}
}