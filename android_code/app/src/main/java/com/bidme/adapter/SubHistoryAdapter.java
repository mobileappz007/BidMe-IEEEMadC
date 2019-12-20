package com.bidme.adapter;


import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.bidme.R;
import com.bidme.databinding.AdapterHistoryBinding;

import com.bidme.model.SubHistoryDTO;
import com.bidme.utils.ProjectUtils;

import java.util.ArrayList;
import java.util.Locale;

public class SubHistoryAdapter extends RecyclerView.Adapter<SubHistoryAdapter.MyViewHolder> {

    private LayoutInflater layoutInflater;
    private Context sContext;
    private AdapterHistoryBinding binding;
    private ArrayList<SubHistoryDTO> objects;
    private ArrayList<SubHistoryDTO> subHistoryDTOArrayList;

    public SubHistoryAdapter(Context sContext, ArrayList<SubHistoryDTO> objects) {
        this.sContext = sContext;
        this.objects = objects;
        this.subHistoryDTOArrayList = new ArrayList<>();
        this.subHistoryDTOArrayList.addAll(objects);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {

        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(parent.getContext());
        }
        binding = DataBindingUtil.inflate(layoutInflater, R.layout.adapter_history, parent, false);
        return new MyViewHolder(binding);

    }


    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int position) {

        myViewHolder.binding.pkgName.setText(objects.get(position).getPackage_name());
        myViewHolder.binding.totalPrice.setText(objects.get(position).getTotal_price()+" "+objects.get(position).getCurrency_code());
        myViewHolder.binding.tax.setText(objects.get(position).getTax());
        myViewHolder.binding.date.setText("From - " + ProjectUtils.changeDateFormate(objects.get(position).getStart_date()) + "  to  " + ProjectUtils.changeDateFormate(objects.get(position).getEnd_date()));
        myViewHolder.binding.discount.setText(objects.get(position).getDiscount());

    }

    @Override
    public int getItemCount() {
        return objects.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        AdapterHistoryBinding binding;

        public MyViewHolder(AdapterHistoryBinding itemBinding) {


            super(itemBinding.getRoot());
            this.binding = itemBinding;
        }
    }

    public void filter(String s) {
        s = s.toLowerCase(Locale.getDefault());
        objects.clear();
        if (s.length() == 0) {
            objects.addAll(subHistoryDTOArrayList);

        } else {
            for (SubHistoryDTO subHistoryDTO : subHistoryDTOArrayList) {
                if (subHistoryDTO.getPackage_name().toLowerCase(Locale.getDefault()).contains(s)) {
                    objects.add(subHistoryDTO);
                }
            }
        }
        notifyDataSetChanged();
    }


}
