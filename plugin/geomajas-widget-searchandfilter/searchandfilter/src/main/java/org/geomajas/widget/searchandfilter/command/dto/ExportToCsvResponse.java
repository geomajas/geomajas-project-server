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
package org.geomajas.widget.searchandfilter.command.dto;

import org.geomajas.command.CommandResponse;

/**
 * Response object for {@link org.geomajas.widget.searchandfilter.command.searchandfilter.ExportToCsvCommand}.
 *
 * @author Kristof Heirwegh
 */
public class ExportToCsvResponse extends CommandResponse {

	private static final long serialVersionUID = 100L;

	private String documentId;

	public String getDocumentId() {
		return documentId;
	}

	public void setDocumentId(String documentId) {
		this.documentId = documentId;
	}

}
