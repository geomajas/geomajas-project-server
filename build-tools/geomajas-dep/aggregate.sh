#! /bin/sh

#TARGETDIR="/home/joachim/tmp/"
TARGETDIR="/var/www/files.geomajas.org/htdocs/maven/trunk/geomajas"
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
	LOCATION="https://oss.sonatype.org/service/local/artifact/maven/redirect?r=snapshots&g=$1&a=$2&v=$3&e=jar"
	PWD=`pwd`
	cd $TARGETDIR
	wget --no-check-certificate $LOCATION -O docs.zip
	mkdir $2
	unzip -o docs.zip -d $2
	#rm docs.zip

	if [ -n "$8" ]
	then
		JDLOCATION="https://oss.sonatype.org/service/local/artifact/maven/redirect?r=releases&g=$8&a=$9&v=${10}&e=jar&c=javadoc"
		echo $JDLOCATION
		wget --no-check-certificate $JDLOCATION -O javadocs.zip
		mkdir $2/javadoc
		unzip -o javadocs.zip -d $2/javadoc
		#rm javadocs.zip
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

include "org.geomajas.documentation" "docbook-gettingstarted" "1.8.0-SNAPSHOT" \
    "Getting started" \
    "How to get your project up-and-running." \
    "incubating" "Getting_Started.pdf" \
    "" "" ""

include "org.geomajas.documentation" "docbook-devuserguide" "1.8.0-SNAPSHOT" \
    "User guide for developers" \
    "Reference guide detailing architecture, implementation and extension possibilities of the back-end core." \
    "incubating" "User_Guide_for_Developers.pdf" \
    "org.geomajas" "geomajas-api" "1.7.1"


# faces

include "org.geomajas.documentation" "geomajas-face-gwt-documentation" "1.8.0-SNAPSHOT" \
    "GWT face" \
    "GWT face for building powerful AJAX web user interfaces in Java using SmartGWT." \
    "incubating" "gwt_face.pdf" \
    "org.geomajas" "geomajas-gwt-client" "1.7.1"

include "org.geomajas.documentation" "geomajas-face-dojo-documentation" "1.5.8-SNAPSHOT" \
    "dojo face" \
    "dojo face for building a web user interface in JavaScript using dojo toolkit." \
    "incubating, deprecated" "dojo_face.pdf" \
    "org.geomajas" "geomajas-dojo-server" "1.5.7"

# plug-ins

include "org.geomajas.plugin" "geomajas-layer-geotools-documentation" "1.8.0-SNAPSHOT" \
    "Geotools layer" \
    "This is a layer which allows accessing GIS data through Geotools, for example for accessing WFS data." \
    "incubating" "Geotools_layer.pdf" \
    "org.geomajas.plugin" "geomajas-layer-geotools" "1.7.1"

include "org.geomajas.plugin" "geomajas-layer-google-documentation" "1.8.0-SNAPSHOT" \
    "Google layer" \
    "This is a layer which allows accessing Google images as raster layer." \
    "incubating" "Google_layer.pdf" \
    "org.geomajas.plugin" "geomajas-layer-google" "1.7.1"

include "org.geomajas.plugin" "geomajas-layer-hibernate-documentation" "1.8.0-SNAPSHOT" \
    "Hibernate layer" \
    "This is a layer which allows accessing data in a GIS database using Hibernate and Hibernate Spatial." \
    "incubating" "Hibernate_layer.pdf" \
    "org.geomajas.plugin" "geomajas-layer-hibernate" "1.7.2"

include "org.geomajas.plugin" "geomajas-layer-openstreetmap-documentation" "1.8.0-SNAPSHOT" \
    "Openstreetmap layer" \
    "This is a layer which allows accessing Openstreetmap images as raster layer." \
    "incubating" "Openstreetmap_layer.pdf" \
    "org.geomajas.plugin" "geomajas-layer-openstreetmap" "1.7.1"

include "org.geomajas.plugin" "geomajas-layer-wms-documentation" "1.8.0-SNAPSHOT" \
    "WMS layer" \
    "This is a layer which allows accessing WMS images as raster layer." \
    "incubating" "WMS_layer.pdf" \
    "org.geomajas.plugin" "geomajas-layer-wms" "1.7.1"

include "org.geomajas.plugin" "geomajas-plugin-staticsecurity-documentation" "1.8.0-SNAPSHOT" \
    "Staticsecurity plug-in" \
    "Geomajas security plug-in which allows all users and policies to be defined as part of spring configuration." \
    "incubating" "staticsecurity.pdf" \
    "org.geomajas.plugin" "geomajas-plugin-staticsecurity" "1.7.1"

include "org.geomajas.plugin" "geomajas-plugin-printing-documentation" "2.1.0-SNAPSHOT" \
    "Printing plug-in" \
    "Geomajas extension for printing." \
    "incubating" "printing.pdf" \
    "org.geomajas.plugin" "geomajas-plugin-printing" "2.0.0"

include "org.geomajas.plugin" "caching-documentation" "1.0.0-SNAPSHOT" \
    "Caching plug-in" \
    "Caching to allow data to be calculated only once and cached for later use." \
    "incubating" "caching.pdf" \
    "" "" ""

include "org.geomajas.plugin" "geocoder-documentation" "1.1.0-SNAPSHOT" \
    "Geocoder plug-in" \
    "Convert a location description to map coordinates." \
    "graduated" "geocoder.pdf" \
    "org.geomajas.plugin" "geomajas-plugin-geocoder" "1.0.0"


# contributors guide

include "org.geomajas.documentation" "docbook-contributorguide" "1.8.0-SNAPSHOT" \
    "Contributors guide" \
    "Information for contributors of the project." \
    "incubating" "Contributor_Guide.pdf" \
    "" "" ""


template_end

exit 0