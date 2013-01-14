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
package org.geomajas.puregwt.client.map.gadget;

import org.geomajas.configuration.client.ClientMapInfo;
import org.geomajas.puregwt.client.map.DefaultMapGadgetFactory;
import org.geomajas.puregwt.client.map.MapGadget;

/**
 * Default implementation of {@link DefaultMapGadgetFactory}.
 * 
 * @author Jan De Moerloose
 * 
 */
public class DefaultMapGadgetFactoryImpl implements DefaultMapGadgetFactory {

	public MapGadget createGadget(Type type, int top, int left, ClientMapInfo mapInfo) {
		switch (type) {
			case PANNING:
				return new PanningGadget();
			case SCALEBAR:
				return new ScalebarGadget(mapInfo);
			case SIMPLE_ZOOM:
				return new SimpleZoomGadget(top, left);
			case WATERMARK:
				return new WatermarkGadget();
			case ZOOM_STEP:
				return new ZoomStepGadget(top, left);
			case ZOOM_TO_RECTANGLE:
			default:
				return new ZoomToRectangleGadget(top, left);
		}
	}

}
