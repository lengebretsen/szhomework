package org.schoolzilla.homework;

import org.junit.Before;
import org.junit.Test;


import static org.junit.Assert.*;
import java.io.IOException;

/**
 * Created by lloydengebretsen on 6/28/14.
 */
public class CsvProcessorTest {

    CsvProcessor csvProcessor;


    @Before
    public void setup() throws IOException {
        csvProcessor = new CsvProcessor("./src/test/resources/csv/input.csv");
    }


    @Test
    public void testCsvRead() throws IOException {
        String[] headers = csvProcessor.getHeaderRow();
        String[] row = csvProcessor.readLine();

        assertEquals( 3, headers.length);
        assertEquals("Student Number", headers[0]);
        assertEquals("Math Score", headers[1]);
        assertEquals("Science Score", headers[2]);

        assertEquals( 3, row.length);
        assertEquals("1597530", row[0]);
        assertEquals("100", row[1]);
        assertEquals("80", row[2]);
    }

    @Test
    public void testEndOfFile() throws IOException {
        String[] row = csvProcessor.readLine();
        assertEquals( 3, row.length);
        row = csvProcessor.readLine();
        assertEquals( 3, row.length);
        row = csvProcessor.readLine();
        assertEquals( 3, row.length);
        row = csvProcessor.readLine();
        assertNull(row);
    }

}
