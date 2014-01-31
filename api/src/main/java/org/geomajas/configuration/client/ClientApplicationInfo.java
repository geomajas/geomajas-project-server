/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2014 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */
package org.geomajas.configuration.client;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.constraints.NotNull;

import org.geomajas.annotation.Api;
import org.geomajas.configuration.IsInfo;

/**
 * Base application info entry point.
 * 
 * @author Joachim Van der Auwera
 * @author Jan De Moerloose
 * @since 1.6.0
 */
@Api(allMethods = true)
public class ClientApplicationInfo implements IsInfo {

	private static final long serialVersionUID = 151L;

	@NotNull
	private String id;

	private List<ClientMapInfo> maps = new ArrayList<ClientMapInfo>();

	private ClientUserDataInfo userData;

	private int screenDpi = 96;

	private Map<String, ClientWidgetInfo> widgetInfo = new HashMap<String, ClientWidgetInfo>();

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

	/**
	 * Get the custom configuration data.
	 * 
	 * @return custom configuration data
	 */
	public ClientUserDataInfo getUserData() {
		return userData;
	}

	/**
	 * Set the custom configuration data you wish to pass to the client.
	 * 
	 * @param userData
	 *            Custom configuration data
	 */
	public void setUserData(ClientUserDataInfo userData) {
		this.userData = userData;
	}

	/**
	 * Get configuration for (custom) widgets which are not map specific.
	 *
	 * @return map keyed on widget id containing widget configurations
	 * @since 1.8.0
	 */
	@Api
	public Map<String, ClientWidgetInfo> getWidgetInfo() {
		return widgetInfo;
	}

	/**
	 * Get configuration for a (custom) map widgets which are not map specific.
	 *
	 * @param widget widget key
	 * @return widget configuration
	 * @since 1.8.0
	 */
	@Api
	public ClientWidgetInfo getWidgetInfo(String widget) {
		return widgetInfo.get(widget);
	}

	/**
	 * Set configuration for (custom) map widgets which are not map specific.
	 *
	 * @param widgetInfo map keyed on widget id containing widget configurations
	 * @since 1.8.0
	 */
	public void setWidgetInfo(Map<String, ClientWidgetInfo> widgetInfo) {
		this.widgetInfo = widgetInfo;
	}

	/**
	 * Dummy implementation to keep GWT serializer happy !
	 *
	 * @author Joachim Van der Auwera
	 */
	public static class DummyClientWidgetInfo implements ClientWidgetInfo {

		private static final long serialVersionUID = 180L;

		private String dummy;

		/**
		 * Dummy field, otherwise checkstyle complains.
		 *
		 * @return dummy field value
		 */
		public String getDummy() {
			return dummy;
		}

		/**
		 * Dummy field, otherwise checkstyle complains.
		 *
		 * @param dummy dummy field value
		 */
		public void setDummy(String dummy) {
			this.dummy = dummy;
		}
	}

	/**
	 * Dummy implementation to keep GWT serializer happy !
	 *
	 * @author Jan De Moerloose
	 */
	public static class DummyClientUserDataInfo implements ClientUserDataInfo {

		private static final long serialVersionUID = 154L;

		private String dummy;

		/**
		 * Dummy field, otherwise checkstyle complains.
		 *
		 * @return dummy field value
		 */
		public String getDummy() {
			return dummy;
		}

		/**
		 * Dummy field, otherwise checkstyle complains.
		 *
		 * @param dummy dummy field value
		 */
		public void setDummy(String dummy) {
			this.dummy = dummy;
		}
	}

	@Override
	public String toString() {
		return "ClientApplicationInfo{" +
				"id='" + id + '\'' +
				", maps=" + maps +
				", userData=" + userData +
				", screenDpi=" + screenDpi +
				", widgetInfo=" + widgetInfo +
				'}';
	}
}
