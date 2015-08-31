package com.eftimoff.idcardreader.ui.camera;

import android.graphics.Rect;
import android.hardware.Camera;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.eftimoff.idcardreader.R;
import com.eftimoff.idcardreader.components.tesseract.DaggerTessaractComponent;
import com.eftimoff.idcardreader.components.tesseract.TessaractModule;
import com.eftimoff.idcardreader.components.tesseract.Tesseract;
import com.eftimoff.idcardreader.components.tesseract.TesseractResult;
import com.eftimoff.idcardreader.components.tesseract.listeners.DownloadListener;
import com.eftimoff.idcardreader.models.Passport;
import com.eftimoff.idcardreader.ui.common.BaseFragment;

import butterknife.Bind;
import rx.Observable;
import rx.Observer;


public class ShowCameraFragment extends BaseFragment {

    ///////////////////////////////////
    ///          CONSTANTS          ///
    ///////////////////////////////////

    private static final String KEY_SETTINGS = "key_settings";

    ///////////////////////////////////
    ///            VIEWS            ///
    ///////////////////////////////////

    @Bind(R.id.flag)
    ImageView flag;
    @Bind(R.id.cameraView)
    ShowCameraView cameraView;
    @Bind(R.id.progressBar)
    ProgressBar progressBar;
    @Bind(R.id.areaView)
    AreaView areaView;

    ///////////////////////////////////
    ///            FIELDS           ///
    ///////////////////////////////////

    private Tesseract tesseract;
    private ShowCameraSettings cameraSettings;

    public static ShowCameraFragment getInstance(final ShowCameraSettings cameraSettings) {
        final ShowCameraFragment showCameraFragment = new ShowCameraFragment();
        final Bundle bundle = new Bundle();
        bundle.putSerializable(KEY_SETTINGS, cameraSettings);
        showCameraFragment.setArguments(bundle);
        return showCameraFragment;
    }

    public static ShowCameraFragment getInstance(final Passport passport) {
        final ShowCameraSettings cameraSettings = ShowCameraSettings.newShowCameraSettings().passport(passport).build();
        final ShowCameraFragment showCameraFragment = new ShowCameraFragment();
        final Bundle bundle = new Bundle();
        bundle.putSerializable(KEY_SETTINGS, cameraSettings);
        showCameraFragment.setArguments(bundle);
        return showCameraFragment;
    }

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        cameraSettings = (ShowCameraSettings) getArguments().getSerializable(KEY_SETTINGS);
    }

    @Override
    protected int layoutResourceId() {
        return R.layout.fragment_camera;
    }

    @Override
    protected void setupComponents() {
        tesseract = DaggerTessaractComponent.builder().tessaractModule(new TessaractModule()).build().provideTesseract();
    }

    @Override
    protected void setupViews() {
        final Passport passport = cameraSettings.getPassport();
        Glide.with(getActivity()).load(passport.getFlagImage()).into(flag);
        areaView.setArea(passport.getArea());
    }

    @Override
    protected void init() {
        final Passport passport = cameraSettings.getPassport();
        tesseract.init(passport.getLanguage(), downloadListener);
    }

    @Override
    public void onDestroyView() {
        cameraView.stop();
        super.onDestroyView();
    }

    private final ShowCameraView.CAMViewListener camViewListener = new ShowCameraView.CAMViewListener() {
        @Override
        public void onCameraReady(Camera camera) {
            Toast.makeText(getActivity(), "Camera ready!", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onCameraStopped() {

        }

        @Override
        public void onCameraError(int i, Camera camera) {
            Toast.makeText(getActivity(), "Camera error!", Toast.LENGTH_LONG).show();
        }

        @Override
        public void onCameraOpenError(Throwable err) {
            Toast.makeText(getActivity(), "Camera open error!", Toast.LENGTH_LONG).show();
            err.printStackTrace();
        }


        @Override
        public boolean onPreviewData(final byte[] bytes, final int i, final Camera.Size size) {
            showLoadingDialog();
            final Observable<TesseractResult> fromBitmapObservable = tesseract.getFromBitmap(bytes, size.width, size.height, new Rect(0, 0, size.width, size.height));
            fromBitmapObservable.subscribe(new Observer<TesseractResult>() {
                @Override
                public void onCompleted() {
                }

                @Override
                public void onError(final Throwable e) {
                }

                @Override
                public void onNext(final TesseractResult tesseractResult) {
                    hideLoadingDialog();
                    cameraView.enablePreviewGrabbing();
//                    Toast.makeText(getActivity(), tesseractResult.toString(), Toast.LENGTH_SHORT).show();
                }
            });
            cameraView.disablePreviewGrabbing();
            return true;
        }
    };


    private void showLoadingDialog() {
        progressBar.setVisibility(View.VISIBLE);
    }

    private void hideLoadingDialog() {
        progressBar.setVisibility(View.GONE);
    }

    private final DownloadListener downloadListener = new DownloadListener() {
        @Override
        public void onStart() {
            showLoadingDialog();
        }

        @Override
        public void onDone() {
            hideLoadingDialog();
            cameraView.start();
            cameraView.setCamViewListener(camViewListener);
        }

        @Override
        public void onError(final Throwable throwable) {

        }
    };

}