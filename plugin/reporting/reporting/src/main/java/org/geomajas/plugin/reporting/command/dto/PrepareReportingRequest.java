/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2016 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */

package org.geomajas.plugin.reporting.command.dto;

import org.geomajas.command.LayerIdCommandRequest;
import org.geomajas.configuration.client.ClientMapInfo;

/**
 * Request object for {@link org.geomajas.plugin.reporting.command.reporting.PrepareReportingCommand}.
 *
 * @author Jan De Moerloose
 * @author Joachim Van der Auwera
 * @since 1.0.0
 */
//@Api(allMethods = true) don't know about api, all this caching stuff causes problems cfr lazy loading etc
public class PrepareReportingRequest extends LayerIdCommandRequest {

	private static final long serialVersionUID = 100L;

	/** Command name. */
	public static final String COMMAND = "command.reporting.Prepare";

	private int imageWidth;
	private int imageHeight;
	private int legendWidth = -1;
	private int legendHeight = -1;
	private double minimumGeometrySize = 1e-7;
	private int margin;
	private ClientMapInfo clientMapInfo;
	private int dpi = 96;
	private String[] featureIds;
	private String filter;

	public int getImageWidth() {
		return imageWidth;
	}

	public void setImageWidth(int imageWidth) {
		this.imageWidth = imageWidth;
	}

	public int getImageHeight() {
		return imageHeight;
	}

	public void setImageHeight(int imageHeight) {
		this.imageHeight = imageHeight;
	}

	public int getLegendWidth() {
		return legendWidth;
	}

	public void setLegendWidth(int legendWidth) {
		this.legendWidth = legendWidth;
	}

	public int getLegendHeight() {
		return legendHeight;
	}

	public void setLegendHeight(int legendHeight) {
		this.legendHeight = legendHeight;
	}

	/**
	 * Minimum size in layer CRS for the geometry. This is used to assure that points and very small features have a
	 * real width and height and that they are not zoomed in too far.
	 * <p/>
	 * This is applied before the margins.
	 *
	 * @return minimum size for the geometry
	 */
	public double getMinimumGeometrySize() {
		return minimumGeometrySize;
	}

	/**
	 * Minimum size in layer CRS for the geometry. This is used to assure that points and very small features have a
	 * real width and height and that they are not zoomed in too far.
	 * <p/>
	 * This is applied before the margins.
	 *
	 * @param minimumGeometrySize minimum size for the geometry
	 */
	public void setMinimumGeometrySize(double minimumGeometrySize) {
		this.minimumGeometrySize = minimumGeometrySize;
	}

	/**
	 * Margin to add, in pixels.
	 *
	 * @return margin in pixels
	 */
	public int getMargin() {
		return margin;
	}

	/**
	 * Margin to add, in pixels.
	 *
	 * @param margin margin in pixels
	 */
	public void setMargin(int margin) {
		this.margin = margin;
	}

	/**
	 * {@link ClientMapInfo} describing the image contents.
	 *
	 * @return map info
	 */
	public ClientMapInfo getClientMapInfo() {
		return clientMapInfo;
	}

	/**
	 * {@link ClientMapInfo} describing the image contents.
	 *
	 * @param clientMapInfo map info
	 */
	public void setClientMapInfo(ClientMapInfo clientMapInfo) {
		this.clientMapInfo = clientMapInfo;
	}

	/**
	 * Get the image resolution in dots per inch.
	 *
	 * @return dpi
	 */
	public int getDpi() {
		return dpi;
	}

	/**
	 * Set the image resolution in dots per inch.
	 *
	 * @param dpi dpi
	 */
	public void setDpi(int dpi) {
		this.dpi = dpi;
	}

	/**
	 * Feature ids to include in data source.
	 * <p/>
	 * Features come from the specified layer.
	 * <p/>
	 * When no feature ids are specified (or null), then all features will be included.
	 *
	 * @return feature ids
	 */
	public String[] getFeatureIds() {
		return featureIds;
	}

	/**
	 * Feature ids to include in data source.
	 * <p/>
	 * Features come from the specified layer.
	 * <p/>
	 * When no feature ids are specified (or null), then all features will be included.
	 *
	 * @param featureIds feature ids
	 */
	public void setFeatureIds(String[] featureIds) {
		this.featureIds = featureIds;
	}

	/**
	 * Filter which should be applied on the layer to obtain the features.
	 * <p/>
	 * Only features matching the filter will be included in the report.
	 *
	 * @return layer filter to apply
	 */
	public String getFilter() {
		return filter;
	}

	/**
	 * Filter which should be applied on the layer to obtain the features.
	 * <p/>
	 * Only features matching the filter will be included in the report.
	 *
	 * @param filter layer filter
	 */
	public void setFilter(String filter) {
		this.filter = filter;
	}
}
