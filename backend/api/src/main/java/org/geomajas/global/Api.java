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
 * Annotation which indicates that a class and/or method are part of the supported Geomajas API.
 * <p/>
 * Only classes and methods with this annotation are guaranteed to be supported for increasing versions with the same
 * major version number.
 * <p/>
 * When the class is annotated, "allMethods" can be used to indicate that the annotation also applies to all public
 * methods in the class (or interface). When this is not used, the class is guaranteed to exist, but the individual
 * supported methods also need to be annotated. Note that the annotation explicitly does not apply for inner classes,
 * they need explicit annotation.
 *
 * @author Joachim Van der Auwera
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE, ElementType.METHOD, ElementType.CONSTRUCTOR, ElementType.LOCAL_VARIABLE,
		ElementType.ANNOTATION_TYPE })
@Documented
@Inherited
@Api
public @interface Api {

	/**
	 * For class level annotation, does it apply to all methods?
	 */
	boolean allMethods() default false;
}
