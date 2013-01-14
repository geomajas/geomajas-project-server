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
package org.geomajas.plugin.deskmanager.reporting.csv;

/**
 * TODO.
 * 
 * @author Jan De Moerloose
 *
 */
public interface CsvExport {

	/**
	 * write yourself to the given builder.
	 * 
	 * @param sb
	 */
	void toCsv(CsvBuilder cb);

	void addHeaderRow(CsvBuilder cb);
}
