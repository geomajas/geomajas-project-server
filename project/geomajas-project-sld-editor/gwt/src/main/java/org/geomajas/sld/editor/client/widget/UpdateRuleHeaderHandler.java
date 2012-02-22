/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2012 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the Apache
 * License, Version 2.0. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */

package org.geomajas.sld.editor.client.widget;

/**
 * Provides call-back to inform the receiver that the style rule header fields (title and/or name) for the current rule
 * have changed.
 * 
 * @author An Buyle
 * 
 */
public interface UpdateRuleHeaderHandler {

	void execute(String ruleTitle, String ruleName);

	void updateTitle(String ruleTitle);
}
