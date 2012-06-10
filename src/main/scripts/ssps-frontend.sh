#!/bin/bash

localdir=`dirname $0`
installdir=`dirname $localdir`

if [[ "$installdir" == "." ]] ; then
	installdir=".."
fi

echo "$installdir"
source "$installdir"/conf/startup.conf

JAVA_DEBUG="-Xdebug -Djava.compiler=NONE -Xnoagent -Xrunjdwp:transport=dt_socket,server=y,suspend=n,address=${DEBUG_PORT}"

if [ "x$1" == "xdebug" ] ; then
	java ${JAVA_DEBUG} -Dorg.ssps.frontend.home="$installdir" -jar "$installdir"/bin/${MAIN_JAR} $@
else
	java -Dorg.ssps.frontend.home="$installdir" -jar "$installdir"/bin/${MAIN_JAR} $@
fi
