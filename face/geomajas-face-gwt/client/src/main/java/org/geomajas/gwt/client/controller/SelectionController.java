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

package org.geomajas.gwt.client.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.geomajas.command.dto.SearchByLocationRequest;
import org.geomajas.command.dto.SearchByLocationResponse;
import org.geomajas.gwt.client.action.menu.DeselectAllAction;
import org.geomajas.gwt.client.action.menu.ToggleSelectionAction;
import org.geomajas.gwt.client.command.AbstractCommandCallback;
import org.geomajas.gwt.client.command.GwtCommand;
import org.geomajas.gwt.client.command.GwtCommandDispatcher;
import org.geomajas.gwt.client.map.MapModel;
import org.geomajas.gwt.client.map.feature.Feature;
import org.geomajas.gwt.client.map.layer.Layer;
import org.geomajas.gwt.client.map.layer.VectorLayer;
import org.geomajas.gwt.client.spatial.Bbox;
import org.geomajas.gwt.client.spatial.geometry.Polygon;
import org.geomajas.gwt.client.util.GeometryConverter;
import org.geomajas.gwt.client.widget.MapWidget;
import org.geomajas.gwt.client.widget.MapWidget.RenderGroup;
import org.geomajas.gwt.client.widget.MapWidget.RenderStatus;

import com.google.gwt.event.dom.client.MouseUpEvent;
import com.smartgwt.client.widgets.menu.Menu;

/**
 * Allow selection of features, either by clicking on individual features, or by dragging a rectangle and selecting all
 * features which (for a certain percentage) fall into the indicated area.
 * 
 * @author Joachim Van der Auwera
 * @author Pieter De Graef
 */
public class SelectionController extends AbstractRectangleController {

	private boolean shiftOrCtrl;

	private Menu menu;

	private int clickTimeout; // timeout in milliseconds for handling as click versus dragging

	private float coverageRatio; // coverage percentage which is used to determine a feature as selected

	private boolean priorityToSelectedLayer; // give priority to the selected layer on when toggling

	private int pixelTolerance;

	// -------------------------------------------------------------------------
	// Constructors:
	// -------------------------------------------------------------------------

	/**
	 * Create a new selection controller, that controls selection on the given map through user interaction.
	 * 
	 * @param mapWidget
	 *            The map onto whom you want to control selection.
	 * @param clickTimeout
	 *            Timeout in milliseconds for handling as click versus dragging.
	 * @param coverageRatio
	 *            Coverage percentage which is used to determine a feature as selected. This is only used when dragging
	 *            a rectangle to select in. Must be a floating value between 0 and 1.
	 * @param priorityToSelectedLayer
	 *            Activate or disable priority to the selected layer. This works only if there is a selected layer, and
	 *            that selected layer is a {@link VectorLayer}. In all other cases, the selection toggle will occur on
	 *            the first object that is encountered. In other words it will depend on the layer drawing order,
	 *            starting at the top.
	 * @param pixelTolerance
	 *            Number of pixels that describes the tolerance allowed when trying to select features.
	 */
	public SelectionController(MapWidget mapWidget, int clickTimeout, float coverageRatio,
			boolean priorityToSelectedLayer, int pixelTolerance) {
		super(mapWidget);

		this.clickTimeout = clickTimeout;
		this.coverageRatio = coverageRatio;
		this.priorityToSelectedLayer = priorityToSelectedLayer;
		this.pixelTolerance = pixelTolerance;
	}

	// -------------------------------------------------------------------------
	// GraphicsController implementation:
	// -------------------------------------------------------------------------

	@Override
	public void onActivate() {
		menu = new Menu();
		menu.addItem(new ToggleSelectionAction(mapWidget, pixelTolerance));
		menu.addItem(new DeselectAllAction(mapWidget.getMapModel()));
		mapWidget.setContextMenu(menu);
	}

	@Override
	public void onDeactivate() {
		super.onDeactivate();
		onDoubleClick(null);
		menu.destroy();
		menu = null;
		mapWidget.setContextMenu(null);
		if (dragging) {
			dragging = false;
			mapWidget.render(rectangle, RenderGroup.SCREEN, RenderStatus.DELETE);
		}
	}

	/**
	 * First mouse button: publish a toggle event on the selection topic for the referenced MapWidget. Second mouse
	 * button opens a menu.
	 * 
	 * @param event
	 *            event
	 */
	@Override
	public void onMouseUp(MouseUpEvent event) {
		// handle click if any?
		if (dragging) {
			// shift or CTRL is used when depressed either at beginning or end:
			shiftOrCtrl = (event.isShiftKeyDown() || event.isControlKeyDown());

			if (timestamp + clickTimeout > new Date().getTime()
					&& (bounds.getWidth() < pixelTolerance || bounds.getHeight() < pixelTolerance)) {
				stopDragging();
				// click behavior instead of drag
				ToggleSelectionAction action = new ToggleSelectionAction(mapWidget, priorityToSelectedLayer,
						pixelTolerance);
				action.toggle(getScreenPosition(event), !shiftOrCtrl);
				return;
			}
		}
		// normal "rectangle" handling
		super.onMouseUp(event);
	}

	// -------------------------------------------------------------------------
	// Getters and setters:
	// -------------------------------------------------------------------------

	/**
	 * Coverage percentage which is used to determine a feature as selected. This is only used when dragging a rectangle
	 * to select in. Must be a floating value between 0 and 1.
	 *
	 * @return coverage ratio
	 */
	public float getCoverageRatio() {
		return coverageRatio;
	}

	/**
	 * Set a new coverage percentage which is used to determine a feature as selected. This is only used when dragging a
	 * rectangle to select in. Must be a floating value between 0 and 1.
	 *
	 * @param coverageRatio coverage ratio
	 */
	public void setCoverageRatio(float coverageRatio) {
		this.coverageRatio = coverageRatio;
	}

	/**
	 * Will priority be given to the selected layer? This works only if there is a selected layer, and that selected
	 * layer is a {@link VectorLayer}. In all other cases, the selection toggle will occur on the first object that is
	 * encountered. In other words it will depend on the layer drawing order, starting at the top.
	 *
	 * @return true when selected layer has priority
	 */
	public boolean isPriorityToSelectedLayer() {
		return priorityToSelectedLayer;
	}

	/**
	 * Activate or disable priority to the selected layer. This works only if there is a selected layer, and that
	 * selected layer is a {@link VectorLayer}. In all other cases, the selection toggle will occur on the first object
	 * that is encountered. In other words it will depend on the layer drawing order, starting at the top.
	 *
	 * @param priorityToSelectedLayer should selected layer have priority
	 */
	public void setPriorityToSelectedLayer(boolean priorityToSelectedLayer) {
		this.priorityToSelectedLayer = priorityToSelectedLayer;
	}

	/**
	 * Number of pixels that describes the tolerance allowed when trying to select features.
	 *
	 * @return pixel tolerance
	 */
	public int getPixelTolerance() {
		return pixelTolerance;
	}

	/**
	 * Number of pixels that describes the tolerance allowed when trying to select features.
	 * 
	 * @param pixelTolerance
	 *            The new value.
	 */
	public void setPixelTolerance(int pixelTolerance) {
		this.pixelTolerance = pixelTolerance;
	}

	// -------------------------------------------------------------------------
	// Private and protected methods:
	// -------------------------------------------------------------------------

	@Override
	protected void selectRectangle(Bbox selectedArea) {
		// we can clear here !
		if (!shiftOrCtrl) {
			MapModel mapModel = mapWidget.getMapModel();
			mapModel.clearSelectedFeatures();
		}
		GwtCommand commandRequest = new GwtCommand(SearchByLocationRequest.COMMAND);
		SearchByLocationRequest request = new SearchByLocationRequest();
		request.setLayerIds(getSelectionLayerIds());
		for (Layer<?> layer : mapWidget.getMapModel().getLayers()) {
			if (layer.isShowing() && layer instanceof VectorLayer) {
				request.setFilter(layer.getServerLayerId(), ((VectorLayer) layer).getFilter());
			}
		}

		Polygon polygon = mapWidget.getMapModel().getGeometryFactory().createPolygon(selectedArea);
		request.setLocation(GeometryConverter.toDto(polygon));
		request.setCrs(mapWidget.getMapModel().getCrs());
		request.setQueryType(SearchByLocationRequest.QUERY_INTERSECTS);
		request.setRatio(coverageRatio);
		request.setSearchType(SearchByLocationRequest.SEARCH_ALL_LAYERS);
		request.setFeatureIncludes(GwtCommandDispatcher.getInstance().getLazyFeatureIncludesSelect());
		commandRequest.setCommandRequest(request);
		GwtCommandDispatcher.getInstance().execute(commandRequest,
				new AbstractCommandCallback<SearchByLocationResponse>() {

			public void execute(SearchByLocationResponse response) {
				Map<String, List<org.geomajas.layer.feature.Feature>> featureMap = response.getFeatureMap();
				for (String layerId : featureMap.keySet()) {
					selectFeatures(layerId, featureMap.get(layerId));
				}
			}
		});
	}

	private String[] getSelectionLayerIds() {
		Layer<?> layer = mapWidget.getMapModel().getSelectedLayer();
		List<String> ids = new ArrayList<String>();
		if (layer != null && layer instanceof VectorLayer) {
			ids.add(layer.getServerLayerId());
		} else {
			for (VectorLayer v : mapWidget.getMapModel().getVectorLayers()) {
				if (v.isShowing()) {
					ids.add(v.getServerLayerId());
				}
			}
		}
		return ids.toArray(new String[ids.size()]);
	}

	private void selectFeatures(String serverLayerId, List<org.geomajas.layer.feature.Feature> orgFeatures) {
		List<VectorLayer> layers = mapWidget.getMapModel().getVectorLayersByServerId(serverLayerId);
		Layer<?> selectedLayer = mapWidget.getMapModel().getSelectedLayer();
		for (VectorLayer vectorLayer : layers) {
			if (isSelectionTargetLayer(vectorLayer, selectedLayer)) {
				for (org.geomajas.layer.feature.Feature orgFeature : orgFeatures) {
					Feature feature = new Feature(orgFeature, vectorLayer);
					vectorLayer.getFeatureStore().addFeature(feature);
					vectorLayer.selectFeature(feature);
				}
			}
		}
	}

	private boolean isSelectionTargetLayer(VectorLayer targetLayer, Layer<?> selectedLayer) {
		if (!targetLayer.isShowing()) {
			return false;
		} else {
			return !(priorityToSelectedLayer && null != selectedLayer) || (selectedLayer.equals(targetLayer));
		}
	}
}
