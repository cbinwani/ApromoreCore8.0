# Coming Soon
This document contains configuration notes for features that aren't yet stable enough for the main README document.

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
