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
package org.geomajas.puregwt.client.widget.control.zoom;

import org.geomajas.annotation.Api;

import com.google.gwt.resources.client.ClientBundle;

/**
 * Client resource bundle for the {@link ZoomControl}.
 * 
 * @author Pieter De Graef
 * @since 1.0.0
 */
@Api(allMethods = true)
public interface ZoomControlResource extends ClientBundle {

	/**
	 * Get the default CSS resource.
	 * 
	 * @return The CSS resource.
	 */
	@Source("geomajas-zoom-control.css")
	ZoomControlCssResource css();
}