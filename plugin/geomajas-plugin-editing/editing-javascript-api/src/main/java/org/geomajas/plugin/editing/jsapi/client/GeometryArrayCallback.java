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

package org.geomajas.plugin.editing.jsapi.client;

import org.geomajas.geometry.Geometry;
import org.timepedia.exporter.client.Export;
import org.timepedia.exporter.client.ExportClosure;
import org.timepedia.exporter.client.ExportPackage;
import org.timepedia.exporter.client.Exportable;

/**
 * Call-back object for asynchronous methods that deal with an array of geometries.
 * 
 * @author Pieter De Graef
 */
@Export
@ExportClosure
public interface GeometryArrayCallback extends Exportable {

	void execute(GeometryArrayHolder geometryArrayHolder);

	/**
	 * Wrapper around a geometry array, because the GWT exporter doesn't know how to handle arrays in a closure.
	 * 
	 * @author Pieter De Graef
	 */
	@Export
	@ExportPackage("org.geomajas.plugin.editing")
	public class GeometryArrayHolder implements Exportable {

		private Geometry[] geometries;

		/** Do not use. This is here only because the GWT exporter requires a no-argument constructor. */
		public GeometryArrayHolder() {
		}

		public GeometryArrayHolder(Geometry[] geometries) {
			this.geometries = geometries;
		}

		public int size() {
			if (geometries == null) {
				return 0;
			}
			return geometries.length;
		}

		public Geometry get(int index) {
			if (geometries == null) {
				return null;
			}
			return geometries[index];
		}
	}
}