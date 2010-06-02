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

package org.geomajas.global;

/**
 * Constants to be used for setting the exception codes.
 * The actual messages are maintained in the GeomajasException resource bundle
 *
 * @author Joachim Van der Auwera
 * @since 1.6.0
 */
@Api(allMethods = true)
public interface ExceptionCode {

	int TEST = -1;
	int UNKNOWN = 0;

	int MAP_MAX_EXTENT_MISSING = 1;
	int LAYER_CRS_PROBLEMATIC = 2;
	int LAYER_CRS_UNKNOWN_AUTHORITY = 3;
	int TRANSFORMER_CREATE_LAYER_TO_MAP_FAILED = 4;
	int ATTRIBUTE_UNKNOWN = 5;
	int LAYER_NOT_FOUND = 6;
	int CRS_TRANSFORMATION_NOT_POSSIBLE = 7;
	int GEOMETRY_TRANSFORMATION_FAILED = 8;
	int VECTOR_LAYER_NOT_FOUND = 9;
	int COMMAND_ACCESS_DENIED = 10;
	int LAYER_NOT_VISIBLE = 11;
	int FEATURE_CREATE_PROHIBITED = 12;
	int FEATURE_UPDATE_PROHIBITED = 13;
	int FEATURE_DELETE_PROHIBITED = 14;
	int FEATURE_ID_MISMATCH = 15;
	int PIPELINE_UNKNOWN = 16;
	int PIPELINE_CONTEXT_MISSING = 17;
	int CONVERSION_PROBLEM = 18;
	int RASTER_LAYER_NOT_FOUND = 19;
	int REFRESH_CONFIGURATION_FAILED = 20;
	int APPLICATION_NOT_FOUND = 21;
	int MAP_NOT_FOUND = 22;
	int CRS_DECODE_FAILURE_FOR_MAP = 23;
	int RENDER_FEATURE_MODEL_PROBLEM = 24;
	int RENDER_TRANSFORMATION_FAILED = 25;
	int FILTER_PARSE_PROBLEM = 26;
	int RENDER_DOCUMENT_IO_EXCEPTION = 28;
	int RENDER_DOCUMENT_EXPECTED_ATTRIBUTE_VALUE = 29;
	int RENDER_DOCUMENT_UNEXPECTED_ATTRIBUTE_END = 30;
	int RENDER_DOCUMENT_NO_REGISTERED_WRITER = 31;
	int INVALID_SHAPE_FILE_URL = 32;
	int INVALID_FEATURE_OBJECT = 33;
	int FEATURE_MODEL_PROBLEM = 34;
	int CANNOT_CREATE_LAYER_MODEL = 35;
	int RENDERER_TYPE_NOT_SUPPORTED = 36;
	int CREATE_FEATURE_NO_FEATURE_TYPE = 37;
	int UNEXPECTED_PROBLEM = 38;
	int LAYER_MODEL_IO_EXCEPTION = 39;
	int CREATE_OR_UPDATE_NOT_IMPLEMENTED = 40;
	int DELETE_NOT_IMPLEMENTED = 41;
	int LAYER_MODEL_FEATURE_NOT_FOUND = 42;
	int RENDER_DIMENSION_MISMATCH = 43;
	int HIBERNATE_COULD_NOT_RESOLVE = 44;
	int HIBERNATE_NO_META_DATA = 45;
	int HIBERNATE_NO_SESSION_FACTORY = 46;
	int HIBERNATE_LOAD_FILTER_FAIL = 47;
	int HIBERNATE_ATTRIBUTE_TYPE_PROBLEM = 48;
	int HIBERNATE_ATTRIBUTE_SET_FAILED = 49;
	int HIBERNATE_ATTRIBUTE_GET_FAILED = 50;
	int HIBERNATE_ATTRIBUTE_ALL_GET_FAILED = 51;
	int PROPERTY_IS_NOT_GEOMETRY = 52;
	int HIBERNATE_CANNOT_CREATE_POJO = 53;
	int GEOMETRY_SET_FAILED = 54;
	int CANNOT_DETERMINE_CLASS_FOR_FEATURE = 55;
	int MODEL_FEATURE_CLASS_NOT_FOUND = 56;
	int COMMAND_NOT_FOUND = 57;
	int NOT_IMPLEMENTED = 58;
	int PRINT_TEMPLATE_XML_PROBLEM = 59;
	int PRINT_TEMPLATE_PERSIST_PROBLEM = 60;
	int MERGE_NO_POLYGON = 61;
	int PARAMETER_MISSING = 62;
	int CREDENTIALS_MISSING_OR_INVALID = 63;
	int CANNOT_CONVERT_GEOMETRY = 64;
	int LAYER_EXTENT_CANNOT_CONVERT = 65;

}
