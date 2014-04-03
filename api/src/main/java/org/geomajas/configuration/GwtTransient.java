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

package org.geomajas.configuration;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marker interface which indicates that this class should not be serialized to the client.
 * This annotation will only be interpreted by GWT RPC:
 * it will consider the field as being transient.
 * For the moment, it should be used for fields of types that inherit
 * {@link org.geomajas.configuration.ServerSideOnlyInfo}.
 *
 * @author Jan Venstermans
 * @since 1.15.0
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target( ElementType.FIELD )
public @interface GwtTransient { }