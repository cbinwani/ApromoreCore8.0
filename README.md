# ApromoreCode8.0

## Building
### Requirements
Development has been done on MacOS 10.14.3 "Mojave", but theoretically only the following requirements exist:
- JDK 9 or later
- Apache Maven 3.6.0 (https://maven.apache.org/)

### Procedure
Assuming the bash shell:
- Build the previous version of Apromore, since v8 recycles some of the previous bundles.  Note that this requires Java 8.
- Then from the top of the v8 directory, execute `mvn clean install javadoc:aggregate`.

## Running
### Requirements
- JDK 10 or JDK 9
- Apache Karaf 4.2.2 (https://karaf.apache.org/)

### Procedure
- Karaf as shipped is configured to only use Java 8 on MacOS.  Remove this limitation by editing the file $KARAF\_HOME/bin/inc to remove "-v 1.8" from the Darwin JAVA\_HOME setting.
- Add a JAAS login module named "apromore", specific to your local installation.  The example provided is the file centaur.xml for UoM's LDAP service; this file can be copied to $KARAF\_HOME/deploy/.
- Copy the configuration file org.apromore.cfg into $KARAF\_HOME/etc/.
- Execute `$KARAF\_HOME/bin/karaf` to start the application server.
- From the karaf prompt, issue the command `feature:repo-add mvn:org.apromore/features/LATEST/xml` to add the Apromore artifacts you built previously.
- Start Apromore using `feature:install apromore`.  You should be able to navigate to [the landing page](http://localhost:8181/index.zul).
