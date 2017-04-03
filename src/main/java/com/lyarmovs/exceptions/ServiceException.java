package com.lyarmovs.exceptions;

/**
 * @author Lev Yarmovsky
 * @version $Id: ServiceException.java,v 1.0 3/31/2017 6:08 PM lyarmovs Exp $
 *
 * Runtime exception for trapping service errors
 */
public class ServiceException extends RuntimeException {
    public ServiceException() {
    }

    public ServiceException(String message) {
        super(message);
    }

    public ServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}
