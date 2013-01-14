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

package org.geomajas.widget.layer.configuration.client;

import org.geomajas.annotation.Api;
import org.geomajas.configuration.client.ClientWidgetInfo;

/**
 * Configuration properties used by LayerTreeWithLegend and LayerInfo.
 * 
 * @author Kristof Heirwegh
 * @author Oliver May
 * 
 * @since 1.0.0
 */
@Api
public class ClientExtraLayerInfo implements ClientWidgetInfo {

	private static final long serialVersionUID = 100L;

	public static final String IDENTIFIER = "ExtraLayerInfo";

	private String smallLayerIconUrl;

	private String largeLayerIconUrl;

	private String legendImageUrl;

	private String legendUrl;

	private String legendUrlTitle;

	private String source;

	private String date;

	/**
	 * Get the source, or owner of the data in this layer.
	 * 
	 * @return the source or owner.
	 */
	public String getSource() {
		return source;
	}

	/**
	 * Set the source, or owner of the data in this layer.
	 * 
	 * @param source the source or owner.
	 */
	public void setSource(String source) {
		this.source = source;
	}

	/**
	 * The date of the source, (eq. when the layer was last updated/refreshed/uploaded)
	 * 
	 * @return the date.
	 */
	public String getDate() {
		return date;
	}

	/**
	 * The date of the source, (eq. when the layer was last updated/refreshed/uploaded)
	 * 
	 * @param date the date.
	 */
	public void setDate(String date) {
		this.date = date;
	}

	/**
	 * The small layer icon link, used to display the layer in the layertree.
	 * 
	 * @return the layer icon.
	 */
	public String getSmallLayerIconUrl() {
		return smallLayerIconUrl;
	}

	/**
	 * The small layer icon link, used to display the layer in the layertree.
	 * 
	 * @param smallLayerIconUrl the layer icon.
	 */
	public void setSmallLayerIconUrl(String smallLayerIconUrl) {
		this.smallLayerIconUrl = smallLayerIconUrl;
	}

	/**
	 * The large layer icon link, used in the layerinfo widget.
	 * 
	 * @return the large layer icon link.
	 */
	public String getLargeLayerIconUrl() {
		return largeLayerIconUrl;
	}
	
	/**
	 * The large layer icon link, used to display in the layerinfo widget.
	 * 
	 * @param largeLayerIconUrl the large layer icon.
	 */
	public void setLargeLayerIconUrl(String largeLayerIconUrl) {
		this.largeLayerIconUrl = largeLayerIconUrl;
	}

	/**
	 * Legend image url, used to display in the layerinfo widget.
	 * 
	 * @return the legend image.
	 */
	public String getLegendImageUrl() {
		return legendImageUrl;
	}

	/**
	 * Legend image url, used to display in the layerinfo widget.
	 * 
	 * @param legendImageUrl the legend image.
	 */
	public void setLegendImageUrl(String legendImageUrl) {
		this.legendImageUrl = legendImageUrl;
	}

	/**
	 * Legend url, used to link to from the layerinfo widget.
	 * 
	 * @param legendUrl the legendUrl to set
	 */
	public void setLegendUrl(String legendUrl) {
		this.legendUrl = legendUrl;
	}

	/**
	 * Legend url, used to link to from the layerinfo widget.
	 * 
	 * @return the legendUrl
	 */
	public String getLegendUrl() {
		return legendUrl;
	}

	/**
	 * Legend url title, used to lilnk to from the layerinfo widget.
	 * 
	 * @param legendUrlTitle the legendUrlTitle to set
	 */
	public void setLegendUrlTitle(String legendUrlTitle) {
		this.legendUrlTitle = legendUrlTitle;
	}

	/**
	 * Legend url title, used to lilnk to from the layerinfo widget.
	 * 
	 * @return the legendUrlTitle
	 */
	public String getLegendUrlTitle() {
		return legendUrlTitle;
	}
}
