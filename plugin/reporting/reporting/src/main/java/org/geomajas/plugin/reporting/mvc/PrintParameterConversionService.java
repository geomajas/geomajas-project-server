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
package org.geomajas.plugin.reporting.mvc;


import org.springframework.core.convert.support.GenericConversionService;

/**
 * Custom conversion service for printing controller parameters.
 * 
 * @author Jan De Moerloose
 */
public class PrintParameterConversionService extends GenericConversionService {

	public PrintParameterConversionService() {
		this.addConverter(new StringToEnvelopeConverter());
		this.addConverter(new StringToStringArrayConverter());
	}
}
