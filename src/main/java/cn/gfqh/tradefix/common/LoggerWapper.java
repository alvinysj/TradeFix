package cn.gfqh.tradefix.common;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Created by Alvin on 2017/6/19.
 */
public class LoggerWapper {
    private final static Log logger = LogFactory.getLog(LoggerWapper.class);
    private final static String CLASS_NAME = LoggerWapper.class.getName();

    public static void debug(Object msg) {
        logger.debug(msg);
    }

    public static void debug(Object msg, Throwable t) {
        logger.debug(msg, t);
    }

    public static void info(Object msg) {
        logger.info(msg);
    }

    public static void info(Object msg, Throwable t) {
        logger.info(msg, t);
    }

    public static void trace(Object msg) {
        logger.trace(msg);
    }

    public static void trace(Object msg, Throwable t) {
        logger.trace(msg, t);
    }

    public static void warn(Object msg) {
        logger.warn(msg);
    }

    public static void warn(Object msg, Throwable t) {
        logger.warn(msg, t);
    }

    public static void error(Object msg) {
        logger.error(msg);
    }

    public static void error(Object msg, Throwable t) {
        logger.error(msg, t);
    }

    public static void fatal(Object msg) {
        logger.fatal(msg);
    }

    public static void fatal(Object msg, Throwable t) {
        logger.fatal(msg, t);
    }

    public static String getInvokeMessage(String message) {
        String msg = "";
        String className = "";
        String methodName = "";
        int lineNumber = 0;

        StackTraceElement stack[] = (new Throwable()).getStackTrace();
        int ix = 0;
        while (ix < stack.length) {
            StackTraceElement frame = stack[ix];
            String cname = frame.getClassName();
            if (cname.equals(CLASS_NAME)) {
                break;
            }
            ix++;
        }
        while (ix < stack.length) {
            StackTraceElement frame = stack[ix];
            String cname = frame.getClassName();
            if (!cname.equals(CLASS_NAME)) {
                className = cname;
                methodName = frame.getMethodName();
                lineNumber = frame.getLineNumber();
                break;
            }
            ix++;
        }

        msg = className + "." + methodName+"("+lineNumber+") " + message;
        return msg;
    }

    public static void traceCallStart() {
        debug(getInvokeMessage("START CALL"));
        //traceCalling();
    }

    public static void traceCalling() {
        info(getInvokeMessage("CALLING..."));
    }

    public static void traceCallEnd() {
        debug(getInvokeMessage("END CALL"));
    }
}
