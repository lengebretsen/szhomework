package org.schoolzilla.homework;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.schoolzilla.homework.exception.InvalidColumnMappingException;
import org.schoolzilla.homework.mapping.ColumnMapping;
import org.schoolzilla.homework.mapping.DataType;

import java.util.ArrayList;
import java.util.List;

/**
 *
 *
 * Created by lloydengebretsen on 6/28/14.
 */
public class Translator {

    private ColumnMapping primaryColumnMapping;
    private List<ColumnMapping> dataColumnMappings;

    private IdValidator idValidator = new SimpleIdValidator();//In real implementation this should be injected via Spring

    /**
     * Constructor that builds a Translator with the passed in column mappings.
     * @param columnMappings
     * @throws InvalidColumnMappingException
     */
    public Translator(List<ColumnMapping> columnMappings) throws InvalidColumnMappingException {
        loadColumnMappings(columnMappings);
    }

    /**
     * Translates a source row according to the column mapping definitions defined for this translator instance.
     *
     * @param inputRow String[] containing the column values for this row.
     * @return TranslatorOutput object containing translated output rows and error rows
     */
    public TranslatorOutput translateRow(String[] inputRow){
        TranslatorOutput outputContainer = new TranslatorOutput();

        ArrayList<String[]> outputRows = new ArrayList<String[]>();
        ArrayList<ErrorRow> errorRows = new ArrayList<ErrorRow>();

        //handle translating primaryColumn
        String primaryValue = inputRow[primaryColumnMapping.getSourcePosition()];
        int primaryPosition = primaryColumnMapping.getValueMapping().getDestinationPosition();

        //validate id against external source
        if(!idValidator.isValidId(primaryValue)){
            errorRows.add(new ErrorRow(inputRow, "Invalid primary key value, unable to process row.", primaryPosition));
        }
        else {
            //handle translation of remaining data rows
            for (int i = 0; i < dataColumnMappings.size(); i++) {
                ColumnMapping columnMapping = dataColumnMappings.get(i);
                String headerValue = columnMapping.getHeaderDestinationValue();
                String value = inputRow[columnMapping.getSourcePosition()];

                //check to see if the value exists, if not no row is added
                if (StringUtils.isNotBlank(value)) {
                    //make sure value is valid for the type defined in the value mapping
                    if (validateValue(value, columnMapping.getValueMapping().getDataType())) {
                        ArrayList<String> outputRow = new ArrayList<String>();
                        outputRow.add(primaryPosition, primaryValue);

                        //map header
                        outputRow.add(columnMapping.getHeaderDestinationPosition(), headerValue);

                        //map value
                        outputRow.add(columnMapping.getValueMapping().getDestinationPosition(), value);

                        outputRows.add(outputRow.toArray(new String[outputRow.size()]));
                    } else {
                        //validation failed so build an error row and add it to the error list
                        errorRows.add(new ErrorRow(inputRow,
                                "Invalid data value, unable to process row. Expected data type: " + columnMapping.getValueMapping().getDataType(),
                                columnMapping.getSourcePosition()));
                    }
                }
            }
        }

        outputContainer.setTranslatedRows(outputRows);
        outputContainer.setErrorRows(errorRows);
        return outputContainer;
    }

    /**
     * Outputs header row for the destination table.
     *
     * @return String[] of headers for the destination table.
     */
    public String[] headerRow(){
        ArrayList<String> outputRow = new ArrayList<String>();

        //generate header row for output csv
        outputRow.add(primaryColumnMapping.getValueMapping().getDestinationPosition() ,primaryColumnMapping.getValueMapping().getDestinationColumn());
        for (int i = 0; i < dataColumnMappings.size(); i++) {
            ColumnMapping columnMapping = dataColumnMappings.get(i);
            //header column
            int headerPos = columnMapping.getHeaderDestinationPosition();
            if(!indexExists(outputRow, headerPos) || outputRow.get(headerPos) == null) //only create the header if the position hasn't been filled already, otherwise extraneous columns are added
                outputRow.add(headerPos, columnMapping.getHeaderDestinationColumn());

            //value column
            int valuePos = columnMapping.getValueMapping().getDestinationPosition();
            if(!indexExists(outputRow, valuePos)  || outputRow.get(valuePos) == null) //only create the header if the position hasn't been filled already, otherwise extraneous columns are added
                outputRow.add(valuePos, columnMapping.getValueMapping().getDestinationColumn());
        }

        return outputRow.toArray(new String[outputRow.size()]);
    }


    /**
     * Takes a list of ColumnMapping objects and validates them. In particular checks for missing or duplicate id column definitions,
     * invalid column types, and invalid data types.
     * @param columnMappings List of ColumnMapping objects
     * @throws InvalidColumnMappingException If mappings contain invalid values.
     */
    private void loadColumnMappings(List<ColumnMapping> columnMappings) throws InvalidColumnMappingException {
        List<ColumnMapping> primaryMappings = new ArrayList<ColumnMapping>(1);
        List<ColumnMapping> dataMappings = new ArrayList<ColumnMapping>(columnMappings.size());

        for (int i = 0; i < columnMappings.size(); i++) {
            ColumnMapping columnMapping = columnMappings.get(i);
            if (columnMapping.getColumnType() != null) {
                switch (columnMapping.getColumnType()) {
                    case ID:
                        primaryMappings.add(columnMapping);
                        break;
                    case DATA:
                        dataMappings.add(columnMapping);
                        break;
                    default:
                        throw new InvalidColumnMappingException("Column type was missing or could not be converted to enum from JSON.", columnMapping);
                }
            }
            else {
                //column type was missing or was not a value that could be converted to an ENUM
                throw new InvalidColumnMappingException("Column type was missing or could not be converted to enum from JSON.", columnMapping);
            }
        }

        if(primaryMappings.size() != 1)
        {
            throw new InvalidColumnMappingException("Exactly 1 mapping with column type ID is required.");
        }
        else{
            primaryColumnMapping = primaryMappings.get(0);
        }

        dataColumnMappings = dataMappings;
    }

    /**
     * Simple validation for data values. In a real implementation this should be replaced with something more robust
     * Commons Validator, Spring Validation, etc.
     * @param value data to be validated
     * @param type  Enum indicating what type of data this is
     * @return
     */
    private boolean validateValue(String value, DataType type){
        switch (type) {
            case NUMERIC:
                return NumberUtils.isNumber(value);
            case STRING:
                return StringUtils.isNotBlank(value);
            case BOOLEAN:
                return StringUtils.equalsIgnoreCase(Boolean.FALSE.toString(), value) || StringUtils.equalsIgnoreCase(Boolean.TRUE.toString(), value);
            default:
                return false; //didn't recognize the value so return false
        }
    }

    /**
     * Helper method that checks to see if the list size has grown long enough to include the position at index
     * @param list
     * @param index
     * @return
     */
    private boolean indexExists(final List list, final int index){
        return index >= 0 && index < list.size();
    }

    public ColumnMapping getPrimaryColumnMapping() {
        return primaryColumnMapping;
    }

    public List<ColumnMapping> getDataColumnMappings() {
        return dataColumnMappings;
    }
}
