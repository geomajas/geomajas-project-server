/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2014 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */
package org.geomajas.configuration.client;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;

import org.geomajas.annotation.Api;
import org.geomajas.configuration.IsInfo;
import org.geomajas.configuration.LayerInfo;
import org.geomajas.geometry.Bbox;
import org.geomajas.layer.LayerType;

/**
 * Abstract class that contains the metadata properties that are common to all client layers. The coupling with the
 * server layer is established via the <code>layerInfo</code> field.
 * 
 * @author Jan De Moerloose
 * @author Oliver May
 * @since 1.6.0
 */
@Api(allMethods = true)
public abstract class ClientLayerInfo implements IsInfo {

	private static final long serialVersionUID = 151L;

	@NotNull
	private String label;

	private boolean visible;

	private ScaleInfo minimumScale = new ScaleInfo(ScaleInfo.MINIMUM_PIXEL_PER_UNIT);

	private ScaleInfo maximumScale = new ScaleInfo(ScaleInfo.MAXIMUM_PIXEL_PER_UNIT);

	private ScaleInfo zoomToPointScale;

	@Null
	private LayerInfo layerInfo;

	private Bbox maxExtent;

	private String id;

	@NotNull
	private String serverLayerId;

	private Map<String, ClientWidgetInfo> widgetInfo = new HashMap<String, ClientWidgetInfo>();

	private ClientUserDataInfo userData;
	
	/**
	 * Get the unique id of this layer.
	 * 
	 * @return the unique id
	 */
	public String getId() {
		return id;
	}

	/**
	 * Set the unique id of this layer (auto-copied from Spring context).
	 * 
	 * @param id
	 *            id
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * Get the layer id of the server layer.
	 * 
	 * @return layer id
	 */
	public String getServerLayerId() {
		return serverLayerId;
	}

	/**
	 * Set the server layer id. This should be the name of an existing {@link org.geomajas.layer.Layer} bean.
	 * 
	 * @param serverLayerId
	 *            server layer id
	 */
	public void setServerLayerId(String serverLayerId) {
		this.serverLayerId = serverLayerId;
	}

	/**
	 * Get the user label of this layer.
	 * 
	 * @return layer id
	 */
	public String getLabel() {
		return label;
	}

	/**
	 * Set the user label of this layer.
	 * 
	 * @param label
	 *            label
	 */
	public void setLabel(String label) {
		this.label = label;
	}

	/**
	 * Is this layer be visible at startup ?
	 * 
	 * @return true if visible
	 */
	public boolean isVisible() {
		return visible;
	}

	/**
	 * Set this layer visible at startup.
	 * 
	 * @param visible
	 *            visible status
	 */
	public void setVisible(boolean visible) {
		this.visible = visible;
	}

	/**
	 * Get the minimum scale for which this layer should be visible (maximum zoom out).
	 * 
	 * @return minimum scale (pix/map unit)
	 * @deprecated use {@link #getMinimumScale()}
	 */
	@Deprecated
	public double getViewScaleMin() {
		return minimumScale.getPixelPerUnit();
	}

	/**
	 * Set the minimum scale for which this layer should be visible (maximum zoom out).
	 * 
	 * @param viewScaleMin
	 *            scale (pix/map unit)
	 * @deprecated use {@link #setMinimumScale(ScaleInfo)}}
	 */
	@Deprecated
	public void setViewScaleMin(double viewScaleMin) {
		setMinimumScale(new ScaleInfo(viewScaleMin));
	}

	/**
	 * Get the maximum scale for which this layer should be visible (maximum zoom in).
	 * 
	 * @return maximum scale (pix/map unit)
	 * @deprecated use {@link #getMaximumScale()}
	 */
	@Deprecated
	public double getViewScaleMax() {
		return maximumScale.getPixelPerUnit();
	}

	/**
	 * Set the maximum scale for which this layer should be visible (maximum zoom in).
	 * 
	 * @param viewScaleMax
	 *            scale (pix/map unit)
	 * @deprecated use {@link #setMaximumScale(ScaleInfo)}
	 */
	@Deprecated
	public void setViewScaleMax(double viewScaleMax) {
		setMaximumScale(new ScaleInfo(viewScaleMax));
	}

	/**
	 * Get the minimum scale for which this layer should be visible (maximum zoom out).
	 * 
	 * @return the minimum scale (unit type is defined in )
	 * @since 1.7.0
	 */
	public ScaleInfo getMinimumScale() {
		return minimumScale;
	}

	/**
	 * Set the minimum scale for which this layer should be visible (maximum zoom out).
	 * 
	 * @param minimumScale
	 *            the minimum scale
	 * @since 1.7.0
	 */
	public void setMinimumScale(ScaleInfo minimumScale) {
		this.minimumScale = minimumScale;
	}

	/**
	 * Get the maximum scale for which this layer should be visible (maximum zoom in).
	 * 
	 * @return the maximum scale
	 * @since 1.7.0
	 */
	public ScaleInfo getMaximumScale() {
		return maximumScale;
	}

	/**
	 * Set the maximum scale for which this layer should be visible (maximum zoom in).
	 * 
	 * @param maximumScale
	 *            the maximum scale
	 * @since 1.7.0
	 */
	public void setMaximumScale(ScaleInfo maximumScale) {
		this.maximumScale = maximumScale;
	}
	
	/**
	 * Get the scale that should be used when zooming in on a point feature. Defaults to the {@link #maximumScale} of
	 * the layer.
	 * 
	 * @return the scale
	 * @since 1.11.0
	 */
	public ScaleInfo getZoomToPointScale() {
		return zoomToPointScale;
	}

	/**
	 * Set the scale that should be used when zooming in on a point feature. Defaults to the {@link #maximumScale} of
	 * the layer.
	 * 
	 * @param zoomToPointScale the scale
	 * @since 1.11.0
	 */
	public void setZoomToPointScale(ScaleInfo zoomToPointScale) {
		this.zoomToPointScale = zoomToPointScale;
	}

	/**
	 * Get the maximum visible extent of this layer (in map space).
	 * 
	 * @return maximum visible extent
	 */
	public Bbox getMaxExtent() {
		return maxExtent;
	}

	/**
	 * Set the maximum visible extent of this layer in map space (auto-set by Spring).
	 * 
	 * @param maxExtent
	 *            visible extent
	 */
	public void setMaxExtent(Bbox maxExtent) {
		this.maxExtent = maxExtent;
	}

	/**
	 * Get the layer information of this layer.
	 * 
	 * @return layer information
	 */
	public LayerInfo getLayerInfo() {
		return layerInfo;
	}

	/**
	 * Set the layer information of this layer (auto-copied from Spring context).
	 * 
	 * @param layerInfo
	 *            information
	 */
	public void setLayerInfo(LayerInfo layerInfo) {
		this.layerInfo = layerInfo;
	}

	/**
	 * Get the type of this layer (same as server layer).
	 * 
	 * @return layer type
	 */
	public LayerType getLayerType() {
		return layerInfo.getLayerType();
	}

	/**
	 * Get the crs of this layer (same as server layer).
	 * 
	 * @return crs
	 */
	public String getCrs() {
		return layerInfo.getCrs();
	}

	/**
	 * Get configuration for (custom) layer widgets.
	 *
	 * @return map keyed on widget id containing widget configurations
	 * @since 1.8.0
	 */
	@Api
	public Map<String, ClientWidgetInfo> getWidgetInfo() {
		return widgetInfo;
	}

	/**
	 * Get configuration for a (custom) layer widgets.
	 *
	 * @param widget widget key
	 * @return widget configuration
	 * @since 1.8.0
	 */
	@Api
	public ClientWidgetInfo getWidgetInfo(String widget) {
		return widgetInfo.get(widget);
	}

	/**
	 * Set configuration for (custom) layer widgets.
	 *
	 * @param widgetInfo map keyed on widget id containing widget configurations
	 * @since 1.8.0
	 */
	public void setWidgetInfo(Map<String, ClientWidgetInfo> widgetInfo) {
		this.widgetInfo = widgetInfo;
	}

	/**
	 * Get the custom configuration data.
	 * 
	 * @return custom configuration data
	 */
	public ClientUserDataInfo getUserData() {
		return userData;
	}

	/**
	 * Set the custom configuration data you wish to pass to the client.
	 * 
	 * @param userData
	 *            Custom configuration data
	 */
	public void setUserData(ClientUserDataInfo userData) {
		this.userData = userData;
	}
	
	/** Finish configuration. */
	@PostConstruct
	protected void postConstruct() {
		if (null == zoomToPointScale) {
			zoomToPointScale = new ScaleInfo(maximumScale);
		}		
	}

}
