package com.eftimoff.idcardreader.ui.choose;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.eftimoff.idcardreader.R;
import com.eftimoff.idcardreader.components.passport.DaggerPassportComponent;
import com.eftimoff.idcardreader.components.passport.PassportModule;
import com.eftimoff.idcardreader.components.passport.service.PassportService;
import com.eftimoff.idcardreader.models.IdCard;
import com.eftimoff.idcardreader.models.Passport;
import com.eftimoff.idcardreader.models.PassportType;
import com.eftimoff.idcardreader.ui.camera.ShowCameraFragment;
import com.eftimoff.idcardreader.ui.common.BaseActivity;

import java.util.List;

import rx.Observable;
import rx.Observer;

public class OcrActivity extends BaseActivity implements ChooseFragment.ChooseFragmentDelegate, ShowCameraFragment.ShowCameraFragmentDelegate {

    private static final String EXTRA_SHOULD_SKIP_CHOOSE = "extra_should_skip_choose";
    private static final String EXTRA_SKIP_CHOOSE_TYPE = "extra_skip_choose_type";
    private static final String EXTRA_ENABLE_LOGGER = "extra_enable_logger";

    public static final String EXTRA_ID_CARD = "idCard";

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
        final boolean shouldChooseSkipStep = getIntent().getBooleanExtra(EXTRA_SHOULD_SKIP_CHOOSE, false);
        if (shouldChooseSkipStep) {
            final PassportModule passportModule = new PassportModule(this);
            final PassportService passportService = DaggerPassportComponent.builder().passportModule(passportModule).build().provideCountryService();
            final Observable<List<Passport>> passportsObservable = passportService.getPassports();
            passportsObservable.subscribe(observer);
            return;
        }
        startFragment(ChooseFragment.getInstance());
    }

    @Override
    public void onChoose(final Passport passport) {
        startFragment(ShowCameraFragment.getInstance(passport));
    }

    @Override
    public void onFinish(final IdCard idCard) {
        final Intent resultData = new Intent();
        resultData.putExtra(EXTRA_ID_CARD, idCard);
        setResult(Activity.RESULT_OK, resultData);
        finish();
    }


    public static Builder buildIntent() {
        return new Builder();
    }

    public static class Builder {

        private PassportType passportType;
        private boolean enableLogger;

        public Builder skipChooseStep(final PassportType passportType) {
            this.passportType = passportType;
            return this;
        }

        public Builder enableLogger(final boolean enableLogger) {
            this.enableLogger = enableLogger;
            return this;
        }

        public Intent build(final Context context) {
            final Intent intent = new Intent(context, OcrActivity.class);
            intent.putExtra(EXTRA_SHOULD_SKIP_CHOOSE, passportType != null);
            intent.putExtra(EXTRA_SKIP_CHOOSE_TYPE, passportType);
            intent.putExtra(EXTRA_ENABLE_LOGGER, enableLogger);
            return intent;
        }
    }

    private final Observer<List<Passport>> observer = new Observer<List<Passport>>() {
        @Override
        public void onCompleted() {

        }

        @Override
        public void onError(final Throwable e) {

        }

        @Override
        public void onNext(final List<Passport> passports) {
            for (final Passport passport : passports) {
                final PassportType passportType = (PassportType) getIntent().getSerializableExtra(EXTRA_SKIP_CHOOSE_TYPE);
                if (passport.getType().ordinal() == passportType.ordinal()) {
                    onChoose(passport);
                    return;
                }
            }

        }
    };
}
