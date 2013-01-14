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

package org.geomajas.gwt.client.widget;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;

import org.geomajas.annotation.Api;
import org.geomajas.gwt.client.i18n.I18nProvider;
import org.geomajas.gwt.client.map.MapView;
import org.geomajas.gwt.client.map.event.MapModelEvent;
import org.geomajas.gwt.client.map.event.MapModelHandler;
import org.geomajas.gwt.client.map.event.MapViewChangedEvent;
import org.geomajas.gwt.client.map.event.MapViewChangedHandler;

import com.smartgwt.client.types.KeyNames;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.ComboBoxItem;
import com.smartgwt.client.widgets.form.fields.events.ChangedEvent;
import com.smartgwt.client.widgets.form.fields.events.ChangedHandler;
import com.smartgwt.client.widgets.form.fields.events.KeyPressEvent;
import com.smartgwt.client.widgets.form.fields.events.KeyPressHandler;
import com.smartgwt.client.widgets.form.validator.CustomValidator;

/**
 * <p>
 * A drop down selection box for setting and displaying the current scale of a map. The displayed scale is a relative
 * scale and is expressed as an abstract number, as opposed to the MapView scale which is expressed in pixels per map
 * unit. An arbitrary scale can be filled in by the user. Implemented as a canvas so it can be added where needed.
 * </p>
 * <p>
 * This widget has the option to add new scale levels to the list, as the user types them (and presses 'Enter'). By
 * default this option is turned off. Turn it on using the <code>setUpdatingScaleList</code> method.
 * </p>
 * 
 * @author Jan De Moerloose
 * @author An Buyle
 * 
 * @since 1.6.0
 */
@Api
public class ScaleSelect extends DynamicForm implements KeyPressHandler, ChangedHandler, MapViewChangedHandler {

	private MapView mapView;
	private MapWidget mapWidget;
	
	private ComboBoxItem scaleItem;

	private LinkedHashMap<String, Double> valueToScale = new LinkedHashMap<String, Double>();

	// pixel length in map units
	private double pixelLength;

	private boolean updatingScaleList;

	// Full list of scales. These will not always be shown (depends on map size and maximum bounds), but they are
	// stored here for when the map resizes. At the point the visible set needs to be e-evaluated.
	private List<Double> scaleList;

	private int precision;

	private int significantDigits;
	private boolean isScaleItemInitialized; /* Initially false, if true a legal getPixelPerUnit() could be retrieved
		from the mapWidget and the selectItem is in a normal state (a list of possible scales, and display value
		shows the current scale of the mapView */ 


	// -------------------------------------------------------------------------
	// Constructor:
	// -------------------------------------------------------------------------

	/**
	 * Constructs a ScaleSelect that acts on the specified map view.
	 *
	 * @param mapView
	 *            the map view
	 * @param pixelLength
	 *            the pixel length in map units
	 * @since 1.6.0
	 */
	@Api
	public ScaleSelect(MapView mapView, double pixelLength) {
		this.mapView = mapView;
		this.pixelLength = pixelLength; // Possibly NaN if called by new ScaleSelect(MapWidget) and 
								// mapWidget hasn't been fully initialized yet 
		setWidth100();
		setHeight100();
		init();
	}
	/**
	 * Constructs a ScaleSelect that acts on the specified map widget.
	 *
	 * @param mapWidget map widget, must be non-null
	 * @since 1.10.0
	 */
	@Api
	public ScaleSelect(final MapWidget mapWidget) {
		this(mapWidget.getMapModel().getMapView(), mapWidget.getPixelPerUnit());
		this.mapWidget = mapWidget;

		mapWidget.getMapModel().addMapModelHandler(new MapModelHandler() {
			public void onMapModelChange(MapModelEvent event) {
				/** When the MapModel changes, , setup the select item if it hasn't been fully initialized yet. */

				refreshPixelLength();
				if ( !Double.isNaN(pixelLength) && (0.0 != pixelLength)) {
					if (!isScaleItemInitialized) {
						scaleItem.clearValue();
						updateResolutions();
						setDisplayScale(mapView.getCurrentScale() * pixelLength);
					} else if (scaleItem.getValueAsString() == null || "".equals(scaleItem.getValueAsString())) {
						setDisplayScale(mapView.getCurrentScale() * pixelLength);
					} else if (scaleItem.getDisplayValue() == null
							|| "".equals(scaleItem.getDisplayValue())) {
						setDisplayScale(mapView.getCurrentScale() * pixelLength);
					}
				}
			}
		});
	}

	// -------------------------------------------------------------------------
	// Public methods:
	// -------------------------------------------------------------------------

	/**
	 * Set the specified relative scale values in the select item.
	 *
	 * @param scales
	 *            array of relative scales (should be multiplied by pixelLength if in pixels/m)
	 * @since 1.6.0
	 */
	@Api
	public void setScales(Double... scales) {
		// Sort decreasing and store the list:
		Arrays.sort(scales, Collections.reverseOrder());
		scaleList = Arrays.asList(scales);
		// Apply the requested scales on the SelectItem. Make sure only available scales are added:
		updateScaleList();
	}


	/**
	 * Set the precision for displaying the scales.
	 * <p/>
	 * For example use 1000 to make sure a scale of 1:12345 is displayed as 1:12000.
	 * <p/>
	 * Use zero to leaves all scales untouched.
	 *
	 * @param precision precision for displaying scales
	 * @since 1.10.0
	 */
	@Api
	public void setPrecision(int precision) {
		this.precision = precision;
		updateScaleList();
	}

	/**
	 * Set the maximum number of significant digits to be displayed.
	 * <p/>
	 * For example when there are two scales 1:2000 and 1:12345000 and using the setting 3 for significantDigits these
	 * will be displayed as 1:2000 and 1:12300000.
	 * <p/>
	 * User zero for unlimited maximum,
	 *
	 * @param significantDigits number of significant digits to display in the scales
	 * @since 1.10.0
	 */
	@Api
	public void setSignificantDigits(int significantDigits) {
		this.significantDigits = significantDigits;
		updateScaleList();
	}

	/**
	 * Return the {@link MapView} object to which this widget is connected.
	 *
	 * @return {@link MapView} object which is connected to this widget
	 */
	public MapView getMapView() {
		return mapView;
	}

	/**
	 * When typing custom scale levels in the select item, should these new scale levels be added to the list or not?
	 *
	 * @return true when new (typed) scales should be added in list
	 */
	public boolean isUpdatingScaleList() {
		return updatingScaleList;
	}

	/**
	 * When typing custom scale levels in the select item, should these new scale levels be added to the list or not?
	 * The default value is false, which means that the list of scales in the select item does not change.
	 *
	 * @param updatingScaleList
	 *            Should the new scale be added?
	 */
	public void setUpdatingScaleList(boolean updatingScaleList) {
		this.updatingScaleList = updatingScaleList;
	}

	/** Will be called when the MapView changes (caused by e.g zooming); updates the select item to the correct scale.
	 *
	 * Note: This method implements MapViewChangedHandler's (only) method, this handler is registered in this.init())
	 * 
	 * @see org.geomajas.gwt.client.map.event.MapViewChangedHandler#onMapViewChanged
	 * 	(org.geomajas.gwt.client.map.event.MapViewChangedEvent)
	 */
	public void onMapViewChanged(MapViewChangedEvent event) {
		refreshPixelLength();
		// Note: if this.mapWidget is null, pixelLength must be valid
		if ( !Double.isNaN(pixelLength) && (0.0 != pixelLength)) {
			if (!isScaleItemInitialized) {
				scaleItem.clearValue();
				updateResolutions();
				setDisplayScale(mapView.getCurrentScale() * pixelLength);
			} else 	if (scaleItem.getValueAsString() == null || "".equals(scaleItem.getValueAsString())) {
				setDisplayScale(mapView.getCurrentScale() * pixelLength);
			} else if (event.isMapResized()) {
				updateScaleList();
				setDisplayScale(mapView.getCurrentScale() * pixelLength);
			} else if (!event.isSameScaleLevel() || scaleItem.getDisplayValue() == null
					|| "".equals(scaleItem.getDisplayValue())) {
				setDisplayScale(mapView.getCurrentScale() * pixelLength);
			}
		}
	}


	/**
	 * Make sure that the scale in the scale select is applied on the map, when the user presses the 'Enter' key.
	 * 
	 * @see com.smartgwt.client.widgets.form.fields.events.KeyPressHandler#onKeyPress
	 * 	(com.smartgwt.client.widgets.form.fields.events.KeyPressEvent)
	 */
	public void onKeyPress(KeyPressEvent event) {
		String name = event.getKeyName();
		if (KeyNames.ENTER.equals(name)) {
			reorderValues();
		}
	}

	/**
	 * When the user selects a different scale, have the map zoom to it.
	 * 
	 * @see com.smartgwt.client.widgets.form.fields.events.ChangedHandler#onChanged
	 * 			(com.smartgwt.client.widgets.form.fields.events.ChangedEvent)
	 */
	public void onChanged(ChangedEvent event) {
		String value = (String) scaleItem.getValue();

		Double scale = valueToScale.get(value);
		if (scale != null && !Double.isNaN(pixelLength) && 0.0 != pixelLength) {
			mapView.setCurrentScale(scale / pixelLength, MapView.ZoomOption.LEVEL_CLOSEST);
		}
	}

	// -------------------------------------------------------------------------
	// Private methods:
	// -------------------------------------------------------------------------

	protected void setDisplayScale(double scale) {
		scaleItem.setValue(ScaleConverter.scaleToString(scale, precision, significantDigits));
	}

	/**
	 * Given the full list of desirable resolutions, which ones are actually available? Update the widget accordingly.
	 */
	private void updateScaleList() {
		refreshPixelLength(); /* retrieve pixelLength from mapWidget. Needed in case mapWidget wasn't fully initialized
					the previous time the pixelLength was requested */

		// Update lookup map (stores user friendly representation):
		valueToScale.clear();
		if ( !Double.isNaN(pixelLength) && (0.0 != pixelLength)) {
			for (Double scale : scaleList) {
				// Eliminate duplicates and null:
				if (scale != null) {
					String value = ScaleConverter.scaleToString(scale, precision, significantDigits);
					valueToScale.put(value, scale);
				}
			}

			List<String> availableScales = new ArrayList<String>();

			for (Double scale : scaleList) {
				if (mapView.isResolutionAvailable(pixelLength / scale)) {
					availableScales.add(ScaleConverter.scaleToString(scale, precision, significantDigits));
				}
			}

			scaleItem.setValueMap(availableScales.toArray(new String[availableScales.size()]));

			isScaleItemInitialized = true;
		}
	}

	private void init() {
//		DynamicForm form = new DynamicForm();
		scaleItem = new ComboBoxItem();
		scaleItem.setTitle(I18nProvider.getToolbar().scaleSelectTitle());
		scaleItem.setValidators(new ScaleValidator());
		scaleItem.setValidateOnChange(true);
		scaleItem.addKeyPressHandler(this);
		scaleItem.addChangedHandler(this);
		setFields(scaleItem);
//		addChild(form);


		updateResolutions();	// Does nothing if mapWidget provided in constructor, hasn't been fully initialized yet.
							  		//	Else, will call setDisplayScale() if scaleItem value's hasn't been set yet
									//	or scaleItem's displayValue is null or empty
		if (null != mapView && isScaleItemInitialized) {
			setDisplayScale(mapView.getCurrentScale() * pixelLength);
		}
		mapView.addMapViewChangedHandler(this);
	}

	private void updateResolutions() {
		if (mapView.getResolutions() != null && !Double.isNaN(pixelLength) && 0.0 != pixelLength) {
			List<Double> scales = new ArrayList<Double>();
			for (Double resolution : mapView.getResolutions()) {
				scales.add(pixelLength / resolution);
			}
			setScales(scales.toArray(new Double[scales.size()]));
				// this will call updateScaleList() which will set isScaleItemInitialized
					// to true if scaleItem can be fully initialized

			if (null == scaleItem.getValue() || scaleItem.getValueAsString() == null
					|| "".equals(scaleItem.getValueAsString())
					) {
				setDisplayScale(mapView.getCurrentScale() * pixelLength);
			} else if (scaleItem.getDisplayValue() == null  || "".equals(scaleItem.getDisplayValue())) {
				setDisplayScale(mapView.getCurrentScale() * pixelLength);
			}
		}
	}

	private void reorderValues() {
		String value = (String) scaleItem.getValue();
		if (value != null) {
			Double scale = valueToScale.get(value);
			if (scale == null) {
				scale = ScaleConverter.stringToScale(value);
				if (updatingScaleList) {
					List<Double> newScales = new ArrayList<Double>(valueToScale.values());
					newScales.add(scale);
					setScales(newScales.toArray(new Double[newScales.size()]));
				}
			}
			scaleItem.setValue(ScaleConverter.scaleToString(scale, precision, significantDigits));
			mapView.setCurrentScale(scale / pixelLength, MapView.ZoomOption.LEVEL_CLOSEST);
		}
	}

	/**
	 * Custom validation of user entered scale
	 */
	private class ScaleValidator extends CustomValidator {

		@Override
		protected boolean condition(Object value) {
			try {
				return ScaleConverter.stringToScale((String) value) >= 0.0;
			} catch (NumberFormatException t) {
				return false;
			}
		}
	}

	private void refreshPixelLength() {
		if (null != this.mapWidget) {
			pixelLength = mapWidget.getPixelPerUnit();
			if (Double.isNaN(pixelLength) || (0.0 == pixelLength)) {
				isScaleItemInitialized = false;
			}
		}

	}

}
