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
package org.geomajas.plugin.deskmanager.service.common;

import org.geomajas.configuration.AssociationAttributeInfo;
import org.geomajas.configuration.FeatureStyleInfo;
import org.geomajas.configuration.FontStyleInfo;
import org.geomajas.configuration.LabelStyleInfo;
import org.geomajas.configuration.NamedStyleInfo;
import org.geomajas.configuration.Parameter;
import org.geomajas.configuration.PrimitiveAttributeInfo;
import org.geomajas.configuration.RasterLayerInfo;
import org.geomajas.configuration.SnappingRuleInfo;
import org.geomajas.configuration.SymbolInfo;
import org.geomajas.configuration.VectorLayerInfo;
import org.geomajas.configuration.client.ClientApplicationInfo;
import org.geomajas.configuration.client.ClientLayerInfo;
import org.geomajas.configuration.client.ClientLayerTreeNodeInfo;
import org.geomajas.configuration.client.ClientMapInfo;
import org.geomajas.configuration.client.ClientRasterLayerInfo;
import org.geomajas.configuration.client.ClientToolInfo;
import org.geomajas.configuration.client.ClientVectorLayerInfo;
import org.geomajas.configuration.client.ScaleInfo;
import org.geomajas.geometry.Bbox;
import org.geomajas.plugin.deskmanager.command.manager.dto.RasterLayerConfiguration;
import org.geomajas.plugin.deskmanager.command.manager.dto.VectorLayerConfiguration;
import org.geomajas.plugin.deskmanager.configuration.client.BitmapInfo;
import org.geomajas.plugin.deskmanager.configuration.client.DeskmanagerClientLayerInfo;
import org.geomajas.plugin.deskmanager.configuration.client.GeodeskLayoutInfo;
import org.geomajas.plugin.deskmanager.domain.dto.DynamicLayerConfiguration;
import org.geomajas.sld.UserStyleInfo;

import com.thoughtworks.xstream.XStream;

/**
 * This can be used as @component, but needs to work standalone as well for the HibernateType as this is not
 * spring-injected.
 * 
 * @author Oliver May
 */
// @Component
public final class XmlConverterService {

	private static final XStream EMI_STREAMER = new XStream();

	static {
		EMI_STREAMER.alias("AssociationAttributeInfo", AssociationAttributeInfo.class);
		EMI_STREAMER.alias("BboxPlus", Bbox.class);
		EMI_STREAMER.alias("BitmapInfo", BitmapInfo.class);
		EMI_STREAMER.alias("ClientApplicationInfo", ClientApplicationInfo.class);
		EMI_STREAMER.alias("ClientLayerTreeNodeInfo", ClientLayerTreeNodeInfo.class);
		EMI_STREAMER.alias("ClientMapInfo", ClientMapInfo.class);
		EMI_STREAMER.alias("ClientRasterLayerInfo", ClientRasterLayerInfo.class);
		EMI_STREAMER.alias("ClientToolInfo", ClientToolInfo.class);
		EMI_STREAMER.alias("ClientVectorLayerInfo", ClientVectorLayerInfo.class);
		EMI_STREAMER.alias("FeatureStyleInfo", FeatureStyleInfo.class);
		EMI_STREAMER.alias("FontStyleInfo", FontStyleInfo.class);
		EMI_STREAMER.alias("LabelStyleInfo", LabelStyleInfo.class);
		EMI_STREAMER.alias("NamedStyleInfo", NamedStyleInfo.class);
		EMI_STREAMER.alias("Parameter", Parameter.class);
		EMI_STREAMER.alias("PrimitiveAttributeInfo", PrimitiveAttributeInfo.class);
		EMI_STREAMER.alias("RasterLayerInfo", RasterLayerInfo.class);
		EMI_STREAMER.alias("ScaleInfo", ScaleInfo.class);
		EMI_STREAMER.alias("SymbolInfo", SymbolInfo.class);
		EMI_STREAMER.alias("SnappingRuleInfo", SnappingRuleInfo.class);
		EMI_STREAMER.alias("UserStyleInfo", UserStyleInfo.class);
		EMI_STREAMER.alias("VectorLayerInfo", VectorLayerInfo.class);

		EMI_STREAMER.alias("GeodeskLayoutInfo", GeodeskLayoutInfo.class);
		EMI_STREAMER.alias("DeskmanagerClientLayerInfo", DeskmanagerClientLayerInfo.class);

		EMI_STREAMER.alias("DynamicLayerConfiguration", DynamicLayerConfiguration.class);
		EMI_STREAMER.alias("VectorLayerConfiguration", VectorLayerConfiguration.class);
		EMI_STREAMER.alias("RasterLayerConfiguration", RasterLayerConfiguration.class);
	}

	public static DynamicLayerConfiguration toLayerConfiguration(String data) {
		if (data == null || "".equals(data)) {
			return null;
		} else {
			return (DynamicLayerConfiguration) EMI_STREAMER.fromXML(data);
		}
	}

	/**
	 * never returns null, but empty MapInfo if no data.
	 * 
	 * @param data
	 * @return
	 */
	public static ClientApplicationInfo toClientApplicationInfo(String data) {
		if (data == null || "".equals(data)) {
			return new ClientApplicationInfo();
		} else {
			return (ClientApplicationInfo) EMI_STREAMER.fromXML(data);
		}
	}

	public static ClientMapInfo toClientMapInfo(String data) {
		if (data == null || "".equals(data)) {
			return new ClientMapInfo();
		} else {
			return (ClientMapInfo) EMI_STREAMER.fromXML(data);
		}
	}

	public static ClientLayerInfo toClientLayerInfo(String data) {
		if (data == null || "".equals(data)) {
			return null;
		} else {
			return (ClientLayerInfo) EMI_STREAMER.fromXML(data);
		}
	}

	public static String toXml(DynamicLayerConfiguration lc) {
		if (lc == null) {
			return null;
		} else {
			return EMI_STREAMER.toXML(lc);
		}
	}

	/**
	 * @param emi
	 * @return xmlString or null if emi is null
	 */
	public static String toXml(ClientApplicationInfo cai) {
		if (cai == null) {
			return null;
		} else {
			return EMI_STREAMER.toXML(cai);
		}
	}

	public static String toXml(ClientMapInfo cmi) {
		if (cmi == null) {
			return null;
		} else {
			return EMI_STREAMER.toXML(cmi);
		}
	}

	public static String toXml(ClientLayerInfo cli) {
		if (cli == null) {
			return null;
		} else {
			return EMI_STREAMER.toXML(cli);
		}
	}

	public static NamedStyleInfo toNamedStyleInfo(String data) {
		if (data == null || "".equals(data)) {
			return null;
		} else {
			return (NamedStyleInfo) EMI_STREAMER.fromXML(data);
		}
	}

	public static String toXml(NamedStyleInfo nsi) {
		if (nsi == null) {
			return null;
		} else {
			return EMI_STREAMER.toXML(nsi);
		}
	}

	public static ScaleInfo toScaleInfo(String data) {
		if (data == null || "".equals(data)) {
			return null;
		} else {
			return (ScaleInfo) EMI_STREAMER.fromXML(data);
		}
	}

	public static String toXml(ScaleInfo nsi) {
		if (nsi == null) {
			return null;
		} else {
			return EMI_STREAMER.toXML(nsi);
		}
	}

	// ----------------------------------------------------------

	public static XStream getStreamer() {
		return EMI_STREAMER;
	}
	
	private XmlConverterService() {}
}
