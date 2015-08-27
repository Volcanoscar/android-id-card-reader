package com.eftimoff.idcardreader.components.tesseract;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {TessaractModule.class})
public interface TessaractComponent {

    Tesseract provideTesseract();

}