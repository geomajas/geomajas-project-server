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

package org.geomajas.layer.pipeline;

import org.geomajas.annotation.Api;

import com.vividsolutions.jts.geom.Envelope;

/**
 * Container for result of getBounds in {@link org.geomajas.layer.VectorLayerService}.
 *
 * @author Joachim Van der Auwera
 * @since 1.8.0
 */
@Api(allMethods = true)
public class GetBoundsContainer {

	private Envelope envelope;

	/**
	 * Get the bounds.
	 *
	 * @return bounds
	 */
	public Envelope getEnvelope() {
		return envelope;
	}

	/**
	 * Set the bounds.
	 *
	 * @param envelope bounds
	 */
	public void setEnvelope(Envelope envelope) {
		this.envelope = envelope;
	}
}
