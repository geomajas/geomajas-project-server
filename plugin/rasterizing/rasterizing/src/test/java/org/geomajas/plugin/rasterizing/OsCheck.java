package org.geomajas.plugin.rasterizing;

public class OsCheck {

	public static boolean isWindows() {
		return System.getProperty("os.name").toLowerCase().contains("windows");
	}
}
