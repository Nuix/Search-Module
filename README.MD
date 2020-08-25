Search Module
=============

![This script was last tested in Nuix 8.6](https://img.shields.io/badge/Script%20Tested%20in%20Nuix-8.6-green.svg)

View the GitHub project [here](https://github.com/Nuix/Search-Module) or download the latest release [here](https://github.com/Nuix/Search-Module/releases).

# Overview

**Written By:** Jason Wells

![sm3 12 0screenshot](https://user-images.githubusercontent.com/11775738/45580661-a9291c80-b848-11e8-8eed-71e77952e681.png)

A script which can be used to bulk search and tag items in a Nuix case.

# Getting Started

## Setup

Begin by downloading the latest release of this code.  Extract the contents of the archive into your Nuix scripts directory.  In Windows the script directory is likely going to be either of the following:

- `%appdata%\Nuix\Scripts` - User level script directory
- `%programdata%\Nuix\Scripts` - System level script directory

## Building from Source

This script relies on a JAR file `SearchModuleGUI.jar`, the source code of which lives in this repository in the `Java` sub directory.  While the compiled JAR is provided in [releases](https://github.com/Nuix/Search-Module/releases), if you clone the repository you will want to build your own copy.

To build the JAR, load the Java source files into your IDE of choice.  Make sure the build path includes `\lib\*.jar` from the Nuix install directory.  For example, if you have Nuix 7.6 installed, include all JAR files located in `C:\Program Files\Nuix\Nuix 7.6\lib` in you build path.

Note that the JAR is intended to be built against JRE 1.8.

# License

```
Copyright 2020 Nuix

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```
