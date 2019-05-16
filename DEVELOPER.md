# Developer Notes
This document provides hints for modifying Apromore.


## Style guide
All source files with a `.java` extension will have a license header automatically inserted.
The copyright notice is based on the `inceptionYear` element in the `pom.xml`.
License header generation can also be performed from the command line:

```
mvn license:update-file-header
```

A style checker will then validate the Java sources.
The style is defined in `$APROMORE_HOME/apromore-parent/checkstyle.xml`.
Global exceptions to the style check are defined in `$APROMORE_HOME/apromore-parent/checkstyle-suppressions.xml`.
Local exceptions to the style check within particular files are annotated with `@SuppressWarning`.

### Requirements
- No line exceeding 80 characters
- No tab characters 
- No trailing whitespace 
- Doc comments


## API documentation generation
Documentation of the Java classes can be generated using Javadoc.

### Procedure
- From `$APROMORE_HOME`, execute `mvn javadoc:aggregate`.
- Browse `$APROMORE_HOME/target/site/apidocs/index.html`.


## Embedding third-party libraries
Third party libraries that are not available from [Maven Central](https://search.maven.org) are kept in an embedded Maven repository `$APROMORE_HOME/repository/`.
If additional libraries are required, they may be added to this embedded repository.

### Requirements
- The library to be installed (e.g. `OpenXES-20181205.jar`)

### Procedure
- From `$APROMORE_HOME`, to install `OpenXES-20181205.jar` as `org.deckfour.openxes-2.26.jar`:

  ```
  mvn install:install-file \
     -DlocalRepositoryPath=repository \
     -DgroupId=org.deckfour \
     -DartifactId=openxes \
     -Dversion=2.26 \
     -Dpackaging=jar \
     -Dfile=OpenXES-20181205.jar
  ```


## Icon generation
The graphic elements in the user interface are mostly derived from vector images in SVG format, but must be rasterized as PNG files before they can be used in bundles.
The SVG icons are sourced from [Font Awesome](https://fontawesome.com/icons).

### Requirements
- [Apache Batik](https://xmlgraphics.apache.org/batik/) 1.11 unpacked in `$APROMORE_HOME/src/batik-1.11/`
- GNU Make 3.81

### Procedure
- Icon dimensions can be controlled by editing the `ICON_SIZE` variable in `$APROMORE_HOME/Makefile`.
- Icon color can be controlled by editing `$APROMORE_HOME/src/icon.css`.
- From `$APROMORE_HOME`, execute `make`.  This will generate the icons and copy them into the source tree.
