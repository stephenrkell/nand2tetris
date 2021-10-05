# naive makefile by SRK

THIS_MAKEFILE := $(lastword $(MAKEFILE_LIST))
srcroot := $(dir $(realpath $(THIS_MAKEFILE)))

JAVAC ?= javac
JAVACFLAGS ?=
JAR ?= jar


.PHONY: default
default: jars builtin-Chips-classes.stamp builtin-VMCode-classes.stamp mainclasses-classes.stamp

.PHONY: jars
jars: \
InstallDir/bin/lib/HackGUI.jar \
InstallDir/bin/lib/SimulatorsGUI.jar \
InstallDir/bin/lib/Simulators.jar \
InstallDir/bin/lib/Compilers.jar \
InstallDir/bin/lib/Hack.jar

# Looks like 2.6 has the following that 2.5.7 doesn't
#InstallDir/bin/lib/TranslatorsGUI.jar \
#InstallDir/bin/lib/AssemblerGUI.jar \

# FIXME: this is wrong because javac ends up prefixing the
# package name of the things it builds
# with the directory name. Maybe it'll work if we delete
# that element from the classpath? Use $(filter )
JAVACFLAGS += -cp \
$(shell (echo -n $(foreach j,HackGUI SimulatorsGUI Simulators Compilers Hack,$(j)PackageSource:); \
echo -n $(foreach j,BuiltInChips BuiltInVMCode MainClasses,$(j)Source:)) | tr -d ' ')

classpath_dirs := $(foreach j,HackGUI SimulatorsGUI Simulators Compilers Hack,$(j)PackageSource)
classpath_dirs += $(foreach j,BuiltInChips BuiltInVMCode MainClasses,$(j)Source)

classpath = $(shell echo '$(filter-out $(1),$(classpath_dirs))' | tr '[:blank:]' ':' )

define jar-rule
InstallDir/bin/lib/$(1).jar: $(1)-classes.stamp
	cd $(1)PackageSource && $(JAR) -cf $(srcroot)/InstallDir/bin/lib/$(1).jar $$$$(find -name *.class)
endef

$(foreach j,HackGUI SimulatorsGUI Simulators Compilers Hack,$(eval $(call jar-rule,$(j))))
#InstallDir/bin/lib/%.jar: classes.stamp
#	cd $*PackageSource && $(JAR) -cf $(srcroot)/$@ $$(find -name *.class)

# installed classes are
# InstallDir/builtInChips/*.class
# InstallDir/builtInVMCode/*.class
# InstallDir/bin/classes/*.class

# hdl files for built-in chips live in the InstallDir already

define classes-rule
$(1)-classes.stamp: $(shell find $(1)PackageSource -name '*.java' )
	$(JAVAC) $(JAVACFLAGS) -cp :$$(call classpath,$(1)PackageSource): -source 1.3 -target 1.3 $$+
	touch $$@
endef
$(foreach j,HackGUI SimulatorsGUI Simulators Compilers Hack,$(eval $(call classes-rule,$(j))))

# && \
#	cp -p BuiltInChipsSource/*.class InstallDir/builtInChips/ && \
#	cp -p BuiltInVMCodeSource/*.class InstallDir/builtInVMCode/ && \
#	cp -p MainClassesSource/*.class InstallDir/bin/classes/ && \
#	touch $@

define builtin-rule
builtin-$(1)-classes.stamp: $(shell find BuiltIn$(1)Source -name '*.java')
	$(JAVAC) $(JAVACFLAGS) -cp :$$(call classpath,BuiltIn$(1)PackageSource): -source 1.3 -target 1.3 $$+ && \
	cp -p BuiltIn$(1)Source/*.class InstallDir/builtIn$(1)/ && \
	touch $$@
endef
$(foreach b,Chips VMCode,$(eval $(call builtin-rule,$(b))))

$(info classpath for MainClassesSource is $(call classpath,MainClassesSource))
mainclasses-classes.stamp: $(shell find MainClassesSource -name '*.java')
	$(JAVAC) $(JAVACFLAGS) -cp :$(call classpath,MainClassesSource): -source 1.3 -target 1.3 $+ && \
	cp -p MainClassesSource/*.class InstallDir/bin/classes/ && \
	touch $@

.PHONY: clean
clean:
	find -name '*.class' -o -name '*.jar' -o -name '*.stamp' | xargs rm -f
