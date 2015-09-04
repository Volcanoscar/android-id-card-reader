package com.eftimoff.idcardreader.ui.choose;

import android.app.Activity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.eftimoff.idcardreader.R;
import com.eftimoff.idcardreader.components.passport.DaggerPassportComponent;
import com.eftimoff.idcardreader.components.passport.PassportModule;
import com.eftimoff.idcardreader.components.passport.service.PassportService;
import com.eftimoff.idcardreader.models.Passport;
import com.eftimoff.idcardreader.ui.common.BaseFragment;

import java.io.Serializable;
import java.util.List;

import rx.Observable;
import rx.functions.Action1;

public class ChooseFragment extends BaseFragment {

    public interface ChooseFragmentDelegate extends Serializable {
        void onChoose(final Passport passport);
    }

    ///////////////////////////////////
    ///          CONSTANTS          ///
    ///////////////////////////////////

    ///////////////////////////////////
    ///            VIEWS            ///
    ///////////////////////////////////

    RecyclerView recyclerView;

    ///////////////////////////////////
    ///            FIELDS           ///
    ///////////////////////////////////

    private PassportService passportService;
    private PassportAdapter passportAdapter;
    private ChooseFragmentDelegate delegate;

    ///////////////////////////////////
    ///          RESOURCES          ///
    ///////////////////////////////////

    int columnCount;


    public static ChooseFragment getInstance() {
        return new ChooseFragment();
    }

    @Override
    public void onAttach(final Activity activity) {
        super.onAttach(activity);
        try {
            delegate = (ChooseFragmentDelegate) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement ChooseFragmentDelegate");
        }
    }

    @Override
    protected int layoutResourceId() {
        return R.layout.fragment_choose;
    }

    @Override
    protected void setupComponents() {
        passportService = DaggerPassportComponent.builder().passportModule(new PassportModule(getActivity())).build().provideCountryService();
    }

    @Override
    protected void setupViews(final View view) {
        columnCount = getResources().getInteger(R.integer.fragment_choose_column_number);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), columnCount));
        passportAdapter = new PassportAdapter();
        passportAdapter.setListener(passportChosenListener);
        recyclerView.setAdapter(passportAdapter);
    }

    @Override
    protected void init() {
        final Observable<List<Passport>> observableCountries = passportService.getPassports();
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
            delegate.onChoose(passport);
        }
    };
}
