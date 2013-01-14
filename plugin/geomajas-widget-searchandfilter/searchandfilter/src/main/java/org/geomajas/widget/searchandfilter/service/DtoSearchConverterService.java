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
package org.geomajas.widget.searchandfilter.service;

import java.util.Map;

import org.geomajas.annotation.Api;
import org.geomajas.geometry.Crs;
import org.geomajas.global.GeomajasException;
import org.geomajas.layer.VectorLayer;
import org.geomajas.widget.searchandfilter.search.dto.Criterion;
import org.opengis.filter.Filter;

/**
 * Converts Dto Criterion to map of filters.
 * 
 * @author Kristof Heirwegh
 * @since 1.0.0
 */
@Api
public interface DtoSearchConverterService {

	Map<VectorLayer, Filter> dtoCriterionToFilters(Criterion criterion, Crs mapCrs) throws GeomajasException;

}
