# ApromoreCore8.0
Apromore is an open-source, online business process analytics platform, supporting the full stack of process mining functionality, from automated process discovery through to predictive process monitoring.
These features are complemented by functionality for managing process model collections.
Visit [http://apromore.org](http://apromore.org) for more information and development resources.

Headquartered at The University of Melbourne, Apromore is the result of over ten years of research and development, with contributions from various universities and individuals and funding from the government and private sectors.

Project leader: Marcello La Rosa

Core contributors: Abel Armas-Cervantes, Adriano Augusto, Raffaele Conforti, Marlon Dumas, Hoang Nguyen, Alireza Ostovar, Artem Polyvyanyy, Simon Raboczi, Ilya Verenich

For a full list of contributors, visit [http://apromore.org/about](http://apromore.org/about).

![](src/resources/apromore_logo.png)


## Building
### Requirements
Development has been done on MacOS 10.14 "Mojave", but theoretically only the following requirements exist:

- git
- JDK 8, 9, 10, or 11
- [Apache Maven](https://maven.apache.org/) 3.6.0

### Procedure
- Obtain Raffaele Conforti's research code repository using `git clone https://github.com/raffaeleconforti/ResearchCode.git`.
- Enter the `ResearchCode` directory and build it using `mvn clean install`.
- Obtain the Apromore source tree using `git clone https://github.com/apromore/ApromoreCore8.0.git`.
- Enter the `ApromoreCore8.0` directory (henceforth `$APROMORE_HOME`) and execute `mvn clean install`.
- On Java 9 or later, execute `mvn javadoc:aggregate` to generate documentation at `$APROMORE_HOME/target/site/apidocs/index.html`.

## Running
### Requirements
- JRE 8, 9, 10, or 11 (prefer [OpenJDK](https://openjdk.java.net) rather than the Oracle JDK for licensing reasons)
- [Apache Karaf](https://karaf.apache.org/) 4.2.4 unpacked at an arbitrary directory `$KARAF_HOME`

### Procedure
- Copy the configuration file `$APROMORE_HOME/etc/org.apromore.cfg` into `$KARAF_HOME/etc/`.
- Execute `$KARAF_HOME/bin/karaf` to start the application server.
- From the Karaf prompt, issue the command `feature:repo-add mvn:org.apromore/features/LATEST/xml` to add the Apromore artifacts you built previously.
- Start Apromore using `feature:install apromore`.  This should not take more than a minute.

### Notes
- The application landing page is [`http://localhost:8181/index.zul`](http://localhost:8181/index.zul).
- By default there is one user named "karaf" with password "karaf".
  This can be changed by editing `$KARAF_HOME/etc/users.properties`.
- The server log is `$KARAF_HOME/data/log/karaf.log`.
- Karaf as shipped is configured to only use Java 1.8 on MacOS.
  You may bypass this by editing the file `$KARAF_HOME/bin/inc` to remove "`-v 1.8`" from the Darwin `JAVA_HOME` setting.
- To manually reset the system, remove the contents of `$KARAF_HOME/data/`, the file `$KARAF_HOME/etc/org.ops4j.datasource-apromore.cfg`, and the H2 database files `$KARAF_HOME/apromore.h2.db` and `$KARAF_HOME/apromore.trace.db`.
  


## MySQL
As distributed, the system creates an embedded H2 database.
With additional configuration, an external MySQL database management system can be used instead.

### Procedure
- Given a MySQL DBMS instance:
  - Ensure MySQL is configured to accept local TCP connections on port 3306 in its `.cnf` file; "skip-networking" should not be present.
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

  - Create and populate the database tables.

    ```
    mysql --user=root --password=MAcri apromore2 < src/sql/db-mysql.sql
    ```

  During development, security can be traded for convenience by permitting Apromore to create its own tables.
  To do this, grant the user "apromore" the additional permissions `ALTER`, `CREATE` and `DROP`.

- Edit `$APROMORE_HOME/features/src/main/feature/feature.xml` and under the element `<feature name="apromore">` replace the first dependency `apromore-datasource-h2` with `apromore-datasource-mysql`.
- Recompile the edited components, e.g. by issuing the command `mvn clean install -pl :features`.
- Remove any pre-existing database configuration `$KARAF_HOME/etc/org.ops4j.datasource-apromore.cfg`.
- Use `feature:add-repo` and `feature-install` as described in the previous section to start Apromore.

### Notes
- To use a different database password, name, or user: edit `$APROMORE_HOME/features/src/main/feature/feature.xml` and modify the contents of the element `<feature name="apromore-datasource-mysql">`.


## LDAP
These instructions are specific to the University of Melbourne's central authentication server, but should provide a reasonable example for other LDAP services.

### Requirements
- Network access to the University of Melbourne's central authentication service at `ldaps://centaur.unimelb.edu.au`

### Procedure
- Copy the file `$APROMORE_HOME/etc/centaur.xml` to `$KARAF_HOME/deploy/`.
  This provides a JAAS login module named "centaur", specific to the University of Melbourne's LDAP service.
- Edit `KARAF_HOME/etc/org.apromore.cfg` and change the property `jaas.loginConfigurationName` to `centaur`.


## Sandbox Security
VM-level sandboxing for OSGi bundles is supported.
Add the following requirements and procedure to **Running**.

### Requirements
- Only if you require bundles to be cryptographically signed, a JKS keystore `truststore.ks` in `$KARAF_HOME/etc/`

### Procedure
- Create a Java security policy file `apromore.policy` based on the example `$APROMORE_HOME/etc/apromore.policy`.
  You may need to add permissions if you've customized your Apromore distribution with additional bundles.
  Details on the policy file format can be found at [docs.oracle.com](https://docs.oracle.com/javase/7/docs/technotes/guides/security/PolicyFiles.html#FileSyntax).
  Take note of the following permissions beyond the stock Java ones:
  - [UserAdminPermission](https://osgi.org/javadoc/r6/cmpn/org/osgi/service/useradmin/UserAdminPermission.html) is used for role-based access control.
- Copy your security policy file `apromore.policy` to `$KARAF_HOME/etc/`.
- Enable the security policy by editing `$KARAF_HOME/etc/system.properties` to add the following properties.

  ```properties
  java.security.policy=${karaf.etc}/apromore.policy
  org.osgi.framework.security=osgi
  ```

- If you require bundles to be cryptographically signed, edit `$KARAF_HOME/etc/system.properties` to also add the following property:

  ```properties
  org.osgi.framework.trust.repositories=${karaf.etc}/trustStore.ks
  ```

- Restart Karaf.
- From the Karaf prompt, issue the command `feature:install framework-security`.


## PQL
The [Process Query Language](http://processquerying.com/pql/) allows process models to be selected by their behavioral properties.

### Requirements
- MySQL; PQL uses MySQL-specific stored procedures.
- JRE 8; the Lucene indexer will fail on other Java versions.

### Procedure
- Create the PQL database:

  ```
  mysql --user=root --password=MAcri
        CREATE USER 'pql_master'@'localhost' IDENTIFIED BY '123456';
        GRANT SELECT, INSERT, UPDATE, DELETE, LOCK TABLES, EXECUTE, SHOW VIEW, ALTER ON pql.* TO 'pql_master'@'localhost';
  ```

- Check out the repository: `git clone https://github.com/processquerying/PQL.git` followed by `git checkout locations`.
- Edit `PQL/PQL.ini` to substitute "newpql" with "pql?noAccessToProcedureBodies=true".
- Edit `PQL/sql/PQL.MySQL-1.3.sql` to substitute "%" with "localhost".
- Load the SQL tables:

  ```
  mysql --user=root --password=MAcri < PQL/sql/PQL.MySQL-1.3.sql
  ```

- Comment out tests 11-21 in `test/org/pql/test/PQLTest.java`.
- Execute `mvn clean test` which has the side effect of indexing the test data.
- Check out the repository: `git clone https://github.com/processquerying/PQL-UI.git`.
- Edit `PQL-UI/PQL.ini` to substitute "newpql" with "pql?noAccessToProcedureBodies=true".
- Enter the directory `PQL-UI` and issue the command `mvn clean install`.
- Start the web server with `mvn jetty:run`.
- Browse [locahost:8080/pql_zk](http://localhost:8080/pql_zk/).

Then magic doesn't yet happen, and:

- Start Apromore.
- From the Karaf prompt, issue the command `feature:install apromore-pql-ui`.
  "Query with PQL" should become available as a new option under the "Analyze" menu.
