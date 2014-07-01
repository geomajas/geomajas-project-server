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
package org.geomajas.plugin.rasterizing.layer;

import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Rectangle2D;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.batik.bridge.BridgeContext;
import org.apache.batik.bridge.DocumentLoader;
import org.apache.batik.bridge.GVTBuilder;
import org.apache.batik.bridge.UserAgent;
import org.apache.batik.bridge.UserAgentAdapter;
import org.apache.batik.dom.svg.SAXSVGDocumentFactory;
import org.apache.batik.gvt.GraphicsNode;
import org.apache.batik.util.XMLResourceDescriptor;
import org.geomajas.configuration.client.ClientLayerInfo;
import org.geomajas.global.GeomajasException;
import org.geomajas.plugin.rasterizing.api.LayerFactory;
import org.geomajas.plugin.rasterizing.api.RasterException;
import org.geomajas.plugin.rasterizing.command.dto.ClientSvgLayerInfo;
import org.geomajas.service.DtoConverterService;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.map.Layer;
import org.geotools.map.MapContent;
import org.geotools.map.MapContext;
import org.geotools.referencing.operation.matrix.XAffineTransform;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.w3c.dom.Document;
import org.w3c.dom.svg.SVGDocument;
import org.xml.sax.InputSource;

/**
 * This factory creates a GeoTools layer that is capable of writing geometries.
 * 
 * @author Jan De Moerloose
 */
@Component
public class SvgLayerFactory implements LayerFactory {

	private final Logger log = LoggerFactory.getLogger(SvgDirectLayer.class);

	@Autowired
	private DtoConverterService converterService;

	public boolean canCreateLayer(MapContext mapContent, ClientLayerInfo clientLayerInfo) {
		return clientLayerInfo instanceof ClientSvgLayerInfo;
	}

	public Layer createLayer(MapContext mapContent, ClientLayerInfo clientLayerInfo) throws GeomajasException {
		if (!(clientLayerInfo instanceof ClientSvgLayerInfo)) {
			throw new IllegalArgumentException(
					"SvgLayerFactory.createLayer() should only be called using ClientSvgLayerInfo");
		}
		try {
			ClientSvgLayerInfo layerInfo = (ClientSvgLayerInfo) clientLayerInfo;
			SvgDirectLayer layer = new SvgDirectLayer();
			// to make sure that all svg content is visible, we expand the viewbox to the full map bounds !
			// first we transform the screen bounds of the full map to SVG user coordinates (same as HTML coordinates as
			// we assume that there is no viewbox defined yet !!!)
			AffineTransform svgToScreen = getSvgToScreen(layerInfo, mapContent);
			// transform screen -> to svg of full screen area to get the new viewbox
			Rectangle2D viewbox = new Rectangle();
			XAffineTransform.inverseTransform(svgToScreen, mapContent.getViewport().getScreenArea(), viewbox);
			// then apply the new viewbox to the svg
			String svgContent = replaceViewBox(layerInfo.getSvgContent(), viewbox.getBounds());
			// now we can pass the svg node to the layer
			layer.setGraphicsNode(createNode(svgContent));
			// and the HTML transformation
			layer.setHtmlToScreen(getScreenTransformation(mapContent.getViewport().getBounds(), viewbox.getBounds(),
					mapContent));
			layer.getUserData().put(USERDATA_KEY_SHOWING, layerInfo.isShowing());
			return layer;
		} catch (Exception e) {
			throw new RasterException(e, RasterException.BAD_SVG, "Cannot initialize SVG layer");
		}
	}

	public Map<String, Object> getLayerUserData(MapContext mapContext, ClientLayerInfo clientLayerInfo) {
		Map<String, Object> userData = new HashMap<String, Object>();
		ClientSvgLayerInfo layerInfo = (ClientSvgLayerInfo) clientLayerInfo;
		userData.put(USERDATA_KEY_SHOWING, layerInfo.isShowing());
		return userData;
	}

	protected String replaceViewBox(String svgContent, Rectangle viewBox) throws IOException {
		Document document;
		try {
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			document = dBuilder.parse(new InputSource(new StringReader(svgContent)));
			document.getDocumentElement().setAttribute("xmlns", "http://www.w3.org/2000/svg");
			document.getDocumentElement().setAttribute("xmlns:xlink", "http://www.w3.org/1999/xlink");
			document.getDocumentElement().setAttribute("width", viewBox.width + "");
			document.getDocumentElement().setAttribute("height", viewBox.height + "");
			if (document.getDocumentElement().getAttribute("viewBox") != null
					&& !document.getDocumentElement().getAttribute("viewBox").isEmpty()) {
				log.error("SVG content with arbitrary viewbox will not be printed correctly ! "
						+ "It is assumed that the SVG has no viewbox transformation.");
			}
			document.getDocumentElement().setAttribute("viewBox",
					viewBox.x + " " + viewBox.y + " " + viewBox.width + " " + viewBox.height);
			return getStringFromDocument(document);
		} catch (Exception e) {
			throw new IOException(e.getMessage());
		}
	}

	protected GraphicsNode createNode(String svgContent) throws IOException {
		// batik magic
		String parser = XMLResourceDescriptor.getXMLParserClassName();
		SAXSVGDocumentFactory f = new SAXSVGDocumentFactory(parser);
		SVGDocument document;
		document = f.createSVGDocument("", new StringReader(svgContent));
		UserAgent userAgent = new UserAgentAdapter();
		DocumentLoader loader = new DocumentLoader(userAgent);
		BridgeContext bridgeContext = new BridgeContext(userAgent, loader);
		bridgeContext.setDynamic(true);
		GVTBuilder builder = new GVTBuilder();
		return builder.build(bridgeContext, document);
	}

	private AffineTransform getSvgToScreen(ClientSvgLayerInfo layerInfo, MapContent mapContent)
			throws NoninvertibleTransformException {
		ReferencedEnvelope svgWorldBounds = new ReferencedEnvelope(
				converterService.toInternal(layerInfo.getBounds()), mapContent.getCoordinateReferenceSystem());
		Rectangle svgUserbounds = new Rectangle(layerInfo.getWidth(), layerInfo.getHeight());
		return getScreenTransformation(svgWorldBounds, svgUserbounds, mapContent);
	}

	/**
	 * Gets the transformation between the internal coordinate system of a rectangle (y-axis pointing down) and the
	 * screen space of the {@link MapContent}, given the world bounds of the rectangle. The x,y values of the rectangle
	 * have no influence here.
	 * 
	 * @param svgWorldBounds
	 * @param svgUserbounds
	 * @param mapContent
	 * @return
	 * @throws NoninvertibleTransformException
	 */
	private AffineTransform getScreenTransformation(ReferencedEnvelope worldBounds, Rectangle rectangle,
			MapContent mapContent) throws NoninvertibleTransformException {
		// leech on map content to calculate the affine tx
		MapContent m = new MapContent();
		m.getViewport().setBounds(worldBounds);
		m.getViewport().setScreenArea(rectangle);
		AffineTransform rectToWorld = m.getViewport().getScreenToWorld();
		// svg -> world + world-> screen (t1.concatenate(t2) = apply t1 after t2 !!!!)
		AffineTransform worldToScreen = (AffineTransform) mapContent.getViewport().getWorldToScreen().clone();
		worldToScreen.concatenate(rectToWorld);
		return worldToScreen;
	}

	private String getStringFromDocument(Document document) throws TransformerException {
		DOMSource domSource = new DOMSource(document);
		StringWriter writer = new StringWriter();
		StreamResult result = new StreamResult(writer);
		TransformerFactory tf = TransformerFactory.newInstance();
		Transformer transformer = tf.newTransformer();
		transformer.transform(domSource, result);
		return writer.toString();
	}

}
