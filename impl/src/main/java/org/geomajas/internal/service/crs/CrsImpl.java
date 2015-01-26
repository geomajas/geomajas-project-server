/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2015 Geosparc nv, http://www.geosparc.com/, Belgium.
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
import org.geomajas.global.CacheableObject;
import org.opengis.metadata.extent.Extent;
import org.opengis.referencing.ReferenceIdentifier;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.cs.CoordinateSystem;
import org.opengis.util.GenericName;
import org.opengis.util.InternationalString;

/**
 * Implementation of a {@link CoordinateReferenceSystem} containing an id and delegating to another implementation.
 *
 * @author Joachim Van der Auwera
 */
class CrsImpl implements Crs, CacheableObject {

	private String id;
	private CoordinateReferenceSystem base;

	public CrsImpl(String id, CoordinateReferenceSystem base) {
		this.id = id;
		this.base = base;
	}

	public String getId() {
		return id;
	}

	public CoordinateSystem getCoordinateSystem() {
		return base.getCoordinateSystem();
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

	/**
	 * {@link org.geomajas.service.GeoService} actually does cache based on this.
	 *
	 * @return CRS id
	 */
	public String getCacheId() {
		return getId();
	}
}
