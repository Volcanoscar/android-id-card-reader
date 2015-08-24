package com.eftimoff.idcardreader.components.common;

public abstract class Listener<T> {

    public abstract void onDone(T t);

    public void onError(final Throwable throwable) {
        //nothing if it is not implicitly overriden.
    }
}
