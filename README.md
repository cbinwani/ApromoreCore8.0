# ApromoreCore8.0
Apromore is an open-source, online business process analytics platform, supporting the full stack of process mining functionality, from automated process discovery through to predictive process monitoring.
These features are complemented by functionality for managing process model collections.
Visit [http://apromore.org](http://apromore.org) for more information and development resources.

Headquartered at The University of Melbourne, Apromore is the result of over ten years of research and development, with contributions from various universities and individuals and funding from the government and private sectors.

**Project leader:** Marcello La Rosa

**Core contributors:** Abel Armas-Cervantes, Adriano Augusto, Raffaele Conforti, Marlon Dumas, Hoang Nguyen, Alireza Ostovar, Artem Polyvyanyy, Simon Raboczi, Ilya Verenich

For a full list of contributors, visit [http://apromore.org/about](http://apromore.org/about).

![](src/resources/apromore_logo.png)


## Building
### Requirements
- [git](https://git-scm.com) 
- JDK 8, 9, 10, or 11
- [Apache Maven](https://maven.apache.org/) 3.6.0

### Procedure
- Obtain Raffaele Conforti's research code repository using `git clone https://github.com/raffaeleconforti/ResearchCode.git`.
- Enter the `ResearchCode` directory and build it using `mvn clean install`.
- Obtain the Apromore source tree using `git clone https://github.com/apromore/ApromoreCore8.0.git`.
- Enter the `ApromoreCore8.0` directory (henceforth `$APROMORE_HOME`) and execute `mvn clean install`.
- Optionally on Java 9 or later, execute `mvn javadoc:aggregate` to generate source documentation.

### Notes
- The source documentation landing page is `$APROMORE_HOME/target/site/apidocs/index.html`.

## Running
### Requirements
- JRE 8, 9, 10, or 11 (prefer [OpenJDK](https://openjdk.java.net) rather than the Oracle JDK for licensing reasons)
- [Apache Karaf](https://karaf.apache.org/) 4.2.4 unpacked at an arbitrary directory `$KARAF_HOME`

### Procedure
- Copy the configuration file `$APROMORE_HOME/etc/org.apromore.cfg` into `$KARAF_HOME/etc/`.
- Running under JRE 8 will normally fail (BPMN documents will fail to import) because it has an earlier version of the JAXB library (2.2.8).
  You may work around this problem by replacing `$KARAF_HOME/etc/jre.properties` with `APROMORE_HOME/etc/jre.properties`.
  This will force the use of JAXB 2.3.1 under JRE 8.
- Execute `$KARAF_HOME/bin/karaf` to start the application server.
- From the Karaf prompt, issue the command `feature:repo-add mvn:org.apromore/features/LATEST/xml` to add the Apromore artifacts you built previously.
- Start Apromore using `feature:install apromore`.  This should not take more than a minute.

### Notes
- The application landing page is [`http://localhost:8181/index.zul`](http://localhost:8181/index.zul).
  Beware that `http://localhost:8181/` does not redirect to the landing page.
- By default there is one user named "karaf" with password "karaf".
  This can be changed by editing `$KARAF_HOME/etc/users.properties`.
- The server log is `$KARAF_HOME/data/log/karaf.log`.
- To manually reset the Apromore application, remove the following files:
  - `$KARAF_HOME/apromore.h2.db`
  - `$KARAF_HOME/apromore.trace.db`
  - `$KARAF_HOME/etc/org.ops4j.datasource-apromore.cfg`
- To manually reset the Karaf server, remove the contents of `$KARAF_HOME/data/`.
  


## MySQL
As distributed, the system creates an embedded H2 database.
With additional configuration, an external MySQL database management system can be used instead.

### Requirements
- A running MySQL 5.7 server, plus the `mysql` and `mysqladmin` commands

### Procedure
- Set the root password of MySQL to the default used by Apromore

  ```
  mysqladmin -u root password MAcri
  ```

- Create a database named "apromore2" in your MySQL server:

  ```
  mysqladmin --user=root --password=MAcri create apromore2
  ```

- Create user "apromore" with appropriate permissions:

  ```
  mysql --user=root --password=MAcri
      CREATE USER 'apromore'@'localhost' IDENTIFIED BY 'MAcri';
      GRANT SELECT, INSERT, UPDATE, DELETE, LOCK TABLES, EXECUTE, SHOW VIEW ON apromore2.* TO 'apromore'@'localhost';
  ```
  During development, security can be traded for convenience by permitting Apromore to create its own tables.
  To do this, grant the user "apromore" the additional permissions `ALTER`, `CREATE` and `DROP`.

- Create and populate the database tables.
  This step is optional if the additional user permissions were granted in the previous step.

  ```
  mysql --user=root --password=MAcri apromore2 < src/sql/db-mysql.sql
  ```

- Edit `$APROMORE_HOME/features/src/main/feature/feature.xml` and under the element `<feature name="apromore">` replace the first dependency `apromore-datasource-h2` with `apromore-datasource-mysql`.
- Install the modifications to `feature.xml` by issuing the command `mvn install -pl :features`.
- Remove any pre-existing database configuration file `$KARAF_HOME/etc/org.ops4j.datasource-apromore.cfg`.

### Notes
- To use a different database password, name, or user: edit `$APROMORE_HOME/features/src/main/feature/feature.xml` and modify the contents of the element `<feature name="apromore-datasource-mysql">`.


## LDAP
As distributed, the Apromore application uses the same credentials as the Karaf server.
With addition configuration, credentials can instead be provided by an external LDAP server.

These instructions use the University of Melbourne's central authentication server as an example.

### Requirements
- Network access to `ldaps://centaur.unimelb.edu.au`

### Procedure
- Copy the file `$APROMORE_HOME/etc/centaur.xml` to `$KARAF_HOME/deploy/`.
  This provides a JAAS login module named "centaur" backed by the University of Melbourne's central authentication server.
- Edit `KARAF_HOME/etc/org.apromore.cfg` and change the property `jaas.loginConfigurationName` to `centaur`.
