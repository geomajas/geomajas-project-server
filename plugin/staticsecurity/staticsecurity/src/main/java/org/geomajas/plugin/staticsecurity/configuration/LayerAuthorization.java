/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2015 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */

package org.geomajas.plugin.staticsecurity.configuration;

import org.geomajas.annotation.Api;
import org.geomajas.security.BaseAuthorization;

import java.util.List;
import java.util.regex.Pattern;

/**
 * Layer authorization matching LayerAuthorizationInfo.
 *
 * @author Joachim Van der Auwera
 * @since 1.6.0
 */
@Api(allMethods = true)
public class LayerAuthorization implements BaseAuthorization {

	private LayerAuthorizationInfo info; // NOSONAR

	/**
	 * Create a {@link LayerAuthorization} for deserialization.
	 */
	protected LayerAuthorization() {
		// for deserialization
	}

	/**
	 * Build authorization for the given configuration.
	 *
	 * @param info authorization info
	 * @since 1.9.0
	 */
	public LayerAuthorization(LayerAuthorizationInfo info) {
		this.info = info;
	}

	@Override
	public String getId() {
		return "LayerAuthorizationInfo." + Integer.toString(info.hashCode());
	}

	@Override
	public boolean isToolAuthorized(String toolId) {
		return check(toolId, info.getToolsInclude(), info.getToolsExclude());
	}

	@Override
	public boolean isCommandAuthorized(String commandName) {
		return check(commandName, info.getCommandsInclude(), info.getCommandsExclude());
	}

	@Override
	public boolean isLayerVisible(String layerId) {
		return check(layerId, info.getVisibleLayersInclude(), info.getVisibleLayersExclude());
	}

	@Override
	public boolean isLayerUpdateAuthorized(String layerId) {
		return check(layerId, info.getUpdateAuthorizedLayersInclude(), info.getUpdateAuthorizedLayersExclude());
	}

	@Override
	public boolean isLayerCreateAuthorized(String layerId) {
		return check(layerId, info.getCreateAuthorizedLayersInclude(), info.getCreateAuthorizedLayersExclude());
	}

	@Override
	public boolean isLayerDeleteAuthorized(String layerId) {
		return check(layerId, info.getDeleteAuthorizedLayersInclude(), info.getDeleteAuthorizedLayersExclude());
	}

	/**
	 * Check whether the given id is included in the list of includes and not excluded.
	 *
	 * @param id id to check
	 * @param includes list of include regular expressions
	 * @param excludes list of exclude regular expressions
	 * @return true when id included and not excluded
	 */
	protected boolean check(String id, List<String> includes, List<String> excludes) {
		return check(id, includes) && !check(id, excludes);
	}

	/**
	 * Check whether the given is is matched by one of the include expressions.
	 *
	 * @param id id to check
	 * @param includes list of include regular expressions
	 * @return true when id is included
	 */
	protected boolean check(String id, List<String> includes) {
		if (null != includes) {
			for (String check : includes) {
				if (check(id, check)) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * Check whether the value is matched by a regular expression.
	 *
	 * @param value value
	 * @param regex regular expression
	 * @return true when value is matched
	 */
	protected boolean check(String value, String regex) {
		Pattern pattern = Pattern.compile(regex);
		return pattern.matcher(value).matches();
	}
}
