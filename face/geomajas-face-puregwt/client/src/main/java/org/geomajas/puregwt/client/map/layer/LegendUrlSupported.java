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

package org.geomajas.puregwt.client.map.layer;

import org.geomajas.annotation.Api;
import org.geomajas.configuration.FontStyleInfo;
import org.geomajas.geometry.Bbox;

/**
 * Interface meant for layers that are able to provide a URL to their legend, such as WMS layers. The URL refers to an
 * image containing all legend icons and their labels for the currently active style of this layer.
 * 
 * @author An Buyle
 * @author Pieter De Graef
 * @since 1.0.0
 */
@Api(allMethods = true)
public interface LegendUrlSupported {

	/**
	 * Default font family used in legend text rendering.
	 */
	String DEFAULT_LEGEND_FONT_FAMILY = "arial";
	
	/**
	 * Default font size used for legend style labels.
	 */
	int DEFAULT_LEGEND_STYLE_LABEL_FONT_SIZE = 13;

	/**
	 * Default font color used in legend text rendering.
	 */
	String DEFAULT_LEGEND_FONT_COLOR = "0x000000";

	/**
	 * Check if the layer supports dynamic legend rendering. 
	 * 
	 * @return true if dynamic legend rendering is supported, else false 
	 */
	boolean hasSupportForDynamicLegend();
	
	/**
	 * Get the URL by which the legend image for this layer can be retrieved.
	 * The format will be PNG.
	 * Since no filter/bounding box argument is specified, the full legend (aka static legend)
	 * will be generated.   
	 * 
	 * @return Returns the default URL to the legend image or warning.
	 */
	String getLegendImageUrl();

	/**
	 * Get the URL by which the legend image for the concerning layer can be retrieved.
	 * Since no filter/bounding box argument is specified, the full legend (aka static legend)
	 * will be generated.   
	 * 
	 * @param width
	 *            width in pixels of the icon for a legend symbol (if 0, the default is used)
	 * @param height
	 *            height in pixels of the icon for a legend symbol (if 0, the default is used)
	 * @param fontStyle 
	 *            hint for the font style (not all implementations take this parameter into account); if
	 *            null, the default is used.
	 *            
	 *            If true (default) the URL will generate a legend image even when there are no style rules active
	 *            (e.g. the layer is invisible for the specified scale). Then the image will contain a warning message.
	 *            If false, no image will be generated in that case, but a textual warning message will be generated.
	 *             
	 * @param imageFormat
	 *            Hint for the image format of the legend; if null it the default of the implementing layer 
	 *            will be used. This can for example depend on the WMS server if it is a WMS client layer.  
	 *            The implementor can choose to support only certain formats, but PNG should always be supported.
	 *            
	 * @return URL for the legend image e.g. "http://www.myserver.org/geoserver/wms?service=WMS&layer=countries
	 *         :population&request=GetLegendGraphic&format=image/png&width=20&height =20&transparent=true"
	 */

	String getLegendImageUrl(int width, int height, FontStyleInfo fontStyle, String imageFormat);
	
	/**
	 * Get the URL by which the legend image for the concerning layer can be retrieved.
	 * Since no filter/bounding box argument is specified, the full legend (aka static legend)
	 * will be generated. 
	 * 
	 * @param iconWidth
	 *            width in pixels of the icon for a legend symbol (if 0, the default is used)
	 * @param iconHeight
	 *            height in pixels of the icon for a legend symbol (if 0, the default is used)
	 * @param fontStyle 
	 *            hint for the font style (not all implementations take this parameter into account); if
	 *            null, the default is used.
	 *            
	 *            If true (default) the URL will generate a legend image even when there are no style rules active
	 *            (e.g. the layer is invisible for the specified scale). Then the image will contain a warning message.
	 *            If false, no image will be generated in that case, but a textual warning message will be generated.
	 *             
	 * @param imageFormat
	 *            Hint for the image format of the legend; if null it the default of the implementing layer 
	 *            will be used. This can for example depend on the WMS server if it is a WMS client layer.  
	 *            The implementor can choose to support only certain formats, but PNG should always be supported.
	 *
	 * @param imageForWarning
	 *            If true (default) the URL will generate an image even for some special cases like
	 *            errors.
	 * 
	 * @return URL for the legend image e.g. "http://www.myserver.org/geoserver/wms?service=WMS&layer=countries
	 *         :population&request=GetLegendGraphic&format=image/png&width=20&height =20&transparent=true"
	 */
	String getLegendImageUrl(int iconWidth, int iconHeight, FontStyleInfo fontStyle, String imageFormat, 
			boolean imageForWarning);
	
	/**
	 * Get the URL by which the legend image for the concerning layer can be retrieved.
	 * When a bounding box is specified (not null) and no style rules are active 
	 * (e.g. the layer is invisible for the specified scale), no image will be generated in 
	 * that case, but a textual warning message will be generated when the parameter imageForWarning 
	 * is set to false.
	 * 
	 * @param iconWidth
	 *            width in pixels of the icon for a legend symbol (if 0, the default is used)
	 * @param iconHeight
	 *            height in pixels of the icon for a legend symbol (if 0, the default is used)
	 * @param fontStyle 
	 *            hint for the font style (not all implementations take this parameter into account); if
	 *            null, the default is used.
	 *            
	 *            If true (default) the URL will generate a legend image even when there are no style rules active
	 *            (e.g. the layer is invisible for the specified scale). Then the image will contain a warning message.
	 *            If false, no image will be generated in that case, but a textual warning message will be generated.
	 *             
	 * @param imageFormat
	 *            Hint for the image format of the legend; if null it the default of the implementing layer 
	 *            will be used. This can for example depend on the WMS server if it is a WMS client layer.  
	 *            The implementor can choose to support only certain formats, but PNG should always be supported.
	 *            
	 *
	 * @param imageForWarning
	 *            If true (default) the URL will generate an image even in some special cases like
	 *            warnings or errors. E.g. when there are no style rules active
	 *            (e.g. the layer is invisible for the specified scale). Then the image will contain a warning message.
	 *            If false, no image will be generated in that case, but a textual warning will be generated.
	 *
	 * @param bounds
	 *            Bounding box of the extend of interest on the layer, if null the full layer extend is of interest.
	 *            In other words the full or static legend will be referenced by the URL. 
	 *            
	 * 
	 * @return URL for the legend image e.g. "http://www.myserver.org/geoserver/wms?service=WMS&layer=countries
	 *         :population&request=GetLegendGraphic&format=image/png&width=20&height =20&transparent=true"
	 */
	String getLegendImageUrl(int iconWidth, int iconHeight, FontStyleInfo fontStyle, String imageFormat,
			boolean imageForWarning, Bbox bounds);

}