cmake_minimum_required(VERSION 2.8.3)

add_custom_target(jguibio ALL
    ant -buildfile "${CMAKE_CURRENT_SOURCE_DIR}/build.xml"
        -Dbuild.compiler=javac1.7
        -Ddest.dir="${JGUIBIO_JARS}")

file(GLOB_RECURSE ANT_BUILD_FILES "${CMAKE_CURRENT_BINARY_DIR}/build/*")
file(GLOB_RECURSE ANT_JAR_FILES "${JGUIBIO_JARS}/*")

set_directory_properties(PROPERTIES
  ADDITIONAL_MAKE_CLEAN_FILES "${ANT_BUILD_FILES};${ANT_JAR_FILES}")

