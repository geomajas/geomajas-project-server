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

/**
 * Default implementation of the {@link WorldVectorContainer}.
 * 
 * @author Pieter De Graef
 */
public class WorldVectorGroup extends VectorGroup implements WorldVectorContainer {

	private boolean useFixedSize;

	/** {@inheritDoc} */
	public void setUseFixedSize(boolean useFixedSize) {
		this.useFixedSize = useFixedSize;
	}

	/** {@inheritDoc} */
	public boolean isUseFixedSize() {
		return useFixedSize;
	}
}