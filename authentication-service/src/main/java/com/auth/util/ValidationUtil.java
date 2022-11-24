package com.auth.util;

import com.auth.util.exception.ErrorType;
import com.auth.util.exception.NotFoundException;
import org.slf4j.Logger;

import javax.servlet.http.HttpServletRequest;

public class ValidationUtil {

//    public static <T> T checkNotFoundWithId(T object, int id) {
//        return checkNotFound(object, "id=" + id);
//    }
//
    private ValidationUtil() {
    }
//
//    public static void checkNotFoundWithId(boolean found, int id) {
//        checkNotFound(found, "id=" + id);
//    }
//
//    public static <T> T checkNotFound(T object, String msg) {
//        checkNotFound(object != null, msg);
//        return object;
//    }
//
//    public static void checkNotFound(boolean found, String arg) {
//        if (!found) {
//            throw new NotFoundException(arg);
//        }
//    }
//
    public static Throwable getRootCause(Throwable t) {
        Throwable result = t;
        Throwable cause;

        while (null != (cause = result.getCause()) && (result != cause)) {
            result = cause;
        }
        return result;
    }

    public static String getMessage(Throwable e) {
        return e.getLocalizedMessage() != null ? e.getLocalizedMessage() : e.getClass().getName();
    }

    public static Throwable logAndGetRootCause(Logger log, HttpServletRequest req, Exception e, boolean logException, ErrorType errorType) {
        Throwable rootCause = getRootCause(e);
        if (logException) {
            log.error(errorType + " at request " + req.getRequestURL(), rootCause);
        } else {
            log.warn("{} at request  {}: {}", errorType, req.getRequestURL(), rootCause.toString());
        }
        return rootCause;
    }
}
