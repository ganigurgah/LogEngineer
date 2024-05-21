package com.anos.logger.loggers;

import com.anos.logger.loggers.base.BaseLogger;
import com.anos.logger.types.EntityEnums;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.Appender;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.appender.RollingFileAppender;
import org.apache.logging.log4j.core.layout.PatternLayout;

import java.net.URISyntaxException;
import java.net.URL;
import java.util.Iterator;
import java.util.Map;

/**
 * @author gani.gurgah
 */
public class ApacheLogger extends BaseLogger {

    Logger logger;

    public ApacheLogger(Class<?> clazz, URL configurationFileURL) {
        super(clazz, configurationFileURL);
        logger = LogManager.getLogger(clazz);
        this.setLibraryType(EntityEnums.LoggerLibrary.APACHE);
    }

    private URL defaultConfigurationURL() {
        URL url = this.getClass().getClassLoader().getResource("Log4j2.xml");
        this.defaultConfigInitialized = true;
        return url;
    }

    @Override
    public void addLog(EntityEnums.LogLevel level, String message, Throwable ex, Object... args) {
        Level ownLogLevel = getOwnLogLevelByLogLevel(level);
        if (ex == null)
            logger.log(ownLogLevel, message);
        else logger.log(ownLogLevel, message, ex);
    }

    @Override
    public void initalizeConfig() {
        if (this.configurationFileURL == null) {
            this.configurationFileURL = defaultConfigurationURL();
        }
        LoggerContext context = (LoggerContext) LogManager.getContext(false);
        try {
            context.setConfigLocation(this.configurationFileURL.toURI());
        } catch (URISyntaxException e) {
            logger.log(Level.WARN, e);
        }

        LoggerConfig conf = new LoggerConfig();

        Map appendersMap = context.getConfiguration().getAppenders();
        if (appendersMap != null && !appendersMap.isEmpty()) {
            long i = 0;
            Iterator<Map.Entry<String, Appender>> it = appendersMap.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry<String, Appender> entry = it.next();

                if (entry.getValue().getClass().getName().contains("RollingFileAppender")) {
                    RollingFileAppender appender = (RollingFileAppender) entry.getValue();
                    PatternLayout layout = (PatternLayout) appender.getLayout();
                    conf.setMessagePattern(layout.getConversionPattern());
                    conf.setLogFilePath(appender.getFileName());
                }
            }
        }
        this.setConfig(conf);
        loggerContext = context;
    }

    @Override
    protected String formatMessage(String message, EntityEnums.LogLevel logLevel, Throwable thrown) {
        this.addToThreadContext("className", getLogMethodInfo());
        return message;
    }

    /*
        private String getPath() {
            RollingFileAppender fileAppender = null;
            Map<String, Appender> appenders = ((LoggerContext) this.loggerContext)
                    .getConfiguration().getAppenders();
            if (appenders != null && !appenders.isEmpty()) {

                for (Map.Entry<String, Appender> entry : appenders.entrySet()) {
                    if (entry.getValue() instanceof RollingFileAppender) {
                        fileAppender = (RollingFileAppender) entry.getValue();
                        break;
                    }
                }
            }
            if (fileAppender == null) {
                return null;
            } else {
                return fileAppender.getFileName();
            }
        }
         */
    @Override
    protected Level getOwnLogLevelByLogLevel(EntityEnums.LogLevel logLevel) {
        if (logLevel == null) {
            logLevel = EntityEnums.LogLevel.INFO;
        }
        switch (logLevel) {
            case DEBUG:
                return Level.DEBUG;
            case ERROR:
                return Level.ERROR;
            case WARNING:
                return Level.WARN;
            default:
                return Level.INFO;
        }

    }

}
