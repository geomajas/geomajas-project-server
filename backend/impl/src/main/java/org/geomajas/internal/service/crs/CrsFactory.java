/*
 * This file is part of Geomajas, a component framework for building
 * rich Internet applications (RIA) with sophisticated capabilities for the
 * display, analysis and management of geographic information.
 * It is a building block that allows developers to add maps
 * and other geographic data capabilities to their web applications.
 *
 * Copyright 2008-2010 Geosparc, http://www.geosparc.com, Belgium
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
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
