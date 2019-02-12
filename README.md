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
Development has been done on MacOS 10.14.3 "Mojave", but theoretically only the following requirements exist:

- JDK 10 (1.8 or later)
- [Apache Maven](https://maven.apache.org/) 3.6.0

### Procedure
Assuming the bash shell:

- Build the previous version of Apromore, since v8 recycles some of the previous bundles.  Note that this requires Java 8.
  The modified bundles are the following:
  - bpmntk-osgi
  - raffaeleconforti-osgi
- Then from the top of the v8 directory, execute `mvn clean install javadoc:aggregate`.

## Running
### Requirements
- JDK 10, 9, 1.8 (Karaf isn't cleared for 11 yet)
- [Apache Karaf](https://karaf.apache.org/) 4.2.2

### Procedure
- Karaf as shipped is configured to only use Java 8 on MacOS.
  Remove this limitation by editing the file `$KARAF_HOME/bin/inc` to remove "`-v 1.8`" from the Darwin `JAVA_HOME` setting.
- Add a JAAS login module named "apromore", specific to your local installation.
  The example provided is the file `$APROMORE_HOME/etc/centaur.xml` for UoM's LDAP service; this file can be copied to `$KARAF_HOME/deploy/`.
- Copy the configuration file `$APROMORE_HOME/etc/org.apromore.cfg` into `$KARAF_HOME/etc/`.
- Execute `$KARAF_HOME/bin/karaf` to start the application server.
- From the Karaf prompt, issue the command `feature:repo-add mvn:org.apromore/features/LATEST/xml` to add the Apromore artifacts you built previously.
- Start Apromore using `feature:install apromore`.  You should be able to navigate to [`http://localhost:8181/index.zul`](http://localhost:8181/index.zul).

## MySQL
As distributed, the system creates an embedded H2 database management system.
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
    mysql --user=root --password=MAcri < src/sql/db-mysql.sql
    ```

  During development, security can be traded for convenience by permitting Apromore to create its own tables.
  To do this, grant the user "apromore" the additional permissions `ALTER`, `CREATE` and `DROP`.

- Edit the `META-INF/persistence.xml` files in each Eclipselink component, replacing `HSQL` with `MYSQL`.
- Edit `feature.xml` and in the feature `apromore` replace the dependency `apromore-datasource-h2` with `apromore-datasource-mysql`.
- Recompile the edited components.
- Remove any pre-existing database configuration `$KARAF_HOME/etc/org.ops4j.datasource-apromore.cfg`.

## Securing
VM-level sandboxing for OSGi bundles is supported.
Add the following requirements and procedure to **Running**.

### Requirements
Only if you require bundles to be cryptographically signed, create a JKS keystore `truststore.ks` in `$KARAF_HOME/etc/`.

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
