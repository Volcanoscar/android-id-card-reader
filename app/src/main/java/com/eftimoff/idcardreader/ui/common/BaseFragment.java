package com.eftimoff.idcardreader.ui.common;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public abstract class BaseFragment extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
        final View view = inflater.inflate(layoutResourceId(), container, false);
        setupViews(view);
        return view;
    }

    @Override
    public void onViewCreated(final View view, @Nullable final Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupComponents();
        init();
    }

    public String getTAG() {
        return this.getClass().getSimpleName();
    }

    @LayoutRes
    protected abstract int layoutResourceId();

    protected abstract void setupComponents();

    protected abstract void setupViews(final View view);

    protected abstract void init();


}
