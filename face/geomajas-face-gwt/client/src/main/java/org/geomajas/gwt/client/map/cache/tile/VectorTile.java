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

package org.geomajas.gwt.client.map.cache.tile;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.geomajas.command.dto.GetVectorTileRequest;
import org.geomajas.command.dto.GetVectorTileResponse;
import org.geomajas.gwt.client.command.AbstractCommandCallback;
import org.geomajas.gwt.client.command.Deferred;
import org.geomajas.gwt.client.command.GwtCommand;
import org.geomajas.gwt.client.command.GwtCommandDispatcher;
import org.geomajas.gwt.client.gfx.PaintableGroup;
import org.geomajas.gwt.client.gfx.PainterVisitor;
import org.geomajas.gwt.client.map.MapModel;
import org.geomajas.gwt.client.map.MapView;
import org.geomajas.gwt.client.map.cache.SpatialCache;
import org.geomajas.gwt.client.map.layer.VectorLayer;
import org.geomajas.gwt.client.spatial.Bbox;
import org.geomajas.gwt.client.util.Dom;
import org.geomajas.gwt.client.util.Log;
import org.geomajas.layer.tile.TileCode;
import org.geomajas.layer.tile.VectorTile.VectorTileContentType;

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

	/** dependent tile codes */
	private List<TileCode> codes = new ArrayList<TileCode>();

	private Deferred deferred;
	
	private GetVectorTileRequest lastRequest;

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
		deferred = GwtCommandDispatcher.getInstance().execute(command,
				new AbstractCommandCallback<GetVectorTileResponse>() {

			public void execute(GetVectorTileResponse tileResponse) {
				if (null == deferred || !deferred.isCancelled()) {
					org.geomajas.layer.tile.VectorTile tile = tileResponse.getTile();
					for (TileCode relatedTile : tile.getCodes()) {
						codes.add(relatedTile);
					}
					code = tile.getCode();
					contentType = tile.getContentType();
					featureContent.setContent(tile.getFeatureContent());
					labelContent.setContent(tile.getLabelContent());
					lastRequest = request;
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
	 * Execute a {@link TileFunction} in this tile and all connected tiles. Only tiles which are not yet included in
	 * updatedTiles are processed. Tiles are added in updatedTiles when processed.
	 * If these connected tiles are not yet part of the cache, then they will be fetched before applying the
	 * {@link TileFunction} on them.
	 *
	 * @param filter A filter that needs to be used in case connected tile need to be fetched.
	 * @param callback The {@link TileFunction} to execute on the connected tiles.
	 * @param updatedTiles list of already processed tiles to assure tiles are only processed once
	 */
	public void applyConnectedOnce(final String filter, final TileFunction<VectorTile> callback,
			final Map<String, VectorTile> updatedTiles) {
		apply(filter, new TileFunction<VectorTile>() {

			public void execute(VectorTile tile) {
				updatedTiles.put(tile.getCode().toString(), tile);
				callback.execute(tile);
				List<TileCode> tileCodes = tile.getCodes();
				for (TileCode tileCode : tileCodes) {
					if (!updatedTiles.containsKey(tileCode.toString())) {
						VectorTile temp = tile.cache.addTile(tileCode);
						updatedTiles.put(tileCode.toString(), temp);
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
				deferred.addCallback(new AbstractCommandCallback<GetVectorTileResponse>() {

					public void execute(GetVectorTileResponse response) {
						callback.execute(self);
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
				break;
			default:
				throw new IllegalStateException("Unknown status " + getStatus());
		}
	}

	/**
	 * Return the current status of this VectorTile. Can be one of the following:
	 * <ul>
	 * <li>STATUS.EMPTY</li>
	 * <li>STATUS.LOADING</li>
	 * <li>STATUS.LOADED</li>
	 * </ul>
	 *
	 * @return status
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
		super.cancel();
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
		return lastRequest == null || !request.isPartOf(lastRequest);
	}

	// -------------------------------------------------------------------------
	// Private methods:
	// -------------------------------------------------------------------------

	private GetVectorTileRequest createRequest(String filter) {
		GetVectorTileRequest request = new GetVectorTileRequest();
		VectorLayer layer = cache.getLayer();
		MapModel mapModel = layer.getMapModel();
		MapView mapView = mapModel.getMapView();
		request.setCode(code);
		request.setCrs(mapModel.getCrs());
		request.setFilter(filter);
		request.setLayerId(layer.getServerLayerId());
		request.setPaintGeometries(!featureContent.isLoaded());
		request.setPaintLabels(layer.isLabelsShowing() && !labelContent.isLoaded());
		request.setPanOrigin(mapView.getPanOrigin());
		request.setRenderer(Dom.isIE() ? "VML" : "SVG");
		request.setScale(mapView.getCurrentScale());
		request.setStyleInfo(layer.getLayerInfo().getNamedStyleInfo());
		return request;
	}

	public String toString() {
		return super.toString();
	}
}