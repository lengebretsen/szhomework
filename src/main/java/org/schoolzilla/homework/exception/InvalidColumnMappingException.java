package org.schoolzilla.homework.exception;

import org.schoolzilla.homework.mapping.ColumnMapping;

/**
 * Exception thrown when there was a problem with validating a ColumnMapping.
 *
 * Created by lloydengebretsen on 6/29/14.
 */
public class InvalidColumnMappingException extends Exception {

    ColumnMapping invalidColumn;

    public InvalidColumnMappingException(String message, ColumnMapping mapping) {
        super(message);
        invalidColumn = mapping;

    }

    public InvalidColumnMappingException(String message) {
        super(message);
        invalidColumn = null;
    }

       public ColumnMapping getInvalidColumn() {
        return invalidColumn;
    }
}
