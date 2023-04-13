# Compose Desktop Hello World with GraalVM and JWM

This repo contains a simple Compose Desktop "Hello World" example program that can be compiled with 
GraalVM and native-image. It also contains glue code by github user [@smallshen]
to make Compose work with JWM window library instead of AWT. (It currently isn't
posible to compile vanilla Compose AWT to native images).

![](composejwm.png?raw=true "Compose JWM")

## Build Prerequisites

### Install GraalVM compiler and native-image tool (required for all Platforms)

I've tested this example with Liberica Native Image Kit which is
a distribution of GraalVM 22.3, Download and install here:


https://bell-sw.com/pages/downloads/native-image-kit/

(In my tests compilation currently fails on the latest official distribution of GraalVM)

Using SDKman(https://sdkman.io/),  Liberica graalvm 22.3 can be installed using the command:

``` sdk install java 22.3.r17-nik ```

See also: https://www.graalvm.org/22.3/reference-manual/native-image/
for more info if needed.

### macOS build tools

Install Xcode command line tools:

```code-select --install```

### Windows build tools
Install Visual Studio and Microsoft Visual C++ (MSVC).
There are two installation options:
* Install the Visual Studio Build Tools with the Windows SDK
* Install Visual Studio with the Windows SDK

You can use Visual Studio 2017 version 15.9 or later.

The gradle build will **only** work when run in **x64 Native Tools Command Prompt**. 
A shortcut to the X64 native tools terminal will be installed on your system together with
Visual Studio Build Tools.

### Ubuntu Linux build tools

Install the dependencies using the command:

```sudo apt-get install build-essential libz-dev zlib1g-dev```


## Gradle Build Instructions

First make sure that the environment variable
```JAVA_HOME``` is set to your GraalVM installation directory
also,  and set ```GRAALVM_HOME``` to the same path. For example on Linux and Mac set:

```export GRAALVM_HOME=$JAVA_HOME```

on Windows set ```JAVA_HOME``` and ```GRAALVM_HOME``` in the Advanced System Properties GUI
or in the X64 native terminal:

```setx /m GRAALVM_HOME %JAVA_HOME%```


### Building an image using the nativeCompile task (native-image tool)

The ```nativeCompile``` task is provided by the 
```org.graalvm.buildtools.native``` gradle plugin:

**To build use e.g:**

```./gradlew nativeCompile```

or 

```gradlew.bat nativeCompile``` (on Windows **X64 Native Tools terminal**)


(This takes about 1 minute even on a fast machine!).

Find the compiler output in

```build/native/nativeCompile/``` directory!


Now hopefully you can try your AOT-compiled Compose app :)
(default binary name is ```composegraal```)

### Running the GraalVM Tracing Agent (Optional Step)

The required json files generated in this step have already been included in this repository
under ```src/main/resources/META-INF/native-image```

The GraalVM compiler cannot always statically predict usage of
of Java Native Interface (JNI), Java Reflection, Dynamic Proxy objects. It also cannot
predict which resources will be opened by your program.
If you use JNI or Native libraries, reflection or depend on any code which does
you will most likely encounter a build error when invoking GraalVM, or a runtime error.

In order to use these features we must provide ```native-image```
with configuration meta data that tells the compiler which dynamic features will be used.
We also tell the compiler what resources will be required by the program, so that they can be included
in the output image. 
The configuration is contained in the files
```jni-config.json```, ```reflect-config.json```, ```resource-config.json```,
```predefined-classes-config.json``` and ```proxy-config.json```

**To automate the generation of these configuration files run:**

```./gradlew -Pagent run```

The program will be run on the JVM together with the GraalVM tracing agent, upon exit the
tracing agent configuration files will be output to 
```build/native/agent-output/run/```.

To copy or merge the configuration files with the existing ones under ```src/resources``` **use the gradle task:**

```./gradlew metaDataCopy```

The files will be copied and merged with any existing files in 
```src/main/resources/META-INF/native-image```

Now you can run ```nativeCompile``` to invoke GraalVM with the new
configuration meta data! 


## Further instructions
See official GraalVM docs
https://www.graalvm.org/latest/docs/

How to configure the graalvm Gradle plugin:
https://graalvm.github.io/native-build-tools/latest/gradle-plugin.html
## Acknowledgements

Jetpack Compose, JetBrains & GraalVM Team.

JWM by Nikita Prokopov [@tonsky](https://github.com/tonsky):

**https://github.com/HumbleUI/JWM**


JWM-Compose by [@smallshen](https://github.com/smallshen),
it is included in this repository under directory:
``src/club/eridani/compose/jwm``

**https://github.com/smallshen/JWM-Compose**




