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
package org.geomajas.plugin.printing.component.impl;

import java.awt.Graphics2D;

import org.geomajas.configuration.client.ClientMapInfo;
import org.geomajas.geometry.Bbox;
import org.geomajas.plugin.printing.component.PdfContext;
import org.geomajas.plugin.printing.component.dto.RasterizedLayersComponentInfo;
import org.geomajas.plugin.rasterizing.api.ImageService;
import org.geomajas.plugin.rasterizing.command.dto.MapRasterizingInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.thoughtworks.xstream.annotations.XStreamOmitField;
import com.vividsolutions.jts.geom.Envelope;

/**
 * Internal implementation of {@link org.geomajas.plugin.printing.component.BaseLayerComponent} for rendering all layers
 * by using the information in the {@link MapRasterizingInfo} of the map. This component should be added as any other
 * layer child to the map but may represent multiple layers.
 * 
 * @author Jan De Moerloose
 */
@Component()
@Scope(value = "prototype")
public class RasterizedLayersComponentImpl extends BaseLayerComponentImpl<RasterizedLayersComponentInfo> {

	private final Logger log = LoggerFactory.getLogger(RasterizedLayersComponentImpl.class);

	/** The calculated bounds. */
	@XStreamOmitField
	private ClientMapInfo clientMapInfo;

	@Autowired
	@XStreamOmitField
	private ImageService imageService;

	/** The calculated bounds. */
	@XStreamOmitField
	protected Envelope bbox;

	@Override
	public void render(PdfContext context) {
		try {
			bbox = createBbox();
			MapRasterizingInfo mapRasterizingInfo = (MapRasterizingInfo) clientMapInfo
					.getWidgetInfo(MapRasterizingInfo.WIDGET_KEY);
			mapRasterizingInfo.setBounds(new Bbox(bbox.getMinX(), bbox.getMinY(), bbox.getWidth(), bbox.getHeight()));
			mapRasterizingInfo.setScale(getMap().getPpUnit());
			Graphics2D graphics = context.getGraphics2D(getBounds());
			imageService.writeMap(graphics, clientMapInfo);
			graphics.dispose();
		} catch (Exception e) {
			log.warn("Failed to render rasterized layers", e);
		}
		super.render(context);
	}

	@Override
	public void fromDto(RasterizedLayersComponentInfo info) {
		super.fromDto(info);
		this.clientMapInfo = info.getMapInfo();
	}

}
