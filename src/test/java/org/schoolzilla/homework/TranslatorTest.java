package org.schoolzilla.homework;

import au.com.bytecode.opencsv.CSVWriter;
import com.google.gson.Gson;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.time.StopWatch;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.schoolzilla.homework.exception.InvalidColumnMappingException;
import org.schoolzilla.homework.mapping.ColumnMapping;
import org.schoolzilla.homework.mapping.ColumnType;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by lloydengebretsen on 6/28/14.
 */
public class TranslatorTest {

    @Rule
    public ExpectedException exception = ExpectedException.none();


    @BeforeClass
    public static void cleanupOutput() {
        try {
            File dir = new File("./src/test/output");
            FileUtils.deleteDirectory(dir);
        }
        catch (IOException e)
        {
            System.err.println("Error, cleaning up test ouput");
            e.printStackTrace();
        }

        File directory = new File("./src/test/output");
        directory.mkdir();
    }

    private Translator buildTranslator(String mappingFile) throws IOException, InvalidColumnMappingException {
        return new Translator(readMappingJsonFromFile(mappingFile));
    }

    private List<ColumnMapping> readMappingJsonFromFile(String fileLocation) throws IOException{
        String json = null;

        File f = new File(fileLocation);
        json = FileUtils.readFileToString(f, "UTF-8");
        ColumnMapping[] columnMappingArray = new Gson().fromJson(json, ColumnMapping[].class);
        return Arrays.asList(columnMappingArray);

    }

    @Test
    public void testLoadColumnMappings() throws IOException, InvalidColumnMappingException{
        Translator translator = buildTranslator("./src/test/resources/mapping_json/input_mapping.json");
        ColumnMapping primary = translator.getPrimaryColumnMapping();
        assertEquals(ColumnType.ID, primary.getColumnType());

        List<ColumnMapping> dataMappings = translator.getDataColumnMappings();
        assertEquals(2, dataMappings.size());
    }

    @Test
    public void testTranslateHeaders() throws IOException, InvalidColumnMappingException {
        Translator translator = buildTranslator("./src/test/resources/mapping_json/input_mapping.json");
        String[] headers = translator.headerRow();
        assertEquals(3, headers.length);
        assertEquals("student_id", headers[0]);
        assertEquals("subject", headers[1]);
        assertEquals("score", headers[2]);
    }

    @Test
    public void testTranslateRows() throws IOException, InvalidColumnMappingException {
        Translator translator = buildTranslator("./src/test/resources/mapping_json/input_mapping.json");
        CsvProcessor csvProcessor = new CsvProcessor("./src/test/resources/csv/input.csv");
        List<String[]> outputRows = new ArrayList<String[]>();
        List<ErrorRow> errorRows = new ArrayList<ErrorRow>();

        String[] inputRow;
        while((inputRow = csvProcessor.readLine()) != null){
            TranslatorOutput output = translator.translateRow(inputRow);
            outputRows.addAll(output.getTranslatedRows());
            errorRows.addAll(output.getErrorRows());
        }

        assertEquals(3, outputRows.size());
        assertEquals(1, errorRows.size());

        //spot check row
        assertEquals(3, outputRows.get(2).length);
        assertEquals("2468975", outputRows.get(2)[0]);
        assertEquals("Math", outputRows.get(2)[1]);
        assertEquals("85", outputRows.get(2)[2]);

        writeOutput("./src/test/output/smallOutputCsv.csv", translator.headerRow(), outputRows);
        writeErrors("./src/test/output/smallErrorCsv.csv", translator.headerRow(), errorRows);
    }

    /**
     * Similar test as testTranslateRows(), but adds some additional input columns and errors
     * @throws IOException
     * @throws InvalidColumnMappingException
     */
    @Test
    public void testTranslateRowsMedium() throws IOException, InvalidColumnMappingException {
        Translator translator = buildTranslator("./src/test/resources/mapping_json/input_mapping_big.json");
        CsvProcessor csvProcessor = new CsvProcessor("./src/test/resources/csv/inputMed.csv");
        List<String[]> outputRows = new ArrayList<String[]>();
        List<ErrorRow> errorRows = new ArrayList<ErrorRow>();

        String[] inputRow;
        while((inputRow = csvProcessor.readLine()) != null){
            TranslatorOutput output = translator.translateRow(inputRow);
            outputRows.addAll(output.getTranslatedRows());
            errorRows.addAll(output.getErrorRows());
        }

        assertEquals(17, outputRows.size());
        assertEquals(6, errorRows.size());

        //spot check row
        assertEquals(3, outputRows.get(4).length);
        assertEquals("2468975", outputRows.get(4)[0]);
        assertEquals("Reading", outputRows.get(4)[1]);
        assertEquals("100", outputRows.get(4)[2]);

        writeOutput("./src/test/output/medOutputCsv.csv", translator.headerRow(), outputRows);
        writeErrors("./src/test/output/medErrorCsv.csv", translator.headerRow(), errorRows);
    }

    @Test
    public void testTranslateRowsBoolean() throws IOException, InvalidColumnMappingException {
        Translator translator = buildTranslator("./src/test/resources/mapping_json/input_mapping_boolean.json");
        CsvProcessor csvProcessor = new CsvProcessor("./src/test/resources/csv/inputBoolean.csv");
        List<String[]> outputRows = new ArrayList<String[]>();
        List<ErrorRow> errorRows = new ArrayList<ErrorRow>();

        String[] inputRow;
        while((inputRow = csvProcessor.readLine()) != null){
            TranslatorOutput output = translator.translateRow(inputRow);
            outputRows.addAll(output.getTranslatedRows());
            errorRows.addAll(output.getErrorRows());
        }

        assertEquals(38, outputRows.size());
        assertEquals(1, errorRows.size());

        //spot check row
        assertEquals(3, outputRows.get(8).length);
        assertEquals("3", outputRows.get(8)[0]);
        assertEquals("Spanish", outputRows.get(8)[1]);
        assertEquals("true", outputRows.get(8)[2]);

        writeOutput("./src/test/output/booleanOutputCsv.csv", translator.headerRow(), outputRows);
        writeErrors("./src/test/output/booleanErrorCsv.csv", translator.headerRow(), errorRows);
    }

    /**
     * Example of streaming output as each row is processed by translator
     *
     * @throws IOException
     * @throws InvalidColumnMappingException
     */
    @Test
    public void testOutputLongCsv() throws IOException, InvalidColumnMappingException {
        Translator bigTranslator = buildTranslator("./src/test/resources/mapping_json/input_mapping_big.json");
        CsvProcessor csvProcessor = new CsvProcessor("./src/test/resources/csv/inputBig.csv");
        StopWatch timer = new StopWatch();
        timer.start();
        CSVWriter writer = new CSVWriter(new FileWriter("./src/test/output/testOutputLongCsv.csv"), ',', CSVWriter.NO_QUOTE_CHARACTER);

        writer.writeNext(bigTranslator.headerRow());
        String[] inputRow;
        while((inputRow = csvProcessor.readLine()) != null){
            TranslatorOutput output = bigTranslator.translateRow(inputRow);
            List<String[]> outputRows = output.getTranslatedRows();
            for (int i = 0; i < outputRows.size(); i++) {
                String[] row = outputRows.get(i);
                writer.writeNext(row);
            }
        }
        writer.close();
        timer.stop();
        System.out.println("Elapsed Time: " + timer.getTime());
    }

    @Test
    public void testMultiplePrimaryKeyMappings() throws IOException, InvalidColumnMappingException{
        exception.expect(InvalidColumnMappingException.class);
        exception.expectMessage("Exactly 1 mapping with column type ID is required.");
        new Translator(readMappingJsonFromFile("./src/test/resources/mapping_json/multiple_pk.json"));
    }

    @Test
    public void testNoPrimaryKeyMappings() throws IOException, InvalidColumnMappingException{
        exception.expect(InvalidColumnMappingException.class);
        exception.expectMessage("Exactly 1 mapping with column type ID is required.");
        new Translator(readMappingJsonFromFile("./src/test/resources/mapping_json/no_pk.json"));
    }

    @Test
    public void testInvalidColumnType() throws IOException, InvalidColumnMappingException{
        exception.expect(InvalidColumnMappingException.class);
        exception.expectMessage("Column type was missing or could not be converted to enum from JSON.");
        new Translator(readMappingJsonFromFile("./src/test/resources/mapping_json/invalid_column.json"));
    }


    private void writeOutput(String filename, String[] headerRow, List<String[]> outputRows) throws IOException {
        CSVWriter writer = new CSVWriter(new FileWriter(filename), ',', CSVWriter.NO_QUOTE_CHARACTER);
        writer.writeNext(headerRow);
        for (int i = 0; i < outputRows.size(); i++) {
            String[] row = outputRows.get(i);
            writer.writeNext(row);
        }
        writer.close();
    }

    private void writeErrors(String filename, String[] headerRow, List<ErrorRow> errorRows) throws IOException {
        CSVWriter writer;
        writer = new CSVWriter(new FileWriter(filename), ',', CSVWriter.NO_QUOTE_CHARACTER);
        writer.writeNext(headerRow);
        for (int i = 0; i < errorRows.size(); i++) {
            String[] row = errorRows.get(i).getErrorRow();
            writer.writeNext(row);
        }
        writer.close();
    }
}
