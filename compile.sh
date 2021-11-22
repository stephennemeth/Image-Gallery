#!/bin/bash

mvn clean
mvn compile
mvn -e -Dprism.order=sw exec:java -Dexec.cleanupDaemonThreads=false
