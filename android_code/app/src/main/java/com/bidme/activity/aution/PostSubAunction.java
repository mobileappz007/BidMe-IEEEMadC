package com.bidme.activity.aution;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ArrayAdapter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.bidme.R;
import com.bidme.adapter.SubCategoryAdapter;
import com.bidme.databinding.ActivityPostSubAunctionBinding;
import com.bidme.https.HttpsRequest;
import com.bidme.interfaces.Const;
import com.bidme.interfaces.Helper;
import com.bidme.model.SubBrandsDTO;
import com.bidme.model.SubCategoryDTO;
import com.bidme.model.SubModelDTO;
import com.bidme.utils.ProjectUtils;

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class PostSubAunction extends AppCompatActivity {

    private ActivityPostSubAunctionBinding binding;
    private Context mContext;
    private String TAG = PostSubAunction.class.getCanonicalName();
    private ArrayList<SubCategoryDTO> subCategoryDTOArrayList;
    private ArrayList<SubBrandsDTO> subBrandsDTOArrayList;
    private ArrayList<SubModelDTO> subModelDTOArrayList;
    private ArrayAdapter<SubCategoryDTO> subCategoryDTOArrayAdapter;
    private ArrayAdapter<SubBrandsDTO> subBrandsDTOArrayAdapter;
    private ArrayAdapter<SubModelDTO> subModelDTOArrayAdapter;
    private boolean isSpinnerInitial = false;
    private SubCategoryAdapter subCategoryAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private String catid = "";
    private String subCatid = "";
    private String brandId = "";
    private String catgoryName;
    private String modelid = "";
    private SubCategoryDTO subCategoryDTO;
    private HashMap<String, String> param = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = PostSubAunction.this;

        binding = DataBindingUtil.setContentView(PostSubAunction.this, R.layout.activity_post_sub_aunction);


        catid = getIntent().getStringExtra(Const.GET_CAT_ID);
        catgoryName = getIntent().getStringExtra(Const.CAT_TITLE);
        binding.tvCategoryName.setText(catgoryName);

        param.put(Const.GET_CAT_ID, catid);

        binding.icBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        setSubcategory();


    }


    private void setSubcategory() {
        new HttpsRequest(Const.GET_All_SUB_CATEGORY,param, mContext).stringPost(TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {
                ProjectUtils.pauseProgressDialog();
                if (flag) {
                    try {
                        subCategoryDTOArrayList = new ArrayList<>();
                        Type getpetDTO = new TypeToken<List<SubCategoryDTO>>() {

                        }.getType();
                        subCategoryDTOArrayList = (ArrayList<SubCategoryDTO>) new Gson().fromJson(response.getJSONArray("data").toString(), getpetDTO);
                        layoutManager=new LinearLayoutManager(mContext,LinearLayoutManager.VERTICAL,false);
                        binding.rvSubCategory.setLayoutManager(layoutManager);
                        subCategoryAdapter = new SubCategoryAdapter(mContext,subCategoryDTOArrayList);
                        binding.rvSubCategory.setAdapter(subCategoryAdapter);

                    } catch (Exception e) {
                        e.printStackTrace();

                    }


                } else {
                    ProjectUtils.showToast(mContext, msg);
                }
            }
        });


    }



 /*   private HashMap<String, String> getCatid() {
        HashMap<String, String> params = new HashMap<>();
        return params;
    }*/
}


