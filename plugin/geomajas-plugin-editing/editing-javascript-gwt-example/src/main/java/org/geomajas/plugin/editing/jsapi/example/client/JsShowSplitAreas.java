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

package org.geomajas.plugin.editing.jsapi.example.client;

import org.geomajas.plugin.editing.jsapi.client.split.JsGeometrySplitService;
import org.geomajas.plugin.editing.jsapi.gwt.client.JsGeometryEditor;
import org.timepedia.exporter.client.Export;
import org.timepedia.exporter.client.ExportPackage;
import org.timepedia.exporter.client.Exportable;

/**
 * JavaScript accessible class for activating and deactivating labels that show areas during splitting.
 * 
 * @author Pieter De Graef
 */
@Export("ShowSplitAreas")
@ExportPackage("org.geomajas.jsapi.alv")
public class JsShowSplitAreas implements Exportable {

	private final ShowSplitAreas delegate;

	// ------------------------------------------------------------------------
	// Public methods:
	// ------------------------------------------------------------------------

	/** Do not use. */
	public JsShowSplitAreas() {
		delegate = null;
	}

	public JsShowSplitAreas(JsGeometryEditor editor, JsGeometrySplitService splitService) {
		delegate = new ShowSplitAreas(editor.getMap().getMapWidget(), splitService.getDelegate());
	}

	// ------------------------------------------------------------------------
	// Public methods:
	// ------------------------------------------------------------------------

	public void activate() {
		delegate.activate();
	}

	public void deactivate() {
		delegate.deactivate();
	}

	public boolean isActivated() {
		return delegate.isActivated();
	}

	// ------------------------------------------------------------------------
	// Getters and setters:
	// ------------------------------------------------------------------------

	public int getLabelWidth() {
		return delegate.getLabelWidth();
	}

	public int getLabelHeight() {
		return delegate.getLabelHeight();
	}

	public String getLabelTxt() {
		return delegate.getLabelTxt();
	}

	public void setLabelWidth(int labelWidth) {
		delegate.setLabelWidth(labelWidth);
	}

	public void setLabelHeight(int labelHeight) {
		delegate.setLabelHeight(labelHeight);
	}

	public void setLabelTxt(String labelTxt) {
		delegate.setLabelTxt(labelTxt);
	}
}