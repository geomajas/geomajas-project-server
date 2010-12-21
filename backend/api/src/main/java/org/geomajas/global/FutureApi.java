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

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation which indicates that a class and/or method are planned to be part of the supported Geomajas API.
 * <p/>
 * This is an annotation to indicate that this should be part of the API, but there is still some room for discussion
 * or validation before changing to {@link Api}.
 * <p/>
 * The intention is that these annotations only remain for a short time, either removing the annotation or changing to
 * {@link Api}. In principle this should be checked before a release. It is recommended to add some comments to
 * indicate why it is not yet @Api and/or indicate when it should become {@link Api}.
 *
 * @author Joachim Van der Auwera
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE, ElementType.METHOD, ElementType.CONSTRUCTOR, ElementType.LOCAL_VARIABLE,
		ElementType.ANNOTATION_TYPE })
@Documented
@Inherited
@Api
public @interface FutureApi {

	/**
	 * For class level annotation, does it apply to all methods?
	 */
	boolean allMethods() default false;
}
