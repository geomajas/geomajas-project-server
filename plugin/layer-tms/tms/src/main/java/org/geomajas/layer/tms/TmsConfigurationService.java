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

package org.geomajas.layer.tms;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.geomajas.configuration.RasterLayerInfo;
import org.geomajas.configuration.client.ScaleInfo;
import org.geomajas.geometry.Bbox;
import org.geomajas.layer.LayerType;
import org.geomajas.layer.common.proxy.LayerHttpService;
import org.geomajas.layer.tms.xml.BoundingBox;
import org.geomajas.layer.tms.xml.TileMap;
import org.geomajas.layer.tms.xml.TileSet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Configuration service for TMS layers. Based upon the base TMS URL, it can retrieve the TMS configuration and present
 * it as a Java object. This service can also transform this TMS configuration object into a {@link RasterLayerInfo}
 * object.
 * 
 * @author Pieter De Graef
 */
@Component
public class TmsConfigurationService {

	private static final String CLASSPATH = "classpath:";

	@Autowired
	private LayerHttpService httpService;

	/**
	 * Get the configuration for a TMS layer by retrieving and parsing it's XML description file. The parsing is done
	 * using JaxB.
	 * @param layer the tms layer to get capabilities for. 
	 * @return Returns the description as a Java configuration object.
	 * @throws TmsLayerException
	 *             In case something went wrong trying to find or parse the XML description file.
	 */
	public TileMap getCapabilities(TmsLayer layer) throws TmsLayerException {
		try {
			// Create a JaxB unmarshaller:
			JAXBContext context = JAXBContext.newInstance(TileMap.class);
			Unmarshaller um = context.createUnmarshaller();

			// Find out where to retrieve the capabilities and unmarshall:
			if (layer.getBaseTmsUrl().startsWith(CLASSPATH)) {
				String location = layer.getBaseTmsUrl().substring(CLASSPATH.length());
				if (location.length() > 0 && location.charAt(0) == '/') {
					// classpath resources should not start with a slash, but they often do
					location = location.substring(1);
				}
				ClassLoader cl = Thread.currentThread().getContextClassLoader();
				if (null == cl) {
					cl = getClass().getClassLoader(); // NOSONAR fallback from proper behaviour for some environments
				}
				InputStream is = cl.getResourceAsStream(location);
				if (null != is) {
					try {
						return (TileMap) um.unmarshal(is);
					} finally {
						try {
							is.close();
						} catch (IOException ioe) {
							// ignore, just closing the stream
						}
					}
				}
				throw new TmsLayerException(TmsLayerException.COULD_NOT_FIND_FILE, layer.getBaseTmsUrl());
			}

			// Normal case, find the URL and unmarshal:
			return (TileMap) um.unmarshal(httpService.getStream(layer.getBaseTmsUrl(), layer));
		} catch (JAXBException e) {
			throw new TmsLayerException(e, TmsLayerException.COULD_NOT_READ_FILE, layer.getBaseTmsUrl());
		} catch (MalformedURLException e) {
			throw new TmsLayerException(e, TmsLayerException.COULD_NOT_FIND_FILE, layer.getBaseTmsUrl());
		} catch (IOException e) {
			throw new TmsLayerException(e, TmsLayerException.COULD_NOT_READ_FILE, layer.getBaseTmsUrl());
		}
	}

	/**
	 * Transform a TMS layer description object into a raster layer info object.
	 * 
	 * @param tileMap
	 *            The TMS layer description object.
	 * @return The raster layer info object as used by Geomajas.
	 */
	public RasterLayerInfo asLayerInfo(TileMap tileMap) {
		RasterLayerInfo layerInfo = new RasterLayerInfo();

		layerInfo.setCrs(tileMap.getSrs());
		layerInfo.setDataSourceName(tileMap.getTitle());
		layerInfo.setLayerType(LayerType.RASTER);
		layerInfo.setMaxExtent(asBbox(tileMap.getBoundingBox()));
		layerInfo.setTileHeight(tileMap.getTileFormat().getHeight());
		layerInfo.setTileWidth(tileMap.getTileFormat().getWidth());

		List<ScaleInfo> zoomLevels = new ArrayList<ScaleInfo>(tileMap.getTileSets().getTileSets().size());
		for (TileSet tileSet : tileMap.getTileSets().getTileSets()) {
			zoomLevels.add(asScaleInfo(tileSet));
		}
		layerInfo.setZoomLevels(zoomLevels);

		return layerInfo;
	}

	/**
	 * Transforms a TMS bounding box information object into a Geomajas {@link Bbox} object.
	 * 
	 * @param boundingBox
	 *            The TMS bounding box object.
	 * @return The default Geomajas bounding box.
	 */
	public Bbox asBbox(BoundingBox boundingBox) {
		double width = boundingBox.getMaxX() - boundingBox.getMinX();
		double height = boundingBox.getMaxY() - boundingBox.getMinY();
		return new Bbox(boundingBox.getMinX(), boundingBox.getMinY(), width, height);
	}

	/**
	 * Transforms a TMS tile-set description object into a Geomajas {@link ScaleInfo} object.
	 * 
	 * @param tileSet
	 *            The tile set description.
	 * @return The default Geomajas scale object.
	 */
	public ScaleInfo asScaleInfo(TileSet tileSet) {
		ScaleInfo scaleInfo = new ScaleInfo();
		scaleInfo.setPixelPerUnit(1 / tileSet.getUnitsPerPixel());
		return scaleInfo;
	}
}