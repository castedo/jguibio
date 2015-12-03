# Java GUI Becomes Input/Output (jguibio)

jguibio runs in front of a Java application to "translate" a Java GUI into JSON
input and output. GUI events such as windows opening with text and images are
output as JSON objects with text and images encoded as text.

If certain JSON objects are input, then they are translated into GUI events
such as closing windows, entering text and clicking buttons.

Example
=======

Suppose you have a Java application whose entry class is `SillySoft.Main`. Run
jguibio as such:

```
java -cp "path/to/jars/*" jguibio.Main SillySoft.Main
```

where directory `path/to/jars` contains `jguibui.jar`, the Gson jar file and
the jar file containing the `SillySoft.Main` class. Any arguments for the
SillySoft.Main can be appended on the command line and they will be passed
along.

Standard output (stdout) of the SillySoft application will get redirected to
standard error (stderr).

GUI events just as windows opening will appear as JSON objects on standard
output such as

```
{"wid":0,"event":"opened","extract":{"title":"SillySoft Signin","content":["Username","Password",{"button":"Signin"},{"button":"Cancel"},{"png":"AElFTkSuQmCC"}]}}
```

Each window gets identified with a "wid" number and further events, such as the
window closing, will use the wid:

```
{"wid":0,"event":"closed"}
```

JSON objects such as the following can be input, followed be enter:

```
["submit", 0, "Signin", "hackman", "sneaky"]
```

which will result in the clicking of the button labeled "Signin" in the window
with wid zero after text `hackman` and `sneaky` get input into the first two
text fields.

The JSON object
```
["dispose", 0]
```
can be sent to close the window with wid zero.


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

