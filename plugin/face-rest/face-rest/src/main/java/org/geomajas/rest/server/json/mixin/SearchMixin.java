/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2014 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */
package org.geomajas.rest.server.json.mixin;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.geomajas.global.ExceptionDto;
import org.geomajas.layer.feature.SearchCriterion;

import java.util.List;

/**
 * Mixin annotations class for {@link org.geomajas.command.dto.SearchAttributesRequest}.
 *
 * @author Dosi Bingov
 */
public abstract class SearchMixin {

	@JsonIgnore
	abstract String getAttributeName();

}
