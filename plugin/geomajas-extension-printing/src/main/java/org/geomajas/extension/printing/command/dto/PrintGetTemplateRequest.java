/*
 * This file is part of Geomajas, a component framework for building
 * rich Internet applications (RIA) with sophisticated capabilities for the
 * display, analysis and management of geographic information.
 * It is a building block that allows developers to add maps
 * and other geographic data capabilities to their web applications.
 *
 * Copyright 2008-2010 Geosparc, http://www.geosparc.com, Belgium
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.geomajas.extension.printing.command.dto;

import org.geomajas.command.CommandRequest;

/**
 * ...
 *
 * @author Joachim Van der Auwera
 */
public class PrintGetTemplateRequest implements CommandRequest {

	private static final long serialVersionUID = 151L;

	public static final int DOWNLOAD_METHOD_BROWSER = 0;

	public static final int DOWNLOAD_METHOD_SAVE = 1;

	private DtoPrintTemplate template;

	private int downloadMethod = DOWNLOAD_METHOD_SAVE;

	private String fileName;

	private String pageSize;

	public PrintGetTemplateRequest() {
	}

	/**
	 * Get print template to be used.
	 *
	 * @return print template
	 */
	public DtoPrintTemplate getTemplate() {
		return template;
	}

	public void setTemplate(DtoPrintTemplate template) {
		this.template = template;
	}

	/**
	 * Get method to use for downloading the generated document.
	 *
	 * @return
	 */
	public int getDownloadMethod() {
		return downloadMethod;
	}

	public void setDownloadMethod(int downloadMethod) {
		this.downloadMethod = downloadMethod;
	}

	/**
	 * Get desired filename for the generated document.
	 *
	 * @return
	 */
	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	/**
	 * Get size for the generated document (ISO).
	 *
	 * @return
	 */
	public String getPageSize() {
		return pageSize;
	}

	public void setPageSize(String pageSize) {
		this.pageSize = pageSize;
	}
}
