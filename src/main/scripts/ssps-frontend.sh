#!/bin/bash

source conf/startup.conf

JAVA_DEBUG="-XDebug -Djava.compiler=NONE -Xnoagent -Xrunjdwp:transport=dt_socket,server=y,suspend=n,address=${DEBUG_PORT}"
JAVA_JMX="-Dcom.sun.management.jmxremote.authenticate=false -Dcom.sun.management.jmxremote.ssl=false -Dcom.sun.management.jmxremote.local.only=false -Dcom.sun.management.jmxremote.port=${JMX_PORT}"

if [ "x$1" == "xdebug" ] ; then
	java ${JAVA_DEBUG} ${JAVA_JMX} -Dorg.ssps.frontend.config.dir=`pwd`/conf -jar ${MAIN_JAR} $@
else
	java ${JAVA_JMX} -Dorg.ssps.frontend.config.dir=`pwd`/conf -jar ${MAIN_JAR} $@
fi