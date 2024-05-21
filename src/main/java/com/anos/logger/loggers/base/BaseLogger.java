package com.anos.logger.loggers.base;

import com.anos.logger.tools.Utils;
import com.anos.logger.types.EntityEnums;
import org.apache.logging.log4j.ThreadContext;

import java.net.URL;

/**
 * @author gani.gurgah
 */
public abstract class BaseLogger {
    protected Class<?> clazz;

    public class LoggerConfig {

        private String messagePattern;
        private String logFilePath;

        public boolean hasMessagePattern() {
            return messagePattern != null && messagePattern.trim().length() > 0;
        }

        public String getMessagePattern() {
            return messagePattern;
        }

        public void setMessagePattern(String messagePattern) {
            this.messagePattern = messagePattern;
        }

        public String getLogFilePath() {
            return logFilePath;
        }

        public void setLogFilePath(String logFilePath) {
            this.logFilePath = logFilePath;
        }
    }

    protected Object loggerContext;
    protected String LOG_CONFIGURATION_FILENAME = "LoggerConfig.xml";
    protected URL configurationFileURL;
    protected Utils utils;
    protected LoggerConfig config;
    protected boolean defaultConfigInitialized = false;

    private boolean debug;
    private EntityEnums.LoggerLibrary libraryType;

    public BaseLogger(Class<?> clazz, URL configurationFileURL) {
        this.utils = new Utils();
        this.config = new LoggerConfig();
        this.clazz = clazz;
        this.configurationFileURL = configurationFileURL;
        this.initalizeConfig();
    }

    public void addToThreadContext(Object key, Object value) {
        ThreadContext.put(key.toString(), value.toString());
    }

    public Object readFromThreadContext(Object key) {
        return ThreadContext.get(key.toString());
    }

    public StringBuilder getTraceOfThread(StackTraceElement[] stackTraceElements) {
        StackTraceElement[] steList = stackTraceElements;
        if (steList == null || steList.length <= 0) {
            steList = Thread.currentThread().getStackTrace();
        }
        StringBuilder sb = new StringBuilder();
        sb.append("\t\n!!! TRACE INFO START !!!");
        int rowId = 0;
        for (int i = 6; i < steList.length; i++) {
            StackTraceElement ste = steList[i];
            String callerClass = ste.getClassName();
            if (!callerClass.startsWith("com.akademi.")) {
                continue;
            }
            String callerMethod = ste.getMethodName();
            String linenumber = String.valueOf(ste.getLineNumber());
            rowId++;
            String formattedStr = "\t\n" + String.format("[%s - %s.%s(%s)]", rowId, callerClass, callerMethod, linenumber);
            sb.append(formattedStr);
        }
        sb.append("\t\n!!! TRACE INFO END !!!");
        return sb;
    }

    protected String getLogMethodInfo() {
        Thread currentThread = Thread.currentThread();
        StackTraceElement[] traces = currentThread.getStackTrace();
        int i = 1;
        boolean isBreak = false;
        while (!isBreak) {
            isBreak = i > traces.length;
            if (!isBreak) {
                StackTraceElement element = traces[i];
                isBreak = (element.getClassName().equals(clazz.getName()))
                        && (!element.getClassName().equals(this.getClass().getName()));
                i += isBreak ? 0 : 1;
            }
        }
        StackTraceElement methodInfo = traces[i];
        if (methodInfo.getClassName().contains(".Base")) {
            while (methodInfo.getClassName().contains(".Base")) {
                if (i >= traces.length) {
                    break;
                }
                methodInfo = traces[i++];
            }
        }
        if (methodInfo.getClassName().equalsIgnoreCase("SearchScheduledJob") || methodInfo.getMethodName().equalsIgnoreCase("SearchScheduledJob")) {
            System.err.println("debug");
        }
        String calledMethodInfo = methodInfo.getClassName();
        calledMethodInfo = calledMethodInfo.substring(calledMethodInfo.lastIndexOf(".") + 1, calledMethodInfo.length())
                + "." + methodInfo.getMethodName() + "()(" + methodInfo.getLineNumber() + ")";
        return calledMethodInfo;
    }

    public boolean isDebug() {
        return debug;
    }

    public void setDebug(boolean debug) {
        this.debug = debug;
    }

    public void setLibraryType(EntityEnums.LoggerLibrary libraryType) {
        this.libraryType = libraryType;
    }

    public String prepareMessage(String message, EntityEnums.LogLevel logLevel, Throwable ex, Object... args) {
        if (utils.isStringNothing(message)) {
            if (ex != null) {
                if (utils.isStringNothing(message))
                    message = ex.getMessage();
                else message += ex.getMessage();
            } else {
                if (args != null && args.length > 0) {
                    if (utils.isStringNothing(message))
                        message = "";
                    for (Object obj : args) {
                        if (obj != null && !utils.isStringNothing(obj.toString())) {
                            message += obj.toString();
                        }
                    }
                }
            }
        }

        if (utils.isStringNothing(message)) {
            return "";
        }
        return formatMessage(message, logLevel, ex);
    }

    public String getNameOfLogger() {
        return clazz.getName();
    }

    public LoggerConfig getConfig() {
        if (this.config == null) {
            this.initalizeConfig();
        }
        if (this.config == null) {
            return new LoggerConfig();
        } else {
            return this.config;
        }
    }

    public void setConfig(LoggerConfig config) {
        this.config = config;
    }


    public void setClazz(Class<?> clazz) {
        this.clazz = clazz;
    }

    public String getFilePath() {
        if (this.getConfig() != null) {
            return getConfig().getLogFilePath();
        } else {
            return "";
        }
    }

    //<editor-fold defaultstate="collapsed" desc="abstract methos">
    public abstract void addLog(EntityEnums.LogLevel level, String message, Throwable ex, Object... args);

    public abstract void initalizeConfig();

    protected abstract String formatMessage(String message, EntityEnums.LogLevel logLevel, Throwable thrown);

    protected abstract <T> T getOwnLogLevelByLogLevel(EntityEnums.LogLevel logLevel);
    //</editor-fold>
}
