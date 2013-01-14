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

import java.io.File;

/**
 * FileDownloadService.
 *
 * @author Kristof Heirwegh
 */
public interface FileDownloadService {

	boolean hasFile(String id);

	Item getFile(String id);

	void addFile(String id, File file, String description);

	Item removeFile(String id);

	/**
	 * An item to download (eg. file) from the FileDownloadService.
	 *
	 * @author Kristof Heirwegh
	 */
	public static class Item {

		private File file;
		private String description;

		public File getFile() {
			return file;
		}

		public void setFile(File file) {
			this.file = file;
		}

		public String getDescription() {
			return description;
		}

		public void setDescription(String description) {
			this.description = description;
		}

		public Item(File file, String description) {
			this.file = file;
			this.description = description;
		}
	}
}
