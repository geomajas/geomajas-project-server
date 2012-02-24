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

package org.geomajas.layer.tms;

import org.springframework.stereotype.Component;

/**
 * Helper service for creating tiles for the TMS layer.
 * 
 * @author Pieter De Graef
 */
@Component
public class TileService {

	public int getTileLevel(TileServiceState state, double resolution) {
		// Check corner cases (too big or too small):
		if (resolution >= state.getResolutions().get(0)) {
			return 0;
		} else if (resolution <= state.getResolutions().get(state.getResolutions().size() - 1)) {
			return state.getResolutions().size() - 1;
		}

		// Somewhere in between minimum and maximum resolution:
		for (int i = 0; i < state.getResolutions().size() - 1; i++) {
			double upper = state.getResolutions().get(i);
			double lower = state.getResolutions().get(i + 1);

			if (resolution <= upper && resolution >= lower) {
				if ((upper - resolution) > 2 * (resolution - lower)) {
					return i + 1;
				} else {
					return i;
				}
			}
		}

		// Should never occur:
		throw new IllegalStateException("Weird resolution encountered...");
	}

	public double getTileWidth(TileServiceState state, int tileLevel) {
		return state.getTileWidth() * state.getResolutions().get(tileLevel);
	}

	public double getTileHeight(TileServiceState state, int tileLevel) {
		return state.getTileHeight() * state.getResolutions().get(tileLevel);
	}
}