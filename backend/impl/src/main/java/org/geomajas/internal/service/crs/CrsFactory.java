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

package org.geomajas.internal.service.crs;

import org.geomajas.geometry.Crs;
import org.opengis.referencing.crs.CompoundCRS;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.crs.GeneralDerivedCRS;
import org.opengis.referencing.crs.GeodeticCRS;
import org.opengis.referencing.crs.GeographicCRS;
import org.opengis.referencing.crs.ProjectedCRS;
import org.opengis.referencing.crs.SingleCRS;
import org.opengis.referencing.crs.TemporalCRS;

/**
 * Factory for building a {@link Crs} from a {@link CoordinateReferenceSystem}.
 *
 * @author Joachim Van der Auwera
 */
public final class CrsFactory {

	private CrsFactory() {
		// avoid instantiation
	}

	public static Crs getCrs(String id, CoordinateReferenceSystem base) {
		if (base instanceof CompoundCRS) {
			return new CompoundCrsImpl(id, (CompoundCRS) base);
		}
		if (base instanceof GeographicCRS) {
			return new GeographicCrsImpl(id, (GeographicCRS) base);
		}
		if (base instanceof GeodeticCRS) {
			return new GeodeticCrsImpl(id, (GeodeticCRS) base);
		}
		if (base instanceof ProjectedCRS) {
			return new ProjectedCrsImpl(id, (ProjectedCRS) base);
		}
		if (base instanceof GeneralDerivedCRS) {
			return new GeneralDerivedCrsImpl(id, (GeneralDerivedCRS) base);
		}
		if (base instanceof TemporalCRS) {
			return new TemporalCrsImpl(id, (TemporalCRS) base);
		}
		if (base instanceof SingleCRS) {
			return new SingleCrsImpl(id, (SingleCRS) base);
		}
		return new CrsImpl(id, base);
	}

}
