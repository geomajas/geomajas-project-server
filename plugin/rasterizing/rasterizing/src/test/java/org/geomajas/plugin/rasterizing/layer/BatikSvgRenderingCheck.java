package org.geomajas.plugin.rasterizing.layer;

import org.apache.batik.bridge.BridgeContext;
import org.apache.batik.bridge.DocumentLoader;
import org.apache.batik.bridge.GVTBuilder;
import org.apache.batik.bridge.UserAgent;
import org.apache.batik.bridge.UserAgentAdapter;
import org.apache.batik.dom.svg.SAXSVGDocumentFactory;
import org.apache.batik.gvt.GraphicsNode;
import org.apache.batik.util.XMLResourceDescriptor;
import org.geomajas.global.GeomajasException;
import org.geomajas.plugin.rasterizing.api.RasterException;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.map.MapContent;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.svg.SVGDocument;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.imageio.ImageIO;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;

public class BatikSvgRenderingCheck {

	public static void main(String[] args) {
		try {
			new BatikSvgRenderingCheck().createSvgBatikRenderedImage();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/* copied from an example: circle and rectangle */
	/*String svgContentFromApplication = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
			"<svg xmlns=\"http://www.w3.org/2000/svg\" xmlns:xlink=\"http://www.w3.org/1999/xlink\" " +
			"width=\"995\" height=\"688\" viewBox=\"0 0 995 688\" >" +
			"<defs/><g/><g/><g transform=\"translate(497.5,256.0)\">" +
			"<circle cx=\"0\" cy=\"0\" fill=\"#66CC66\" fill-opacity=\"0.4\" r=\"38\" stroke=\"black\" " +
			"stroke-opacity=\"1.0\" stroke-width=\"1\"/>" +
			"<rect fill=\"#CC9900\" fill-opacity=\"0.4\" height=\"12\" stroke=\"black\" stroke-opacity=\"1.0\" " +
			"stroke-width=\"1\" width=\"25\" x=\"13\" y=\"-26\"/>" +
			"</g><g transform=\"translate(497.5,256.0)\"/></svg>";*/
	String svgContentPath = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
			"<svg xmlns=\"http://www.w3.org/2000/svg\" xmlns:xlink=\"http://www.w3.org/1999/xlink\" " +
			"width=\"300\" height=\"200\" viewBox=\"0 0 300 200\">" +
			"<path d=\"M10 10 L90 10 L90 90 L10 90 Z\" style=\"fill:lime;stroke:purple;stroke-width:3px\"/>" +
			"</svg>";
	String svgContentRect = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
			"<svg xmlns=\"http://www.w3.org/2000/svg\" xmlns:xlink=\"http://www.w3.org/1999/xlink\" " +
			"width=\"300\" height=\"200\" viewBox=\"0 0 300 200\">" +
			"<rect fill=\"#CC9900\" fill-opacity=\"0.4\" height=\"200\" " +
			"width=\"300\" x=\"0\" y=\"0\"/>" +
			"</svg>";
	String svgContentWithPartNegativeValues = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
			"<svg xmlns=\"http://www.w3.org/2000/svg\" xmlns:xlink=\"http://www.w3.org/1999/xlink\" " +
			"width=\"200\" height=\"200\" viewBox=\"0 0 200 200\">" +
			"<circle cx=\"0\" cy=\"0\" fill=\"#66CC66\" fill-opacity=\"0.4\" r=\"50\" stroke=\"black\" " +
			"stroke-opacity=\"1.0\" stroke-width=\"1\"/>" +
			"</svg>";

	Rectangle screenArea = new Rectangle(0, 0, 10, 10);
	ReferencedEnvelope viewBboxOther = new ReferencedEnvelope(0, 0, 10, 10, null);

	private String fileLocationAndName = "target/test4.png";

	public void createSvgBatikRenderedImage() throws Exception {
		GraphicsNode graphicsNode = createNode(svgContentWithPartNegativeValues, 0, 0);
		/*AffineTransform affineTransform = new AffineTransform();
		affineTransform.translate(20, 20);
		graphicsNode.setTransform(affineTransform);*/
		GraphicsNode graphicsNodeBackground = createNode(svgContentRect, 0, 0);

		BufferedImage bufferedImage = new BufferedImage(200, 200, BufferedImage.TYPE_INT_ARGB);
		Graphics2D graphics2D = bufferedImage.createGraphics();
		graphics2D.setBackground(Color.BLUE);
		graphicsNodeBackground.paint(graphics2D);
		graphicsNode.paint(graphics2D);
		graphics2D.dispose();
		ImageIO.write(bufferedImage, "png", new File(fileLocationAndName));

	}

	private AffineTransform getTransform() {
		MapContent m = new MapContent();
		m.getViewport().setBounds(viewBboxOther);
		m.getViewport().setScreenArea(screenArea);
		return m.getViewport().getScreenToWorld();
	}

	private GraphicsNode createNode(String svgContent, int viewBoxXDelta, int viewPortYDelta) throws GeomajasException {
		// batik magic
		String parser = XMLResourceDescriptor.getXMLParserClassName();
		SAXSVGDocumentFactory f = new SAXSVGDocumentFactory(parser);
		SVGDocument document;
		try {
			svgContent = performViewPortDeltaPosition(svgContent, viewBoxXDelta, viewPortYDelta);
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

	private String performViewPortDeltaPosition(String originalSvgContent, int viewBoxXDelta, int viewPortYDelta) {
		Document doc = parseXml(originalSvgContent);
		if (doc.getFirstChild().getNodeName().equals("svg")) {
			Node svg = doc.getFirstChild();
			Node viewBox = svg.getAttributes().getNamedItem("viewBox");
			if (viewBox != null) {
				String nodeValue = viewBox.getNodeValue();
				String[] values = nodeValue.split(" ");
				if (values.length == 4) {
					int xValue = Integer.parseInt(values[0]) + viewBoxXDelta;
					int yValue = Integer.parseInt(values[1]) + viewPortYDelta;
					String newValue = xValue + " " + yValue + " " + values[2] + " " + values[3];
					viewBox.setNodeValue(newValue);
				}
			} else {
				// create a new value
				//viewBox = new DeferredAttrNSImpl();
			}
		}
		try {
			return toXmlString(doc);
		} catch (TransformerException e) {
			e.printStackTrace();
		}
		return null;
	}

	private Document parseXml(String svgContent) {
		try {
			//Using factory get an instance of document builder
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			dbf.setNamespaceAware(true);
			DocumentBuilder db = dbf.newDocumentBuilder();
			return db.parse(new InputSource(new StringReader(svgContent)));
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	private String toXmlString(Document doc) throws TransformerException {
		TransformerFactory tf = TransformerFactory.newInstance();
		Transformer transformer = tf.newTransformer();
		transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
		StringWriter writer = new StringWriter();
		transformer.transform(new DOMSource(doc), new StreamResult(writer));
		return writer.getBuffer().toString().replaceAll("\n|\r", "");
	}

}
