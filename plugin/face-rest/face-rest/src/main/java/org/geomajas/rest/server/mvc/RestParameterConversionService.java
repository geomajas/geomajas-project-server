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
package org.geomajas.rest.server.mvc;

import org.springframework.core.convert.support.GenericConversionService;

/**
 * Custom conversion service for REST parameters.
 * 
 * @author Jan De Moerloose
 * 
 */
public class RestParameterConversionService extends GenericConversionService {

	public RestParameterConversionService() {
		this.addConverter(new StringToEnvelopeConverter());
		this.addConverter(new StringToStringArrayConverter());
	}
}
