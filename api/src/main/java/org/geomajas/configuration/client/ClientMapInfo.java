/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2015 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */
package org.geomajas.configuration.client;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.constraints.NotNull;

import org.geomajas.annotation.Api;
import org.geomajas.configuration.FeatureStyleInfo;
import org.geomajas.geometry.Bbox;

/**
 * Map configuration.
 * <p>
 * Since version 1.10.0 this configuration object implements {@link ClientWidgetInfo}, so that it can be easily
 * retrieved like any other widget configuration object.
 * </p>
 * 
 * @author Joachim Van der Auwera
 * @since 1.6.0
 */
@Api(allMethods = true)
public class ClientMapInfo implements ClientWidgetInfo {

	private static final long serialVersionUID = 151L;

	private String id;

	private String backgroundColor;

	private FeatureStyleInfo lineSelectStyle = new FeatureStyleInfo();

	private FeatureStyleInfo pointSelectStyle = new FeatureStyleInfo();

	private FeatureStyleInfo polygonSelectStyle = new FeatureStyleInfo();

	@NotNull
	private String crs;

	private int precision = 2;

	private boolean scaleBarEnabled;

	private boolean panButtonsEnabled;

	private UnitType displayUnitType = UnitType.METRIC;

	private ScaleConfigurationInfo scaleConfiguration = new ScaleConfigurationInfo();

	private boolean resolutionsRelative;

	private List<Double> resolutions = new ArrayList<Double>();

	@NotNull
	private Bbox initialBounds;

	private List<ClientLayerInfo> layers = new ArrayList<ClientLayerInfo>();

	private ClientLayerTreeInfo layerTree;

	private double unitLength = 1.0;

	private double pixelLength;

	private ClientToolbarInfo toolbar;

	private Bbox maxBounds = Bbox.ALL;
	
	private BoundsLimitOption viewBoundsLimitOption = BoundsLimitOption.COMPLETELY_WITHIN_MAX_BOUNDS;

	private Map<String, ClientWidgetInfo> widgetInfo = new HashMap<String, ClientWidgetInfo>();

	private ClientUserDataInfo userData;

	private ClientPreferredPixelsPerTile preferredPixelsPerTile = new ClientPreferredPixelsPerTile();

	
	/**
	 * Get the id of this map. This id is unique within the application.
	 * 
	 * @return the unique id
	 */
	public String getId() {
		return id;
	}

	/**
	 * Set the id of this map (auto-set by spring). The id must not be globally unique, but must be unique within the
	 * application.
	 * 
	 * @param id
	 *            id
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * Gets the background color of the map, expressed as a hexadecimal string (eg '#FFFFFF').
	 * 
	 * @return the hex string of the background color
	 */
	public String getBackgroundColor() {
		return backgroundColor;
	}

	/**
	 * Set the background color of this map, expressed as a hexadecimal string (eg '#FFFFFF').
	 * 
	 * @param backgroundColor
	 *            the hex string of the background color
	 */
	public void setBackgroundColor(String backgroundColor) {
		this.backgroundColor = backgroundColor;
	}

	/**
	 * Get the line selection style for this map.
	 * 
	 * @return the line selection style
	 */
	public FeatureStyleInfo getLineSelectStyle() {
		return lineSelectStyle;
	}

	/**
	 * Set the line selection style for this map.
	 * 
	 * @param lineSelectStyle
	 *            line selection style
	 */
	public void setLineSelectStyle(FeatureStyleInfo lineSelectStyle) {
		this.lineSelectStyle = lineSelectStyle;
	}

	/**
	 * Get the point selection style for this map.
	 * 
	 * @return the point selection style
	 */
	public FeatureStyleInfo getPointSelectStyle() {
		return pointSelectStyle;
	}

	/**
	 * Set the point selection style for this map.
	 * 
	 * @param pointSelectStyle
	 *            point selection style
	 */
	public void setPointSelectStyle(FeatureStyleInfo pointSelectStyle) {
		this.pointSelectStyle = pointSelectStyle;
	}

	/**
	 * Get the polygon selection style for this map.
	 * 
	 * @return the polygon selection style
	 */
	public FeatureStyleInfo getPolygonSelectStyle() {
		return polygonSelectStyle;
	}

	/**
	 * Set the polygon selection style for this map.
	 * 
	 * @param polygonSelectStyle
	 *            polygon selection style
	 */
	public void setPolygonSelectStyle(FeatureStyleInfo polygonSelectStyle) {
		this.polygonSelectStyle = polygonSelectStyle;
	}

	/**
	 * Get the coordinate reference system of this map (SRS notation).
	 * 
	 * @return the CRS (SRS notation)
	 */
	public String getCrs() {
		return crs;
	}

	/**
	 * Set the coordinate reference system of this map (SRS notation).
	 * 
	 * @param crs
	 *            the CRS (SRS notation)
	 */
	public void setCrs(String crs) {
		this.crs = crs;
	}

	/**
	 * Get the precision of this map (number of decimals).
	 * 
	 * @return the precision of this map (number of decimals)
	 */
	public int getPrecision() {
		return precision;
	}

	/**
	 * Set the precision of this map (number of decimals).
	 * 
	 * @param precision
	 *            the precision of this map (number of decimals)
	 */
	public void setPrecision(int precision) {
		this.precision = precision;
	}

	/**
	 * Should this map show a scalebar ?
	 * 
	 * @return true if shown
	 */
	public boolean isScaleBarEnabled() {
		return scaleBarEnabled;
	}

	/**
	 * Enable the scalebar for this map.
	 * 
	 * @param scaleBarEnabled
	 *            true if enabled (shown)
	 */
	public void setScaleBarEnabled(boolean scaleBarEnabled) {
		this.scaleBarEnabled = scaleBarEnabled;
	}

	/**
	 * Should this map show pan buttons ?
	 * 
	 * @return true if shown
	 */
	public boolean isPanButtonsEnabled() {
		return panButtonsEnabled;
	}

	/**
	 * Enable the pan buttons for this map.
	 * 
	 * @param panButtonsEnabled
	 *            true if enabled (shown)
	 */
	public void setPanButtonsEnabled(boolean panButtonsEnabled) {
		this.panButtonsEnabled = panButtonsEnabled;
	}

	/**
	 * Returns the display unit type of this map. This unit type will be used in eg the scalebar.
	 * 
	 * @return the display unit type
	 */
	public UnitType getDisplayUnitType() {
		return displayUnitType;
	}

	/**
	 * Set the display unit type of this map.
	 * 
	 * @param displayUnitType
	 *            unit type of this map
	 */
	public void setDisplayUnitType(UnitType displayUnitType) {
		this.displayUnitType = displayUnitType;
	}

	/**
	 * Retruns the scale configuration of this map.
	 * 
	 * @return scale configuration.
	 * @since 1.7.0
	 */
	public ScaleConfigurationInfo getScaleConfiguration() {
		return scaleConfiguration;
	}

	/**
	 * Sets the scale configuration of this map.
	 * 
	 * @param scaleConfiguration
	 *            the scale configuration
	 * @since 1.7.0
	 */
	public void setScaleConfiguration(ScaleConfigurationInfo scaleConfiguration) {
		this.scaleConfiguration = scaleConfiguration;
	}

	/**
	 * Returns the maximum scale (maximum zoom in) of this map. The minimum scale is indirectly determined from the
	 * maximum bounds. As a better alternative for this value, use {@link #getScaleConfiguration}.
	 * 
	 * @return the maximum scale (pixels/unit)
	 * @deprecated use {@link #getScaleConfiguration}
	 */
	@Deprecated
	public float getMaximumScale() {
		return (float) getScaleConfiguration().getMaximumScale().getPixelPerUnit();
	}

	/**
	 * Set maximum scale (maximum zoom in) of this map. As a better alternative for this value, use
	 * {@link #getScaleConfiguration}.
	 * 
	 * @param maximumScale
	 *            the maximum scale (pixels/unit)
	 * @deprecated use {@link #setScaleConfiguration}
	 */
	@Deprecated
	public void setMaximumScale(float maximumScale) {
		getScaleConfiguration().setMaximumScale(new ScaleInfo((double) maximumScale));
	}

	/**
	 * Returns the startup bounds/extent of this map. Warning: the map's view will be fitted to the initial bounds, but
	 * the view's aspect ratio will not be affected !
	 * 
	 * @return the initial bounds
	 */
	public Bbox getInitialBounds() {
		return initialBounds;
	}

	/**
	 * Set the startup bounds/extent of this map. Warning: the map's view will be fitted to the initial bounds, but the
	 * view's aspect ratio will not be affected !
	 * 
	 * @param initialBounds
	 *            initial bounds
	 */
	public void setInitialBounds(Bbox initialBounds) {
		this.initialBounds = initialBounds;
	}

	/**
	 * Returns the list of resolutions (inverse scale values) allowed by this map. This determines the predefined scale
	 * levels at which this map will be shown. If this list is non-empty, the map will not adjust to arbitrary scale
	 * levels but will instead snap to one of the scale levels defined in this list when zooming.
	 * 
	 * @deprecated use {@link #getScaleConfiguration()}
	 * @return a list of resolutions (unit/pixel or pure number if relative)
	 */
	@Deprecated
	public List<Double> getResolutions() {
		return resolutions;
	}

	/**
	 * Sets the list of resolutions (inverse scale values) allowed by this map. This determines the predefined scale
	 * levels at which this map will be shown. If this list is non-empty, the map will not adjust to arbitrary scale
	 * levels but will instead snap to one of the scale levels defined in this list when zooming.
	 * 
	 * @param resolutions
	 *            a list of resolutions (unit/pixel or pure number if relative)
	 * @deprecated use {@link #setScaleConfiguration}
	 */
	@Deprecated
	public void setResolutions(List<Double> resolutions) {
		this.resolutions = resolutions;
	}

	/**
	 * Are the resolutions relative ? If true, the resolutions are expressed as pure numbers and denote the ratio of the
	 * map unit and 1 m on the screen (as computed from the screen DPI).
	 * 
	 * @deprecated use {@link #getScaleConfiguration}
	 * @return true if relative
	 */
	@Deprecated
	public boolean isResolutionsRelative() {
		return resolutionsRelative;
	}

	/**
	 * If true, the resolutions are expressed as pure numbers and denote the ratio of the map unit and 1 m on the screen
	 * (as computed from the screen dpi).
	 * 
	 * @param resolutionsRelative
	 *            true if relative
	 * @deprecated use {@link #setScaleConfiguration}
	 */
	@Deprecated
	public void setResolutionsRelative(boolean resolutionsRelative) {
		this.resolutionsRelative = resolutionsRelative;
	}

	/**
	 * Get the layer tree that belongs with this map.
	 * 
	 * @return the layer tree
	 */
	public ClientLayerTreeInfo getLayerTree() {
		return layerTree;
	}

	/**
	 * Set layer tree that belongs with this map.
	 * 
	 * @param layerTree
	 *            the layer tree
	 */
	public void setLayerTree(ClientLayerTreeInfo layerTree) {
		this.layerTree = layerTree;
	}

	/**
	 * Get the tool bar that belongs with this map.
	 * 
	 * @return the tool bar
	 */
	public ClientToolbarInfo getToolbar() {
		return toolbar;
	}

	/**
	 * Set tool bar that belongs with this map.
	 * 
	 * @param toolbar
	 *            the layer tree
	 */
	public void setToolbar(ClientToolbarInfo toolbar) {
		this.toolbar = toolbar;
	}

	/**
	 * Get the list of layers of this map. The order is the drawing order.
	 * 
	 * @return the layers
	 */
	public List<ClientLayerInfo> getLayers() {
		return layers;
	}

	/**
	 * Set the list of layers of this map. The order is the drawing order.
	 * 
	 * @param layers
	 *            the layers
	 */
	public void setLayers(List<ClientLayerInfo> layers) {
		this.layers = layers;
	}

	/**
	 * Get the unit length of this map in actual meters. This is an approximate value in the horizontal direction and in
	 * the initial center of the map.
	 * 
	 * @return unit length in m
	 */
	public double getUnitLength() {
		return unitLength;
	}

	/**
	 * Set the unit length of the map (auto-set by Spring).
	 * 
	 * @param unitLength
	 *            unit length in m
	 */
	public void setUnitLength(double unitLength) {
		this.unitLength = unitLength;
	}

	/**
	 * Get the length in meters of a pixel on the map.
	 * 
	 * @return length in meters of a pixel on the map (measured at the center of the map)
	 */
	public double getPixelLength() {
		return pixelLength;
	}

	/**
	 * Set the unit length in meters of a pixel the map (auto-set by Spring).
	 * 
	 * @param pixelLength
	 *            length of pixel in m
	 */
	public void setPixelLength(double pixelLength) {
		this.pixelLength = pixelLength;
	}

	/**
	 * Returns the maximum bounds/extent of this map.
	 * 
	 * @return the maximum bounds
	 */
	public Bbox getMaxBounds() {
		return maxBounds;
	}

	/**
	 * Sets the maximum bounds/extent of this map.
	 * 
	 * @param maxBounds
	 *            the maximum bounds
	 */
	public void setMaxBounds(Bbox maxBounds) {
		this.maxBounds = maxBounds;
	}
	
	/**
	 * Get configuration option for limiting the mapview's bounds.
	 * 
	 * @return BoundsLimitOption
	 * 					The current value of the configuration item for limiting the mapview's bounds
	 * 					when applying the maxBounds limitation.
	 * @since 1.10.0
	 */
	public BoundsLimitOption getViewBoundsLimitOption() {
		return viewBoundsLimitOption;
	}

	/**
	 * Sets the configuration option for limiting the mapview's bounds.
	 * 
	 * @param viewBoundsLimitOption 	
	 * 					The desired value of the configuration item for limiting the mapview's bounds 
	 * 					when applying the maxBounds limitation.
	 * @since 1.10.0
	 */
	public void setViewBoundsLimitOption(BoundsLimitOption viewBoundsLimitOption) {
		this.viewBoundsLimitOption = viewBoundsLimitOption;
	}

	/**
	 * Get configuration for (custom) map widgets.
	 * 
	 * @return map keyed on widget id containing widget configurations
	 * @since 1.8.0
	 */
	@Api
	public Map<String, ClientWidgetInfo> getWidgetInfo() {
		return widgetInfo;
	}

	/**
	 * Get configuration for a (custom) map widgets.
	 * 
	 * @param widget
	 *            widget key
	 * @return widget configuration
	 * @since 1.8.0
	 */
	@Api
	public ClientWidgetInfo getWidgetInfo(String widget) {
		return widgetInfo.get(widget);
	}

	/**
	 * Set configuration for (custom) map widgets.
	 * 
	 * @param widgetInfo
	 *            map keyed on widget id containing widget configurations
	 * @since 1.8.0
	 */
	public void setWidgetInfo(Map<String, ClientWidgetInfo> widgetInfo) {
		this.widgetInfo = widgetInfo;
	}

	/**
	 * Get the custom configuration data. The idea is that you extend this object, so that you can store any information
	 * you want.
	 * 
	 * @return custom configuration data
	 */
	public ClientUserDataInfo getUserData() {
		return userData;
	}

	/**
	 * Set the custom configuration data you wish to pass to the client. The idea is that you extend this object, so
	 * that you can store any information you want.
	 * 
	 * @param userData
	 *            Custom configuration data
	 */
	public void setUserData(ClientUserDataInfo userData) {
		this.userData = userData;
	}

	/**
	 * Set the preferred pixels per tile size you wish the client will fetch from the server. This allows some tweaks
	 * for performance. The default is closest to 256x256 pixels.
	 * 
	 * @param preferredPixelsPerTile
	 *            the preferredPixelsPerTile to set
	 * @since 1.9.0
	 */
	@Api
	public void setPreferredPixelsPerTile(ClientPreferredPixelsPerTile preferredPixelsPerTile) {
		this.preferredPixelsPerTile = preferredPixelsPerTile;
	}

	/**
	 * Get the preferred pixels per tile size you wish the client will fetch from the server. This allows some tweaks
	 * for performance. The default is closest to 256x256 pixels.
	 *
	 * @return the preferredPixelsPerTile
	 * @since 1.9.0
	 */
	@Api
	public ClientPreferredPixelsPerTile getPreferredPixelsPerTile() {
		return preferredPixelsPerTile;
	}

	@Override
	public String toString() {
		return "ClientMapInfo{" +
				"id='" + id + '\'' +
				", backgroundColor='" + backgroundColor + '\'' +
				", lineSelectStyle=" + lineSelectStyle +
				", pointSelectStyle=" + pointSelectStyle +
				", polygonSelectStyle=" + polygonSelectStyle +
				", crs='" + crs + '\'' +
				", precision=" + precision +
				", scaleBarEnabled=" + scaleBarEnabled +
				", panButtonsEnabled=" + panButtonsEnabled +
				", displayUnitType=" + displayUnitType +
				", scaleConfiguration=" + scaleConfiguration +
				", resolutionsRelative=" + resolutionsRelative +
				", resolutions=" + resolutions +
				", initialBounds=" + initialBounds +
				", layers=" + layers +
				", layerTree=" + layerTree +
				", unitLength=" + unitLength +
				", pixelLength=" + pixelLength +
				", toolbar=" + toolbar +
				", maxBounds=" + maxBounds +
				", viewBoundsLimitOption=" + viewBoundsLimitOption +
				", widgetInfo=" + widgetInfo +
				", userData=" + userData +
				", preferredPixelsPerTile=" + preferredPixelsPerTile +
				'}';
	}
}
