package org.geomajas.gwt.client.map;

public interface MapGadget {
	void onDraw(ViewPort port, VectorContainer container);

	void onPan();

	void onScale();

	void onResize();

	void onDestroy();
}
