package org.geomajas.sld.client.model;

import org.geomajas.sld.StyledLayerDescriptorInfo;

public interface SldModel extends SldGeneralInfo {

	String getName();

	StyledLayerDescriptorInfo getSld();

	RuleGroup getRuleGroup();

	boolean isSupported();

	boolean isDirty();

	void setDirty(boolean dirty);

	boolean isComplete();
	
	void synchronize();

	void refresh(SldModel create);

	public abstract String getSupportedWarning();


}