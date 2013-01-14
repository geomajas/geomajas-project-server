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

import java.util.Collection;
import java.util.Set;

import org.geomajas.geometry.Crs;
import org.opengis.metadata.extent.Extent;
import org.opengis.referencing.ReferenceIdentifier;
import org.opengis.referencing.crs.TemporalCRS;
import org.opengis.referencing.cs.TimeCS;
import org.opengis.referencing.datum.TemporalDatum;
import org.opengis.util.GenericName;
import org.opengis.util.InternationalString;

/**
 * Implementation of a {@link TemporalCRS} containing an id and delegating to another implementation.
 *
 * @author Joachim Van der Auwera
 */
class TemporalCrsImpl implements Crs, TemporalCRS {

	private String id;
	private TemporalCRS base;

	public TemporalCrsImpl(String id, TemporalCRS base) {
		this.id = id;
		this.base = base;
	}

	public String getId() {
		return id;
	}

	public TimeCS getCoordinateSystem() {
		return base.getCoordinateSystem();
	}

	public TemporalDatum getDatum() {
		return base.getDatum();
	}

	public Extent getDomainOfValidity() {
		return base.getDomainOfValidity();
	}

	public InternationalString getScope() {
		return base.getScope();
	}

	public ReferenceIdentifier getName() {
		return base.getName();
	}

	public Collection<GenericName> getAlias() {
		return base.getAlias();
	}

	public Set<ReferenceIdentifier> getIdentifiers() {
		return base.getIdentifiers();
	}

	public InternationalString getRemarks() {
		return base.getRemarks();
	}

	public String toWKT() throws UnsupportedOperationException {
		return base.toWKT();
	}
}
