package org.geomajas.sld.client.model;

import org.geomajas.sld.RuleInfo;
import org.geomajas.sld.editor.client.GeometryType;

public interface RuleModelFactory {

	RuleModel create(RuleGroup ruleGroup, RuleInfo ruleInfo, GeometryType geometryType);
	//TODO: 2 creators: RuleModel create(GeometryType geometryType);
}
