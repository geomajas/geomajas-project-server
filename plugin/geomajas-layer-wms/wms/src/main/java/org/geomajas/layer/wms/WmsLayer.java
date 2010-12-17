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
package org.geomajas.layer.wms;

import java.util.ArrayList;
import java.util.List;

import org.geomajas.configuration.Parameter;
import org.geomajas.configuration.RasterLayerInfo;
import org.geomajas.global.Api;
import org.geomajas.global.GeomajasException;
import org.geomajas.layer.LayerException;
import org.geomajas.layer.RasterLayer;
import org.geomajas.layer.tile.RasterTile;
import org.geomajas.service.GeoService;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.springframework.beans.factory.annotation.Autowired;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Envelope;

import javax.annotation.PostConstruct;

/**
 * <p>
 * Layer model for accessing raster data from WMS servers. It has support for most WMS versions: up to 1.3.0. When using
 * this layer, note that the following fields are required:
 * <ul>
 * <li><b>baseWmsUrl</b>: The base URL to the WMS server.</li>
 * <li><b>format</b>: The format for the returned images.</li>
 * <li><b>version</b>: The version of WMS to use.</li>
 * <li><b>styles</b>: The styles to use when rendering the WMS images.</li>
 * </ul>
 * There always is the option of adding additional parameters to the WMS GetMap requests, by filling the
 * <code>parameters</code> list. Such parameters could include optional WMS GetMap parameters, such as "transparency",
 * but also "user" and "password".
 * </p>
 * <p>
 * This layer also supports BASIC and DIGEST authentication. In order to make this functionality, make sure you have
 * configured the <code>authentication</code> field.
 * </p>
 * 
 * @author Jan De Moerloose
 * @author Pieter De Graef
 * @author Oliver May
 * @since 1.7.1
 */
@Api
public class WmsLayer implements RasterLayer {

	@Autowired
	private WmsLayerService wmsLayerService;

	@Autowired
	private GeoService geoService;

	protected List<Resolution> resolutions = new ArrayList<Resolution>();

	private String id, baseWmsUrl, format, version, styles = "";

	private List<Parameter> parameters;

	private RasterLayerInfo layerInfo;

	private CoordinateReferenceSystem crs;

	private HttpAuthentication authentication;

	// ------------------------------------------------------------------------
	// RasterLayer implementation:
	// ------------------------------------------------------------------------

	/**
	 * Paints the specified bounds optimized for the specified scale in pixel/unit.
	 * 
	 * @param targetCrs
	 *            Coordinate reference system used for bounds
	 * @param bounds
	 *            bounds to request images for
	 * @param scale
	 *            scale or zoom level (unit?)
	 * @return a list of raster images that covers the bounds
	 * @throws GeomajasException
	 *             oops
	 */
	public List<RasterTile> paint(CoordinateReferenceSystem targetCrs, Envelope bounds, double scale)
			throws GeomajasException {
		return wmsLayerService.paint(this, targetCrs, bounds, scale);
	}

	// ------------------------------------------------------------------------
	// Getters and setters:
	// ------------------------------------------------------------------------

	/**
	 * Return the layers identifier.
	 */
	public String getId() {
		return id;
	}

	/**
	 * Set the layers identifier.
	 * 
	 * @param id
	 *            The new value.
	 */
	public void setId(String id) {
		this.id = id;
	}

	@PostConstruct
	private void postConstruct() throws GeomajasException {
		crs = geoService.getCrs(getLayerInfo().getCrs());
		wmsLayerService.calculateResolutions(this);
	}

	/**
	 * Return this layers coordinate reference system (CRS). This value is initialized when the <code>LayerInfo</code>
	 * object is set.
	 */
	public CoordinateReferenceSystem getCrs() {
		return crs;
	}

	/**
	 * Return the full list of supported resolutions for this WMS layer. This value is initialized when the
	 * <code>LayerInfo</code> object is set.
	 * 
	 * @return Returns the resolution list.
	 * @since 1.8.0
	 */
	public List<Resolution> getResolutions() {
		return resolutions;
	}

	/**
	 * Return the layer info object.
	 * 
	 * @since 1.7.1
	 */
	public RasterLayerInfo getLayerInfo() {
		return layerInfo;
	}

	/**
	 * Set the layer configuration.
	 * 
	 * @param layerInfo
	 *            layer information
	 * @throws LayerException
	 *             oops
	 * @since 1.7.1
	 */
	@Api
	public void setLayerInfo(RasterLayerInfo layerInfo) throws LayerException {
		this.layerInfo = layerInfo;
	}

	/**
	 * Return the base URL to the WMS server.
	 * 
	 * @return Returns the base URL.
	 */
	public String getBaseWmsUrl() {
		return baseWmsUrl;
	}

	/**
	 * 
	 * @param baseWmsUrl
	 * @since 1.7.1
	 */
	@Api
	public void setBaseWmsUrl(String baseWmsUrl) {
		this.baseWmsUrl = baseWmsUrl;
	}

	/**
	 * Get the format for the returned images for all <code>GetMap</code> requests.
	 * 
	 * @return The format.
	 * @since 1.7.1
	 */
	public String getFormat() {
		return format;
	}

	/**
	 * Set file format to request.
	 * 
	 * @param format
	 *            file format. For allowed values, check your WMS server.
	 * @since 1.7.1
	 */
	@Api
	public void setFormat(String format) {
		this.format = format;
	}

	/**
	 * Returns the WMS version to use.
	 * 
	 * @return Returns the version.
	 * @since 1.7.1
	 */
	public String getVersion() {
		return version;
	}

	/**
	 * Set WMS version to use.
	 * 
	 * @param version
	 *            WMS version. For allowed values, check your WMS server.
	 * @since 1.7.1
	 */
	@Api
	public void setVersion(String version) {
		this.version = version;
	}

	/**
	 * Returns the styles to use in the <code>GetMap</code> requests.
	 * 
	 * @return Returns the styles.
	 * @since 1.7.1
	 */
	public String getStyles() {
		return styles;
	}

	/**
	 * Set the styles.
	 * 
	 * @param styles
	 *            styles. For allowed values, check your WMS server.
	 * @since 1.7.1
	 */
	@Api
	public void setStyles(String styles) {
		this.styles = styles;
	}

	/**
	 * Set additional parameters to include in all WMS <code>getMap</code> requests.
	 * 
	 * @param parameters
	 *            parameters. For possible keys and values, check your WMS server.
	 * @since 1.7.1
	 */
	@Api
	public void setParameters(List<Parameter> parameters) {
		this.parameters = parameters;
	}

	/**
	 * Returns the list of additional parameters to include in all WMS <code>getMap</code> requests.
	 * 
	 * @return The additional parameter list.
	 * @since 1.7.1
	 */
	public List<Parameter> getParameters() {
		return parameters;
	}

	/**
	 * Return the HTTP authentication object. This configuration object provides support for basic, digest and NTLM
	 * authentication on the WMS server.
	 * 
	 * @return Return the configuration object, or null if no HTTP authentication is required.
	 * @since 1.8.0
	 */
	public HttpAuthentication getAuthentication() {
		return authentication;
	}

	/**
	 * <p>
	 * Set the HTTP authentication object. This configuration object provides support for basic, digest and NTLM
	 * authentication on the WMS server. If no HTTP authentication is required, leave this empty.
	 * </p>
	 * <p>
	 * Note that there is still the option of adding a user name and password as HTTP parameters, as some WMS server
	 * support. To do that, just add <code>parameters</code>.
	 * </p>
	 * 
	 * @param digest
	 *            The digest configuration object.
	 * @since 1.8.0
	 */
	@Api
	public void setAuthentication(HttpAuthentication digest) {
		this.authentication = digest;
	}

	// ------------------------------------------------------------------------
	// Protected methods:
	// ------------------------------------------------------------------------

	protected void addResolution(double resolution, int level, int tileWidth, int tileHeight) {
		resolutions.add(new Resolution(resolution, level, tileWidth, tileHeight));
	}

	protected Resolution createResolution(double resolution, int level, int tileWidth, int tileHeight) {
		return new Resolution(resolution, level, tileWidth, tileHeight);
	}

	protected RasterGrid createRasterGrid(Coordinate lowerLeft, int xmin, int ymin, int xmax, int ymax,
			double tileWidth, double tileHeight) {
		return new RasterGrid(lowerLeft, xmin, ymin, xmax, ymax, tileWidth, tileHeight);
	}

	// ------------------------------------------------------------------------
	// Public inner classes:
	// ------------------------------------------------------------------------

	/**
	 * Grid definition for a WMS layer. It is used internally in the WMS layer.
	 * 
	 * @author Jan De Moerloose
	 * @author Pieter De Graef
	 */
	public class RasterGrid {

		private Coordinate lowerLeft;

		private int xmin;

		private int ymin;

		private int xmax;

		private int ymax;

		private double tileWidth;

		private double tileHeight;

		RasterGrid(Coordinate lowerLeft, int xmin, int ymin, int xmax, int ymax, double tileWidth, double tileHeight) {
			super();
			this.lowerLeft = lowerLeft;
			this.xmin = xmin;
			this.ymin = ymin;
			this.xmax = xmax;
			this.ymax = ymax;
			this.tileWidth = tileWidth;
			this.tileHeight = tileHeight;
		}

		public Coordinate getLowerLeft() {
			return lowerLeft;
		}

		public double getTileHeight() {
			return tileHeight;
		}

		public double getTileWidth() {
			return tileWidth;
		}

		public int getXmax() {
			return xmax;
		}

		public int getXmin() {
			return xmin;
		}

		public int getYmax() {
			return ymax;
		}

		public int getYmin() {
			return ymin;
		}
	}

	/**
	 * Single resolution definition for a WMS layer. This class is used internally in the WMS layer, and therefore has
	 * no public constructors.
	 * 
	 * @author Jan De Moerloose
	 * @author Pieter De Graef
	 */
	class Resolution {

		private double resolution;

		private int level;

		private int tileWidth;

		private int tileHeight;

		/**
		 * Constructor that immediately requires all fields.
		 * 
		 * @param resolution
		 *            The actual resolution value. This is the reverse of the scale.
		 * @param level
		 *            The level in the quad tree.
		 * @param tileWidth
		 *            The width of a tile at the given tile level.
		 * @param tileHeight
		 *            The height of a tile at the given tile level.
		 */
		Resolution(double resolution, int level, int tileWidth, int tileHeight) {
			this.resolution = resolution;
			this.level = level;
			this.tileWidth = tileWidth;
			this.tileHeight = tileHeight;
		}

		public int getLevel() {
			return level;
		}

		public int getTileHeightPx() {
			return tileHeight;
		}

		public int getTileWidthPx() {
			return tileWidth;
		}

		public double getTileHeight() {
			return tileHeight * resolution;
		}

		public double getTileWidth() {
			return tileWidth * resolution;
		}

		public double getResolution() {
			return resolution;
		}
	}
}