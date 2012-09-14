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
package org.geomajas.plugin.deskmanager.domain;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.PrecisionModel;

/**
 * Helper class for crab data import.
 * 
 * @author Oliver May
 * 
 */
public class CrabData {

	private final GeometryFactory gf = new GeometryFactory(new PrecisionModel(), 31300);

	private long adrespuntId;

	private int aard;

	private Geometry geom;

	private String aardBeschrijving;

	private String crabStraatnaam;

	private String crabHuisnummer;

	/**
	 * @return the aardBeschrijving
	 */
	public String getAardBeschrijving() {
		return aardBeschrijving;
	}

	/**
	 * @return the crabStraatnaam
	 */
	public String getCrabStraatnaam() {
		return crabStraatnaam;
	}

	/**
	 * @return the crabHuisnummer
	 */
	public String getCrabHuisnummer() {
		return crabHuisnummer;
	}

	public CrabData() {
	}

	// ------------------------------------------------------------------

	public long getAdrespuntId() {
		return adrespuntId;
	}

	public void setAdrespuntId(long adrespuntId) {
		this.adrespuntId = adrespuntId;
	}

	public Geometry getGeom() {
		return geom;
	}

	public void setGeom(Geometry geom) {
		this.geom = geom;
	}

	public void setGeom(double x, double y) {
		Coordinate c = new Coordinate(x, y);
		this.geom = gf.createPoint(c);
	}

	public int getAard() {
		return aard;
	}

	public void setAard(int aard) {
		this.aard = aard;
	}

	/**
	 * @param string
	 */
	public void setAardBeschrijving(String string) {
		this.aardBeschrijving = string;
	}

	/**
	 * @param string
	 */
	public void setCrabStraatnaam(String string) {
		this.crabStraatnaam = string;
	}

	/**
	 * @param string
	 */
	public void setCrabHuisnummer(String string) {
		this.crabHuisnummer = string;
	}
}