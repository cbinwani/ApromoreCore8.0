# Developer Notes
This document provides hints for modifying Apromore.

## Source code
The source code is written in a subset of Java 8.
The subsetting is performed by the [Checker Framework](https://checkerframework.org) and comprises the following constraints:

- All references types disallow null values unless explicitly annotated `@Nullable`.

## Style guide
All source files with a `.java` extension will have a license header automatically inserted.
The copyright notice is based on the `inceptionYear` element in the `pom.xml`.
License header generation can also be performed from the command line:

```
mvn license:update-file-header
```

Java sources are validated with a style checker.
The style is defined in `$APROMORE_HOME/checkstyle.xml`.

### Bypassing style checking
Global exceptions to the style check are defined in `$APROMORE_HOME/checkstyle-suppressions.xml`.
Local exceptions to the style check within particular files are annotated with `@SuppressWarnings`.
If an entire module contains invalid source code, the following entry in its `pom.xml` will disable style checks:

```xml
<build>
  <plugins>
    ...
    <plugin>
      <groupId>org.apache.maven.plugins</groupId>
      <artifactId>maven-checkstyle-plugin</artifactId>
      <configuration>
        <skip>true</skip>
      </configuration>
    </plugin>
    ...
  </plugins>
</build>
```

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
Third party libraries that are not available from [Maven Central](https://maven.org) can be kept in embedded Maven repositories.

### Requirements
- The library to be installed (e.g. `OpenXES-20181205.jar`)

### Procedure
- From the `$APROMORE_HOME/openxes-osgi`, to install `OpenXES-20181205.jar` as `org.deckfour.openxes-2.26.jar`:

  ```
  mvn install:install-file \
     -Dfile=OpenXES-20181205.jar \
     -DlocalRepositoryPath=repository \
     -DgroupId=org.deckfour \
     -DartifactId=openxes \
     -Dversion=2.26 \
     -Dpackaging=jar \
     -DcreateChecksum=true
  ```
- Add the following declaration in the module's `pom.xml`:

  ```xml
  <repositories>
    <repository>
      <id>project-repository</id>
      <url>file://${project.basedir}/repository</url>
    </repository>
  </repositories>
  ```

- Other modules should not declare dependencies to the embedded artifacts, because they will not initially be present in a downloader's local Maven repository.


## Diagrams
The diagrams in `$APROMORE_HOME/src/draw.io/` can be edited by [draw.io](https://www.draw.io).


## Icons (Font-based)
ZK 8 bundles Font Awesome 4.3 which is quite different from the current version 5+.
These can be accessed by assigning the iconSclass property on ZK buttons and labels.
For example, the Font Awesome `fa-bell` icon would be obtained by setting iconSclass to `z-icon-bell`.
James Croft's [cheat sheet](https://fontawesome.bootstrapcheatsheets.com) summarizes the available icons.


## Icons (SVG)
Graphic elements in the user interface can be derived from vector images in SVG format, but must be rasterized as PNG files before they can be used in ZK.

### Requirements
- [Apache Batik](https://xmlgraphics.apache.org/batik/) 1.11

### Procedure
- Icon color can be controlled by editing `icon.css`.

  ```css
  svg {
    color: #888;
  }
  ```
- Execute the following command:

  ```
  java -jar batik-1.11/batik-rasterizer-1.11.jar icon.svg -d target -cssUser icon.css -maxw 24 -maxh 24
  ```
