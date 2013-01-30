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

package org.geomajas.layer.tms.tile;

import java.util.StringTokenizer;

import org.geomajas.layer.tile.TileCode;
import org.geomajas.layer.tms.xml.TileMap;
import org.geomajas.layer.tms.xml.TileSet;

/**
 * Tile URL builder that builds URLs based upon a {@link TileMap} configuration object. This class uses the
 * tile-level to get the correct {@link TileSet} object, and assumes that these tile-sets are ordered.
 * 
 * @author Pieter De Graef
 * @author Kristof Heirwegh
 */
public class TileMapUrlBuilder implements TileUrlBuilder {

	private final TileMap tileMap;

	public TileMapUrlBuilder(TileMap tileMap) {
		if (tileMap == null) {
			throw new IllegalStateException("No null values allowed.");
		}
		this.tileMap = tileMap;
	}

	/** {@inheritDoc} */
	public String buildUrl(TileCode tileCode, String baseTmsUrl) {
		return buildUrl(tileCode, tileMap, baseTmsUrl);
	}
	
	/**
	 * Replaces the proxy url with the correct url from the tileMap.
	 * 
	 * @return correct url to TMS service
	 */
	public static String resolveProxyUrl(String relativeUrl, TileMap tileMap, String baseTmsUrl) {
		TileCode tc = parseTileCode(relativeUrl);
		return buildUrl(tc, tileMap, baseTmsUrl);
	}

	// ----------------------------------------------------------
	
	private static String buildUrl(TileCode tileCode, TileMap tileMap, String baseTmsUrl) {
		if (tileCode == null || tileMap == null || baseTmsUrl == null) {
			throw new IllegalArgumentException("All parameters are required");
		}
		
		StringBuilder builder;

		// assuming they are ordered:
		TileSet tileSet = tileMap.getTileSets().getTileSets().get(tileCode.getTileLevel());
		String href = tileSet.getHref();
		if (href.startsWith("http://") || href.startsWith("https://")) {
			builder = new StringBuilder(href);
			if (!href.endsWith("/")) {
				builder.append("/");
			}
		} else {
			builder = new StringBuilder(baseTmsUrl);
			if (!baseTmsUrl.endsWith("/")) {
				builder.append("/");
			}
			builder.append(href);
			builder.append("/");
		}

		builder.append(tileCode.getX());
		builder.append("/");
		builder.append(tileCode.getY());
		builder.append(".");
		builder.append(tileMap.getTileFormat().getExtension());
		return builder.toString();
	}
	
	/**
	 * @param relativeUrl just the part with level/x/y.extension
	 * @return
	 */
	public static TileCode parseTileCode(String relativeUrl) {
		TileCode tc = new TileCode();
		StringTokenizer tokenizer = new StringTokenizer(relativeUrl, "/");
		tc.setTileLevel(Integer.parseInt(tokenizer.nextToken()));
		tc.setX(Integer.parseInt(tokenizer.nextToken()));
		tc.setY(Integer.parseInt(tokenizer.nextToken().split("\\.")[0]));
		return tc;
	}
}