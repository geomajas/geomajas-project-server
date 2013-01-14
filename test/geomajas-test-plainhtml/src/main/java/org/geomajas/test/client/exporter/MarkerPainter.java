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
