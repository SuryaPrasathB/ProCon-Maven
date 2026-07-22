package com.tasnetwork.calibration.conveyor.logger;

import org.apache.log4j.Logger;

/**
 * Utility class to standardise logging output across the application.
 * Enforces the format: [BAY] [CONTEXT] [EVENT] - message
 */
public class LogUtil {

    /**
     * Standardized info logging.
     * 
     * @param logger  The logger instance (e.g. Comm.logger, Ft.logger)
     * @param bay     The Bay Name/Key
     * @param context The sequence or class name
     * @param event   The event descriptor (ENTRY, EXIT, WAITING, ERROR, etc)
     * @param message The detailed message
     */
    public static void logInfo(Logger logger, String bay, String context, String event, String message) {
        logger.info(String.format("[%s] [%s] [%s] - %s", bay, context, event, message));
    }
    
    /**
     * Standardized debug logging.
     * 
     * @param logger  The logger instance
     * @param bay     The Bay Name/Key
     * @param context The sequence or class name
     * @param event   The event descriptor
     * @param message The detailed message
     */
    public static void logDebug(Logger logger, String bay, String context, String event, String message) {
        if (logger.isDebugEnabled()) {
            logger.debug(String.format("[%s] [%s] [%s] - %s", bay, context, event, message));
        }
    }
    
    /**
     * Standardized error logging.
     * 
     * @param logger  The logger instance
     * @param bay     The Bay Name/Key
     * @param context The sequence or class name
     * @param event   The event descriptor
     * @param message The detailed message
     */
    public static void logError(Logger logger, String bay, String context, String event, String message) {
        logger.error(String.format("[%s] [%s] [%s] - %s", bay, context, event, message));
    }
    
    /**
     * Standardized error logging with an Exception stack trace.
     * 
     * @param logger  The logger instance
     * @param bay     The Bay Name/Key
     * @param context The sequence or class name
     * @param event   The event descriptor
     * @param message The detailed message
     * @param t       The exception/throwable
     */
    public static void logError(Logger logger, String bay, String context, String event, String message, Throwable t) {
        logger.error(String.format("[%s] [%s] [%s] - %s", bay, context, event, message), t);
    }
}
