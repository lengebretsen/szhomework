package org.schoolzilla.homework;

/**
 * Container for any rows that have a translation error. Stores the entire row,
 * error message with details about the error, and the index of the column in error.
 *
 * Created by lloydengebretsen on 6/29/14.
 */
public class ErrorRow {

    private String[] errorRow;
    private String errorMessage;
    private Integer errorPosition;


    public ErrorRow(String[] errorRow, String errorMessage, Integer errorPosition) {
        this.errorRow = errorRow;
        this.errorMessage = errorMessage;
        this.errorPosition = errorPosition;
    }

    public String[] getErrorRow() {
        return errorRow;
    }

    public void setErrorRow(String[] errorRow) {
        this.errorRow = errorRow;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public Integer getErrorPosition() {
        return errorPosition;
    }

    public void setErrorPosition(Integer errorPosition) {
        this.errorPosition = errorPosition;
    }
}
