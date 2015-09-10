package com.eftimoff.idcardreader.ui.choose;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.eftimoff.idcardreader.R;
import com.eftimoff.idcardreader.models.Passport;

import java.util.ArrayList;
import java.util.List;

public class PassportAdapter extends RecyclerView.Adapter<PassportAdapter.ViewHolder> {

    private List<Passport> passportList = new ArrayList<Passport>();
    private PassportChosenListener listener;

    @Override
    public PassportAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.ocr_library_item_list_country, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Passport passport = passportList.get(position);
        holder.name.setText(passport.getName());
        Glide.with(holder.flag.getContext()).load(passport.getFlagImage()).fitCenter().centerCrop().into(holder.flag);
    }

    @Override
    public int getItemCount() {
        return passportList.size();
    }

    public void setPassportList(final List<Passport> passportList) {
        this.passportList = passportList;
    }

    public void setListener(final PassportChosenListener listener) {
        this.listener = listener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        CardView cardView;
        ImageView flag;
        TextView name;

        public ViewHolder(View view) {
            super(view);
            cardView = (CardView) view.findViewById(R.id.card_view);
            flag = (ImageView) view.findViewById(R.id.flag);
            name = (TextView) view.findViewById(R.id.name);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(final View v) {
            final int position = getAdapterPosition();
            getInformationAndSend(position);
        }
    }

    private void getInformationAndSend(final int position) {
        if (listener != null) {
            final Passport passport = passportList.get(position);
            listener.onChoose(passport);
        }
    }
}