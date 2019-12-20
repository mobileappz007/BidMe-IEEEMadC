package com.bidme.activity;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.bidme.interfaces.Const;
import com.bidme.interfaces.Helper;
import com.bidme.R;
import com.bidme.activity.categories.CategoryList;
import com.bidme.adapter.FilterAdapter;
import com.bidme.databinding.ActivityFilterBinding;
import com.bidme.https.HttpsRequest;
import com.bidme.model.CategoryDTO;
import com.bidme.model.UserDTO;
import com.bidme.preferences.SharedPrefrence;
import com.bidme.utils.ProjectUtils;
import com.google.gson.Gson;
import com.xw.repo.BubbleSeekBar;

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class FilterActivity extends AppCompatActivity implements SeekBar.OnSeekBarChangeListener {
    private Context mContext;
    private ArrayList<CategoryDTO> categoryDTOArrayList;
    private ActivityFilterBinding binding;
    private FilterAdapter filterAdapter;
    private SharedPrefrence sharedPrefrence;
    private UserDTO userDTO;
    private CategoryDTO categoryDTO;
    String MaxPrice;
    String MinPrice;
    String id="";
    String distance = "0";
    int PLACE_PICKER_REQUEST = 6;
    String catName;
    String progress;
    TextView mProgressText;
    TextView mTrackingText;
    private double lattitude;
    private double longitude;
    float p = 01;

    private String mTag;
    private HashMap<String, String> params = new HashMap<>();
    RecyclerView.LayoutManager layoutManager;
    private String TAG = CategoryList.class.getCanonicalName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_filter);
        sharedPrefrence = SharedPrefrence.getInstance(mContext);


        mContext = FilterActivity.this;
        id = getIntent().getStringExtra(Const.GET_CAT_ID);

        binding.bsbFilter.setOnProgressChangedListener(new BubbleSeekBar.OnProgressChangedListener() {
            @Override
            public void onProgressChanged(int progress, float progressFloat) {
                distance = String.valueOf(progress);

            }

            @Override
            public void getProgressOnActionUp(int progress, float progressFloat) {

            }

            @Override
            public void getProgressOnFinally(int progress, float progressFloat) {

            }
        });

        binding.Applyfilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setparamter();


            }
        });
        binding.clearfilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.MaxPrice.getText().clear();
                binding.MinPrice.getText().clear();
                distance = "0";
                binding.bsbFilter.setProgress(0f);
                binding.recycleview1.setAdapter(filterAdapter);


            }
        });
        binding.ivBackFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


    }



    private void setparamter() {

        Intent intent = new Intent(mContext, CategoryBasedAdsAuction.class);
        if (!binding.MaxPrice.getText().toString().isEmpty() && !binding.MinPrice.getText().toString().isEmpty()) {
            intent.putExtra(Const.MAX_ID, binding.MaxPrice.getText().toString());
            intent.putExtra(Const.MIN_ID, binding.MinPrice.getText().toString());
        }
        intent.putExtra(Const.GET_CAT_ID, id);
        intent.putExtra(Const.CAT_TITLE, catName);
        intent.putExtra(Const.DISTANCE_ID, distance);
        startActivity(intent);
        finish();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        return super.onOptionsItemSelected(item);
    }


    private void setUIAction() {
        layoutManager = new GridLayoutManager(this, 5);
        binding.recycleview1.setHasFixedSize(true);
        binding.recycleview1.setLayoutManager(layoutManager);
        filterAdapter = new FilterAdapter(FilterActivity.this, categoryDTOArrayList);
        binding.recycleview1.setAdapter(filterAdapter);


    }

    @Override
    protected void onResume() {
        getAllCategory();
        super.onResume();
    }

    private void getAllCategory() {

        new HttpsRequest(Const.GET_ALL_CATEGORY, mContext).stringGet(TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {
                ProjectUtils.pauseProgressDialog();

                if (flag) {
                    try {
                        binding.recycleview1.setVisibility(View.VISIBLE);
                        categoryDTOArrayList = new ArrayList<>();
                        Type getPetDTO = new TypeToken<List<CategoryDTO>>() {
                        }.getType();

                        categoryDTOArrayList = (ArrayList<CategoryDTO>) new Gson().fromJson(response.getJSONArray("data").toString(), getPetDTO);
                        setUIAction();

                    } catch (Exception e) {
                        e.printStackTrace();

                    }


                } else {
                    ProjectUtils.showToast(mContext, msg);
                }
            }
        });

    }


    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {


    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        mTrackingText.setText(getString(R.string.seekbar_tracking_on));

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        mTrackingText.setText(getString(R.string.seekbar_tracking_off));

    }

    public void getGetId(String catID) {
        id = catID;



    }

    public void getName(String cat_title) {
        catName = cat_title;
        binding.tvtitle3.setText(catName);
    }
}
