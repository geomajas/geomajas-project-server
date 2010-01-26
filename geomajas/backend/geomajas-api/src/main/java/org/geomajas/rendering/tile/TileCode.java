/*
 * This file is part of Geomajas, a component framework for building
 * rich Internet applications (RIA) with sophisticated capabilities for the
 * display, analysis and management of geographic information.
 * It is a building block that allows developers to add maps
 * and other geographic data capabilities to their web applications.
 *
 * Copyright 2008-2010 Geosparc, http://www.geosparc.com, Belgium
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.geomajas.rendering.tile;

import java.io.Serializable;

/**
 * A spatial code determining the location of a tile.
 *
 * @author Pieter De Graef
 */
public class TileCode implements Serializable {

	private static final long serialVersionUID = 151L;

	private int x;

	private int y;

	private int tileLevel;

	public TileCode() {
	}

	public TileCode(int tileLevel, int x, int y) {
		this.x = x;
		this.y = y;
		this.tileLevel = tileLevel;
	}

	public int getTileLevel() {
		return tileLevel;
	}

	public void setTileLevel(int tileLevel) {
		this.tileLevel = tileLevel;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public Object clone() {
		return new TileCode(tileLevel, x, y);
	}

	public boolean equals(Object obj) {
		if (obj == null || !(obj instanceof TileCode)) {
			return false;
		} else {
			TileCode other = (TileCode) obj;
			return tileLevel == other.getTileLevel() && x == other.getX() && y == other.getY();
		}
	}

	public int hashCode() {
		return toString().hashCode();
	}

	public String toString() {
		return tileLevel + "-" + x + "-" + y;
	}
}
