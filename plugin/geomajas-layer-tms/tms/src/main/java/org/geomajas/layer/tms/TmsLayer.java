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

package org.geomajas.layer.tms;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.geomajas.annotation.Api;
import org.geomajas.configuration.RasterLayerInfo;
import org.geomajas.geometry.Bbox;
import org.geomajas.geometry.CrsTransform;
import org.geomajas.global.ExceptionCode;
import org.geomajas.global.GeomajasException;
import org.geomajas.layer.RasterLayer;
import org.geomajas.layer.tile.RasterTile;
import org.geomajas.layer.tile.TileCode;
import org.geomajas.layer.tms.configuration.TileMapInfo;
import org.geomajas.layer.tms.configuration.TileSetInfo;
import org.geomajas.service.GeoService;
import org.opengis.geometry.MismatchedDimensionException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.vividsolutions.jts.geom.Envelope;

/**
 * A raster layer implementation that supports the TMS "standard". This layer has the advantage that no
 * {@link RasterLayerInfo} needs to be configured, as the target TMS url should contain a description of the TMS layer.
 * 
 * @author Pieter De Graef
 * @since 1.0.0
 */
@Api
public class TmsLayer implements RasterLayer {

	private final Logger log = LoggerFactory.getLogger(TmsLayer.class);

	private String id;

	private String baseTmsUrl;

	private String extension = "jpg";

	private String version = "1.0.0";

	private boolean useVersionInUrl;

	private TileMapInfo tileMapInfo;

	private RasterLayerInfo layerInfo;

	// Spring services:

	@Autowired
	private GeoService geoService;

	@Autowired
	private TmsConfigurationService configurationService;

	@Autowired
	private TileService tileService;

	private TileServiceState state;

	// ------------------------------------------------------------------------
	// Construction:
	// ------------------------------------------------------------------------

	@PostConstruct
	protected void postConstruct() throws GeomajasException {
		if (null == baseTmsUrl) {
			throw new GeomajasException(ExceptionCode.PARAMETER_MISSING, "baseTmsUrl");
		}

		// Make sure we have a base URL we can work with:
		if (!baseTmsUrl.endsWith("/")) {
			baseTmsUrl += "/";
		}

		// Make sure there is a correct RasterLayerInfo object:
		if (layerInfo == null) {
			try {
				tileMapInfo = configurationService.getCapabilities(baseTmsUrl);
				version = tileMapInfo.getVersion();
				extension = tileMapInfo.getTileFormat().getExtension();
				layerInfo = configurationService.asLayerInfo(tileMapInfo);
			} catch (TmsConfigurationException e) {
				throw new GeomajasException(e);
			}
		} else if (extension == null) {
			throw new GeomajasException(ExceptionCode.PARAMETER_MISSING, "extension");
		}

		// Finally prepare some often needed values:
		state = new TileServiceState(geoService, layerInfo);
	}

	// ------------------------------------------------------------------------
	// RasterLayer implementation:
	// ------------------------------------------------------------------------

	/** {@inheritDoc} */
	public RasterLayerInfo getLayerInfo() {
		return layerInfo;
	}

	/** {@inheritDoc} */
	public CoordinateReferenceSystem getCrs() {
		return state.getCrs();
	}

	/** {@inheritDoc} */
	public String getId() {
		return id;
	}

	public List<RasterTile> paint(CoordinateReferenceSystem boundsCrs, Envelope bounds, double scale)
			throws GeomajasException {
		log.debug("Fetching TMS tiles for bounds : {}", bounds.toString());
		Envelope layerBounds = bounds;
		double layerScale = scale;
		CrsTransform layerToMap = null;
		boolean needTransform = !state.getCrs().equals(boundsCrs);

		try {
			// We don't necessarily need to split into same CRS and different CRS cases, the latter implementation uses
			// identity transform if crs's are equal for map and layer but might introduce bugs in rounding and/or
			// conversions.
			if (needTransform) {
				layerToMap = geoService.getCrsTransform(state.getCrs(), boundsCrs);
				CrsTransform mapToLayer = geoService.getCrsTransform(boundsCrs, state.getCrs());

				// Translate the map coordinates to layer coordinates, assumes equal x-y orientation
				layerBounds = geoService.transform(bounds, mapToLayer);
				layerScale = bounds.getWidth() * scale / layerBounds.getWidth();
			}
		} catch (MismatchedDimensionException e) {
			throw new GeomajasException(e, ExceptionCode.RENDER_DIMENSION_MISMATCH);
		}

		// Clip with maximum bounds and check for null:
		layerBounds = layerBounds.intersection(state.getMaxBounds());
		if (bounds.isNull()) {
			return new ArrayList<RasterTile>();
		}

		int tileLevel = tileService.getTileLevel(state, 1 / layerScale);
		double tileWidth = tileService.getTileWidth(state, tileLevel);
		double tileHeight = tileService.getTileHeight(state, tileLevel);

		int ymin = (int) Math.floor((layerBounds.getMinY() - state.getMaxBounds().getMinY()) / tileHeight);
		int ymax = (int) Math.ceil((layerBounds.getMaxY() - state.getMaxBounds().getMinY()) / tileHeight);
		int xmin = (int) Math.floor((layerBounds.getMinX() - state.getMaxBounds().getMinX()) / tileWidth);
		int xmax = (int) Math.ceil((layerBounds.getMaxX() - state.getMaxBounds().getMinX()) / tileWidth);

		double lowerLeftX = state.getMaxBounds().getMinX() + xmin * tileWidth;
		double lowerLeftY = state.getMaxBounds().getMinY() + ymin * tileHeight;

		List<RasterTile> result = new ArrayList<RasterTile>();
		for (int i = xmin; i < xmax; i++) {
			for (int j = ymin; j < ymax; j++) {
				// Get the lower-left corner of each tile:
				double x = lowerLeftX + (i - xmin) * tileWidth;
				double y = lowerLeftY + (j - ymin) * tileHeight;

				Bbox worldBox;
				Bbox layerBox;

				if (needTransform) {
					layerBox = new Bbox(x, y, tileWidth, tileHeight);
					// Transforming back to map coordinates will only result in a proper grid if the transformation
					// is nearly affine
					worldBox = geoService.transform(layerBox, layerToMap);
				} else {
					worldBox = new Bbox(x, y, tileWidth, tileHeight);
					layerBox = worldBox;
				}
				// Rounding to avoid white space between raster tiles lower-left becomes upper-left in inverted y-space
				Bbox screenBox = new Bbox(Math.round(scale * worldBox.getX()), -Math.round(scale * worldBox.getMaxY()),
						Math.round(scale * worldBox.getMaxX()) - Math.round(scale * worldBox.getX()), Math.round(scale
								* worldBox.getMaxY())
								- Math.round(scale * worldBox.getY()));

				RasterTile image = new RasterTile(screenBox, getId() + "." + tileLevel + "." + i + "," + j);

				TileCode tileCode = new TileCode(tileLevel, i, j);
				image.setCode(tileCode);
				image.setUrl(formatUrl(tileCode));
				result.add(image);
			}
		}
		return result;
	}

	private String formatUrl(TileCode tileCode) {
//		StringBuilder stringBuilder = new StringBuilder(baseTmsUrl);
//		if (useVersionInUrl && version != null) {
//			stringBuilder.append(version);
//			stringBuilder.append("/");
//		}
//		stringBuilder.append(tileCode.getTileLevel());
//		stringBuilder.append("/");
		StringBuilder stringBuilder = new StringBuilder(tileTileLevelBaseUrl(tileCode.getTileLevel()));
		stringBuilder.append(tileCode.getX());
		stringBuilder.append("/");
		stringBuilder.append(tileCode.getY());
		stringBuilder.append(".");
		stringBuilder.append(extension);
		return stringBuilder.toString();
	}

	private String tileTileLevelBaseUrl(int tileLevel) {
		String tileString = tileLevel + "";
		if (tileMapInfo != null) {
			TileSetInfo tileSet = tileMapInfo.getTileSets().getTileSets().get(tileLevel); // assuming they are ordered
			String href = tileSet.getHref();
			if (href.startsWith("http://") || href.startsWith("https://")) {
				return href;
			}
			tileString = tileSet.getHref();
		}
		String temp = baseTmsUrl;
		if (tileMapInfo == null && useVersionInUrl && version != null) {
			temp += version + "/";
		}
		return temp + tileString + "/";
	}

	// ------------------------------------------------------------------------
	// Getters and setters:
	// ------------------------------------------------------------------------

	/**
	 * Set the configuration object for this layer. This is not a requirement for the TMS layer, as the target TMS URL
	 * should point to a TMS description file. If no layerInfo object is configured, it will be built using the TMS
	 * description.
	 * 
	 * @param layerInfo
	 *            The new configuration object.
	 */
	public void setLayerInfo(RasterLayerInfo layerInfo) {
		this.layerInfo = layerInfo;
	}

	/**
	 * Set the layer identifier.
	 * 
	 * @param id
	 *            layer id
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * Get the base URL for the TMS layer.
	 * 
	 * @return The base URL for the TMS layer.
	 */
	public String getBaseTmsUrl() {
		return baseTmsUrl;
	}

	/**
	 * Set the base URL for the TMS layer.
	 * 
	 * @param baseTmsUrl
	 *            The base URL for the TMS layer.
	 */
	public void setBaseTmsUrl(String baseTmsUrl) {
		this.baseTmsUrl = baseTmsUrl;
	}

	/**
	 * Get the extension of the images/tiles to retrieve. Default value is "jpg".
	 * 
	 * @return The image extension.
	 */
	public String getExtension() {
		return extension;
	}

	/**
	 * The extension of the images/tiles to retrieve. This only needs to be supplied if you're using a
	 * {@link RasterLayerInfo} as configuration.
	 * 
	 * @param extension
	 *            The extension. Default value is "jpg".
	 */
	public void setExtension(String extension) {
		this.extension = extension;
	}

	/**
	 * Get the TMS layer version. Default value is "1.0.0".
	 * 
	 * @return The TMS version used.
	 */
	public String getVersion() {
		return version;
	}

	/**
	 * The TMS version used. This only needs to be supplied if you're using a {@link RasterLayerInfo} as configuration.
	 * 
	 * @param version
	 *            The TMS version used. Default value is "1.0.0".
	 */
	public void setVersion(String version) {
		this.version = version;
	}

	/**
	 * Will the version be added in the tile URLs or not?
	 * 
	 * @return true or false.
	 */
	public boolean isUseVersionInUrl() {
		return useVersionInUrl;
	}

	/**
	 * Determine whether or not to add the version in the URL or not. By default the version is added after the base TMS
	 * URL, but not all implementation actually do this, so we made it optional.
	 * 
	 * @param useVersionInUrl
	 *            true or false.
	 */
	public void setUseVersionInUrl(boolean useVersionInUrl) {
		this.useVersionInUrl = useVersionInUrl;
	}
}