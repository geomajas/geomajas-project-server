package org.geomajas.puregwt.client.widget;

import org.geomajas.puregwt.client.map.VectorContainer;


public interface MapWidget {
	VectorContainer getWorldContainer(String id);

	VectorContainer getScreenContainer(String id);

}
