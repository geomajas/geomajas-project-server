package org.geomajas.sld.client.model;

import org.geomajas.sld.RuleInfo;

public interface RuleModelFactory {

	RuleModel create(RuleInfo ruleInfo);
}
