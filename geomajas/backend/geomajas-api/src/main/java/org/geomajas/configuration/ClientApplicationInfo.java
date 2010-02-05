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
package org.geomajas.configuration;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Base application info entry point.
 *
 * @author Joachim Van der Auwera
 * @author Jan De Moerloose
 */
public class ClientApplicationInfo implements Serializable {

	private static final long serialVersionUID = 151L;
	private String id;
	private String name;
	private List<MapInfo> maps;
	private int screenDpi;

	/**
	 * Get the id for this application.
	 *
	 * @return url start for this application
	 */
	public String getId() {
		return id;
	}

	/**
	 * Set the id for this application.
	 *
	 * @param value start for this application
	 */
	public void setId(String value) {
		this.id = value;
	}

	/**
	 * Get the name of the application.
	 *
	 * @return application name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Set the name for this application.
	 *
	 * @param value application name
	 */
	public void setName(String value) {
		this.name = value;
	}

	/**
	 * Get the list of maps for this application.
	 *
	 * @return list of {@link MapInfo}
	 */
	public List<MapInfo> getMaps() {
		if (null == maps) {
			maps = new ArrayList<MapInfo>();
		}
		return maps;
	}

	/**
	 * Set the list of maps for this application.
	 *
	 * @param value list of {@link MapInfo}
	 */
	public void setMaps(List<MapInfo> value) {
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
	 * @param screenDpi number of dots per inch (default = 96)
	 */
	public void setScreenDpi(int screenDpi) {
		this.screenDpi = screenDpi;
	}
}
