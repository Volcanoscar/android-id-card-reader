package com.eftimoff.idcardreader.ui.choose;

import android.support.v4.app.Fragment;

import com.eftimoff.idcardreader.R;
import com.eftimoff.idcardreader.ui.common.BaseFragment;

public class ChooseFragment extends BaseFragment {

    ///////////////////////////////////
    ///          CONSTANTS          ///
    ///////////////////////////////////

    ///////////////////////////////////
    ///            VIEWS            ///
    ///////////////////////////////////

    ///////////////////////////////////
    ///            FIELDS           ///
    ///////////////////////////////////


    public static Fragment getInstance() {
        return new ChooseFragment();
    }

    @Override
    protected int layoutResourceId() {
        return R.layout.fragment_choose;
    }

    @Override
    protected void setupComponents() {

    }

    @Override
    protected void setupViews() {

    }
}
