#!/bin/sh
cd `dirname $0`
java -classpath "${CLASSPATH}:.:bin/classes:BuiltIn:bin/lib/Hack.jar:bin/lib/HackGUI.jar:bin/lib/Simulators.jar:bin/lib/SimulatorsGUI.jar:bin/lib/Compilers.jar" ASMValidator $*
