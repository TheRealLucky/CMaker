#CMakeLists.txt created by CMaker!
cmake_minimum_required(VERSION 3.5)
project(TestCMaker LANGUAGES Java)
enable_testing()
find_package(Java 1.7 REQUIRED COMPONENTS Development)
include(UseJava)
file(GLOB SOURCES "src/*.java")
add_jar(TestCMakerJar ${SOURCES} ENTRY_POINT Main.java)