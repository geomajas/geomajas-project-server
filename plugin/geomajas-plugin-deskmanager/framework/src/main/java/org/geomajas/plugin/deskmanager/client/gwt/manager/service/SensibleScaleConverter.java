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
package org.geomajas.plugin.deskmanager.client.gwt.manager.service;

import org.geomajas.configuration.client.ScaleInfo;

import com.google.gwt.i18n.client.NumberFormat;

/**
 * 
 * @author Oliver May
 *
 */
public final class SensibleScaleConverter {

	private static final double PPM = 3779.527559055; // == 96 PPI a (randomly) assumed screenresolution.

	// that's a no-no
	private SensibleScaleConverter() {
	}

	public static String scaleToString(ScaleInfo scale) {
		NumberFormat numberFormat = NumberFormat.getFormat("###,###");
		return numberFormat.format(scale.getNumerator()) + " : " + numberFormat.format(scale.getDenominator());
	}

	public static ScaleInfo stringToScale(String value) {
		NumberFormat numberFormat = NumberFormat.getFormat("###,###");
		String[] scale2 = value.split(":");
		ScaleInfo si;
		if (scale2.length == 1) {
			si = new ScaleInfo(1D, numberFormat.parse(scale2[0].trim()));
		} else {
			si = new ScaleInfo(numberFormat.parse(scale2[0].trim()), numberFormat.parse(scale2[1].trim()));
		}
		si.setPixelPerUnit(si.getNumerator() / si.getDenominator() * PPM);
		return si;
	}

}
