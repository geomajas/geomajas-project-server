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
package org.geomajas.test.client.exporter;

import org.geomajas.gwt.client.gfx.MapContext;
import org.geomajas.gwt.client.gfx.Paintable;
import org.geomajas.gwt.client.gfx.Painter;
import org.geomajas.gwt.client.spatial.Bbox;

/**
 * Custom painter for markers.
 * 
 * @author Jan De Moerloose
 * 
 */
public class MarkerPainter implements Painter {

	public void deleteShape(Paintable paintable, Object group, MapContext context) {
		Marker marker = (Marker) paintable;
		context.getVectorContext().deleteElement(group, marker.getId());
	}

	public String getPaintableClassName() {
		return Marker.class.getName();
	}

	/**
	 * Paints the image without rescaling width/height.
	 */
	public void paint(Paintable paintable, Object group, MapContext context) {
		Marker marker = (Marker) paintable;
		context.getVectorContext().drawImage(
				group,
				marker.getId(),
				marker.getImageSrc(),
				new Bbox(marker.getLocation().getX(), marker.getLocation().getY(), marker.getWidth(), marker
						.getHeight()), marker.getStyle());
	}
}
