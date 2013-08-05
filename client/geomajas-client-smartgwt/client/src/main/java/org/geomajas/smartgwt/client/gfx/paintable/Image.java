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

package org.geomajas.smartgwt.client.gfx.paintable;

import org.geomajas.smartgwt.client.gfx.PainterVisitor;
import org.geomajas.smartgwt.client.gfx.style.PictureStyle;
import org.geomajas.smartgwt.client.spatial.Bbox;
import org.geomajas.layer.tile.RasterTile;

/**
 * <p>
 * Implementation of the <code>Paintable</code> interface for drawing images.
 * </p>
 * 
 * @author Pieter De Graef
 */
public class Image extends AbstractWorldPaintable {

	/**
	 * Location of the actual image.
	 */
	private String href;

	private PictureStyle style;

	// -------------------------------------------------------------------------
	// Constructors:
	// -------------------------------------------------------------------------

	public Image(String id) {
		super(id);
	}

	public Image(RasterTile raster) {
		super(raster.getId());
		href = raster.getUrl();
		original = new Bbox(raster.getBounds());
		style = new PictureStyle(1);
	}

	// -------------------------------------------------------------------------
	// Paintable implementation:
	// -------------------------------------------------------------------------

	/**
	 * Everything that can be drawn on the map, must be accessible by a PainterVisitor!
	 * 
	 * @param visitor
	 *            A PainterVisitor object. Comes from a MapWidget.
	 * @param bounds
	 *            Not used here.
	 * @param recursive
	 *            Not used here.
	 */
	public void accept(PainterVisitor visitor, Object group, Bbox bounds, boolean recursive) {
		visitor.visit(this, group);
	}

	// -------------------------------------------------------------------------
	// Getters and setters:
	// -------------------------------------------------------------------------

	public void setStyle(PictureStyle style) {
		this.style = style;
	}

	public PictureStyle getStyle() {
		return style;
	}

	public Bbox getBounds() {
		return (Bbox) getLocation();
	}

	public void setBounds(Bbox bounds) {
		setOriginalLocation(bounds);
	}

	public String getHref() {
		return href;
	}

	public void setHref(String href) {
		this.href = href;
	}
}