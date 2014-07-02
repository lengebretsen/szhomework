package org.schoolzilla.homework;

/**
 * Interface for validating id values against an external source.
 *
 * Created by lloydengebretsen on 6/28/14.
 */
public interface IdValidator {

    public boolean isValidId(Object id);
}
