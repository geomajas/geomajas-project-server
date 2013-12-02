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

package org.geomajas.plugin.printing.command.dto;

import org.geomajas.annotation.Api;
import org.geomajas.command.CommandRequest;

/**
 * Request object for {@link org.geomajas.plugin.printing.command.print.PrintGetTemplateCommand}.
 *
 * @author Joachim Van der Auwera
 * @author Jan De Moerloose
 * @since 2.0.0
 */
@Api(allMethods = true)
public class PrintGetTemplateRequest implements CommandRequest {

	private static final long serialVersionUID = 151L;

	/**
	 * Command name constant for request object.
	 *
	 * @since 2.2.0
	 */
	public static final String COMMAND = "command.print.GetTemplate";

	private PrintTemplateInfo template;

	private String pageSize;

	/** No-arguments constructor for GWT. */
	public PrintGetTemplateRequest() {
	}

	/**
	 * Get print template to be used.
	 *
	 * @return print template
	 */
	public PrintTemplateInfo getTemplate() {
		return template;
	}

	/**
	 * Set print template to be used.
	 *
	 * @param template template
	 */
	public void setTemplate(PrintTemplateInfo template) {
		this.template = template;
	}

	/**
	 * Get size for the generated document (ISO).
	 *
	 * @return get page size as ISO name (eg "A4")
	 */
	public String getPageSize() {
		return pageSize;
	}

	/**
	 * Set the size of the generated document.
	 * 
	 * @param pageSize page size as ISO name (eg "A4")
	 */
	public void setPageSize(String pageSize) {
		this.pageSize = pageSize;
	}
}
