package com.eftimoff.idcardreader;

import com.eftimoff.idcardreader.components.tesseract.text.TesseractTextCleaner;
import com.eftimoff.idcardreader.components.tesseract.text.numbersletters.NumbersLettersTesseractTextCleaner;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */
public class NumbersLettersTesseractTextCleanerTest {

    private TesseractTextCleaner tesseractTextCleaner;

    @Before
    public void setup() {
        tesseractTextCleaner = new NumbersLettersTesseractTextCleaner();
    }

    @Test
    public void testCleanText() {
        final String actual = tesseractTextCleaner.cleanText("aecwqecqwebqwe123");
        final String expected = "aecwqecqwebqwe123";
        assertEquals(expected, actual);
    }

    @Test
    public void testCleanTextCyrillic() {
        final String actual = tesseractTextCleaner.cleanText("асдцявеявеaecwqecqwebqwe123");
        final String expected = "асдцявеявеaecwqecqwebqwe123";
        assertEquals(expected, actual);
    }

    @Test
    public void tesCleanTextSpecialCharacters() {
        final String actual = tesseractTextCleaner.cleanText(".34532345123.6;'\\][paecwqecqwebqwe123");
        final String expected = "345323451236paecwqecqwebqwe123";
        assertEquals(expected, actual);
    }

    @Test
    public void tesCleanTextCyrillicSpecialCharacters() {
        final String actual = tesseractTextCleaner.cleanText(".3асдцевя4532345123.6;'явец\\][paecwqecqwebqwe123");
        final String expected = "3асдцевя45323451236явецpaecwqecqwebqwe123";
        assertEquals(expected, actual);
    }
}