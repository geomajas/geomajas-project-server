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

dojo.provide("geomajas.spatial.Matrix3D");
dojo.declare("Matrix3D", null, {

	/**
	 * @fileoverview 3D matrices can be used for 2D homogenous coordinates...
	 * @class 3D matrices can be used for 2D homogenous coordinates...
	 * @author Pieter De Graef
	 *
	 * @constructor
	 * @param elements A 2-dimensional array of elements. Like this :
	 *                 [ [x11, x12, x13], [x21, x22, x23], [x31, x32, x33] ].
	 */
	constructor : function (elements) {
		this.elements = [];
		this.elements[0] = [];
		this.elements[1] = [];
		this.elements[2] = [];
		if (elements != null) {
			this.elements[0][0] = elements[0][0];
			this.elements[0][1] = elements[0][1];
			this.elements[0][2] = elements[0][2];
			this.elements[1][0] = elements[1][0];
			this.elements[1][1] = elements[1][1];
			this.elements[1][2] = elements[1][2];
			this.elements[2][0] = elements[2][0];
			this.elements[2][1] = elements[2][1];
			this.elements[2][2] = elements[2][2];
		} else {
			this.elements[0][0] = 0;
			this.elements[0][1] = 0;
			this.elements[0][2] = 0;
			this.elements[1][0] = 0;
			this.elements[1][1] = 0;
			this.elements[1][2] = 0;
			this.elements[2][0] = 0;
			this.elements[2][1] = 0;
			this.elements[2][2] = 0;
		}
	},
	
	/**
	 * Make a clone of the matrix.
	 * @returns: A Matrix3D object, with the same elements.
	 */
	clone : function () {
		return new Matrix2D(this.elements);
	},
	
	/**
	 * Is the given matrix object equal to this matrix object?
	 * @param matrix The matrix to compare to this one.
	 */
	equals : function (matrix) {
		return (this.elements[0][0] == matrix.elements[0][0]
		     && this.elements[0][1] == matrix.elements[0][1]
		     && this.elements[0][2] == matrix.elements[0][2]
		     && this.elements[1][0] == matrix.elements[1][0]
		     && this.elements[1][1] == matrix.elements[1][1]
		     && this.elements[1][2] == matrix.elements[1][2]
		     && this.elements[2][0] == matrix.elements[2][0]
		     && this.elements[2][1] == matrix.elements[2][0]
		     && this.elements[2][2] == matrix.elements[2][2]);
	},
	
	/**
	 * Set a value at the given indeces.
	 * @param i The row-count (0, 1 or 2).
	 * @param j The column-count (0, 1 or 2).
	 * @param value The value to be set.
	 */
	set : function (i, j, value) {
		if (0 <= i < 3 && 0 <= j < 3) {
			this.elements[i][j] = value;
		}
	},
	
	/**
	 * Get a value at the given indeces.
	 * @param i The row-count (0, 1 or 2).
	 * @param j The column-count (0, 1 or 2).
	 * @returns The value found at the given indices.
	 */
	get : function (i, j) {
		return this.elements[i][j];
	},

	/**
	 * Add the given matrix to the current one, and return the result.
	 * @param matrix The matrix to be added to this one.
	 * @returns The result matrix.
	 */
	add : function(matrix) {
		return new Matrix3D([
			[ this.elements[0][0] + matrix.elements[0][0], this.elements[0][1] + matrix.elements[0][1], this.elements[0][2] + matrix.elements[0][2] ],
			[ this.elements[1][0] + matrix.elements[1][0], this.elements[1][1] + matrix.elements[1][1], this.elements[1][2] + matrix.elements[1][2] ],
			[ this.elements[2][0] + matrix.elements[2][0], this.elements[2][1] + matrix.elements[2][1], this.elements[2][2] + matrix.elements[2][2] ]
		]);
	},
	
	/**
	 * Subtract the given matrix from the current one, and return the result.
	 * @param matrix The matrix to be subtracted from this one.
	 * @returns The result matrix.
	 */
	subtract : function (matrix) {
		return new Matrix3D([
			[ this.elements[0][0] - matrix.elements[0][0], this.elements[0][1] - matrix.elements[0][1], this.elements[0][2] - matrix.elements[0][2] ],
			[ this.elements[1][0] - matrix.elements[1][0], this.elements[1][1] - matrix.elements[1][1], this.elements[1][2] - matrix.elements[1][2] ],
			[ this.elements[2][0] - matrix.elements[2][0], this.elements[2][1] - matrix.elements[2][1], this.elements[2][2] - matrix.elements[2][2] ]
		]);
	},
	
	/**
	 * Multiply the given matrix with the current one, and return the result.
	 * @param matrix The matrix to be multiplied with this one. We expect a 3x3 matrix again.
	 * @returns The result matrix.
	 */
	multiply : function (matrix) {
		return new Matrix3D([
			[ this.elements[0][0]*matrix.elements[0][0] + this.elements[0][1]*matrix.elements[1][0] + this.elements[0][2]*matrix.elements[2][0], 
			  this.elements[0][0]*matrix.elements[0][1] + this.elements[0][1]*matrix.elements[1][1] + this.elements[0][2]*matrix.elements[2][1],
			  this.elements[0][0]*matrix.elements[0][2] + this.elements[0][1]*matrix.elements[1][2] + this.elements[0][2]*matrix.elements[2][2] ],

			[ this.elements[1][0]*matrix.elements[0][0] + this.elements[1][1]*matrix.elements[1][0] + this.elements[1][2]*matrix.elements[2][0], 
			  this.elements[1][0]*matrix.elements[0][1] + this.elements[1][1]*matrix.elements[1][1] + this.elements[1][2]*matrix.elements[2][1],
			  this.elements[1][0]*matrix.elements[0][2] + this.elements[1][1]*matrix.elements[1][2] + this.elements[1][2]*matrix.elements[2][2] ],

			[ this.elements[2][0]*matrix.elements[0][0] + this.elements[2][1]*matrix.elements[1][0] + this.elements[2][2]*matrix.elements[2][0], 
			  this.elements[2][0]*matrix.elements[0][1] + this.elements[2][1]*matrix.elements[1][1] + this.elements[2][2]*matrix.elements[2][1],
			  this.elements[2][0]*matrix.elements[0][2] + this.elements[2][1]*matrix.elements[1][2] + this.elements[2][2]*matrix.elements[2][2] ]
		]);
	},

	/**
	 * Rotate this matrix over a certain angle(radial) and return the result.
	 * @param angle The radial angle we should rotate this matrix over. Should be a normalized 3D vector right? Gotta look this up...
	 * @returns return the resulting Matrix3D.
	 */
	rotate : function (/*Vector3D*/angle) {
		alert("Matrix3D:rotate => to be implemented");
	},
	
	/**
	 * Rotate this matrix over the Z-axis and result the result.
	 * @param angle The radial angle (double).
	 * @returns The resulting Matrix3D.
	 */
	rotateZ : function(/*Double*/angle) {
		var c = Math.cos(t), s = Math.sin(t);
		var rotMat = new Matrix3D ([
			[  c, -s,  0 ],
			[  s,  c,  0 ],
			[  0,  0,  1 ]
		]);
		return this.multiply (rotMat);
	},

	/**
	 * Scale this matrix and return the result.
	 * @param x The first scaling factor.
	 * @param y The second scaling factor.
	 * @param z The third scaling factor.
	 * @returns return the resulting Matrix3D.
	 */
	scale : function (x, y, z) {
		var scaleMat = new Matrix3D ([ [x, 0, 0], [0, y, 0], [0, 0, z] ]);
		return this.multiply (scaleMat);
	},
	
	toString : function () {
		alert("Matrix3D:toString => implement me");
	}

});


/**
 * Simple and direct method for returning a 3D identity matrix.
 */
Matrix3D.identity = function () {
	return new Matrix3D ([ [1, 0, 0], [0, 1, 0], [0, 0, 1] ]);
}
