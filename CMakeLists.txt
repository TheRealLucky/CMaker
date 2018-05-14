cmake_minimum_required(VERSION 3.5)
project(cmaker LANGUAGES Java)

set(CMAKE_JAVA_COMPILE_FLAGS -nowarn)

enable_testing()

find_package(Java 1.7 REQUIRED COMPONENTS Development)
include(UseJava)

file(GLOB_RECURSE SOURCES "src/*.java")

add_jar(cmaker ${SOURCES} INCLUDE_JARS ${JARS} ENTRY_POINT i15091.project.cmaker.CMakerUI)

get_target_property(jarFile cmaker JAR_FILE)

file(GLOB TESTS "tests/*.java")
set(ALL ${SOURCES} ${TESTS})

file(GLOB_RECURSE JUNIT_JAR_FILE "tests/junit*.jar")
file(GLOB_RECURSE HAMCREST_JAR_FILE "tests/hamcrest*.jar")



add_jar(cmakerTest ${ALL} INCLUDE_JARS ${JUNIT_JAR_FILE}
                          ENTRY_POINT i15091.project.cmaker.TestProject)

get_target_property(junitJarFile cmakerTest JAR_FILE)

add_test(NAME cmakerTest
    COMMAND ${Java_JAVA_EXECUTABLE}
    -cp .:${junitJarFile}:${JUNIT_JAR_FILE}:${HAMCREST_JAR_FILE}
    org.junit.runner.JUnitCore i15091.project.cmaker.TestProject)



add_jar(makerTest ${ALL} INCLUDE_JARS ${JUNIT_JAR_FILE}
                          ENTRY_POINT i15091.project.cmaker.TestMaker)

get_target_property(junitJarFile makerTest JAR_FILE)

add_test(NAME makerTest
    COMMAND ${Java_JAVA_EXECUTABLE}
    -cp .:${junitJarFile}:${JUNIT_JAR_FILE}:${HAMCREST_JAR_FILE}
    org.junit.runner.JUnitCore i15091.project.cmaker.TestMaker)


add_jar(csvTest ${ALL} INCLUDE_JARS ${JUNIT_JAR_FILE}
                          ENTRY_POINT i15091.project.cmaker.TestCSVHandler)

get_target_property(junitJarFile csvTest JAR_FILE)

add_test(NAME csvTest
    COMMAND ${Java_JAVA_EXECUTABLE}
    -cp .:${junitJarFile}:${JUNIT_JAR_FILE}:${HAMCREST_JAR_FILE}
    org.junit.runner.JUnitCore i15091.project.cmaker.TestCSVHandler)