package com.eftimoff.idcardreader.components.tesseract.text.numbersletters;

import com.eftimoff.idcardreader.components.tesseract.text.TesseractTextCleaner;

public class NumbersLettersTesseractTextCleaner implements TesseractTextCleaner {

    @Override
    public String cleanText(final String text) {
        return text.replaceAll("[^\\p{L}0-9]", "");
    }
}
