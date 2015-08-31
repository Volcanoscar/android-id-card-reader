package com.eftimoff.idcardreader.ui.common;

import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.v7.app.AppCompatActivity;

import butterknife.ButterKnife;

public abstract class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(layoutResourceId());
        ButterKnife.bind(this);
        init();
    }

    /**
     * The layout id for this activity.
     * It will be entered in setContentView.
     */
    @LayoutRes
    protected abstract int layoutResourceId();

    /**
     * The container for all the fragments.
     */
    @IdRes
    protected abstract int containerId();

    /**
     * After all the layout and bind is done.
     */
    protected abstract void init();

    protected void startFragment(final BaseFragment fragment) {
        getSupportFragmentManager().beginTransaction().replace(containerId(), fragment, fragment.getTAG()).commit();
    }
}
