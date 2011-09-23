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

package org.geomajas.gwt.client.map.cache.tile;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.geomajas.command.CommandResponse;
import org.geomajas.command.dto.GetVectorTileRequest;
import org.geomajas.command.dto.GetVectorTileResponse;
import org.geomajas.gwt.client.command.CommandCallback;
import org.geomajas.gwt.client.command.Deferred;
import org.geomajas.gwt.client.command.GwtCommand;
import org.geomajas.gwt.client.command.GwtCommandDispatcher;
import org.geomajas.gwt.client.gfx.PaintableGroup;
import org.geomajas.gwt.client.gfx.PainterVisitor;
import org.geomajas.gwt.client.map.cache.SpatialCache;
import org.geomajas.gwt.client.map.feature.Feature;
import org.geomajas.gwt.client.map.feature.LazyLoadCallback;
import org.geomajas.gwt.client.map.feature.LazyLoader;
import org.geomajas.gwt.client.spatial.Bbox;
import org.geomajas.gwt.client.util.Log;
import org.geomajas.layer.tile.TileCode;
import org.geomajas.layer.tile.VectorTile.VectorTileContentType;

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

	/** width in screen units */
	private double screenWidth;

	/** height in screen units */
	private double screenHeight;

	/** dependent tile codes */
	private List<TileCode> codes = new ArrayList<TileCode>();

	private Set<GetVectorTileRequest> requestCache = new HashSet<GetVectorTileRequest>();

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
	 * @deprecated features are no longer included in the tile
	 */
	@Deprecated
	public void getFeatures(int featureIncludes, LazyLoadCallback callback) {
		List<Feature> list = new ArrayList<Feature>();
		LazyLoader.lazyLoad(list, featureIncludes, callback);
	}

	/**
	 * Return all partial features in this tile. Warning : this will return possibly incomplete features !
	 * 
	 * @return a list of all features in this tile
	 * @deprecated features are no longer included in the tile
	 */
	@Deprecated
	public List<Feature> getPartialFeatures() {
		return new ArrayList<Feature>();
	}

	/**
	 * Fetch all data related to this tile.
	 * 
	 * @param filter
	 *            When fetching it is possible to filter the data with this filter object. Null otherwise.
	 * @param callback
	 *            When this node's data comes from the server, it will be handled by this callback function.
	 */
	public void fetch(final String filter, final TileFunction<VectorTile> callback) {
		final GetVectorTileRequest request = createRequest(filter);
		GwtCommand command = new GwtCommand(GetVectorTileRequest.COMMAND);
		command.setCommandRequest(request);
		final VectorTile self = this;
		deferred = GwtCommandDispatcher.getInstance().execute(command, new CommandCallback<GetVectorTileResponse>() {

			public void execute(GetVectorTileResponse response) {
				if (!(deferred != null && deferred.isCancelled())) {
					GetVectorTileResponse tileResponse = (GetVectorTileResponse) response;
					org.geomajas.layer.tile.VectorTile tile = tileResponse.getTile();
					for (TileCode relatedTile : tile.getCodes()) {
						codes.add(relatedTile);
					}
					code = tile.getCode();
					screenWidth = tile.getScreenWidth();
					screenHeight = tile.getScreenHeight();
					contentType = tile.getContentType();
					switch (contentType) {
						case STRING_CONTENT:
							featureContent.setContent(tile.getFeatureContent());
							labelContent.setContent(tile.getLabelContent());
							break;
						case URL_CONTENT:
							if (request.isPaintLabels()) {
								if (tile.getLabelContent() == null) {
									// feature content may also contain labels !
									labelContent.setContent(tile.getFeatureContent());
								} else {
									labelContent.setContent(tile.getLabelContent());
								}
							} else {
								featureContent.setContent(tile.getFeatureContent());
							}
							break;

					}
					requestCache.add(request);
					try {
						callback.execute(self);
					} catch (Throwable t) {
						Log.logError("VectorTile: error calling the callback after a fetch.", t);
					}
				}
				deferred = null;
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
	 * Execute a TileFunction on this tile. If the tile is not yet loaded, attach it to the isLoaded event.
	 * 
	 * @param filter
	 *            filter which needs to be applied when fetching
	 * @param callback
	 *            callback to call
	 */
	public void apply(final String filter, final TileFunction<VectorTile> callback) {
		switch (getStatus()) {
			case EMPTY:
				fetch(filter, callback);
				break;
			case LOADING:
				final VectorTile self = this;
				deferred.addCallback(new CommandCallback() {

					public void execute(CommandResponse response) {
						if (response instanceof GetVectorTileResponse) {
							callback.execute(self);
						}
					}
				});
				break;
			case LOADED:
				if (needsReload(filter)) {
					// Check if the labels need to be fetched as well:
					fetch(filter, callback);
				} else {
					callback.execute(this);
				}
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
		if (featureContent.isLoaded()) {
			return STATUS.LOADED;
		}
		if (deferred == null) {
			return STATUS.EMPTY;
		}
		return STATUS.LOADING;
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
	 * Holds string content.
	 * 
	 * @author Jan De Moerloose
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
	
	private boolean needsReload(String filter) {
		GetVectorTileRequest request = createRequest(filter);
		return !requestCache.contains(request);
	}

	// -------------------------------------------------------------------------
	// Private methods:
	// -------------------------------------------------------------------------

	private GetVectorTileRequest createRequest(String filter) {
		GetVectorTileRequest request = new GetVectorTileRequest();
		request.setCode(code);
		request.setCrs(cache.getLayer().getMapModel().getCrs());
		request.setFilter(filter);
		request.setLayerId(cache.getLayer().getServerLayerId());
		// always paint geometries, except when we already have the svg/vml
		request.setPaintGeometries(!(VectorTileContentType.STRING_CONTENT == contentType && featureContent.isLoaded()));
		request.setPaintLabels(cache.getLayer().isLabeled());
		request.setPanOrigin(cache.getLayer().getMapModel().getMapView().getPanOrigin());
		request.setRenderer(SC.isIE() ? "VML" : "SVG");
		request.setScale(cache.getLayer().getMapModel().getMapView().getCurrentScale());
		request.setStyleInfo(cache.getLayer().getLayerInfo().getNamedStyleInfo());
		return request;
	}

}