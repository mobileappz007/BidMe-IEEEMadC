package com.bidme.fragment.dashboad;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.bidme.R;
import com.bidme.activity.authentication.Sign_in;
import com.bidme.activity.aution.AutionAll;
import com.bidme.activity.aution.PostAuction;
import com.bidme.activity.categories.CategoryList;
import com.bidme.adapter.AdvertiseAdapter;
import com.bidme.adapter.BrowseAunctionAdapter;
import com.bidme.adapter.CategoryAdapter;
import com.bidme.adapter.FilterAunctionsAdapter;
import com.bidme.databinding.CommentBinding;
import com.bidme.databinding.DialogboxBinding;
import com.bidme.databinding.FragmentBrowseBinding;
import com.bidme.https.HttpsRequest;
import com.bidme.interfaces.Const;
import com.bidme.interfaces.Helper;
import com.bidme.model.AdvertiseAllDTO;
import com.bidme.model.AutionAllDTO;
import com.bidme.model.CategoryDTO;
import com.bidme.model.FilterDTO;
import com.bidme.model.UserDTO;
import com.bidme.network.NetworkManager;
import com.bidme.preferences.SharedPrefrence;
import com.bidme.utils.ProjectUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class Browse extends Fragment implements View.OnClickListener {

    private String TAG = Browse.class.getCanonicalName();
    private FragmentBrowseBinding binding;
    private ArrayList<AdvertiseAllDTO> advertiseDTOList;
    private ArrayList<AutionAllDTO> allDTOArrayList;
    private AdvertiseAdapter advertiseAdapterDashboard;
    private LinearLayoutManager linearLayoutManager;
    private LinearLayoutManager linearLayoutManager2;
    private LinearLayoutManager linearLayoutManager3;
    private BrowseAunctionAdapter autionAllAdapter;
    private CategoryAdapter categoryAdapter;
    private Dialog dialogbox_menu;

    private FilterAunctionsAdapter filterAdapter;
    private ArrayList<CategoryDTO> categoryDTOList;
    private ArrayList<FilterDTO> filterDTOArrayList;
    private HashMap<String, String> params = new HashMap<>();
    private SharedPrefrence prefrence;
    private UserDTO userDTO;
    AlertDialog alertDialog1;
    private String user_pub_id;
    android.support.v7.app.AlertDialog.Builder builder1 ;

    CharSequence[] values = {" Price(Low to high) ", " Price(high to Low) ", " Date (Ascending)", " Date (Descending)"};



    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_browse, container, false);
        prefrence = SharedPrefrence.getInstance(getActivity());

        userDTO = prefrence.getParentUser(Const.USER_DTO);
        user_pub_id=userDTO.getUser_pub_id();

        builder1=new android.support.v7.app.AlertDialog.Builder(getActivity());

        params.put(Const.USER_PUB_ID,user_pub_id);


        setUiAction();

        //timer();
        return binding.getRoot();
    }

    public void setUiAction() {
        binding.llDisc.setOnClickListener(this);
        binding.btnViewAllAuc.setOnClickListener(this);
        // binding.btnAdvertisements.setOnClickListener(this);
        binding.fabPostAuction.setOnClickListener(this);
        binding.filter.setOnClickListener(this);




        linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        //binding.rvAdveritsements.setLayoutManager(linearLayoutManager);

        linearLayoutManager2 = new GridLayoutManager(getActivity(), 2, LinearLayoutManager.VERTICAL, false);
        binding.rvAuction.setHasFixedSize(true);
        binding.rvAuction.setLayoutManager(linearLayoutManager2);


        linearLayoutManager3 = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        binding.rvChosse.setLayoutManager(linearLayoutManager3);
        if (NetworkManager.isConnectToInternet(getActivity())) {

            getAuctionList();

            //getAdvertiseList();
            getCategoryList();
        } else {
            ProjectUtils.InternetAlertDialog(getActivity());
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        getAuctionList();

    }


    private void alertGuestUser() {


        builder1.setMessage(getActivity().getResources().getString(R.string.guestMsg))
                .setCancelable(false)
                .setPositiveButton("Register", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Intent intent= new Intent(getActivity(), Sign_in.class);
                        getActivity().startActivity(intent);


                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //  Action for 'NO' Button
                        dialog.cancel();
                        //Toast.makeText(getApplicationContext(), "you clicked NO",
                        // Toast.LENGTH_SHORT).show();
                    }
                });

        android.support.v7.app.AlertDialog alert = builder1.create();

        alert.setTitle("GetRid");
        alert.setIcon(R.drawable.ic_logout1);
        alert.show();
    }
/*  public void getAdvertiseList() {
        new HttpsRequest(Const.GET_ALL_ADVERTISE, getActivity()).stringGet(TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {
                if (flag) {
                    ProjectUtils.showToast(getActivity(), msg);


                    try {
                        advertiseDTOList = new ArrayList<>();
                        Type getpetDTO = new TypeToken<List<AdvertiseAllDTO>>() {
                        }.getType();
                        advertiseDTOList = (ArrayList<AdvertiseAllDTO>) new Gson().fromJson(response.getJSONArray("data").toString(), getpetDTO);
                        advertiseAdapterDashboard = new AdvertiseAdapter(getActivity(), advertiseDTOList);
                       // binding.rvAdveritsements.setAdapter(advertiseAdapterDashboard);
                    } catch (JSONException e) {


                        e.printStackTrace();
                    }

                } else {
                    ProjectUtils.showToast(getActivity(), msg);
                }
            }
        });

    }*/

    public void getAuctionList() {
        new HttpsRequest(Const.GET_ALL_AUCTION, params, getActivity()).stringPost(TAG, (new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {

                if (flag) {
                    binding.filter.setVisibility(View.VISIBLE);
                    binding.filterAuc.setVisibility(View.VISIBLE);


                    /// ProjectUtils.showToast(getActivity(), msg);
                    try {
                        allDTOArrayList = new ArrayList<>();
                        Type getpetDTO = new TypeToken<List<AutionAllDTO>>() {
                        }.getType();
                        allDTOArrayList = (ArrayList<AutionAllDTO>) new Gson().fromJson(response.getJSONArray("data").toString(), getpetDTO);
                        autionAllAdapter = new BrowseAunctionAdapter(getActivity(), allDTOArrayList, "1",Browse.this);
                        binding.rvAuction.setAdapter(autionAllAdapter);
                        binding.ftvNoAuction.setVisibility(View.GONE);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    //ProjectUtils.showToast(getActivity(), msg);
                    binding.ftvNoAuction.setVisibility(View.VISIBLE);
                    binding.filter.setVisibility(View.GONE);
                    binding.filterAuc.setVisibility(View.GONE);


                }
            }
        }));

    }

    public void getCategoryList() {
        new HttpsRequest(Const.GET_ALL_CATEGORY, getActivity()).stringGet(TAG, (new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {

                if (flag) {
                    //ProjectUtils.showToast(getActivity(), msg);
                    try {
                        categoryDTOList = new ArrayList<>();
                        Type getpetDTO = new TypeToken<List<CategoryDTO>>() {

                        }.getType();
                        categoryDTOList = (ArrayList<CategoryDTO>) new Gson().fromJson(response.getJSONArray("data").toString(), getpetDTO);

                        categoryAdapter = new CategoryAdapter(getActivity(), categoryDTOList);
                        binding.rvChosse.setAdapter(categoryAdapter);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    ProjectUtils.showToast(getActivity(), msg);
                }
            }
        }));

    }




    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_ViewAllAuc:

                startActivity(new Intent(getActivity(), AutionAll.class));
                getActivity().overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_left);
                break;
            //  case R.id.btn_Advertisements:
            // startActivity(new Intent(getActivity(), AllAdvertise.class));
            //  getActivity().overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_left);
            //   break;
            case R.id.llDisc:
                startActivity(new Intent(getActivity(), CategoryList.class));
                getActivity().overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_left);
                break;


            case R.id.fab_PostAuction:


                if(user_pub_id.equals(Const.GUEST_USER_PUB_ID)){
                    alertGuestUser();
                }else{
                    startActivity(new Intent(getActivity(), PostAuction.class));
                    getActivity().overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_left);
                }


                break;
                case R.id.filter:
                    CreateAlertDialogWithRadioButtonGroup();
                    break;


        }
    }


    private void dialogbox_comment() {

        dialogbox_menu = new Dialog(getActivity()/*, android.R.style.Theme_Dialog*/);
        dialogbox_menu.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialogbox_menu.requestWindowFeature(Window.FEATURE_NO_TITLE);
       final DialogboxBinding binding = DataBindingUtil.inflate(LayoutInflater.from(dialogbox_menu.getContext()),R.layout.dialogbox,null,false);
        dialogbox_menu.setContentView(binding .getRoot());
        //   final CommentBinding binding=DataBindingUtil.setContentView(this,R.layout.comment);
        dialogbox_menu.show();
        dialogbox_menu.setCancelable(true);





        binding.ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogbox_menu.dismiss();
            }
        });


    }




    public void shortlistlowtohigh() {
        Collections.sort(allDTOArrayList, new Comparator<AutionAllDTO>() {

            public int compare(AutionAllDTO obj1, AutionAllDTO obj2) {
                float az = 0f;
                float za = 0f;
                if (obj1.getPrice().equals("") | obj2.getPrice().equals("")) {

                } else {
                    az = Float.parseFloat(obj1.getPrice());
                    za = Float.parseFloat(obj2.getPrice());
                }
                return (az < za) ? -1 : (az > za) ? 1 : 0;

            }
        });
    }

    public void shortlisthightolow() {
        Collections.sort(allDTOArrayList, new Comparator<AutionAllDTO>() {

            public int compare(AutionAllDTO obj1, AutionAllDTO obj2) {

                float az = 0f;
                float za = 0f;
                if (obj1.getPrice().equals("") | obj2.getPrice().equals("")) {

                } else {
                    az = Float.parseFloat(obj1.getPrice());
                    za = Float.parseFloat(obj2.getPrice());
                }
                return (az > za) ? -1 : (az < za) ? 1 : 0;

            }
        });
    }

    public void shortDatedescending() {
        Collections.sort(allDTOArrayList, new Comparator<AutionAllDTO>() {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

            @Override
            public int compare(AutionAllDTO ord1, AutionAllDTO ord2) {
                Date d1 = new Date();
                Date d2 = new Date();
                try {
                    d1 = sdf.parse(ord1.getS_date());
                    d2 = sdf.parse(ord2.getS_date());
                } catch (ParseException e) {
// TODO Auto-generated catch block
                    e.printStackTrace();
                }
                return (d1.getTime() > d2.getTime() ? -1 : 1); //descending
//return (d1.getTime() > d2.getTime() ? 1 : -1); //ascending
            }
        });


    }

    public void shortDateascending() {
        Collections.sort(allDTOArrayList, new Comparator<AutionAllDTO>() {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

            @Override
            public int compare(AutionAllDTO ord1, AutionAllDTO ord2) {
                Date d1 = new Date();
                Date d2 = new Date();
                try {

                    d1 = sdf.parse(ord1.getE_date());
                    d2 = sdf.parse(ord2.getE_date());

                } catch (ParseException e) {
// TODO Auto-generated catch block
                    e.printStackTrace();
                }
//return (d1.getTime() > d2.getTime() ? -1 : 1); //descending
                return (d1.getTime() > d2.getTime() ? 1 : -1); //ascending
            }
        });


    }


    public void CreateAlertDialogWithRadioButtonGroup() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setTitle("Sort by");


        builder.setSingleChoiceItems(values, -1, new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int item) {

                switch (item) {
                    case 0:
                        if (allDTOArrayList.size() > 0) {
                            shortlistlowtohigh();
                            autionAllAdapter.notifyDataSetChanged();
                        }
                        break;
                    case 1:
                        if (allDTOArrayList.size() > 0) {
                            shortlisthightolow();
                            autionAllAdapter.notifyDataSetChanged();
                        }
                        break;
                    case 2:
                        if (allDTOArrayList.size() > 0) {
                            shortDateascending();
                            autionAllAdapter.notifyDataSetChanged();
                        }
                        break;
                    case 3:
                        if (allDTOArrayList.size() > 0) {
                            shortDatedescending();
                            autionAllAdapter.notifyDataSetChanged();
                        }
                        break;
                }
                alertDialog1.dismiss();
            }
        });
        alertDialog1 = builder.create();
        alertDialog1.show();

    }



    public void timer(){
        final Handler handler = new Handler();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                getAuctionList();                // Toast.makeText(mContext,"ashu",Toast.LENGTH_SHORT).show();
                handler.postDelayed(this, 5000);
            }
        };
        handler.postDelayed(runnable, 5000);
    }


}