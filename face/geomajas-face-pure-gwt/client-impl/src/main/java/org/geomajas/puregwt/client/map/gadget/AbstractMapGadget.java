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

package org.geomajas.puregwt.client.map.gadget;

import org.geomajas.puregwt.client.map.MapGadget;
import org.geomajas.puregwt.client.map.MapPresenter;

import com.google.gwt.layout.client.Layout.Alignment;

/**
 * Simple base implementation of a {@link MapGadget}. This can be used as a base for your gadgets.
 * 
 * @author Pieter De Graef
 */
public abstract class AbstractMapGadget implements MapGadget {

	protected MapPresenter mapPresenter;

	protected int horizontalMargin;

	protected int verticalMargin;

	protected Alignment horizontalAlignment = Alignment.BEGIN;

	protected Alignment verticalAlignment = Alignment.BEGIN;

	public void beforeDraw(MapPresenter mapPresenter) {
		this.mapPresenter = mapPresenter;
	}

	// ------------------------------------------------------------------------
	// Getters and setters:
	// ------------------------------------------------------------------------

	public int getHorizontalMargin() {
		return horizontalMargin;
	}

	public void setHorizontalMargin(int horizontalMargin) {
		this.horizontalMargin = horizontalMargin;
	}

	public int getVerticalMargin() {
		return verticalMargin;
	}

	public void setVerticalMargin(int verticalMargin) {
		this.verticalMargin = verticalMargin;
	}

	public Alignment getHorizontalAlignment() {
		return horizontalAlignment;
	}

	public void setHorizontalAlignment(Alignment horizontalAlignment) {
		this.horizontalAlignment = horizontalAlignment;
	}

	public Alignment getVerticalAlignment() {
		return verticalAlignment;
	}

	public void setVerticalAlignment(Alignment verticalAlignment) {
		this.verticalAlignment = verticalAlignment;
	}
}