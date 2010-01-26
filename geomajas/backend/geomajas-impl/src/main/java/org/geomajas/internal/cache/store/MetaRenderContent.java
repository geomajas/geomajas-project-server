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

package org.geomajas.internal.cache.store;

import org.geomajas.cache.CacheService;
import org.geomajas.cache.store.RenderContent;

import java.io.File;

/**
 * <p>
 * Meta information object for the <code>CacheContentStore</code>. It has the
 * same ID as a <code>RenderContent</code> object, to bind them exclusively. In
 * essence a <code>RenderContent</code> is this class' data containing
 * counterpart. Furthermore this object contains extra data that determines how
 * popular it is (created, nrRequested). This in turn can be used in caching
 * algorithms.
 * </p>
 *
 * @author Pieter De Graef
 */
public class MetaRenderContent {

	/**
	 * Statusses.
	 */
	public static enum Status {

		UNSET, LOCKED
	}

	/**
	 * Status for a specific cache entry. The <code>CacheContentStore</code>
	 * never allows the same entry to be touched by more then one thread at the
	 * same time.
	 */
	private Status status = Status.UNSET;

	/**
	 * Unique ID. This id is taken from a <code>RenderContent</code> object.
	 */
	private String id;

	/**
	 * Time of creation.
	 */
	private long created;

	/**
	 * Number of times this information has been requested.
	 */
	private int nrRequested;

	/**
	 * The tileLevel at which the tile lies.
	 */
	private int tileLevel;

	/**
	 * Tile X ordinate.
	 */
	private int x;

	/**
	 * Tile Y ordinate.
	 */
	private int y;

	// -------------------------------------------------------------------------
	// Constructors:
	// -------------------------------------------------------------------------

	/**
	 * Constructor that sets only the ID.
	 */
	public MetaRenderContent(String id) {
		this.id = id;
	}

	/**
	 * Initialize this meta information object directly with the ID of it data
	 * counterpart.
	 */
	public MetaRenderContent(RenderContent renderContent) {
		this.id = renderContent.getId();
		nrRequested = 0;
		created = System.currentTimeMillis();
		try {
			tileLevel = (Integer) renderContent.getParameters().get(CacheService.PARAM_TILELEVEL);
		} catch (Exception e) {
			tileLevel = 0;
		}
		try {
			x = (Integer) renderContent.getParameters().get(CacheService.PARAM_X);
		} catch (Exception e) {
			x = 0;
		}
		try {
			y = (Integer) renderContent.getParameters().get(CacheService.PARAM_Y);
		} catch (Exception e) {
			y = 0;
		}
	}

	// -------------------------------------------------------------------------
	// Class specific functions:
	// -------------------------------------------------------------------------

	/**
	 * Create a clone of this object.
	 */
	public Object clone() {
		MetaRenderContent clone = new MetaRenderContent(id);
		clone.setCreated(created);
		clone.setNrRequested(nrRequested);
		clone.setStatus(status);
		clone.setTileLevel(tileLevel);
		clone.setX(x);
		clone.setY(y);
		return clone;
	}

	/**
	 * The equals function compares ID's.
	 */
	public boolean equals(Object object) {
		if (object instanceof MetaRenderContent && id != null) {
			MetaRenderContent other = (MetaRenderContent) object;
			return (id.equals(other.id));
		}
		return false;
	}

	@Override
	public int hashCode() {
		return id.hashCode(); // limited calculation to assure it behaves the same as "equals"
	}

	/**
	 * Own toString implementation, making it more readable.
	 */
	public String toString() {
		return tileLevel + File.separator + x + File.separator + id;
	}

	// -------------------------------------------------------------------------
	// Getters and setters:
	// -------------------------------------------------------------------------

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public long getCreated() {
		return created;
	}

	public void setCreated(long created) {
		this.created = created;
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

	public int getNrRequested() {
		return nrRequested;
	}

	public void setNrRequested(int nrRequested) {
		this.nrRequested = nrRequested;
	}
}