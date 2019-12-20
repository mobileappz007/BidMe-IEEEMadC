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
import com.bidme.R;
import com.bidme.activity.aution.AddAuctionNew;
import com.bidme.databinding
        .AdapterAddImageBinding;

import java.io.File;
import java.util.ArrayList;

public class ImageAdapterNew extends RecyclerView.Adapter<ImageAdapterNew.MyViewHolder> {
    private AdapterAddImageBinding binding;

    private AddAuctionNew mContext;
    private ArrayList<File> imageDTOArrayList;
    private LayoutInflater layoutInflater;
    int pos = -1;

    public ImageAdapterNew(AddAuctionNew mContext, ArrayList<File> imageDTOArrayList) {
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

        return new ImageAdapterNew.MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, final int i) {

        Glide.with(mContext)
                .load(imageDTOArrayList.get(i))
                .centerCrop()
                .placeholder(R.drawable.noimage)
                .into(myViewHolder.binding.addImages);

        myViewHolder.binding.cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pos = i;
                deleteDailog(pos);



            }
        });

    }


    private void deleteDailog(final int pos) {

        AlertDialog.Builder builder1 = new AlertDialog.Builder(mContext);
        builder1.setMessage("Are you sure you want to cancel this image ? ")
                .setTitle(R.string.dialog_title)
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        imageDTOArrayList.remove(pos);
                        mContext.delete();

                       // mContext.files.remove(pos);
                        mContext.imageRemove(pos);
                        notifyDataSetChanged();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //  Action for 'NO' Button
                        dialog.cancel();

                    }
                });

        AlertDialog alert = builder1.create();

        alert.setTitle("Delete auction.");

        alert.setIcon(R.drawable.ic_deleteauction);
        alert.show();
    }


    @Override
    public int getItemCount() {
        return imageDTOArrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private AdapterAddImageBinding binding;


        public MyViewHolder(@NonNull AdapterAddImageBinding itemBinding) {
            super(itemBinding.getRoot());
            this.binding = itemBinding;
        }
    }
}
