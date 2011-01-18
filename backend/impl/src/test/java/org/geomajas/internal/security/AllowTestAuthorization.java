package org.geomajas.internal.security;

import org.geomajas.security.BaseAuthorization;

/**
 * Authorization that allows execution of the SecurityTestCommand.
 * 
 * @author Jan De Moerloose
 * 
 */
public class AllowTestAuthorization implements BaseAuthorization {

	public String getId() {
		return "AllowTest";
	}

	public boolean isToolAuthorized(String toolId) {
		return false;
	}

	public boolean isCommandAuthorized(String commandName) {
		return "test.SecurityTestCommand".equals(commandName);
	}

	public boolean isLayerVisible(String layerId) {
		return false;
	}

	public boolean isLayerUpdateAuthorized(String layerId) {
		return false;
	}

	public boolean isLayerCreateAuthorized(String layerId) {
		return false;
	}

	public boolean isLayerDeleteAuthorized(String layerId) {
		return false;
	}

}
