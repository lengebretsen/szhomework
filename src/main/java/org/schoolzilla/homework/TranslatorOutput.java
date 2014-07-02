package org.schoolzilla.homework;

import java.util.List;

/**
 * Container Class to hold the output rows after they've been translated as well as any error rows.
 *
 * Created by lloydengebretsen on 6/29/14.
 */
public class TranslatorOutput {

    private List<String[]> translatedRows;
    private List<ErrorRow> errorRows;

    public List<String[]> getTranslatedRows() {
        return translatedRows;
    }

    public void setTranslatedRows(List<String[]> translatedRows) {
        this.translatedRows = translatedRows;
    }

    public List<ErrorRow> getErrorRows() {
        return errorRows;
    }

    public void setErrorRows(List<ErrorRow> errorRows) {
        this.errorRows = errorRows;
    }
}
