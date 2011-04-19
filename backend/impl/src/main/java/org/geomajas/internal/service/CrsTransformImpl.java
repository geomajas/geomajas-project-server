/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2011 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */

package org.geomajas.internal.service;

import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.Geometry;
import org.geomajas.geometry.Bbox;
import org.geomajas.geometry.Crs;
import org.geomajas.geometry.CrsTransform;
import org.geotools.geometry.jts.JTS;
import org.opengis.geometry.DirectPosition;
import org.opengis.geometry.MismatchedDimensionException;
import org.opengis.referencing.operation.MathTransform;
import org.opengis.referencing.operation.Matrix;
import org.opengis.referencing.operation.NoninvertibleTransformException;
import org.opengis.referencing.operation.TransformException;

/**
 * Extension of a {@link MathTransform} which allows getting the source and target CRSs and a
 * {@link com.vividsolutions.jts.geom.Geometry} with the transformable area.
 *
 * @author Joachim Van der Auwera
 */
public class CrsTransformImpl implements CrsTransform {

	private String id;
	private Crs source;
	private Crs target;
	private MathTransform mathTransform;
	private Geometry transformableGeometry;
	private Envelope transformableEnvelope;
	private Bbox transformableBbox;
	private boolean transforming;

	public CrsTransformImpl(String id, Crs source, Crs target, MathTransform mathTransform) {
		this.id = id;
		this.source = source;
		this.target = target;
		this.mathTransform = mathTransform;
		transforming = !(getSource().equals(getTarget()));
	}

	public CrsTransformImpl(String id, Crs source, Crs target, MathTransform mathTransform,
			Bbox transformableBbox) {
		this(id, source, target, mathTransform);
		if (null != transformableBbox) {
			this.transformableBbox = transformableBbox;
			this.transformableEnvelope =
					new Envelope(transformableBbox.getX(), transformableBbox.getMaxX(), transformableBbox.getY(),
							transformableBbox.getMaxY());
			this.transformableGeometry = JTS.toGeometry(transformableEnvelope);
		}
	}

	public CrsTransformImpl(String id, Crs source, Crs target, MathTransform mathTransform,
			Envelope transformableEnvelope) {
		this(id, source, target, mathTransform);
		if (null != transformableEnvelope) {
			this.transformableEnvelope = transformableEnvelope;
			this.transformableBbox = new Bbox(transformableEnvelope.getMinX(), transformableEnvelope.getMinY(),
					transformableEnvelope.getMaxX(), transformableEnvelope.getMaxY());
			this.transformableGeometry = JTS.toGeometry(transformableEnvelope);
		}
	}

	public String getId() {
		return id;
	}

	public Crs getSource() {
		return source;
	}

	public Crs getTarget() {
		return target;
	}

	public Geometry getTransformableGeometry() {
		return transformableGeometry;
	}

	public Envelope getTransformableEnvelope() {
		return transformableEnvelope;
	}

	public Bbox getTransformableBbox() {
		return transformableBbox;
	}

	public int getSourceDimensions() {
		return mathTransform.getSourceDimensions();
	}

	public int getTargetDimensions() {
		return mathTransform.getTargetDimensions();
	}

	public boolean isTransforming() {
		return transforming;
	}

	public DirectPosition transform(DirectPosition directPosition, DirectPosition directPosition1)
			throws MismatchedDimensionException, TransformException {
		return mathTransform.transform(directPosition, directPosition1);
	}

	public void transform(double[] doubles, int i, double[] doubles1, int i1, int i2) throws TransformException {
		mathTransform.transform(doubles, i, doubles1, i1, i2);
	}

	public void transform(float[] floats, int i, float[] floats1, int i1, int i2) throws TransformException {
		mathTransform.transform(floats, i, floats1, i1, i2);
	}

	public void transform(float[] floats, int i, double[] doubles, int i1, int i2) throws TransformException {
		mathTransform.transform(floats, i, doubles, i1, i2);
	}

	public void transform(double[] doubles, int i, float[] floats, int i1, int i2) throws TransformException {
		mathTransform.transform(doubles, i, floats, i1, i2);
	}

	public Matrix derivative(DirectPosition directPosition) throws MismatchedDimensionException, TransformException {
		return mathTransform.derivative(directPosition);
	}

	public MathTransform inverse() throws NoninvertibleTransformException {
		return mathTransform.inverse();
	}

	public boolean isIdentity() {
		return mathTransform.isIdentity();
	}

	public String toWKT() throws UnsupportedOperationException {
		return mathTransform.toWKT();
	}
}
