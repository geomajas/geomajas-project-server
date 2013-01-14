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
package org.geomajas.plugin.geocoder.client;

import org.geomajas.annotation.Api;

/**
 * <p>
 * Class which helps to provide styling for the {@link GeocoderWidget}.
 * <p/>
 * Implemented as static class to allow overwriting values. Values are used during construction,
 * so (if needed) adjust them before you instantiate a {@link GeocoderWidget}
 *
 * @author Emiel Ackermann
 * @since 1.2.0
 */
@Api(allMethods = true)
public final class GeocoderLayout {

	// CHECKSTYLE VISIBILITY MODIFIER: OFF

	/** Set to true if the clear icon should be shown, set to false if it should not be shown. */
	public static boolean showClearIcon = true;

	// CHECKSTYLE VISIBILITY MODIFIER: ON

	private GeocoderLayout() {
		// do not allow instantiation.
	}
}
