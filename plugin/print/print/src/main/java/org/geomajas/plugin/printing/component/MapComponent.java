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
package org.geomajas.plugin.printing.component;

import java.util.Map;

import org.geomajas.plugin.printing.component.dto.MapComponentInfo;

import com.vividsolutions.jts.geom.Coordinate;

/**
 * Component representing a map.
 * 
 * @author Jan De Moerloose
 *
 * @param <T> DTO object class
 */
public interface MapComponent<T extends MapComponentInfo> extends PrintComponent<T> {

	Coordinate getLocation();

	String getMapId();

	float getPpUnit();

	double getRasterResolution();

	void clearLayers();

	String getApplicationId();

	void setMapId(String mapId);

	void setApplicationId(String applicationId);

	void setLocation(Coordinate mapLocation);

	void setPpUnit(float mapPpUnit);

	void setRasterResolution(double mapRasterResolution);

	void setFilter(Map<String, String> filters);

}