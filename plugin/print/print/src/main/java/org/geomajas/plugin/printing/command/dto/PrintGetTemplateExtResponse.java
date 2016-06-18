/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2016 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */
package org.geomajas.plugin.printing.command.dto;

import org.geomajas.annotation.Api;

/**
 * Response object for PrintGetTemplateExtCommand.
 *
 * @author An Buyle
 * @since 2.4.0
 */
@Api(allMethods = true)
public class PrintGetTemplateExtResponse extends PrintGetTemplateResponse {

	private static final long serialVersionUID = 250L;

	/** No-arguments constructor for GWT. */
	public PrintGetTemplateExtResponse() {
		super();
	}
	/**
	 * Construct response for document.
	 *
	 * @param documentId document id
	 */
	public PrintGetTemplateExtResponse(String documentId) {
		super(documentId);
	}

}
