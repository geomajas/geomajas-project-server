/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2013 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */
package org.geomajas.gwt.client.map.render;

import org.geomajas.gwt.client.event.LayerAddedEvent;
import org.geomajas.gwt.client.event.LayerHideEvent;
import org.geomajas.gwt.client.event.LayerOrderChangedEvent;
import org.geomajas.gwt.client.event.LayerRefreshedEvent;
import org.geomajas.gwt.client.event.LayerRemovedEvent;
import org.geomajas.gwt.client.event.LayerShowEvent;
import org.geomajas.gwt.client.event.LayerStyleChangedEvent;
import org.geomajas.gwt.client.event.LayerVisibilityMarkedEvent;
import org.geomajas.gwt.client.event.MapResizedEvent;
import org.geomajas.gwt.client.event.ViewPortChangedEvent;
import org.geomajas.gwt.client.event.ViewPortChangingEvent;
import org.geomajas.gwt.client.event.ViewPortScaledEvent;
import org.geomajas.gwt.client.event.ViewPortScalingEvent;
import org.geomajas.gwt.client.event.ViewPortTranslatedEvent;
import org.geomajas.gwt.client.event.ViewPortTranslatingEvent;


public class MockMapRenderer implements MapRenderer {

	public void onViewPortChanged(ViewPortChangedEvent event) {
		// TODO Auto-generated method stub
		
	}

	public void onViewPortScaled(ViewPortScaledEvent event) {
		// TODO Auto-generated method stub
		
	}

	public void onViewPortTranslated(ViewPortTranslatedEvent event) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onViewPortChanging(ViewPortChangingEvent event) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onViewPortScaling(ViewPortScalingEvent event) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onViewPortTranslating(ViewPortTranslatingEvent event) {
		// TODO Auto-generated method stub
		
	}

	public void onLayerOrderChanged(LayerOrderChangedEvent event) {
		// TODO Auto-generated method stub
		
	}

	public void onShow(LayerShowEvent event) {
		// TODO Auto-generated method stub
		
	}

	public void onHide(LayerHideEvent event) {
		// TODO Auto-generated method stub
		
	}

	public void onVisibilityMarked(LayerVisibilityMarkedEvent event) {
		// TODO Auto-generated method stub
		
	}

	public void onLayerStyleChanged(LayerStyleChangedEvent event) {
		// TODO Auto-generated method stub
		
	}

	public void onLayerRefreshed(LayerRefreshedEvent event) {
		// TODO Auto-generated method stub
		
	}

	public void onMapResized(MapResizedEvent event) {
		// TODO Auto-generated method stub
		
	}

	public void onLayerAdded(LayerAddedEvent event) {
		// TODO Auto-generated method stub
		
	}

	public void onLayerRemoved(LayerRemovedEvent event) {
		// TODO Auto-generated method stub
		
	}

	public void setMapExtentScaleAtFetch(double scale) {
		// TODO Auto-generated method stub
		
	}

	public void setAnimationMillis(int animationMillis) {
		// TODO Auto-generated method stub
		
	}

	public void setNrAnimatedLayers(int nrAnimatedLayers) {
		// TODO Auto-generated method stub
		
	}

}
