package org.apromore.itest;

/*-
 * #%L
 * Apromore :: integration test
 * %%
 * Copyright (C) 2018 - 2019 The Apromore Initiative
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Lesser Public License for more details.
 * 
 * You should have received a copy of the GNU General Lesser Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/lgpl-3.0.html>.
 * #L%
 */

import java.io.File;
import javax.inject.Inject;
import org.apromore.user.User;
import org.apromore.user.UserService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.ops4j.pax.exam.Configuration;
import org.ops4j.pax.exam.ConfigurationManager;
import static org.ops4j.pax.exam.CoreOptions.repository;
import static org.ops4j.pax.exam.CoreOptions.maven;
import static org.ops4j.pax.exam.CoreOptions.mavenBundle;
import org.ops4j.pax.exam.Option;
import org.ops4j.pax.exam.junit.PaxExam;
import org.ops4j.pax.exam.options.MavenArtifactUrlReference;
import static org.ops4j.pax.exam.karaf.options.KarafDistributionOption.configureConsole;
import static org.ops4j.pax.exam.karaf.options.KarafDistributionOption.features;
import static org.ops4j.pax.exam.karaf.options.KarafDistributionOption.karafDistributionConfiguration;
import static org.ops4j.pax.exam.karaf.options.KarafDistributionOption.keepRuntimeFolder;
import org.ops4j.pax.exam.options.MavenUrlReference;
//import org.ops4j.pax.exam.sample8.ds.Calculator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.zk.ui.Sessions;

@RunWith(PaxExam.class)
public class ITest {

    private static Logger LOG = LoggerFactory.getLogger(ITest.class);

    @Inject
    protected UserService userService;

    @Configuration
    public Option[] config() {

        MavenArtifactUrlReference karafUrl = maven()
            .groupId("org.apache.karaf")
            .artifactId("apache-karaf")
            .version(karafVersion())
            .type("zip");

        MavenUrlReference karafStandardRepo = maven()
            .groupId("org.apache.karaf.features")
            .artifactId("standard")
            .version(karafVersion())
            .classifier("features")
            .type("xml");

        /*
        MavenUrlReference apromoreRepo = maven()
            .groupId("org.apromore")
            .artifactId("features")
            .version("1.0-SNAPSHOT")
            .classifier("features")
            .type("xml");
        */

        return new Option[] {
            // KarafDistributionOption.debugConfiguration("5005", true),
            karafDistributionConfiguration()
                .frameworkUrl(karafUrl)
                .unpackDirectory(new File("target", "exam"))
                .useDeployFolder(false),
            keepRuntimeFolder(),
            configureConsole().ignoreLocalConsole(),
            features(karafStandardRepo , "scr"),
            features(karafStandardRepo , "aries-blueprint"),
            features(karafStandardRepo , "http"),
            features(karafStandardRepo , "http-whiteboard"),
            features(karafStandardRepo , "jpa"),
            mavenBundle()
                .groupId("org.apromore")
                .artifactId("zk")
                .versionAsInProject().start(),
            mavenBundle()
                .groupId("org.apromore")
                .artifactId("ui-spi")
                .versionAsInProject().start(),
            mavenBundle()
                .groupId("org.apromore")
                .artifactId("ui")
                .versionAsInProject().start(),
            mavenBundle()
                .groupId("org.apromore")
                .artifactId("user-api")
                .versionAsInProject().start(),
            mavenBundle()
                .groupId("org.apromore")
                .artifactId("user-logic")
                .versionAsInProject().start(),
        };
    }

    public static String karafVersion() {
        ConfigurationManager cm = new ConfigurationManager();
        String karafVersion = cm.getProperty("pax.exam.karaf.version", "4.2.5");
        return karafVersion;
    }

    @Test
    public void testGetUser() {
        LOG.info("Get user service: {}", userService);
        Assert.assertNotNull(userService);
        Assert.assertNotNull(Sessions.getCurrent());
        try {
            User user = userService.getUser();
        LOG.info("Get user: {}", user);
        Assert.assertNull(user);
        } catch (NullPointerException e) { e.printStackTrace(); } 
    }

}
