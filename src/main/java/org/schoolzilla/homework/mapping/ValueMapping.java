package org.schoolzilla.homework.mapping;

/**
 * Created by lloydengebretsen on 6/28/14.
 */
public class ValueMapping {
    private DataType dataType;
    private String destinationColumn;
    private Integer destinationPosition;

    public DataType getDataType() {
        return dataType;
    }

    public void setDataType(DataType dataType) {
        this.dataType = dataType;
    }

    public String getDestinationColumn() {
        return destinationColumn;
    }

    public void setDestinationColumn(String destinationColumn) {
        this.destinationColumn = destinationColumn;
    }

    public Integer getDestinationPosition() {
        return destinationPosition;
    }

    public void setDestinationPosition(Integer destinationPosition) {
        this.destinationPosition = destinationPosition;
    }
}
