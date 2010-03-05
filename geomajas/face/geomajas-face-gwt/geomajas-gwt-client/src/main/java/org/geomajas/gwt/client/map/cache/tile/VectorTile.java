/*
 * This file is part of Geomajas, a component framework for building
 * rich Internet applications (RIA) with sophisticated capabilities for the
 * display, analysis and management of geographic information.
 * It is a building block that allows developers to add maps
 * and other geographic data capabilities to their web applications.
 *
 * Copyright 2008-2010 Geosparc, http://www.geosparc.com, Belgium
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.geomajas.gwt.client.map.cache.tile;

import java.util.ArrayList;
import java.util.List;

import org.geomajas.command.CommandResponse;
import org.geomajas.command.dto.GetRenderedTileRequest;
import org.geomajas.command.dto.GetRenderedTileResponse;
import org.geomajas.gwt.client.command.CommandCallback;
import org.geomajas.gwt.client.command.GwtCommand;
import org.geomajas.gwt.client.command.GwtCommandDispatcher;
import org.geomajas.gwt.client.map.cache.SpatialCache;
import org.geomajas.gwt.client.map.feature.Feature;
import org.geomajas.gwt.client.spatial.Bbox;
import org.geomajas.layer.tile.TileCode;
import org.geomajas.layer.tile.VectorTile.VectorTileContentType;

import com.smartgwt.client.util.SC;

/**
 * TODO: not conform with latest Dojo implementation.
 * 
 * @author Pieter De Graef
 */
public class VectorTile extends AbstractVectorTile {

	/**
	 * Data holder: contains SVG or VML from the server for features.
	 */
	private String featureContent;

	/**
	 * Data holder: contains SVG or VML from the server for labels.
	 */
	private String labelContent;
	
	private VectorTileContentType contentType;

	/** Array of feature ID's. */
	private List<String> featureIds = new ArrayList<String>();

	/** width in screen units */
	private double screenWidth;

	/** height in screen units */
	private double screenHeight;

	/** dependent tile codes */
	private List<TileCode> codes = new ArrayList<TileCode>();

	/**
	 * Is the tile clipped : clipping is necessary to avoid too big coordinates
	 */
	private boolean clipped;

	private boolean rendered;

	private boolean canceled;

	// -------------------------------------------------------------------------
	// Constructors:
	// -------------------------------------------------------------------------

	public VectorTile(TileCode code, Bbox bbox, SpatialCache cache) {
		super(code, bbox, cache);
		this.featureContent = null;

	}

	// -------------------------------------------------------------------------
	// Spatial node functions:
	// -------------------------------------------------------------------------

	/**
	 * Return all features in this tile. Warning : this will not return the features from other tiles that intersect
	 * with this tile ! If you want to interact with all features, use the query() method.
	 */
	public List<Feature> getFeatures() {
		List<Feature> features = new ArrayList<Feature>();
		for (String featureId : featureIds) {
			features.add(cache.getFeature(featureId));
		}
		return features;
	}

	/**
	 * Fetch all data related to this tile.
	 * 
	 * @param filter
	 *            When fetching it is possible to filter the data with this filter object. Null otherwise.
	 * @param callback
	 *            When this node's data comes from the server, it will be handled by this callback function.
	 */
	public void fetch(String filter, final TileFunction<VectorTile> callback) {
		GwtCommand command = createCommand(filter);
		final VectorTile self = this;
		GwtCommandDispatcher.getInstance().execute(command, new CommandCallback() {

			public void execute(CommandResponse response) {
				if (!canceled && response instanceof GetRenderedTileResponse) {
					GetRenderedTileResponse tileResponse = (GetRenderedTileResponse) response;
					if (tileResponse.getTile().getFeatures() != null) {
						for (org.geomajas.layer.feature.Feature dto : tileResponse.getTile().getFeatures()) {
							cache.addFeature(new Feature(dto, cache.getLayer()));
							featureIds.add(dto.getId());
						}
					}
					org.geomajas.layer.tile.VectorTile tile = tileResponse.getTile();
					code = tile.getCode();
					// TODO: is it normal to round from double to int in the tile width and height??
					screenWidth = tile.getScreenWidth();
					screenHeight = tile.getScreenHeight();
					featureContent = tile.getFeatureContent();
					labelContent = tile.getLabelContent();
					contentType = tile.getContentType();
					try {
						callback.execute(self);
					} catch (Throwable t) {
						// log some error....
					}
					rendered = true;
				}
			}
		});
	}

	private GwtCommand createCommand(String filter) {
		GetRenderedTileRequest request = new GetRenderedTileRequest();
		request.setCode(code);
		request.setCrs(cache.getLayer().getMapModel().getCrs());
		request.setFilter(filter);
		request.setLayerId(cache.getLayer().getId());
		// always paint geometries in first fetch
		request.setPaintGeometries(!rendered);
		request.setPaintLabels(cache.getLayer().isLabeled());
		request.setPanOrigin(cache.getLayer().getMapModel().getMapView().getPanOrigin());
		request.setRenderer(SC.isIE() ? "VML" : "SVG");
		request.setScale(cache.getLayer().getMapModel().getMapView().getCurrentScale());
		request.setStyleInfo(cache.getLayer().getLayerInfo().getNamedStyleInfo());
		//request.setFeatureIncludes(); // @todo lazy loading
		GwtCommand command = new GwtCommand("command.render.GetRenderedTile");
		command.setCommandRequest(request);
		return command;
	}

	/**
	 * Execute a <code>TileFunction</code> in this tile and all connected tiles. If these connected tiles are not yet
	 * part of the cache, then they will be fetched before applying the <code>TileFunction</code> on them.
	 * 
	 * @param filter
	 *            A possible filter that needs to be used in case connected tile need to be fetched.
	 * @param callback
	 *            The actual <code>TileFunction</code> to be executed on the connected tiles.
	 */
	public void applyConnected(final String filter, final TileFunction<VectorTile> callback) {
		apply(new TileFunction<VectorTile>() {

			public void execute(VectorTile tile) {
				List<TileCode> tileCodes = tile.getCodes();
				for (TileCode tileCode : tileCodes) {
					VectorTile temp = tile.cache.addTile(tileCode);
					if (temp.getStatus() == STATUS.EMPTY) {
						temp.fetch(filter, callback);
					} else {
						apply(callback);
					}
				}
			}
		});
	}

	/**
	 * Execute a TileFunction on this tile. If the tile is not yet loaded, attach it to the isLoaded event.
	 * 
	 * @param callback
	 */
	public void apply(TileFunction<VectorTile> callback) {
		if (getStatus() == STATUS.LOADED) {
			callback.execute(this);
		} else {
			// TODO: add the callback to the deffered (find alternative first)
		}
	}

	/**
	 * Return the current status of this VectorTile. Can be one of the following:
	 * <ul>
	 * <li>STATUS.EMPTY</li>
	 * <li>STATUS.LOADING</li>
	 * <li>STATUS.LOADED</li>
	 * </ul>
	 */
	public STATUS getStatus() {
		// if (deferred == false) { // TODO: find an alternative for the Dojo deferred.
		// return STATUS.EMPTY;
		// }
		if (featureIds == null || featureIds.size() == 0) {
			return STATUS.LOADING;
		}
		return STATUS.LOADED;
	}

	// -------------------------------------------------------------------------
	// Some getters and setters:
	// -------------------------------------------------------------------------

	public List<TileCode> getCodes() {
		return codes;
	}

	public boolean isClipped() {
		return clipped;
	}

	public String getFeatureContent() {
		return featureContent;
	}

	public String getLabelContent() {
		return labelContent;
	}

	public void cancel() {
		canceled = true;
	}

	public double getScreenWidth() {
		return screenWidth;
	}

	public double getScreenHeight() {
		return screenHeight;
	}

	public boolean isComplete() {
		if (cache.getLayer().isLabeled() && labelContent == null) {
			return false;
		}
		return true;
	}

	public VectorTileContentType getContentType() {
		return contentType;
	}
}