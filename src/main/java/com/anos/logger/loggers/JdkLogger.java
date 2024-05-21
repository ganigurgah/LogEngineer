package com.anos.logger.loggers;

import com.anos.logger.loggers.base.BaseLogger;
import com.anos.logger.types.EntityEnums;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

/**
 * @author gani.gurgah
 */
public class JdkLogger extends BaseLogger {

    Logger logger;

    public JdkLogger(Class<?> clazz, URL configurationFileURL) {
        super(clazz, configurationFileURL);
        logger = LogManager.getLogManager().getLogger(clazz.getName());
        if (logger == null) {
            logger = Logger.getLogger(clazz.getName());
        }
        this.setLibraryType(EntityEnums.LoggerLibrary.JDK);
    }

    private InputStream getConfigurationFile() {
        this.LOG_CONFIGURATION_FILENAME = "JdkLog.properties";
        if (this.configurationFileURL == null) {
            this.defaultConfigInitialized = true;
            return this.getClass().getClassLoader().getResourceAsStream(this.LOG_CONFIGURATION_FILENAME);
        } else {
            try {
                return this.configurationFileURL.openStream();
            } catch (IOException e) {
                logger.log(Level.WARNING, e.getMessage());
                return this.getClass().getClassLoader().getResourceAsStream(this.LOG_CONFIGURATION_FILENAME);
            }
        }
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
        InputStream configFile = getConfigurationFile();
        LoggerConfig config = new LoggerConfig();
        LogManager logManager = LogManager.getLogManager();
        try {
            logManager.readConfiguration(configFile);
        } catch (IOException e) {
            logger.log(Level.WARNING, e.getMessage());
        }
        String logDir = logManager.getProperty("log.dir");
        if (!utils.isStringNothing(logDir)) {
            config.setLogFilePath(logDir);
        }
        String pattern = logManager.getProperty("java.util.logging.SimpleFormatter.format");
        if (!utils.isStringNothing(pattern)) {
            config.setMessagePattern(pattern);
        } else {
            //config.setMessagePattern("%1$s %2$s");
        }
        this.setConfig(config);
    }

    @Override
    protected Level getOwnLogLevelByLogLevel(EntityEnums.LogLevel logLevel) {
        if (logLevel == null) {
            logLevel = EntityEnums.LogLevel.INFO;
        }
        switch (logLevel) {
            case INFO:
                return Level.INFO;
            case DEBUG:
                return Level.CONFIG;
            case ERROR:
                return Level.SEVERE;
            case WARNING:
                return Level.WARNING;
            default:
                return Level.INFO;
        }
    }

    @Override
    protected String formatMessage(String message, EntityEnums.LogLevel logLevel, Throwable thrown) {
        initalizeConfig();
        Object userName = readFromThreadContext("userName");
        Object wsSessionToken = readFromThreadContext("wsSessionToken");
        Object requestId = readFromThreadContext("requestId");
        String logger = (userName == null ? "" : userName.toString()) + "    " +
                (wsSessionToken == null ? "" : wsSessionToken.toString()) + "    " +
                (requestId == null ? "" : requestId.toString());

        String result = "";
        if (utils.isStringNothing(config.getMessagePattern())) {
            config.setMessagePattern(null);
            SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss.SSS");
            result = "[" + sdf.format(Calendar.getInstance().getTime()) + "]  " +
                    getOwnLogLevelByLogLevel(logLevel).toString() + " " +
                    logger + "    " + getLogMethodInfo() + " " + message;
        } else
            result = String.format(
                    config.getMessagePattern(),
                    Calendar.getInstance().getTime(),
                    getLogMethodInfo(),
                    logger,
                    getOwnLogLevelByLogLevel(logLevel),
                    message,
                    thrown);
        return result;
    }
}
