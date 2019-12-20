package com.bidme.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bidme.R;
import com.bidme.activity.aution.GalleryActivity;
import com.bidme.activity.aution.ViewMyAunction;
import com.bidme.databinding.AdapterGalleryImageSliderBinding;
import com.bidme.https.HttpsRequest;
import com.bidme.interfaces.Const;
import com.bidme.interfaces.Helper;
import com.bidme.model.GalleryDTO;
import com.bidme.utils.ProjectUtils;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class GalleryAdapter extends RecyclerView.Adapter<GalleryAdapter.MyViewHolder> {
    private Context mContext;
    private ArrayList<GalleryDTO>galleryDTOArrayList;
    private AdapterGalleryImageSliderBinding binding;
    private LayoutInflater layoutInflater;
    private GalleryActivity galleryActivity;
    HashMap<String, String> params = new HashMap<>();
    private String flag="";
    private ViewMyAunction viewMyAunction=new ViewMyAunction();
    int pos=0;
    private static final String TAG = GalleryActivity.class.getCanonicalName();

    public GalleryAdapter(Context mContext, ArrayList<GalleryDTO> galleryDTOArrayList,String flag,GalleryActivity galleryActivity) {
        this.mContext = mContext;
        this.flag=flag;
       this.mContext=galleryActivity;
       // this.mContext=galleryActivity;

        this.galleryDTOArrayList = galleryDTOArrayList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, final int position) {
        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(parent.getContext());
        }
        AdapterGalleryImageSliderBinding binding = DataBindingUtil.inflate(layoutInflater, R.layout.adapter_gallery_image_slider, parent, false);
        return new GalleryAdapter.MyViewHolder(binding);

    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, final int position) {
      pos =position;
        Glide.with(mContext)
                .load(galleryDTOArrayList.get(pos).getImage())
                .centerCrop()
                .placeholder(R.drawable.noimage)
                 .into(myViewHolder.binding.imageView);





        if(flag.equals("1")){
            myViewHolder.binding.ivRemove.setVisibility(View.GONE);
        }else {
            myViewHolder.binding.ivRemove.setVisibility(View.VISIBLE);
        }

        myViewHolder.binding.ivRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                params.put(Const.Pro_pub_id,galleryDTOArrayList.get(pos).getPro_pub_id());
                params.put(Const.ID,galleryDTOArrayList.get(pos).getId());

                deleteDailog();




            }
        });


    }


    private void deleteDailog() {

        AlertDialog.Builder builder1 = new AlertDialog.Builder(mContext);
        builder1.setMessage("Are you sure you want to cancel this image ? ")
                .setTitle(R.string.dialog_title)
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        removeGallery();
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

        return galleryDTOArrayList.size();

    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        AdapterGalleryImageSliderBinding binding;

        public MyViewHolder(@NonNull   AdapterGalleryImageSliderBinding itemBinding) {
            super(itemBinding.getRoot());
            this.binding=itemBinding;
        }
    }


    private void removeGallery() {

        new HttpsRequest(Const.REMOVE_IMAGE, params, mContext).stringPost(TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {
                ProjectUtils.pauseProgressDialog();
                if (flag) {
                    try {
                        galleryDTOArrayList.remove(pos);
                        notifyDataSetChanged();
                       // ProjectUtils.showToast(mContext,msg);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                } else {
                    //ProjectUtils.showToast(mContext, msg);
                }
            }
        });


    }




}
