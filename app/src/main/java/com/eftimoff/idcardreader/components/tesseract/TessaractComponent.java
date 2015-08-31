package com.eftimoff.idcardreader.components.tesseract;

import com.eftimoff.idcardreader.ui.common.BaseFragment;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {TessaractModule.class})
public interface TessaractComponent {

    void inject(final BaseFragment baseFragment);

    Tesseract provideTesseract();

}