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
package org.geomajas.smartgwt.client.gfx.paintable.mapaddon;

import java.util.ArrayList;
import java.util.List;

import org.geomajas.annotation.Api;
import org.geomajas.configuration.client.ScaleInfo;
import org.geomajas.smartgwt.client.controller.ZoomSliderController;
import org.geomajas.smartgwt.client.gfx.PainterVisitor;
import org.geomajas.smartgwt.client.gfx.paintable.Image;
import org.geomajas.smartgwt.client.map.event.MapViewChangedEvent;
import org.geomajas.smartgwt.client.map.event.MapViewChangedHandler;
import org.geomajas.smartgwt.client.spatial.Bbox;
import org.geomajas.smartgwt.client.widget.MapWidget;

/**
 * A customizable zoom slider map add-on, which is always on top of other add-ons to make sure the
 * onDrag event of {@link ZoomSliderController} is not interrupted. 
 * Using setters, you need to provide this class with;
 * <ul>
 * <li>{@link #setZoomIn(SingleMapAddon)}; This addon will be placed at the top of the slider.</li>
 * <li>{@link #setBackgroundPart(org.geomajas.smartgwt.client.gfx.paintable.Image)}; image that represents one zoom
 * level/scale in the background.</li>
 * <li>{@link #setSliderUnit(org.geomajas.smartgwt.client.gfx.paintable.Image)}; image that represents the slider's
 * unit or handle.</li>
 * <li>{@link #setZoomOut(SingleMapAddon)}; This addon will be placed at the bottom of the slider.</li>
 * </ul>  
 * 
 * @author Emiel Ackermann
 * @since 1.10.0
 */
@Api
public class ZoomSlider extends MapAddon implements MapViewChangedHandler {
	
	private Image backgroundPart;
	private Image sliderUnit;
	
	public static final String SLIDER = "slider";
	public static final String SLIDER_UNIT = "sliderUnit";
	public static final String SLIDER_AREA = "sliderArea";
	
	private MapWidget mapWidget;
	
	private List<Double> currentScaleList = new ArrayList<Double>();
	private SingleMapAddon zoomIn;
	private SliderArea sliderArea;
	private SingleMapAddon zoomOut;

	/**
	 * A customizable zoom slider map add-on. Using setters, you need to provide this class with;
	 * <ul>
	 * <li>{@link #setZoomIn(SingleMapAddon)}; This addon will be placed at the top of the slider.</li>
	 * <li>{@link #setBackgroundPart(Image)}; image that represents <b>one</b> zoom level/scale in the background.</li>
	 * <li>{@link #setSliderUnit(Image)}; image that represents the slider's unit or handle.</li>
	 * <li>{@link #setZoomOut(SingleMapAddon)}; This addon will be placed at the bottom of the slider.</li>
	 * </ul>
	 * 
	 * @param id id
	 * @param mapWidget map widget
	 */
	public ZoomSlider(String id, MapWidget mapWidget) {
		super(id, 0, 0);
		this.mapWidget = mapWidget;
	}
	
	/**
	 * Get the MapWidget object.
	 * 
	 * @return the MapWidget object.
	 */
	public MapWidget getMapWidget() {
		return mapWidget;
	}

	/**
	 * Get the SliderArea object.
	 * 
	 * @return the SliderArea object.
	 */
	public SliderArea getSliderArea() {
		return sliderArea;
	}

	/**
	 * Update the list of scales to include only the list of usable scales (from all possible scales).
	 */
	public void updateUsableScales() {
		Bbox bgBounds = backgroundPart.getBounds();
		/*
		 * First move background a little bit to right based on difference in width with the sliderUnit.
		 */
		int internalHorMargin = (int) (sliderUnit.getBounds().getWidth() - backgroundPart.getBounds().getWidth()) / 2;
		bgBounds.setX(internalHorMargin);
		
		int backgroundPartHeight = (int) bgBounds.getHeight();
		int sliderAreaHeight = 0;
		/*
		 * Needed for aligning the sliderUnit's y with currentScale's y.
		 */
		int currentUnitY = sliderAreaHeight;
		double currentScale = mapWidget.getMapModel().getMapView().getCurrentScale();
		
		List<Bbox> partBounds = new ArrayList<Bbox>();
		List<ScaleInfo> zoomLevels = mapWidget.getMapModel().getMapInfo().getScaleConfiguration().getZoomLevels();
		int size = zoomLevels.size();
		currentScaleList.clear();
		for (int i = size - 1 ; i >= 0 ; i--) {
			double scale = zoomLevels.get(i).getPixelPerUnit();
			if (mapWidget.getMapModel().getMapView().isResolutionAvailable(1.0 / scale)) {
				Bbox bounds = (Bbox) bgBounds.clone();
				bounds.setY(sliderAreaHeight);
				partBounds.add(bounds);
				currentScaleList.add(scale);
				if (scale == currentScale) {
					currentUnitY = sliderAreaHeight;
				}
				sliderAreaHeight += backgroundPartHeight;
			}
		}
		/*
		 * Align zoom slider unit with current zoom
		 */
		Bbox bounds = sliderUnit.getBounds();
		bounds.setY(currentUnitY);

		/*
		 * Zoom slider area 
		 */
		sliderArea = new SliderArea(SLIDER_AREA, (int) sliderUnit.getBounds().getWidth(), 
				sliderAreaHeight, sliderUnit, backgroundPart, partBounds,
				mapWidget, new ZoomSliderController(this));
		sliderArea.setHorizontalMargin((int) -bgBounds.getX());
		sliderArea.setVerticalMargin((int) backgroundPart.getBounds().getWidth());
		
		/*
		 * Zoom out button internal margin.
		 */
		zoomOut.getBackground().getBounds().setY(sliderArea.getVerticalMargin() + sliderAreaHeight);
		zoomOut.getIcon().getBounds().setY(sliderArea.getVerticalMargin() + sliderAreaHeight);
	}

	@Override
	public void onMapViewChanged(MapViewChangedEvent event) {
		/** When the map size changes, update the usable scales and the background of the zoom slider. */
		if (event.isMapResized()) {
			mapWidget.unregisterMapAddon(this);
			mapWidget.registerMapAddon(this);
		}
		if (!event.isSameScaleLevel()) {
			double currentScale = mapWidget.getMapModel().getMapView().getCurrentScale();
			for (int i = 0 ; i < currentScaleList.size() ; i++) {
				double scale = currentScaleList.get(i);
				if (currentScale == scale) {
					Bbox bounds = sliderArea.getIcon().getBounds();
					bounds.setY(i * backgroundPart.getBounds().getHeight());
					sliderArea.drawImage(sliderArea.applyMargins(bounds));
				}
			}
		}
	}
	/**
	 * Get list of current scales. This list will be determined depending on map size and maximum bounds.
	 * 
	 * @return list of current scales
	 */
	public List<Double> getCurrentScaleList() {
		return currentScaleList;
	}
	
	@Override
	public void accept(PainterVisitor visitor, Object group, Bbox bounds,
			boolean recursive) {
		mapWidget.getVectorContext().drawGroup(null, this);
		updateUsableScales();
		zoomIn.accept(visitor, this, bounds, recursive);
		zoomOut.accept(visitor, this, bounds, recursive);
		//Draw area last to make sure its extended rectangle is always on top.
		sliderArea.accept(visitor, this, bounds, recursive);
	}
	
	@Override
	public void setMapSize(int mapWidth, int mapHeight) {
		super.setMapSize(mapWidth, mapHeight);
		zoomIn.setMapSize(mapWidth, mapHeight);
		zoomOut.setMapSize(mapWidth, mapHeight);
	}
	/**
	 * Get the image object that is used for creating the background of the slider. 
	 * For each scale this image is used once.
	 * 
	 * @return part of the background that represents one scale.
	 */
	public Image getBackgroundPart() {
		return backgroundPart;
	}
	/**
	 * Set the image object that is used for creating the background of the slider. 
	 * For each scale this image is used once.
	 * 
	 * @param backgroundPart part of the background that represents one scale.
	 */
	public void setBackgroundPart(Image backgroundPart) {
		this.backgroundPart = backgroundPart;
		
	}
	/**
	 * Get the slider unit image object.
	 * 
	 * @return the slider unit image object
	 */
	public Image getSliderUnit() {
		return sliderUnit;
	}
	/**
	 * Set the slider unit image object.
	 * 
	 * @param sliderUnit the slider unit image object
	 */
	public void setSliderUnit(Image sliderUnit) {
		this.sliderUnit = sliderUnit;
	}
	/**
	 * Get the SingleMapAddon that zooms in.
	 * 
	 * @return zoomOut SingleMapAddon that zooms in
	 */
	public SingleMapAddon getZoomIn() {
		return zoomIn;
	}
	/**
	 * Set the SingleMapAddon that zooms in.
	 * 
	 * @param zoomIn SingleMapAddon that zooms in
	 */
	public void setZoomIn(SingleMapAddon zoomIn) {
		this.zoomIn = zoomIn;
	}
	/**
	 * Get the SingleMapAddon that zooms out.
	 * 
	 * @return zoomOut SingleMapAddon that zooms out
	 */
	public SingleMapAddon getZoomOut() {
		return zoomOut;
	}
	/**
	 * Set the SingleMapAddon that zooms out.
	 * 
	 * @param zoomOut SingleMapAddon that zooms out
	 */
	public void setZoomOut(SingleMapAddon zoomOut) {
		this.zoomOut = zoomOut;
	}

	@Override
	public void onDraw() {
	}

	@Override
	public void onRemove() {
	}
}
