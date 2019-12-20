package com.bidme.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.bidme.R;
import com.bidme.databinding.AdapterSupprtBinding;
import com.bidme.model.SupportDTO;
import com.bidme.utils.ProjectUtils;

import java.util.ArrayList;
import java.util.Locale;

public class SupportAdapter extends RecyclerView.Adapter<SupportAdapter.MyViewHolder> {
    private LayoutInflater layoutInflater;
    private AdapterSupprtBinding binding;
    private Context mContext;
    private ArrayList<SupportDTO> objects;
    private ArrayList<SupportDTO>supportDTOArrayList;


    public SupportAdapter(Context mContext, ArrayList<SupportDTO> objects) {
        this.mContext = mContext;
        this.objects = objects;
        this.supportDTOArrayList=new ArrayList<>();
        this.supportDTOArrayList.addAll(objects);
    }

    @NonNull
    @Override
    public SupportAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {

        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(parent.getContext());
        }
        binding = DataBindingUtil.inflate(layoutInflater, R.layout.adapter_supprt, parent, false);
        return new SupportAdapter.MyViewHolder(binding);

    }

    @Override
    public void onBindViewHolder(@NonNull SupportAdapter.MyViewHolder viewHolder, int position) {

        viewHolder.binding.title.setText(ProjectUtils.capWordFirstLetter(objects.get(position).getTitle()));
        viewHolder.binding.tvDescription.setText(objects.get(position).getDescription());
        viewHolder.binding.tvTime.setText(ProjectUtils.changeDateFormate(objects.get(position).getCreated_at()));
        if(objects.get(position).getStatus().equals("0")){
            viewHolder.binding.tvStatus.setText("Pending");

        }else {
            viewHolder.binding.tvStatus.setText("Resolved");
        }





    }

    @Override
    public int getItemCount() {
        return objects.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        AdapterSupprtBinding binding;


        public MyViewHolder(@NonNull AdapterSupprtBinding itemBinding) {
            super(itemBinding.getRoot());
            this.binding = itemBinding;
        }
    }

    public void filter(String s){
        s=s.toLowerCase(Locale.getDefault());
        objects.clear();
        if(s.length()==0){
            objects.addAll(supportDTOArrayList);

        }else {
            for (SupportDTO supportDTO:supportDTOArrayList){
                if (supportDTO.getTitle().toLowerCase(Locale.getDefault()).contains(s)){
                    objects.add(supportDTO);
                }


            }
        }
        notifyDataSetChanged();
    }
}
