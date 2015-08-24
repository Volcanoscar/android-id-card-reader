package com.eftimoff.idcardreader.ui.choose;

import android.support.v4.app.Fragment;
import android.widget.Toast;

import com.eftimoff.idcardreader.R;
import com.eftimoff.idcardreader.components.country.CountryService;
import com.eftimoff.idcardreader.components.country.DaggerCountryComponent;
import com.eftimoff.idcardreader.models.Country;
import com.eftimoff.idcardreader.ui.common.BaseFragment;

import java.util.List;

import rx.Observable;
import rx.functions.Action1;

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

    private CountryService countryService;

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

    }

    @Override
    protected void init() {
        final Observable<List<Country>> observableCountries = countryService.getCountries();
        observableCountries.subscribe(new Action1<List<Country>>() {
            @Override
            public void call(final List<Country> countries) {
                Toast.makeText(getActivity(), "Size : " + countries.size(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
