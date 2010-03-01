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
package org.geomajas.configuration.client;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotNull;

/**
 * Base application info entry point.
 * 
 * @author Joachim Van der Auwera
 * @author Jan De Moerloose
 */
public class ClientApplicationInfo implements Serializable {

	private static final long serialVersionUID = 151L;

	@NotNull
	private String id;

	private List<ClientMapInfo> maps = new ArrayList<ClientMapInfo>();

	private int screenDpi = 96;

	/**
	 * Get the unique id for this application (auto-copied from Spring context).
	 * 
	 * @return id unique id
	 */
	public String getId() {
		return id;
	}

	/**
	 * Set the id for this application.
	 * 
	 * @param value
	 *            id unique id
	 */
	public void setId(String value) {
		this.id = value;
	}

	/**
	 * Get the list of maps for this application.
	 * 
	 * @return list of {@link ClientMapInfo}
	 */
	public List<ClientMapInfo> getMaps() {
		if (null == maps) {
			maps = new ArrayList<ClientMapInfo>();
		}
		return maps;
	}

	/**
	 * Set the list of maps for this application.
	 * 
	 * @param value
	 *            list of {@link ClientMapInfo}
	 */
	public void setMaps(List<ClientMapInfo> value) {
		this.maps = value;
	}

	/**
	 * Get number of dots per inch of the client screen.
	 * 
	 * @return number of dots per inch (default = 96)
	 */
	public int getScreenDpi() {
		return screenDpi;
	}

	/**
	 * Set number of dots per inch of the client screen.
	 * 
	 * @param screenDpi
	 *            number of dots per inch (default = 96)
	 */
	public void setScreenDpi(int screenDpi) {
		this.screenDpi = screenDpi;
	}
}
