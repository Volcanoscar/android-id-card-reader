package com.eftimoff.idcardreader;

import org.junit.Before;
import org.junit.Test;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static org.junit.Assert.assertEquals;

/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */
public class DateUtilsTest {

    private static final String DATE_FORMATTER = "ddMMyyyy";
    private DateFormat dateFormat;

    @Before
    public void setup() {
        dateFormat = new SimpleDateFormat(DATE_FORMATTER);
    }


    @Test
    public void testCleanText() throws ParseException {
        final long actual = dateFormat.parse("01021988").getTime() / 1000;
        final long expected = 570664800;
        assertEquals(expected, actual);
    }
}