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
package org.geomajas.command.dto;

import org.geomajas.command.LayerIdsCommandRequest;
import org.geomajas.geometry.Bbox;
import org.geomajas.geometry.Coordinate;
import org.geomajas.global.Api;


/**
 * Request for {@link org.geomajas.command.feature.SearchRasterLayersByLocationCommand} that allows searching features
 * on raster layers that implement {@link org.geomajas.layer.wms.LayerFeatureInfoSupport}.
 * 
 * @author Oliver May
 * @since 1.9.0
 */
@Api(allMethods = true)
public class SearchLayersByPointRequest extends LayerIdsCommandRequest {

	private static final long serialVersionUID = 190L;

	/**
	 * Command name for this request.
	 *
	 * @since 1.9.0
	 */
	public static final String COMMAND = "command.feature.SearchLayersByPoint";

	// -------------------------------------------------------------------------
	// Command statics:
	// -------------------------------------------------------------------------

	/**
	 * Search type in which the layers are searched from the beginning to the end of the array until a result is found.
	 * In other words when a layer returns a successful result, the command will look no further.
	 */
	public static final int SEARCH_FIRST_LAYER = 1;

	/**
	 * Search type in which all layers are searched for a result, and more than one result may be returned.
	 */
	public static final int SEARCH_ALL_LAYERS = 2;

	// -------------------------------------------------------------------------
	// Command fields:
	// -------------------------------------------------------------------------
	
	/**
	 * The scale of the map view
	 */
	private double scale;
	
	/**
	 * The bounding box of the layer in map coordinates
	 */
	private Bbox bbox;

	/**
	 * The geometric description of the location to search features at in map coordinates.
	 */
	private Coordinate location;

	/**
	 * The type of search. Can be either 1 (=SEARCH_FIRST_LAYER), or 2 (=SEARCH_ALL_LAYERS).
	 */
	private int searchType = SEARCH_FIRST_LAYER;

	/**
	 * The coordinate reference system in which to search by location.
	 */
	private String crs;

	/**
	 * The optional buffer that should be added around the location before executing the search.
	 */
	private double buffer = -1;
	
	// -------------------------------------------------------------------------
	// Command fields:
	// -------------------------------------------------------------------------
	
	/**
	 * Get the scale of the request.
	 * @return the scale
	 */
	public double getScale() {
		return scale;
	}
	
	/**
	 * Set the scale of the request.
	 * @param scale the scale to set
	 */
	public void setScale(double scale) {
		this.scale = scale;
	}

	/**
	 * Get the bounding box.
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
	public Bbox getBbox() {
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
	 * Set the search buffer.
	 * 
	 * @param buffer the buffer to set
	 */
	public void setBuffer(double buffer) {
		this.buffer = buffer;
	}

	/**
	 * Get the search buffer.
	 * 
	 * @return the buffer
	 */
	public double getBuffer() {
		return buffer;
	}

}
