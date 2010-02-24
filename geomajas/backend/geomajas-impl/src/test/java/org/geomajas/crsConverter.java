package org.geomajas;

import org.geotools.geometry.jts.JTS;
import org.geotools.referencing.CRS;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.MathTransform;
import org.opengis.referencing.operation.TransformException;

import com.vividsolutions.jts.geom.Envelope;

public class crsConverter {

	public static void main(String[] args) {
		Envelope env = new Envelope(-20026376.393709917, 20026376.393709917, -20026376.393709917, 20026376.393709917);
		Envelope lonlat = getClientMaxExtent("EPSG:4326", "EPSG:900913", env);
		System.out.println(lonlat);
		
		lonlat = getClientMaxExtent("EPSG:900913", "EPSG:4326", new Envelope(-180, 180, -90, 90));
		System.out.println(lonlat);
	}

	public static Envelope getClientMaxExtent(String mapCrsKey, String layerCrsKey, Envelope serverBbox) {
		try {
			CoordinateReferenceSystem mapCrs = CRS.decode(mapCrsKey);
			CoordinateReferenceSystem layerCrs = CRS.decode(layerCrsKey);
			MathTransform transformer = CRS.findMathTransform(layerCrs, mapCrs);
			return JTS.transform(serverBbox, transformer);
		} catch (FactoryException e) {
			e.printStackTrace();
		} catch (TransformException e) {
			e.printStackTrace();
		}
		return null;
	}

}
