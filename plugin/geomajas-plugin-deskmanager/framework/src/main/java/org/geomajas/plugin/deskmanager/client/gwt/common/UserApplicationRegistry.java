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
package org.geomajas.plugin.deskmanager.client.gwt.common;

import java.util.LinkedHashMap;
import java.util.Map;

import org.geomajas.annotation.FutureApi;


/**
 * 
 * @author Oliver May
 *
 */
@FutureApi
public class UserApplicationRegistry {

	private static final UserApplicationRegistry INSTANCE;
	private Map<String, UserApplication> loketten;

	static {
		INSTANCE = new UserApplicationRegistry();
	}
	
	public UserApplicationRegistry() {
		loketten = new LinkedHashMap<String, UserApplication>();
	}

	public void register(UserApplication loket) {
		if (null != loket) {
			loketten.put(loket.getClientApplicationKey(), loket);
		}
	}

	public UserApplication get(String key) {
		return loketten.get(key);
	}
	
	public static UserApplicationRegistry getInstance() {
		return INSTANCE;
	}
	
	public Map<String, UserApplication> getLoketten() {
		return loketten;
	}
	
	public LinkedHashMap<String, String> getLoketNames() {
		LinkedHashMap<String, String> loketNames = new LinkedHashMap<String, String>();
		for (UserApplication ca : getLoketten().values()) {
			loketNames.put(ca.getClientApplicationKey(), ca.getClientApplicationName());
		}
		return loketNames;
	}
}
