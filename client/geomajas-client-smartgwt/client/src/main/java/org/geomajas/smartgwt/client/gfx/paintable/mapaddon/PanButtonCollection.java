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

package org.geomajas.smartgwt.client.gfx.paintable.mapaddon;

import org.geomajas.smartgwt.client.Geomajas;
import org.geomajas.smartgwt.client.gfx.PainterVisitor;
import org.geomajas.smartgwt.client.gfx.paintable.Image;
import org.geomajas.smartgwt.client.gfx.style.PictureStyle;
import org.geomajas.smartgwt.client.spatial.Bbox;
import org.geomajas.smartgwt.client.widget.MapWidget;

import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.VerticalAlignment;

/**
 * <p>
 * Collection of pan buttons that cover the four corners. This class uses 4 {@link PanButton} objects to create the
 * individual panning buttons.
 * </p>
 * 
 * @author Pieter De Graef
 */
public class PanButtonCollection extends MapAddon {

	private PanButton north;

	private PanButton east;

	private PanButton south;

	private PanButton west;

	private Image background;

	private MapWidget map;

	// -------------------------------------------------------------------------
	// Constructor:
	// -------------------------------------------------------------------------

	/**
	 * Create a panning collection for a certain map.
	 * 
	 * @param id
	 *            The unique identifier.
	 * @param map
	 *            The map onto which to pan.
	 */
	public PanButtonCollection(String id, MapWidget map) {
		super(id, 50, 50);
		this.map = map;

		north = new PanButton(getId() + "_panNorth", map, PanButton.PanButtonDirection.NORTH);
		north.setParent(this);
		east = new PanButton(getId() + "_panEast", map, PanButton.PanButtonDirection.EAST);
		east.setParent(this);
		south = new PanButton(getId() + "_panSouth", map, PanButton.PanButtonDirection.SOUTH);
		south.setParent(this);
		west = new PanButton(getId() + "_panWest", map, PanButton.PanButtonDirection.WEST);
		west.setParent(this);

		background = new Image(getId() + "_panImageBG");
		background.setHref(Geomajas.getIsomorphicDir() + "geomajas/mapaddon/panbg.png");
		background.setStyle(new PictureStyle(1));

		applyPosition();
	}

	// -------------------------------------------------------------------------
	// Constructor:
	// -------------------------------------------------------------------------

	/**
	 * On the first draw: call the onDraw methods for each panning button individually.
	 */
	public void onDraw() {
		north.onDraw();
		east.onDraw();
		south.onDraw();
		west.onDraw();
	}

	/**
	 * Does nothing.
	 */
	public void onRemove() {
	}

	/**
	 * Apply the correct positions on all panning buttons, and render them.
	 */
	public void accept(PainterVisitor visitor, Object group, Bbox bounds, boolean recursive) {
		// Apply position on all pan buttons:
		applyPosition();

		// Then render them:
		map.getVectorContext().drawGroup(group, this);
		map.getVectorContext().drawImage(this, background.getId(), background.getHref(), background.getBounds(),
				(PictureStyle) background.getStyle());
		north.accept(visitor, group, bounds, recursive);
		east.accept(visitor, group, bounds, recursive);
		south.accept(visitor, group, bounds, recursive);
		west.accept(visitor, group, bounds, recursive);
	}

	public void setMapSize(int mapWidth, int mapHeight) {
		super.setMapSize(mapWidth, mapHeight);
		north.setMapSize(mapWidth, mapHeight);
		east.setMapSize(mapWidth, mapHeight);
		south.setMapSize(mapWidth, mapHeight);
		west.setMapSize(mapWidth, mapHeight);
	}

	// -------------------------------------------------------------------------
	// Constructor:
	// -------------------------------------------------------------------------

	public void setAlignment(Alignment alignment) {
		super.setAlignment(alignment);
		applyPosition();
	}

	public void setVerticalAlignment(VerticalAlignment verticalAlignment) {
		super.setVerticalAlignment(verticalAlignment);
		applyPosition();
	}

	public void setHorizontalMargin(int horizontalMargin) {
		super.setHorizontalMargin(horizontalMargin);
		applyPosition();
	}

	public void setVerticalMargin(int verticalMargin) {
		super.setVerticalMargin(verticalMargin);
		applyPosition();
	}

	// -------------------------------------------------------------------------
	// Private methods:
	// -------------------------------------------------------------------------

	private void applyPosition() {
		background.setBounds(new Bbox(getHorizontalMargin(), getVerticalMargin(), getWidth(), getHeight()));

		north.setHorizontalMargin(getHorizontalMargin() + 16);
		north.setVerticalMargin(getVerticalMargin());

		east.setHorizontalMargin(getHorizontalMargin() + 32);
		east.setVerticalMargin(getVerticalMargin() + 16);

		south.setHorizontalMargin(getHorizontalMargin() + 16);
		south.setVerticalMargin(getVerticalMargin() + 32);

		west.setHorizontalMargin(getHorizontalMargin());
		west.setVerticalMargin(getVerticalMargin() + 16);
	}
}