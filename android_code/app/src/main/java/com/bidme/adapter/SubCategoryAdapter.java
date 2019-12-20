package com.bidme.adapter;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bidme.activity.aution.AddAuction;
import com.bidme.databinding.AdapterSubCategoryBinding;
import com.bidme.interfaces.Const;
import com.bidme.model.SubCategoryDTO;

import java.util.ArrayList;

import static com.bidme.R.layout.adapter_sub_category;

public class SubCategoryAdapter extends RecyclerView.Adapter<SubCategoryAdapter.ViewHolder> {
    private Context mContext;
    private ArrayList<SubCategoryDTO> subCategoryDTOArrayList;
    private LayoutInflater layoutInflater;
    private AdapterSubCategoryBinding binding;


    public SubCategoryAdapter(Context mContext, ArrayList<SubCategoryDTO> subCategoryDTOArrayList) {
        this.mContext = mContext;
        this.subCategoryDTOArrayList = subCategoryDTOArrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {


        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(parent.getContext());
        }
        binding = DataBindingUtil.inflate(layoutInflater, adapter_sub_category, parent, false);

        return new SubCategoryAdapter.ViewHolder(binding);

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int position) {
        viewHolder.binding.tvtitle.setText(subCategoryDTOArrayList.get(position).getSub_cat_title());
        viewHolder.binding.relSubCat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(mContext, AddAuction.class);
                in.putExtra(Const.GET_SUB_CAT_ID,subCategoryDTOArrayList.get(position).getSub_cat_id());
                in.putExtra(Const.GET_CAT_ID,subCategoryDTOArrayList.get(position).getCat_id());
                mContext.startActivity(in);
            }
        });


    }

    @Override
    public int getItemCount() {
        return subCategoryDTOArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        AdapterSubCategoryBinding binding;

        public ViewHolder(@NonNull AdapterSubCategoryBinding itemBinding) {
            super(itemBinding.getRoot());
            this.binding = itemBinding;
        }
    }
}
