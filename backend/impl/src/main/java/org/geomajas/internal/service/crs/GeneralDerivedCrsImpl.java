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
import org.opengis.metadata.extent.Extent;
import org.opengis.referencing.ReferenceIdentifier;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.crs.GeneralDerivedCRS;
import org.opengis.referencing.cs.CoordinateSystem;
import org.opengis.referencing.datum.Datum;
import org.opengis.referencing.operation.Conversion;
import org.opengis.util.GenericName;
import org.opengis.util.InternationalString;

import java.util.Collection;
import java.util.Set;

/**
 * Implementation of a {@link GeneralDerivedCRS} containing an id and delegating to another implementation.
 *
 * @author Joachim Van der Auwera
 */
class GeneralDerivedCrsImpl implements Crs, GeneralDerivedCRS {

	private String id;
	private GeneralDerivedCRS base;

	public GeneralDerivedCrsImpl(String id, GeneralDerivedCRS base) {
		this.id = id;
		this.base = base;
	}

	public String getId() {
		return id;
	}

	public CoordinateReferenceSystem getBaseCRS() {
		return base.getBaseCRS();
	}

	public Conversion getConversionFromBase() {
		return base.getConversionFromBase();
	}

	public CoordinateSystem getCoordinateSystem() {
		return base.getCoordinateSystem();
	}

	public Datum getDatum() {
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
