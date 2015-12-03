# Java GUI Becomes Input/Output (jguibio)


Jar Files
=========

Dependencies
------------
jguibio depends on the [Gson](https://en.wikipedia.org/wiki/Gson) library.

Pre-build Jars
--------------
For convenience, pre-built jar files of jguibio and Gson have been uploaded to:

* http://dist.brokertron.com/jguibio/gson-2.2.2.jar
* http://dist.brokertron.com/jguibio/jguibio.jar

Building (like a C++ programmer)
--------------------------------

The original author programs mainly in C++ and used jguibio within a larger C++
project. The included `java/CMakeFiles.txt` was used to make jguibio via cmake,
make and ant.

Others will probably want to compile using more normal Java tools like maven or
ant or maybe just call javac directly. The included `java/build.xml` is for ant
(triggered from cmake).

Here are the steps taken to build jguibio in this unholy C++-ish setup:

```
cd java
mkdir release
cd release/
cmake -DJGUIBIO_JARS=dist ..
make
ls ../dist
```

