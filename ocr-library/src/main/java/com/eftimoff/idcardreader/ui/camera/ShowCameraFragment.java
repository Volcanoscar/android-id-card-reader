package com.eftimoff.idcardreader.ui.camera;

import android.app.Activity;
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
import com.eftimoff.idcardreader.components.tesseract.listeners.ProgressListener;
import com.eftimoff.idcardreader.components.tesseract.models.TesseractResult;
import com.eftimoff.idcardreader.models.IdAreaField;
import com.eftimoff.idcardreader.models.IdCard;
import com.eftimoff.idcardreader.models.Passport;
import com.eftimoff.idcardreader.ui.common.BaseFragment;

import rx.Observable;
import rx.Observer;


public class ShowCameraFragment extends BaseFragment {

    public interface ShowCameraFragmentDelegate {
        void onFinish(final IdCard idCard);
    }

    ///////////////////////////////////
    ///          CONSTANTS          ///
    ///////////////////////////////////

    private static final String KEY_SETTINGS = "key_settings";

    ///////////////////////////////////
    ///            VIEWS            ///
    ///////////////////////////////////

    ImageView flag;
    ShowCameraView cameraView;
    ProgressBar progressBar;
    AreaView areaView;

    ///////////////////////////////////
    ///            FIELDS           ///
    ///////////////////////////////////

    private Tesseract tesseract;
    private ShowCameraSettings cameraSettings;
    private IdAreaField idAreaField;

    private ShowCameraFragmentDelegate delegate;

    public static ShowCameraFragment getInstance(final ShowCameraSettings cameraSettings) {
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
    public void onAttach(final Activity activity) {
        super.onAttach(activity);
        try {
            delegate = (ShowCameraFragmentDelegate) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement ChooseFragmentDelegate");
        }
    }

    @Override
    protected int layoutResourceId() {
        return R.layout.fragment_camera;
    }

    @Override
    protected void setupComponents() {
        final boolean enableLogging = cameraSettings.isEnableLogging();
        tesseract = DaggerTessaractComponent.builder().tessaractModule(new TessaractModule(enableLogging)).build().provideTesseract();
    }

    @Override
    protected void setupViews(final View view) {
        flag = (ImageView) view.findViewById(R.id.flag);
        cameraView = (ShowCameraView) view.findViewById(R.id.cameraView);
        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        areaView = (AreaView) view.findViewById(R.id.areaView);
        final Passport passport = cameraSettings.getPassport();
        Glide.with(getActivity()).load(passport.getFlagImage()).into(flag);
        areaView.setIdArea(passport.getIdArea());
        areaView.setListener(areaViewListener);
        areaView.setShowTempResults(cameraSettings.isShowTempResults());
    }

    @Override
    protected void init() {
        final Passport passport = cameraSettings.getPassport();
        tesseract.init(passport.getLanguage(), progressListener);
    }

    @Override
    public void onDestroyView() {
        cameraView.stop();
        super.onDestroyView();
    }

    /**
     * Create rect from the preview frame.
     * All the IdAreaField methods are percentage so it is not a problem.
     * First construct the id card on the preview frame.
     * Then we construct this IdAreaField and return the result
     * as Rect to be used for the Tesseract.
     */
    private Rect createRect(final Camera.Size size, final IdAreaField idAreaField) {
        final int offsetWidth = (int) (size.width * 0.2);
        final int idCardWidth = (int) (size.width * 0.6);
        final int idCardHeight = (int) (idCardWidth / 1.58);
        final int topIdCardHeight = idCardWidth / 2 - idCardHeight / 2;
        final int bottomIdCardHeight = idCardWidth / 2 + idCardHeight / 2;
        final Rect parentRect = new Rect(offsetWidth, topIdCardHeight, size.width - offsetWidth, bottomIdCardHeight);
        final int left = parentRect.left + idAreaField.getPercentageFromParentLeft() * parentRect.width() / 100;
        final int top = parentRect.top + idAreaField.getPercentageFromParentTop() * parentRect.height() / 100;
        final int right = left + idAreaField.getPercentageWidth() * parentRect.width() / 100;
        final int bottom = top + idAreaField.getPercentageHeight() * parentRect.height() / 100;
        return new Rect(left, top, right, bottom);
    }


    private void showLoadingDialog() {
        progressBar.setVisibility(View.VISIBLE);
    }

    private void hideLoadingDialog() {
        progressBar.setVisibility(View.GONE);
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
            if (idAreaField == null) {
                return true;
            }

            final Observable<TesseractResult> fromBitmapObservable = tesseract.getFromBitmap(bytes, size.width, size.height, createRect(size, idAreaField));
            fromBitmapObservable.subscribe(tesseractResultObserver);
            cameraView.disablePreviewGrabbing();
            return true;
        }
    };


    private final ProgressListener progressListener = new ProgressListener() {
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

    private Observer<TesseractResult> tesseractResultObserver = new Observer<TesseractResult>() {

        @Override
        public void onCompleted() {
        }

        @Override
        public void onError(final Throwable e) {
            Toast.makeText(getActivity(), "Error : " + e.getMessage(), Toast.LENGTH_SHORT).show();
            cameraView.enablePreviewGrabbing();
        }

        @Override
        public void onNext(final TesseractResult tesseractResult) {
            areaView.tempResult(tesseractResult);
            if (tesseractResult.getMeanConfidence() > cameraSettings.getPercentageToCapture()) {
                final String text = tesseractResult.getText();
                cameraSettings.getPassport().getType().getIdCardConstructor().setText(text, idAreaField);
                idAreaField = areaView.increment(text);
            }
            cameraView.enablePreviewGrabbing();
        }
    };

    private AreaView.AreaViewListener areaViewListener = new AreaView.AreaViewListener() {
        @Override
        public void onStart(final IdAreaField areaField) {
            idAreaField = areaField;
        }

        @Override
        public void onFinish() {
            final IdCard idCard = cameraSettings.getPassport().getType().getIdCardConstructor().construct();
            delegate.onFinish(idCard);
        }
    };
}