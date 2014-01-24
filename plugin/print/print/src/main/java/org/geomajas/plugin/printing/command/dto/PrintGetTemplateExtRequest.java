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

package org.geomajas.plugin.printing.command.dto;

import org.geomajas.annotation.Api;

/**
 * Request object for {@link org.geomajas.plugin.printing.command.print.PrintGetTemplateExtCommand}.
 * Extension of {@link org.geomajas.plugin.printing.command.dto.PrintGetTemplateRequest} class.
 *
 * @author An Buyle
 * @since 2.4.0
 */
@Api(allMethods = true)
public class PrintGetTemplateExtRequest extends PrintGetTemplateRequest {

	private static final long serialVersionUID = 250L;

	/**
	 * Command name constant for request object.
	 *
	 * @since 2.4.0
	 */
	public static final String COMMAND = "command.print.GetTemplateExt";


	/**
	 * Default output format.
	 *
	 * @since 2.4.0
	 */	
	public static final String DEFAULT_OUTPUT_FORMAT = "pdf";

	private String outputFormat;
	/** No-arguments constructor for GWT. */
	public PrintGetTemplateExtRequest() {
		super();
	}

	
	/** Constructor that copies the settings of a 
	 * {@link org.geomajas.plugin.printing.command.dto.PrintGetTemplateRequest} instance.
	 *  
	 * @param request
	 */
	public PrintGetTemplateExtRequest(PrintGetTemplateRequest request) {
		setOutputFormat(DEFAULT_OUTPUT_FORMAT);
		setTemplate(request.getTemplate());
		setPageSize(request.getPageSize());
	}


	/**
	 * Get output format/extension for the generated document.
	 *
	 * @return get the output format/extension for the generated document. E.g. "pdf".
	 */
	public String getOutputFormat() {
		return outputFormat;
	}

	/**
	 * Set the output format/extension for the generated document.
	 * 
	 * @param pageSize page size as ISO name (eg "A4")
	 */
	public void setOutputFormat(String outputFormat) {
		this.outputFormat = outputFormat;
	}
}
