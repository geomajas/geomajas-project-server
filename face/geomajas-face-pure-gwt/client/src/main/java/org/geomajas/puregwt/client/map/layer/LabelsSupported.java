package org.geomajas.puregwt.client.map.layer;


/**
 * Extension for layers that indicates whether or not labeling is supported.
 * 
 * @author Pieter De Graef
 */
public interface LabelsSupported {

	void setLabeled(boolean labeled);
	
	boolean isLabeled();
}
