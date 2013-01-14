/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2013 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the Apache
 * License, Version 2.0. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */
package org.geomajas.sld.editor.common.client.model;

import org.geomajas.sld.RuleInfo;
import org.geomajas.sld.editor.common.client.GeometryType;

/**
 * {@link RuleModel} factory (generated, see client module).
 * 
 * @author Jan De Moerloose
 * 
 */
public interface RuleModelFactory {

	RuleModel create(RuleGroup ruleGroup, RuleInfo ruleInfo, GeometryType geometryType);
	// TODO: 2 creators: RuleModel create(GeometryType geometryType);
}
