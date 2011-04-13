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

package org.geomajas.puregwt.client.map.layer;

import java.util.ArrayList;
import java.util.List;

import org.geomajas.command.CommandResponse;
import org.geomajas.command.dto.GetVectorTileRequest;
import org.geomajas.command.dto.GetVectorTileResponse;
import org.geomajas.layer.tile.TileCode;
import org.geomajas.layer.tile.VectorTile;
import org.geomajas.layer.tile.VectorTile.VectorTileContentType;
import org.geomajas.puregwt.client.command.Command;
import org.geomajas.puregwt.client.command.CommandCallback;
import org.geomajas.puregwt.client.command.CommandService;
import org.geomajas.puregwt.client.command.Deferred;
import org.geomajas.puregwt.client.map.gfx.VectorTileObject;

/**
 * Presenter for a single tile.
 * 
 * @author Pieter De Graef
 */
public class TilePresenter {

	/**
	 * Enumeration the determines tile loading status.
	 * 
	 * @author Pieter De Graef
	 */
	static enum STATUS {

		EMPTY, LOADING, LOADED
	}

	/**
	 * Tile view definition.
	 * 
	 * @author Pieter De Graef
	 */
	public interface TileView {

		void setContent(String content);
	}

	private VectorLayerRenderer renderer;

	private TileCode tileCode;

	private TileView display;

	private List<TileCode> siblings;

	private Deferred deferred;

	private CommandService service;

	// -------------------------------------------------------------------------
	// Constructor:
	// -------------------------------------------------------------------------

	public TilePresenter(VectorLayerRenderer renderer, TileCode tileCode) {
		this.renderer = renderer;
		this.tileCode = tileCode;
		siblings = new ArrayList<TileCode>();
		service = new CommandService();
	}

	// -------------------------------------------------------------------------
	// Public methods:
	// -------------------------------------------------------------------------

	public void render() {
		render(true);
	}

	public void renderSiblings() {
		for (TileCode siblingCode : siblings) {
			TilePresenter tilePresenter = renderer.getTile(siblingCode);
			if (tilePresenter == null) {
				// The sibling is not yet rendered, do so now:
				tilePresenter = renderer.addTile(siblingCode);
				tilePresenter.render(false);
			} else if (tilePresenter.getStatus() == STATUS.EMPTY) {
				tilePresenter.render(false);
			}
		}
	}

	/** Cancel the fetching of this tile. No call-back will be executed anymore. */
	public void cancel() {
		if (deferred != null) {
			deferred.cancel();
		}
	}

	// -------------------------------------------------------------------------
	// Getters and setters:
	// -------------------------------------------------------------------------

	public TileCode getTileCode() {
		return tileCode;
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
		if (display != null) {
			return STATUS.LOADED;
		}
		if (deferred == null) {
			return STATUS.EMPTY;
		}
		return STATUS.LOADING;
	}

	public STATUS getSiblingStatus() {
		if (getStatus() == STATUS.LOADED) {
			if (siblings.size() > 0) {
				// Check the first sibling:
				TilePresenter tilePresenter = renderer.getTile(siblings.get(0));
				if (tilePresenter == null) {
					return STATUS.EMPTY;
				} else {
					return tilePresenter.getStatus();
				}
			}
			return STATUS.LOADED;
		}
		return STATUS.EMPTY;
	}

	// -------------------------------------------------------------------------
	// Private methods:
	// -------------------------------------------------------------------------

	private void render(final boolean renderSiblings) {
		Command command = createCommand();
		deferred = service.execute(command, new CommandCallback() {

			public void onSuccess(CommandResponse response) {
				if (!(deferred != null && deferred.isCancelled()) && response instanceof GetVectorTileResponse) {
					GetVectorTileResponse tileResponse = (GetVectorTileResponse) response;
					VectorTile tile = tileResponse.getTile();
					for (TileCode relatedTile : tile.getCodes()) {
						siblings.add(relatedTile);
					}
					if (tile.getContentType() == VectorTileContentType.STRING_CONTENT) {
						display = new VectorTileObject();
						display.setContent(tile.getFeatureContent());
						renderer.getVectorContainer().add((VectorTileObject) display);
					} else {
						throw new RuntimeException("TilePresenter - Rendering rasterized tiles: Not implemented...");
					}
					if (renderSiblings) {
						renderSiblings();
					}
				}
			}

			public void onFailure(Throwable error) {
			}
		});
	}

	private Command createCommand() {
		GetVectorTileRequest request = new GetVectorTileRequest();
		request.setCode(tileCode);
		request.setCrs(renderer.getViewPort().getCrs());
		request.setFilter(renderer.getLayer().getFilter());
		request.setLayerId(renderer.getLayer().getServerLayerId());

		// TODO Add support for labels
		request.setPaintGeometries(true);
		request.setPaintLabels(false);
		// request.setPaintLabels(renderer.getLayer().isLabeled());
		request.setPanOrigin(renderer.getViewPort().getPanOrigin());
		// request.setRenderer(SC.isIE() ? "VML" : "SVG");
		// TODO Add support for VML.
		request.setRenderer("SVG");
		request.setScale(renderer.getViewPort().getScale());
		request.setStyleInfo(renderer.getLayer().getLayerInfo().getNamedStyleInfo());
		request.setFeatureIncludes(0);
		Command command = new Command("command.render.GetVectorTile");
		command.setCommandRequest(request);
		return command;
	}
}