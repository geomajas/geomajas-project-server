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
package org.geomajas.plugin.rasterizing.command.dto;

import java.util.ArrayList;
import java.util.List;

import org.geomajas.annotation.Api;
import org.geomajas.configuration.client.ClientLayerInfo;
import org.geomajas.configuration.client.ClientWidgetInfo;
import org.geomajas.geometry.Bbox;

/**
 * Extension of client map to carry extra rendering information.
 * 
 * @author Jan De Moerloose
 * @since 1.0.0
 */
@Api(allMethods = true)
public class MapRasterizingInfo implements ClientWidgetInfo, RasterizingConstants {

	private static final long serialVersionUID = 100L;

	private Bbox bounds;

	private double scale;
	
	private int dpi;

	// default to true (users should provide a background if necessary)
	private boolean transparent = true;

	private List<ClientLayerInfo> extraLayers = new ArrayList<ClientLayerInfo>();

	private LegendRasterizingInfo legendRasterizingInfo;
	
	/**
	 * Returns true if the underlying medium is transparent.
	 * 
	 * @return true if transparent, false otherwise
	 */
	public boolean isTransparent() {
		return transparent;
	}

	/**
	 * Sets whether the rendering should be done on a transparent medium.
	 * 
	 * @param transparent
	 *            true if rendering should be done on transparent medium
	 */
	public void setTransparent(boolean transparent) {
		this.transparent = transparent;
	}

	/**
	 * Returns the world bounds of the map.
	 * 
	 * @return bbox of world coordinates in the map's crs
	 */
	public Bbox getBounds() {
		return bounds;
	}

	/**
	 * Set the bounds of this map.
	 * 
	 * @param bounds
	 *            bbox of world coordinates in the map's crs
	 */
	public void setBounds(Bbox bounds) {
		this.bounds = bounds;
	}

	/**
	 * Returns the scale (pixel/map unit) of this map.
	 * 
	 * @return scale value
	 */
	public double getScale() {
		return scale;
	}

	/**
	 * Sets the scale (pixel/map unit) of this map.
	 * 
	 * @param scale
	 *            the new scale
	 */
	public void setScale(double scale) {
		this.scale = scale;
	}
	
	/**
	 * Get the DPI (Dots per inch) used for rendering the image. If not set or 0, the renderer's default DPI is used.
	 * @return the DPI
	 * @since 1.1.0
	 */
	public int getDpi() {
		return dpi;
	}
	
	/**
	 * Set the DPI (Dots per inch) used for rendering the image.
	 * @param dpi the DPI
	 * @since 1.1.0
	 */
	public void setDpi(int dpi) {
		this.dpi = dpi;
	}

	/**
	 * Returns the extra layers to be added to the rasterized map. Extra layers are added after configuration, e.g.
	 * world paintables, copyright images. This method returns a live copy.
	 * 
	 * @return list of extra layers
	 */
	public List<ClientLayerInfo> getExtraLayers() {
		return extraLayers;
	}

	/**
	 * Sets the list of extra layers to be added to the rasterized map. Extra layers are added after configuration, e.g.
	 * world paintables, copyright images.
	 * 
	 * @param extraLayers
	 *            new list of extra layers
	 */
	public void setExtraLayers(List<ClientLayerInfo> extraLayers) {
		this.extraLayers = extraLayers;
	}

	/**
	 * Returns the legend metadata (title, font, etc...).
	 * 
	 * @return the legend metadata
	 */
	public LegendRasterizingInfo getLegendRasterizingInfo() {
		return legendRasterizingInfo;
	}

	/**
	 * Sets the legend metadata (title, font, etc...).
	 * 
	 * @param legendRasterizingInfo the legend metadata
	 */
	public void setLegendRasterizingInfo(LegendRasterizingInfo legendRasterizingInfo) {
		this.legendRasterizingInfo = legendRasterizingInfo;
	}

	@Override
	public String toString() {
		return "MapRasterizingInfo{" +
				"bounds=" + bounds +
				", scale=" + scale +
				", dpi=" + dpi +
				", transparent=" + transparent +
				", extraLayers=" + extraLayers +
				", legendRasterizingInfo=" + legendRasterizingInfo +
				'}';
	}
}
