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
package org.geomajas.example.gwt.client.samples.i18n;

import com.google.gwt.core.client.GWT;

/**
 * Central provider for all i18n constants.
 * 
 * @author Jan De Moerloose
 */
public final class I18nProvider {

	private static final SampleMessages MESSAGES = GWT.create(SampleMessages.class);

	private I18nProvider() {
	}

	public static SampleMessages getSampleMessages() {
		return MESSAGES;
	}
}
