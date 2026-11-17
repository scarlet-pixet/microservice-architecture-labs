package org.example.analytics.config;

import feign.Logger;
import org.slf4j.LoggerFactory;

public class OneLineFeignLogger extends Logger {

    private final org.slf4j.Logger log = LoggerFactory.getLogger(OneLineFeignLogger.class);

    @Override
    protected void log(String configKey, String format, Object... args) {
        String message = String.format(methodTag(configKey) + format, args)
                .replace('\n', ' ')
                .replace('\r', ' ')
                .replaceAll("\\s+", " ")
                .trim();
        log.info(message);
    }
}
