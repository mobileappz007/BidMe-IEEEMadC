package com.bidme.activity.aution;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.cocosw.bottomsheet.BottomSheet;
import com.darsh.multipleimageselect.activities.AlbumSelectActivity;
import com.darsh.multipleimageselect.helpers.Constants;
import com.darsh.multipleimageselect.models.Image;
import com.bidme.R;

import com.bidme.adapter.GalleryAdapter;
import com.bidme.databinding.ActivityGalleryBinding;
import com.bidme.https.HttpsRequest;
import com.bidme.interfaces.Const;
import com.bidme.interfaces.Helper;
import com.bidme.model.GalleryDTO;
import com.bidme.preferences.SharedPrefrence;
import com.bidme.utils.ImageCompression;
import com.bidme.utils.ProjectUtils;

import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class GalleryActivity extends AppCompatActivity {
    private Context mContext;
    private ActivityGalleryBinding binding;
    private ArrayList<GalleryDTO> galleryDTOArrayList = new ArrayList<>();
    private RecyclerView.LayoutManager layoutManager;
    private GalleryAdapter galleryAdapter;
    private String imageId = "";
    private String Pro_pub_id;
    private String User_pub_id;
    ArrayList<Image> images;

    private String flag = "";
    private GalleryDTO galleryDTO;
    File file;
    private Map<String, ArrayList<File>> paramsFile = new HashMap<>();

    BottomSheet.Builder builder;
    Uri picUri;
    int PICK_FROM_CAMERA = 1, PICK_FROM_GALLERY = 2;
    int CROP_CAMERA_IMAGE = 3, CROP_GALLERY_IMAGE = 4;
    int PICK_IMAGE_MULTIPLE = 5;
    String imageName;
    String pathOfImage;
    Bitmap bm;
    ImageCompression imageCompression;
    SharedPrefrence prefrence;

    private String TAG = GalleryActivity.class.getCanonicalName();

    private HashMap<String, String> params = new HashMap<>();

    private int imageSize=5;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = GalleryActivity.this;
        binding = DataBindingUtil.setContentView(this, R.layout.activity_gallery);
        if (getIntent().hasExtra(Const.DTO)) {
            galleryDTOArrayList = (ArrayList<GalleryDTO>) getIntent().getSerializableExtra(Const.DTO);
        }


        flag = getIntent().getStringExtra(Const.FLAG);

        if (flag.equals("2")) {
            binding.ivGalleryUpload.setVisibility(View.VISIBLE);

        } else {
            binding.ivGalleryUpload.setVisibility(View.GONE);
        }

        imageSize=imageSize-galleryDTOArrayList.size();
        Log.e(TAG, "imagesize................"+imageSize );

        Pro_pub_id = getIntent().getStringExtra(Const.Pro_pub_id);
        User_pub_id = getIntent().getStringExtra(Const.USER_PUB_ID);


        binding.ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        binding.ivGalleryUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (galleryDTOArrayList.size() < 5) {
                    multipleImages();
                } else {
                    Toast.makeText(mContext, "You can not select more images.", Toast.LENGTH_SHORT).show();
                }


            }
        });


        setUiAction();


    }


    private void setUiAction() {

        layoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false);
        binding.recycleview1.setHasFixedSize(true);
        binding.recycleview1.setLayoutManager(layoutManager);
        galleryAdapter = new GalleryAdapter(mContext, galleryDTOArrayList, flag, GalleryActivity.this);
        binding.recycleview1.setAdapter(galleryAdapter);


    }


    public void uploadImage() {
        params.put(Const.Pro_pub_id, Pro_pub_id);
        params.put(Const.USER_PUB_ID, User_pub_id);
        new HttpsRequest(Const.ADD_IMAGES, params, paramsFile, mContext, mContext).multiImagePost(TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {
                ProjectUtils.pauseProgressDialog();

                if (flag) {
                    try {
                       // ProjectUtils.showToast(mContext, msg);
                        finish();


                    } catch (Exception e) {
                        e.printStackTrace();

                    }


                } else {
                   // ProjectUtils.showToast(mContext, msg);

                }
            }
        });


    }


    private void multipleImages() {
        Intent intent = new Intent(this, AlbumSelectActivity.class);
//set limit on number of images that can be selected, default is 10
        intent.putExtra(Constants.INTENT_EXTRA_LIMIT, imageSize);
        startActivityForResult(intent, Constants.REQUEST_CODE);
    }


    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == Constants.REQUEST_CODE && resultCode == RESULT_OK && data != null) {

            images = data.getParcelableArrayListExtra(Constants.INTENT_EXTRA_IMAGES);

            Log.e("ImageData===>", "AA gya");

            final ArrayList<File> files = new ArrayList<>();
            for (int i = 0; i < images.size(); i++) {

                files.add(new File(images.get(i).path));

               /* imageCompression = new ImageCompression(mContext);
                imageCompression.execute(String.valueOf(images.get(i).path));
                imageCompression.setOnTaskFinishedEvent(new ImageCompression.AsyncResponse() {
                    @Override
                    public void processFinish(String imagePath) {

                    }
                });*/

            }

                paramsFile.put(Const.IMAGE, files);



            Log.e("ImageData===>", "List AA gyi");


            uploadImage();

        }
    }


}
