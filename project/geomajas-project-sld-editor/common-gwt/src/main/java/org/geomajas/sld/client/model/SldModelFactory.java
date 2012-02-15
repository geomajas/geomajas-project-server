package org.geomajas.sld.client.model;

import org.geomajas.sld.StyledLayerDescriptorInfo;


public interface SldModelFactory {
	SldModel create(StyledLayerDescriptorInfo styledLayerDescriptorInfo);
}
