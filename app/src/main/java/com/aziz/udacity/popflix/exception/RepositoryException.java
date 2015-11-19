package com.aziz.udacity.popflix.exception;

/**
 * General-purpose persistence exception.
 * <p/>
 * Google field naming convention:
 * Non-public, non-static field names start with m.
 * Static field names start with s.
 * Other fields start with a lower case letter.
 * Public static final fields (constants) are ALL_CAPS_WITH_UNDERSCORES.
 *
 * @author Aziz Kadhi
 */
public class RepositoryException extends RuntimeException {

    public RepositoryException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }

    public RepositoryException(String detailMessage) {
        super(detailMessage);
    }
}
