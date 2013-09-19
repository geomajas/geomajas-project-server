/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2013 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the Apache
 * License, Version 2.0. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */
package org.geomajas.graphics.client.object.anchor;

import org.geomajas.graphics.client.object.role.RoleType;

/**
 * Implemented by anchored graphics objects. An anchored object has an anchor position to which it is connected. This
 * may be visualized by a connector line and point.
 * 
 * @author Jan De Moerloose
 * 
 */
public interface MaskHasLines {

	RoleType<MaskHasLines> TYPE = new RoleType<MaskHasLines>("MaskHasLines");
}
