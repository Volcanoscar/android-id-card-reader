package com.eftimoff.idcardreader.ui.choose;

import com.eftimoff.idcardreader.R;
import com.eftimoff.idcardreader.ui.common.BaseActivity;

public class ChooseActivity extends BaseActivity {

    @Override
    protected int layoutResourceId() {
        return R.layout.activity_choose;
    }

    @Override
    protected int containerId() {
        return R.id.container;
    }

    @Override
    protected void init() {
        startFragment(ChooseFragment.getInstance());
    }

}
