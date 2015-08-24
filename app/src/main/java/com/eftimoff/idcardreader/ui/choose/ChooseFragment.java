package com.eftimoff.idcardreader.ui.choose;

import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.eftimoff.idcardreader.R;
import com.eftimoff.idcardreader.components.country.CountryService;
import com.eftimoff.idcardreader.components.country.DaggerCountryComponent;
import com.eftimoff.idcardreader.models.Country;
import com.eftimoff.idcardreader.ui.common.BaseFragment;

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
    }
}
