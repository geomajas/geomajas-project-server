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
import org.geomajas.layer.tms.xml.TileMap;
import org.geomajas.layer.tms.tile.SimpleTmsUrlBuilder;
import org.geomajas.layer.tms.tile.TileMapUrlBuilder;
import org.geomajas.layer.tms.tile.TileService;
import org.geomajas.layer.tms.tile.TileServiceState;
import org.geomajas.layer.tms.tile.TileUrlBuilder;
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

	private TileMap tileMap;

	private RasterLayerInfo layerInfo;

	// Spring services:

	@Autowired
	private GeoService geoService;

	@Autowired
	private TmsConfigurationService configurationService;

	@Autowired
	private TileService tileService;

	private TileServiceState state;

	private TileUrlBuilder urlBuilder;

	// ------------------------------------------------------------------------
	// Construction:
	// ------------------------------------------------------------------------

	/** Finish initializing the service. */
	@PostConstruct
	protected void postConstruct() throws GeomajasException {
		if (null == baseTmsUrl) {
			throw new GeomajasException(ExceptionCode.PARAMETER_MISSING, "baseTmsUrl");
		}

		// Make sure we have a base URL we can work with:
		if ((baseTmsUrl.startsWith("http://") || baseTmsUrl.startsWith("https://")) && !baseTmsUrl.endsWith("/")) {
			baseTmsUrl += "/";
		}

		// Make sure there is a correct RasterLayerInfo object:
		if (layerInfo == null) {
			tileMap = configurationService.getCapabilities(baseTmsUrl);
			version = tileMap.getVersion();
			extension = tileMap.getTileFormat().getExtension();
			layerInfo = configurationService.asLayerInfo(tileMap);
		} else if (extension == null) {
			throw new GeomajasException(ExceptionCode.PARAMETER_MISSING, "extension");
		}

		// Finally prepare some often needed values:
		state = new TileServiceState(geoService, layerInfo);
		if (tileMap != null) {
			urlBuilder = new TileMapUrlBuilder(tileMap, baseTmsUrl);
		} else {
			urlBuilder = new SimpleTmsUrlBuilder(baseTmsUrl, extension);
		}
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

	/** {@inheritDoc} */
	public List<RasterTile> paint(CoordinateReferenceSystem boundsCrs, Envelope bounds, double scale)
			throws GeomajasException {
		log.debug("Fetching TMS tiles for bounds : {}", bounds.toString());
		Envelope layerBounds = bounds;
		double layerScale = scale;
		CrsTransform layerToMap = null;
		boolean needTransform = !state.getCrs().equals(boundsCrs);

		try {
			// We don't necessarily need to split into same CRS and different CRS cases, the latter implementation uses
			// identity transform if CRSs are equal for map and layer but might introduce bugs in rounding and/or
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

		// Get the tile level and it's size in world space:
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

				// Get the bounding box for each tile:
				Bbox tileBounds = new Bbox(x, y, tileWidth, tileHeight);
				if (needTransform) {
					// Transforming back to map coordinates will only result in a proper grid if the transformation
					// is nearly affine
					tileBounds = geoService.transform(tileBounds, layerToMap);
				}
				// Rounding to avoid white space between raster tiles lower-left becomes upper-left in inverted y-space
				Bbox screenBox = new Bbox(Math.round(scale * tileBounds.getX()), -Math.round(scale
						* tileBounds.getMaxY()), Math.round(scale * tileBounds.getMaxX())
						- Math.round(scale * tileBounds.getX()), Math.round(scale * tileBounds.getMaxY())
						- Math.round(scale * tileBounds.getY()));

				RasterTile image = new RasterTile(screenBox, getId() + "." + tileLevel + "." + i + "," + j);

				TileCode tileCode = new TileCode(tileLevel, i, j);
				image.setCode(tileCode);
				image.setUrl(urlBuilder.buildUrl(tileCode));
				result.add(image);
			}
		}
		return result;
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
	 * @since 1.0.0
	 */
	@Api
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
	 * @since 1.0.0
	 */
	@Api
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
}