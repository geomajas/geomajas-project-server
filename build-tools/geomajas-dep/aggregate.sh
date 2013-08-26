#! /bin/bash

#TARGETDIR="/home/joachim/tmp/"
TARGETDIR="/srv/www/files.geomajas.org/htdocs/maven/trunk/geomajas"
TARGET="$TARGETDIR/temp.html"
FINAL="$TARGETDIR/documentation.html"
#LINKPREFIX="file:/home/joachim/tmp/"
LINKPREFIX="http://files.geomajas.org/maven/trunk/geomajas/"

template_start() {
	rm $TARGET
	echo "<html>" > $TARGET
	echo "<head><title>Geomajas documents</title></head>" >> $TARGET
	echo "<body>" >> $TARGET
	echo "<h1>Geomajas documentation</h1>" >> $TARGET
}

template_end() {
	echo "" >> $TARGET
	echo "</body>" >> $TARGET
	echo "</html>" >> $TARGET
	mv $TARGET $FINAL

	# assure google analytics is used in HTML pages
	perl -e "s/<\/body>/ \
<script type=\"text\/javascript\"> \
var _gaq = _gaq \|\| \[\]; \
_gaq.push(\[\'_setAccount\', \'UA-8078092-3\'\]); \
_gaq.push(\[\'_trackPageview\'\]); \
(function() { \
   var ga = document.createElement(\'script\'); ga.type = \'text\/javascript\'; ga.async = true; \
   ga.src = (\'https:\' == document.location.protocol \? \'https:\/\/ssl\' : \'http:\/\/www\') + \'.google-analytics.com\/ga.js\'; \
   var s = document.getElementsByTagName(\'script\')\[0\]; s.parentNode.insertBefore(ga, s); \
})(); \
<\/script> \
	<\/body>/gi;" -pi $(find $TARGETDIR -name \*.html)

}

# sample link https://oss.sonatype.org/service/local/artifact/maven/redirect?r=snapshots&g=org.geomajas.documentation&a=geomajas-layer-geotools-documentation&v=1.7.0-SNAPSHOT&e=jdocbook
# sample javadoc link https://oss.sonatype.org/service/local/artifact/maven/redirect?r=releases&g=org.geomajas&a=geomajas-api&v=1.7.1&e=jar&c=javadoc
# parameters: groupId, artifactId, version, title, description, state, pdf-filename, javadoc-groupid, javadoc-artifactid, javadoc-version
include() {
	echo "" >> $TARGET
	FILE="docs.zip"
	LOCATION="https://oss.sonatype.org/service/local/artifact/maven/redirect?r=releases&g=$1&a=$2&v=$3&e=jar"
	if [[ "$3" == *"SNAPSHOT"* ]]; then
		LOCATION="https://oss.sonatype.org/service/local/artifact/maven/redirect?r=snapshots&g=$1&a=$2&v=$3&e=jar"
	fi
	PWD=`pwd`
	cd $TARGETDIR
	wget -q --no-check-certificate $LOCATION -O docs.zip
	mkdir -p $2
	unzip -q -o docs.zip -d $2
	if [ $? -ne 0 ]; then
		echo ERROR processing $LOCATION
	fi	
	rm docs.zip

	if [ -n "$8" ]
	then
		JDLOCATION="https://oss.sonatype.org/service/local/artifact/maven/redirect?r=releases&g=$8&a=$9&v=${10}&e=jar&c=javadoc"
		if [[ "${10}" == *"SNAPSHOT"* ]]; then
			JDLOCATION="https://oss.sonatype.org/service/local/artifact/maven/redirect?r=snapshots&g=$8&a=$9&v=${10}&e=jar&c=javadoc"
		fi	
		wget -q --no-check-certificate $JDLOCATION -O javadocs.zip
		mkdir -p $2/javadoc
		unzip -q -o javadocs.zip -d $2/javadoc
		if [ $? -ne 0 ]; then
			echo ERROR processing $JDLOCATION
		fi	
		rm javadocs.zip
	fi

	cd $PWD
	echo "<h2>$4</h2>" >> $TARGET
	echo "<p class="state">state: $6</p>" >> $TARGET
	echo "<p class="desc">$5</p>" >> $TARGET
	echo "<p class="links">View: <a href="$LINKPREFIX$2/pdf/master.pdf">PDF</a> | <a href="$LINKPREFIX$2/html/master.html">html</a>" >> $TARGET
	if [ -n "$8" ]
	then
		echo " | <a href="$LINKPREFIX$2/javadoc/index.html">javadoc</a>" >> $TARGET
	fi
	echo "</p>" >> $TARGET
}

template_start

# main guides

include "org.geomajas.documentation" "docbook-gettingstarted" "1.12.0-SNAPSHOT" \
    "Getting started" \
    "How to get your project up-and-running." \
    "incubating" "Getting_Started.pdf" \
    "org.geomajas" "geomajas-command" "1.12.0"

include "org.geomajas.documentation" "docbook-devuserguide" "1.12.0-SNAPSHOT" \
    "User guide for developers" \
    "Reference guide detailing architecture, implementation and extension possibilities of the back-end core." \
    "incubating" "User_Guide_for_Developers.pdf" \
    "org.geomajas" "geomajas-api" "1.12.0"


# projects

include "org.geomajas.project" "geomajas-project-api-annotation" "1.1.0-SNAPSHOT" \
    "API annotations project" \
    "Set of annotations to allow detailed marking of the supported API." \
    "incubating" "master.pdf" \
    "org.geomajas.project" "geomajas-project-api-annotation" "1.0.0"

include "org.geomajas.documentation" "geomajas-project-codemirror-gwt-documentation" "3.13.0-SNAPSHOT" \
    "Codemirror GWT wrapper project" \
    "In-browser code editing made bearable. Based on CodeMirror version 3.1." \
    "incubating" "master.pdf" \
    "org.geomajas.project" "geomajas-project-codemirror-gwt" "3.1.1"

include "org.geomajas.documentation" "geomajas-project-geometry-documentation" "1.2.0-SNAPSHOT" \
    "Geometry DTO project" \
    "Set of GWT compatible Geometry DTOs and services to manipulate them." \
    "incubating" "master.pdf" \
    "org.geomajas.project" "geomajas-project-geometry-core" "1.1.0"

include "org.geomajas.documentation" "geomajas-project-sld-documentation" "1.2.0-SNAPSHOT" \
    "SLD DTO project" \
    "Set of GWT compatible SLD DTOs and services to read/write them." \
    "incubating" "master.pdf" \
    "org.geomajas.project" "geomajas-project-sld-api" "1.1.0"

include "org.geomajas.documentation" "geomajas-project-profiling-documentation" "1.2.0-SNAPSHOT" \
    "Generic profiling project." \
    "Generic utility profiling code for gathering number of invocations and total execution time, possible surfacing this as JMX bean. project" \
    "incubating" "master.pdf" \
    "" "" ""
#    "org.geomajas.project" "geomajas-project-profiling-api" "1.0.0"


# faces

include "org.geomajas.documentation" "geomajas-face-gwt-documentation" "1.15.0-SNAPSHOT" \
    "GWT face" \
    "GWT face for building powerful AJAX web user interfaces in Java using SmartGWT." \
    "incubating" "gwt_face.pdf" \
    "org.geomajas" "geomajas-gwt-client" "1.14.0"

include "org.geomajas.documentation" "geomajas-face-gwt-documentation" "2.1.0-SNAPSHOT" \
    "PureGWT face" \
    "GWT face for building powerful AJAX web user interfaces without depending on a widget library. Ideal for mobile." \
    "incubating" "master.pdf" \
    "org.geomajas" "geomajas-puregwt-client" "2.0.0"

include "org.geomajas.documentation" "common-gwt-documentation" "1.5.0-SNAPSHOT" \
    "Common-GWT " \
    "Common module which is used by both the GWT and PureGWT faces." \
    "incubating" "master.pdf" \
    "org.geomajas" "geomajas-face-common-gwt" "1.4.0"

#include "org.geomajas.documentation" "geomajas-face-dojo-documentation" "1.5.7" \
#    "dojo face" \
#    "dojo face for building a web user interface in JavaScript using dojo toolkit." \
#    "retired" "dojo_face.pdf" \
#    "org.geomajas" "geomajas-dojo-server" "1.5.7"

include "org.geomajas.documentation" "geomajas-face-rest-documentation" "1.1.0-SNAPSHOT" \
    "REST face" \
    "face for communication with the Geomajas back-end using REST and GeoJSON." \
    "incubating" "master.pdf" \
    "org.geomajas" "geomajas-face-rest" "1.0.0"

# plug-ins

include "org.geomajas.plugin" "geomajas-layer-geotools-documentation" "1.12.0-SNAPSHOT" \
    "Geotools layer" \
    "This is a layer which allows accessing GIS data through GeoTools, for example for accessing WFS data." \
    "graduated" "Geotools_layer.pdf" \
    "org.geomajas.plugin" "geomajas-layer-geotools" "1.11.0"

include "org.geomajas.plugin" "geomajas-layer-google-documentation" "2.2.0-SNAPSHOT" \
    "Google layer" \
    "This is a layer which allows accessing Google images as raster layer." \
    "graduated" "Google_layer.pdf" \
    "org.geomajas.plugin" "geomajas-layer-google" "2.1.0"

include "org.geomajas.plugin" "geomajas-layer-hibernate-documentation" "1.12.0-SNAPSHOT" \
    "Hibernate layer" \
    "This is a layer which allows accessing data in a GIS database using Hibernate and Hibernate Spatial." \
    "graduated" "Hibernate_layer.pdf" \
    "org.geomajas.plugin" "geomajas-layer-hibernate" "1.11.0"

include "org.geomajas.plugin" "geomajas-layer-openstreetmap-documentation" "1.11.0-SNAPSHOT" \
    "Openstreetmap layer" \
    "This is a layer which allows accessing Openstreetmap images as raster layer." \
    "graduated" "Openstreetmap_layer.pdf" \
    "org.geomajas.plugin" "geomajas-layer-openstreetmap" "1.10.0"

include "org.geomajas.plugin" "geomajas-layer-common-documentation" "1.1.0-SNAPSHOT" \
    "Common layer tools" \
    "This plug-in contains common classes used by layers. (to help with proxying and security)." \
    "incubating" "master.pdf" \
    "org.geomajas.plugin" "geomajas-plugin-layer-common" "1.0.0"

include "org.geomajas.plugin" "geomajas-layer-tms-documentation" "1.2.0-SNAPSHOT" \
    "TMS layer" \
    "This is a layer which allows accessing TMS images as raster layer." \
    "incubating" "master.pdf" \
    "org.geomajas.plugin" "geomajas-layer-tms" "1.1.0"

include "org.geomajas.plugin" "geomajas-layer-wms-documentation" "1.12.0-SNAPSHOT" \
    "WMS layer" \
    "This is a layer which allows accessing WMS images as raster layer." \
    "graduated" "WMS_layer.pdf" \
    "org.geomajas.plugin" "geomajas-layer-wms" "1.11.0"

include "org.geomajas.plugin" "geomajas-plugin-wmsclient-documentation" "1.0.0-SNAPSHOT" \
    "WMS client plugin" \
    "WMS client plugin." \
    "incubating" "master.pdf" \
    "org.geomajas.plugin" "geomajas-plugin-wmsclient" "1.0.0-M3"

include "org.geomajas.plugin" "geomajas-plugin-staticsecurity-documentation" "1.10.0-SNAPSHOT" \
    "Staticsecurity plug-in" \
    "Geomajas security plug-in which allows all users and policies to be defined as part of spring configuration." \
    "graduated" "staticsecurity.pdf" \
    "org.geomajas.plugin" "geomajas-plugin-staticsecurity" "1.9.0"

include "org.geomajas.plugin" "geomajas-plugin-printing-documentation" "2.5.0-SNAPSHOT" \
    "Printing plug-in" \
    "Geomajas extension for printing." \
    "graduated" "printing.pdf" \
    "org.geomajas.plugin" "geomajas-plugin-printing" "2.5.0-SNAPSHOT"

# include "org.geomajas.plugin" "geomajas-plugin-profiling-documentation" "1.0.0-SNAPSHOT" \
#    "Profiling plug-in" \
#    "Geomajas extension for profiling using JMX." \
#    "incubating" "master.pdf" \
#    "org.geomajas.plugin" "geomajas-plugin-profiling" "1.0.0-SNAPSHOT"

include "org.geomajas.plugin" "caching-documentation" "2.1.0-SNAPSHOT" \
    "Caching plug-in" \
    "Caching to allow data to be calculated only once and cached for later use." \
    "graduated" "caching.pdf" \
    "org.geomajas.plugin" "geomajas-plugin-caching" "2.0.0"

include "org.geomajas.plugin" "geocoder-documentation" "1.4.0-SNAPSHOT" \
    "Geocoder plug-in" \
    "Convert a location description to map coordinates." \
    "graduated" "geocoder.pdf" \
    "org.geomajas.plugin" "geomajas-plugin-geocoder" "1.3.0"

include "org.geomajas.plugin" "rasterizing-documentation" "1.3.0-SNAPSHOT" \
    "Rasterizing plug-in" \
    "Allows tiles to be rasterized server-side." \
    "graduated" "master.pdf" \
    "org.geomajas.plugin" "geomajas-plugin-rasterizing" "1.2.0"

include "org.geomajas.widget" "geomajas-widget-advancedviews-documentation" "1.0.0-SNAPSHOT" \
    "Advanced views widget plug-in" \
    "Advanced views widget plug-in." \
    "incubating" "master.pdf" \
    "org.geomajas.widget" "geomajas-widget-advancedviews" "1.0.0-M3"

include "org.geomajas.widget" "geomajas-widget-featureinfo-documentation" "1.0.0-SNAPSHOT" \
    "Feature info widget plug-in" \
    "Feature info widget plug-in." \
    "incubating" "master.pdf" \
    "org.geomajas.widget" "geomajas-widget-featureinfo" "1.0.0-M2"

include "org.geomajas.widget" "geomajas-widget-layer-documentation" "1.0.0-SNAPSHOT" \
    "Layer widget plug-in" \
    "Layer widget plug-in." \
    "incubating" "master.pdf" \
    "org.geomajas.widget" "geomajas-widget-layer" "1.0.0-M3"

include "org.geomajas.widget" "geomajas-widget-searchandfilter-documentation" "1.0.0-SNAPSHOT" \
    "Search and filter widget plug-in" \
    "Search and filter widget plug-in." \
    "incubating" "master.pdf" \
    "org.geomajas.widget" "geomajas-widget-searchandfilter" "1.0.0-M5"

include "org.geomajas.plugin" "geomajas-plugin-deskmanager-documentation" "1.0.0-SNAPSHOT" \
    "Deskmanager plug-in" \
    "Deskmanager plug-in." \
    "incubating" "master.pdf" \
    "org.geomajas.plugin" "geomajas-plugin-deskmanager" "1.0.0-M4"

include "org.geomajas.plugin" "geomajas-plugin-runtimeconfig-documentation" "1.0.0-SNAPSHOT" \
    "Runtimeconfig plug-in" \
    "Runtimeconfig plug-in." \
    "incubating" "master.pdf" \
    "org.geomajas.plugin" "geomajas-plugin-runtimeconfig" "1.0.0-M2"

include "org.geomajas.widget" "geomajas-widget-utility-documentation" "1.0.0-SNAPSHOT" \
    "Utility widgets for GWT" \
    "Utility widgets for GWT" \
    "incubating" "master.pdf" \
    "org.geomajas.widget" "geomajas-widget-utility" "1.0.0-M4"

include "org.geomajas.plugin" "geomajas-plugin-javascript-api-documentation" "1.0.0-SNAPSHOT" \
    "JavaScript API plug-in" \
    "JavaScript API wrapper around the GWT faces for client side integration support." \
    "incubating" "master.pdf" \
    "" "" ""
#    "org.geomajas.plugin" "geomajas-plugin-javascript-api" "1.0.0-SNAPSHOT"

include "org.geomajas.plugin" "geomajas-plugin-editing-documentation" "1.0.1-SNAPSHOT" \
    "Editing plug-in" \
    "Geomajas extension for more powerful editing." \
    "incubating" "master.pdf" \
    "org.geomajas.plugin" "geomajas-plugin-editing" "1.0.0-M3"

include "org.geomajas.plugin" "geomajas-puregwt-widget-documentation" "1.0.0-SNAPSHOT" \
    "Core widgets for the PureGWT face" \
    "Set of widgets which alow you to make a PureGWT map more expressive." \
    "incubating" "master.pdf" \
    "org.geomajas.plugin" "geomajas-widget-puregwt-core" "1.0.0-M3"

include "org.geomajas.project" "geomajas-project-sld-documentation" "1.2.0-SNAPSHOT" \
    "SLD project" \
    "SLD project." \
    "incubating" "master.pdf" \
    "org.geomajas.project" "geomajas-project-sld-api" "1.1.0"

include "org.geomajas.project" "geomajas-project-sld-editor-documentation" "1.0.0-SNAPSHOT" \
    "SLD editor project" \
    "SLD editor project." \
    "incubating" "master.pdf" \
    "org.geomajas.project" "geomajas-project-sld-editor" "1.0.0-M1"

include "org.geomajas.project" "geomajas-project-geometry-documentation" "1.2.0-SNAPSHOT" \
    "Geometry project" \
    "Geometry project." \
    "incubating" "master.pdf" \
    "org.geomajas.project" "geomajas-project-geometry-core" "1.1.0"

include "org.geomajas.plugin" "geomajas-plugin-vendorspecificpipeline" "1.1.0-SNAPSHOT" \
    "Vendor specific pipeline plugin" \
    "Vendor specific pipeline plugin." \
    "incubating" "master.pdf" \
    "org.geomajas.plugin" "geomajas-plugin-vendorspecificpipeline" "1.0.0"

# contributors guide

include "org.geomajas.documentation" "docbook-contributorguide" "1.12.0-SNAPSHOT" \
    "Contributors guide" \
    "Information for contributors of the project." \
    "incubating" "Contributor_Guide.pdf" \
    "" "" ""

template_end

exit 0
