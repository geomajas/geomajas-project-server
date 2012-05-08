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
package org.geomajas.configuration;

import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotNull;

import org.geomajas.annotation.Api;
import org.geomajas.configuration.client.ScaleInfo;
import org.geomajas.layer.LayerType;

/**
 * Information about a raster layer.
 * 
 * @author Joachim Van der Auwera
 * @since 1.6.0
 */
@Api(allMethods = true)
public class RasterLayerInfo extends LayerInfo implements IsInfo {

	private static final long serialVersionUID = 151L;

	private String dataSourceName;

	@NotNull
	private int tileWidth;

	@NotNull
	private int tileHeight;

	private List<ScaleInfo> zoomLevels = new ArrayList<ScaleInfo>();

	/**
	 * Create raster layer.
	 */
	public RasterLayerInfo() {
		super();
		setLayerType(LayerType.RASTER);
	}

	/**
	 * Get the data source name. This is used by the layer to know which data source to contact.
	 * 
	 * @return data source name
	 */
	public String getDataSourceName() {
		return dataSourceName;
	}

	/**
	 * Set the data source name. This is used by the layer to know which data source to contact.
	 * 
	 * @param dataSourceName
	 *            data source name
	 */
	public void setDataSourceName(String dataSourceName) {
		this.dataSourceName = dataSourceName;
	}

	/**
	 * Get tile width in pixels.
	 * <p/>
	 * Raster layers often use fixed tile sizes which need to be combined to get the full picture. This allows you to
	 * get the width of these tiles (when applicable).
	 * 
	 * @return tile width in pixels
	 */
	public int getTileWidth() {
		return tileWidth;
	}

	/**
	 * Set tile width in pixels.
	 * <p/>
	 * Raster layers often use fixed tile sizes which need to be combined to get the full picture. This allows you to
	 * get the width of these tiles (when applicable).
	 * 
	 * @param tileWidth
	 *            tile width
	 */
	public void setTileWidth(int tileWidth) {
		this.tileWidth = tileWidth;
	}

	/**
	 * Get tile height in pixels.
	 * <p/>
	 * Raster layers often use fixed tile sizes which need to be combined to get the full picture. This allows you to
	 * get the height of these tiles (when applicable).
	 * 
	 * @return tile height
	 */
	public int getTileHeight() {
		return tileHeight;
	}

	/**
	 * Set tile height in pixels.
	 * <p/>
	 * Raster layers often use fixed tile sizes which need to be combined to get the full picture. This allows you to
	 * get the height of these tiles (when applicable).
	 * 
	 * @param tileHeight
	 *            tile height
	 */
	public void setTileHeight(int tileHeight) {
		this.tileHeight = tileHeight;
	}

	/**
	 * Get the list of supported resolutions for the layer. Each resolution is specified in map units per pixel.
	 * 
	 * @return list of supported resolutions
	 * @deprecated use {@link #getZoomLevels()}
	 */
	@Deprecated
	public List<Double> getResolutions() {
		List<Double> resolutions = new ArrayList<Double>();
		for (ScaleInfo scale : getZoomLevels()) {
			resolutions.add(1. / scale.getPixelPerUnit());
		}
		return resolutions;
	}

	/**
	 * Set the list of supported resolutions. Each resolution is specified in map units per pixel.
	 * 
	 * @param resolutions
	 *            resolutions
	 * @deprecated use {@link #setZoomLevels()}
	 */
	@Deprecated
	public void setResolutions(List<Double> resolutions) {
		getZoomLevels().clear();
		for (Double resolution : resolutions) {
			getZoomLevels().add(new ScaleInfo(1. / resolution));
		}
	}

	/**
	 * Returns the list of zoom levels for the layer.
	 * 
	 * @return the list of zoom levels
	 * @since 1.7.0
	 */
	public List<ScaleInfo> getZoomLevels() {
		return zoomLevels;
	}

	/**
	 * Sets the list of zoom levels for this layer.
	 * 
	 * @param zoomLevels
	 *            the list of zoom levels
	 * @since 1.7.0
	 */
	public void setZoomLevels(List<ScaleInfo> zoomLevels) {
		this.zoomLevels = zoomLevels;
	}

}
