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

package org.geomajas.spring;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.geomajas.global.ExceptionCode;
import org.geomajas.global.GeomajasException;
import org.geomajas.global.PluginInfo;
import org.geomajas.global.PluginVersionInfo;
import org.geomajas.service.TestRecorder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Spring configuration post-processor which verifies that all registered plug-ins (including the back-end), have their
 * their declared dependency requirements met.
 *
 * @author Joachim Van der Auwera
 */
@Component
public class DependencyCheckPostProcessor {

	/** Group name for recorder. */
	protected static final String GROUP = "depcheck";
	/** Value which needs to be recorded. */
	protected static final String VALUE = "done";

	private static final String EXPR_START = "$";

	@Autowired(required = false)
	protected Map<String, PluginInfo> declaredPlugins;

	@Autowired
	private TestRecorder recorder;

	/**
	 * Finish initializing.
	 *
	 * @throws GeomajasException oops
	 */
	@PostConstruct
	protected void checkPluginDependencies() throws GeomajasException {
		if ("true".equals(System.getProperty("skipPluginDependencyCheck"))) {
			return;
		}

		if (null == declaredPlugins) {
			return;
		}

		// start by going through all plug-ins to build a map of versions for plug-in keys
		// includes verification that each key is only used once
		Map<String, String> versions = new HashMap<String, String>();
		for (PluginInfo plugin : declaredPlugins.values()) {
			String name = plugin.getVersion().getName();
			String version = plugin.getVersion().getVersion();
			// check for multiple plugin with same name but different versions (duplicates allowed for jar+source dep)
			if (null != version) {
				String otherVersion = versions.get(name);
				if (null != otherVersion) {
					if (!version.startsWith(EXPR_START)) {
						if (!otherVersion.startsWith(EXPR_START) && !otherVersion.equals(version)) {
							throw new GeomajasException(ExceptionCode.DEPENDENCY_CHECK_INVALID_DUPLICATE,
									name, version, versions.get(name));
						}
						versions.put(name, version);
					}
				} else {
					versions.put(name, version);
				}
			}
		}

		// Check dependencies
		StringBuilder message = new StringBuilder();
		String backendVersion = versions.get("Geomajas");
		for (PluginInfo plugin : declaredPlugins.values()) {
			String name = plugin.getVersion().getName();
			message.append(checkVersion(name, "Geomajas back-end", plugin.getBackendVersion(), backendVersion));
			List<PluginVersionInfo> dependencies = plugin.getDependencies();
			if (null != dependencies) {
				for (PluginVersionInfo dependency : plugin.getDependencies()) {
					String depName = dependency.getName();
					message.append(checkVersion(name, depName, dependency.getVersion(), versions.get(depName)));
				}
			}
			dependencies = plugin.getOptionalDependencies();
			if (null != dependencies) {
				for (PluginVersionInfo dependency : dependencies) {
					String depName = dependency.getName();
					String availableVersion = versions.get(depName);
					if (null != availableVersion) {
						message.append(checkVersion(name, depName, dependency.getVersion(), versions.get(depName)));
					}
				}
			}
		}
		if (message.length() > 0) {
			throw new GeomajasException(ExceptionCode.DEPENDENCY_CHECK_FAILED, message.toString());
		}

		recorder.record(GROUP, VALUE);
	}

	/**
	 * Check the version to assure it is allowed.
	 *
	 * @param pluginName plugin name which needs the dependency
	 * @param dependency dependency which needs to be verified
	 * @param requestedVersion requested/minimum version
	 * @param availableVersion available version
	 * @return version check problem or empty string when all is fine
	 */
	String checkVersion(String pluginName, String dependency, String requestedVersion, String availableVersion) {
		if (null == availableVersion) {
			return "Dependency " + dependency + " not found for " + pluginName + ", version " + requestedVersion +
					" or higher needed.\n";
		}
		if (requestedVersion.startsWith(EXPR_START) || availableVersion.startsWith(EXPR_START)) {
			return "";
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

		/**
		 * Construct a version from a version string.
		 *
		 * @param version version string
		 */
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

		/**
		 * Check whether this version is more recent as the other version.
		 * Note that we explicitly allow intermediate milestone releases.
		 * When there is a qualifier, we order them alphabetically. This way a 1.0.0-M1 precedes 1.0.0-SNAPSHOT which
		 * precedes 1.0.0-ZZZ.
		 *
		 * @param other version to compare with
		 * @return false when other is older or equal, true when other is younger
		 */
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
				return ("".equals(qualifier) && !("".equals(other.qualifier))) ||
						(!"".equals(other.qualifier) && qualifier.compareTo(other.qualifier) > 0);
			}
			return false;
		}

		/**
		 * Get major version number, the 1 in 1.2.3-SNAPSHOT.
		 *
		 * @return major version number
		 */
		public int getMajor() {
			return major;
		}

		/**
		 * Get minor version number, the 2 in 1.2.3-SNAPSHOT.
		 *
		 * @return minor version number
		 */
		public int getMinor() {
			return minor;
		}

		/**
		 * Get revision, the 3 in 1.2.3-SNAPSHOT.
		 *
		 * @return revision
		 */
		public int getRevision() {
			return revision;
		}

		/**
		 * Get qualifier, the SNAPSHOT in 1.2.3-SNAPSHOT.
		 *
		 * @return qualifier or ""
		 */
		public String getQualifier() {
			return qualifier;
		}
	}
}

