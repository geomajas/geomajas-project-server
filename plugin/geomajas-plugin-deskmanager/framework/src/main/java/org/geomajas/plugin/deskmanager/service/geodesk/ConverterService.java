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
package org.geomajas.plugin.deskmanager.service.geodesk;

import org.geomajas.geometry.Coordinate;
import org.geomajas.geometry.Geometry;
import org.geomajas.widget.searchandfilter.search.dto.AndCriterion;
import org.geomajas.widget.searchandfilter.search.dto.AttributeCriterion;
import org.geomajas.widget.searchandfilter.search.dto.Criterion;
import org.geomajas.widget.searchandfilter.search.dto.GeometryCriterion;
import org.geomajas.widget.searchandfilter.search.dto.OrCriterion;

import com.thoughtworks.xstream.XStream;

/**
 * This can be used as @component, but needs to work standalone as well for the HibernateType as this is not
 * spring-injected.
 * 
 * @author Oliver May
 */
// @Component
public final class ConverterService {

	private static final XStream XS = new XStream();

	static {
		XS.alias("Criterion", Criterion.class);
		XS.alias("AndCriterion", AndCriterion.class);
		XS.alias("OrCriterion", OrCriterion.class);
		XS.alias("AttributeCriterion", AttributeCriterion.class);
		XS.alias("GeometryCriterion", GeometryCriterion.class);
		XS.alias("Geometry", Geometry.class);
		XS.alias("C", Coordinate.class);
		XS.useAttributeFor(Coordinate.class, "x");
		XS.useAttributeFor(Coordinate.class, "y");
	}

	public static Criterion toCriterion(String data) {
		if (data == null || "".equals(data)) {
			return null;
		} else {
			return (Criterion) XS.fromXML(data);
		}
	}

	public static String toXml(Criterion critter) {
		if (critter == null) {
			return null;
		} else {
			return XS.toXML(critter);
		}
	}
	
	private ConverterService() {}

}
