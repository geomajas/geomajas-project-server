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
package org.geomajas.plugin.rasterizing.tms;

import com.sun.xml.bind.v2.runtime.IllegalAnnotationException;
import com.sun.xml.bind.v2.runtime.IllegalAnnotationsException;

import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import org.geomajas.geometry.Bbox;
import org.geomajas.geometry.Crs;
import org.geomajas.global.GeomajasException;
import org.geomajas.layer.VectorLayer;
import org.geomajas.layer.VectorLayerService;
import org.geomajas.plugin.rasterizing.mvc.TmsController;
import org.geomajas.service.ConfigurationService;
import org.geomajas.service.DispatcherUrlService;
import org.geomajas.service.DtoConverterService;
import org.geomajas.service.GeoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Default implementation of {@link TmsService}.
 * 
 * @author Jan De Moerloose
 *
 */
@Component
public class TmsServiceImpl implements TmsService {

	private final Logger log = LoggerFactory.getLogger(TmsServiceImpl.class);

	public static final String MAPPING_1_0_0 = "tms/1.0.0/";

	@Autowired
	private ConfigurationService configurationService;

	@Autowired
	private VectorLayerService vectorLayerService;

	@Autowired
	private DispatcherUrlService dispatcherUrlService;

	@Autowired
	private DtoConverterService dtoConverterService;

	@Autowired
	private GeoService geoService;

	@Autowired(required = false)
	private List<VectorLayer> layers = new ArrayList<VectorLayer>();

	@Override
	public void writeService(Writer writer) throws GeomajasException {
		TileMapService tileMapService = new TileMapService();
		tileMapService.setTitle("Geomajas Tile Map Service");
		tileMapService.setAbstract("Tile Map Service publication of the Geomajas vector layers");
		// loop over all layers
		for (VectorLayer layer : layers) {
			// 3 profiles, local uses layer crs and maximum extent
			for (ProfileType profile : ProfileType.values()) {
				Crs crs = vectorLayerService.getCrs(layer);
				String srs = (profile.getCrs() == null ? geoService.getCodeFromCrs(crs) : profile.getCrs());
				// populate the reference
				TileMapRef tileMapRef = new TileMapRef();
				tileMapRef.setProfile(profile.getName());
				tileMapRef.setTitle(layer.getId());
				tileMapRef.setSrs(srs);
				// url = http://localhost:8080/d/tms/1.0.0/{layerId}@{crs}/{styleId}
				StringBuilder sb = new StringBuilder(dispatcherUrlService.getDispatcherUrl());
				sb.append(MAPPING_1_0_0).append(layer.getId());
				sb.append("@").append(srs);
				sb.append("/").append(layer.getLayerInfo().getNamedStyleInfos().get(0).getName());
				tileMapRef.setHref(sb.toString());
				// add it
				tileMapService.getTileMaps().add(tileMapRef);
			}
		}
		try {
			JAXBContext context = JAXBContext.newInstance(TileMapService.class);
			Marshaller m = context.createMarshaller();
			m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			m.marshal(tileMapService, writer);
		} catch (IllegalAnnotationsException ie) {
			for (IllegalAnnotationException error : ie.getErrors()) {
				log.error("Could not instantiate jaxb", error);
			}
		} catch (JAXBException e) {
			log.error("Could not instantiate jaxb", e);
			// can't happen
		}

	}

	@Override
	public void writeTileMap(String layerId, String styleRef, ProfileType profile, Writer writer)
			throws GeomajasException {
		TileMap tileMap = new TileMap();
		tileMap.setTitle(layerId);
		tileMap.setAbstract("Tile Map Service publication of the Geomajas layer " + layerId);
		tileMap.setVersion("1.0.0");
		StringBuilder sb = new StringBuilder(dispatcherUrlService.getDispatcherUrl());
		sb.append(MAPPING_1_0_0.substring(0, MAPPING_1_0_0.length() - 1));
		tileMap.setTileMapService(sb.toString());
		// get the profile
		TmsProfile p = null;
		VectorLayer layer = configurationService.getVectorLayer(layerId);
		StringBuilder href = new StringBuilder(dispatcherUrlService.getDispatcherUrl());
		href.append(MAPPING_1_0_0).append(layerId);
		switch (profile) {
			case GLOBAL_GEODETIC:
				href.append("@").append(profile.getCrs());
				tileMap.setSrs(profile.getCrs());
				p = new GlobalGeodeticProfile();
				break;
			case GLOBAL_MERCATOR:
				href.append("@").append(profile.getCrs());
				tileMap.setSrs(profile.getCrs());
				p = new GlobalMercatorProfile();
				break;
			case LOCAL:
			default:
				href.append("@").append(layer.getLayerInfo().getCrs());
				tileMap.setSrs(layer.getLayerInfo().getCrs());
				Bbox layerBounds = layer.getLayerInfo().getMaxExtent();
				p = new LocalProfile(dtoConverterService.toInternal(layerBounds), TmsController.PROFILE_TILE_SIZE);
				break;
		}
		href.append("/").append(layer.getLayerInfo().getNamedStyleInfos().get(0).getName());
		tileMap.setBoundingBox(p.getBounds());
		tileMap.setOrigin(p.getOrigin());
		// tile format
		TileFormat tileFormat = new TileFormat();
		tileFormat.setMimeType("image/png");
		tileFormat.setExtension("png");
		tileFormat.setWidth(p.getTileWidth());
		tileFormat.setHeight(p.getTileHeight());
		tileMap.setTileFormat(tileFormat);
		tileMap.setProfile(p, href.toString());
		try {
			JAXBContext context = JAXBContext.newInstance(TileMap.class);
			Marshaller m = context.createMarshaller();
			m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			m.marshal(tileMap, writer);
		} catch (JAXBException e) {
			log.error("Could not instantiate jaxb", e);
			// can't happen
		}

	}

}
