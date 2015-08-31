package com.eftimoff.idcardreader.ui.choose;

import com.eftimoff.idcardreader.R;
import com.eftimoff.idcardreader.models.Passport;
import com.eftimoff.idcardreader.ui.camera.ShowCameraFragment;
import com.eftimoff.idcardreader.ui.common.BaseActivity;

public class ChooseActivity extends BaseActivity implements ChooseFragment.ChooseFragmentDelegate {

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

    @Override
    public void onChoose(final Passport passport) {
        startFragment(ShowCameraFragment.getInstance(passport));
    }
}
