#! /bin/sh

#TARGETDIR="/home/joachim/tmp/"
TARGETDIR="/var/www/files.geomajas.org/htdocs/maven/trunk/geomajas"
TARGET="$TARGETDIR/temp.html"
FINAL="$TARGETDIR/documentation.html"
LINKPREFIX="http://files.geomajas.org/maven/trunk/geomajas/"

template_start() {
	rm $TARGET
	echo "<html>" > $TARGET
	echo "<head><title>Geomajas documentation</title></head>" >> $TARGET
	echo "<body>" >> $TARGET
	echo "<h1>Geomajas documentation</h1>" >> $TARGET
}

template_end() {
	echo "" >> $TARGET
	echo "</body>" >> $TARGET
	echo "</html>" >> $TARGET
	mv $TARGET $FINAL
}

# sample link https://oss.sonatype.org/service/local/artifact/maven/redirect?r=snapshots&g=org.geomajas.documentation&a=geomajas-layer-geotools-documentation&v=1.7.0-SNAPSHOT&e=jdocbook
include() {
needs groupId, artifactId, version, title, description, state, pdf-filename
	echo "" >> $TARGET
	FILE="docs.zip"
	LOCATION="https://oss.sonatype.org/service/local/artifact/maven/redirect?r=snapshots&g=$1&a=$2&v=$3&e=jdocbook"
	PWD=`pwd`
	cd $TARGETDIR
	wget --no-check-certificate $LOCATION -O docs.zip
	mkdir $2
	unzip -o docs.zip -d $2
	#rm docs.zip
	cd $PWD
	echo "<h2>$4</h2>" >> $TARGET
	echo "<p class="state">state: $6</p>" >> $TARGET
	echo "<p class="desc">$5</p>" >> $TARGET
	echo "<p class="links"><a href="$LINKPREFIX$2/pdf/$7">PDF</a> | <a href="$LINKPREFIX$2/html/index.html">html</a> | <a href="$LINKPREFIX$2/html/index.html">one page html</a></p>" >> $TARGET
}

template_start

# main guides

include "org.geomajas.documentation" "docbook-gettingstarted" "1.7.0-SNAPSHOT" \
    "Getting started" \
    "How to get your project up-and-running." \
    "incubating" "Getting_Started.pdf"

include "org.geomajas.documentation" "docbook-devuserguide" "1.7.0-SNAPSHOT" \
    "User guide for developers" \
    "Reference guide detailing architecture, implementation and extension possibilities of the back-end core." \
    "incubating" "User_Guide_for_Developers.pdf"


# faces

include "org.geomajas.documentation" "geomajas-face-gwt-documentation" "1.7.0-SNAPSHOT" \
    "GWT face" \
    "GWT face for building powerful AJAX web user interfaces in Java using SmartGWT." \
    "incubating" "gwt_face.pdf"

include "org.geomajas.documentation" "geomajas-face-dojo-documentation" "1.5.6-SNAPSHOT" \
    "dojo face" \
    "dojo face for building a web user interface in JavaScript using dojo toolkit." \
    "incubating, deprecated" "dojo_face.pdf"

# plug-ins

include "org.geomajas.documentation" "geomajas-layer-geotools-documentation" "1.7.0-SNAPSHOT" \
    "Geotools layer" \
    "This is a layer which allows accessing GIS data through Geotools, for example for accessing WFS data." \
    "incubating" "Geotools_layer.pdf"

include "org.geomajas.documentation" "geomajas-layer-google-documentation" "1.7.0-SNAPSHOT" \
    "Google layer" \
    "This is a layer which allows accessing Google images as raster layer." \
    "incubating" "Google_layer.pdf"

include "org.geomajas.documentation" "geomajas-layer-hibernate-documentation" "1.7.0-SNAPSHOT" \
    "Hibernate layer" \
    "This is a layer which allows accessing data in a GIS database using Hibernate and Hibernate Spatial." \
    "incubating" "Hibernate_layer.pdf"

include "org.geomajas.documentation" "geomajas-layer-openstreetmap-documentation" "1.7.0-SNAPSHOT" \
    "Openstreetmap layer" \
    "This is a layer which allows accessing Openstreetmap images as raster layer." \
    "incubating" "Openstreetmap_layer.pdf"

include "org.geomajas.documentation" "geomajas-layer-wms-documentation" "1.7.0-SNAPSHOT" \
    "WMS layer" \
    "This is a layer which allows accessing WMS images as raster layer." \
    "incubating" "WMS_layer.pdf"

include "org.geomajas.documentation" "geomajas-plugin-staticsecurity-documentation" "1.7.0-SNAPSHOT" \
    "Staticsecurity plug-in" \
    "Geomajas security plug-in which allows all users and policies to be defined as part of spring configuration." \
    "incubating" "staticsecurity.pdf"

include "org.geomajas.documentation" "geomajas-plugin-printing-documentation" "1.7.0-SNAPSHOT" \
    "Printing plug-in" \
    "Geomajas extension for printing." \
    "incubating" "printing.pdf"

include "org.geomajas.documentation" "geomajas-plugin-caching-documentation" "1.7.0-SNAPSHOT" \
    "Caching plug-in" \
    "Caching to allow data to be calculated only once and cached for later use." \
    "incubating" "caching.pdf"


# contributors guide

#include "org.geomajas.documentation" "docbook-contributorguide" "1.7.0-SNAPSHOT" \
#    "Getting started" \
#    "Information for contributors of the project." \
#    "incubating" "Contributor_Guide.pdf"


template_end

exit 0