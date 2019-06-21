package org.apromore.pql.impl;

/*-
 * #%L
 * Apromore :: pql-logic
 * %%
 * Copyright (C) 2019 The Apromore Initiative
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

import org.apromore.pql.PQLService;
import org.osgi.service.component.annotations.Component;
import org.pql.api.PQLAPI;
import org.pql.ini.PQLIniFile;
import org.pql.query.PQLQueryResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Implementation of {@link PQLService}.
 */
@Component
public class PQLServiceImpl implements PQLService {
    private static final Logger LOGGER =
        LoggerFactory.getLogger(PQLServiceImpl.class);

    @Override
    public void test() {
        LOGGER.info("Test");

        try {
            PQLIniFile iniFile = new PQLIniFile();
            if (!iniFile.load()) {
                iniFile.create();
                if (!iniFile.load()) {
                    LOGGER.error("Cannot load PQL ini file.");
                    return;
                }
            }

            PQLAPI pql = new PQLAPI(
                iniFile.getMySQLURL(),
                iniFile.getMySQLUser(),
                iniFile.getMySQLPassword(),
                iniFile.getPostgreSQLHost(),
                iniFile.getPostgreSQLName(),
                iniFile.getPostgreSQLUser(),
                iniFile.getPostgreSQLPassword(),
                iniFile.getLoLA2Path(),
                iniFile.getLabelSimilaritySeacrhConfiguration(),
                iniFile.getIndexType(),
                iniFile.getLabelManagerType(),
                iniFile.getDefaultLabelSimilarityThreshold(),
                iniFile.getIndexedLabelSimilarityThresholds(),
                iniFile.getNumberOfQueryThreads(),
                iniFile.getDefaultBotMaxIndexTime(),
                iniFile.getDefaultBotSleepTime());
            PQLQueryResult result = pql.query("SELECT * FROM *;");
            if (result.getNumberOfParseErrors() != 0) {
                for (String message: result.getParseErrorMessages()) {
                    LOGGER.error(message);
                }
                return;
            }

            LOGGER.info("Result: " + result.getSearchResults());

        } catch (Exception e) {
            LOGGER.error("Unable to make PQL query", e);
        }
    }
}
