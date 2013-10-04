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

package org.geomajas.gwt2.example.base.client.resource;

import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.resources.client.ImageResource.ImageOptions;
import com.google.gwt.resources.client.ImageResource.RepeatStyle;
import com.google.gwt.user.cellview.client.DataGrid;
import com.google.gwt.user.cellview.client.DataGrid.Style;

/**
 * Overriding the default DataGrid style.
 * 
 * @author Pieter De Graef
 */
public interface DataGridResource extends DataGrid.Resources {

	@Source("image/header.png")
	@ImageOptions(repeatStyle = RepeatStyle.Horizontal)
	ImageResource header();

	/** The styles used in this widget. */
	@Source("DataGrid.css")
	Style dataGridStyle();
}