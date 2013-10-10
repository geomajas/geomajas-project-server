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
package org.geomajas.plugin.rasterizing.layer;

import java.awt.Rectangle;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
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
import org.geotools.map.MapContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.svg.SVGDocument;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 * This factory creates a GeoTools layer that is capable of writing geometries.
 * 
 * @author Jan De Moerloose
 */
@Component
public class SvgLayerFactory implements LayerFactory {

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
		ClientSvgLayerInfo layerInfo = (ClientSvgLayerInfo) clientLayerInfo;
		SvgDirectLayer layer = new SvgDirectLayer(mapContent);
		String finalSvg;
		finalSvg = addAttributesToSvg(layerInfo.getSvgContent(), layerInfo.getViewBoxWidth(),
				layerInfo.getViewBoxHeight());
		GraphicsNode graphicsNode = createNode(finalSvg);
		layer.setGraphicsNode(graphicsNode);
		layer.setSvgWorldBounds(new ReferencedEnvelope(converterService.toInternal(layerInfo.getViewBoxBounds()),
				mapContent.getCoordinateReferenceSystem()));
		layer.setSvgScreenBounds(new Rectangle(layerInfo.getViewBoxWidth(), layerInfo.getViewBoxHeight()));
		layer.getUserData().put(USERDATA_KEY_SHOWING, layerInfo.isShowing());
		return layer;
	}

	private GraphicsNode createNode(String svgContent) throws GeomajasException {
		// batik magic
		String parser = XMLResourceDescriptor.getXMLParserClassName();
		SAXSVGDocumentFactory f = new SAXSVGDocumentFactory(parser);
		SVGDocument document;
		try {
			document = f.createSVGDocument("", new StringReader(svgContent));
		} catch (IOException e) {
			throw new RasterException(e, RasterException.BAD_SVG, "Cannot parse SVG");
		}
		UserAgent userAgent = new UserAgentAdapter();
		DocumentLoader loader = new DocumentLoader(userAgent);
		BridgeContext bridgeContext = new BridgeContext(userAgent, loader);
		bridgeContext.setDynamic(true);
		GVTBuilder builder = new GVTBuilder();
		return builder.build(bridgeContext, document);
	}

	public String addAttributesToSvg(String svgContent, int viewBoxWidth, int viewBoxHeight) throws GeomajasException {
		Document document;
		try {
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			document = dBuilder.parse(new InputSource(new StringReader(svgContent)));
			document.getDocumentElement().setAttribute("xmlns", "http://www.w3.org/2000/svg");
			document.getDocumentElement().setAttribute("xmlns:xlink", "http://www.w3.org/1999/xlink");
			document.getDocumentElement().setAttribute("width", viewBoxWidth + "");
			document.getDocumentElement().setAttribute("height", viewBoxHeight + "");
			document.getDocumentElement().setAttribute("viewBox", "0 0 " + viewBoxWidth + " " + viewBoxHeight);
			return getStringFromDocument(document);
		} catch (DOMException e) {
			throw new RasterException(e, RasterException.BAD_SVG, "Cannot add namespaces to SVG");
		} catch (ParserConfigurationException e) {
			throw new RasterException(e, RasterException.BAD_SVG, "Cannot add namespaces to SVG");
		} catch (SAXException e) {
			throw new RasterException(e, RasterException.BAD_SVG, "Cannot add namespaces to SVG");
		} catch (IOException e) {
			throw new RasterException(e, RasterException.BAD_SVG, "Cannot add namespaces to SVG");
		} catch (TransformerException e) {
			throw new RasterException(e, RasterException.BAD_SVG, "Cannot add namespaces to SVG");
		}
	}

	public String getStringFromDocument(Document document) throws TransformerException {
		DOMSource domSource = new DOMSource(document);
		StringWriter writer = new StringWriter();
		StreamResult result = new StreamResult(writer);
		TransformerFactory tf = TransformerFactory.newInstance();
		Transformer transformer = tf.newTransformer();
		transformer.transform(domSource, result);
		return writer.toString();
	}

	public Map<String, Object> getLayerUserData(MapContext mapContext, ClientLayerInfo clientLayerInfo) {
		Map<String, Object> userData = new HashMap<String, Object>();
		ClientSvgLayerInfo layerInfo = (ClientSvgLayerInfo) clientLayerInfo;
		userData.put(USERDATA_KEY_SHOWING, layerInfo.isShowing());
		return userData;
	}

}
