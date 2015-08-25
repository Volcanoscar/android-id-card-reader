package com.eftimoff.idcardreader.ui.choose;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.eftimoff.idcardreader.R;
import com.eftimoff.idcardreader.models.Country;
import com.eftimoff.idcardreader.models.PassportEnum;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class CountryAdapter extends RecyclerView.Adapter<CountryAdapter.ViewHolder> {

    private List<Country> countryList = new ArrayList<Country>();
    private CountryChosenListener listener;

    @Override
    public CountryAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_country, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Country country = countryList.get(position);
        holder.name.setText(country.getName());
        Glide.with(holder.flag.getContext()).load(country.getFlagImage()).fitCenter().centerCrop().into(holder.flag);
        changeButtonsVisibility(holder, country.getPassportEnums());
    }

    private void changeButtonsVisibility(final ViewHolder holder, final EnumSet<PassportEnum> passportEnums) {
        if (!passportEnums.contains(PassportEnum.PASSPORT)) {
            holder.passport.setVisibility(View.GONE);
        }

        if (!passportEnums.contains(PassportEnum.ID_CARD_BACK)) {
            holder.idCardBack.setVisibility(View.GONE);
        }

        if (!passportEnums.contains(PassportEnum.ID_CARD_FRONT)) {
            holder.idCardFront.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return countryList.size();
    }

    public void setCountryList(final List<Country> countryList) {
        this.countryList = countryList;
    }

    public void setListener(final CountryChosenListener listener) {
        this.listener = listener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @Bind(R.id.card_view)
        CardView cardView;
        @Bind(R.id.flag)
        ImageView flag;
        @Bind(R.id.name)
        TextView name;
        @Bind(R.id.buttonsContainer)
        LinearLayout buttonsContainer;
        @Bind(R.id.passport)
        TextView passport;
        @Bind(R.id.id_card_back)
        TextView idCardBack;
        @Bind(R.id.id_card_front)
        TextView idCardFront;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            view.setOnClickListener(this);
            passport.setOnClickListener(this);
            idCardBack.setOnClickListener(this);
            idCardFront.setOnClickListener(this);
        }

        @Override
        public void onClick(final View v) {
            if (v.getId() == passport.getId()) {
                final int position = getAdapterPosition();
                getInformationAndSend(position, PassportEnum.PASSPORT);
                return;
            }
            if (v.getId() == idCardBack.getId()) {
                final int position = getAdapterPosition();
                getInformationAndSend(position, PassportEnum.ID_CARD_BACK);
                return;
            }
            if (v.getId() == idCardFront.getId()) {
                final int position = getAdapterPosition();
                getInformationAndSend(position, PassportEnum.ID_CARD_FRONT);
                return;
            }
            buttonsContainer.setVisibility(buttonsContainer.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
        }
    }

    private void getInformationAndSend(final int position, final PassportEnum passportEnum) {
        if (listener != null) {
            final Country country = countryList.get(position);
            country.setChosenPassportEnum(passportEnum);
            listener.onChoose(country);
        }
    }
}