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

package org.geomajas.gwt.client.spatial.snapping;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.geomajas.command.dto.SearchByLocationRequest;
import org.geomajas.command.dto.SearchByLocationResponse;
import org.geomajas.configuration.SnappingRuleInfo;
import org.geomajas.configuration.SnappingRuleInfo.SnappingType;
import org.geomajas.geometry.Coordinate;
import org.geomajas.global.GeomajasConstant;
import org.geomajas.gwt.client.command.AbstractCommandCallback;
import org.geomajas.gwt.client.command.GwtCommand;
import org.geomajas.gwt.client.command.GwtCommandDispatcher;
import org.geomajas.gwt.client.map.MapModel;
import org.geomajas.gwt.client.map.feature.Feature;
import org.geomajas.gwt.client.map.layer.VectorLayer;
import org.geomajas.gwt.client.spatial.Bbox;
import org.geomajas.gwt.client.spatial.geometry.Polygon;
import org.geomajas.gwt.client.util.GeometryConverter;
import org.geomajas.layer.LayerType;

/**
 * <p>
 * This is the main handler for snapping to coordinates. It supports different
 * modes of operating and different algorithms for the actual snapping. The
 * different algorithms to use are defined in the vector layer configurations
 * objects, while the modes are defined by the {@link SnappingMode} class.
 * </p>
 * <p>
 * All you have to do to make use of snapping, is to make an instance of this
 * class, and call the {@link #snap(org.geomajas.geometry.Coordinate)} method.
 * </p>
 *
 * @author Pieter De Graef
 * @author Kristof Heirwegh
 */
public class Snapper {

	/**
	 * General definition for the different snapping modes. The
	 * ALL_GEOMETRIES_EQUAL will use a SnappingMode that treats all geometries
	 * equally see {@link EqualSnappingMode}, while the
	 * PRIORITY_TO_INTERSECTING_GEOMETRIES will give priority to geometries that
	 * intersect the given coordinate see {@link IntersectPriorityMode}.
	 */
	public static enum SnapMode {
		ALL_GEOMETRIES_EQUAL, PRIORITY_TO_INTERSECTING_GEOMETRIES
	}

	/**
	 * The MapModel that contains the layers to which snapping is possible. If a
	 * snapping rule should exist that points to a layer that does not exist
	 * within this MapModel, then no snapping will occur.
	 */
	private MapModel mapModel;

	/**
	 * Features are cached so they do not need to be retrieved from the server
	 * every time the mouse is moved.
	 */
	private Map<VectorLayer, List<Feature>> featureCache;

	/**
	 * The featureCache needs to be invalidated if the bounds change between
	 * requests.
	 */
	private Bbox currentBounds;

	/**
	 * The full list of snapping rules to be used.
	 */
	private List<SnappingRuleInfo> rules;

	private String[] serverLayerIds;

	private Map<String, String> layerFilters;

	/**
	 * The current snapping mode to use.
	 */
	private SnapMode mode;

	// -------------------------------------------------------------------------
	// Constructors:
	// -------------------------------------------------------------------------

	/**
	 * Create a snapper with a certain set of rules and the given MapModel. As
	 * mode, the SnapMode.ALL_GEOMETRIES_EQUAL is used by default.
	 */
	public Snapper(MapModel mapModel, List<SnappingRuleInfo> rules) {
		this(mapModel, rules, SnapMode.ALL_GEOMETRIES_EQUAL);
	}

	/**
	 * Create a snapper with a certain set of rules and the given MapModel,
	 * thereby immediately setting the snapping mode to use.
	 */
	public Snapper(MapModel mapModel, List<SnappingRuleInfo> rules, SnapMode mode) {
		this.featureCache = new HashMap<VectorLayer, List<Feature>>();
		this.mapModel = mapModel;
		this.rules = rules;
		setMode(mode);
		retrieveFeatures();
	}

	// -------------------------------------------------------------------------
	// Public methods:
	// -------------------------------------------------------------------------

	/**
	 * Execute the actual snapping!
	 *
	 * @param coordinate
	 *            The original coordinate that needs snapping.
	 * @return Returns the given coordinate, or a snapped coordinate if one was
	 *         found.
	 */
	public Coordinate snap(Coordinate coordinate) {
		if (rules == null || mapModel == null) {
			return coordinate;
		}
		Coordinate snappedCoordinate = coordinate;
		double snappedDistance = Double.MAX_VALUE;

		for (SnappingRuleInfo rule : rules) {
			// Check for supported snapping algorithms: TODO use factory
			if (rule.getType() != SnappingType.CLOSEST_ENDPOINT && rule.getType() != SnappingType.NEAREST_POINT) {
				throw new IllegalArgumentException("Unknown snapping rule type was found: " + rule.getType());
			}

			// Get the target snap layer:
			VectorLayer snapLayer;
			try {
				snapLayer = mapModel.getVectorLayer(rule.getLayerId());
			} catch (Exception e) { // NOSONAR
				throw new IllegalArgumentException("Target snapping layer (" + rule.getLayerId()
						+ ") was not a vector layer.");
			}

			SnapMode tempMode = this.mode;
			if (snapLayer.getLayerInfo().getLayerType() != LayerType.POLYGON
					&& snapLayer.getLayerInfo().getLayerType() != LayerType.MULTIPOLYGON) {
				// For mode=MODE_PRIORITY_TO_INTERSECTING_GEOMETRIES, an area > 0 is required.
				tempMode = SnapMode.ALL_GEOMETRIES_EQUAL;
			}

			// TODO: don't create the handler every time...
			SnappingMode handler;
			if (tempMode == SnapMode.ALL_GEOMETRIES_EQUAL) {
				handler = new EqualSnappingMode(rule);
			} else {
				handler = new IntersectPriorityMode(rule);
			}

			// Calculate snapping:
			handler.setCoordinate(coordinate);
			iterateFeatures(snapLayer, handler);

			if (handler.getDistance() < snappedDistance) {
				snappedCoordinate = handler.getSnappedCoordinate();
				snappedDistance = handler.getDistance();
			}
		}
		return snappedCoordinate;
	}

	// -------------------------------------------------------------------------
	// Getters and setters:
	// -------------------------------------------------------------------------

	public SnapMode getMode() {
		return mode;
	}

	public void setMode(SnapMode mode) {
		this.mode = mode;
	}

	public List<SnappingRuleInfo> getRules() {
		return rules;
	}

	public void setRules(List<SnappingRuleInfo> rules) {
		this.rules = rules;
	}

	// ----------------------------------------------------------
	// private methods
	// ----------------------------------------------------------

	private void iterateFeatures(VectorLayer layer, SnappingMode handler) {
		if (currentBounds != null && currentBounds.equals(mapModel.getMapView().getBounds(), 0)) {
			List<Feature> feats = featureCache.get(layer);
			if (feats != null) {
				handler.execute(feats);
			}
		} else {
			retrieveFeatures();
		}
	}

	private void retrieveFeatures() {
		// setting current bounds before method returns so it isn't called
		// multiple times while waiting for result
		// (this is adequate for javascript, no real concurrency)
		currentBounds = mapModel.getMapView().getBounds();
		if (serverLayerIds == null) {
			init();
		}

		Polygon polygon = mapModel.getGeometryFactory().createPolygon(currentBounds);
		GwtCommand commandRequest = new GwtCommand(SearchByLocationRequest.COMMAND);
		SearchByLocationRequest request = new SearchByLocationRequest();
		request.setLayerIds(serverLayerIds);
		addFilters(request);
		request.setFeatureIncludes(GeomajasConstant.FEATURE_INCLUDE_GEOMETRY);
		request.setLocation(GeometryConverter.toDto(polygon));
		request.setCrs(mapModel.getCrs());
		request.setQueryType(SearchByLocationRequest.QUERY_INTERSECTS);
		request.setSearchType(SearchByLocationRequest.SEARCH_ALL_LAYERS);
		commandRequest.setCommandRequest(request);
		GwtCommandDispatcher.getInstance().execute(commandRequest,
				new AbstractCommandCallback<SearchByLocationResponse>() {
			public void execute(SearchByLocationResponse response) {
				Map<String, List<org.geomajas.layer.feature.Feature>> featureMap = response.getFeatureMap();
				featureCache.clear();
				for (String serverLayerId : featureMap.keySet()) {
					VectorLayer vl = findLayer(serverLayerId);
					List<Feature> features = new ArrayList<Feature>();
					featureCache.put(vl, features);
					for (org.geomajas.layer.feature.Feature dtoFeat : featureMap.get(serverLayerId)) {
						features.add(new Feature(dtoFeat, vl));
					}
				}
			}
		});
	}

	private VectorLayer findLayer(String serverLayerId) {
		List<VectorLayer> res = mapModel.getVectorLayersByServerId(serverLayerId);
		if (res.size() == 1) {
			return res.get(0);
		} else {
			for (VectorLayer vl : res) {
				for (SnappingRuleInfo sri : rules) {
					if (sri.getLayerId().equals(vl.getId())) {
						return vl; // there's no way to know if it's the correct one
					}
				}
			}
			return null; // shouldn't happen
		}
	}

	private void addFilters(SearchByLocationRequest sblr) {
		// -- a simple String[], like ids would have been easier...
		for (Entry<String, String> entry : layerFilters.entrySet()) {
			sblr.setFilter(entry.getKey(), entry.getValue());
		}
	}

	private void init() {
		List<String> layerIds = new ArrayList<String>();
		layerFilters = new HashMap<String, String>();
		for (SnappingRuleInfo sri : rules) {
			VectorLayer vl = mapModel.getVectorLayer(sri.getLayerId());
			layerIds.add(vl.getServerLayerId());
			if (vl.getFilter() != null) {
				layerFilters.put(vl.getServerLayerId(), vl.getFilter());
			}
		}
		serverLayerIds = layerIds.toArray(new String[layerIds.size()]);
	}
}
