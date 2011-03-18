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
package org.geomajas.gwt.client.action.menu;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.geomajas.command.CommandResponse;
import org.geomajas.command.dto.SearchByLocationRequest;
import org.geomajas.command.dto.SearchByLocationResponse;
import org.geomajas.geometry.Coordinate;
import org.geomajas.gwt.client.action.MenuAction;
import org.geomajas.gwt.client.command.CommandCallback;
import org.geomajas.gwt.client.command.GwtCommand;
import org.geomajas.gwt.client.command.GwtCommandDispatcher;
import org.geomajas.gwt.client.i18n.I18nProvider;
import org.geomajas.gwt.client.map.MapModel;
import org.geomajas.gwt.client.map.layer.Layer;
import org.geomajas.gwt.client.map.layer.VectorLayer;
import org.geomajas.gwt.client.spatial.Mathlib;
import org.geomajas.gwt.client.spatial.WorldViewTransformer;
import org.geomajas.gwt.client.spatial.geometry.Point;
import org.geomajas.gwt.client.util.GeometryConverter;
import org.geomajas.gwt.client.widget.MapWidget;
import org.geomajas.layer.feature.Feature;

import com.smartgwt.client.widgets.menu.events.MenuItemClickEvent;

/**
 * <p>
 * Toggle selection of item under cursor. Has to option to prioritize the selected layer. In order to accomplish this,
 * there is the <code>priorityToSelectedLayer</code> option.
 * </p>
 * <p>
 * This option gives priority to the selected layer. This works only if there is a selected layer, and that selected
 * layer is a {@link VectorLayer}. If all these checks are okay, but that particular layer is not visible, then nothing
 * will happen. When one of the previous checks is not okay, the selection toggle will occur on the first object that is
 * encountered. In other words it will depend on the layer drawing order, starting at the top.
 * </p>
 * 
 * @author Joachim Van der Auwera
 * @author Pieter De Graef
 */
public class ToggleSelectionAction extends MenuAction {

	private MapWidget mapWidget;

	/**
	 * Give priority to the selected layer. This works only if there is a selected layer, and that selected layer is a
	 * {@link VectorLayer}. If all these checks are okay, but that particular layer is not visible, then nothing will
	 * happen. When one of the previous checks is not okay, the selection toggle will occur on the first object that is
	 * encountered. In other words it will depend on the layer drawing order, starting at the top.
	 */
	private boolean priorityToSelectedLayer;

	/** Number of pixels that describes the tolerance allowed when trying to select features. */
	private int pixelTolerance;

	// -------------------------------------------------------------------------
	// Constructors:
	// -------------------------------------------------------------------------

	/**
	 * Construct an instance, with the <code>MapWidget</code> on whom selection takes place.
	 */
	public ToggleSelectionAction(MapWidget mapWidget, int pixelTolerance) {
		this(mapWidget, false, pixelTolerance);
	}

	/**
	 * Construct an instance, with the <code>MapWidget</code> on whom selection takes place.
	 * 
	 * @param mapWidget
	 *            The <code>MapWidget</code> on whom selection takes place.
	 * @param priorityToSelectedLayer
	 *            Activate or disable priority to the selected layer. This works only if there is a selected layer, and
	 *            that selected layer is a {@link VectorLayer}. If all these checks are okay, but that particular layer
	 *            is not visible, then nothing will happen. When one of the previous checks is not okay, the selection
	 *            toggle will occur on the first object that is encountered. In other words it will depend on the layer
	 *            drawing order, starting at the top.
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
	 * Simply calls the toggle method, using the lastly clicked right mouse button position. Also automatically clears
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
			request.setLayerIds(new String[] { layer.getServerLayerId() });
			request.setFilter(((VectorLayer) layer).getFilter());
		} else {
			request.setLayerIds(getVisibleServerLayerIds(mapModel));
		}
		Point point = mapModel.getGeometryFactory().createPoint(worldPosition);
		request.setLocation(GeometryConverter.toDto(point));
		request.setCrs(mapWidget.getMapModel().getCrs());
		request.setQueryType(SearchByLocationRequest.QUERY_INTERSECTS);
		request.setSearchType(SearchByLocationRequest.SEARCH_ALL_LAYERS);
		request.setBuffer(calculateBufferFromPixelTolerance());
		request.setFeatureIncludes(GwtCommandDispatcher.getInstance().getLazyFeatureIncludesSelect());
		commandRequest.setCommandRequest(request);
		GwtCommandDispatcher.getInstance().execute(commandRequest, new CommandCallback() {
			public void execute(CommandResponse commandResponse) {
				if (commandResponse instanceof SearchByLocationResponse) {
					SearchByLocationResponse response = (SearchByLocationResponse) commandResponse;
					Map<String, List<Feature>> featureMap = response.getFeatureMap();
					for (String layerId : featureMap.keySet()) {
						selectFeatures(layerId, featureMap.get(layerId), singleSelectionId);
					}
				}
			}
		});
	}

	// -------------------------------------------------------------------------
	// Getters and setters:
	// -------------------------------------------------------------------------

	/**
	 * Will priority be given to the selected layer? This works only if there is a selected layer, and that selected
	 * layer is a {@link VectorLayer}. If all these checks are okay, but that particular layer is not visible, then
	 * nothing will happen. When one of the previous checks is not okay, the selection toggle will occur on the first
	 * object that is encountered. In other words it will depend on the layer drawing order, starting at the top.
	 */
	public boolean isPriorityToSelectedLayer() {
		return priorityToSelectedLayer;
	}

	/**
	 * Activate or disable priority to the selected layer. This works only if there is a selected layer, and that
	 * selected layer is a {@link VectorLayer}. If all these checks are okay, but that particular layer is not visible,
	 * then nothing will happen. When one of the previous checks is not okay, the selection toggle will occur on the
	 * first object that is encountered. In other words it will depend on the layer drawing order, starting at the top.
	 */
	public void setPriorityToSelectedLayer(boolean priorityToSelectedLayer) {
		this.priorityToSelectedLayer = priorityToSelectedLayer;
	}

	/** Number of pixels that describes the tolerance allowed when trying to select features. */
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

	private void selectFeatures(String serverLayerId, List<org.geomajas.layer.feature.Feature> orgFeatures,
			String selectionId) {
		List<VectorLayer> layers = mapWidget.getMapModel().getVectorLayersByServerId(serverLayerId);
		for (VectorLayer vectorLayer : layers) {
			for (org.geomajas.layer.feature.Feature orgFeature : orgFeatures) {
				org.geomajas.gwt.client.map.feature.Feature feature = new org.geomajas.gwt.client.map.feature.Feature(
						orgFeature, vectorLayer);
				vectorLayer.getFeatureStore().addFeature(feature);
				if (vectorLayer.isFeatureSelected(feature.getId()) || feature.getId().equals(selectionId)) {
					vectorLayer.deselectFeature(feature);
				} else {
					vectorLayer.selectFeature(feature);
				}
			}
		}
	}

	private String[] getVisibleServerLayerIds(MapModel mapModel) {
		List<String> layerIds = new ArrayList<String>();
		for (VectorLayer layer : mapModel.getVectorLayers()) {
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
}
