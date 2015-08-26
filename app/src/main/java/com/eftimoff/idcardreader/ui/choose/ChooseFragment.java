package com.eftimoff.idcardreader.ui.choose;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.eftimoff.idcardreader.R;
import com.eftimoff.idcardreader.components.country.CountryService;
import com.eftimoff.idcardreader.components.country.DaggerCountryComponent;
import com.eftimoff.idcardreader.models.Country;
import com.eftimoff.idcardreader.ui.common.BaseFragment;
import com.googlecode.tesseract.android.TessBaseAPI;

import java.util.List;

import butterknife.Bind;
import butterknife.BindInt;
import rx.Observable;
import rx.functions.Action1;

public class ChooseFragment extends BaseFragment {

    ///////////////////////////////////
    ///          CONSTANTS          ///
    ///////////////////////////////////

    ///////////////////////////////////
    ///            VIEWS            ///
    ///////////////////////////////////

    @Bind(R.id.recycler_view)
    RecyclerView recyclerView;

    ///////////////////////////////////
    ///            FIELDS           ///
    ///////////////////////////////////

    private CountryService countryService;
    private CountryAdapter countryAdapter;

    ///////////////////////////////////
    ///          RESOURCES          ///
    ///////////////////////////////////

    @BindInt(R.integer.fragment_choose_column_number)
    int columnCount;

    public static Fragment getInstance() {
        return new ChooseFragment();
    }

    @Override
    protected int layoutResourceId() {
        return R.layout.fragment_choose;
    }

    @Override
    protected void setupComponents() {
        countryService = DaggerCountryComponent.create().provideCountryService();
    }

    @Override
    protected void setupViews() {
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), columnCount));
        countryAdapter = new CountryAdapter();
        countryAdapter.setListener(countryChosenListener);
        recyclerView.setAdapter(countryAdapter);
    }

    @Override
    protected void init() {
        final Observable<List<Country>> observableCountries = countryService.getCountries();
        observableCountries.subscribe(new Action1<List<Country>>() {
            @Override
            public void call(final List<Country> countries) {
                countryAdapter.setCountryList(countries);
            }
        });
        TessBaseAPI baseApi = new TessBaseAPI();

        baseApi.init(Environment.getExternalStorageDirectory().getAbsolutePath() + "/tesseract/", "eng");
        baseApi.setPageSegMode(TessBaseAPI.PageSegMode.PSM_SINGLE_LINE);
        baseApi.setImage(getTextImage("hello", 300, 300));
        Toast.makeText(getActivity(), baseApi.getUTF8Text(), Toast.LENGTH_SHORT).show();
    }

    private final CountryChosenListener countryChosenListener = new CountryChosenListener() {
        @Override
        public void onChoose(final Country country) {
            Toast.makeText(getActivity(), country.toString(), Toast.LENGTH_SHORT).show();
        }
    };

    private Bitmap getTextImage(String text, int width, int height) {
        final Bitmap bmp = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        final Paint paint = new Paint();
        final Canvas canvas = new Canvas(bmp);

        canvas.drawColor(Color.WHITE);

        paint.setColor(Color.BLACK);
        paint.setAntiAlias(true);
        paint.setTextAlign(Paint.Align.CENTER);
        paint.setTextSize(24.0f);
        canvas.drawText(text, width / 2, height / 2, paint);

        return bmp;
    }
}
