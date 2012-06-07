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
package org.geomajas.layer.wms.command.dto;

import java.util.Map;

import org.geomajas.command.LayerIdsCommandRequest;
import org.geomajas.geometry.Bbox;
import org.geomajas.geometry.Coordinate;
import org.geomajas.annotation.Api;

/**
 * Request for {@link org.geomajas.layer.wms.command.wms.SearchByPointCommand} that allows searching features
 * on raster layers that implement {@link org.geomajas.layer.wms.LayerFeatureInfoSupport}.
 *
 * @author Oliver May
 * @since 1.8.0
 */
@Api(allMethods = true)
public class SearchByPointRequest extends LayerIdsCommandRequest {

	private static final long serialVersionUID = 180L;

	/**
	 * Command name for this request.
	 */
	public static final String COMMAND = "command.wms.SearchByPoint";

	// -------------------------------------------------------------------------
	// Command statics:
	// -------------------------------------------------------------------------

	/**
	 * Search type in which the layers are searched from the beginning to the end of the array until a result is found.
	 * In other words when a layer returns a successful result, the command will look no further.
	 */
	public static final int SEARCH_FIRST_LAYER = 1;

	/** Search type in which all layers are searched for a result, and more than one result may be returned. */
	public static final int SEARCH_ALL_LAYERS = 2;

	// -------------------------------------------------------------------------
	// Command fields:
	// -------------------------------------------------------------------------

	/** The scale of the map view */
	private double scale;

	/** The bounding box of the layer in map coordinates */
	private Bbox bbox;

	/** The location to search features in given CRS. */
	private Coordinate location;

	/** The type of search. Can be either {@link #SEARCH_FIRST_LAYER} or {@link #SEARCH_ALL_LAYERS}. */
	private int searchType = SEARCH_FIRST_LAYER;

	/** The coordinate reference system in which to search by location. */
	private String crs;

	/** Search accuracy in pixels. */
	private int pixelTolerance = 1;
	
	/** Map that contains the client to server layer mapping.*/
	private Map<String, String> layerMapping;
	
	// -------------------------------------------------------------------------
	// Command fields:
	// -------------------------------------------------------------------------

	/**
	 * Get the scale of the request.
	 *
	 * @return the scale
	 */
	public double getScale() {
		return scale;
	}

	/**
	 * Set the scale of the request.
	 *
	 * @param scale the scale to set
	 */
	public void setScale(double scale) {
		this.scale = scale;
	}

	/**
	 * Get the bounding box.
	 *
	 * @param bbox the bbox to set
	 */
	public void setBbox(Bbox bbox) {
		this.bbox = bbox;
	}

	/**
	 * Set the bounding box.
	 *
	 * @return the bbox
	 */
	public Bbox getMapBounds() {
		return bbox;
	}

	/**
	 * Set search location.
	 *
	 * @param location the location to set
	 */
	public void setLocation(Coordinate location) {
		this.location = location;
	}

	/**
	 * Get the search location.
	 *
	 * @return the location
	 */
	public Coordinate getLocation() {
		return location;
	}

	/**
	 * Set the search type.
	 *
	 * @param searchType the searchType to set
	 */
	public void setSearchType(int searchType) {
		this.searchType = searchType;
	}

	/**
	 * Get the search type.
	 *
	 * @return the searchType
	 */
	public int getSearchType() {
		return searchType;
	}

	/**
	 * Get the coordinate reference space of the search location and bounds.
	 *
	 * @param crs the crs to set
	 */
	public void setCrs(String crs) {
		this.crs = crs;
	}

	/**
	 * Set the coordinate reference space of the search location and bounds.
	 *
	 * @return the crs
	 */
	public String getCrs() {
		return crs;
	}

	/**
	 * Set the search pixelTolerance in pixels.
	 *
	 * @param pixelTolerance the pixelTolerance to set
	 */
	public void setPixelTolerance(int pixelTolerance) {
		this.pixelTolerance = pixelTolerance;
	}

	/**
	 * Get the search pixelTolerance int pixels.
	 *
	 * @return the pixelTolerance
	 */
	public int getPixelTolerance() {
		return pixelTolerance;
	}

	@Override
	public String toString() {
		return "SearchByPointRequest{" +
				"scale=" + scale +
				", bbox=" + bbox +
				", location=" + location +
				", searchType=" + searchType +
				", crs='" + crs + '\'' +
				", pixelTolerance=" + pixelTolerance +
				'}';
	}

	/**
	 * Set the server layer mapping. This contains keys (client Layer id's) and values (server layer id's).
	 * 
	 * This deprecates {@link LayerIdsCommandRequest}.setLayerIds()
	 * 
	 * @param layerMapping the serverLayerMapping to set
	 * @since 1.10.0
	 */
	public void setLayerMapping(Map<String, String> layerMapping) {
		this.layerMapping = layerMapping;
	}

	/**
	 * Get the server layer mapping. This contains keys (client Layer id's) and values (server layer id's).
	 *
	 * This deprecates {@link LayerIdsCommandRequest}.getLayerIds()
	 * 
	 * @return the layerMapping
	 * @since 1.10.0
	 */
	public Map<String, String> getLayerMapping() {
		return layerMapping;
	}
	
	/**
	 * @deprecated Use getlayerMapping()
	 * @since 1.10.0
	 */
	@Deprecated
	public String[] getLayerIds() {
		return super.getLayerIds();
	}

	/**
	 * @deprecated Use setlayerMapping()
	 * @since 1.10.0
	 */
	@Deprecated
	public void setLayerIds(String[] layerIds) {
		super.setLayerIds(layerIds);
	}
	
}
