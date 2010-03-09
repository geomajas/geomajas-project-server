/*
 * This software project is called Majas, a component framework for building
 * rich Internet applications (RIA) with sophisticated capabilities for the
 * display, analysis and management of geographic information.
 * It is a building block that allows developers to add maps
 * and other geographic data capabilities to their web applications.
 *
 * Copyright 2006-2007 DFC Software Engineering, http://www.dfc.be, Belgium
 * This file is part of Majas.
 *
 * Majas is free software. You can redistribute it and/or modify
 * it under the terms of the GNU General Public License Version 2
 * as published by the Free Software Foundation
 *
 * Majas is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Majas; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301 USA
 */

// these constants should be set by the maven profile !!!

var djConfig={
	isDebug: false,
	parseOnLoad: true,
	usePlainJson: true,
	locale: "en"
};

var geomajasConfig={
	dijitTheme: "soria",
	showLog: true,
	useLazyLoading: true, // use lazy loading
	lazyFeatureIncludesDefault: 4, // by default, only include style
	lazyFeatureIncludesSelect: 15, // attributes + geometry + style + label (see VectorLayerService)
	lazyFeatureIncludesAll: 15 // attributes + geometry + style + label (see VectorLayerService)
};
