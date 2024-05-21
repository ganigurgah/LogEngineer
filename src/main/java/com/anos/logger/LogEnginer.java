package com.anos.logger;

import com.anos.logger.loggers.ApacheLogger;
import com.anos.logger.loggers.JdkLogger;
import com.anos.logger.loggers.base.BaseLogger;
import com.anos.logger.types.EntityEnums.LogLevel;
import com.anos.logger.types.EntityEnums.LoggerLibrary;

import java.net.URL;

/**
 * @author gani.gurgah
 */
public class LogEnginer {

    BaseLogger logger;

    LogEnginer() {
    }

    public static LogEnginer initialize(Class<?> clazz, LoggerLibrary libraryType, URL configurationFileURL) {
        LogEnginer enginer = new LogEnginer();

        switch (libraryType) {
            case JDK:
            case CONSOLE:
                enginer.logger = new JdkLogger(clazz, configurationFileURL);
                break;
            case APACHE:
                enginer.logger = new ApacheLogger(clazz, configurationFileURL);
                break;
        }
        return enginer;
    }

    public static LogEnginer initializeApacheLogger(Class<?> clazz, URL configurationFile) {
        return initialize(clazz, LoggerLibrary.APACHE, configurationFile);
    }

    public static LogEnginer initializeJdkLogger(Class<?> clazz, URL configurationFile) {
        return initialize(clazz, LoggerLibrary.JDK, configurationFile);
    }

    public BaseLogger getInitializedLogger() {
        return logger;
    }

    //<editor-fold defaultstate="collapsed" desc="ADD METHODS">
    private void addLog(LogLevel level, String message, Throwable ex, Object... args) {
        String logMessage;

        if (isDebug() || level == LogLevel.DEBUG) {
            logMessage = logger.getTraceOfThread(ex == null ? null : ex.getStackTrace()).toString();
        } else {
            logMessage = logger.prepareMessage(message, level, ex, args);
        }
        logger.addLog(level, logMessage, ex, args);
    }

    public void addToThreadContext(Object key, Object value) {
        getInitializedLogger().addToThreadContext(key, value);
    }

    public Object readFromThreadContext(Object key) {
        return getInitializedLogger().readFromThreadContext(key);
    }

    public String getFilePath() {
        return getInitializedLogger().getFilePath();
    }

    public boolean isDebug() {
        return getInitializedLogger().isDebug();
    }

    public void setDebug(boolean debug) {
        getInitializedLogger().setDebug(debug);
    }

    public void info(String message) {
        addLog(LogLevel.INFO, message, null);
    }

    public void info(String message, Throwable t) {
        addLog(LogLevel.INFO, message, t);
    }

    public void info(Throwable t) {
        addLog(LogLevel.INFO, null, t);
    }

    public void error(String message) {
        addLog(LogLevel.ERROR, message, null);
    }

    public void error(String message, Throwable t) {
        addLog(LogLevel.ERROR, message, t);
    }

    public void error(Throwable t) {
        addLog(LogLevel.ERROR, null, t);
    }

    public void debug(String message) {
        addLog(LogLevel.DEBUG, message, null);
    }

    public void debug(Throwable t) {
        addLog(LogLevel.DEBUG, null, t);
    }

    public void debug(String message, Throwable t) {
        addLog(LogLevel.DEBUG, message, t);
    }

    public void warn(String message) {
        addLog(LogLevel.WARNING, message, null);
    }

    public void warn(Throwable t) {
        addLog(LogLevel.WARNING, null, t);
    }

    public void warn(String message, Throwable t) {
        addLog(LogLevel.WARNING, message, t);
    }
    //</editor-fold>
}
