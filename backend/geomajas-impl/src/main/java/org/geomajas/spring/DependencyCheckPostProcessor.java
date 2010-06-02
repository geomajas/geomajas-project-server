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

package org.geomajas.spring;

import org.geomajas.global.PluginInfo;
import org.geomajas.global.PluginVersionInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Spring configuration post-processor which verifies that all registered plug-ins (including the back-end), have their
 * their declared dependency requirements met.
 *
 * @author Joachim Van der Auwera
 */
@Component
public class DependencyCheckPostProcessor {

	@Autowired(required = false)
	protected Map<String, PluginInfo> declaredPlugins;

	@PostConstruct
	public void checkPluginDependencies() {
		if (null == declaredPlugins) {
			return;
		}
		// remove unfiltered plugin metadata (needed for eclipse !)
		for (Map.Entry<String, PluginInfo> entry :
				new ArrayList<Map.Entry<String, PluginInfo>>(declaredPlugins.entrySet())) {
			String version = entry.getValue().getVersion().getVersion();
			if (null != version && version.startsWith("$")) {
				declaredPlugins.remove(entry.getKey());
			}
		}

		// start by going through all plug-ins to build a map of versions for plug-in keys
		// includes verification that each key is only used once
		Map<String, String> versions = new HashMap<String, String>();
		for (PluginInfo plugin : declaredPlugins.values()) {
			String name = plugin.getVersion().getName();
			String version = plugin.getVersion().getVersion();
			// check for multiple plugin with same name but different versions (duplicates allowed for jar+source dep)
			if (null != version) {
				if (versions.containsKey(name) && !versions.get(name).equals(version)) {
					throw new RuntimeException("Invalid configuration, the plug-in with name " + name +
							" has been declared twice. It is know both as version " + version + " and " +
							versions.get(name) + ". The plug-in name is either used by more than one plug-in or " +
							"the plug-in is on the classpath more than once.");
				}
				versions.put(name, version);
			}
		}

		// Check dependencies
		StringBuffer message = new StringBuffer();
		String backendVersion = versions.get("Geomajas");
		for (PluginInfo plugin : declaredPlugins.values()) {
			String name = plugin.getVersion().getName();
			message.append(checkVersion(name, "Geomajas back-end", plugin.getBackendVersion(), backendVersion));
			for (PluginVersionInfo dependency : plugin.getDependencies()) {
				String depName = dependency.getName();
				message.append(checkVersion(name, depName, dependency.getVersion(), versions.get(depName)));
			}
		}
		if (message.length() > 0) {
			throw new RuntimeException("Plug-in dependencies not met\n" + message.toString());
		}
	}

	String checkVersion(String pluginName, String dependency, String requestedVersion, String availableVersion) {
		if (null == availableVersion) {
			return "Dependency " + dependency + " not found for " + pluginName + ", version " + requestedVersion +
					" or higher needed.\n";
		}
		Version requested = new Version(requestedVersion);
		Version available = new Version(availableVersion);
		if (requested.getMajor() != available.getMajor()) {
			return "Dependency " + dependency + " is provided in a incompatible API version for plug-in " +
					pluginName + ", which requests version " + requestedVersion +
					", but version " + availableVersion + " supplied.\n";
		}
		if (requested.after(available)) {
			return "Dependency " + dependency + " too old for " + pluginName + ", version " + requestedVersion +
					" or higher needed, but version " + availableVersion + " supplied.\n";
		}
		return "";
	}

	/** Parsed version representation. */
	public static class Version {

		private int major, minor, revision;
		private String qualifier;

		public Version(String version) {
			int pos;
			String part;

			pos = getSeparatorPosition(version);
			part = version.substring(0, pos);
			version = getRest(version, pos);
			try {
				major = Integer.parseInt(part);
			} catch (NumberFormatException nfe) {
				qualifier = part;
				return;
			}
			pos = getSeparatorPosition(version);
			part = version.substring(0, pos);
			version = getRest(version, pos);
			try {
				minor = Integer.parseInt(part);
			} catch (NumberFormatException nfe) {
				qualifier = part;
				return;
			}
			pos = getSeparatorPosition(version);
			part = version.substring(0, pos);
			version = getRest(version, pos);
			try {
				revision = Integer.parseInt(part);
			} catch (NumberFormatException nfe) {
				qualifier = part;
				return;
			}
			qualifier = version;
		}

		private int getSeparatorPosition(String str) {
			int pos1 = str.indexOf('.');
			int pos2 = str.indexOf('-');
			if (pos1 >= 0) {
				if (pos2 >= 0) {
					return Math.min(pos1, pos2);
				}
				return pos1;
			}
			if (pos2 >= 0) {
				return pos2;
			}
			return str.length();
		}

		private String getRest(String str, int pos) {
			if (str.length() > pos) {
				return str.substring(pos + 1);
			}
			return "";
		}

		public boolean after(Version other) {
			if (major > other.major) {
				return true;
			}
			if (major == other.major && minor > other.minor) {
				return true;
			}
			if (major == other.major && minor == other.minor && revision > other.revision) {
				return true;
			}
			if (major == other.major && minor == other.minor && revision == other.revision) {
				return qualifier.compareTo(other.qualifier) < 0;
			}
			return false;
		}

		public int getMajor() {
			return major;
		}

		public int getMinor() {
			return minor;
		}

		public int getRevision() {
			return revision;
		}

		public String getQualifier() {
			return qualifier;
		}
	}
}

