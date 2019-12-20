package com.bidme.adapter;

import android.content.DialogInterface;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.darsh.multipleimageselect.models.Image;
import com.bidme.R;
import com.bidme.activity.aution.AddAuction;
import com.bidme.databinding.AdapterAddImageBinding;

import java.util.ArrayList;

public class AddImageAdapter extends RecyclerView.Adapter<AddImageAdapter.MyViewHolder> {
    private AddAuction mContext;
    private ArrayList<Image> imageDTOArrayList;
    private LayoutInflater layoutInflater;
    private AdapterAddImageBinding binding;
    int pos = -1;


    public AddImageAdapter(AddAuction mContext, ArrayList<Image> imageDTOArrayList) {
        this.mContext = mContext;
        this.imageDTOArrayList = imageDTOArrayList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(parent.getContext());
        }
        binding = DataBindingUtil.inflate(layoutInflater, R.layout.adapter_add_image, parent, false);

        return new MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, final int position) {

        Glide.with(mContext)
                .load(imageDTOArrayList.get(position).path)
                .centerCrop()
                .placeholder(R.drawable.noimage)
                .into(myViewHolder.binding.addImages);

        binding.cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               pos = position;
                deleteDailog(position);
              //mContext.imageRemove(position);



            }
        });

    }

    @Override
    public int getItemCount() {
        return imageDTOArrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        AdapterAddImageBinding binding;

        public MyViewHolder(@NonNull AdapterAddImageBinding itemBinding) {
            super(itemBinding.getRoot());
            this.binding = itemBinding;
        }
    }


    private void deleteDailog(final int pos) {

        AlertDialog.Builder builder1 = new AlertDialog.Builder(mContext);
        builder1.setMessage("Do you want to delete this Aunction?")
                .setTitle(R.string.dialog_title)
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        mContext.imageRemove(pos);


                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //  Action for 'NO' Button


                    }
                });

        AlertDialog alert = builder1.create();

        alert.setTitle("Delete auction.");

        alert.setIcon(R.drawable.ic_deleteauction);
        alert.show();
    }

}
