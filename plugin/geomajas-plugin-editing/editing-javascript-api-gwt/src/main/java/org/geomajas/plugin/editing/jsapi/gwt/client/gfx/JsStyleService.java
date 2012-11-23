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
import org.geomajas.plugin.editing.gwt.client.gfx.DefaultStyleService;
import org.geomajas.plugin.editing.gwt.client.gfx.StyleService;
import org.timepedia.exporter.client.Export;
import org.timepedia.exporter.client.ExportPackage;
import org.timepedia.exporter.client.Exportable;
import org.timepedia.exporter.client.NoExport;

/**
 * JavaScript wrapper of {@link StyleService}.
 * 
 * @author Jan De Moerloose
 * @since 1.0.0
 * 
 */
@Export("StyleService")
@ExportPackage("org.geomajas.plugin.editing.gfx")
@Api(allMethods = true)
public class JsStyleService implements Exportable {

	private StyleService delegate;

	/**
	 * Default constructor (needed for exporter?).
	 */
	public JsStyleService() {
		delegate = new DefaultStyleService();
	}

	/**
	 * Delegating constructor.
	 * 
	 * @param delegate
	 */
	@NoExport
	public JsStyleService(StyleService delegate) {
		this.delegate = delegate;
	}

	/**
	 * @see #isCloseRingWhileInserting()
	 * @param closeRingWhileInserting
	 */
	public void setCloseRingWhileInserting(boolean closeRingWhileInserting) {
		delegate.setCloseRingWhileInserting(closeRingWhileInserting);
	}

	/**
	 * Should a second drag line be drawn when creating a linear ring ?
	 * 
	 * @return true when it should be drawn, false otherwise
	 */
	public boolean isCloseRingWhileInserting() {
		return delegate.isCloseRingWhileInserting();
	}

	/**
	 * @see #isShowDragLabels()
	 * @param showDragLabels
	 */
	public void setShowDragLabels(boolean showDragLabels) {
		delegate.setShowDragLabels(showDragLabels);
	}

	/**
	 * Should labels (A, B) be shown on drag lines ?
	 * 
	 * @return true when they should be shown, false otherwise
	 */
	public boolean isShowDragLabels() {
		return delegate.isShowDragLabels();
	}

	/**
	 * @see #isShowInfo()
	 * @param showInfo
	 */
	public void setShowInfo(boolean showInfo) {
		delegate.setShowInfo(showInfo);
	}

	/**
	 * Should geometry information be shown while editing ?
	 * 
	 * @return true when it should be shown, false otherwise
	 */
	public boolean isShowInfo() {
		return delegate.isShowInfo();
	}

	/**
	 * Set the info provider for the geometry information window.
	 * 
	 * @param provider the provider
	 */
	public void setInfoProvider(JsInfoProvider provider) {
		delegate.setInfoProvider(provider);
	}

}
