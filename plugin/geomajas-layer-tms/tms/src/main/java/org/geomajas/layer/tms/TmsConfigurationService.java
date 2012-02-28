/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2012 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */

package org.geomajas.layer.tms;

import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.geomajas.configuration.RasterLayerInfo;
import org.geomajas.configuration.client.ScaleInfo;
import org.geomajas.geometry.Bbox;
import org.geomajas.layer.LayerType;
import org.geomajas.layer.tms.configuration.BoundingBoxInfo;
import org.geomajas.layer.tms.configuration.TileMapInfo;
import org.geomajas.layer.tms.configuration.TileSetInfo;
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

	/**
	 * Get the configuration for a TMS layer by retrieving and parsing it's XML description file. The parsing is done
	 * using JaxB.
	 * 
	 * @param layerCapabilitiesUrl
	 *            The TMS base URL where to find the description.
	 * @return Returns the description as a Java configuration object.
	 * @throws TmsConfigurationException
	 *             In case something went wrong trying to find or parse the XML description file.
	 */
	public TileMapInfo getCapabilities(String layerCapabilitiesUrl) throws TmsConfigurationException {
		try {
			// Create a JaxB unmarshaller:
			JAXBContext context = JAXBContext.newInstance(TileMapInfo.class);
			Unmarshaller um = context.createUnmarshaller();

			// Find out where to retrieve the capabilities and unmarshall:
			if (layerCapabilitiesUrl.startsWith("classpath:")) {
				String location = layerCapabilitiesUrl.substring(10);
				InputStream is = getClass().getResourceAsStream(location);
				return (TileMapInfo) um.unmarshal(is);
			}

			// Normal case, find the URL and unmarshal:
			URL url = new URL(layerCapabilitiesUrl);
			return (TileMapInfo) um.unmarshal(url);
		} catch (JAXBException e) {
			throw new TmsConfigurationException("Could not read the capabilities file.", e);
		} catch (MalformedURLException e) {
			throw new TmsConfigurationException("Could not find the capabilities file.", e);
		}
	}

	/**
	 * Transform a TMS layer description object into a raster layer info object.
	 * 
	 * @param tileMapInfo
	 *            The TMS layer description object.
	 * @return The raster layer info object as used by Geomajas.
	 */
	public RasterLayerInfo asLayerInfo(TileMapInfo tileMapInfo) {
		RasterLayerInfo layerInfo = new RasterLayerInfo();

		layerInfo.setCrs(tileMapInfo.getSrs());
		layerInfo.setDataSourceName(tileMapInfo.getTitle());
		layerInfo.setLayerType(LayerType.RASTER);
		layerInfo.setMaxExtent(asBbox(tileMapInfo.getBoundingBox()));
		layerInfo.setTileHeight(tileMapInfo.getTileFormat().getHeight());
		layerInfo.setTileWidth(tileMapInfo.getTileFormat().getWidth());

		List<ScaleInfo> zoomLevels = new ArrayList<ScaleInfo>(tileMapInfo.getTileSets().getTileSets().size());
		for (TileSetInfo tileSet : tileMapInfo.getTileSets().getTileSets()) {
			zoomLevels.add(asScaleInfo(tileSet));
		}
		layerInfo.setZoomLevels(zoomLevels);

		return layerInfo;
	}

	/**
	 * Transforms a TMS bounding box information object into a Geomajas {@link Bbox} object.
	 * 
	 * @param boundingBoxInfo
	 *            The TMS bounding box object.
	 * @return The default Geomajas bounding box.
	 */
	public Bbox asBbox(BoundingBoxInfo boundingBoxInfo) {
		double width = boundingBoxInfo.getMaxX() - boundingBoxInfo.getMinX();
		double height = boundingBoxInfo.getMaxY() - boundingBoxInfo.getMinY();
		return new Bbox(boundingBoxInfo.getMinX(), boundingBoxInfo.getMinY(), width, height);
	}

	/**
	 * Transforms a TMS tile-set description object into a Geomajas {@link ScaleInfo} object.
	 * 
	 * @param tileSetInfo
	 *            The tile set description.
	 * @return The default Geomajas scale object.
	 */
	public ScaleInfo asScaleInfo(TileSetInfo tileSetInfo) {
		ScaleInfo scaleInfo = new ScaleInfo();
		scaleInfo.setPixelPerUnit(1 / tileSetInfo.getUnitsPerPixel());
		return scaleInfo;
	}
}