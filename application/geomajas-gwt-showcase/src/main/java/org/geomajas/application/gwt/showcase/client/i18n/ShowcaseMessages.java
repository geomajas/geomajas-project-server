/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2012 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */

package org.geomajas.application.gwt.showcase.client.i18n;

import com.google.gwt.i18n.client.Messages;

/**
 * <p>
 * Localization messages for the GWT showcase.
 * </p>
 * <p>
 * Most messages come from the combined example jars, but these are the messages for the samples which are directly
 * included.
 * </p>
 *
 * @author Pieter De Graef
 */
public interface ShowcaseMessages extends Messages {

	String treeGroupSecurity();

	// Login sample:

	String loginTitle();

	String loginDescription();

	String loginSuccess(String userToken);

	String loginFailure();

	String logoutSuccess();

	String logoutFailure();

	// LayerSecurity sample:

	String layerSecurityTitle();

	String layerSecurityDescription();

	String securityLogInWith(String name);

	// FeatureSecurity sample:

	String featureSecurityTitle();

	String featureSecurityDescription();

	// FilterSecurity sample:

	String filterSecurityTitle();

	String filterSecurityDescription();

	// CommandSecurity sample:

	String commandSecurityTitle();

	String commandSecurityDescription();

	// ToolSecurity sample:

	String toolSecurityTitle();

	String toolSecurityDescription();

	// AttributeSecurity sample:

	String attributeSecurityTitle();

	String attributeSecurityDescription();

	String attributeSecurityButtonTitle();

	// layer samples

	String openCycleMapTitle();
	String openCycleMapDescription();

	String tmsTitle();
	String tmsDescription();

}