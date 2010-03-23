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
import org.geomajas.gwt.client.command.Deferred;
import org.geomajas.gwt.client.command.GwtCommand;
import org.geomajas.gwt.client.command.GwtCommandDispatcher;
import org.geomajas.gwt.client.gfx.PaintableGroup;
import org.geomajas.gwt.client.gfx.PainterVisitor;
import org.geomajas.gwt.client.map.cache.SpatialCache;
import org.geomajas.gwt.client.map.feature.Feature;
import org.geomajas.gwt.client.map.feature.LazyLoadCallback;
import org.geomajas.gwt.client.spatial.Bbox;
import org.geomajas.layer.tile.TileCode;
import org.geomajas.layer.tile.VectorTile.VectorTileContentType;

import com.google.gwt.core.client.GWT;
import com.smartgwt.client.util.SC;

/**
 * Representation of a vector tile in the GWT client.
 * 
 * @author Pieter De Graef
 */
public class VectorTile extends AbstractVectorTile {

	/**
	 * Data holder: contains SVG or VML from the server for features.
	 */
	private ContentHolder featureContent;

	/**
	 * Data holder: contains SVG or VML from the server for labels.
	 */
	private ContentHolder labelContent;

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

	private Deferred deferred;

	// -------------------------------------------------------------------------
	// Constructors:
	// -------------------------------------------------------------------------

	public VectorTile(TileCode code, Bbox bbox, SpatialCache cache) {
		super(code, bbox, cache);
		featureContent = new ContentHolder(code.toString());
		labelContent = new ContentHolder(code.toString());
	}

	// -------------------------------------------------------------------------
	// Spatial node functions:
	// -------------------------------------------------------------------------

	/**
	 * Return all features in this tile. Warning : this will not return the features from other tiles that intersect
	 * with this tile ! If you want to interact with all features, use the query() method.
	 * 
	 * @param featureIncludes
	 *            what data should be available in the features
	 * @param callback
	 *            callback which gets the features
	 */
	public void getFeatures(int featureIncludes, LazyLoadCallback callback) {
		cache.getFeatures(featureIncludes, callback);
	}

	/**
	 * Return all partial features in this tile. Warning : this will return possibly incomplete features !
	 * 
	 *@return a list of all features in this tile
	 */
	public List<Feature> getPartialFeatures() {
		List<Feature> partials = new ArrayList<Feature>();
		for (String id : featureIds) {
			partials.add(getCache().getPartialFeature(id));
		}
		return partials;
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
		deferred = GwtCommandDispatcher.getInstance().execute(command, new CommandCallback() {

			public void execute(CommandResponse response) {
				if (!deferred.isCanceled() && response instanceof GetRenderedTileResponse) {
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
					featureContent.setContent(tile.getFeatureContent());
					labelContent.setContent(tile.getLabelContent());
					contentType = tile.getContentType();
					try {
						callback.execute(self);
					} catch (Throwable t) {
						GWT.log("VectorTile: error calling the callback after a fetch.", t);
					}
					rendered = true;
				}
			}
		});
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
		apply(filter, new TileFunction<VectorTile>() {

			public void execute(VectorTile tile) {
				List<TileCode> tileCodes = tile.getCodes();
				for (TileCode tileCode : tileCodes) {
					VectorTile temp = tile.cache.addTile(tileCode);
					if (temp.getStatus() == STATUS.EMPTY) {
						temp.fetch(filter, callback);
					} else {
						temp.apply(filter, callback);
					}
				}
			}
		});
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
		if (deferred == null) {
			return STATUS.EMPTY;
		}
		if (featureIds == null || featureIds.size() == 0) {
			return STATUS.LOADING;
		}
		return STATUS.LOADED;
	}

	/**
	 * Cancel the fetching of this tile. No callback will be executed anymore.
	 */
	public void cancel() {
		if (deferred != null) {
			deferred.cancel();
		}
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

	public double getScreenWidth() {
		return screenWidth;
	}

	public double getScreenHeight() {
		return screenHeight;
	}

	public ContentHolder getFeatureContent() {
		return featureContent;
	}

	public ContentHolder getLabelContent() {
		return labelContent;
	}

	public VectorTileContentType getContentType() {
		return contentType;
	}

	/**
	 * Holds string content + TODO: hash for the content ?
	 * 
	 * @author Jan De Moerloose
	 * 
	 */
	public class ContentHolder implements PaintableGroup {

		private String groupName;

		private String content;

		ContentHolder(String groupName) {
			this.groupName = groupName;
		}

		public String getGroupName() {
			return groupName;
		}

		public String getContent() {
			return content;
		}

		public void setContent(String content) {
			this.content = content;
		}

		public boolean isLoaded() {
			return content != null;
		}

		public void accept(PainterVisitor visitor, Object group, Bbox bounds, boolean recursive) {
		}
	}
	
	//-------------------------------------------------------------------------
	// Private methods:
	//-------------------------------------------------------------------------

	/**
	 * Execute a TileFunction on this tile. If the tile is not yet loaded, attach it to the isLoaded event.
	 * 
	 * @param callback
	 */
	private void apply(final String filter, final TileFunction<VectorTile> callback) {
		switch (getStatus()) {
			case EMPTY:
				fetch(filter, callback);
				break;
			case LOADING:
				final VectorTile self = this;
				deferred.addSuccesCallback(new CommandCallback() {

					public void execute(CommandResponse response) {
						if (!deferred.isCanceled() && response instanceof GetRenderedTileResponse) {
							callback.execute(self);
						}
					}
				});
				break;
			case LOADED:
				callback.execute(this);
		}
	}

	private GwtCommand createCommand(String filter) {
		GetRenderedTileRequest request = new GetRenderedTileRequest();
		request.setCode(code);
		request.setCrs(cache.getLayer().getMapModel().getCrs());
		request.setFilter(filter);
		request.setLayerId(cache.getLayer().getServerLayerId());
		// always paint geometries in first fetch
		request.setPaintGeometries(!rendered);
		request.setPaintLabels(cache.getLayer().isLabeled());
		request.setPanOrigin(cache.getLayer().getMapModel().getMapView().getPanOrigin());
		request.setRenderer(SC.isIE() ? "VML" : "SVG");
		request.setScale(cache.getLayer().getMapModel().getMapView().getCurrentScale());
		request.setStyleInfo(cache.getLayer().getLayerInfo().getNamedStyleInfo());
		request.setFeatureIncludes(GwtCommandDispatcher.getInstance().getLazyFeatureIncludesDefault());
		GwtCommand command = new GwtCommand("command.render.GetRenderedTile");
		command.setCommandRequest(request);
		return command;
	}

}