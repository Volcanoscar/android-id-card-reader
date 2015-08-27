package com.eftimoff.idcardreader.ui.choose;

import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.eftimoff.idcardreader.R;
import com.eftimoff.idcardreader.components.passport.PassportService;
import com.eftimoff.idcardreader.components.passport.DaggerPassportComponent;
import com.eftimoff.idcardreader.components.tesseract.DaggerTessaractComponent;
import com.eftimoff.idcardreader.components.tesseract.Tesseract;
import com.eftimoff.idcardreader.models.Passport;
import com.eftimoff.idcardreader.ui.common.BaseFragment;

import java.util.List;

import butterknife.Bind;
import butterknife.BindInt;
import rx.Observable;
import rx.functions.Action1;

public class ChooseFragment extends BaseFragment {

    public interface ChooseFragmentDelegate {

    }

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

    private PassportService passportService;
    private PassportAdapter passportAdapter;
    private Tesseract tesseract;

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
        passportService = DaggerPassportComponent.create().provideCountryService();
        tesseract = DaggerTessaractComponent.create().provideTesseract();
    }

    @Override
    protected void setupViews() {
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), columnCount));
        passportAdapter = new PassportAdapter();
        passportAdapter.setListener(passportChosenListener);
        recyclerView.setAdapter(passportAdapter);
    }

    @Override
    protected void init() {
        final Observable<List<Passport>> observableCountries = passportService.getCountries();
        observableCountries.subscribe(new Action1<List<Passport>>() {
            @Override
            public void call(final List<Passport> countries) {
                passportAdapter.setPassportList(countries);
            }
        });
    }

    private final PassportChosenListener passportChosenListener = new PassportChosenListener() {
        @Override
        public void onChoose(final Passport passport) {
            Toast.makeText(getActivity(), passport.toString(), Toast.LENGTH_SHORT).show();
        }
    };
}
