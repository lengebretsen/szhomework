package org.schoolzilla.homework.mapping;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * Created by lloydengebretsen on 6/28/14.
 */
public class ColumnMapping {

    private Integer sourcePosition;
    private ColumnType columnType;
    private ValueMapping valueMapping;

    private String headerDestinationColumn;
    private String headerDestinationValue;
    private Integer headerDestinationPosition;


    @Override
    public String toString() {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String json = gson.toJson(this);
        return json;
    }

    public Integer getSourcePosition() {
        return sourcePosition;
    }

    public void setSourcePosition(Integer sourcePosition) {
        this.sourcePosition = sourcePosition;
    }

    public ColumnType getColumnType() {
        return columnType;
    }

    public void setColumnType(ColumnType columnType) {
        this.columnType = columnType;
    }

    public ValueMapping getValueMapping() {
        return valueMapping;
    }

    public void setValueMapping(ValueMapping valueMapping) {
        this.valueMapping = valueMapping;
    }

    public String getHeaderDestinationColumn() {
        return headerDestinationColumn;
    }

    public void setHeaderDestinationColumn(String headerDestinationColumn) {
        this.headerDestinationColumn = headerDestinationColumn;
    }

    public String getHeaderDestinationValue() {
        return headerDestinationValue;
    }

    public void setHeaderDestinationValue(String headerDestinationValue) {
        this.headerDestinationValue = headerDestinationValue;
    }

    public Integer getHeaderDestinationPosition() {
        return headerDestinationPosition;
    }

    public void setHeaderDestinationPosition(Integer headerDestinationPosition) {
        this.headerDestinationPosition = headerDestinationPosition;
    }
}
