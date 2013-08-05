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
package org.geomajas.smartgwt.client.action.menu;

import java.util.List;
import java.util.Map;

import org.geomajas.command.dto.SearchByLocationRequest;
import org.geomajas.command.dto.SearchByLocationResponse;
import org.geomajas.geometry.Coordinate;
import org.geomajas.smartgwt.client.action.MenuAction;
import org.geomajas.gwt.client.command.AbstractCommandCallback;
import org.geomajas.gwt.client.command.GwtCommand;
import org.geomajas.gwt.client.command.GwtCommandDispatcher;
import org.geomajas.smartgwt.client.i18n.I18nProvider;
import org.geomajas.smartgwt.client.map.MapModel;
import org.geomajas.smartgwt.client.map.layer.Layer;
import org.geomajas.smartgwt.client.map.layer.VectorLayer;
import org.geomajas.smartgwt.client.spatial.Mathlib;
import org.geomajas.smartgwt.client.spatial.WorldViewTransformer;
import org.geomajas.smartgwt.client.spatial.geometry.Point;
import org.geomajas.smartgwt.client.util.GeometryConverter;
import org.geomajas.smartgwt.client.widget.MapWidget;
import org.geomajas.layer.feature.Feature;

import com.smartgwt.client.widgets.menu.events.MenuItemClickEvent;

/**
 * <p>
 * Toggle selection of item under cursor. Has to option to prioritize the selected layer. In order to accomplish this,
 * there is the {@link #priorityToSelectedLayer} option.
 * </p>
 * <p>
 * This option gives priority to the selected layer. This works only if there is a selected layer, and that selected
 * layer is a {@link org.geomajas.smartgwt.client.map.layer.VectorLayer}. If all these checks are okay,
 * but that particular layer is not visible, then nothing will happen. When one of the previous checks is not okay,
 * the selection toggle will occur on the first object that is encountered. In other words it will depend on the
 * layer drawing order, starting at the top.
 * </p>
 * 
 * @author Joachim Van der Auwera
 * @author Pieter De Graef
 */
public class ToggleSelectionAction extends MenuAction {

	private MapWidget mapWidget;

	/**
	 * Give priority to the selected layer. This works only if there is a selected layer, and that selected layer is a
	 * {@link org.geomajas.smartgwt.client.map.layer.VectorLayer}. If all these checks are okay,
	 * but that particular layer is not visible, then nothing will happen. When one of the previous checks is not
	 * okay, the selection toggle will occur on the first object that is encountered. In other words it will depend
	 * on the layer drawing order, starting at the top.
	 */
	private boolean priorityToSelectedLayer;

	/** Number of pixels that describes the tolerance allowed when trying to select features. */
	private int pixelTolerance;

	// -------------------------------------------------------------------------
	// Constructors:
	// -------------------------------------------------------------------------

	/**
	 * Construct an instance, with the {@link MapWidget} on whom selection takes place.
	 *
	 * @param mapWidget map widget
	 * @param pixelTolerance pixel tolerance
	 */
	public ToggleSelectionAction(MapWidget mapWidget, int pixelTolerance) {
		this(mapWidget, false, pixelTolerance);
	}

	/**
	 * Construct an instance, with the {@link MapWidget} on whom selection takes place.
	 * 
	 * @param mapWidget
	 *            The {@link MapWidget} on whom selection takes place.
	 * @param priorityToSelectedLayer
	 *            Activate or disable priority to the selected layer. This works only if there is a selected layer, and
	 *            that selected layer is a {@link org.geomajas.smartgwt.client.map.layer.VectorLayer}. If all these
	 *            checks are okay, but that particular layer is not visible, then nothing will happen. When one of
	 *            the previous checks is not okay, the selection toggle will occur on the first object that is
	 *            encountered. In other words it will depend on the layer drawing order, starting at the top.
	 * @param pixelTolerance
	 *            Number of pixels that describes the tolerance allowed when trying to select features.
	 */
	public ToggleSelectionAction(MapWidget mapWidget, boolean priorityToSelectedLayer, int pixelTolerance) {
		super(I18nProvider.getMenu().toggleSelection(), null);
		this.mapWidget = mapWidget;
		this.priorityToSelectedLayer = priorityToSelectedLayer;
		this.pixelTolerance = pixelTolerance;
	}

	// -------------------------------------------------------------------------
	// Public methods
	// -------------------------------------------------------------------------

	/**
	 * Calls the toggle method, using the lastly clicked right mouse button position. Also automatically clears
	 * selection first.
	 */
	public void onClick(MenuItemClickEvent menuItemClickEvent) {
		toggle(mapWidget.getMenuContext().getRightButtonCoordinate(), true);
	}

	/**
	 * Toggle the selection on a certain coordinate. This methods will try and search at the given coordinate in all
	 * visible layers until it encounters a feature. This features selection state is then toggled.
	 * 
	 * @param coordinate
	 *            The view space coordinate where to search for features.
	 * @param clearSelection
	 *            Should the current selection be cleared as well?
	 */
	public void toggle(Coordinate coordinate, final boolean clearSelection) {
		toggle(coordinate, clearSelection, false);
	}

	/**
	 * Toggle selection for the given location in view space, while making sure no more then a single feature is ever
	 * selected.
	 * 
	 * @param coordinate
	 *            The view space coordinate where to search for features.
	 */
	public void toggleSingle(Coordinate coordinate) {
		toggle(coordinate, true, true);
	}

	// -------------------------------------------------------------------------
	// Getters and setters:
	// -------------------------------------------------------------------------

	/**
	 * Will priority be given to the selected layer? This works only if there is a selected layer, and that selected
	 * layer is a {@link org.geomajas.smartgwt.client.map.layer.VectorLayer}. If all these checks are okay,
	 * but that particular layer is not visible, then nothing will happen. When one of the previous checks is not
	 * okay, the selection toggle will occur on the first object that is encountered. In other words it will depend
	 * on the layer drawing order, starting at the top.
	 *
	 * @return true when priority is given to the selected layer
	 */
	public boolean isPriorityToSelectedLayer() {
		return priorityToSelectedLayer;
	}

	/**
	 * Activate or disable priority to the selected layer. This works only if there is a selected layer, and that
	 * selected layer is a {@link org.geomajas.smartgwt.client.map.layer.VectorLayer}. If all these checks are okay,
	 * but that particular layer is not visible, then nothing will happen. When one of the previous checks is not
	 * okay, the selection toggle will occur on the first object that is encountered. In other words it will depend
	 * on the layer drawing order, starting at the top.
	 *
	 * @param priorityToSelectedLayer should priority be given to the selected layer
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
	// Private methods:
	// -------------------------------------------------------------------------

	private void toggle(Coordinate coordinate, final boolean clearSelection, final boolean singleSelection) {
		if (null == coordinate) {
			return;
		}
		// we can clear here (but remember the selected feature for the special case of single selection) !
		final String singleSelectionId = mapWidget.getMapModel().getSelectedFeature();
		if (clearSelection) {
			mapWidget.getMapModel().clearSelectedFeatures();
		}
		MapModel mapModel = mapWidget.getMapModel();
		Coordinate worldPosition = mapModel.getMapView().getWorldViewTransformer().viewToWorld(coordinate);
		GwtCommand commandRequest = new GwtCommand(SearchByLocationRequest.COMMAND);
		SearchByLocationRequest request = new SearchByLocationRequest();
		Layer<?> layer = mapModel.getSelectedLayer();
		if (priorityToSelectedLayer && layer != null && layer instanceof VectorLayer) {
			if (!layer.isShowing()) {
				return;
			}
			request.addLayerWithFilter(layer.getId(), layer.getServerLayerId(), ((VectorLayer) layer).getFilter());
		} else {
			addVisibleLayers(request, mapModel);
		}
		Point point = mapModel.getGeometryFactory().createPoint(worldPosition);
		request.setLocation(GeometryConverter.toDto(point));
		request.setCrs(mapWidget.getMapModel().getCrs());
		request.setQueryType(SearchByLocationRequest.QUERY_INTERSECTS);
		request.setSearchType(SearchByLocationRequest.SEARCH_ALL_LAYERS);
		request.setBuffer(calculateBufferFromPixelTolerance());
		request.setFeatureIncludes(GwtCommandDispatcher.getInstance().getLazyFeatureIncludesSelect());
		commandRequest.setCommandRequest(request);
		GwtCommandDispatcher.getInstance().execute(commandRequest,
				new AbstractCommandCallback<SearchByLocationResponse>() {

			public void execute(SearchByLocationResponse response) {
				Map<String, List<Feature>> featureMap = response.getFeatureMap();
				for (String layerId : featureMap.keySet()) {
					selectFeatures(layerId, featureMap.get(layerId), singleSelectionId, singleSelection);
					if (singleSelection) {
						break;
					}
				}
			}
		});
	}

	private void selectFeatures(String clientLayerId, List<org.geomajas.layer.feature.Feature> orgFeatures,
			String selectionId, boolean singleSelection) {
		VectorLayer layer = mapWidget.getMapModel().getVectorLayer(clientLayerId);
		Layer<?> selectedLayer = mapWidget.getMapModel().getSelectedLayer();
		if (isSelectionTargetLayer(layer, selectedLayer)) {
			for (org.geomajas.layer.feature.Feature orgFeature : orgFeatures) {
				org.geomajas.smartgwt.client.map.feature.Feature feature =
						new org.geomajas.smartgwt.client.map.feature.Feature(orgFeature, layer);
				layer.getFeatureStore().addFeature(feature);
				if (layer.isFeatureSelected(feature.getId()) || feature.getId().equals(selectionId)) {
					layer.deselectFeature(feature);
				} else {
					layer.selectFeature(feature);
					if (singleSelection) {
						break;
					}

				}
			}
		}
	}

	private boolean isSelectionTargetLayer(VectorLayer targetLayer, Layer<?> selectedLayer) {
		if (!targetLayer.isShowing()) {
			return false;
		} else {
			if (priorityToSelectedLayer && selectedLayer != null) {
				return (selectedLayer.equals(targetLayer));
			}
			return true;
		}
	}

	private void addVisibleLayers(SearchByLocationRequest request, MapModel mapModel) {
		for (VectorLayer layer : mapModel.getVectorLayers()) {
			if (layer.isShowing()) {
				request.addLayerWithFilter(layer.getId(), layer.getServerLayerId(), layer.getFilter());
			}
		}
	}

	private double calculateBufferFromPixelTolerance() {
		WorldViewTransformer transformer = mapWidget.getMapModel().getMapView().getWorldViewTransformer();
		Coordinate c1 = transformer.viewToWorld(new Coordinate(0, 0));
		Coordinate c2 = transformer.viewToWorld(new Coordinate(pixelTolerance, 0));
		return Mathlib.distance(c1, c2);
	}
}