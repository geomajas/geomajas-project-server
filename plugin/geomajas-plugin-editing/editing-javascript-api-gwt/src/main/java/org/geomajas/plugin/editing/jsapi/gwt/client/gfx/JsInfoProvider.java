/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2012 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */
package org.geomajas.plugin.editing.jsapi.gwt.client.gfx;

import org.geomajas.annotation.Api;
import org.geomajas.geometry.Coordinate;
import org.geomajas.geometry.Geometry;
import org.geomajas.plugin.editing.gwt.client.gfx.InfoProvider;
import org.timepedia.exporter.client.Export;
import org.timepedia.exporter.client.ExportClosure;
import org.timepedia.exporter.client.ExportPackage;
import org.timepedia.exporter.client.Exportable;

/**
 * JavaScript wrapper of {@link InfoProvider}.
 * 
 * @author Jan De Moerloose
 * @since 1.0.0
 * 
 */
@Export("InfoProvider")
@ExportPackage("org.geomajas.plugin.editing.gfx")
@Api(allMethods = true)
public class JsInfoProvider implements Exportable, InfoProvider {

	private TitleCallback titleCallback;

	private HtmlCallback htmlCallback;

	@Override
	public String getTitle() {
		return titleCallback.execute();
	}

	@Override
	public String getHtml(Geometry geometry, Coordinate dragPoint, Coordinate startA, Coordinate startB) {
		return htmlCallback.execute(geometry, dragPoint, startA, startB);
	}

	/**
	 * Set the callback closure to get the window title.
	 * 
	 * @param titleCallback the callback
	 */
	public void setTitleCallBack(TitleCallback titleCallback) {
		this.titleCallback = titleCallback;
	}

	/**
	 * Set the callback closure to get the HTML content.
	 * 
	 * @param titleCallback the callback
	 */
	public void setHtmlCallBack(HtmlCallback htmlCallback) {
		this.htmlCallback = htmlCallback;
	}

	/**
	 * 
	 * Closure that returns a string value with the title.
	 * 
	 * @since 1.0.0
	 * 
	 */
	@Export
	@ExportClosure
	@Api(allMethods = true)
	public interface TitleCallback extends Exportable {

		String execute();
	}

	/**
	 * 
	 * Closure that returns an HTML fragment with information about the geometry.
	 * 
	 * @since 1.0.0
	 * 
	 */
	@Export
	@ExportClosure
	@Api(allMethods = true)
	public interface HtmlCallback extends Exportable {

		String execute(Geometry geometry, Coordinate dragPoint, Coordinate startA, Coordinate startB);
	}

}
