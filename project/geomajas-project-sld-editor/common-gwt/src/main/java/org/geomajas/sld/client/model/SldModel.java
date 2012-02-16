package org.geomajas.sld.client.model;

import org.geomajas.sld.StyledLayerDescriptorInfo;

public interface SldModel {

	public abstract boolean isComplete();

	public abstract String getName();

	public abstract StyledLayerDescriptorInfo getSld();

	public abstract SldGeneralInfo getGeneralInfo();

	public abstract RuleGroup getRuleGroup();

	public abstract boolean isRulesSupported();

	public abstract void synchronize();

	public abstract void copyFrom(SldModel create);

}