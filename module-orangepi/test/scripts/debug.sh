#!/usr/bin/env sh
java -Xdebug -Xrunjdwp:transport=dt_socket,address=8000,server=y,suspend=y -jar test-1.0-SNAPSHOT.jar
