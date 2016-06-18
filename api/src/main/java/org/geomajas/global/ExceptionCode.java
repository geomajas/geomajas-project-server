/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2016 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */

package org.geomajas.global;

import org.geomajas.annotation.Api;

/**
 * Constants to be used for setting the exception codes.
 * The actual messages are maintained in the GeomajasException resource bundle
 *
 * @author Joachim Van der Auwera
 * @since 1.6.0
 */
@Api(allMethods = true)
public interface ExceptionCode {

	/** Exception code for testing. */
	int TEST = -1;
	/**
	 * Unknown error.
	 * @deprecated use a real exception code
	 */
	@Deprecated
	int UNKNOWN = 0;

	/** MaxExtent missing for map. */
	int MAP_MAX_EXTENT_MISSING = 1;
	/** Layer ${0} has problematic CRS ${1}. */
	int LAYER_CRS_PROBLEMATIC = 2;
	/** Layer ${0} has CRS ${1} with unknown authority code. */
	int LAYER_CRS_UNKNOWN_AUTHORITY = 3;
	/** Could not create CRS transformer to transform geometries from layer to map coordinate system. */
	int TRANSFORMER_CREATE_LAYER_TO_MAP_FAILED = 4;
	/** Attribute ${0} is unknown, known attributes are ${1}. */
	int ATTRIBUTE_UNKNOWN = 5;
	/** Layer ${0} not found. */
	int LAYER_NOT_FOUND = 6;
	/** Transformation from CRS ${0} to ${1} not possible. */
	int CRS_TRANSFORMATION_NOT_POSSIBLE = 7;
	/** Transformation of geometry to required CRS failed. */
	int GEOMETRY_TRANSFORMATION_FAILED = 8;
	/** Vector layer ${0} not found. The layer either does not exist or does not implement VectorLayer. */
	int VECTOR_LAYER_NOT_FOUND = 9;
	/** User ${1} is not authorized to use the command ${0}. */
	int COMMAND_ACCESS_DENIED = 10;
	/** Layer ${0} does not exist or is not visible for user ${1}. */
	int LAYER_NOT_VISIBLE = 11;
	/** Create of new feature not allowed for ${0}. */
	int FEATURE_CREATE_PROHIBITED = 12;
	/** Update of feature ${0} not allowed for ${1}. */
	int FEATURE_UPDATE_PROHIBITED = 13;
	/** Delete of feature ${0} not allowed for ${1}. */
	int FEATURE_DELETE_PROHIBITED = 14;
	/**
	 * saveAndUpdate requires features at the same index in old and new lists to match (have the same id), problem
	 * at index ${0}.
	 */
	int FEATURE_ID_MISMATCH = 15;
	/** Pipeline ${0} is unknown, it was requested for layer ${1}. */
	int PIPELINE_UNKNOWN = 16;
	/** Pipeline context is expected to have a suitable value for key ${0}, but it is missing. */
	int PIPELINE_CONTEXT_MISSING = 17;
	/** Unexpected error occurred while converting object ${0}. */
	int CONVERSION_PROBLEM = 18;
	/** Raster layer ${0} not found. The layer either does not exist or does not implement RasterLayer. */
	int RASTER_LAYER_NOT_FOUND = 19;
	/** Configuration cannot be reloaded, context is not an instance of ReconfigurableApplicationContext. */
	int REFRESH_CONFIGURATION_FAILED = 20;
	/** Application ${0} not found. */
	int APPLICATION_NOT_FOUND = 21;
	/** Map ${0} in application ${1} not found. */
	int MAP_NOT_FOUND = 22;
	/** Problem decoding CRS for map ${0}. */
	int CRS_DECODE_FAILURE_FOR_MAP = 23;
	/** Feature model problem while rendering feature. */
	int RENDER_FEATURE_MODEL_PROBLEM = 24;
	/** Could not render, transformation failed. */
	int RENDER_TRANSFORMATION_FAILED = 25;
	/** The filter "${0}" cannot be parsed. */
	int FILTER_PARSE_PROBLEM = 26;
	/** IOException while rendering document. */
	int RENDER_DOCUMENT_IO_EXCEPTION = 28;
	/** Expected attribute value. */
	int RENDER_DOCUMENT_EXPECTED_ATTRIBUTE_VALUE = 29;
	/** Unexpected end of attribute. */
	int RENDER_DOCUMENT_UNEXPECTED_ATTRIBUTE_END = 30;
	/** No registered writer for ${0}. */
	int RENDER_DOCUMENT_NO_REGISTERED_WRITER = 31;
	/** Invalid shape file url ${0}. */
	int INVALID_SHAPE_FILE_URL = 32;
	/** Not a legal feature, does not implement the "Feature" interface ${0}. */
	int INVALID_FEATURE_OBJECT = 33;
	/** Feature model problem. */
	int FEATURE_MODEL_PROBLEM = 34;
	/** Cannot create layer model for ${0}. */
	int CANNOT_CREATE_LAYER_MODEL = 35;
	/**  Unsupported renderer type ${0}. */
	int RENDERER_TYPE_NOT_SUPPORTED = 36;
	/** Could not create new feature, FeatureType was not found. */
	int CREATE_FEATURE_NO_FEATURE_TYPE = 37;
	/** Unexpected problem. */
	int UNEXPECTED_PROBLEM = 38;
	/** IOException in layer model. */
	int LAYER_MODEL_IO_EXCEPTION = 39;
	/** Don't know how to create or update ${0}, class ${1} does not implement FeatureStore. */
	int CREATE_OR_UPDATE_NOT_IMPLEMENTED = 40;
	/** Don't know how to delete ${0}, class ${1} does not implement FeatureStore. */
	int DELETE_NOT_IMPLEMENTED = 41;
	/** Feature with id ${0} could not be found. */
	int LAYER_MODEL_FEATURE_NOT_FOUND = 42;
	/** Dimension mismatch. */
	int RENDER_DIMENSION_MISMATCH = 43;
	/** Could not resolve ${0} for ${1}. */
	int HIBERNATE_COULD_NOT_RESOLVE = 44;
	/** Could not get metadata for ${0}. */
	int HIBERNATE_NO_META_DATA = 45;
	/** Could not configure session factory.*/
	int HIBERNATE_NO_SESSION_FACTORY = 46;
	/** Failed to load ${0} with filter ${1}. */
	int HIBERNATE_LOAD_FILTER_FAIL = 47;
	/** Attribute ${0} is not an object type. */
	int HIBERNATE_ATTRIBUTE_TYPE_PROBLEM = 48;
	/** Failed to set attributes for ${0}. */
	int HIBERNATE_ATTRIBUTE_SET_FAILED = 49;
	/** Getting attribute ${0} failed for ${1}. */
	int HIBERNATE_ATTRIBUTE_GET_FAILED = 50;
	/** Getting all attributes failed for ${0}. */
	int HIBERNATE_ATTRIBUTE_ALL_GET_FAILED = 51;
	/** Property ${0} is not of type Geometry. */
	int PROPERTY_IS_NOT_GEOMETRY = 52;
	/** Unexpected failure to create a POJO for entity ${0}. */
	int HIBERNATE_CANNOT_CREATE_POJO = 53;
	/** Failed to set geometry for ${0}. */
	int GEOMETRY_SET_FAILED = 54;
	/** Cannot determine class for feature ${0}. You should not overwrite "toString()" for the feature. */
	int CANNOT_DETERMINE_CLASS_FOR_FEATURE = 55;
	/** Feature class ${0} not found. */
	int MODEL_FEATURE_CLASS_NOT_FOUND = 56;
	/** Command ${0} not found, maybe you need to install the extension which contains it. */
	int COMMAND_NOT_FOUND = 57;
	/** Not implemented. */
	int NOT_IMPLEMENTED = 58;
	/** Print template ${0} cannot be converted to XML. */
	int PRINT_TEMPLATE_XML_PROBLEM = 59;
	/** Print template ${0} could not be persisted. */
	int PRINT_TEMPLATE_PERSIST_PROBLEM = 60;
	/** Merge result was not a normal polygon. */
	int MERGE_NO_POLYGON = 61;
	/** Parameter ${0} is required but not given. */
	int PARAMETER_MISSING = 62;
	/** Security credentials (token ${0}) missing or invalid. */
	int CREDENTIALS_MISSING_OR_INVALID = 63;

	/**
	 * Cannot convert geometry, type ${0} unknown.
	 *
	 * @since 1.7.0
	 */
	int CANNOT_CONVERT_GEOMETRY = 64;

	/**
	 * Convert layer extent to map CRS ${1} failed for layer ${0}.
	 *
	 * @since 1.7.0
	 */
	int LAYER_EXTENT_CANNOT_CONVERT = 65;

	/**
	 * Pipeline ${0} for layer ${1} contains extensions for non-existing hooks.
	 *
	 * @since 1.7.0
	 */
	int PIPELINE_UNSATISFIED_EXTENSION = 66;

	/**
	 * Map ${0} uses mixed scale notations (x:y and z), which is not allowed. Maybe you should use 1:1 for scale 1.
	 *
	 * @since 1.7.1
	 */
	int SCALE_CONVERSION_PROBLEM = 67;

	/**
	 * Cannot parse geometry ${0}, invalid WKT expression.
	 *
	 * @since 1.8.0
	 */
	int CANNOT_PARSE_WKT_GEOMETRY = 68;

	/**
	 * Pipeline interceptor ${0} contains non-existing step ${1}.
	 *
	 * @since 1.9.0
	 */
	int PIPELINE_INTERCEPTOR_INVALID_STEP = 69;
	/**
	 * Pipeline interceptor ${0} contains a fromStep which is after the toStep in the pipeline.
	 *
	 * @since 1.9.0
	 */
	int PIPELINE_INTERCEPTOR_STEPS_ORDER = 70;
	/**
	 * A pipeline definition for ${0} contains both a pipeline definition and a delegate pipeline. Only one of both
	 * should be used.
	 *
	 * @since 1.9.0
	 */
	int PIPELINE_DEFINED_AND_DELEGATE = 71;
	/**
	 * Pipeline interceptor nesting invalid, includes interceptor ${0}.
	 *
	 * @since 1.9.0
	 */
	int PIPELINE_INTERCEPTOR_INVALID_NESTING = 72;
	/**
	 * Filter ${0} cannot be evaluated for layer ${1}.
	 *
	 * @since 1.9.0
	 */
	int FILTER_EVALUATION_PROBLEM = 73;
	/**
	 * Plug-in dependencies not met: ${0}.
	 *
	 * @since 1.9.0
	 */
	int DEPENDENCY_CHECK_FAILED = 74;
	/**
	 * Invalid configuration, the plug-in with name ${0} has been declared twice. It is know both as version ${1} and
	 * ${2}. The plug-in name is either used by more than one plug-in or the plug-in is on the classpath more than once.
	 *
	 * @since 1.9.0
	 */
	int DEPENDENCY_CHECK_INVALID_DUPLICATE = 75;
	/**
	 * Invalid attribute name ${0} in layer ${1}.
	 *
	 * @since 1.9.0
	 */
	int INVALID_ATTRIBUTE_NAME = 76;
	/**
	 * Invalid value for parameter ${0}.
	 *
	 * @since 1.9.0
	 */
	int PARAMETER_INVALID_VALUE = 77;
	/**
	 * Invalid SLD ${0} for layer ${1}.
	 *
	 * @since 1.9.0
	 */
	int INVALID_SLD = 78;
	/**
	 * Invalid SLD UserStyle ${0}.
	 *
	 * @since 1.10.0
	 */
	int INVALID_USER_STYLE = 79;
	/**
	 * Style ${0} not found for layer ${1}.
	 *
	 * @since 1.10.0
	 */
	int STYLE_NOT_FOUND = 80;
	/**
	 * Problem rendering legend graphic ${0}.
	 *
	 * @since 1.10.0
	 */
	int LEGEND_GRAPHIC_RENDERING_PROBLEM = 81;
	/**
	 * Resource ${0} not found.
	 *
	 * @since 1.10.0
	 */
	int RESOURCE_NOT_FOUND = 82;
	/**
	 * Style rule ${0} not found for style ${1}.
	 *
	 * @since 1.10.0
	 */
	int RULE_NOT_FOUND = 83;
	/**
	 * Invalid layer type ${0} for feature style, see javadoc.
	 *
	 * @since 1.10.0
	 */
	int INVALID_FEATURE_STYLE_LAYER_TYPE = 84;
	/**
	 * Could not parse expression ${0} for layer ${1}.
	 *
	 * @since 1.10.0
	 */
	int EXPRESSION_INVALID = 85;
	/**
	 * Could not evaluate expression ${0} for layer ${1}.
	 *
	 * @since 1.10.0
	 */
	int EXPRESSION_EVALUATION_FAILED = 86;
	/**
	 * Layer ${0} does not allow features with an empty geometry.
	 *
	 * @since 1.10.0
	 */
	int LAYER_EMPTY_GEOMETRY_NOT_ALLOWED = 87;
	/**
	 * Layer ${0} is not correctly configured: ${1}.
	 *
	 * @since 1.10.0
	 */
	int LAYER_CONFIGURATION_PROBLEM = 88;
	/**
	 * Duplicate attribute name ${0} in layer ${1}, path ${2}.
	 *
	 * @since 1.10.0
	 */
	int DUPLICATE_ATTRIBUTE_NAME = 89;
	/**
	 * Cannot parse number ${0} in SLD file.
	 *
	 * @since 1.11.1
	 */
	int SLD_PARSE_NUMBER = 90;
	/**
	 * Cannot parse number ${0} in SLD file for attribute ${1}.
	 *
	 * @since 1.11.1
	 */
	int SLD_PARSE_NUMBER_ATTRIBUTE = 91;
	/**
	 * Cannot create feature without geometry for layer ${0}. This can be caused by passing a null geometry,
	 * security limitations or geometry attribute not having the editable capability.
	 *
	 * @since 1.11.1
	 */
	int CANNOT_CREATE_FEATURE_WITHOUT_GEOMETRY = 92;
}
