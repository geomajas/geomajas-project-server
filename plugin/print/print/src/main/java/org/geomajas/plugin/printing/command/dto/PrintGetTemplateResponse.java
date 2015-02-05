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
package org.geomajas.plugin.printing.command.dto;

import org.geomajas.command.CommandResponse;
import org.geomajas.annotation.Api;

/**
 * Response object for PrintGetTemplateCommand.
 *
 * @author Jan De Moerloose
 * @since 2.0.0
 */
@Api(allMethods = true)
public class PrintGetTemplateResponse extends CommandResponse {

	private static final long serialVersionUID = 151L;

	private String documentId;

	/** No-arguments constructor for GWT. */
	public PrintGetTemplateResponse() {
	}

	/**
	 * Construct response for document.
	 *
	 * @param documentId document id
	 */
	public PrintGetTemplateResponse(String documentId) {
		this.documentId = documentId;
	}

	/**
	 * Get document id.
	 *
	 * @return document id.
	 */
	public String getDocumentId() {
		return documentId;
	}

	/**
	 * Set document id.
	 *
	 * @param documentId document id
	 */
	public void setDocumentId(String documentId) {
		this.documentId = documentId;
	}
}
