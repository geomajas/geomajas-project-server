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
package org.geomajas.sld.client.presenter;

/**
 * Provides call-back to be called when the user has interacted with the Viewer so that
 * the model data has changed since the last copyToView(model) or resetAndFocus() on the Viewer.
 *
 * @author An Buyle
 *
 */

public interface ChangeHandler {
	void execute();
}
