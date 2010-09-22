/*
 * This file is part of Geomajas, a component framework for building
 * rich Internet applications (RIA) with sophisticated capabilities for the
 * display, analysis and management of geographic information.
 * It is a building block that allows developers to add maps
 * and other geographic data capabilities to their web applications.
 *
 * Copyright 2008-2010 Geosparc, http://www.geosparc.com, Belgium
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

dojo.provide("geomajas.spatial.Matrix2D");
dojo.declare("Matrix2D", null, {

	/**
	 * @fileoverview Simple implementation of a 2x2 matrix.
	 * @class Simple implementation of a 2x2 matrix.
	 * @author Pieter De Graef
	 *
	 * @constructor
	 * @param elements 2-dimensional array of elements.<br/>
	 *                 Like this : [ [x11, x12], [x21, x22] ].
	 */
	constructor : function (elements) {
		this.elements = [];
		this.elements[0] = [];
		this.elements[1] = [];
		if (elements != null) {
			this.elements[0][0] = elements[0][0];
			this.elements[0][1] = elements[0][1];
			this.elements[1][0] = elements[1][0];
			this.elements[1][1] = elements[1][1];
		} else {
			this.elements[0][0] = 0;
			this.elements[0][1] = 0;
			this.elements[1][0] = 0;
			this.elements[1][1] = 0;
		}
	},
	
	/**
	 * Make a clone of the matrix.
	 * @returns A Matrix2D object, with the same elements.
	 */
	clone : function () {
		return new Matrix2D(this.elements);
	},
	
	/**
	 * Is the given matrix object equal to this matrix object?
	 * @param matrix The matrix to compare to this one.
	 */
	equals : function (/*Matrix2D*/matrix) {
		return (this.elements[0][0] == matrix.elements[0][0]
		     && this.elements[0][1] == matrix.elements[0][1]
		     && this.elements[1][0] == matrix.elements[1][0]
		     && this.elements[1][1] == matrix.elements[1][1]);
	},

	/**
	 * Set a value at the given indeces.
	 * @param i The row-count (0 or 1).
	 * @param j The column-count (0 or 1).
	 * @param value The value to be set. 
	 */
	set : function (/*Integer*/i, /*Integer*/j, /*Double*/value) {
		if (0 <= i < 2 && 0 <= j < 2) {
			this.elements[i][j] = value;
		}
	},
	
	/**
	 * Get a value at the given indeces.
	 * @param i The row-count (0 or 1).
	 * @param j The column-count (0 or 1).
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
	add : function(/*Matrix2D*/matrix) {
		return new Matrix2D([
			[ this.elements[0][0] + matrix.elements[0][0], this.elements[0][1] + matrix.elements[0][1] ],
			[ this.elements[1][0] + matrix.elements[1][0], this.elements[1][1] + matrix.elements[1][1] ]
		]);
	},
	
	/**
	 * Subtract the given matrix from the current one, and return the result.
	 * @param matrix The matrix to be subtracted from this one.
	 * @returns The result matrix.
	 */
	subtract : function (/*Matrix2D*/matrix) {
		return new Matrix2D([
			[ this.elements[0][0] - matrix.elements[0][0], this.elements[0][1] - matrix.elements[0][1] ],
			[ this.elements[1][0] - matrix.elements[1][0], this.elements[1][1] - matrix.elements[1][1] ]
		]);
	},
	
	/**
	 * Multiply the given matrix with the current one, and return the result.
	 * @param matrix The matrix to be multiplied with this one. We expect a 2x2 matrix again.
	 * @returns The result matrix.
	 */
	multiply : function (/*Matrix2D*/matrix) {
		return new Matrix2D([
			[ this.elements[0][0]*matrix.elements[0][0] + this.elements[0][1]*matrix.elements[1][0], 
			  this.elements[0][0]*matrix.elements[0][1] + this.elements[0][1]*matrix.elements[1][1] ],
			[ this.elements[1][0]*matrix.elements[0][0] + this.elements[1][1]*matrix.elements[1][0],
			  this.elements[1][0]*matrix.elements[0][1] + this.elements[1][1]*matrix.elements[1][1] ]
		]);
	},

	/**
	 * Multiply this matrix with a Vector2D object and return the result.
	 * @param vector The vector this matrix is to be multiplied with.
	 * @returns Returns the vector2D result.
	 */
	multiplyVector : function (/*Vector2D*/vector) {
		return new Vector2D (
			this.elements[0][0] * vector.get(0) + this.elements[0][1] * vector.get(1),
			this.elements[1][0] * vector.get(0) + this.elements[1][1] * vector.get(1)
		);
	},

	/**
	 * Rotate this matrix over a certain angle(radial) and return the result.
	 * @param angle The radial angle we should rotate this matrix over.
	 * @returns return the resulting Matrix2D.
	 */
	rotate : function (angle) {
		var rotMat = new Matrix2D ([
			[Math.cos(angle), -Math.sin(angle)],
			[Math.sin(angle),  Math.cos(angle)]
		]);
		return this.multiply (rotMat);
	},
	
	/**
	 * Scale this matrix and return the result.
	 * @param x The first scaling factor.
	 * @param y The second scaling facator.
	 * @returns return the resulting Matrix2D.
	 */
	scale : function (x, y) {
		var scaleMat = new Matrix2D ([ [x, 0], [0, y] ]);
		return this.multiply (scaleMat);
	},
	
	/**
	 * Guess...
	 */
	toString : function () {
		return "[ ["+this.elements[0][0]+", "+this.elements[0][1]+"], ["+this.elements[1][0]+", "+this.elements[1][1]+"] ]";
	}

});

/**
 * Simple and direct method for returning a 2D identity matrix.
 */
Matrix2D.identity = function () {
	return new Matrix2D ([ [1, 0], [0, 1] ]);
}
