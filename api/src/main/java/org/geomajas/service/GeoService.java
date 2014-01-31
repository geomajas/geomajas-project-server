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

package org.geomajas.service;

import org.geomajas.annotation.Api;
import org.geomajas.geometry.Bbox;
import org.geomajas.geometry.Crs;
import org.geomajas.geometry.CrsTransform;
import org.geomajas.global.GeomajasException;
import org.geomajas.layer.LayerException;
import org.geomajas.layer.feature.InternalFeature;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.MathTransform;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.Point;

/**
 * Collection of utility functions concerning geometries.
 *
 * @author Joachim Van der Auwera
 * @since 1.6.0
 */
@Api(allMethods = true)
public interface GeoService {

	/**
	 * Get the {@link org.opengis.referencing.crs.CoordinateReferenceSystem} with given code.
	 *
	 * @param crs Coordinate reference system code. (EPSG:xxxx)
	 * @return {@link org.opengis.referencing.crs.CoordinateReferenceSystem}
	 * @throws org.geomajas.layer.LayerException CRS code not found
	 * @since 1.7.0
	 * @deprecated use getCrs2()
	 */
	@Deprecated
	CoordinateReferenceSystem getCrs(String crs) throws LayerException;

	/**
	 * Get the {@link Crs} with given code.
	 *
	 * @param crs Coordinate reference system code. (EPSG:xxxx)
	 * @return {@link org.opengis.referencing.crs.CoordinateReferenceSystem}
	 * @throws org.geomajas.layer.LayerException CRS code not found
	 * @since 1.8.0
	 */
	Crs getCrs2(String crs) throws LayerException;

	/**
	 * Try to extract the SRID (Spatial Reference Id) from the CRS.
	 * Unreliable but works if SRIDs are same as EPSG numbers.
	 *
	 * @param crs
	 *            CRS string in the form of 'EPSG:<srid>'.
	 * @return SRID as integer.
	 */
	int getSridFromCrs(String crs);

	/**
	 * Try to extract the SRID (Spatial Reference Id) from the CRS.
	 * Unreliable but works if SRIDs are same as EPSG numbers.
	 *
	 * @param crs reference system of EPSG type.
	 * @return SRID as integer.
	 */
	int getSridFromCrs(CoordinateReferenceSystem crs);

	/**
	 * Try to extract the code from the CRS.
	 * Uses the actual code in case of a {@link Crs} or best guess based on the srid code otherwise.
	 *
	 * @param crs reference system of EPSG type
	 * @return CRS code
	 * @since 1.8.0
	 */
	String getCodeFromCrs(CoordinateReferenceSystem crs);

	/**
	 * Get the code from the CRS.
	 *
	 * @param crs reference system of EPSG type.
	 * @return CRS code
	 * @since 1.8.0
	 */
	String getCodeFromCrs(Crs crs);

	/**
	 * Get the transformation which converts between two coordinate systems.
	 *
	 * @param sourceCrs crs used by source data
	 * @param targetCrs crs to be used by target
	 * @return transformation matrix
	 * @throws GeomajasException building the transformation is not possible
	 */
	MathTransform findMathTransform(CoordinateReferenceSystem sourceCrs,
			CoordinateReferenceSystem targetCrs) throws GeomajasException;

	/**
	 * Get the transformation which converts between two coordinate systems.
	 *
	 * @param sourceCrs crs used by source data
	 * @param targetCrs crs to be used by target
	 * @return transformation matrix
	 * @throws GeomajasException building the transformation is not possible
	 * @since 1.8.0
	 */
	CrsTransform getCrsTransform(Crs sourceCrs, Crs targetCrs) throws GeomajasException;

	/**
	 * Get the transformation which converts between two coordinate systems.
	 *
	 * @param sourceCrs crs used by source data
	 * @param targetCrs crs to be used by target
	 * @return transformation matrix
	 * @throws GeomajasException building the transformation is not possible
	 * @since 1.8.0
	 */
	CrsTransform getCrsTransform(CoordinateReferenceSystem sourceCrs, CoordinateReferenceSystem targetCrs)
			throws GeomajasException;

	/**
	 * Get the transformation which converts between two coordinate systems.
	 *
	 * @param sourceCrs crs used by source data
	 * @param targetCrs crs to be used by target
	 * @return transformation matrix
	 * @throws GeomajasException building the transformation is not possible
	 * @since 1.8.0
	 */
	CrsTransform getCrsTransform(String sourceCrs, String targetCrs)
			throws GeomajasException;

	/**
	 * Transform a {@link Geometry} using the given transformation.
	 * <p/>
	 * An empty geometry is returned (and a warning logged) if the transformation failed.
	 *
	 * @param source source geometry
	 * @param crsTransform transformation to be applied
	 * @return transformed source, now in target CRS
	 * @since 1.8.0
	 */
	Geometry transform(Geometry source, CrsTransform crsTransform);

	/**
	 * Transform a {@link Geometry} from the source to the target CRS.
	 * <p/>
	 * An empty geometry is returned (and a warning logged) if the transformation failed.
	 *
	 * @param source source geometry
	 * @param sourceCrs source CRS
	 * @param targetCrs target CRS
	 * @return transformed source, now in target CRS
	 * @throws GeomajasException building the transformation failed
	 * @since 1.8.0
	 */
	Geometry transform(Geometry source, Crs sourceCrs, Crs targetCrs) throws GeomajasException;

	/**
	 * Transform a {@link Geometry} from the source to the target CRS.
	 * <p/>
	 * An empty geometry is returned (and a warning logged) if the transformation failed.
	 *
	 * @param source source geometry
	 * @param sourceCrs source CRS
	 * @param targetCrs target CRS
	 * @return transformed source, now in target CRS
	 * @throws GeomajasException building the transformation failed
	 * @since 1.8.0
	 */
	Geometry transform(Geometry source, String sourceCrs, String targetCrs) throws GeomajasException;

	/**
	 * Transform a {@link Geometry} from the source to the target CRS.
	 *
	 * @param source source geometry
	 * @param sourceCrs source CRS
	 * @param targetCrs target CRS
	 * @return transformed source, now in target CRS
	 * @throws GeomajasException building the transformation
	 * @since 1.7.0
	 * @deprecated use transform(Geometry, Crs, Crs) instead
	 */
	@Deprecated
	Geometry transform(Geometry source, CoordinateReferenceSystem sourceCrs, CoordinateReferenceSystem targetCrs)
			throws GeomajasException;

	/**
	 * Transform a {@link Envelope} using the given transformation.
	 * <p/>
	 * An empty envelope is returned (and a warning logged) if the transformation failed.
	 *
	 * @param source source envelope
	 * @param crsTransform transformation to be applied
	 * @return transformed source, now in target CRS
	 * @since 1.8.0
	 */
	Envelope transform(Envelope source, CrsTransform crsTransform);

	/**
	 * Transform a {@link Envelope} from the source to the target CRS.
	 * <p/>
	 * An empty envelope is returned (and a warning logged) if the transformation failed.
	 *
	 * @param source source geometry
	 * @param sourceCrs source CRS
	 * @param targetCrs target CRS
	 * @return transformed source, now in target CRS
	 * @throws GeomajasException building the transformation failed
	 * @since 1.8.0
	 */
	Envelope transform(Envelope source, Crs sourceCrs, Crs targetCrs) throws GeomajasException;

	/**
	 * Transform a {@link Envelope} from the source to the target CRS.
	 * <p/>
	 * An empty envelope is returned (and a warning logged) if the transformation failed.
	 *
	 * @param source source geometry
	 * @param sourceCrs source CRS
	 * @param targetCrs target CRS
	 * @return transformed source, now in target CRS
	 * @throws GeomajasException building the transformation failed
	 * @since 1.8.0
	 */
	Envelope transform(Envelope source, String sourceCrs, String targetCrs) throws GeomajasException;

	/**
	 * Transform a {@link Bbox} using the given transformation.
	 * <p/>
	 * An empty bbox is returned (and a warning logged) if the transformation failed.
	 *
	 * @param source source bbox
	 * @param crsTransform transformation to be applied
	 * @return transformed source, now in target CRS
	 * @since 1.8.0
	 */
	Bbox transform(Bbox source, CrsTransform crsTransform);

	/**
	 * Transform a {@link Bbox} from the source to the target CRS.
	 * <p/>
	 * An empty bbox is returned (and a warning logged) if the transformation failed.
	 *
	 * @param source source bbox
	 * @param sourceCrs source CRS
	 * @param targetCrs target CRS
	 * @return transformed source, now in target CRS
	 * @throws GeomajasException building the transformation
	 * @since 1.8.0
	 */
	Bbox transform(Bbox source, Crs sourceCrs, Crs targetCrs) throws GeomajasException;

	/**
	 * Transform a {@link Bbox} from the source to the target CRS.
	 * <p/>
	 * An empty bbox is returned (and a warning logged) if the transformation failed.
	 *
	 * @param source source bbox
	 * @param sourceCrs source CRS
	 * @param targetCrs target CRS
	 * @return transformed source, now in target CRS
	 * @throws GeomajasException building the transformation
	 * @since 1.8.0
	 */
	Bbox transform(Bbox source, String sourceCrs, String targetCrs) throws GeomajasException;

	/**
	 * Transform a {@link Coordinate} using the given transformation.
	 * <p/>
	 * Null is returned (and a warning logged) if the transformation failed.
	 *
	 * @param source source coordinate
	 * @param crsTransform transformation to be applied
	 * @return transformed source, now in target CRS
	 * @since 1.9.0
	 */
	Coordinate transform(Coordinate source, CrsTransform crsTransform);

	/**
	 * Transform a {@link Coordinate} from the source to the target CRS.
	 * <p/>
	 * An empty bbox is returned (and a warning logged) if the transformation failed.
	 *
	 * @param source source bbox
	 * @param sourceCrs source CRS
	 * @param targetCrs target CRS
	 * @return transformed source, now in target CRS
	 * @throws GeomajasException building the transformation
	 * @since 1.9.0
	 */
	Coordinate transform(Coordinate source, Crs sourceCrs, Crs targetCrs) throws GeomajasException;

	/**
	 * Transform a {@link Coordinate} from the source to the target CRS.
	 * <p/>
	 * An empty bbox is returned (and a warning logged) if the transformation failed.
	 *
	 * @param source source bbox
	 * @param sourceCrs source CRS
	 * @param targetCrs target CRS
	 * @return transformed source, now in target CRS
	 * @throws GeomajasException building the transformation
	 * @since 1.8.0
	 */
	Coordinate transform(Coordinate source, String sourceCrs, String targetCrs) throws GeomajasException;

	/**
	 * Determine a default position for positioning the label for a feature.
	 *
	 * @param feature feature which needs the label
	 * @return coordinate where the label would make sense.
	 */
	Coordinate calcDefaultLabelPosition(InternalFeature feature);

	/**
	 * Create a geometry which approximates like a circle.
	 *
	 * @param center center for the circle
	 * @param radius radius for the circle
	 * @param nrPoints number of points to calculate for approximating the circle
	 * @return created circle
	 */
	Geometry createCircle(Point center, double radius, int nrPoints);
}
