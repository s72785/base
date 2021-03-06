/*
 * Copyright 2016 Karlsruhe Institute of Technology.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package edu.kit.dama.mdm.audit.impl;

import edu.kit.dama.commons.exceptions.ConfigurationException;
import edu.kit.dama.mdm.audit.interfaces.AbstractAuditPublisher;
import edu.kit.dama.mdm.audit.types.AuditEvent;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.TimeZone;
import org.apache.commons.configuration.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author jejkal
 */
public class LogbackPublisher extends AbstractAuditPublisher {

    private static final Logger LOGGER = LoggerFactory.getLogger(LogbackPublisher.class);

    @Override
    public boolean initialize() {
        //nothing to do
        LOGGER.debug("LogbackPublisher successfully initialized.");
        return true;
    }

    @Override
    public void destroy() {
        LOGGER.debug("LogbackPublisher destroyed.");
    }

    @Override
    public void publish(AuditEvent entry) {
        StringBuilder b = new StringBuilder();
        final DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        df.setTimeZone(TimeZone.getTimeZone("UTC"));
        b.append(entry.getPid()).append("|").append(entry.getEventType()).append("|").append(entry.getCategory()).append("|").append(df.format(entry.getEventDate())).append("|").append(entry.getOwner()).append("|").append(entry.getAgent()).append("|").append(entry.getResource()).append("|").append(entry.getDetails());
        LOGGER.info(b.toString());
    }

    @Override
    public boolean performCustomConfiguration(Configuration config) throws ConfigurationException {
        LOGGER.debug("LogbackPublisher successfully configured.");
        return true;
    }

}
