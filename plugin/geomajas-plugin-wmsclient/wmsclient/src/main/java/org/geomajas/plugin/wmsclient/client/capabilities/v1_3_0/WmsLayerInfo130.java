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

package org.geomajas.plugin.wmsclient.client.capabilities.v1_3_0;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.geomajas.geometry.Bbox;
import org.geomajas.plugin.wmsclient.client.capabilities.AbstractXmlNodeWrapper;
import org.geomajas.plugin.wmsclient.client.capabilities.WmsLayerInfo;
import org.geomajas.plugin.wmsclient.client.capabilities.WmsLayerMetadataUrlInfo;
import org.geomajas.plugin.wmsclient.client.capabilities.WmsLayerStyleInfo;

import com.google.gwt.xml.client.Element;
import com.google.gwt.xml.client.NamedNodeMap;
import com.google.gwt.xml.client.Node;
import com.google.gwt.xml.client.NodeList;

/**
 * Default {@link WmsLayerInfo} implementation for WMS 1.3.0.
 * 
 * @author Pieter De Graef
 * @author An Buyle
 */
public class WmsLayerInfo130 extends AbstractXmlNodeWrapper implements WmsLayerInfo {

	private static final long serialVersionUID = 100L;

	private String name;

	private String title;

	private String abstractt;

	private final List<String> keywords = new ArrayList<String>();

	private final List<String> crs = new ArrayList<String>();

	private Bbox latlonBoundingBox;

	private final Map<String, Bbox> boundingBoxes = new HashMap<String, Bbox>(); // key = boundingBoxCrs

	private WmsLayerMetadataUrlInfo metadataUrl;

	private List<WmsLayerStyleInfo> styleInfo = new ArrayList<WmsLayerStyleInfo>();

	private boolean queryable;

	public WmsLayerInfo130(Node node) {
		super(node);
	}

	public String getName() {
		if (name == null) {
			parse(getNode());
		}
		return name;
	}

	public String getTitle() {
		if (name == null) {
			parse(getNode());
		}
		return title;
	}

	public String getAbstract() {
		if (name == null) {
			parse(getNode());
		}
		return abstractt;
	}

	public List<String> getKeywords() {
		if (name == null) {
			parse(getNode());
		}
		return keywords;
	}

	public List<String> getCrs() {
		if (name == null) {
			parse(getNode());
		}
		return crs;
	}

	public boolean isQueryable() {
		if (name == null) {
			parse(getNode());
		}
		return queryable;
	}

	
	@Override
	public Bbox getBoundingBox(String boundingBoxCrs) {
		if (name == null) {
			parse(getNode());
		}
		return boundingBoxes.get(boundingBoxCrs);
	}

	@Override
	public String getBoundingBoxCrs() {
		if (name == null) {
			parse(getNode());
		}
		if (null != this.crs &&  !this.crs.isEmpty() && null != boundingBoxes.get(this.crs.get(0))) {
			return this.crs.get(0);
		}
		return null;
	}

	@Override
	public Bbox getBoundingBox() {
		if (name == null) {
			parse(getNode());
		}
		if (null != this.crs &&  !this.crs.isEmpty() && null != boundingBoxes.get(this.crs.get(0))) {
			return boundingBoxes.get(this.crs.get(0));
		}
		return null;
	}

	public Bbox getLatlonBoundingBox() {
		if (name == null) {
			parse(getNode());
		}
		return latlonBoundingBox;
	}

	public WmsLayerMetadataUrlInfo getMetadataUrl() {
		if (name == null) {
			parse(getNode());
		}
		return metadataUrl;
	}

	public List<WmsLayerStyleInfo> getStyleInfo() {
		if (name == null) {
			parse(getNode());
		}
		return styleInfo;
	}

	// ------------------------------------------------------------------------
	// AbstractNodeInfo implementation:
	// ------------------------------------------------------------------------

	protected void parse(Node node) {
		queryable = isQueryable(node);

		NodeList childNodes = node.getChildNodes();
		for (int i = 0; i < childNodes.getLength(); i++) {
			Node child = childNodes.item(i);
			String nodeName = child.getNodeName();
			if ("Name".equalsIgnoreCase(nodeName)) {
				name = getValueRecursive(child);
			} else if ("Title".equalsIgnoreCase(nodeName)) {
				title = getValueRecursive(child);
			} else if ("Abstract".equalsIgnoreCase(nodeName)) {
				abstractt = getValueRecursive(child);
			} else if ("KeywordList".equalsIgnoreCase(nodeName)) {
				addKeyWords(child);
			} else if ("CRS".equalsIgnoreCase(nodeName)) {
				crs.add(getValueRecursive(child));
			} else if ("EX_GeographicBoundingBox".equalsIgnoreCase(nodeName)) {
				addLatLonBoundingBox(child);
			} else if ("BoundingBox".equalsIgnoreCase(nodeName)) {
				addBoundingBox(child);
			} else if ("MetadataURL".equalsIgnoreCase(nodeName)) {
				metadataUrl = new WmsLayerMetadataUrlInfo130(child);
			} else if ("Style".equalsIgnoreCase(nodeName)) {
				styleInfo.add(new WmsLayerStyleInfo130(child));
			}
		}
	}

	private boolean isQueryable(Node layerNode) {
		NamedNodeMap attributes = layerNode.getAttributes();
		Node q = attributes.getNamedItem("queryable");
		if (q != null) {
			return "1".equals(q.getNodeValue());
		}
		return false;
	}

	private void addKeyWords(Node keywordListNode) {
		Element keywordListEl = (Element) keywordListNode;
		NodeList keywordList = keywordListEl.getElementsByTagName("Keyword");
		for (int i = 0; i < keywordList.getLength(); i++) {
			Node keywordNode = keywordList.item(i);
			keywords.add(getValueRecursive(keywordNode));
		}
	}

	private void addBoundingBox(Node bboxNode) {
		NamedNodeMap attributes = bboxNode.getAttributes();
		Node crs = attributes.getNamedItem("CRS");
		String boundingBoxCrs = getValueRecursive(crs);

		Node minx = attributes.getNamedItem("minx");
		Node miny = attributes.getNamedItem("miny");
		Node maxx = attributes.getNamedItem("maxx");
		Node maxy = attributes.getNamedItem("maxy");

		double x = getValueRecursiveAsDouble(minx);
		double y = getValueRecursiveAsDouble(miny);
		double width = getValueRecursiveAsDouble(maxx) - x;
		double height = getValueRecursiveAsDouble(maxy) - y;
		Bbox boundingBox = new Bbox(x, y, width, height);
		this.boundingBoxes.put(boundingBoxCrs, boundingBox);
	}
	
	private void addLatLonBoundingBox(Node bboxNode) {
		NodeList childNodes = bboxNode.getChildNodes();
		double x = 0, y = 0, maxx = 0, maxy = 0;
		for (int i = 0; i < childNodes.getLength(); i++) {
			Node child = childNodes.item(i);
			String nodeName = child.getNodeName();
			if ("westBoundLongitude".equalsIgnoreCase(nodeName)) {
				x = getValueRecursiveAsDouble(child);
			} else if ("eastBoundLongitude".equalsIgnoreCase(nodeName)) {
				maxx = getValueRecursiveAsDouble(child);
			} else if ("southBoundLatitude".equalsIgnoreCase(nodeName)) {
				y = getValueRecursiveAsDouble(child);
			} else if ("northBoundLatitude".equalsIgnoreCase(nodeName)) {
				maxy = getValueRecursiveAsDouble(child);
			}
		}
		latlonBoundingBox = new Bbox(x, y, maxx - x, maxy - y);
	}

}