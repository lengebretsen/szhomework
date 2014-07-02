package org.schoolzilla.homework;

import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of IdValidator for Unit testing purposes. This needs to be replaced with
 * a real implementation for anything beyond the PoC.
 *
 * Created by lloydengebretsen on 6/28/14.
 */
public class SimpleIdValidator implements IdValidator {

    private final List<String> VALID_KEY_LIST;

    public SimpleIdValidator() {
        VALID_KEY_LIST = new ArrayList<String>();

        VALID_KEY_LIST.add("1597530");
        VALID_KEY_LIST.add("2468975");
        VALID_KEY_LIST.add("1235523");
        VALID_KEY_LIST.add("4324444");
        VALID_KEY_LIST.add("2344444");
        VALID_KEY_LIST.add("1235555");
    }

    @Override
    public boolean isValidId(Object id) {
        //any id between 1 and 100,000 considered valid for generating large test files
        return Long.parseLong((String) id) <= 100000L || VALID_KEY_LIST.contains(id);
    }
}
