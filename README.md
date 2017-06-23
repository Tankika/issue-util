# Issue Tracker Utilities

## Prerequisities

Programs and tools listed below must be installed prior to compiling and/or using the library.

* Java 8 or above
* Maven 3.3.9 or above (only needed for compiling and test running)

## Installation

In order to use the library you need to include the `issue.util-x.jar` file in your application's runtime.

## Compilation

The library can be compiled by using the Maven tool on the command line (or with Maven integration within a suitable IDE). From the command line you need to invoke the `mvn clean package` command in the project's root directory, where the pom.xml file resides (if Maven is not on your environment path, then you need to include it or specifiy a full path to its installation directory). After the command has successfully finished, you will be able to find the packaged `issue.util-x.jar` archive in the `target/` directory in the project's root directory. You can also find the compiled `.class` files for the library sources and test sources in the `target/` directory in the `classes` and `test-classes` directories.

## Running Tests

Compiling the library will also run any unit tests found in the project, but you can also run only the tests without producing a `.jar` file. To do this, from the command line you need to navigate into he project's root folder. Here you can invoke the `mvn clean test` command, which will compile the source files and run the tests on them. You can find the result of the tests in the `target/surefire-reports/` directory.