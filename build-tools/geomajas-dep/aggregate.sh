#! /bin/sh

TARGET="/var/www/file.geomajas.org/https/..."
TMPDIR=""
TARGET="/home/joachim/tmp/documentation.html"
TARGETDIR="/home/joachim/tmp/"

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
}

# sample link https://oss.sonatype.org/service/local/artifact/maven/redirect?r=snapshots&g=org.geomajas.documentation&a=geomajas-layer-geotools-documentation&v=1.7.0-SNAPSHOT&e=jdocbook
include() {
needs groupId, artifactId, version, title, description
	echo "" >> $TARGET
	FILE="docs.zip"
	LOCATION="https://oss.sonatype.org/service/local/artifact/maven/redirect?r=snapshots&g=$1&a=$2&v=$3&e=jdocbook"
	PWD=`pwd`
	cd $TARGETDIR
	wget $LOCATION -O docs.zip
	mkdir $2
	cd $PWD
	echo "<h2>$4</h2>" >> $TARGET
	echo "<p>$5</p>" >> $TARGET
}

template_start
include "org.geomajas.documentation" "geomajas-layer-geotools-documentation" "1.7.0-SNAPSHOT" \
    "Geotools layer" \
    "This is a layer which allows accessing GIS data through Geotools, for example for accessing WFS data."
template_end

exit 0