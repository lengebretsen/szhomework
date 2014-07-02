package org.schoolzilla.homework;

import au.com.bytecode.opencsv.CSVReader;

import java.io.FileReader;
import java.io.IOException;

/**
 * Wrapper class around opencsv CSVReader that handles csv files with column headers
 * in the first row.
 *
 * @see http://opencsv.sourceforge.net/
 *
 * Created by lloydengebretsen on 6/28/14.
 */
public class CsvProcessor {
    private String inputFile;
    private CSVReader reader;

    private String[] headerRow;

    /**
     * Constructor takes in String location of the input CSV file. Also reads off the first
     * row of the file (column headers) of the file and stores it as a String[] for later
     * reference.
     *
     * @param inputFile String location of input CSV file
     * @throws IOException
     */
    public CsvProcessor(String inputFile) throws IOException {
        this.inputFile = inputFile;
        reader = new CSVReader(new FileReader(inputFile));
        headerRow = readLine();
    }

    /**
     * Reads the next line from the CSV file and returns it as a String[]
     * @return String[] with values from the next row in the CSV file or null if EoF
     * has been reached
     * @throws IOException
     */
    public String[] readLine() throws IOException {
        return reader.readNext();
    }

    public String[] getHeaderRow(){
        return headerRow;
    }

}
