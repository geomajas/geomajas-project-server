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

package org.geomajas.puregwt.client.gfx;

import org.geomajas.gwt.client.util.Log;
import org.vaadin.gwtgraphics.client.Group;
import org.vaadin.gwtgraphics.client.VectorObject;

/**
 * Implementation of the {@link VectorContainer} interface.
 * 
 * @author Pieter De Graef
 */
public class VectorGroup extends Group implements VectorContainer {

	@Override
	public VectorObject add(VectorObject vo) {
		if (vo instanceof Group) {
			if (hasScale() || hasTranslation()) {
				Log.logWarn("WARNING: nested groups with scaling/translation become invisible in IE !");
			}
		}
		return super.add(vo);
	}

	@Override
	public VectorObject insert(VectorObject vo, int beforeIndex) {
		if (vo instanceof Group) {
			if (hasScale() || hasTranslation()) {
				Log.logWarn("WARNING: nested groups with scaling/translation become invisible in IE !");
			}
		}
		return super.insert(vo, beforeIndex);
	}

}